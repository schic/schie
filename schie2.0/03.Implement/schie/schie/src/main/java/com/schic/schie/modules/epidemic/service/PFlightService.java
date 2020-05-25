/**
 * * .
 */
package com.schic.schie.modules.epidemic.service;

import com.alibaba.fastjson.JSON;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.redis.RedisUtils;
import com.jeespring.common.service.AbstractBaseService;
import com.schic.schie.modules.epidemic.dao.PFlightDao;
import com.schic.schie.modules.epidemic.entity.PFlight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 病患同航班实体类Service
 * @author Leo
 * @version 2020-03-17
 */
 //启用dubbo服务器时，要去掉下面注解
 //com.alibaba.dubbo.config.annotation.Service(interfaceClass = ISysServerService.class,version = "1.0.0", timeout = 60000)
@Service
@Transactional(readOnly = true)
public class PFlightService extends AbstractBaseService<PFlightDao, PFlight> implements IPFlightService{

	/**
	 * redis caches
	 */
	@Autowired
	private RedisUtils redisUtils;

	public PFlight get(String id) {
		//获取数据库数据
		PFlight  pFlight=super.get(id);
		return pFlight;
	}

	public PFlight getCache(String id) {
		//获取缓存数据
		PFlight pFlight=(PFlight)redisUtils.get(RedisUtils.getIdKey(PFlightService.class.getName(),id));
		if( pFlight!=null) return  pFlight;
		//获取数据库数据
		pFlight=super.get(id);
		//设置缓存数据
		redisUtils.set(RedisUtils.getIdKey(PFlightService.class.getName(),id),pFlight);
		return pFlight;
	}

	public List<PFlight> total(PFlight pFlight) {
		//获取数据库数据
		List<PFlight> pFlightList=super.total(pFlight);
		return pFlightList;
	}

	public List<PFlight> totalCache(PFlight pFlight) {
		//获取缓存数据
		String totalKey = RedisUtils.getTotalKey(PFlightService.class.getName(),JSON.toJSONString(pFlight));
		List<PFlight> pFlightList=(List<PFlight>)redisUtils.get(totalKey);
		if(pFlightList!=null) return pFlightList;
		//获取数据库数据
		pFlightList=super.total(pFlight);
		//设置缓存数据
		redisUtils.set(totalKey,pFlightList);
		return pFlightList;
	}

	public List<PFlight> findList(PFlight pFlight) {
		//获取数据库数据
		List<PFlight> pFlightList=super.findList(pFlight);
		//设置缓存数据
		return pFlightList;
	}

	public List<PFlight> findListCache(PFlight pFlight) {
		//获取缓存数据
		String findListKey = RedisUtils.getFindListKey(PFlightService.class.getName(),JSON.toJSONString(pFlight));
		List<PFlight> pFlightList=(List<PFlight>)redisUtils.get(findListKey);
		if(pFlightList!=null) return pFlightList;
		//获取数据库数据
		pFlightList=super.findList(pFlight);
		//设置缓存数据
		redisUtils.set(findListKey,pFlightList);
		return pFlightList;
	}

	public PFlight findListFirst(PFlight pFlight) {;
		//获取数据库数据
		List<PFlight> pFlightList=super.findList(pFlight);
		if(pFlightList.size()>0) pFlight=pFlightList.get(0);
		return pFlight;
	}

	public PFlight findListFirstCache(PFlight pFlight) {
		//获取缓存数据
		String findListFirstKey = RedisUtils.getFindListFirstKey(PFlightService.class.getName(),JSON.toJSONString(pFlight));
		PFlight pFlightRedis=(PFlight)redisUtils.get(findListFirstKey);
		if(pFlightRedis!=null) return pFlightRedis;
		//获取数据库数据
		List<PFlight> pFlightList=super.findList(pFlight);
		if(pFlightList.size()>0) pFlight=pFlightList.get(0);
		else pFlight=new PFlight();
		//设置缓存数据
		redisUtils.set(findListFirstKey,pFlight);
		return pFlight;
	}

	public Page<PFlight> findPage(Page<PFlight> page, PFlight pFlight) {
		//获取数据库数据
		Page<PFlight> pageReuslt=super.findPage(page, pFlight);
		return pageReuslt;
	}

	public Page<PFlight> findPageCache(Page<PFlight> page, PFlight pFlight) {
		//获取缓存数据
		String findPageKey =  RedisUtils.getFindPageKey(PFlightService.class.getName(),JSON.toJSONString(page)+JSON.toJSONString(pFlight));
		Page<PFlight> pageReuslt=(Page<PFlight>)redisUtils.get(findPageKey);
		if(pageReuslt!=null) return pageReuslt;
		//获取数据库数据
		pageReuslt=super.findPage(page, pFlight);
		//设置缓存数据
		redisUtils.set(findPageKey,pageReuslt);
		return pageReuslt;
	}

	@Transactional(readOnly = false)
	public void save(PFlight pFlight) {
		//保存数据库记录
		super.save(pFlight);
		//设置清除缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PFlightService.class.getName(),pFlight.getId()));
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PFlightService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PFlightService.class.getName()));
	}
	
	@Transactional(readOnly = false)
	public void delete(PFlight pFlight) {
		//清除记录缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PFlightService.class.getName(),pFlight.getId()));
		//删除数据库记录
		super.delete(pFlight);
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PFlightService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PFlightService.class.getName()));
	}

	@Transactional(readOnly = false)
	public void deleteByLogic(PFlight pFlight) {
		//清除记录缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PFlightService.class.getName(),pFlight.getId()));
		//逻辑删除数据库记录
		super.deleteByLogic(pFlight);
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PFlightService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PFlightService.class.getName()));
	}

}