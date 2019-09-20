/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.database.service;

import com.alibaba.fastjson.JSON;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.redis.RedisUtils;
import com.jeespring.common.service.AbstractBaseService;
import com.schic.schie.modules.database.dao.ExDbDao;
import com.schic.schie.modules.database.entity.ExDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//import com.alibaba.dubbo.config.annotation.Service;

/**
 * DBService
 * @author DHP
 * @version 2019-08-07
 */
//启用dubbo服务器时，要去掉下面注解
//com.alibaba.dubbo.config.annotation.Service(interfaceClass = ISysServerService.class,version = "1.0.0", timeout = 60000)
@Service
@Transactional(readOnly = true)
public class ExDbService extends AbstractBaseService<ExDbDao, ExDb> implements IExDbService {

    /**
     * redis caches
     */
    @Autowired
    private RedisUtils redisUtils;

    public ExDb get(String id) {
        //获取数据库数据
        ExDb exDb = super.get(id);
        return exDb;
    }

    public ExDb getCache(String id) {
        //获取缓存数据
        ExDb exDb = (ExDb) redisUtils.get(RedisUtils.getIdKey(ExDbService.class.getName(), id));
        if (exDb != null)
            return exDb;
        //获取数据库数据
        exDb = super.get(id);
        //设置缓存数据
        redisUtils.set(RedisUtils.getIdKey(ExDbService.class.getName(), id), exDb);
        return exDb;
    }

    public List<ExDb> total(ExDb exDb) {
        //获取数据库数据
        List<ExDb> exDbList = super.total(exDb);
        return exDbList;
    }

    public List<ExDb> totalCache(ExDb exDb) {
        //获取缓存数据
        String totalKey = RedisUtils.getTotalKey(ExDbService.class.getName(), JSON.toJSONString(exDb));
        List<ExDb> exDbList = (List<ExDb>) redisUtils.get(totalKey);
        if (exDbList != null)
            return exDbList;
        //获取数据库数据
        exDbList = super.total(exDb);
        //设置缓存数据
        redisUtils.set(totalKey, exDbList);
        return exDbList;
    }

    public List<ExDb> findList(ExDb exDb) {
        //获取数据库数据
        List<ExDb> exDbList = super.findList(exDb);
        //设置缓存数据
        return exDbList;
    }

    public List<ExDb> findListCache(ExDb exDb) {
        //获取缓存数据
        String findListKey = RedisUtils.getFindListKey(ExDbService.class.getName(), JSON.toJSONString(exDb));
        List<ExDb> exDbList = (List<ExDb>) redisUtils.get(findListKey);
        if (exDbList != null)
            return exDbList;
        //获取数据库数据
        exDbList = super.findList(exDb);
        //设置缓存数据
        redisUtils.set(findListKey, exDbList);
        return exDbList;
    }

    public ExDb findListFirst(ExDb exDb) {
        ;
        //获取数据库数据
        List<ExDb> exDbList = super.findList(exDb);
        if (!exDbList.isEmpty())
            exDb = exDbList.get(0);
        return exDb;
    }

    public ExDb findListFirstCache(ExDb exDb) {
        //获取缓存数据
        String findListFirstKey = RedisUtils.getFindListFirstKey(ExDbService.class.getName(), JSON.toJSONString(exDb));
        ExDb exDbRedis = (ExDb) redisUtils.get(findListFirstKey);
        if (exDbRedis != null)
            return exDbRedis;
        //获取数据库数据
        List<ExDb> exDbList = super.findList(exDb);
        if (!exDbList.isEmpty())
            exDb = exDbList.get(0);
        else
            exDb = new ExDb();
        //设置缓存数据
        redisUtils.set(findListFirstKey, exDb);
        return exDb;
    }

    public Page<ExDb> findPage(Page<ExDb> page, ExDb exDb) {
        //获取数据库数据
        Page<ExDb> pageReuslt = super.findPage(page, exDb);
        return pageReuslt;
    }

    public Page<ExDb> findPageCache(Page<ExDb> page, ExDb exDb) {
        //获取缓存数据
        String findPageKey = RedisUtils.getFindPageKey(ExDbService.class.getName(),
                JSON.toJSONString(page) + JSON.toJSONString(exDb));
        Page<ExDb> pageReuslt = (Page<ExDb>) redisUtils.get(findPageKey);
        if (pageReuslt != null)
            return pageReuslt;
        //获取数据库数据
        pageReuslt = super.findPage(page, exDb);
        //设置缓存数据
        redisUtils.set(findPageKey, pageReuslt);
        return pageReuslt;
    }

    @Transactional(readOnly = false)
    public void save(ExDb exDb) {
        //保存数据库记录
        super.save(exDb);
        //设置清除缓存数据
        redisUtils.remove(RedisUtils.getIdKey(ExDbService.class.getName(), exDb.getId()));
        //清除列表和页面缓存数据
        redisUtils.removePattern(RedisUtils.getFindListKeyPattern(ExDbService.class.getName()));
        redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(ExDbService.class.getName()));
    }

    @Transactional(readOnly = false)
    public void delete(ExDb exDb) {
        //清除记录缓存数据
        redisUtils.remove(RedisUtils.getIdKey(ExDbService.class.getName(), exDb.getId()));
        //删除数据库记录
        super.delete(exDb);
        //清除列表和页面缓存数据
        redisUtils.removePattern(RedisUtils.getFindListKeyPattern(ExDbService.class.getName()));
        redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(ExDbService.class.getName()));
    }

    @Transactional(readOnly = false)
    public void deleteByLogic(ExDb exDb) {
        //清除记录缓存数据
        redisUtils.remove(RedisUtils.getIdKey(ExDbService.class.getName(), exDb.getId()));
        //逻辑删除数据库记录
        super.deleteByLogic(exDb);
        //清除列表和页面缓存数据
        redisUtils.removePattern(RedisUtils.getFindListKeyPattern(ExDbService.class.getName()));
        redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(ExDbService.class.getName()));
    }

}