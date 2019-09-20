/**
 * 
 */
package com.schic.schie.modules.exjob.service;

import com.schic.schie.modules.exjob.entity.ExJob;

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
}
