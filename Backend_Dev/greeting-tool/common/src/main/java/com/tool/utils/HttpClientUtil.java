package com.tool.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.config.RequestConfig;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * Http tools
 */
public class HttpClientUtil {

    static final int TIMEOUT_MSEC = 5000;

    /**
     * get request
     * @param url
     * @param paramMap
     * @return
     */
    public static String doGet(String url, Map<String, String> paramMap) {
        // create a httpclient obj
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        CloseableHttpResponse response = null;

        try {
            //create URIBuilder obj
            URIBuilder uriBuilder = new URIBuilder(url);
            if (paramMap != null ) {
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    uriBuilder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            // set URI
            URI uri = uriBuilder.build();

            // send HttpGet request
            HttpGet httpGet = new HttpGet(uri);

            // get response
            response = httpClient.execute(httpGet);

            if(response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(),"UTF-8");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  result;
    }

    /**
     * post request
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     */
    public static String doPost(String url, Map<String, String> paramMap)throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;

        try {
            HttpPost httpPost = new HttpPost(url);
            if (paramMap != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "UTF-8");
                httpPost.setEntity(entity);
            }

            httpPost.setConfig(builderRequestConfig());

            response = httpClient.execute(httpPost);

            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        }catch (Exception e){
            throw new IOException(e);
        }finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultString;
    }

    /**
     * post request by JSON
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     */
    public String doPostByJson(String url, Map<String, String> paramMap) throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";

        try {
            // create http request
            HttpPost httpPost = new HttpPost(url);

            if (paramMap != null) {
                //set JSON
                JSONObject jsonObject = new JSONObject();
                for (Map.Entry<String, String> param : paramMap.entrySet()) {
                    jsonObject.put(param.getKey(),param.getValue());
                }
                StringEntity entity = new StringEntity(jsonObject.toString(),"utf-8");
                entity.setContentEncoding("utf-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }

            httpPost.setConfig(builderRequestConfig());

            // 执行http请求
            response = httpClient.execute(httpPost);

            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    /**
     * request config
     * @return
     */
    private static RequestConfig builderRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(TIMEOUT_MSEC)
                .setConnectionRequestTimeout(TIMEOUT_MSEC)
                .setSocketTimeout(TIMEOUT_MSEC).build();
    }

}
