/**
 * *
 */
package com.schic.schie.modules.epidemic.service;

import com.alibaba.fastjson.JSON;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.redis.RedisUtils;
import com.jeespring.common.service.AbstractBaseService;
import com.schic.schie.modules.epidemic.dao.PhqAddrDao;
import com.schic.schie.modules.epidemic.entity.PhqAddr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 高频地址实体Service
 * @author leo
 * @version 2020-03-11
 */
@Service
@Transactional(readOnly = true)
public class PhqAddrService extends AbstractBaseService<PhqAddrDao, PhqAddr> implements IPhqAddrService {

	/**
	 * redis caches
	 */
	@Autowired
	private RedisUtils redisUtils;

	public PhqAddr get(String id) {
		//获取数据库数据
		PhqAddr phqAddr=super.get(id);
		return phqAddr;
	}

	public PhqAddr getCache(String id) {
		//获取缓存数据
		PhqAddr phqAddr=(PhqAddr)redisUtils.get(RedisUtils.getIdKey(PhqAddrService.class.getName(),id));
		if( phqAddr!=null) return  phqAddr;
		//获取数据库数据
		phqAddr=super.get(id);
		//设置缓存数据
		redisUtils.set(RedisUtils.getIdKey(PhqAddrService.class.getName(),id),phqAddr);
		return phqAddr;
	}

	public List<PhqAddr> total(PhqAddr phqAddr) {
		//获取数据库数据
		List<PhqAddr> phqAddrList=super.total(phqAddr);
		return phqAddrList;
	}

	public List<PhqAddr> totalCache(PhqAddr phqAddr) {
		//获取缓存数据
		String totalKey = RedisUtils.getTotalKey(PhqAddrService.class.getName(),JSON.toJSONString(phqAddr));
		List<PhqAddr> phqAddrList=(List<PhqAddr>)redisUtils.get(totalKey);
		if(phqAddrList!=null) return phqAddrList;
		//获取数据库数据
		phqAddrList=super.total(phqAddr);
		//设置缓存数据
		redisUtils.set(totalKey,phqAddrList);
		return phqAddrList;
	}

	public List<PhqAddr> findList(PhqAddr phqAddr) {
		//获取数据库数据
		List<PhqAddr> phqAddrList=super.findList(phqAddr);
		//设置缓存数据
		return phqAddrList;
	}

	public List<PhqAddr> findListCache(PhqAddr phqAddr) {
		//获取缓存数据
		String findListKey = RedisUtils.getFindListKey(PhqAddrService.class.getName(),JSON.toJSONString(phqAddr));
		List<PhqAddr> phqAddrList=(List<PhqAddr>)redisUtils.get(findListKey);
		if(phqAddrList!=null) return phqAddrList;
		//获取数据库数据
		phqAddrList=super.findList(phqAddr);
		//设置缓存数据
		redisUtils.set(findListKey,phqAddrList);
		return phqAddrList;
	}

	public PhqAddr findListFirst(PhqAddr phqAddr) {;
		//获取数据库数据
		List<PhqAddr> phqAddrList=super.findList(phqAddr);
		if(phqAddrList.size()>0) phqAddr=phqAddrList.get(0);
		return phqAddr;
	}

	public PhqAddr findListFirstCache(PhqAddr phqAddr) {
		//获取缓存数据
		String findListFirstKey = RedisUtils.getFindListFirstKey(PhqAddrService.class.getName(),JSON.toJSONString(phqAddr));
		PhqAddr phqAddrRedis=(PhqAddr)redisUtils.get(findListFirstKey);
		if(phqAddrRedis!=null) return phqAddrRedis;
		//获取数据库数据
		List<PhqAddr> phqAddrList=super.findList(phqAddr);
		if(phqAddrList.size()>0) phqAddr=phqAddrList.get(0);
		else phqAddr=new PhqAddr();
		//设置缓存数据
		redisUtils.set(findListFirstKey,phqAddr);
		return phqAddr;
	}

	public Page<PhqAddr> findPage(Page<PhqAddr> page, PhqAddr phqAddr) {
		//获取数据库数据
		Page<PhqAddr> pageReuslt=super.findPage(page, phqAddr);
		return pageReuslt;
	}

	public Page<PhqAddr> findPageCache(Page<PhqAddr> page, PhqAddr phqAddr) {
		//获取缓存数据
		String findPageKey =  RedisUtils.getFindPageKey(PhqAddrService.class.getName(),JSON.toJSONString(page)+JSON.toJSONString(phqAddr));
		Page<PhqAddr> pageReuslt=(Page<PhqAddr>)redisUtils.get(findPageKey);
		if(pageReuslt!=null) return pageReuslt;
		//获取数据库数据
		pageReuslt=super.findPage(page, phqAddr);
		//设置缓存数据
		redisUtils.set(findPageKey,pageReuslt);
		return pageReuslt;
	}

	@Transactional(readOnly = false)
	public void save(PhqAddr phqAddr) {
		//保存数据库记录
		super.save(phqAddr);
		//设置清除缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PhqAddrService.class.getName(),phqAddr.getId()));
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PhqAddrService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PhqAddrService.class.getName()));
	}
	
	@Transactional(readOnly = false)
	public void delete(PhqAddr phqAddr) {
		//清除记录缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PhqAddrService.class.getName(),phqAddr.getId()));
		//删除数据库记录
		super.delete(phqAddr);
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PhqAddrService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PhqAddrService.class.getName()));
	}

	@Transactional(readOnly = false)
	public void deleteByLogic(PhqAddr phqAddr) {
		//清除记录缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PhqAddrService.class.getName(),phqAddr.getId()));
		//逻辑删除数据库记录
		super.deleteByLogic(phqAddr);
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PhqAddrService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PhqAddrService.class.getName()));
	}

}