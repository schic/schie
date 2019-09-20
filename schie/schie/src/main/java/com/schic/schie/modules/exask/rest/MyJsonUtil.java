package com.schic.schie.modules.exask.rest;


import com.alibaba.fastjson.JSONObject;

public class MyJsonUtil {
    public static String getString(JSONObject srcdata, String key){
        if (srcdata.containsKey(key)) {
            return  srcdata.getString(key);
        }else {
            return null;
        }
    }
    public static Long getLong(JSONObject srcdata,String key){
        if(srcdata.containsKey(key)){
            return  srcdata.getLong(key);
        }else {
            return null;
        }
    }
    public static Integer getint(JSONObject srcdata,String key){
        if(srcdata.containsKey(key)){
            return  srcdata.getInteger(key);
        }else {
            return null;
        }
    }

}
