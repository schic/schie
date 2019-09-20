package com.schic.schie.modules.exchange.utils;

import com.jeespring.common.constant.Constants;
import com.jeespring.common.utils.HttpUtil;
import com.jeespring.common.utils.http.HttpUtils;
import com.schic.schie.modules.exchange.common.MyX509TrustManager;
import com.schic.schie.modules.exchange.common.NullHostNameVerifier;
import org.json.JSONObject;
import org.json.XML;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;

public class Httpsutils {
    public static JSONObject https(String Basic, String spec) throws Exception {
        HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
        TrustManager[] tm = { new MyX509TrustManager() };
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, tm, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        //多个的通道监管
        //    URL url = new URL("https://localhost:8443/api/channels/statuses");
        URL url = new URL(spec);
        //单个的通道监管
        //        URL url = new URL("https://localhost:8443/api/channels/cafd2b14-efce-4299-8246-7503c1cdbfad/status");
        // 打开restful链接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");// POST GET PUT DELETE
        // 设置访问提交模式，表单提交
        conn.setRequestProperty("Content-Type", "application/xml");
        //设置请求头添加参数 YWRtaW46YWRtaW4=
        conn.setRequestProperty("Authorization", "Basic " + Basic);
        conn.setConnectTimeout(130000);// 连接超时 单位毫秒
        conn.setReadTimeout(130000);// 读取超时 单位毫秒
        // 读取请求返回值
        BufferedReader in;
        in = new BufferedReader(new InputStreamReader(conn.getInputStream(), Constants.UTF8));
        String line;
        StringBuilder resultBuilder = new StringBuilder();
        while ((line = in.readLine()) != null) {
            resultBuilder.append(line);
        }
        // 结果输出
        //    System.out.println(resultBuilder.toString());
        JSONObject xmlJSONObj = XML.toJSONObject(resultBuilder.toString());
        //设置缩进
        //        String jsonPrettyPrintString = xmlJSONObj.toString(4);
        //        System.out.println(xmlJSONObj);
        return xmlJSONObj;
    }
}
