package com.schic.schie.modules.resource.entity;

/*
出参表格
 */
public class HttpOutTable {

    private String name; //名称
    private String level; //权限等级
    private String remark; //说明
    private String math;  //取值公式

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMath() {
        return math;
    }

    public void setMath(String math) {
        this.math = math;
    }
}
