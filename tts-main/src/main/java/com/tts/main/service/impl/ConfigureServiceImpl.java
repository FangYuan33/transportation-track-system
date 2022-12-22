package com.tts.main.service.impl;

import com.tts.base.constant.TtsConstant;
import com.tts.framework.cache.redis.RedisClient;
import com.tts.framework.cache.redis.operator.RedisClientObjectOperator;
import com.tts.framework.cache.redis.operator.RedisClientQueueOperator;
import com.tts.main.dao.TtsConfigureMapper;
import com.tts.main.domain.TtsConfigure;
import com.tts.main.service.ConfigureService;
import com.tts.main.service.ITtsGlobalConfigureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 配置管理 servcie 实现类
 *
 * @author FangYuan
 * @since 2022-12-22 20:11:34
 */
@Service
@Slf4j
public class ConfigureServiceImpl implements ConfigureService {

    @Resource
    private TtsConfigureMapper ttsConfigureMapper;

    private RedisClientObjectOperator<TtsConfigure> redisClientObjectOperator;

    @Autowired
    private ITtsGlobalConfigureService ttsGlobalConfigureService;

    @PostConstruct
    public void myInit() {
        this.redisClientObjectOperator = RedisClient.getInstance().getObjectOperator();
    }

    private final String CONFIGURE_NAMESAPCE = "tts_configure";

    ////////////////////////////
    // 数据查询方法区域 //////////
    ////////////////////////////


    /**
     * 查询所有配置信息列表
     *
     * @return 查询的配置信息列表
     */
    @Override
    public List<TtsConfigure> selectConfigureList() {
        return selectConfigureList(null);
    }

    /**
     * 根据配置信息ID查询配置信息
     *
     * @param id 配置信息id
     * @return
     */
    @Override
    public TtsConfigure getConfigureById(Long id) {
        return ttsConfigureMapper.selectById(id);
    }

    @Override
    public TtsConfigure getConfigureByTendIdWithRedis(Long tenantId) {
        return getConfigureByTendIdWithRedis(tenantId, false);
    }


    /**
     * 通过租户id查询配置信息,如果redis可用则从redis查询信息
     * 如果redis信息为空,则从数据库查询信息,并将结果存入redis
     * <p>
     * 如果没有这个租户的专属配置信息,则使用默认的配置信息,即 tenantId = -1;
     * <p>
     * 如果 isRefresh 为 true 表示  强制刷新内存
     *
     * @param tenantId
     * @return
     */
    @Override
    public TtsConfigure getConfigureByTendIdWithRedis(Long tenantId, boolean isRefresh) {

        if (!ttsGlobalConfigureService.getGlobalRedisSwitch()) {
            return new TtsConfigure();
        }

        TtsConfigure ttsConfigure = null;

        if (!isRefresh) {
            ttsConfigure = redisClientObjectOperator.get(CONFIGURE_NAMESAPCE, tenantId.toString());
        }

        //如果缓存中的 租户配置 为空,则从数据库中查找,并存入redis中以备下次使用
        if (ttsConfigure == null) {
            TtsConfigure configureQueryConditions = new TtsConfigure();
            configureQueryConditions.setTenantId(tenantId);
            List<TtsConfigure> ttsConfigureList = ttsConfigureMapper.selectConditionList(configureQueryConditions);


            //如果默认配置为空则抛出异常
            if (ttsConfigureList.size() == 0 && tenantId.equals(TtsConstant.DEFAULT_TENANT_ID)) {
                log.error(" 默认租户的 ttsConfigure 不能为空! ");
                return null;
            }

            //获取这个租户的配置, 一个租户只会有一个配置,所以这个ttsConfigureList 的size 一定为 0 或者 1
            //没有这个租户的配置信息,那么就使用默认的配置信息替代
            if (ttsConfigureList.size() == 0) {
                ttsConfigure = getConfigureByTendIdWithRedis(TtsConstant.DEFAULT_TENANT_ID);
            } else {
                ttsConfigure = ttsConfigureList.get(0);
            }

            if (ttsConfigure == null) {
                log.error(" 租户[" + tenantId + "]的 ttsConfigure 为空! ");
                return null;
            } else {
                //将查询到的配置值存入redis缓存
                redisClientObjectOperator.setWithDefaultTTL(CONFIGURE_NAMESAPCE, tenantId.toString(), ttsConfigure);
                return ttsConfigure;
            }
        } else {
            return ttsConfigure;
        }

    }


    /**
     * 根据查询条件查询配置信息列表
     *
     * @param configureQueryConditions 查询条件
     * @return 查询的配置信息列表
     */
    @Override
    public List<TtsConfigure> selectConfigureList(TtsConfigure configureQueryConditions) {
        return this.ttsConfigureMapper.selectConditionList(configureQueryConditions);
    }


    /**
     * 检查租户id是否唯一
     *
     * @param tenantId 配置信息
     * @return
     */
    @Override
    public boolean checkTenantIdUnique(Long tenantId) {

        TtsConfigure configureQueryConditions = new TtsConfigure();
        configureQueryConditions.setTenantId(tenantId);

        List<TtsConfigure> ttsConfigure = this.selectConfigureList(configureQueryConditions);

        return ttsConfigure.size() == 0;

    }


    ////////////////////////////
    // 数据修改方法区域 //////////
    ///////////////////////////


    /**
     * 保存一个新的租户配置信息
     * 不加事务,这样即使redis操作报错了数据也会存到数据库中
     *
     * @param ttsConfigure 配置信息
     */
    @Override
    public Integer save(TtsConfigure ttsConfigure) {

        //将配置信息存到数据库
        int row = ttsConfigureMapper.insert(ttsConfigure);


        //如果当前redis 没有开启,则默认相关开关全是关闭,则不执行以下动作
        if (ttsGlobalConfigureService.getGlobalRedisSwitch()) {
            //将配置信息存到redis中
            this.redisClientObjectOperator.setWithDefaultTTL(CONFIGURE_NAMESAPCE, ttsConfigure.getTenantId().toString(), ttsConfigure);
            //根据配置变更执行业务动作
            dealConfigChange(ttsConfigure);
        }

        return row;
    }

    /**
     * 更新配置信息
     * 不加事务,这样即使redis操作报错了数据也会存到数据库中
     *
     * @param ttsConfigure 配置信息
     * @return
     */
    @Override
    public Integer update(TtsConfigure ttsConfigure) {
        int row = ttsConfigureMapper.updateById(ttsConfigure);


        //如果当前redis 没有开启,则默认相关开关全是关闭,则不执行以下动作
        if (ttsGlobalConfigureService.getGlobalRedisSwitch()) {
            //将配置信息存到redis中
            this.redisClientObjectOperator.setWithDefaultTTL(CONFIGURE_NAMESAPCE, ttsConfigure.getTenantId().toString(), ttsConfigure);
            //根据配置变更执行业务动作
            dealConfigChange(ttsConfigure);
        }

        return row;
    }

    /**
     * 根据租户id删除其配置信息
     * 注意租户id为-1的信息不能删除,因为其为默认信息
     */
    @Override
    public boolean deleteByTenantId(Long tenantId) throws Exception {

        //判断租户id 是否为 -1 ,如果是的话则不能删除
        if (tenantId.equals(TtsConstant.DEFAULT_TENANT_ID)) {
            return false;
        } else {
            int resultCount = this.ttsConfigureMapper.deleteByTenantId(tenantId);

            //如果redis 开启则执行这些动作
            if (ttsGlobalConfigureService.getGlobalRedisSwitch()) {
                //删除redis配置中这个租户的配置缓存
                this.redisClientObjectOperator.getAndDelete(CONFIGURE_NAMESAPCE, tenantId.toString());
                //刷新这个租户的配置的redis缓存为默认配置信息
                this.getConfigureByTendIdWithRedis(tenantId, true);
            }

            return true;
        }
    }


    /**
     * 根据配置信息的变化刷新系统信息
     *
     * @param ttsConfigure
     */
    private void dealConfigChange(TtsConfigure ttsConfigure) {

        //检查下,如果全部租户的 geofenceMonitorSwitch 或者 geofenceMonitorNotifySwitch 均是关闭状态
        //那么就清空 redis中的 GEO-MONITOR-QUEUE-
        boolean isAllMonitorClosed = false;

        //查询有没有开启状态的监听器
        TtsConfigure configureQueryCondition1 = new TtsConfigure();
        configureQueryCondition1.setGeofenceMonitorSwitch(true);
        List<TtsConfigure> configureList1 = this.ttsConfigureMapper.selectConditionList(configureQueryCondition1);

        //当前没有开启状态的监听器
        if (configureList1.size() == 0) {
            isAllMonitorClosed = true;
        }

        if (!isAllMonitorClosed) {
            TtsConfigure configureQueryCondition2 = new TtsConfigure();
            configureQueryCondition2.setGeofenceMonitorNotifySwitch(true);
            List<TtsConfigure> configureList2 = this.ttsConfigureMapper.selectConditionList(configureQueryCondition2);

            //当前没有开启主动通知状态的监听器
            if (configureList2.size() == 0) {
                isAllMonitorClosed = true;
            }
        }


        //如果当前没有需要主动通知的内容,那么就清空 redis中的这个队列
        if (isAllMonitorClosed) {
            RedisClient redisClient = RedisClient.getInstance();
            RedisClientQueueOperator redisClientQueueOperator = redisClient.getQueueOperator();
            redisClientQueueOperator.clear(TtsConstant.REDIS_NAMESPACE, TtsConstant.REDIS_KEY_GEO_MONITOR_QUEUE);
        }

    }

}
