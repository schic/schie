/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.exchange.service;

import com.alibaba.fastjson.JSON;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.redis.RedisUtils;
import com.jeespring.common.service.AbstractBaseService;
import com.schic.schie.modules.exchange.dao.ExDbStandardDao;
import com.schic.schie.modules.exchange.entity.ExDbStandard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//import com.alibaba.dubbo.config.annotation.Service;

/**
 * 数据表管理Service
 * @author XC
 * @version 2019-07-31
 */
//启用dubbo服务器时，要去掉下面注解
//com.alibaba.dubbo.config.annotation.Service(interfaceClass = ISysServerService.class,version = "1.0.0", timeout = 60000)
@Service
@Transactional(readOnly = true)
public class ExDbStandardService extends AbstractBaseService<ExDbStandardDao, ExDbStandard>
        implements IExDbStandardService {

    /**
     * redis caches
     */
    @Autowired
    private RedisUtils redisUtils;

    public ExDbStandard get(String id) {
        //获取数据库数据
        ExDbStandard exDbStandard = super.get(id);
        return exDbStandard;
    }

    public ExDbStandard getCache(String id) {
        //获取缓存数据
        ExDbStandard exDbStandard = (ExDbStandard) redisUtils
                .get(RedisUtils.getIdKey(ExDbStandardService.class.getName(), id));
        if (exDbStandard != null)
            return exDbStandard;
        //获取数据库数据
        exDbStandard = super.get(id);
        //设置缓存数据
        redisUtils.set(RedisUtils.getIdKey(ExDbStandardService.class.getName(), id), exDbStandard);
        return exDbStandard;
    }

    public List<ExDbStandard> total(ExDbStandard exDbStandard) {
        //获取数据库数据
        List<ExDbStandard> exDbStandardList = super.total(exDbStandard);
        return exDbStandardList;
    }

    public List<ExDbStandard> totalCache(ExDbStandard exDbStandard) {
        //获取缓存数据
        String totalKey = RedisUtils.getTotalKey(ExDbStandardService.class.getName(), JSON.toJSONString(exDbStandard));
        List<ExDbStandard> exDbStandardList = (List<ExDbStandard>) redisUtils.get(totalKey);
        if (exDbStandardList != null)
            return exDbStandardList;
        //获取数据库数据
        exDbStandardList = super.total(exDbStandard);
        //设置缓存数据
        redisUtils.set(totalKey, exDbStandardList);
        return exDbStandardList;
    }

    public List<ExDbStandard> findList(ExDbStandard exDbStandard) {
        //获取数据库数据
        List<ExDbStandard> exDbStandardList = super.findList(exDbStandard);
        //设置缓存数据
        return exDbStandardList;
    }

    public List<ExDbStandard> findListCache(ExDbStandard exDbStandard) {
        //获取缓存数据
        String findListKey = RedisUtils.getFindListKey(ExDbStandardService.class.getName(),
                JSON.toJSONString(exDbStandard));
        List<ExDbStandard> exDbStandardList = (List<ExDbStandard>) redisUtils.get(findListKey);
        if (exDbStandardList != null)
            return exDbStandardList;
        //获取数据库数据
        exDbStandardList = super.findList(exDbStandard);
        //设置缓存数据
        redisUtils.set(findListKey, exDbStandardList);
        return exDbStandardList;
    }

    public ExDbStandard findListFirst(ExDbStandard exDbStandard) {
        ;
        //获取数据库数据
        List<ExDbStandard> exDbStandardList = super.findList(exDbStandard);
        if (!exDbStandardList.isEmpty())
            exDbStandard = exDbStandardList.get(0);
        return exDbStandard;
    }

    public ExDbStandard findListFirstCache(ExDbStandard exDbStandard) {
        //获取缓存数据
        String findListFirstKey = RedisUtils.getFindListFirstKey(ExDbStandardService.class.getName(),
                JSON.toJSONString(exDbStandard));
        ExDbStandard exDbStandardRedis = (ExDbStandard) redisUtils.get(findListFirstKey);
        if (exDbStandardRedis != null)
            return exDbStandardRedis;
        //获取数据库数据
        List<ExDbStandard> exDbStandardList = super.findList(exDbStandard);
        if (!exDbStandardList.isEmpty())
            exDbStandard = exDbStandardList.get(0);
        else
            exDbStandard = new ExDbStandard();
        //设置缓存数据
        redisUtils.set(findListFirstKey, exDbStandard);
        return exDbStandard;
    }

    public Page<ExDbStandard> findPage(Page<ExDbStandard> page, ExDbStandard exDbStandard) {
        //获取数据库数据
        Page<ExDbStandard> pageReuslt = super.findPage(page, exDbStandard);
        return pageReuslt;
    }

    public Page<ExDbStandard> findPageCache(Page<ExDbStandard> page, ExDbStandard exDbStandard) {
        //获取缓存数据
        String findPageKey = RedisUtils.getFindPageKey(ExDbStandardService.class.getName(),
                JSON.toJSONString(page) + JSON.toJSONString(exDbStandard));
        Page<ExDbStandard> pageReuslt = (Page<ExDbStandard>) redisUtils.get(findPageKey);
        if (pageReuslt != null)
            return pageReuslt;
        //获取数据库数据
        pageReuslt = super.findPage(page, exDbStandard);
        //设置缓存数据
        redisUtils.set(findPageKey, pageReuslt);
        return pageReuslt;
    }

    @Transactional(readOnly = false)
    public void save(ExDbStandard exDbStandard) {
        //保存数据库记录
        super.save(exDbStandard);
        //设置清除缓存数据
        redisUtils.remove(RedisUtils.getIdKey(ExDbStandardService.class.getName(), exDbStandard.getId()));
        //清除列表和页面缓存数据
        redisUtils.removePattern(RedisUtils.getFindListKeyPattern(ExDbStandardService.class.getName()));
        redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(ExDbStandardService.class.getName()));
    }

    @Transactional(readOnly = false)
    public void delete(ExDbStandard exDbStandard) {
        //清除记录缓存数据
        redisUtils.remove(RedisUtils.getIdKey(ExDbStandardService.class.getName(), exDbStandard.getId()));
        //删除数据库记录
        super.delete(exDbStandard);
        //清除列表和页面缓存数据
        redisUtils.removePattern(RedisUtils.getFindListKeyPattern(ExDbStandardService.class.getName()));
        redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(ExDbStandardService.class.getName()));
    }

    @Transactional(readOnly = false)
    public void deleteByLogic(ExDbStandard exDbStandard) {
        //清除记录缓存数据
        redisUtils.remove(RedisUtils.getIdKey(ExDbStandardService.class.getName(), exDbStandard.getId()));
        //逻辑删除数据库记录
        super.deleteByLogic(exDbStandard);
        //清除列表和页面缓存数据
        redisUtils.removePattern(RedisUtils.getFindListKeyPattern(ExDbStandardService.class.getName()));
        redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(ExDbStandardService.class.getName()));
    }

}