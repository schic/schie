/**
 * 
 */
package com.jeespring.modules.scheduling;

//import javax.jms.Destination;
import java.text.SimpleDateFormat;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Service("jeeSpringScheduledTaskService")
@Component
public class JeeSpringScheduledTaskService {

    // @Autowired
    // private XXXService xxxxService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // @Autowired
    // private JeeSpringProducer jeeSpringProducer;

    // 每个10s执行一次任务
    @Scheduled(fixedRate = 600000)
    public void run() {
        // xxxxService.method();
        // System.out.println(dateFormat.format(new Date()) + " | " +
        // "com.jeespring.modules.scheduling:每隔60s执行一次任务");
    }

    // 每个2s执行一次任务#朝九晚七工作时间内每五分钟
    @Scheduled(cron = "0 0/10 9-19 * * ?")
    public void run2() {
        // System.out.println(dateFormat.format(new Date()) + " | " +
        // "com.jeespring.modules.scheduling:朝九晚七工作时间内每十分钟执行一次任务");
        // jeeSpringProducer.sendMessageA( "ActiveMQ JeeSpringProducer queueA。");
        // jeeSpringProducer.sendMessageB("ActiveMQ JeeSpringProducer queueB。");
    }

    // 每天15点29分执行该任务
    @Scheduled(cron = "0 29 15 ? * *")
    public void run3() {
        // System.out.println(dateFormat.format(new Date()) + " | " +
        // "com.jeespring.modules.scheduling:每天在指定时间执行任务");
    }
}