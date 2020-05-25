/**
 *
 */
package com.schic.schie.modules.onlinenode.entity;

import com.jeespring.common.persistence.AbstractBaseEntity;

import java.util.Date;

public class OnlineNode extends AbstractBaseEntity<OnlineNode> {

    private static final long serialVersionUID = 1L;

    //节点名称
    private String name;

    //节点任务执行正确数
    private Integer tasksOk;
    //节点任务执行错误数
    private Integer tasksErr;
    //节点交换量
    private Long exNums;
    //错误数
    private Integer errs;

    private Date beginCreateDate;        // 数据生成时间

    public OnlineNode() {
        //
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTasksOk() {
        return tasksOk;
    }

    public void setTasksOk(Integer tasksOk) {
        this.tasksOk = tasksOk;
    }

    public Integer getTasksErr() {
        return tasksErr;
    }

    public void setTasksErr(Integer tasksErr) {
        this.tasksErr = tasksErr;
    }

    public Long getExNums() {
        return exNums;
    }

    public Integer getErrs() {
        return errs;
    }

    public void setExNums(Long exNums) {
        this.exNums = exNums;
    }

    public void setErrs(Integer errs) {
        this.errs = errs;
    }

    public Date getBeginCreateDate() {
        return beginCreateDate;
    }

    public void setBeginCreateDate(Date beginCreateDate) {
        this.beginCreateDate = beginCreateDate;
    }

}
