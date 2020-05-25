/**
 *
 */
package com.schic.schie.modules.exchange.utils;

import com.jeespring.common.constant.Constants;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

public class Base64Demo {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String DATA = "admin:admin";
        byte[] data = Base64.encodeBase64(DATA.getBytes(Constants.UTF8));
        System.out.println("BASE64加密：" + new String(data, Constants.UTF8));

        // BASE64解密
        byte[] bytes = Base64.decodeBase64(data);
        System.out.println("BASE64解密：" + new String(bytes, Constants.UTF8));
    }
}
