/**
 *
 */
package com.schic.schie.modules.exjob.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.jeespring.common.persistence.AbstractBaseEntity;
import com.jeespring.common.utils.excel.annotation.ExcelField;
import com.jeespring.modules.sys.utils.DictUtils;

/**
 * <p>Title: ExJobLog</p>
 * <p>Description: 定时任务调度日志表Entity</p>
 *
 * @author caiwb
 * @date 2019年8月19日
 */
public class ExJobLog extends AbstractBaseEntity<ExJobLog> {

    private static final long serialVersionUID = 1L;

    public static final String STATUS_OK = "0";
    public static final String STATUS_ERR = "1";

    private String jobName; // 任务名称
    private String resId;//资源id
    private String resNodeId;//资源节点id
    private String resAskId;//资源申请id
    private String status; // 执行状态（0正常 1失败）
    private String exceptionInfo; // 异常信息
    private java.util.Date beginCreateDate; // 开始 创建时间
    private java.util.Date endCreateDate; // 结束 创建时间
    private Long costTime;

    public ExJobLog() {
        super();
    }

    public ExJobLog(String id) {
        super(id);
    }

    @Length(min = 1, max = 256, message = "任务名称长度必须介于 1 和256 之间")
    @ExcelField(title = "任务名称", align = 2, sort = 1)
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Length(min = 0, max = 1, message = "执行状态（0正常 1失败）长度必须介于 0 和 1 之间")
    @ExcelField(title = "执行状态（0正常 1失败）", dictType = "job_status", align = 2, sort = 6)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusLabel() {
        return DictUtils.getDictLabel(status, "job_status", "");
    }

    public String getStatusPicture() {
        return DictUtils.getDictPicture(status, "job_status", "");
    }

    @ExcelField(title = "异常信息", align = 2, sort = 7)
    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

    public Date getBeginCreateDate() {
        return beginCreateDate;
    }

    public void setBeginCreateDate(Date beginCreateDate) {
        this.beginCreateDate = beginCreateDate;
    }

    public Date getEndCreateDate() {
        return endCreateDate;
    }

    public void setEndCreateDate(Date endCreateDate) {
        this.endCreateDate = endCreateDate;
    }

    /**
     * @return the resId
     */
    public String getResId() {
        return resId;
    }

    /**
     * @param resId the resId to set
     */
    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResNodeId() {
        return resNodeId;
    }

    public void setResNodeId(String resNodeId) {
        this.resNodeId = resNodeId;
    }

    /**
     * @return the resAskId
     */
    public String getResAskId() {
        return resAskId;
    }

    /**
     * @param resAskId the resAskId to set
     */
    public void setResAskId(String resAskId) {
        this.resAskId = resAskId;
    }

    /**
     * @return the costTime
     */
    public Long getCostTime() {
        return costTime;
    }

    /**
     * @param costTime the costTime to set
     */
    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }

}
