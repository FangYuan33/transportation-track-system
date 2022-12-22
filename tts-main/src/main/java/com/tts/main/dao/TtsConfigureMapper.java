package com.tts.main.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tts.main.domain.TtsConfigure;

import java.util.List;

/**
 * 配置信息 数据层
 *
 * @author FangYuan
 * @since 2022-12-22 20:12:54
 */
public interface TtsConfigureMapper extends BaseMapper<TtsConfigure> {

    /**
     * 根据查询条件查询配置信息列表
     */
    List<TtsConfigure> selectConditionList(TtsConfigure configureQueryConditions);

    /**
     * 通过租户id删除其配置信息,注意租户id不能为-1
     */
    int deleteByTenantId(Long tenantId);
}
