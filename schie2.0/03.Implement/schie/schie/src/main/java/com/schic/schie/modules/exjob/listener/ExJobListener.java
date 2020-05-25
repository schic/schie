/**
 * 
 */
package com.schic.schie.modules.exjob.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.jeespring.common.config.Global;
import com.jeespring.common.spring.SpringUtils;
import com.schic.schie.modules.exjob.service.IExJobExecService;

@WebListener
public class ExJobListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        if (!"true".equals(Global.getConfig("exjob.enabled"))) {
            return;
        }
        IExJobExecService exJobExecService = SpringUtils.getBean(IExJobExecService.class);
        exJobExecService.startScanJob();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        IExJobExecService exJobExecService = SpringUtils.getBean(IExJobExecService.class);
        exJobExecService.destroy();
    }

}
