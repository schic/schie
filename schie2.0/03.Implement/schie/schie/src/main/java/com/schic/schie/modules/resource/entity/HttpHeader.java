/**
 *
 */
package com.schic.schie.modules.resource.entity;

/**
 * 头信息json
 */
public class HttpHeader {
    private String hName;   //名称
    private String hValue;  //值

    public String gethName() {
        return hName;
    }

    public void sethName(String hName) {
        this.hName = hName;
    }

    public String gethValue() {
        return hValue;
    }

    public void sethValue(String hValue) {
        this.hValue = hValue;
    }
}
