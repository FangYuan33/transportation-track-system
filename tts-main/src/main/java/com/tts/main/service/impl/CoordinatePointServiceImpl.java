package com.tts.main.service.impl;

import com.tts.main.service.CoordinatePointService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 坐标存储与查询相关的service实现
 *
 * @author FangYuan
 * @since 2022-12-22 17:32:06
 */
@SuppressWarnings({"ALL", "JdTransactionMustHaveRollback"})
@Slf4j
@Service
public class CoordinatePointServiceImpl implements CoordinatePointService {

    @Autowired
    private TtsSessionMapper ttsSessionMapper;

    /**
     * mybatis dao
     */
    @Autowired
    private TtsCoordinatePointMapper ttsCoordinatePointMapper;

    @Autowired
    private ConfigureService configureService;

    @Autowired
    private GeofenceMonitorService geofenceMonitorService;

    @Autowired
    private TtsCoordinateCalMapper ttsCoordinateCalMapper;

    @Autowired
    private TtsCoordinateLatestMapper ttsCoordinateLatestMapper;

    @Autowired
    private ITtsGlobalConfigureService ttsGlobalConfigureService;

    /**
     * 处理远程设备传递过来的坐标数据
     * 处理内容为将坐标保存到数据库
     * 同时将坐标放到 redis 的 队列中,方便后续处理
     *
     * @param appCoordinatePointDto
     * @return 如果这个步骤需要创建sessionId, 则返回sessionId, 否则返回空字符串
     */
    @Override
    public String saveCoordinatePointFlowRecord(AppCoordinatePointDto appCoordinatePointDto) {

        /***************
         *** 获取配置 ***
         **************/
        //获取租户id
        //租户id肯定不会为空,因为前置方法已经做判断,如果租户id为空则设置为 -1
        String tenantId = appCoordinatePointDto.getTenantId();

        //根据租户id获取其配置信息
        TtsConfigure ttsConfigure = configureService.getConfigureByTendIdWithRedis(Long.valueOf(tenantId));

        //如果获取的租户配置为空,则打印异常日志,记录坐标流水,但是忽略基于租户配置的其他操作,效果等同于租户配置中的所有开关均为关闭.
        if (ttsConfigure == null) {
            log.error("租户[" + tenantId + "]的配置信息为空,请检查!");
            ttsConfigure = new TtsConfigure();
        }

        //坐标精确度过滤标志,如果为 true 则表示合格点位
        boolean accurateFlag = true;

        //根据配置过滤坐标设置过滤标记
        if (ttsConfigure.isFilterSwitch()) {
            accurateFlag = filterCoordinate(appCoordinatePointDto, ttsConfigure);
        }
        //设置过滤标记到dto对象中
        appCoordinatePointDto.setAccurateFlag(accurateFlag);


        //创建session信息
        //这是一个特殊的坐标信息,里面包含该设备的初始化信息,即对应TtsSession的信息
        if (TtsConstant.SYS_ACTION_CREATE_SESSION.equals(appCoordinatePointDto.getAction())) {

            TtsSession ttsSession = ConvertTool.toTtsSession(appCoordinatePointDto);
            ttsSessionMapper.insert(ttsSession);
            Long sessionId = ttsSession.getSessionId();
            appCoordinatePointDto.setSessionId(String.valueOf(sessionId));
        }

        //将app传过来的 appCoordinatePointDto 进行转换并存储为 坐标数据到数据库
        List<TtsCoordinatePoint> ttsCoordinatePointList = ConvertTool.toTtsCoordinatePoint(appCoordinatePointDto);

        for (TtsCoordinatePoint ttsCoordinatePoint : ttsCoordinatePointList) {
            ttsCoordinatePointMapper.insert(ttsCoordinatePoint);
        }

        //计算新坐标的 geohash 值
        double longitude = new BigDecimal(appCoordinatePointDto.getLongitude()).doubleValue();
        double latitude = new BigDecimal(appCoordinatePointDto.getLatitude()).doubleValue();

        GeoHashBoxDto geoHashRectangle = GPSTools.generateGeoHash4Point(longitude, latitude);

        for (TtsCoordinatePoint ttsCoordinatePoint : ttsCoordinatePointList) {
            try {

                CoordinatePointDto4RedisLatest currentCoordinatePointDto4RedisLatest = ConvertTool.toCoordinatePointDto4RedisLatest(ttsCoordinatePoint);

                currentCoordinatePointDto4RedisLatest.setCreateTime(new Date());

                currentCoordinatePointDto4RedisLatest.setGeoHash(geoHashRectangle.getGeoHashBase32());

                //替换数据库中的坐标
                replaceCoordinateLatest(currentCoordinatePointDto4RedisLatest);

            } catch (Exception e) {
                log.error("dealCoordinateLatestPoint error!", e);
            }
        }

        //判断是否开启 redis 开关,如果redis 开关没有开启则不进行处理
        if (ttsConfigure.isRedisSwitch()) {
            //获取redis操作topic 的 operator
            RedisClientQueueOperator redisClientQueueOperator = RedisClient.getInstance().getQueueOperator();

            //将数据信息存入redis 队列 中,然后 redis listener 就可以监听消息进行针对坐标的计算处理
            for (TtsCoordinatePoint ttsCoordinatePoint : ttsCoordinatePointList) {

                //仅仅处理精确度符合要求的坐标
                if (ttsCoordinatePoint.isAccurateFlag()) {
                    CoordinatePointDto4RedisQueue coordinatePointDto4RedisQueue
                            = ConvertTool.toCoordinatePointDto4RedisQueue(ttsCoordinatePoint);
                    coordinatePointDto4RedisQueue.setGeoHash(geoHashRectangle.getGeoHashBase32());
                    //将坐标对象加入指定的 redis 队列中
                    redisClientQueueOperator.add(TtsConstant.REDIS_NAMESPACE, TtsConstant.REDIS_KEY_COORDINATE_QUEUE, coordinatePointDto4RedisQueue);
                    //设置有效期
                    redisClientQueueOperator.expire(TtsConstant.REDIS_NAMESPACE, TtsConstant.REDIS_KEY_COORDINATE_QUEUE, ttsConfigure.getRedisExpiryHour(), TimeUnit.HOURS);
                }
            }
        }


        return appCoordinatePointDto.getSessionId();

    }


    /**
     * 根据打点类型的坐标经度过滤坐标
     * 如果没有打点类型或者坐标经度 则默认 accurateFlag 为true
     *
     * @param appCoordinatePointDto
     * @param ttsConfigure
     * @return
     */
    private boolean filterCoordinate(AppCoordinatePointDto appCoordinatePointDto, TtsConfigure ttsConfigure) {

        boolean accurateFlag = true;

        //如果是正常点才进行处理,并且不是 判定为 误差多大的点才进行处理
        if (!TtsConstant.NORMAL_POINT_ERROR_CODE.equals(appCoordinatePointDto.getErrorCode())) {
            accurateFlag = false;
        }

        //根据打点类型进行过滤
        //对于配置信息里面的打点类型不予处理
        //如果为空则表示对所有打点类型都正常处理
        String locationTypeFilter = ttsConfigure.getLocationTypeFilter();
        if (accurateFlag
                && StringUtils.isNotEmpty(appCoordinatePointDto.getLocationType())
                && StringUtils.isNotEmpty(locationTypeFilter)) {
            if (locationTypeFilter.contains(appCoordinatePointDto.getLocationType().toUpperCase())) {
                accurateFlag = false;
            }
        }

        //进行打点精确度的过滤
        //如果当前 accurateFlag 已经是false ,则已经表示不需要进行该判断了
        if (accurateFlag) {
            double accuracyFilterThreshold4Config = ttsConfigure.getAccuracyFilterThreshold();
            String accuracyStr = appCoordinatePointDto.getAccuracy();

            Double accuracyDouble = 0.0;

            if (StringUtils.isNotEmpty(accuracyStr)) {
                accuracyDouble = Double.parseDouble(accuracyStr);
            }
            //如果坐标上的精确度数据大于配置中的值,则设置为精确度不符合要求
            if (accuracyDouble > accuracyFilterThreshold4Config) {
                accurateFlag = false;
            }
        }
        return accurateFlag;
    }


    /**
     * queryType = 1: 查询给定车辆列表最新位置
     * 查询条件:
     * 车辆id列表
     * 返回结果:
     * 返回 Map<VehicleId,TtsCoordinatePoint>
     *
     * @param coordinateQueryDto
     * @return
     */
    @Override
    public Map<Long, TtsCoordinateLatest> queryCoordinateMapInner(CoordinateQueryDto coordinateQueryDto) {

        List<Long> vehicleIdList = coordinateQueryDto.getVehicleIdList();

        Map<Long, TtsCoordinateLatest> queryResultMap = new HashMap();


        //根据租户id获取其配置信息
        TtsConfigure ttsConfigure = configureService.getConfigureByTendIdWithRedis(Long.valueOf(coordinateQueryDto.getTenantId()));

        //如果获取的租户配置为空,则打印异常日志,记录坐标流水,但是忽略基于租户配置的其他操作,效果等同于租户配置中的所有开关均为关闭.
        if (ttsConfigure == null) {
            ttsConfigure = new TtsConfigure();
        }

        for (Long vehicleId : vehicleIdList) {

            TtsCoordinateLatest queryDto = new TtsCoordinateLatest();
            queryDto.setTenantId(coordinateQueryDto.getTenantId());
            queryDto.setVehicleId(vehicleId);

            // 拼接 运算用坐标在redis中的 key
            // 前缀为 REDIS_KEY_COORDINATE_LATEST_PRE-{租户id}-{车辆id}, 如果运单id为 -1 ,则表示当前车辆没有运单id
            String redisKey = TtsConstant.REDIS_KEY_COORDINATE_LATEST_PRE
                    + queryDto.getTenantId()
                    + TtsConstant.REDIS_KEY_SPLIT + queryDto.getVehicleId();

            //查询redis 是否有这个值
            CoordinatePointDto4RedisLatest coordinatePointDto4RedisLatest = null;

            RedisClientObjectOperator<CoordinatePointDto4RedisLatest> ttsCoordinateLatestRedisOperator = null;


            if (ttsConfigure.isRedisSwitch()) {
                ttsCoordinateLatestRedisOperator = RedisClient.getInstance().getObjectOperator();
                coordinatePointDto4RedisLatest = ttsCoordinateLatestRedisOperator.get(TtsConstant.REDIS_NAMESPACE, redisKey);
            }

            //如果redis 中没有,则查询数据库,同时将查询结果放入redis
            if (coordinatePointDto4RedisLatest == null) {
                TtsCoordinateLatest ttsCoordinatePoitTemp = ttsCoordinateLatestMapper.queryLatestPointByTenantIdAndVehicleId(queryDto);

                if (ttsCoordinatePoitTemp != null) {

                    coordinatePointDto4RedisLatest = ConvertTool.toCoordinatePointDto4RedisLatest(ttsCoordinatePoitTemp);

                    if (ttsConfigure.isRedisSwitch()) {
                        ttsCoordinateLatestRedisOperator.set(TtsConstant.REDIS_NAMESPACE, redisKey, coordinatePointDto4RedisLatest, ttsConfigure.getRedisExpiryHour(), TimeUnit.HOURS);
                    }
                }
            }

            if (coordinatePointDto4RedisLatest != null) {
                TtsCoordinateLatest ttsCoordinatePoit = ConvertTool.toTtsCoordinateLatest(coordinatePointDto4RedisLatest);
                queryResultMap.put(vehicleId, ttsCoordinatePoit);
            }
        }

        return queryResultMap;
    }


    /**
     * queryType = 1: 查询给定车辆列表最新位置
     * 查询条件:
     * 车辆id列表
     * 返回结果:
     * 返回 Map<VehicleId,CoordinateQueryResultDto>
     *
     * @param coordinateQueryDto 查询条件
     * @return 车辆id 和坐标的映射列表
     */
    @Override
    public Map<Long, CoordinatePointResultDto> queryCoordinateMap(CoordinateQueryDto coordinateQueryDto) {

        try {
            long startTime = System.currentTimeMillis();

            log.info("queryCoordinateMap: [" + coordinateQueryDto.toString() + "]");

            //2.校验请求参数的完整性

            //如果没有传租户id 则默认为 -1 ,一般用于本地化系统
            if (coordinateQueryDto.getTenantId() == null) {
                coordinateQueryDto.setTenantId(Long.valueOf(TtsConstant.DEFAULT_NUM_VALUE));
            }


            Map<Long, TtsCoordinateLatest> coordinatePointMap = this.queryCoordinateMapInner(coordinateQueryDto);

            Map<Long, CoordinatePointResultDto> resultMap = new HashMap();


            for (Map.Entry<Long, TtsCoordinateLatest> entry : coordinatePointMap.entrySet()) {
                resultMap.put(entry.getKey(), ConvertTool.toCoordinatePointResultDto(entry.getValue()));
            }

            long consumeTime = System.currentTimeMillis() - startTime;
            log.info("response(" + consumeTime + " ms): [" + resultMap.size() + "]");

            return resultMap;
        } catch (Exception e) {
            log.error("queryCoordinateMap error", e);
            throw new BusinessException("queryCoordinateMap 调用异常!");
        }
    }


    /**
     * queryType = 2: 查询给定车辆列表 在时间范围内的轨迹
     * 查询条件:
     * 车辆id列表: 给定车辆id列表
     * startTime: 查询 >= startTime范围内的数据,startTime不能为空
     * endTime :  查询 <= endTime 范围内的数据,endTime可以为空,表示截止到当前时间
     * 返回结果:
     * 返回 List<TtsCoordinatePoint> 即属于指定车辆的,在时间范围内的坐标数据,按照时间正向排序生成一个list返回
     *
     * @param coordinateQueryDto
     * @return
     */
    @Override
    public List<TtsCoordinatePoint> queryCoordinateListInner(CoordinateQueryDto coordinateQueryDto) {

        List<Long> vehicleIdList = coordinateQueryDto.getVehicleIdList();

        List<TtsCoordinatePoint> queryResultList = new LinkedList<>();

        Date startTime = coordinateQueryDto.getStartTime();
        Date endTime = coordinateQueryDto.getEndTime();

        Long shipmentId = coordinateQueryDto.getShipmentId();

        //如果 endTime 为空则为当前最新时间
        if (endTime == null) {
            endTime = new Date();
        }

        for (Long vehicleId : vehicleIdList) {

            TrackQueryDto trackQueryDTO = new TrackQueryDto();

            trackQueryDTO.setTenantId(coordinateQueryDto.getTenantId());
            trackQueryDTO.setVehicleId(vehicleId);
            trackQueryDTO.setShipmentId(shipmentId);
            trackQueryDTO.setAccurateFlag(true);
            trackQueryDTO.setStartTime(startTime);
            trackQueryDTO.setEndTime(endTime);

            List<TtsCoordinatePoint> coordinatePointList = ttsCoordinatePointMapper.queryTrackByVehicleId(trackQueryDTO);

            queryResultList.addAll(coordinatePointList);
        }

        return queryResultList;
    }


    /**
     * queryType = 2: 查询给定车辆列表 在时间范围内的轨迹
     * 查询条件:
     * 车辆id列表: 给定车辆id列表
     * 装车单id
     * startTime: 查询 >= startTime范围内的数据,startTime不能为空
     * endTime :  查询 <= endTime 范围内的数据,endTime可以为空,表示截止到当前时间
     * 返回结果:
     * 返回 List<CoordinateQueryResultDto> 即属于指定车辆的,在时间范围内的坐标数据,按照时间正向排序生成一个list返回
     *
     * @param coordinateQueryDto 查询条件
     * @return 车辆id 和坐标的映射列表
     */
    @Override
    public List<CoordinatePointResultDto> queryCoordinateList(CoordinateQueryDto coordinateQueryDto) {

        try {
            long startTime = System.currentTimeMillis();

            log.info("queryCoordinateList: [" + coordinateQueryDto.toString() + "]");

            //2.校验请求参数的完整性

            //如果没有传租户id 则默认为 -1 ,一般用于本地化系统
            if (coordinateQueryDto.getTenantId() == null) {
                coordinateQueryDto.setTenantId(Long.valueOf(TtsConstant.DEFAULT_NUM_VALUE));
            }

            List<TtsCoordinatePoint> ttsCoordinatePointList = this.queryCoordinateListInner(coordinateQueryDto);

            List<CoordinatePointResultDto> resultList = new ArrayList<>();

            for (TtsCoordinatePoint ttsCoordinatePoint : ttsCoordinatePointList) {
                resultList.add(ConvertTool.toCoordinatePointQueryDto(ttsCoordinatePoint));
            }

            long consumeTime = System.currentTimeMillis() - startTime;
            log.info("response(" + consumeTime + " ms): [" + resultList.size() + "]");

            return resultList;
        } catch (Exception e) {
            log.error("queryCoordinateList error", e);
            throw new BusinessException("queryCoordinateList 调用异常!");
        }
    }

    /**
     * 处理从 redis 队列中得到的坐标
     * 如果 GlobalRedisSwitch 为空则该方法不会执行
     *
     * @param coordinatePointDto4RedisQueue
     */
    @Override
    public void dealCoordinateFromRedisQueue(CoordinatePointDto4RedisQueue coordinatePointDto4RedisQueue) {

        //获取租户id
        //租户id肯定不会为空,因为前置方法已经做判断,如果租户id为空则设置为 -1
        Long tenantId = coordinatePointDto4RedisQueue.getTenantId();

        //根据租户id获取其配置信息
        TtsConfigure ttsConfigure = configureService.getConfigureByTendIdWithRedis(tenantId);

        //如果获取的租户配置为空,则打印异常日志,记录坐标流水,但是忽略基于租户配置的其他操作,效果等同于租户配置中的所有开关均为关闭.
        if (ttsConfigure == null) {
            log.error("租户[" + tenantId + "]的配置信息为空,请检查!");
            ttsConfigure = new TtsConfigure();
        }

        try {
            dealCoordinateLatestPoint(coordinatePointDto4RedisQueue, ttsConfigure);
        } catch (Exception e) {
            log.error("dealCoordinateLatestPoint error!", e);
        }

        try {
            dealCoordinateTrace(coordinatePointDto4RedisQueue, ttsConfigure);
        } catch (Exception e) {
            log.error("dealCoordinateTrace error!", e);
        }


    }

    /**
     * 处理从 redis  队列中获取到的坐标,信息,对其进行进一步坐标轨迹相关的运算处理
     *
     * @param appCoordinatePointDto
     * @return 如果这个步骤需要创建sessionId, 则返回sessionId, 否则返回空字符串
     */
    @Override
    public void dealCoordinateTrace(CoordinatePointDto4RedisQueue coordinatePointDto4RedisQueue, TtsConfigure ttsConfigure) {
        // 拼接 运算用坐标在redis中的 key
        // 前缀为 REDIS_KEY_COORDINATE_PRE-{租户id}-{车辆id}-{运单id}, 如果运单id为 -1 ,则表示当前车辆没有运单id
        //注意,我们的 里程运算和车速运算只针对有运单id的车辆信息进行运算
        //所以不会对装车单id为-1 的 坐标进行运算

        //仅仅对有装车单信息的坐标进行轨迹计算
        if (coordinatePointDto4RedisQueue.getShipmentId() == -1) {
            return;
        }

        String redisKey = TtsConstant.REDIS_KEY_COORDINATE_CAL_PRE
                + coordinatePointDto4RedisQueue.getTenantId()
                + TtsConstant.REDIS_KEY_SPLIT + coordinatePointDto4RedisQueue.getVehicleId()
                + TtsConstant.REDIS_KEY_SPLIT + coordinatePointDto4RedisQueue.getShipmentId();


        RedisClientObjectOperator<CoordinatePointDto4RedisCal> CoordinatePointDto4RedisCalRedisOperator
                = RedisClient.getInstance().getObjectOperator();

        //获取这个车辆目前在redis中缓存的运算用坐标值,即上一个坐标值
        CoordinatePointDto4RedisCal previousCoordinatePointDto4RedisCal
                = CoordinatePointDto4RedisCalRedisOperator.get(TtsConstant.REDIS_NAMESPACE, redisKey);

        //根据 coordinatePointDto4RedisQueue 生成这个记录对应的 CoordinatePointDto4RedisCal 对象
        CoordinatePointDto4RedisCal currentCoordinatePointDto4RedisCal = ConvertTool.toCoordinatePointDto4RedisCal(coordinatePointDto4RedisQueue);

        currentCoordinatePointDto4RedisCal.setCal(false);

        //进行坐标实时里程运算
        if (ttsConfigure.isMileageComputeSwitch()) {

            if (previousCoordinatePointDto4RedisCal != null) {

                //上一个坐标点的时间
                long prevTime = previousCoordinatePointDto4RedisCal.getReceiverServerTime().getTime();
                //下一个坐标点的时间
                long currentTime = currentCoordinatePointDto4RedisCal.getReceiverServerTime().getTime();

                long currentDuration = (currentTime - prevTime) / 1000;


                if (currentDuration > 0) {
                    //说明  currentCoordinatePointDto 在 previousCoordinatePointDto4RedisCal 后面
                    double currentDistance = GPSTools.calculateDistance4Gcj02(previousCoordinatePointDto4RedisCal, currentCoordinatePointDto4RedisCal);

                    currentCoordinatePointDto4RedisCal.setCurrentDistance(currentDistance);
                    currentCoordinatePointDto4RedisCal.setCurrentDuration(currentDuration);

                    currentCoordinatePointDto4RedisCal.setWholeDuration(ConvertTool.toBigDecimal(previousCoordinatePointDto4RedisCal.getWholeDuration()).add(ConvertTool.toBigDecimal(currentDuration)).longValue());
                    currentCoordinatePointDto4RedisCal.setWholeDistance(ConvertTool.toBigDecimal(previousCoordinatePointDto4RedisCal.getWholeDistance()).add(ConvertTool.toBigDecimal(currentDistance)).doubleValue());
                    currentCoordinatePointDto4RedisCal.setCal(true);
                }


                if (currentCoordinatePointDto4RedisCal.getCurrentDuration() > 0) {

                    currentCoordinatePointDto4RedisCal.setCurrentSpeed(ConvertTool.toBigDecimal(currentCoordinatePointDto4RedisCal.getCurrentDistance()).divide(ConvertTool.toBigDecimal(currentCoordinatePointDto4RedisCal.getCurrentDuration()).divide(ConvertTool.toBigDecimal(1000), TtsConstant.DIVEIDE_SCALE, TtsConstant.DIVEIDE_ROUNDINGMODE), TtsConstant.DIVEIDE_SCALE, TtsConstant.DIVEIDE_ROUNDINGMODE).doubleValue());
                    currentCoordinatePointDto4RedisCal.setWholeSpeed(ConvertTool.toBigDecimal(currentCoordinatePointDto4RedisCal.getWholeDistance()).divide(ConvertTool.toBigDecimal(currentCoordinatePointDto4RedisCal.getWholeDuration()).divide(new BigDecimal(1000), TtsConstant.DIVEIDE_SCALE, TtsConstant.DIVEIDE_ROUNDINGMODE), TtsConstant.DIVEIDE_SCALE, TtsConstant.DIVEIDE_ROUNDINGMODE).doubleValue());
                } else {
                    currentCoordinatePointDto4RedisCal.setCurrentSpeed(0);
                    currentCoordinatePointDto4RedisCal.setWholeSpeed(previousCoordinatePointDto4RedisCal.getWholeSpeed());
                }
            }
            //第一个坐标点
            else {
                currentCoordinatePointDto4RedisCal.setCurrentDuration(0);
                currentCoordinatePointDto4RedisCal.setCurrentDistance(0);
                currentCoordinatePointDto4RedisCal.setCurrentSpeed(0);

                currentCoordinatePointDto4RedisCal.setWholeDuration(0);
                currentCoordinatePointDto4RedisCal.setWholeDistance(0);
                currentCoordinatePointDto4RedisCal.setWholeSpeed(0);

                currentCoordinatePointDto4RedisCal.setCal(true);
            }
        }

        //缓存当前坐标信息到redis数据库
        CoordinatePointDto4RedisCalRedisOperator.set(TtsConstant.REDIS_NAMESPACE, redisKey, currentCoordinatePointDto4RedisCal, ttsConfigure.getRedisExpiryHour(), TimeUnit.HOURS);


        //将计算结果插入数据库
        TtsCoordinateCal ttsCoordinateCal = ConvertTool.toTtsCoordinateCal(currentCoordinatePointDto4RedisCal);
        ttsCoordinateCal.setCalTime(new Date());


        this.ttsCoordinateCalMapper.insert(ttsCoordinateCal);
    }


    /**
     * 处理从 redis  队列中获取到的坐标,信息,对其进行进一步坐标点相关的运算处理
     *
     * @param coordinatePointDto4RedisQueue
     */
    @Override
    public void dealCoordinateLatestPoint(CoordinatePointDto4RedisQueue coordinatePointDto4RedisQueue, TtsConfigure ttsConfigure) {

        // 拼接 运算用坐标在redis中的 key
        // 前缀为 REDIS_KEY_COORDINATE_LATEST_PRE-{租户id}-{车辆id}, 如果运单id为 -1 ,则表示当前车辆没有运单id
        String redisKey = TtsConstant.REDIS_KEY_COORDINATE_LATEST_PRE
                + coordinatePointDto4RedisQueue.getTenantId()
                + TtsConstant.REDIS_KEY_SPLIT + coordinatePointDto4RedisQueue.getVehicleId();

        RedisClientObjectOperator<CoordinatePointDto4RedisLatest> lastestPointRedisOperator = RedisClient.getInstance().getObjectOperator();

        CoordinatePointDto4RedisLatest currentCoordinatePointDto4RedisLatest = ConvertTool.toCoordinatePointDto4RedisLatest(coordinatePointDto4RedisQueue);

        //从 redis 中获取 上一次坐标信息
        CoordinatePointDto4RedisLatest preCoordinatePointDto4RedisLatest = lastestPointRedisOperator.get(TtsConstant.REDIS_NAMESPACE, redisKey);

        //根据时间判断这个坐标是不是最新的坐标,如果不是则忽略
        if (preCoordinatePointDto4RedisLatest != null) {
            if (preCoordinatePointDto4RedisLatest.getReceiverServerTime().getTime() > currentCoordinatePointDto4RedisLatest.getReceiverServerTime().getTime()) {
                log.error("dealCoordinateLatestPoint 接收坐标顺序错误:point1:[" + preCoordinatePointDto4RedisLatest.toString() + "],point2:[" + currentCoordinatePointDto4RedisLatest.toString() + "]");
                return;
            }
        }

        //处理坐标电子围栏的自动监控
        this.geofenceMonitorService.dealGeofenceMonitor(coordinatePointDto4RedisQueue.getVehicleId(), preCoordinatePointDto4RedisLatest, currentCoordinatePointDto4RedisLatest, ttsConfigure);

        //更新redis
        lastestPointRedisOperator.set(TtsConstant.REDIS_NAMESPACE, redisKey, currentCoordinatePointDto4RedisLatest, ttsConfigure.getRedisExpiryHour(), TimeUnit.HOURS);

    }

    /**
     * 更新数据库中的值,即按照 tenantId+vehicleId 更新记录,删除已有记录,插入新记录
     * 即保证 tenantId+vehicleId 只有一个记录
     *
     * @param currentCoordinatePointDto4RedisLatest
     */
    @Transactional
    public void replaceCoordinateLatest(CoordinatePointDto4RedisLatest currentCoordinatePointDto4RedisLatest) {

        TtsCoordinateLatest ttsCoordinateLatest = ConvertTool.toTtsCoordinateLatest(currentCoordinatePointDto4RedisLatest);

        ttsCoordinateLatest.setCreateTime(new Date());
        TtsCoordinateLatest ttsCoordinateLatest1 = this.ttsCoordinateLatestMapper.queryLatestPointByTenantIdAndVehicleId(ttsCoordinateLatest);
        if (null != ttsCoordinateLatest1) {
            ttsCoordinateLatestMapper.updateLatestByVehicleId(ttsCoordinateLatest);
        } else {
            ttsCoordinateLatestMapper.saveTtsCoordinateLatest(ttsCoordinateLatest);
        }


    }
}
