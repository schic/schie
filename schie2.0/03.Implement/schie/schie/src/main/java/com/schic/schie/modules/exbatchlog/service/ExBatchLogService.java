/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.exbatchlog.service;

import com.alibaba.fastjson.JSON;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.redis.RedisUtils;
import com.jeespring.common.service.AbstractBaseService;
import com.schic.schie.modules.exbatchlog.dao.ExBatchLogDao;
import com.schic.schie.modules.exbatchlog.entity.ExBatchLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//import com.alibaba.dubbo.config.annotation.Service;

/**
 * 保存日志Service
 *
 * @author leodeyang
 * @version 2019-08-09
 */
//启用dubbo服务器时，要去掉下面注解
//com.alibaba.dubbo.config.annotation.Service(interfaceClass = ISysServerService.class,version = "1.0.0", timeout = 60000)
@Service
@Transactional(readOnly = true)
public class ExBatchLogService extends AbstractBaseService<ExBatchLogDao, ExBatchLog> implements IExBatchLogService {

    /**
     * redis caches
     */
    @Autowired
    private RedisUtils redisUtils;

    public ExBatchLog get(String id) {
        //获取数据库数据
        ExBatchLog exBatchLog = super.get(id);
        return exBatchLog;
    }

    public ExBatchLog getCache(String id) {
        //获取缓存数据
        ExBatchLog exBatchLog = (ExBatchLog) redisUtils.get(RedisUtils.getIdKey(ExBatchLogService.class.getName(), id));
        if (exBatchLog != null) {
            return exBatchLog;
        }
        //获取数据库数据
        exBatchLog = super.get(id);
        //设置缓存数据
        redisUtils.set(RedisUtils.getIdKey(ExBatchLogService.class.getName(), id), exBatchLog);
        return exBatchLog;
    }

    public List<ExBatchLog> total(ExBatchLog exBatchLog) {
        //获取数据库数据
        List<ExBatchLog> exBatchLogList = super.total(exBatchLog);
        return exBatchLogList;
    }

    public List<ExBatchLog> totalCache(ExBatchLog exBatchLog) {
        //获取缓存数据
        String totalKey = RedisUtils.getTotalKey(ExBatchLogService.class.getName(), JSON.toJSONString(exBatchLog));
        List<ExBatchLog> exBatchLogList = (List<ExBatchLog>) redisUtils.get(totalKey);
        if (exBatchLogList != null) {
            return exBatchLogList;
        }
        //获取数据库数据
        exBatchLogList = super.total(exBatchLog);
        //设置缓存数据
        redisUtils.set(totalKey, exBatchLogList);
        return exBatchLogList;
    }

    public List<ExBatchLog> findList(ExBatchLog exBatchLog) {
        //获取数据库数据
        List<ExBatchLog> exBatchLogList = super.findList(exBatchLog);
        //设置缓存数据
        return exBatchLogList;
    }

    public List<ExBatchLog> findListCache(ExBatchLog exBatchLog) {
        //获取缓存数据
        String findListKey = RedisUtils.getFindListKey(ExBatchLogService.class.getName(),
                JSON.toJSONString(exBatchLog));
        List<ExBatchLog> exBatchLogList = (List<ExBatchLog>) redisUtils.get(findListKey);
        if (exBatchLogList != null) {
            return exBatchLogList;
        }
        //获取数据库数据
        exBatchLogList = super.findList(exBatchLog);
        //设置缓存数据
        redisUtils.set(findListKey, exBatchLogList);
        return exBatchLogList;
    }

    public ExBatchLog findListFirst(ExBatchLog exBatchLog) {
        ;
        //获取数据库数据
        List<ExBatchLog> exBatchLogList = super.findList(exBatchLog);
        if (!exBatchLogList.isEmpty()) {
            exBatchLog = exBatchLogList.get(0);
        }
        return exBatchLog;
    }

    public ExBatchLog findListFirstCache(ExBatchLog exBatchLog) {
        //获取缓存数据
        String findListFirstKey = RedisUtils.getFindListFirstKey(ExBatchLogService.class.getName(),
                JSON.toJSONString(exBatchLog));
        ExBatchLog exBatchLogRedis = (ExBatchLog) redisUtils.get(findListFirstKey);
        if (exBatchLogRedis != null) {
            return exBatchLogRedis;
        }
        //获取数据库数据
        List<ExBatchLog> exBatchLogList = super.findList(exBatchLog);
        if (!exBatchLogList.isEmpty()) {
            exBatchLog = exBatchLogList.get(0);
        } else {
            exBatchLog = new ExBatchLog();
        }
        //设置缓存数据
        redisUtils.set(findListFirstKey, exBatchLog);
        return exBatchLog;
    }

    public Page<ExBatchLog> findPage(Page<ExBatchLog> page, ExBatchLog exBatchLog) {
        //获取数据库数据
        Page<ExBatchLog> pageReuslt = super.findPage(page, exBatchLog);
        return pageReuslt;
    }

    public Page<ExBatchLog> findPageCache(Page<ExBatchLog> page, ExBatchLog exBatchLog) {
        //获取缓存数据
        String findPageKey = RedisUtils.getFindPageKey(ExBatchLogService.class.getName(),
                JSON.toJSONString(page) + JSON.toJSONString(exBatchLog));
        Page<ExBatchLog> pageReuslt = (Page<ExBatchLog>) redisUtils.get(findPageKey);
        if (pageReuslt != null) {
            return pageReuslt;
        }
        //获取数据库数据
        pageReuslt = super.findPage(page, exBatchLog);
        //设置缓存数据
        redisUtils.set(findPageKey, pageReuslt);
        return pageReuslt;
    }

    @Transactional(readOnly = false)
    public void save(ExBatchLog exBatchLog) {
        //保存数据库记录
        super.save(exBatchLog);
        //设置清除缓存数据
        redisUtils.remove(RedisUtils.getIdKey(ExBatchLogService.class.getName(), exBatchLog.getId()));
        //清除列表和页面缓存数据
        redisUtils.removePattern(RedisUtils.getFindListKeyPattern(ExBatchLogService.class.getName()));
        redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(ExBatchLogService.class.getName()));
    }

    @Transactional(readOnly = false)
    public void delete(ExBatchLog exBatchLog) {
        //清除记录缓存数据
        redisUtils.remove(RedisUtils.getIdKey(ExBatchLogService.class.getName(), exBatchLog.getId()));
        //删除数据库记录
        super.delete(exBatchLog);
        //清除列表和页面缓存数据
        redisUtils.removePattern(RedisUtils.getFindListKeyPattern(ExBatchLogService.class.getName()));
        redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(ExBatchLogService.class.getName()));
    }

    @Transactional(readOnly = false)
    public void deleteByLogic(ExBatchLog exBatchLog) {
        //清除记录缓存数据
        redisUtils.remove(RedisUtils.getIdKey(ExBatchLogService.class.getName(), exBatchLog.getId()));
        //逻辑删除数据库记录
        super.deleteByLogic(exBatchLog);
        //清除列表和页面缓存数据
        redisUtils.removePattern(RedisUtils.getFindListKeyPattern(ExBatchLogService.class.getName()));
        redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(ExBatchLogService.class.getName()));
    }

}
