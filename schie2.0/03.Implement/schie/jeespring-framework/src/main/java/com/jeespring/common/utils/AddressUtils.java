/**
 * 
 */
package com.jeespring.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.jeespring.common.utils.http.HttpUtils;

/**
 * 获取地址类
 * 
 * @author JeeSpring
 */
public class AddressUtils {
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    public static final String IP_URL = "http://ip.taobao.com/service/getIpInfo.php";
    /** 获取地址开关 */
    private static boolean addressEnabled = false;

    public static String getRealAddressByIP(String ip) {
        String address = "";
        try {
            if (addressEnabled) {
                address = HttpUtils.sendPost(IP_URL, "ip=" + ip);
                JSONObject json = JSONObject.parseObject(address);
                JSONObject object = json.getObject("data", JSONObject.class);
                String region = object.getString("region");
                String city = object.getString("city");
                address = region + " " + city;
            }
        } catch (Exception e) {
            log.error("获取地理位置异常:", e.getMessage());
        }
        return address;
    }
}
