package com.tts.common.utils.http;

import com.tts.common.constant.Constants;
import com.tts.common.constant.HttpMethod;
import com.tts.common.constant.SystemHeader;
import com.tts.common.core.domain.Response;
import com.tts.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * FAQ：列举部分使用sdk遇到的问题，仅供参考
 * 1、每次请求new HttpClient，tcp + handshake + ssl...导致接口调用效率低。httpclient是一个线程安全的类，推荐多线程复用httpclient
 * 2、多线程下，未根据场景修改DefaultMaxPerRoute值，默认为5，调用效率低。 同一时间向同一域名发起的总请求数<=DefaultMaxPerRoute<=MaxTotal
 */
@Slf4j
public class HttpPoolUtil {

    public static final String UTF8 = "UTF-8";
    public static volatile boolean isClosed = false;
    //总并发线程数
    public static final int maxTotalPool = 200;
    //同一域名下，并发线程数
    public static final int maxTotalPerRoute = 10;
    public static final int MAX_TIMEOUT = 10*1000;
    public static final int RequestTimeout = 5000;

    private static RequestConfig requestConfig;
    private static HttpClientBuilder httpClientBuilder;
    private static PoolingHttpClientConnectionManager poolConnManager;
    private static IdleConnectionMonitorThread idleConnectionMonitorThread;
    private static CloseableHttpClient httpClient;

    static {
        // 设置连接池
        poolConnManager = new PoolingHttpClientConnectionManager();
        poolConnManager.setMaxTotal(maxTotalPool);
        poolConnManager.setDefaultMaxPerRoute(maxTotalPerRoute);

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(RequestTimeout);
        // 在提交请求之前 测试连接是否可用（有性能问题）
        //configBuilder.setStaleConnectionCheckEnabled(true);
        idleConnectionMonitorThread = new IdleConnectionMonitorThread(poolConnManager);
        idleConnectionMonitorThread.start();
        requestConfig = configBuilder.build();
        httpClientBuilder = HttpClients.custom()
                .setConnectionManagerShared(true)
                .setConnectionManager(poolConnManager)
                .setDefaultRequestConfig(requestConfig);
        httpClient = httpClientBuilder.build();
        log.info(">>>>>>>>>>> PoolingHttpClientConnectionManager初始化成功 >>>>>>>>>>>");
    }

    /**
     * 单例
     *
     * @return httpClient
     */
    public static  CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * 获得状态
     */
    public static String getTotalStats() {
        return poolConnManager.getTotalStats().toString();
    }

    /**
     * 关闭连接池资源
     */
    public static void closePool() {
        if (!isClosed) {
            isClosed = true;
            poolConnManager.close();
        }
    }

    public static class IdleConnectionMonitorThread extends Thread {

        private final HttpClientConnectionManager connMgr;
        private volatile boolean shutdown;

        public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
            super();
            this.connMgr = connMgr;
        }

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(30 * 1000);
                        // Close expired connections
                        connMgr.closeExpiredConnections();
                        // Optionally, close connections
                        // that have been idle longer than 30 sec
                        connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
                    }
                }
            } catch (InterruptedException ex) {
                // terminate
            }
        }

        public void shutdown() {
            shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }

    private static String initUrl(String host, String path, Map<String, String> queries) throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(host);
        if (!StringUtils.isBlank(path)) {
            sbUrl.append(path);
        }
        if (null != queries) {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, String> query : queries.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append(Constants.SPE3_CONNECT);
                }
                if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!StringUtils.isBlank(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isBlank(query.getValue())) {
                        sbQuery.append(Constants.SPE4_EQUAL);
                        sbQuery.append(URLEncoder.encode(query.getValue(), Constants.ENCODING));
                    }
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append(Constants.SPE5_QUESTION).append(sbQuery);
            }
        }

        return sbUrl.toString();
    }

    /**
     * 初始化基础Header
     */
    private static Map<String, String> initialBasicHeader(String method, String path,
                                                          Map<String, String> headers,
                                                          Map<String, String> queries,
                                                          Map<String, String> bodies,
                                                          String appKey, String appSecret)
            throws MalformedURLException {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
//        headers.put(HttpHeader.HTTP_HEADER_G7_TIMESTAMP,"1562925901520" /*+ System.currentTimeMillis()*/);
        //headers.put(SystemHeader.X_CA_NONCE, UUID.randomUUID().toString());
        //headers.put(SystemHeader.X_CA_KEY, appKey);

        String tmppath = path;
        if(tmppath.startsWith("/custom")){
            tmppath = tmppath.replace("/custom","");
        }
        String sign = SignUtil.sign(appSecret, method, tmppath, headers, queries, bodies);
        log.info("sign:{}", sign);
        headers.put(SystemHeader.X_CA_SIGNATURE, Constants.AUTH_PREFIX + " " + appKey + Constants.SPE2_COLON + sign);

        log.info("headers:", headers);
        return headers;
    }

    private static Response convert(CloseableHttpResponse response) throws Exception {
        Response res = new Response();

        if (null != response) {
            res.setStatusCode(response.getStatusLine().getStatusCode());
            for (Header header : response.getAllHeaders()) {
                res.setHeader(header.getName(), MessageDigestUtil.iso88591ToUtf8(header.getValue()));
            }

            res.setContentType(res.getHeader("Content-Type"));
            res.setRequestId(res.getHeader("X-Ca-Request-Id"));
            res.setErrorMessage(res.getHeader("X-Ca-Error-Message"));
            try {
                res.setBody(EntityUtils.toString(response.getEntity(), "UTF-8"));
            }catch (Exception e){
                throw e;
            }finally {
                EntityUtils.consume(response.getEntity());
                response.close();
            }

        } else {
            //服务器无回应
            res.setStatusCode(500);
            res.setErrorMessage("No Response");
        }

        return res;
    }

    public static Response httpGet(String host, String path, int connectTimeout, Map<String, String> headers, Map<String, String> queries, String appKey, String appSecret)
            throws Exception {
        headers = initialBasicHeader(HttpMethod.GET, path, headers, queries, null, appKey, appSecret);

        HttpGet get = new HttpGet(initUrl(host, path, queries));

        for (Map.Entry<String, String> e : headers.entrySet()) {
            get.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        return convert(httpClient.execute(get));
    }

    public static Response httpPost(String host, String path, int connectTimeout, Map<String, String> headers, Map<String, String> queries, String body, String appKey, String appSecret)
            throws Exception {
        headers = initialBasicHeader(HttpMethod.POST, path, headers, queries, null, appKey, appSecret);

        HttpPost post = new HttpPost(initUrl(host, path, queries));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            post.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        if (StringUtils.isNotBlank(body)) {
            post.setEntity(new StringEntity(body, Constants.ENCODING));

        }

        return convert(httpClient.execute(post));
    }
}