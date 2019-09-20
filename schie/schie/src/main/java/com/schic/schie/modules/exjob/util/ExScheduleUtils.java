/**
 * 
 */
package com.schic.schie.modules.exjob.util;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeespring.common.utils.StringUtils;
import com.schic.schie.modules.exjob.entity.ExJob;

/**
 * 定时任务工具类
 * 
 * @author JeeSpring
 *
 */
public final class ExScheduleUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExScheduleUtils.class);

    private ExScheduleUtils() {
        //
    }

    /**
     * 获取触发器key
     */
    private static TriggerKey getTriggerKey(String jobId) {
        return TriggerKey.triggerKey(ExScheduleConst.EXJOB_CLASS_NAME + jobId);
    }

    /**
     * 获取jobKey
     */
    private static JobKey getJobKey(String jobId) {
        return JobKey.jobKey(ExScheduleConst.EXJOB_CLASS_NAME + jobId);
    }

    /**
     * 获取表达式触发器
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler, String jobId) {
        try {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(jobId));
        } catch (SchedulerException e) {
            LOGGER.error("getCronTrigger 异常：", e);
        }
        return null;
    }

    /**
     * 创建定时任务
     */
    public static void createScheduleJob(Scheduler scheduler, ExJob job) {
        try {
            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(ExScheduleJob.class).withIdentity(getJobKey(job.getId())).build();

            // 表达式调度构建器
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(job.getId()))
                    .withSchedule(cronScheduleBuilder).build();

            // 放入参数，运行时的方法可以获取
            jobDetail.getJobDataMap().put(ExScheduleConst.EXJOB_PROPERTIES, job);

            scheduler.scheduleJob(jobDetail, trigger);

        } catch (SchedulerException e) {
            LOGGER.error("createScheduleJob 异常：", e);
        }
    }

    /**
     * 创建Exjob任务扫描定时任务
     */
    public static void createScanExJobScheduleJob(Scheduler scheduler, String scanCron) {
        try {

            String cron = scanCron;
            if (StringUtils.isEmpty(cron) || cron.contains("$")) {
                cron = "0 0/1 * * * ? *";
            }

            // 表达式调度构建器
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            //0 0/1 * * * ? *    ，每分钟的0秒执行一次

            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(getTriggerKey(ExScheduleConst.EXJOB_SCANJOBID)).withSchedule(cronScheduleBuilder)
                    .build();

            CronTrigger cronTrigger = getCronTrigger(scheduler, ExScheduleConst.EXJOB_SCANJOBID);
            if (cronTrigger == null) {
                // 构建job信息
                JobDetail jobDetail = JobBuilder.newJob(ExScanExJobScheduleJob.class)
                        .withIdentity(getJobKey(ExScheduleConst.EXJOB_SCANJOBID)).build();
                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                TriggerKey triggerKey = getTriggerKey(ExScheduleConst.EXJOB_SCANJOBID);
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (SchedulerException e) {
            LOGGER.error("createScheduleJob 异常：", e);
        }
    }

    /**
     * 更新定时任务
     */
    public static void updateScheduleJob(Scheduler scheduler, ExJob job) {
        try {

            CronTrigger trigger = getCronTrigger(scheduler, job.getId());
            if (trigger == null) {
                throw new RuntimeException("获取cronTrigger失败");
            }

            ExJob oldJob = (ExJob) trigger.getJobDataMap().get(ExScheduleConst.EXJOB_PROPERTIES);
            if (job.equals(oldJob)) {
                return;
            }

            TriggerKey triggerKey = getTriggerKey(job.getId());

            // 表达式调度构建器
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();

            // 参数
            trigger.getJobDataMap().put(ExScheduleConst.EXJOB_PROPERTIES, job);

            scheduler.rescheduleJob(triggerKey, trigger);

            LOGGER.debug("更新计划任务：{}", job.getJobName());

        } catch (SchedulerException e) {
            LOGGER.error("SchedulerException 异常：", e);
        }
    }

    /**
     * 更新定时任务
     */
    public static void updateScheduleJobDataMap(Scheduler scheduler, ExJob job) {
        CronTrigger trigger = getCronTrigger(scheduler, job.getId());
        if (trigger == null) {
            throw new RuntimeException("获取cronTrigger失败");
        }

        // 参数
        trigger.getJobDataMap().put(ExScheduleConst.EXJOB_PROPERTIES, job);
    }

    /**
     * 立即执行任务
     */
    public static int run(Scheduler scheduler, ExJob job) {
        try {
            // 参数
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(ExScheduleConst.EXJOB_PROPERTIES, job);

            scheduler.triggerJob(getJobKey(job.getId()), dataMap);
            return 1;
        } catch (SchedulerException e) {
            LOGGER.error("run 异常：", e.getMessage());
        }
        return 0;
    }

    /**
     * 暂停任务
     */
    public static void pauseJob(Scheduler scheduler, String jobId) {
        try {
            scheduler.pauseJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            LOGGER.error("pauseJob 异常：", e);
        }
    }

    /**
     * 恢复任务
     */
    public static void resumeJob(Scheduler scheduler, String jobId) {
        try {
            scheduler.resumeJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            LOGGER.error("resumeJob 异常：", e);
        }
    }

    /**
     * 删除定时任务
     */
    public static void deleteScheduleJob(Scheduler scheduler, String jobId) {
        try {
            scheduler.deleteJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            LOGGER.error("deleteScheduleJob 异常：", e);
        }
    }
}
