package com.tts.main.service;

import com.tts.main.domain.TtsConfigure;

import java.util.List;

/**
 * 配置管理 service
 *
 * @author FangYuan
 * @since 2022-12-22 20:10:18
 */
public interface ConfigureService {

    /**
     * 查询配置信息列表
     *
     * @return 查询的配置信息列表
     */
    List<TtsConfigure> selectConfigureList();

    /**
     * 根据查询条件查询配置信息列表
     *
     * @param configureQueryCondition 查询条件
     * @return 查询的配置信息列表
     */
    List<TtsConfigure> selectConfigureList(TtsConfigure configureQueryCondition);

    /**
     * 检查租户id是否唯一
     *
     * @param tenantId 租户id
     */
    boolean checkTenantIdUnique(Long tenantId);

    /**
     * 通过配置id查询配置信息
     */
    TtsConfigure getConfigureById(Long id);

    /**
     * 通过租户id查询配置信息,如果redis可用则从redis查询信息
     * 如果redis信息为空,则从数据库查询信息,并将结果存入redis
     */
    TtsConfigure getConfigureByTendIdWithRedis(Long tenantId);

    TtsConfigure getConfigureByTendIdWithRedis(Long tenantId, boolean isRefresh);

    /**
     * 保存一个新的租户配置信息
     *
     * @param ttsConfigure 配置信息
     */
    Integer save(TtsConfigure ttsConfigure);

    /**
     * 更新一个已有租户的配置信息
     *
     * @param ttsConfigure 配置信息
     */
    Integer update(TtsConfigure ttsConfigure);

    /**
     * 根据租户id删除其配置信息
     * 注意租户id为-1的信息不能删除,因为其为默认信息
     */
    boolean deleteByTenantId(Long tenantId) throws Exception;
}
