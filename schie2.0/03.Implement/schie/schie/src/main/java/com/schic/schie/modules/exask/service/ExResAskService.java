/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.exask.service;

import com.alibaba.fastjson.JSON;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.redis.RedisUtils;
import com.jeespring.common.service.AbstractBaseService;
import com.schic.schie.modules.exask.dao.ExResAskDao;
import com.schic.schie.modules.exask.entity.ExResAsk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//import com.alibaba.dubbo.config.annotation.Service;

/**
 * 资源申请表Service
 * @author leodeyang
 * @version 2019-08-12
 */
//启用dubbo服务器时，要去掉下面注解
//com.alibaba.dubbo.config.annotation.Service(interfaceClass = ISysServerService.class,version = "1.0.0", timeout = 60000)
@Service
@Transactional(readOnly = true)
public class ExResAskService extends AbstractBaseService<ExResAskDao, ExResAsk> implements IExResAskService {

    /**
     * redis caches
     */
    @Autowired
    private RedisUtils redisUtils;

    public ExResAsk get(String id) {
        //获取数据库数据
        ExResAsk exResAsk = super.get(id);
        return exResAsk;
    }

    public ExResAsk getCache(String id) {
        //获取缓存数据
        ExResAsk exResAsk = (ExResAsk) redisUtils.get(RedisUtils.getIdKey(ExResAskService.class.getName(), id));
        if (exResAsk != null) {
            return exResAsk;
        }
        //获取数据库数据
        exResAsk = super.get(id);
        //设置缓存数据
        redisUtils.set(RedisUtils.getIdKey(ExResAskService.class.getName(), id), exResAsk);
        return exResAsk;
    }

    public List<ExResAsk> total(ExResAsk exResAsk) {
        //获取数据库数据
        List<ExResAsk> exResAskList = super.total(exResAsk);
        return exResAskList;
    }

    public List<ExResAsk> totalCache(ExResAsk exResAsk) {
        //获取缓存数据
        String totalKey = RedisUtils.getTotalKey(ExResAskService.class.getName(), JSON.toJSONString(exResAsk));
        List<ExResAsk> exResAskList = (List<ExResAsk>) redisUtils.get(totalKey);
        if (exResAskList != null) {
            return exResAskList;
        }
        //获取数据库数据
        exResAskList = super.total(exResAsk);
        //设置缓存数据
        redisUtils.set(totalKey, exResAskList);
        return exResAskList;
    }

    public List<ExResAsk> findList(ExResAsk exResAsk) {
        //获取数据库数据
        List<ExResAsk> exResAskList = super.findList(exResAsk);
        //设置缓存数据
        return exResAskList;
    }

    public List<ExResAsk> findListCache(ExResAsk exResAsk) {
        //获取缓存数据
        String findListKey = RedisUtils.getFindListKey(ExResAskService.class.getName(), JSON.toJSONString(exResAsk));
        List<ExResAsk> exResAskList = (List<ExResAsk>) redisUtils.get(findListKey);
        if (exResAskList != null) {
            return exResAskList;
        }
        //获取数据库数据
        exResAskList = super.findList(exResAsk);
        //设置缓存数据
        redisUtils.set(findListKey, exResAskList);
        return exResAskList;
    }

    public ExResAsk findListFirst(ExResAsk exResAsk) {
        ;
        //获取数据库数据
        List<ExResAsk> exResAskList = super.findList(exResAsk);
        if (!exResAskList.isEmpty()) {
            exResAsk = exResAskList.get(0);
        }
        return exResAsk;
    }

    public ExResAsk findListFirstCache(ExResAsk exResAsk) {
        //获取缓存数据
        String findListFirstKey = RedisUtils.getFindListFirstKey(ExResAskService.class.getName(),
                JSON.toJSONString(exResAsk));
        ExResAsk exResAskRedis = (ExResAsk) redisUtils.get(findListFirstKey);
        if (exResAskRedis != null) {
            return exResAskRedis;
        }
        //获取数据库数据
        List<ExResAsk> exResAskList = super.findList(exResAsk);
        if (!exResAskList.isEmpty()) {
            exResAsk = exResAskList.get(0);
        } else {
            exResAsk = new ExResAsk();
        }
        //设置缓存数据
        redisUtils.set(findListFirstKey, exResAsk);
        return exResAsk;
    }

    public Page<ExResAsk> findPage(Page<ExResAsk> page, ExResAsk exResAsk) {
        //获取数据库数据
        Page<ExResAsk> pageReuslt = super.findPage(page, exResAsk);
        return pageReuslt;
    }

    public Page<ExResAsk> findPageCache(Page<ExResAsk> page, ExResAsk exResAsk) {
        //获取缓存数据
        String findPageKey = RedisUtils.getFindPageKey(ExResAskService.class.getName(),
                JSON.toJSONString(page) + JSON.toJSONString(exResAsk));
        Page<ExResAsk> pageReuslt = (Page<ExResAsk>) redisUtils.get(findPageKey);
        if (pageReuslt != null) {
            return pageReuslt;
        }
        //获取数据库数据
        pageReuslt = super.findPage(page, exResAsk);
        //设置缓存数据
        redisUtils.set(findPageKey, pageReuslt);
        return pageReuslt;
    }

    @Transactional(readOnly = false)
    public void save(ExResAsk exResAsk) {
        //保存数据库记录
        super.save(exResAsk);
        //设置清除缓存数据
        redisUtils.remove(RedisUtils.getIdKey(ExResAskService.class.getName(), exResAsk.getId()));
        //清除列表和页面缓存数据
        redisUtils.removePattern(RedisUtils.getFindListKeyPattern(ExResAskService.class.getName()));
        redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(ExResAskService.class.getName()));
    }

    @Transactional(readOnly = false)
    public void delete(ExResAsk exResAsk) {
        //清除记录缓存数据
        redisUtils.remove(RedisUtils.getIdKey(ExResAskService.class.getName(), exResAsk.getId()));
        //删除数据库记录
        super.delete(exResAsk);
        //清除列表和页面缓存数据
        redisUtils.removePattern(RedisUtils.getFindListKeyPattern(ExResAskService.class.getName()));
        redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(ExResAskService.class.getName()));
    }

    @Transactional(readOnly = false)
    public void deleteByLogic(ExResAsk exResAsk) {
        //清除记录缓存数据
        redisUtils.remove(RedisUtils.getIdKey(ExResAskService.class.getName(), exResAsk.getId()));
        //逻辑删除数据库记录
        super.deleteByLogic(exResAsk);
        //清除列表和页面缓存数据
        redisUtils.removePattern(RedisUtils.getFindListKeyPattern(ExResAskService.class.getName()));
        redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(ExResAskService.class.getName()));
    }

    @Transactional(readOnly = false)
    @Override
    public int updateSubNow(ExResAsk resAsk) {
        return dao.updateSubNow(resAsk);
    }

}
