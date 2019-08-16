package com.schic.schie.modules.resource.web;

public class test {
    public static void main(String[] args) {
        String str ="我的 ,你的,他的 ,大家的额";
        String str1 = str.substring(0,str.indexOf(","));
        int index = str.indexOf(",", str.indexOf(",")+1);
        System.out.println(index);
        String str2 = str.substring(str.indexOf(",")+1,index);
        String str3 = str.substring(index+1,str.lastIndexOf(","));
        String str4 = str.substring(str.lastIndexOf(",")+1);

        System.out.println(str1);
        System.out.println(str2);
        System.out.println(str3);
        System.out.println(str4);

    }




}
