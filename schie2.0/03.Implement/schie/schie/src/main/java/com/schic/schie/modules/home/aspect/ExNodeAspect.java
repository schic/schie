/**
 *
 */
package com.schic.schie.modules.home.aspect;

import com.schic.schie.modules.home.HomeManager;
import com.schic.schie.modules.nodes.entity.ExNode;
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
public class ExNodeAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchLogAspect.class);

    @Around("execution(* com.schic.schie.modules.nodes.service.ExNodeService.save(..))")
    public void save(ProceedingJoinPoint point) throws Throwable {
        Map<String, Object> map = null;
        try {
            map = AspectUtils.listParams(point);
        } catch (Exception e) {
            LOGGER.error("交换节点保存切面获取参数失败", e);
        }
        point.proceed();

        if (map != null && map.size() > 0 && map.values().toArray()[0].getClass().equals(ExNode.class)) {
            HomeManager.updateNode((ExNode) map.values().toArray()[0]);
        }
    }

    @Around("execution(* com.schic.schie.modules.nodes.service.ExNodeService.delete*(..))")
    public void delete(ProceedingJoinPoint point) throws Throwable {
        Map<String, Object> map = null;
        try {
            map = AspectUtils.listParams(point);
        } catch (Exception e) {
            LOGGER.error("删除交换节点切面获取参数失败", e);
        }
        point.proceed();

        if (map != null && map.size() > 0 && map.values().toArray()[0].getClass().equals(ExNode.class)) {
            HomeManager.delNode((ExNode) map.values().toArray()[0]);
        }
    }
}
