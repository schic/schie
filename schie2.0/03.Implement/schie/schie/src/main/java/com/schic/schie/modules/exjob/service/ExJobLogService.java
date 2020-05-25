/**
 * 
 */
package com.schic.schie.modules.exjob.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.redis.RedisUtils;
import com.jeespring.common.service.AbstractBaseService;
import com.schic.schie.modules.exjob.dao.ExJobLogDao;
import com.schic.schie.modules.exjob.entity.ExJobLog;

/**
 * 
* <p>Title: ExJobLogService</p>  
* <p>Description: 定时任务调度日志表Service</p>  
* @author caiwb 
* @date 2019年8月19日
 */
@Service
@Transactional(readOnly = true)
public class ExJobLogService extends AbstractBaseService<ExJobLogDao, ExJobLog> {

    /**
     * redis caches
     */
    @Autowired
    private RedisUtils redisUtils;

    public ExJobLogService() {
        //
    }

    public ExJobLog getCache(String id) {
        // 获取缓存数据
        ExJobLog exJobLog = (ExJobLog) redisUtils.get(RedisUtils.getIdKey(ExJobLogService.class.getName(), id));
        if (exJobLog != null) {
            return exJobLog;
        }
        // 获取数据库数据
        exJobLog = super.get(id);
        // 设置缓存数据
        redisUtils.set(RedisUtils.getIdKey(ExJobLogService.class.getName(), id), exJobLog);
        return exJobLog;
    }

    public List<ExJobLog> totalCache(ExJobLog exJobLog) {
        // 获取缓存数据
        String totalKey = RedisUtils.getTotalKey(ExJobLogService.class.getName(), JSON.toJSONString(exJobLog));
        @SuppressWarnings("unchecked")
        List<ExJobLog> exJobLogList = (List<ExJobLog>) redisUtils.get(totalKey);
        if (exJobLogList != null) {
            return exJobLogList;
        }
        // 获取数据库数据
        exJobLogList = super.total(exJobLog);
        // 设置缓存数据
        redisUtils.set(totalKey, exJobLogList);
        return exJobLogList;
    }

    public List<ExJobLog> findListCache(ExJobLog exJobLog) {
        // 获取缓存数据
        String findListKey = RedisUtils.getFindListKey(ExJobLogService.class.getName(), JSON.toJSONString(exJobLog));
        @SuppressWarnings("unchecked")
        List<ExJobLog> exJobLogList = (List<ExJobLog>) redisUtils.get(findListKey);
        if (exJobLogList != null) {
            return exJobLogList;
        }
        // 获取数据库数据
        exJobLogList = super.findList(exJobLog);
        // 设置缓存数据
        redisUtils.set(findListKey, exJobLogList);
        return exJobLogList;
    }

    public ExJobLog findListFirst(ExJobLog exJobLog) {
        // 获取数据库数据
        List<ExJobLog> exJobLogList = super.findList(exJobLog);
        if (!exJobLogList.isEmpty()) {
            return exJobLogList.get(0);
        }
        return exJobLog;
    }

    public ExJobLog findListFirstCache(ExJobLog exJobLog) {
        // 获取缓存数据
        String findListFirstKey = RedisUtils.getFindListFirstKey(ExJobLogService.class.getName(),
                JSON.toJSONString(exJobLog));
        ExJobLog exJobLogRedis = (ExJobLog) redisUtils.get(findListFirstKey);
        if (exJobLogRedis != null) {
            return exJobLogRedis;
        }
        // 获取数据库数据
        List<ExJobLog> exJobLogList = super.findList(exJobLog);
        ExJobLog exJobLogRetult;
        if (!exJobLogList.isEmpty()) {
            exJobLogRetult = exJobLogList.get(0);
        } else {
            exJobLogRetult = new ExJobLog();
        }
        // 设置缓存数据
        redisUtils.set(findListFirstKey, exJobLogRetult);
        return exJobLogRetult;
    }

    public Page<ExJobLog> findPageCache(Page<ExJobLog> page, ExJobLog exJobLog) {
        // 获取缓存数据
        String findPageKey = RedisUtils.getFindPageKey(ExJobLogService.class.getName(),
                JSON.toJSONString(page) + JSON.toJSONString(exJobLog));
        @SuppressWarnings("unchecked")
        Page<ExJobLog> pageReuslt = (Page<ExJobLog>) redisUtils.get(findPageKey);
        if (pageReuslt != null) {
            return pageReuslt;
        }
        // 获取数据库数据
        pageReuslt = super.findPage(page, exJobLog);
        // 设置缓存数据
        redisUtils.set(findPageKey, pageReuslt);
        return pageReuslt;
    }

    @Override
    @Transactional(readOnly = false)
    public void save(ExJobLog exJobLog) {
        // 保存数据库记录
        super.save(exJobLog);
        // 设置清除缓存数据
        redisUtils.remove(RedisUtils.getIdKey(ExJobLogService.class.getName(), exJobLog.getId()));
        // 清除列表和页面缓存数据
        redisUtils.removePattern(RedisUtils.getFindListKeyPattern(ExJobLogService.class.getName()));
        redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(ExJobLogService.class.getName()));
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ExJobLog exJobLog) {
        // 清除记录缓存数据
        redisUtils.remove(RedisUtils.getIdKey(ExJobLogService.class.getName(), exJobLog.getId()));
        // 删除数据库记录
        super.delete(exJobLog);
        // 清除列表和页面缓存数据
        redisUtils.removePattern(RedisUtils.getFindListKeyPattern(ExJobLogService.class.getName()));
        redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(ExJobLogService.class.getName()));
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteByLogic(ExJobLog exJobLog) {
        // 清除记录缓存数据
        redisUtils.remove(RedisUtils.getIdKey(ExJobLogService.class.getName(), exJobLog.getId()));
        // 逻辑删除数据库记录
        super.deleteByLogic(exJobLog);
        // 清除列表和页面缓存数据
        redisUtils.removePattern(RedisUtils.getFindListKeyPattern(ExJobLogService.class.getName()));
        redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(ExJobLogService.class.getName()));
    }

}
