package com.tts.common.context;

/**
 * TTS 上下文工具类
 *
 * @author FangYuan
 * @since 2023-01-07 14:37:47
 */
public class TtsContext {

    private static final InheritableThreadLocal<String> NODE_NAME = new InheritableThreadLocal<>();

    public static String getNodeServerName() {
        return NODE_NAME.get();
    }

    public static void setNodeServerName(String serverName) {
        NODE_NAME.set(serverName);
    }
}
