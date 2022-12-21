package com.tts.base.utils;

import com.alibaba.dubbo.rpc.RpcContext;

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * java 默认数据类型格式化和转换类
 * <p>
 * 主要是 存放一些自定义的小工具方法
 */
public class BaseUtilTool {

    /**
     * 将传入的 到毫秒的时间戳 转换为 202103260510 这种格式,
     * 即 2021年03月26日05时10分
     */
    public static Long formatTimeToyyyyMMddHHmm(Date time) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

        return Long.valueOf(sdf.format(time));

    }

    /***
     * 获取本地地址
     */
    public static String getLocalIpByNetcard() {
        try {
            for (Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements(); ) {
                NetworkInterface item = e.nextElement();
                for (InterfaceAddress address : item.getInterfaceAddresses()) {
                    if (item.isLoopback() || !item.isUp()) {
                        continue;
                    }
                    if (address.getAddress() instanceof Inet4Address) {
                        Inet4Address inet4Address = (Inet4Address) address.getAddress();
                        return inet4Address.getHostAddress();
                    }
                }
            }
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * 获取dubbo调用方的地址
     */
    public static String getDubboRequestIp() {
        String remoteHost = "0.0.0.1";
        try {
            RpcContext context = RpcContext.getContext();
            if (null == context) {
                return remoteHost;
            } else {
                remoteHost = context.getRemoteHost();
                return remoteHost;
            }
        } catch (Exception e) {
            return remoteHost;
        }
    }
}
