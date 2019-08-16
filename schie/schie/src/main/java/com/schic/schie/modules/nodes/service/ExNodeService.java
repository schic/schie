/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.nodes.service;

import com.alibaba.fastjson.JSON;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.redis.RedisUtils;
import com.jeespring.common.service.AbstractBaseService;
import com.schic.schie.modules.nodes.dao.ExNodeDao;
import com.schic.schie.modules.nodes.entity.ExNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//import com.alibaba.dubbo.config.annotation.Service;


/**
 * 节点管理Service
 * @author DHP
 * @version 2019-08-07
 */
 //启用dubbo服务器时，要去掉下面注解
 //com.alibaba.dubbo.config.annotation.Service(interfaceClass = ISysServerService.class,version = "1.0.0", timeout = 60000)
@Service
@Transactional(readOnly = true)
public class ExNodeService extends AbstractBaseService<ExNodeDao, ExNode> implements IExNodeService{

	/**
	 * redis caches
	 */
	@Autowired
	private RedisUtils redisUtils;

	public ExNode get(String id) {
		//获取数据库数据
		ExNode  exNode=super.get(id);
		return exNode;
	}

	public ExNode getCache(String id) {
		//获取缓存数据
		ExNode exNode=(ExNode)redisUtils.get(RedisUtils.getIdKey(ExNodeService.class.getName(),id));
		if( exNode!=null) return  exNode;
		//获取数据库数据
		exNode=super.get(id);
		//设置缓存数据
		redisUtils.set(RedisUtils.getIdKey(ExNodeService.class.getName(),id),exNode);
		return exNode;
	}

	public List<ExNode> total(ExNode exNode) {
		//获取数据库数据
		List<ExNode> exNodeList=super.total(exNode);
		return exNodeList;
	}

	public List<ExNode> totalCache(ExNode exNode) {
		//获取缓存数据
		String totalKey = RedisUtils.getTotalKey(ExNodeService.class.getName(),JSON.toJSONString(exNode));
		List<ExNode> exNodeList=(List<ExNode>)redisUtils.get(totalKey);
		if(exNodeList!=null) return exNodeList;
		//获取数据库数据
		exNodeList=super.total(exNode);
		//设置缓存数据
		redisUtils.set(totalKey,exNodeList);
		return exNodeList;
	}

	public List<ExNode> findList(ExNode exNode) {
		//获取数据库数据
		List<ExNode> exNodeList=super.findList(exNode);
		//设置缓存数据
		return exNodeList;
	}

	public List<ExNode> findListCache(ExNode exNode) {
		//获取缓存数据
		String findListKey = RedisUtils.getFindListKey(ExNodeService.class.getName(),JSON.toJSONString(exNode));
		List<ExNode> exNodeList=(List<ExNode>)redisUtils.get(findListKey);
		if(exNodeList!=null) return exNodeList;
		//获取数据库数据
		exNodeList=super.findList(exNode);
		//设置缓存数据
		redisUtils.set(findListKey,exNodeList);
		return exNodeList;
	}

	public ExNode findListFirst(ExNode exNode) {;
		//获取数据库数据
		List<ExNode> exNodeList=super.findList(exNode);
		if(exNodeList.size()>0) exNode=exNodeList.get(0);
		return exNode;
	}

	public ExNode findListFirstCache(ExNode exNode) {
		//获取缓存数据
		String findListFirstKey = RedisUtils.getFindListFirstKey(ExNodeService.class.getName(),JSON.toJSONString(exNode));
		ExNode exNodeRedis=(ExNode)redisUtils.get(findListFirstKey);
		if(exNodeRedis!=null) return exNodeRedis;
		//获取数据库数据
		List<ExNode> exNodeList=super.findList(exNode);
		if(exNodeList.size()>0) exNode=exNodeList.get(0);
		else exNode=new ExNode();
		//设置缓存数据
		redisUtils.set(findListFirstKey,exNode);
		return exNode;
	}

	public Page<ExNode> findPage(Page<ExNode> page, ExNode exNode) {
		//获取数据库数据
		Page<ExNode> pageReuslt=super.findPage(page, exNode);
		return pageReuslt;
	}

	public Page<ExNode> findPageCache(Page<ExNode> page, ExNode exNode) {
		//获取缓存数据
		String findPageKey =  RedisUtils.getFindPageKey(ExNodeService.class.getName(),JSON.toJSONString(page)+JSON.toJSONString(exNode));
		Page<ExNode> pageReuslt=(Page<ExNode>)redisUtils.get(findPageKey);
		if(pageReuslt!=null) return pageReuslt;
		//获取数据库数据
		pageReuslt=super.findPage(page, exNode);
		//设置缓存数据
		redisUtils.set(findPageKey,pageReuslt);
		return pageReuslt;
	}

	@Transactional(readOnly = false)
	public void save(ExNode exNode) {
		//保存数据库记录
		super.save(exNode);
		//设置清除缓存数据
		redisUtils.remove(RedisUtils.getIdKey(ExNodeService.class.getName(),exNode.getId()));
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(ExNodeService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(ExNodeService.class.getName()));
	}
	
	@Transactional(readOnly = false)
	public void delete(ExNode exNode) {
		//清除记录缓存数据
		redisUtils.remove(RedisUtils.getIdKey(ExNodeService.class.getName(),exNode.getId()));
		//删除数据库记录
		super.delete(exNode);
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(ExNodeService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(ExNodeService.class.getName()));
	}

	@Transactional(readOnly = false)
	public void deleteByLogic(ExNode exNode) {
		//清除记录缓存数据
		redisUtils.remove(RedisUtils.getIdKey(ExNodeService.class.getName(),exNode.getId()));
		//逻辑删除数据库记录
		super.deleteByLogic(exNode);
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(ExNodeService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(ExNodeService.class.getName()));
	}

}