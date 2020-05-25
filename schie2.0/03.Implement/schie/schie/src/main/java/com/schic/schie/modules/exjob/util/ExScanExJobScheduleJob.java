/**
 * 
 */
package com.schic.schie.modules.exjob.util;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.jeespring.common.spring.SpringUtils;
import com.schic.schie.modules.exjob.service.IExJobExecService;

/**
 * 
* <p>Title: ExScanExJobScheduleJob</p>  
* <p>Description: 定时任务</p>  
* @author caiwb 
* @date 2019年8月15日
 */
//不允许当前任务多次运行
@DisallowConcurrentExecution
public class ExScanExJobScheduleJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExScanExJobScheduleJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        long startTime = System.currentTimeMillis();

        try {
            // 执行任务
            IExJobExecService exJobExecService = SpringUtils.getBean(IExJobExecService.class);
            exJobExecService.startEngine();

            long times = System.currentTimeMillis() - startTime;

            LOGGER.debug("扫描任务执行结束 - 耗时：{} 毫秒", times);
        } catch (Exception e) {
            LOGGER.error("扫描任务执行异常：", e);
        }
    }
}
