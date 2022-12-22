package com.tts.remote.dto;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *   业务系统查询坐标参数dto
 *
 * @author  FangYuan
 * @since 2022-12-22 17:32:06
 */
public class CoordinateQueryDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -3097098719628261091L;


    /**
     * 查询类别:
     * 预留参数,目前没用
     */
    private int queryType;

    /**
     *  租户id,陕煤这种本地化的就是一个常量
     *  用于多租户情况
     * */
    private Long tenantId;

    /** 要查询的车辆id列表 */
    private List<Long> vehicleIdList;

    /**　要查询的装车单id */
    private Long shipmentId;

    /** 查询坐标范围的起始时间 */
    private Date startTime;

    /** 查询坐标范围的结束时间 */
    private Date endTime;


    /** 业务动作,选填 */
    private String action;

    /** 坐标系 */
    private String coordinateType;

    private Integer timeNearBy;


    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }


    public int getQueryType() {
        return queryType;
    }

    public void setQueryType(int queryType) {
        this.queryType = queryType;
    }

    public List<Long> getVehicleIdList() {
        return vehicleIdList;
    }

    public void setVehicleIdList(List<Long> vehicleIdList) {
        this.vehicleIdList = vehicleIdList;
    }


    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getCoordinateType() {
        return coordinateType;
    }

    public void setCoordinateType(String coordinateType) {
        this.coordinateType = coordinateType;
    }

    public Integer getTimeNearBy() {
        return timeNearBy;
    }

    public void setTimeNearBy(Integer timeNearBy) {
        this.timeNearBy = timeNearBy;
    }

    @Override
    public String toString() {
        return "CoordinateQueryDto{" +
                       "queryType=" + queryType +
                       ", tenantId=" + tenantId +
                       ", vehicleIdList=" + vehicleIdList +
                       ", shipmentId=" + shipmentId +
                       ", startTime=" + startTime +
                       ", endTime=" + endTime +
                       ", action='" + action + '\'' +
                       ", coordinateType='" + coordinateType + '\'' +
                       ", timeNearBy='" + timeNearBy + '\'' +
                       '}';
    }
}
