/**
 *
 */
package com.jeespring.common.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

/**
 * http 工具类
 */
public final class HttpUtil {

    public static final String IS_SHOW_SEARCH_FORM = "isShowSearchForm";
    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";

    private HttpUtil() {
        //
    }

    public static String post(String requestUrl, String accessToken, String params) throws Exception {
        String contentType = "application/x-www-form-urlencoded";
        return post(requestUrl, accessToken, contentType, params);
    }

    public static String post(String requestUrl, String accessToken, String contentType, String params)
            throws Exception {
        String encoding = "UTF-8";
        if (requestUrl.contains("nlp")) {
            encoding = "GBK";
        }
        return post(requestUrl, accessToken, contentType, params, encoding);
    }

    public static String post(String requestUrl, String accessToken, String contentType, String params, String encoding)
            throws Exception {
        String url;
        if (StringUtils.isEmpty(accessToken)) {
            url = requestUrl;
        } else {
            url = requestUrl + "?access_token=" + accessToken;
        }
        return postGeneralUrl(url, contentType, params, encoding);
    }

    public static String postGeneralUrl(String generalUrl, String contentType, String params, String encoding)
            throws Exception {
        URL url = new URL(generalUrl);
        // 打开和URL之间的连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        // 设置通用的请求属性
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 得到请求的输出流对象
        try (DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
            out.write(params.getBytes(encoding));
            out.flush();
        }

        // 建立实际的连接
        connection.connect();
        // 遍历所有的响应头字段
        /*Map<String, List<String>> headers = connection.getHeaderFields();
        for (String key : headers.keySet()) {
            System.err.println(key + "--->" + headers.get(key));
        }*/
        // 定义 BufferedReader输入流来读取URL的响应
        StringBuilder result = new StringBuilder();

        int code = connection.getResponseCode();
        InputStream inputStream;
        if (code == HttpServletResponse.SC_OK) {
            inputStream = connection.getInputStream();
        } else {
            inputStream = connection.getErrorStream();
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, encoding))) {
            String getLine;
            while ((getLine = in.readLine()) != null) {
                result.append(getLine);
            }
        }

        return result.toString();
    }
}
