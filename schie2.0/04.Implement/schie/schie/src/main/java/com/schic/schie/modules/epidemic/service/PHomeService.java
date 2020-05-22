/**
 *
 */
package com.schic.schie.modules.epidemic.service;

import com.alibaba.fastjson.JSON;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.redis.RedisUtils;
import com.jeespring.common.service.AbstractBaseService;
import com.schic.schie.modules.epidemic.dao.PHomeDao;
import com.schic.schie.modules.epidemic.entity.PHome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 病患同户实体类Service
 * @author Leo
 * @version 2020-03-17
 */
 //启用dubbo服务器时，要去掉下面注解
 //com.alibaba.dubbo.config.annotation.Service(interfaceClass = ISysServerService.class,version = "1.0.0", timeout = 60000)
@Service
@Transactional(readOnly = true)
public class PHomeService extends AbstractBaseService<PHomeDao, PHome> implements IPHomeService{

	/**
	 * redis caches
	 */
	@Autowired
	private RedisUtils redisUtils;

	public PHome get(String id) {
		//获取数据库数据
		PHome  pHome=super.get(id);
		return pHome;
	}

	public PHome getCache(String id) {
		//获取缓存数据
		PHome pHome=(PHome)redisUtils.get(RedisUtils.getIdKey(PHomeService.class.getName(),id));
		if( pHome!=null) return  pHome;
		//获取数据库数据
		pHome=super.get(id);
		//设置缓存数据
		redisUtils.set(RedisUtils.getIdKey(PHomeService.class.getName(),id),pHome);
		return pHome;
	}

	public List<PHome> total(PHome pHome) {
		//获取数据库数据
		List<PHome> pHomeList=super.total(pHome);
		return pHomeList;
	}

	public List<PHome> totalCache(PHome pHome) {
		//获取缓存数据
		String totalKey = RedisUtils.getTotalKey(PHomeService.class.getName(),JSON.toJSONString(pHome));
		List<PHome> pHomeList=(List<PHome>)redisUtils.get(totalKey);
		if(pHomeList!=null) return pHomeList;
		//获取数据库数据
		pHomeList=super.total(pHome);
		//设置缓存数据
		redisUtils.set(totalKey,pHomeList);
		return pHomeList;
	}

	public List<PHome> findList(PHome pHome) {
		//获取数据库数据
		List<PHome> pHomeList=super.findList(pHome);
		//设置缓存数据
		return pHomeList;
	}

	public List<PHome> findListCache(PHome pHome) {
		//获取缓存数据
		String findListKey = RedisUtils.getFindListKey(PHomeService.class.getName(),JSON.toJSONString(pHome));
		List<PHome> pHomeList=(List<PHome>)redisUtils.get(findListKey);
		if(pHomeList!=null) return pHomeList;
		//获取数据库数据
		pHomeList=super.findList(pHome);
		//设置缓存数据
		redisUtils.set(findListKey,pHomeList);
		return pHomeList;
	}

	public PHome findListFirst(PHome pHome) {;
		//获取数据库数据
		List<PHome> pHomeList=super.findList(pHome);
		if(pHomeList.size()>0) pHome=pHomeList.get(0);
		return pHome;
	}

	public PHome findListFirstCache(PHome pHome) {
		//获取缓存数据
		String findListFirstKey = RedisUtils.getFindListFirstKey(PHomeService.class.getName(),JSON.toJSONString(pHome));
		PHome pHomeRedis=(PHome)redisUtils.get(findListFirstKey);
		if(pHomeRedis!=null) return pHomeRedis;
		//获取数据库数据
		List<PHome> pHomeList=super.findList(pHome);
		if(pHomeList.size()>0) pHome=pHomeList.get(0);
		else pHome=new PHome();
		//设置缓存数据
		redisUtils.set(findListFirstKey,pHome);
		return pHome;
	}

	public Page<PHome> findPage(Page<PHome> page, PHome pHome) {
		//获取数据库数据
		Page<PHome> pageReuslt=super.findPage(page, pHome);
		return pageReuslt;
	}

	public Page<PHome> findPageCache(Page<PHome> page, PHome pHome) {
		//获取缓存数据
		String findPageKey =  RedisUtils.getFindPageKey(PHomeService.class.getName(),JSON.toJSONString(page)+JSON.toJSONString(pHome));
		Page<PHome> pageReuslt=(Page<PHome>)redisUtils.get(findPageKey);
		if(pageReuslt!=null) return pageReuslt;
		//获取数据库数据
		pageReuslt=super.findPage(page, pHome);
		//设置缓存数据
		redisUtils.set(findPageKey,pageReuslt);
		return pageReuslt;
	}

	@Transactional(readOnly = false)
	public void save(PHome pHome) {
		//保存数据库记录
		super.save(pHome);
		//设置清除缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PHomeService.class.getName(),pHome.getId()));
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PHomeService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PHomeService.class.getName()));
	}
	
	@Transactional(readOnly = false)
	public void delete(PHome pHome) {
		//清除记录缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PHomeService.class.getName(),pHome.getId()));
		//删除数据库记录
		super.delete(pHome);
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PHomeService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PHomeService.class.getName()));
	}

	@Transactional(readOnly = false)
	public void deleteByLogic(PHome pHome) {
		//清除记录缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PHomeService.class.getName(),pHome.getId()));
		//逻辑删除数据库记录
		super.deleteByLogic(pHome);
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PHomeService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PHomeService.class.getName()));
	}

}