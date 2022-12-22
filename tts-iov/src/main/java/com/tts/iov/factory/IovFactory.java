package com.tts.iov.factory;



import com.tts.iov.exception.IovException;
import com.tts.iov.facade.IovFacade;

import java.util.Properties;

/**
 * iov 工厂类,根据 iov 类型 产生对应 的 iov  facade 实例
 *
 * @author FangYuan
 * @since 2022-12-22 17:32:06
 */
public class IovFactory {
    private final static String G7_TYPE_NAME = "G7";
    private final static String SOINOIOV_TYPE_NAME = "SINOIOV";

    private final static String G7_CLASS = "com.jd.jcloud.tts.iov.g7.facade.G7Facade";
    private final static String SOINOIOV_CLASS = "com.jd.jcloud.tts.iov.sinoiov.facade.SinoiovFacade";
    private final static String E6YUN_CLASS = "com.jd.jcloud.tts.iov.e6yun.facade.E6yunFacade";

    /**
     * 创建 面门实现类
     *
     * @param type
     * @return
     */
    public static IovFacade createIovFacade(String type, Properties properties) throws Exception {
        String className = null;

        if (G7_TYPE_NAME.equals(type)) {
            className = G7_CLASS;
        } else if (SOINOIOV_TYPE_NAME.equals(type)) {
            className = SOINOIOV_CLASS;
        } else if (E6YUN_CLASS.equals(type)) {
            className = E6YUN_CLASS;
        } else {
            throw new IovException("IOV_TYPE [" + type + "] is error!  ");
        }

        IovFacade iovFacade = (IovFacade) Class.forName(className).newInstance();

        iovFacade.initProperties(properties);

        return iovFacade;
    }

}
