package com.tts.main.service;

import com.tts.main.domain.TtsConfigure;
import com.tts.main.domain.TtsCoordinateLatest;
import com.tts.main.domain.TtsCoordinatePoint;
import com.tts.main.dto.CoordinatePointDto4RedisQueue;
import com.tts.remote.app.dto.AppCoordinatePointDto;
import com.tts.remote.dto.CoordinatePointResultDto;
import com.tts.remote.dto.CoordinateQueryDto;

import java.util.List;
import java.util.Map;

/**
 * 坐标存储与查询相关的service
 *
 * @author FangYuan
 * @since 2022-12-22 17:32:06
 */
public interface CoordinatePointService {

    /**
     * 处理远程设备传递过来的坐标数据
     * 处理内容为将坐标保存到数据库
     * 同时将坐标放到 redis 的 队列中,方便后续处理
     *
     * @return 如果这个步骤需要创建sessionId, 则返回sessionId, 否则返回空字符串
     */
    String saveCoordinatePointFlowRecord(AppCoordinatePointDto appCoordinatePointDto);


    /**
     * queryType = 1: 查询给定车辆列表最新位置
     * 查询条件:
     * 车辆id列表
     * 返回结果:
     * 返回 Map<VehicleId,TtsCoordinateLatest>
     */
    Map<Long, TtsCoordinateLatest> queryCoordinateMapInner(CoordinateQueryDto coordinateQueryDto);

    /**
     * queryType = 1: 查询给定车辆列表最新位置
     * 查询条件:
     * 车辆id列表
     * 返回结果:
     * 返回 Map<VehicleId,CoordinateQueryResultDto>
     */
    Map<Long, CoordinatePointResultDto> queryCoordinateMap(CoordinateQueryDto coordinateQueryDto);


    /**
     * queryType = 2: 查询给定车辆列表 在时间范围内的轨迹
     * 查询条件:
     * 车辆id列表: 给定车辆id列表
     * startTime: 查询 >= startTime范围内的数据,startTime不能为空
     * endTime :  查询 <= endTime 范围内的数据,endTime可以为空,表示截止到当前时间
     * 返回结果:
     * 返回 List<CoordinateQueryResultDto> 即属于指定车辆的,在时间范围内的坐标数据,按照时间正向排序生成一个list返回
     */
    List<TtsCoordinatePoint> queryCoordinateListInner(CoordinateQueryDto coordinateQueryDto);

    /**
     * queryType = 2: 查询给定车辆列表 在时间范围内的轨迹
     * 查询条件:
     * 车辆id列表: 给定车辆id列表
     * startTime: 查询 >= startTime范围内的数据,startTime不能为空
     * endTime :  查询 <= endTime 范围内的数据,endTime可以为空,表示截止到当前时间
     * 返回结果:
     * 返回 List<CoordinateQueryResultDto> 即属于指定车辆的,在时间范围内的坐标数据,按照时间正向排序生成一个list返回
     */
    List<CoordinatePointResultDto> queryCoordinateList(CoordinateQueryDto coordinateQueryDto);


    /**
     * 处理从 redis  队列中获取到的坐标,信息,对其进行进一步相关的运算处理
     */
    void dealCoordinateFromRedisQueue(CoordinatePointDto4RedisQueue coordinatePointDto4RedisQueue);


    /**
     * 进行坐标轨迹相关的运算
     */
    void dealCoordinateTrace(CoordinatePointDto4RedisQueue coordinatePointDto4RedisQueue, TtsConfigure ttsConfigure);


    /**
     * 进行坐标点最新的点相关的处理
     */
    void dealCoordinateLatestPoint(CoordinatePointDto4RedisQueue coordinatePointDto4RedisQueue, TtsConfigure ttsConfigure);

}
