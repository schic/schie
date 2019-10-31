/**
 *
 */
package com.schic.schie.modules.resource.entity;


/**
 * 订阅JSON
 */
public class SubJson {

    private String batch;   //批处理数
    private String dateText;  //增量日期字段
    private String days;  //数据增量天数
    private String key;  //主键
    private String cron;  //cron表达式


    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getDateText() {
        return dateText;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
}
