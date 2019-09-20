/**
 * 
 */
package com.schic.schie.modules.resource.entity;

import java.io.Serializable;

/*
数据资源出参
 */
public class OutJson implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1565811912670259631L;

    private String oName; //名称
    private String oRemark; //类型
    private String oLevel; //权限等级

    public OutJson() {
    }

    public OutJson(String oName, String oRemark, String oLevel) {
        this.oName = oName;
        this.oRemark = oRemark;
        this.oLevel = oLevel;
    }

    public String getoName() {
        return oName;
    }

    public void setoName(String oName) {
        this.oName = oName;
    }

    public String getoRemark() {
        return oRemark;
    }

    public void setoRemark(String oRemark) {
        this.oRemark = oRemark;
    }

    public String getoLevel() {
        return oLevel;
    }

    public void setoLevel(String oLevel) {
        this.oLevel = oLevel;
    }
}
