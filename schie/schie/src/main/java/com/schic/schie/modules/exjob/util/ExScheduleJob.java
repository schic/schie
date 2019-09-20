/**
 * 
 */
package com.schic.schie.modules.exjob.util;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.jeespring.common.constant.Constants;
import com.jeespring.common.spring.SpringUtils;
import com.jeespring.common.utils.IdGen;
import com.jeespring.common.utils.StringUtils;
import com.schic.schie.modules.exjob.entity.ExJob;
import com.schic.schie.modules.exjob.entity.ExJobLog;
import com.schic.schie.modules.exjob.service.ExJobLogService;

/**
 * 
* <p>Title: ExScheduleJob</p>  
* <p>Description: 定时任务</p>  
* @author caiwb 
* @date 2019年8月15日
 */
//不允许当前任务多次运行
@DisallowConcurrentExecution
public class ExScheduleJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExScheduleJob.class);

    private ExecutorService service = Executors.newSingleThreadExecutor();

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        ExJob job = (ExJob) context.getMergedJobDataMap().get(ExScheduleConst.EXJOB_PROPERTIES);

        ExJobLogService jobLogService = SpringUtils.getBean(ExJobLogService.class);

        ExJobLog jobLog = new ExJobLog();
        jobLog.setIsNewRecord(true);
        jobLog.setId(IdGen.uuid());
        jobLog.setJobName(job.getJobName());
        jobLog.setResId(job.getRes().getId());
        jobLog.setResAskId(job.getResAsk().getId());
        jobLog.setCreateDate(new Date());

        long startTime = System.currentTimeMillis();

        try {
            // 执行任务
            LOGGER.info("任务开始执行 - 名称：{} ", jobLog.getJobName());
            ExScheduleCallable task = new ExScheduleCallable(job);
            Future<?> future = service.submit(task);
            String result = future.get().toString();
            long times = System.currentTimeMillis() - startTime;
            jobLog.setCostTime(times);
            if (StringUtils.isNotEmpty(result)) {
                jobLog.setStatus(Constants.FAIL);
                jobLog.setExceptionInfo(result);
                return;
            }

            // 任务状态 0：成功 1：失败
            jobLog.setStatus(Constants.SUCCESS);

            LOGGER.info("任务执行结束 - 名称：{} 耗时：{} 毫秒", jobLog.getJobName(), times);
        } catch (Exception e) {
            LOGGER.error("任务执行异常  - 名称：{} ：", jobLog.getJobName(), e);
            long times = System.currentTimeMillis() - startTime;
            jobLog.setCostTime(times);
            // 任务状态 0：成功 1：失败
            jobLog.setStatus(Constants.FAIL);
            jobLog.setExceptionInfo(e.toString());
        } finally {
            jobLogService.save(jobLog);
        }
    }
}
