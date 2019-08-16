/**
 * 
 */
package com.schic.schie.modules.exjob;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.jeespring.common.config.Global;

@WebListener
public class ExJobListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        if (!"true".equals(Global.getConfig("exjob.enabled"))) {
            return;
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //
    }

}
