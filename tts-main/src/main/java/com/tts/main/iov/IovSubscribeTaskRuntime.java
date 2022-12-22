package com.tts.main.iov;

import com.google.gson.Gson;
import com.tts.base.constant.IovTaskStateEnum;
import com.tts.base.constant.TtsConstant;
import com.tts.framework.common.constant.IovTaskEventTypeEnum;
import com.tts.framework.common.constant.StatusEnum;
import com.tts.iov.dto.IovVehicleInputDto;
import com.tts.iov.dto.IovVehiclePointResultDto;
import com.tts.iov.facade.IovFacade;
import com.tts.iov.factory.IovFactory;
import com.tts.main.domain.TtsIovConfig;
import com.tts.main.domain.TtsIovSubscribeTask;
import com.tts.main.domain.TtsIovSubscribeTaskLog;
import com.tts.main.domain.TtsIovSubscribeTaskVehicle;
import com.tts.main.service.CoordinatePointService;
import com.tts.main.service.TtsIovSubscribeTaskService;
import com.tts.main.utils.ConvertTool;
import com.tts.remote.app.dto.AppCoordinatePointDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * iov 订阅  任务 运行时 ,对应 TtsIovSubscribeTask
 *
 * @author FangYuan
 * @since 2022-12-22 17:32:06
 */
public class IovSubscribeTaskRuntime {

    private static final Logger log = LoggerFactory.getLogger(IovSubscribeTaskRuntime.class);

    /**
     * 目前进程中所有运行的任务实例
     * 其中key 为 TtsIovSubscribeTask 的 id;
     */
    private static Map<Long, IovSubscribeTaskRuntime> taskRuntimeMap = new HashMap<>();

    /**
     * 周期性运行的线程
     */
    private Thread innerThread;

    /**
     * 周期性线程运行的标志位 ,
     * 如果为 true 表示运行,
     * 如果为 false 表示停止运行
     */
    private volatile boolean flag = false;

    private TtsIovSubscribeTask ttsIovSubscribeTask;
    private TtsIovConfig ttsIovConfig;

    private TtsIovSubscribeTaskService ttsIovSubscribeTaskService;
    private CoordinatePointService coordinatePointService;

    public IovSubscribeTaskRuntime(TtsIovConfig ttsIovConfig, TtsIovSubscribeTask ttsIovSubscribeTask, TtsIovSubscribeTaskService ttsIovSubscribeTaskService, CoordinatePointService coordinatePointService) {
        this.ttsIovConfig = ttsIovConfig;
        this.ttsIovSubscribeTask = ttsIovSubscribeTask;
        this.ttsIovSubscribeTaskService = ttsIovSubscribeTaskService;
        this.coordinatePointService = coordinatePointService;
    }


    /**
     * 启动iov 抓取数据的任务
     *
     * @param ttsIovConfig
     * @param ttsIovSubscribeTask
     * @param ttsIovSubscribeTaskService
     */
    public static synchronized void start(TtsIovConfig ttsIovConfig, TtsIovSubscribeTask ttsIovSubscribeTask, TtsIovSubscribeTaskService ttsIovSubscribeTaskService, CoordinatePointService coordinatePointService) {

        IovSubscribeTaskRuntime iovSubscribeTaskRuntime = taskRuntimeMap.get(ttsIovSubscribeTask.getId());

        //如果 taskRuntimeMap 中已经有相关的任务,则需要先停止老任务 再启动
        if (iovSubscribeTaskRuntime != null) {
            iovSubscribeTaskRuntime.close();
        }

        iovSubscribeTaskRuntime = new IovSubscribeTaskRuntime(ttsIovConfig, ttsIovSubscribeTask, ttsIovSubscribeTaskService, coordinatePointService);
        taskRuntimeMap.put(ttsIovSubscribeTask.getId(), iovSubscribeTaskRuntime);

        iovSubscribeTaskRuntime.startInner();

    }

    /**
     * iov 数据抓取任务启动
     */
    private void startInner() {


        //获取 轮询间隔时间, 最小 为 10秒
        Long interval = ttsIovConfig.getSubscribeInterval();

        if (interval == null || interval < TtsConstant.PERIODIC_INTERVAL_TIME) {
            interval = TtsConstant.PERIODIC_INTERVAL_TIME;
        }

        final Long finalInterval = interval;

        //创建线程
        innerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //当flag 为 true 时运行
                //当flag 为false 时 退出
                while (flag) {

                    //判断任务是不是应该继续正常运行,例如用户手工关闭任务了
                    //如果不应该运行则退出循环关闭任务
                    boolean isRunning = judgeTask();

                    //如果不是运行中则退出循环
                    if (!isRunning) {
                        break;
                    }

                    //进行数据抓取操作
                    try {
                        fetchIovDate();
                    } catch (Exception e) {
                        log.error("fetchIovDate[taskId:" + ttsIovSubscribeTask.getId() + "]  异常 !!! ", e);
                    }

                    //休息配置设置的时间
                    try {
                        Thread.sleep(finalInterval);
                    } catch (InterruptedException e) {
                        log.error("IovSubscribeTaskRuntime[taskId:" + ttsIovSubscribeTask.getId() + ",interval:" + finalInterval + "]     空闲等待 异常 !!! ", e);
                    }

                }

                //执行任务的退出机制
                closeInner();
            }
        });


        //启动线程
        flag = true;
        innerThread.setDaemon(true);
        innerThread.setName("iovTaskId-" + ttsIovSubscribeTask.getId() + "-thread");
        innerThread.start();
    }


    /**
     * 判断任务是否应该正常运行中
     *
     * @return
     */
    private boolean judgeTask() {
        boolean isRunning = false;

        //获取任务的最新状态是不是 RUNNING ,如果不是则 isRunning 为 false
        TtsIovSubscribeTask ttsIovSubscribeTaskNew = this.ttsIovSubscribeTaskService.getIovSubscribeTaskById(ttsIovSubscribeTask.getId());

        if (ttsIovSubscribeTaskNew != null && IovTaskStateEnum.RUNNING.getValue().equals(ttsIovSubscribeTaskNew.getState())) {
            isRunning = true;
        }
        //获取任务的最新手动操作日志是不是 "停止任务" 如果是的话则停止任务
        //这里这么设计是为了防止并发问题
        //比如 当前任务 所在节点心跳超时 , 然后 leader 节点判断了出来 ,正要将这个任务做恢复
        // 同时 用户 又点击了关闭这个任务
        //此时会有个情况,就是  leader 节点先查询出了这个异常的任务 ,然后同时 用户点击关闭,将任务状态设置为 stopping ,然后 leader 节点恢复线程由将其设置为 ALLOCATING
        //导致 用户认为 手动关闭的任务又自动启动了这种问题出现
        //所以需要检查用户最新的手动操作记录,如果是关闭任务的话则停止任务.
        if (isRunning) {
            //查询该任务的手工操作的最新记录
            TtsIovSubscribeTaskLog taskLog = ttsIovSubscribeTaskService.queryIovSubscribeTaskLogLatestManual(this.ttsIovSubscribeTask.getId());

            //判断最新的手动操作记录是不是 停止任务 ,如果 是的话设置任务 为 false
            if (taskLog != null && "停止任务".equals(taskLog.getContent())) {
                isRunning = false;
            }
        }


        return isRunning;

    }

    /**
     * 抓取IOV数据的核心逻辑
     */
    private void fetchIovDate() throws Exception {

        Date currentDate = new Date();

        //请求失败
        Integer status = IovTaskEventTypeEnum.NORMAL.getValue();
        IovVehicleInputDto iovVehicleInputDto = new IovVehicleInputDto();
        List<IovVehiclePointResultDto> iovVehiclePointResultDtoList = null;

        try {
            //获取要抓取的车辆信息列表
            List<TtsIovSubscribeTaskVehicle> vehicleList = ttsIovSubscribeTaskService.queryIovSubscribeTaskVehicle(this.ttsIovSubscribeTask.getId());

            if (vehicleList.size() > 0) {

                Gson gson = new Gson();
                Properties properties = gson.fromJson(ttsIovConfig.getConfig(), Properties.class);

                //调用对应的iov客户端获取数据
                IovFacade iovFacade = IovFactory.createIovFacade(ttsIovConfig.getIovType(), properties);

                Map<String, TtsIovSubscribeTaskVehicle> vehicleMap = new HashMap<>();

                List<String> vehicleNoList = new ArrayList<>();
                List<Integer> vehicleNoColorList = new ArrayList<>();

                for (TtsIovSubscribeTaskVehicle ttsIovSubscribeTaskVehicle : vehicleList) {
                    vehicleNoList.add(ttsIovSubscribeTaskVehicle.getVehicleNo());
                    vehicleNoColorList.add(ttsIovSubscribeTaskVehicle.getVehicleColorType());
                    vehicleMap.put(ttsIovSubscribeTaskVehicle.getVehicleNo(), ttsIovSubscribeTaskVehicle);
                }

                iovVehicleInputDto.setVehicleNoList(vehicleNoList);
                iovVehicleInputDto.setVehicleNoColorList(vehicleNoColorList);
                iovVehicleInputDto.setTimeNearBy(this.ttsIovConfig.getTimeNearBy());

                iovVehiclePointResultDtoList = iovFacade.queryVehicleLastLocation(iovVehicleInputDto);


                //调用 tts 的坐标处理接口处理获取到坐标数据
                for (IovVehiclePointResultDto iovVehiclePointResultDto : iovVehiclePointResultDtoList) {

                    TtsIovSubscribeTaskVehicle ttsIovSubscribeTaskVehicle = vehicleMap.get(iovVehiclePointResultDto.getVehicleNo());

                    AppCoordinatePointDto appCoordinatePointDto = ConvertTool.toAppCoordinatePointDto(iovVehiclePointResultDto, ttsIovSubscribeTaskVehicle, this.ttsIovConfig.getIovType());
                    appCoordinatePointDto.setUploadTime(String.valueOf(currentDate.getTime()));
                    coordinatePointService.saveCoordinatePointFlowRecord(appCoordinatePointDto);
                }
            }


        } catch (Exception e) {
            log.error("iov task [" + this.ttsIovSubscribeTask.getId() + "] fetchIovDate error", e);
            status = IovTaskEventTypeEnum.ABNORMAL.getValue();
        }

        boolean isDebug = ttsIovConfig.getLogFlag().equals(StatusEnum.ENABLE.getValue());
        long timeConsuming = System.currentTimeMillis() - currentDate.getTime();
        //记录调用日志
        ttsIovSubscribeTaskService.appendIovLog(ttsIovSubscribeTask, iovVehicleInputDto, iovVehiclePointResultDtoList, status, timeConsuming, isDebug);

    }


    public static Map<Long, IovSubscribeTaskRuntime> getTaskRuntimeMap() {
        return taskRuntimeMap;
    }

    /**
     * 关闭运行中的任务
     */
    private void close() {
        flag = false;

    }


    /**
     * 关闭运行中的任务
     */
    private void closeInner() {

        //移除缓存中的句柄
        taskRuntimeMap.remove(this.ttsIovSubscribeTask.getId());

        //任务状态变更为任务已停止
        this.ttsIovSubscribeTaskService.stoppedSubscribeIovTask(this.ttsIovSubscribeTask, IovTaskEventTypeEnum.NORMAL);


    }

}
