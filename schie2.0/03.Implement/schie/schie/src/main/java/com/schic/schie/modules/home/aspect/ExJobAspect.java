/**
 *
 */
package com.schic.schie.modules.home.aspect;

import com.schic.schie.modules.exjob.entity.ExJob;
import com.schic.schie.modules.home.HomeManager;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class ExJobAspect {

    @AfterReturning(pointcut = "execution(* com.schic.schie.modules.exjob.service.ExJobService.findAllList(..))", returning = "object")
    public void doAfterReturning(Object object) {
        List<ExJob> list = (List<ExJob>) object;
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            ExJob exJob = list.get(i);
            String nodeid = exJob.getRes().getNodeId();
            if (map.containsKey(nodeid)) {
                map.put(nodeid, map.get(nodeid) + 1);
            } else {
                map.put(nodeid, 1);
            }
        }

        if (map.isEmpty()) {
            return;
        }
        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            HomeManager.updateTasks(entry.getKey(), entry.getValue());
        }
    }
}
