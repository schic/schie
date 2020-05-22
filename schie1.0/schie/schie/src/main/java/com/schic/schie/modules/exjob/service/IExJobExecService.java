/**
 *
 */
package com.schic.schie.modules.exjob.service;

import com.schic.schie.modules.exjob.entity.ExJob;

import java.util.List;

public interface IExJobExecService {

    /**
     * 交换计划任务初始化
     */
    void init();

    /**
     * 交换任务结束
     */
    void destroy();

    /**
     * 更新job实体
     *
     * @param job
     */
    void updateJobDataMap(ExJob job);

    /**
     * 启动扫描任务
     */
    void startScanJob();

    /**
     * 执行扫描任务
     */
    void startEngine();

    /**
     * 立即执行任务
     *
     * @return
     */
    String runJob(String jobId);
}
