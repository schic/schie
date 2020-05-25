/**
 *
 */
package com.schic.schie.modules.home.aspect;

import com.schic.schie.modules.exjob.entity.ExJobLog;
import com.schic.schie.modules.home.HomeManager;
import com.schic.schie.modules.utils.AspectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class ExJobLogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchLogAspect.class);

    @Around("execution(* com.schic.schie.modules.exjob.service.ExJobLogService.save(..))")
    public void save(ProceedingJoinPoint point) throws Throwable {
        Map<String, Object> map = null;
        try {
            map = AspectUtils.listParams(point);
        } catch (Exception e) {
            LOGGER.error("批量数据调度日志切面获取参数失败", e);
        }
        point.proceed();

        if (map != null && map.size() > 0 && map.values().toArray()[0].getClass().equals(ExJobLog.class)) {
            HomeManager.addExJobLog((ExJobLog) map.values().toArray()[0]);
        }
    }
}
