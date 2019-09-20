/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.resource.service;

import com.alibaba.fastjson.JSON;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.redis.RedisUtils;
import com.jeespring.common.service.AbstractBaseService;
import com.schic.schie.modules.resource.dao.ExResourcesDao;
import com.schic.schie.modules.resource.entity.ExResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//import com.alibaba.dubbo.config.annotation.Service;

/**
 * 资源管理Service
 * @author daihp
 * @version 2019-07-24
 */
//启用dubbo服务器时，要去掉下面注解
//com.alibaba.dubbo.config.annotation.Service(interfaceClass = ISysServerService.class,version = "1.0.0", timeout = 60000)
@org.springframework.stereotype.Service
@Transactional(readOnly = true)
public class ExResourcesService extends AbstractBaseService<ExResourcesDao, ExResources>
        implements IExResourcesService {

    /**
     * redis caches
     */
    @Autowired
    private RedisUtils redisUtils;

    public ExResources get(String id) {
        //获取数据库数据
        ExResources exResources = super.get(id);
        return exResources;
    }

    public ExResources getCache(String id) {
        //获取缓存数据
        ExResources exResources = (ExResources) redisUtils
                .get(RedisUtils.getIdKey(ExResourcesService.class.getName(), id));
        if (exResources != null)
            return exResources;
        //获取数据库数据
        exResources = super.get(id);
        //设置缓存数据
        redisUtils.set(RedisUtils.getIdKey(ExResourcesService.class.getName(), id), exResources);
        return exResources;
    }

    public List<ExResources> total(ExResources exResources) {
        //获取数据库数据
        List<ExResources> exResourcesList = super.total(exResources);
        return exResourcesList;
    }

    public List<ExResources> totalCache(ExResources exResources) {
        //获取缓存数据
        String totalKey = RedisUtils.getTotalKey(ExResourcesService.class.getName(), JSON.toJSONString(exResources));
        List<ExResources> exResourcesList = (List<ExResources>) redisUtils.get(totalKey);
        if (exResourcesList != null)
            return exResourcesList;
        //获取数据库数据
        exResourcesList = super.total(exResources);
        //设置缓存数据
        redisUtils.set(totalKey, exResourcesList);
        return exResourcesList;
    }

    public List<ExResources> findList(ExResources exResources) {
        //获取数据库数据
        List<ExResources> exResourcesList = super.findList(exResources);
        //设置缓存数据
        return exResourcesList;
    }

    public List<ExResources> findListCache(ExResources exResources) {
        //获取缓存数据
        String findListKey = RedisUtils.getFindListKey(ExResourcesService.class.getName(),
                JSON.toJSONString(exResources));
        List<ExResources> exResourcesList = (List<ExResources>) redisUtils.get(findListKey);
        if (exResourcesList != null)
            return exResourcesList;
        //获取数据库数据
        exResourcesList = super.findList(exResources);
        //设置缓存数据
        redisUtils.set(findListKey, exResourcesList);
        return exResourcesList;
    }

    public ExResources findListFirst(ExResources exResources) {
        ;
        //获取数据库数据
        List<ExResources> exResourcesList = super.findList(exResources);
        if (!exResourcesList.isEmpty())
            exResources = exResourcesList.get(0);
        return exResources;
    }

    public ExResources findListFirstCache(ExResources exResources) {
        //获取缓存数据
        String findListFirstKey = RedisUtils.getFindListFirstKey(ExResourcesService.class.getName(),
                JSON.toJSONString(exResources));
        ExResources exResourcesRedis = (ExResources) redisUtils.get(findListFirstKey);
        if (exResourcesRedis != null)
            return exResourcesRedis;
        //获取数据库数据
        List<ExResources> exResourcesList = super.findList(exResources);
        if (!exResourcesList.isEmpty())
            exResources = exResourcesList.get(0);
        else
            exResources = new ExResources();
        //设置缓存数据
        redisUtils.set(findListFirstKey, exResources);
        return exResources;
    }

    public Page<ExResources> findPage(Page<ExResources> page, ExResources exResources) {
        //获取数据库数据
        Page<ExResources> pageReuslt = super.findPage(page, exResources);
        return pageReuslt;
    }

    public Page<ExResources> findPageCache(Page<ExResources> page, ExResources exResources) {
        //获取缓存数据
        String findPageKey = RedisUtils.getFindPageKey(ExResourcesService.class.getName(),
                JSON.toJSONString(page) + JSON.toJSONString(exResources));
        Page<ExResources> pageReuslt = (Page<ExResources>) redisUtils.get(findPageKey);
        if (pageReuslt != null)
            return pageReuslt;
        //获取数据库数据
        pageReuslt = super.findPage(page, exResources);
        //设置缓存数据
        redisUtils.set(findPageKey, pageReuslt);
        return pageReuslt;
    }

    @Transactional(readOnly = false)
    public void save(ExResources exResources) {
        //保存数据库记录
        super.save(exResources);
        //设置清除缓存数据
        redisUtils.remove(RedisUtils.getIdKey(ExResourcesService.class.getName(), exResources.getId()));
        //清除列表和页面缓存数据
        redisUtils.removePattern(RedisUtils.getFindListKeyPattern(ExResourcesService.class.getName()));
        redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(ExResourcesService.class.getName()));
    }

    @Transactional(readOnly = false)
    public void delete(ExResources exResources) {
        //清除记录缓存数据
        redisUtils.remove(RedisUtils.getIdKey(ExResourcesService.class.getName(), exResources.getId()));
        //删除数据库记录
        super.delete(exResources);
        //清除列表和页面缓存数据
        redisUtils.removePattern(RedisUtils.getFindListKeyPattern(ExResourcesService.class.getName()));
        redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(ExResourcesService.class.getName()));
    }

    @Transactional(readOnly = false)
    public void deleteByLogic(ExResources exResources) {
        //清除记录缓存数据
        redisUtils.remove(RedisUtils.getIdKey(ExResourcesService.class.getName(), exResources.getId()));
        //逻辑删除数据库记录
        super.deleteByLogic(exResources);
        //清除列表和页面缓存数据
        redisUtils.removePattern(RedisUtils.getFindListKeyPattern(ExResourcesService.class.getName()));
        redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(ExResourcesService.class.getName()));
    }

}