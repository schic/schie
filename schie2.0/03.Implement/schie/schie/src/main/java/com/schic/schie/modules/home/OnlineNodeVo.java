/**
 *
 */
package com.schic.schie.modules.home;

import com.jeespring.common.persistence.AbstractBaseEntity;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OnlineNodeVo {

    private static final long serialVersionUID = 1L;

    //节点Id
    private String id;
    //节点名称
    private String name;
    //节点任务数
    private AtomicInteger tasks = new AtomicInteger(0);
    //任务执行正确数
    private AtomicInteger tasksOk = new AtomicInteger(0);
    //任务执行错误数
    private AtomicInteger tasksErr = new AtomicInteger(0);
    //节点交换量
    private AtomicLong exNums = new AtomicLong(0);
    //节点在线状态
    private AtomicBoolean online = new AtomicBoolean(false);
    //错误数
    private AtomicInteger errs = new AtomicInteger(0);
    //图片
    private String picName;
    //背景颜色
    private String bgColor;
    //删除标志
    private AtomicBoolean deleted = new AtomicBoolean(false);
    //节点变化标志
    private AtomicBoolean changed = new AtomicBoolean(false);

    public OnlineNodeVo() {
        //
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AtomicInteger getTasks() {
        return tasks;
    }

    public AtomicInteger getTasksOk() {
        return tasksOk;
    }

    public AtomicInteger getTasksErr() {
        return tasksErr;
    }

    public AtomicLong getExNums() {
        return exNums;
    }

    public AtomicInteger getErrs() {
        return errs;
    }

    public AtomicBoolean getOnline() {
        return online;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public AtomicBoolean getDeleted() {
        return deleted;
    }

    public AtomicBoolean getChanged() {
        return changed;
    }

}
