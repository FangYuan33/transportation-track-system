package com.tts.iov.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tts.iov.domain.IovSubscribeTaskVehicle;
import org.apache.ibatis.annotations.Param;

public interface IovSubscribeTaskVehicleMapper extends BaseMapper<IovSubscribeTaskVehicle> {

    /**
     * 根据ID获取该车任务的iov设备类型
     */
    String getIovTypeById(@Param("id") Long id);
}
