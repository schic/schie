package com.schic.schie.modules.resource.entity;

/*
查询参数json
 */
public class HttpQuery {

    private String qName; //名称
    private String qValue; //值


    public String getqName() {
        return qName;
    }

    public void setqName(String qName) {
        this.qName = qName;
    }

    public String getqValue() {
        return qValue;
    }

    public void setqValue(String qValue) {
        this.qValue = qValue;
    }
}
