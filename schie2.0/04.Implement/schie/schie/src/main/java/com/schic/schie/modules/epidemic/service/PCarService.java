/**
 * *
 */
package com.schic.schie.modules.epidemic.service;

import com.alibaba.fastjson.JSON;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.redis.RedisUtils;
import com.jeespring.common.service.AbstractBaseService;
import com.schic.schie.modules.epidemic.dao.PCarDao;
import com.schic.schie.modules.epidemic.entity.PCar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 病患同汽车实体类Service
 * @author Leo
 * @version 2020-03-17
 */
 //启用dubbo服务器时，要去掉下面注解
 //com.alibaba.dubbo.config.annotation.Service(interfaceClass = ISysServerService.class,version = "1.0.0", timeout = 60000)
@Service
@Transactional(readOnly = true)
public class PCarService extends AbstractBaseService<PCarDao, PCar> implements IPCarService{

	/**
	 * redis caches
	 */
	@Autowired
	private RedisUtils redisUtils;

	public PCar get(String id) {
		//获取数据库数据
		PCar  pCar=super.get(id);
		return pCar;
	}

	public PCar getCache(String id) {
		//获取缓存数据
		PCar pCar=(PCar)redisUtils.get(RedisUtils.getIdKey(PCarService.class.getName(),id));
		if( pCar!=null) {return  pCar;}
		//获取数据库数据
		pCar=super.get(id);
		//设置缓存数据
		redisUtils.set(RedisUtils.getIdKey(PCarService.class.getName(),id),pCar);
		return pCar;
	}

	public List<PCar> total(PCar pCar) {
		//获取数据库数据
		List<PCar> pCarList=super.total(pCar);
		return pCarList;
	}

	public List<PCar> totalCache(PCar pCar) {
		//获取缓存数据
		String totalKey = RedisUtils.getTotalKey(PCarService.class.getName(),JSON.toJSONString(pCar));
		List<PCar> pCarList=(List<PCar>)redisUtils.get(totalKey);
		if(pCarList!=null) {return pCarList;}
		//获取数据库数据
		pCarList=super.total(pCar);
		//设置缓存数据
		redisUtils.set(totalKey,pCarList);
		return pCarList;
	}

	public List<PCar> findList(PCar pCar) {
		//获取数据库数据
		List<PCar> pCarList=super.findList(pCar);
		//设置缓存数据
		return pCarList;
	}

	public List<PCar> findListCache(PCar pCar) {
		//获取缓存数据
		String findListKey = RedisUtils.getFindListKey(PCarService.class.getName(),JSON.toJSONString(pCar));
		List<PCar> pCarList=(List<PCar>)redisUtils.get(findListKey);
		if(pCarList!=null) {return pCarList;}
		//获取数据库数据
		pCarList=super.findList(pCar);
		//设置缓存数据
		redisUtils.set(findListKey,pCarList);
		return pCarList;
	}

	public PCar findListFirst(PCar pCar) {;
		//获取数据库数据
		List<PCar> pCarList=super.findList(pCar);
		if(pCarList.isEmpty()) {pCar=pCarList.get(0);}
		return pCar;
	}

	public PCar findListFirstCache(PCar pCar) {
		//获取缓存数据
		String findListFirstKey = RedisUtils.getFindListFirstKey(PCarService.class.getName(),JSON.toJSONString(pCar));
		PCar pCarRedis=(PCar)redisUtils.get(findListFirstKey);
		if(pCarRedis!=null) {return pCarRedis;}
		//获取数据库数据
		List<PCar> pCarList=super.findList(pCar);
		if(!pCarList.isEmpty()) {pCar=pCarList.get(0);}
		else {pCar=new PCar();}
		//设置缓存数据
		redisUtils.set(findListFirstKey,pCar);
		return pCar;
	}

	public Page<PCar> findPage(Page<PCar> page, PCar pCar) {
		//获取数据库数据
		Page<PCar> pageReuslt=super.findPage(page, pCar);
		return pageReuslt;
	}

	public Page<PCar> findPageCache(Page<PCar> page, PCar pCar) {
		//获取缓存数据
		String findPageKey =  RedisUtils.getFindPageKey(PCarService.class.getName(),JSON.toJSONString(page)+JSON.toJSONString(pCar));
		Page<PCar> pageReuslt=(Page<PCar>)redisUtils.get(findPageKey);
		if(pageReuslt!=null) {return pageReuslt;}
		//获取数据库数据
		pageReuslt=super.findPage(page, pCar);
		//设置缓存数据
		redisUtils.set(findPageKey,pageReuslt);
		return pageReuslt;
	}

	@Transactional(readOnly = false)
	public void save(PCar pCar) {
		//保存数据库记录
		super.save(pCar);
		//设置清除缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PCarService.class.getName(),pCar.getId()));
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PCarService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PCarService.class.getName()));
	}
	
	@Transactional(readOnly = false)
	public void delete(PCar pCar) {
		//清除记录缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PCarService.class.getName(),pCar.getId()));
		//删除数据库记录
		super.delete(pCar);
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PCarService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PCarService.class.getName()));
	}

	@Transactional(readOnly = false)
	public void deleteByLogic(PCar pCar) {
		//清除记录缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PCarService.class.getName(),pCar.getId()));
		//逻辑删除数据库记录
		super.deleteByLogic(pCar);
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PCarService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PCarService.class.getName()));
	}
}
