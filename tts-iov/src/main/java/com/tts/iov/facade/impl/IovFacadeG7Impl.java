package com.tts.iov.facade.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tts.common.constant.HttpHeader;
import com.tts.common.constant.HttpSchema;
import com.tts.common.core.domain.Request;
import com.tts.common.core.domain.Response;
import com.tts.common.enums.Method;
import com.tts.common.utils.http.Client;
import com.tts.common.utils.http.MessageDigestUtil;
import com.tts.iov.domain.IovConfig;
import com.tts.iov.facade.IovFacade;
import com.tts.iov.facade.constant.G7UrlConstant;
import com.tts.iov.facade.dto.PointToPointLocationData;
import com.tts.iov.facade.dto.PointToPointTrackData;
import com.tts.iov.service.IovConfigService;
import com.tts.remote.dto.CoordinatePointResultDto;
import com.tts.remote.dto.IovVehicleQueryDto;
import com.tts.remote.enums.IovTypeEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class IovFacadeG7Impl implements IovFacade, InitializingBean {

    @Autowired
    private IovConfigService iovConfigService;

    @SuppressWarnings("unchecked")
    @Override
    public void afterPropertiesSet() {
        IovConfig iovConfig = iovConfigService.getByIovType(IovTypeEnums.G7.getValue());
        if (iovConfig != null) {
            Map<String, String> map = JSONObject.parseObject(iovConfig.getConfigInfo(), Map.class);

            baseUrl = map.get("baseUrl");
            accessId = map.get("accessId");
            secretKey = map.get("secretKey");
        }
    }

    private String baseUrl;
    private String accessId;
    private String secretKey;

    @Override
    public List<CoordinatePointResultDto> queryIovVehicleLastLocationDirectly(IovVehicleQueryDto vehicleQueryDto) {
        // 封装入参
        String json = initialQueryVehicleLastLocationByVehicleNoParam(vehicleQueryDto.getVehicleNo());
        // 执行请求，获取结果参数
        JSONObject resultData = doPostOrPutRequest(json, G7UrlConstant.QUERY_LAST_LOCATION_BY_SINGLE_PLATE_NUM);

        if (resultData != null) {
            PointToPointLocationData pointInfo = resultData.toJavaObject(PointToPointLocationData.class);
            // 初始化结果对象参数
            CoordinatePointResultDto result = initialVehicleLastLocationByVehicleNoResult(pointInfo, vehicleQueryDto.getVehicleNo());

            return Collections.singletonList(result);
        }

        return Collections.emptyList();
    }

    /**
     * 初始化端对端接口查询车辆位置的请求参数
     *
     * @param vehicleNo 车牌号
     */
    private String initialQueryVehicleLastLocationByVehicleNoParam(String vehicleNo) {
        HashMap<String, String> requestParam = new HashMap<>(2);
        // G7侧接口为了合规，我们默认写死一个单号给到他们
        requestParam.put("wayBillNum", "PSJH2022112401");
        requestParam.put("plateNum", vehicleNo);

        return JSONObject.toJSONString(requestParam);
    }

    /**
     * 初始化端对端查询车辆位置的结果对象参数
     */
    private CoordinatePointResultDto initialVehicleLastLocationByVehicleNoResult(PointToPointLocationData pointInfo,
                                                                                 String vehicleNo) {
        CoordinatePointResultDto result = new CoordinatePointResultDto();
        // 车牌号
        result.setVehicleNo(vehicleNo);
        // 定位时间
        result.setTime(pointInfo.getGpstime());
        // 经纬度
        result.setLongitude(pointInfo.getLng());
        result.setLatitude(pointInfo.getLat());
        // 速度、方向
        result.setSpeed(pointInfo.getSpeed());
        result.setDirection(pointInfo.getCourse());
        // 坐标系没地儿赋值，返回的是GCJ

        return result;
    }

    /**
     * 查询轨迹点位，单次最多返回1000个点位
     * 第一次查询若小于1000个，则直接返回结果
     * 若为1000个，则需要继续查询后边儿可能存在的点位
     * 第二次及之后的请求，需要带上上一次请求返回的序列号并且以上一次的最后一个点位的打点时间作为第二次请求的开始时间
     */
    @Override
    public List<CoordinatePointResultDto> queryIovVehicleTrackDirectly(IovVehicleQueryDto vehicleQueryDto) {
        // 封装入参
        String json = initialQueryVehicleTrackParam(vehicleQueryDto.getVehicleNo(),
                vehicleQueryDto.getTimeStart(), vehicleQueryDto.getTimeEnd());
        // 保存所有点位的结果集
        List<PointToPointTrackData> allTracePoints = new ArrayList<>();

        // 查询轨迹点位
        getAllTracePoints(json, allTracePoints);

        // 转换成通用的点位结果对象并返回
        return convertG7PointsToCommonIovPoint(allTracePoints, vehicleQueryDto.getVehicleNo());
    }

    /**
     * 初始化端对端接口查询车辆轨迹的请求参数
     *
     * @param vehicleNo    车牌号
     * @param startTime    起始时间
     * @param endTime      结束时间
     */
    private String initialQueryVehicleTrackParam(String vehicleNo, LocalDateTime startTime, LocalDateTime endTime) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        HashMap<String, String> requestParam = new HashMap<>(8);
        // G7侧接口为了合规，我们默认写死一个单号给到他们
        requestParam.put("wayBillNum", "PSJH2022112401");
        // 车牌号
        requestParam.put("plateNum", vehicleNo);
        // 起止时间
        requestParam.put("startTime", dateFormat.format(startTime));
        requestParam.put("endTime", dateFormat.format(endTime));

        return JSONObject.toJSONString(requestParam);
    }

    /**
     * 执行该方法来将所有点位保存到结果集中
     * 该方法是循环调用，如果点位数量为1000，那么则需要继续调用该方法拉取点位
     *
     * @param currentJson    本次请求参数的json串
     * @param allTracePoints 保存所有点位的结果集
     */
    private void getAllTracePoints(String currentJson, List<PointToPointTrackData> allTracePoints) {
        // 查询本次轨迹
        JSONObject currentResultData = doPostOrPutRequest(currentJson, G7UrlConstant.QUERY_TRACK_BY_SINGLE_PLATE_NUM);

        if (currentResultData != null) {
            JSONArray trace = currentResultData.getJSONArray("trace");
            if (trace != null) {
                // 本次查询获取的点位信息，保存到结果集中
                List<PointToPointTrackData> currentTrace = trace.toJavaList(PointToPointTrackData.class);
                allTracePoints.addAll(currentTrace);

                // 循环结束条件，点位小于1000，证明没有再多的点位需要拉取了
                if (currentTrace.size() < 1000) {
                    return;
                }

                // 初始化下一次轨迹查询的参数
                String nextJson = initialNextQueryVehicleTrackParam((String) currentResultData.get("sn"),
                        currentTrace.get(currentTrace.size() - 1).getTime(), currentJson);

                // 进行下一次查询
                getAllTracePoints(nextJson, allTracePoints);
            }
        }
    }

    /**
     * 封装进行下一次轨迹查询的参数
     *
     * @param sn            序列号，上一次请求中结果中获取，需要在第二次请求中携带
     * @param lastPointTime 上一次请求的最后一个点位的打点时间，需要作为本次请求的开始时间
     * @param previousJson  上一次请求的参数json串，因为有部分参数不变，所以重新解析，并在基础上覆盖开始时间和序列号参数
     */
    private String initialNextQueryVehicleTrackParam(String sn, String lastPointTime, String previousJson) {
        // 上一次请求最后一个点位的打点时间作为本次请求的开始时间
        Date nextStartTime = new Date(Long.parseLong(lastPointTime));
        // 解析上一次的请求参数，并更新序列号参数和开始时间参数
        JSONObject nextJson = JSONObject.parseObject(previousJson);
        nextJson.put("startTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nextStartTime));
        nextJson.put("sn", sn);

        return nextJson.toJSONString();
    }

    /**
     * 将G7端到端轨迹信息转换成通用的点位结果对象
     */
    private List<CoordinatePointResultDto> convertG7PointsToCommonIovPoint(List<PointToPointTrackData> allTracePoints,
                                                                           String vehicleNo) {
        List<CoordinatePointResultDto> result = new ArrayList<>(allTracePoints.size());
        for (PointToPointTrackData point : allTracePoints) {
            result.add(initialVehicleTrackInfo(point, vehicleNo));
        }

        return result;
    }

    /**
     * 初始化端对端车辆轨迹点位信息
     */
    private CoordinatePointResultDto initialVehicleTrackInfo(PointToPointTrackData point, String vehicleNo) {
        CoordinatePointResultDto result = new CoordinatePointResultDto();
        // 车牌号
        result.setVehicleNo(vehicleNo);
        try {
            // 定位时间
            result.setTime(String.valueOf(point.getTime()));
        } catch (Exception e) {
            log.error("端对端车辆轨迹接口定位时间字段异常", e);
        }
        // 经纬度
        result.setLongitude(point.getLng());
        result.setLatitude(point.getLat());
        // 速度、方向
        result.setSpeed(point.getSpeed());
        result.setDirection(point.getCourse());

        return result;
    }

    /**
     * 执行Post or Put 请求
     *
     * @param json       请求参数的json串
     * @param requestUrl 请求地址
     */
    private JSONObject doPostOrPutRequest(String json, String requestUrl) {
        try {
            // 请求头信息
            Map<String, String> headers = initialPostOrPutRequestHeaders(json);
            // 生成Request请求
            Request request = initialRequestInfo(headers, json, requestUrl);
            log.info("调用接口 {}，入参: {}", requestUrl, JSONObject.toJSONString(request));

            // 调用服务端
            Response response = Client.execute(request);
            log.info("调用接口 {}，出参: {}", requestUrl, JSONObject.toJSONString(response));

            // 返回的参数
            String body = response.getBody();
            JSONObject g7PullData = JSONObject.parseObject(body);

            // 请求成功返回结果值
            if (checkPointToPointResult(g7PullData)) {
                return g7PullData.getJSONObject("data");
            }
        } catch (Exception e) {
            log.error("请求G7接口 {" + requestUrl + "} 失败", e);
        }

        return null;
    }

    /**
     * 初始化 post/put 请求的通用请求头
     *
     * @param jsonParam 请求参数的json格式
     */
    private Map<String, String> initialPostOrPutRequestHeaders(String jsonParam) {
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeader.HTTP_HEADER_CONTENT_MD5, MessageDigestUtil.base64AndMD5(jsonParam));
        //（POST/PUT请求必选）请求Body内容格式
        headers.put(HttpHeader.HTTP_HEADER_CONTENT_TYPE, "application/json; charset=UTF-8");
        headers.put(HttpHeader.HTTP_HEADER_G7_TIMESTAMP, "" + System.currentTimeMillis());

        return headers;
    }

    /**
     * 初始化request请求
     *
     * @param headers    请求头信息
     * @param jsonParam  json请求参数
     * @param requestUrl 请求接口地址
     */
    private Request initialRequestInfo(Map<String, String> headers, String jsonParam, String requestUrl) {
        Request request = new Request(Method.POST_JSON, HttpSchema.HTTP + baseUrl,
                requestUrl, accessId, secretKey, 5000);
        request.setHeaders(headers);
        request.setJsonStrBody(jsonParam);

        return request;
    }

    /**
     * 校验端对端接口调用是否成功
     */
    private boolean checkPointToPointResult(JSONObject g7PullData) {
        return g7PullData != null
                && (Integer.valueOf(0).equals(g7PullData.get("code")) || "succ".equals(g7PullData.get("msg")));
    }
}
