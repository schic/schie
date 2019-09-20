/**
 * 
 */
package com.schic.schie.modules.exjob.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jeespring.common.spring.SpringUtils;
import com.jeespring.common.utils.IdGen;
import com.schic.schie.modules.exjob.entity.ExJob;
import com.schic.schie.modules.exjob.util.ExScheduleUtils;

@Service
public class ExJobExecService implements IExJobExecService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExJobExecService.class);

    private static final int SLEEP_TIMES = 1000;

    private static final int INTERVAL = 60;

    private static Map<String, String> mapJob = new HashMap<>();

    @Autowired
    private Scheduler scheduler;

    @Value("${exjob.isend}")
    private String isEnd;

    @Value("${exjob.scanCron}")
    private String scanCron;

    public ExJobExecService() {
        //
    }

    @Override
    public void init() {
        LOGGER.debug("------------------交换任务执行引擎初始化");
        // 启动线程，每隔60秒从表中获取需要执行的任务，替换更新计划任务
        new Thread() {
            @Override
            public void run() {
                long i = 0;
                while (Thread.currentThread().isInterrupted() || !"true".equals(isEnd)) {
                    if (i % INTERVAL == 0) {
                        LOGGER.debug("------------------交换任务执行引擎开始");
                        startEngine();
                        break;
                    }
                    i++;
                    try {
                        Thread.sleep(SLEEP_TIMES);
                    } catch (InterruptedException e) {
                        LOGGER.warn("Interrupted!", e);
                        Thread.currentThread().interrupt();
                    }
                }
                LOGGER.debug("------------------交换任务执行引擎结束");
            }
        }.start();

    }

    @Override
    public void startScanJob() {
        ExScheduleUtils.createScanExJobScheduleJob(scheduler, scanCron);
    }

    @Override
    public void startEngine() {
        // 扫描任务
        List<ExJob> jobs = scanExJob();
        // 更新到计划任务
        updateJob(jobs);
    }

    private static List<ExJob> scanExJob() {
        ExJobService exJobService = SpringUtils.getBean(ExJobService.class);
        return exJobService.findAllList(null);
    }

    private void updateJob(List<ExJob> jobs) {
        //本次的标识
        String flags = IdGen.uuid();

        for (ExJob job : jobs) {
            CronTrigger cronTrigger = ExScheduleUtils.getCronTrigger(scheduler, job.getId());
            if (cronTrigger == null) {
                ExScheduleUtils.createScheduleJob(scheduler, job);
            } else {
                ExScheduleUtils.updateScheduleJob(scheduler, job);
            }
            mapJob.put(job.getId(), flags);
        }

        //把上轮有，本轮没有的任务删除
        Iterator<Entry<String, String>> iterator = mapJob.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, String> entry = iterator.next();
            if (!flags.equals(entry.getValue())) {
                iterator.remove();
                ExScheduleUtils.deleteScheduleJob(scheduler, entry.getKey());
            }
        }
    }

    /**
     * 更新定时任务
     */
    @Override
    public void updateJobDataMap(ExJob job) {
        ExScheduleUtils.updateScheduleJobDataMap(scheduler, job);
    }

    @Override
    public void destroy() {
        isEnd = "true";
    }

}
