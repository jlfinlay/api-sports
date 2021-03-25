package com.jl.openapi.utils;

import com.alibaba.fastjson.JSONObject;
import com.jl.openapi.exception.ConnectTimeoutException;
import com.jl.openapi.exception.HttpClientException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 功能描述: httpclient 工具类Client
 */
@Slf4j
@Component
public class HttpClientUtils {

    private final static int CONNECT_TIMEOUT = 15000;//与远程服务器连接超时时间

    private final static int CONNECTION_REQUEST_TIMEOUT = 5*1000;//从连接池中获取连接的超时时间

    private final static int SOCKET_TIMEOUT = 15000; //socket读数据超时时间：从服务器获取响应数据的超时时间

    public static final String UTF_8 = "UTF-8";

    public static final String KEY_CODE = "code";

    public static final String KEY_ENTITY = "entity";

    public static CloseableHttpClient getCloseableHttpClient(Integer timeout) throws Exception {
        return HttpClients.custom()
                .setConnectionManager(createConnectionManager())
                .setDefaultRequestConfig(getRequestConfig(timeout))
                .build();
    }
    private static RequestConfig getRequestConfig(Integer timeout) {
        return RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setSocketTimeout(timeout)
                .build();
    }
    public static PoolingHttpClientConnectionManager createConnectionManager() throws Exception {
        SSLContext context = SSLContext.getInstance("TLS");
        TrustManager tm = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
        };
        context.init(null, new TrustManager[] { tm }, null);

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(context, NoopHostnameVerifier.INSTANCE);

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.INSTANCE).register("https", socketFactory).build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry);
        // 设置连接池大小
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultMaxPerRoute(connectionManager.getMaxTotal());
        // Validate connections after 1 sec of inactivity
        connectionManager.setValidateAfterInactivity(2000);
        return connectionManager;
    }



    /******************  GET  *******************/

    public static JSONObject doGetOfReturnJson(String url, Integer timeout) throws Exception {
        return doGetOfReturnJson(url,null,timeout);
    }

    public static JSONObject doGetOfReturnJson(String url, Map<String,Object> headerMap, Integer timeout) throws Exception {
        CloseableHttpResponse response = null;
        String result = null;
        Integer code = null;//http_status
        JSONObject jsonObject = new JSONObject();
        log.info("HttpClientUtils-doGetOfReturnJson请求url={} -->headerMap={}",url,headerMap);
        try {
            response = doGetBaseInit(url,timeout,headerMap);
            if(response != null){
                code = response.getStatusLine().getStatusCode();
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, UTF_8);
                }
            }
            jsonObject.put(KEY_CODE,code);
            jsonObject.put(KEY_ENTITY,result);
            log.info("HttpClientUtils-doGetOfReturnJson返回数据："+jsonObject);
            return jsonObject;
        }finally {
            if(response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static CloseableHttpResponse doGetBaseInit(String url, Integer timeout, Map<String,Object> headerMap) throws Exception {
        if(timeout == null){
            timeout = SOCKET_TIMEOUT;
        }
        //创建HTTPCLIENT链接对象
        CloseableHttpClient httpclient = getCloseableHttpClient(timeout);

        HttpGet httpGet = new HttpGet(url);
        // 设置头部参数。
        if (headerMap != null) {
            Iterator iterator = headerMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                httpGet.setHeader(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        CloseableHttpResponse response;
        try {
            response = httpclient.execute(httpGet);
        } catch (SocketTimeoutException e) {//只处理响应超时，需要订单轮询
            e.printStackTrace();
            log.info("发起HTTP-GET请求SocketTimeout!{}",e.getMessage());
            throw new HttpClientException("发起HTTP-GET请求异常!",e);
        } catch (org.apache.http.conn.ConnectTimeoutException e){
            e.printStackTrace();
            log.info("发起HTTP-GET ConnectTimeout!{}",e.getMessage());
            throw new ConnectTimeoutException();
        }
        return response;
    }

    /******************  POST  *******************/
    public static String doPostBase(String url, Map<String, Object> paramsMap,String paramsStr, Integer timeout,String paramsStrContentType,Map<String, Object> headerMap) throws Exception {
        CloseableHttpResponse response = null;
        log.info("HttpClientUtils-doPostBase请求url={},参数Map={},参数str={}",url,paramsMap,paramsStr);
        try {
            response = doPostBaseInit(url,paramsMap,headerMap,paramsStr,paramsStrContentType,timeout);
            String result = null;
            log.info("HttpClientUtils-HTTP响应消息<========" + response);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, UTF_8);
                }
            }else {
                log.info("HttpClientUtils-getResponseResult未成功的响应的消息体entity=" + response.getEntity()
                        + ",statusCode=" + response.getStatusLine().getStatusCode());
            }
            return result;
        } finally {
            if(response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static JSONObject doPostXmlOfReturnJson(String url, String paramsStr, Map<String, Object> headerMap, Integer timeout) throws Exception {
        return doPostBaseOfReturnJson(url,null,headerMap,paramsStr,"application/xml",null);
    }

    public static JSONObject doPostOfReturnJson(String url, String paramsStr, Integer timeout) throws Exception {
        return doPostOfReturnJson(url,paramsStr,"application/json",timeout);
    }

    public static JSONObject doPostOfReturnJson(String url, String paramsStr, String paramsStrContentType, Integer timeout) throws Exception {
        return doPostBaseOfReturnJson(url,null,null,paramsStr,paramsStrContentType,timeout);
    }

    public static JSONObject doPostOfReturnJson(String url, Map<String, Object> paramsMap, Integer timeout) throws Exception {
        return doPostOfReturnJson(url,paramsMap,null,timeout);
    }

    public static JSONObject doPostOfReturnJson(String url, String paramsStr, Map<String, Object> headerMap, Integer timeout) throws Exception {
        return doPostBaseOfReturnJson(url,null,headerMap,paramsStr,null,timeout);
    }

    public static JSONObject doPostOfReturnJson(String url, Map<String, Object> paramsMap, Map<String, Object> headerMap, Integer timeout) throws Exception {
        return doPostBaseOfReturnJson(url,paramsMap,headerMap,null,null,timeout);
    }

    public static JSONObject doPostBaseOfReturnJson(String url, Map<String, Object> paramsMap, Map<String, Object> headerMap, String paramsStr, String paramsStrContentType, Integer timeout) throws Exception{
        CloseableHttpResponse response = null;
        String result = null;
        Integer code = null;//http_status
        JSONObject jsonObject = new JSONObject();
        log.info("HttpClientUtils-doPostBaseOfReturnJson请求url={},参数Map={},参数str={}",url,paramsMap,paramsStr);
        try {
            response = doPostBaseInit(url,paramsMap,headerMap,paramsStr,paramsStrContentType,timeout);
            if(response != null){
                code = response.getStatusLine().getStatusCode();
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, UTF_8);
                }
            }
            jsonObject.put(KEY_CODE,code);
            jsonObject.put(KEY_ENTITY,result);
            log.info("HttpClientUtils-doPostBaseOfReturnJson返回数据："+jsonObject);
            return jsonObject;
        } finally {
            if(response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static CloseableHttpResponse doPostBaseInit(String url, Map<String, Object> paramsMap, Map<String, Object> headerMap, String paramsStr, String paramsStrContentType, Integer timeout) throws Exception {
        if(timeout == null){
            timeout = SOCKET_TIMEOUT;
        }
        //创建HTTPCLIENT链接对象
        CloseableHttpClient httpclient = getCloseableHttpClient(timeout);

        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<>();
        if(paramsMap != null && !paramsMap.isEmpty()){//设置参数
            Iterator iterator = paramsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                nvps.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, UTF_8));
        }
        // 设置头部参数。
        if (headerMap != null && !headerMap.isEmpty()) {
            Iterator iterator = headerMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                httpPost.setHeader(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        if(paramsStr != null){
            StringEntity se = new StringEntity(paramsStr,UTF_8);
            se.setContentEncoding(UTF_8);
            if(paramsStrContentType != null){
                se.setContentType(paramsStrContentType);
            }
            httpPost.setEntity(se);
        }
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
        } catch (SocketTimeoutException e) {//只处理响应超时，需要订单轮询
            e.printStackTrace();
            log.info("发起HTTP-POST请求异常!{}",e.getMessage());
            throw new HttpClientException("发起HTTP-GET请求异常!",e);
        } catch (org.apache.http.conn.ConnectTimeoutException e){
            e.printStackTrace();
            log.info("发起HTTP-POST ConnectTimeout!{}",e.getMessage());
            throw new ConnectTimeoutException();
        }
        return response;
    }

    /******************  PUT  *******************/
    public static JSONObject doPutBase(String url, Map<String, Object> headerMap, String paramsStr, Integer timeout) throws Exception {
        if(timeout == null){
            timeout = SOCKET_TIMEOUT;
        }
        //创建HTTPCLIENT链接对象
        CloseableHttpClient httpClient = getCloseableHttpClient(timeout);
        HttpPut httpPut = new HttpPut(url);
        // 设置头部参数。
        if (headerMap != null) {
            Iterator iterator = headerMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                httpPut.setHeader(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        if(paramsStr != null){
            StringEntity se = new StringEntity(paramsStr);
            se.setContentEncoding(UTF_8);
            se.setContentType("application/json");
            httpPut.setEntity(se);
        }
        CloseableHttpResponse response = null;
        String result = null;
        Integer code = null;//http_status
        try {
            response = httpClient.execute(httpPut);
            if(response != null){
                code = response.getStatusLine().getStatusCode();
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, UTF_8);
                }
            }
        } catch (SocketTimeoutException e) {
            throw new HttpClientException("发起PUT请求异常!",e);
        }  finally {
            if(response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KEY_CODE,code);
        jsonObject.put(KEY_ENTITY,result);
        log.info("HttpClientUtils-doPutBase返回数据："+jsonObject);
        return jsonObject;
    }

}
