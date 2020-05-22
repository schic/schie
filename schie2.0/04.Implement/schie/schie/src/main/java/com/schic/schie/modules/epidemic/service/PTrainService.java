/**
 * * .
 */
package com.schic.schie.modules.epidemic.service;

import com.alibaba.fastjson.JSON;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.redis.RedisUtils;
import com.jeespring.common.service.AbstractBaseService;
import com.schic.schie.modules.epidemic.dao.PTrainDao;
import com.schic.schie.modules.epidemic.entity.PTrain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 病患同火车实体类Service
 * @author Leo
 * @version 2020-03-17
 */
 //启用dubbo服务器时，要去掉下面注解
 //com.alibaba.dubbo.config.annotation.Service(interfaceClass = ISysServerService.class,version = "1.0.0", timeout = 60000)
@Service
@Transactional(readOnly = true)
public class PTrainService extends AbstractBaseService<PTrainDao, PTrain> implements IPTrainService{

	/**
	 * redis caches
	 */
	@Autowired
	private RedisUtils redisUtils;

	public PTrain get(String id) {
		//获取数据库数据
		PTrain  pTrain=super.get(id);
		return pTrain;
	}

	public PTrain getCache(String id) {
		//获取缓存数据
		PTrain pTrain=(PTrain)redisUtils.get(RedisUtils.getIdKey(PTrainService.class.getName(),id));
		if( pTrain!=null) return  pTrain;
		//获取数据库数据
		pTrain=super.get(id);
		//设置缓存数据
		redisUtils.set(RedisUtils.getIdKey(PTrainService.class.getName(),id),pTrain);
		return pTrain;
	}

	public List<PTrain> total(PTrain pTrain) {
		//获取数据库数据
		List<PTrain> pTrainList=super.total(pTrain);
		return pTrainList;
	}

	public List<PTrain> totalCache(PTrain pTrain) {
		//获取缓存数据
		String totalKey = RedisUtils.getTotalKey(PTrainService.class.getName(),JSON.toJSONString(pTrain));
		List<PTrain> pTrainList=(List<PTrain>)redisUtils.get(totalKey);
		if(pTrainList!=null) return pTrainList;
		//获取数据库数据
		pTrainList=super.total(pTrain);
		//设置缓存数据
		redisUtils.set(totalKey,pTrainList);
		return pTrainList;
	}

	public List<PTrain> findList(PTrain pTrain) {
		//获取数据库数据
		List<PTrain> pTrainList=super.findList(pTrain);
		//设置缓存数据
		return pTrainList;
	}

	public List<PTrain> findListCache(PTrain pTrain) {
		//获取缓存数据
		String findListKey = RedisUtils.getFindListKey(PTrainService.class.getName(),JSON.toJSONString(pTrain));
		List<PTrain> pTrainList=(List<PTrain>)redisUtils.get(findListKey);
		if(pTrainList!=null) return pTrainList;
		//获取数据库数据
		pTrainList=super.findList(pTrain);
		//设置缓存数据
		redisUtils.set(findListKey,pTrainList);
		return pTrainList;
	}

	public PTrain findListFirst(PTrain pTrain) {;
		//获取数据库数据
		List<PTrain> pTrainList=super.findList(pTrain);
		if(pTrainList.size()>0) pTrain=pTrainList.get(0);
		return pTrain;
	}

	public PTrain findListFirstCache(PTrain pTrain) {
		//获取缓存数据
		String findListFirstKey = RedisUtils.getFindListFirstKey(PTrainService.class.getName(),JSON.toJSONString(pTrain));
		PTrain pTrainRedis=(PTrain)redisUtils.get(findListFirstKey);
		if(pTrainRedis!=null) return pTrainRedis;
		//获取数据库数据
		List<PTrain> pTrainList=super.findList(pTrain);
		if(pTrainList.size()>0) pTrain=pTrainList.get(0);
		else pTrain=new PTrain();
		//设置缓存数据
		redisUtils.set(findListFirstKey,pTrain);
		return pTrain;
	}

	public Page<PTrain> findPage(Page<PTrain> page, PTrain pTrain) {
		//获取数据库数据
		Page<PTrain> pageReuslt=super.findPage(page, pTrain);
		return pageReuslt;
	}

	public Page<PTrain> findPageCache(Page<PTrain> page, PTrain pTrain) {
		//获取缓存数据
		String findPageKey =  RedisUtils.getFindPageKey(PTrainService.class.getName(),JSON.toJSONString(page)+JSON.toJSONString(pTrain));
		Page<PTrain> pageReuslt=(Page<PTrain>)redisUtils.get(findPageKey);
		if(pageReuslt!=null) return pageReuslt;
		//获取数据库数据
		pageReuslt=super.findPage(page, pTrain);
		//设置缓存数据
		redisUtils.set(findPageKey,pageReuslt);
		return pageReuslt;
	}

	@Transactional(readOnly = false)
	public void save(PTrain pTrain) {
		//保存数据库记录
		super.save(pTrain);
		//设置清除缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PTrainService.class.getName(),pTrain.getId()));
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PTrainService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PTrainService.class.getName()));
	}
	
	@Transactional(readOnly = false)
	public void delete(PTrain pTrain) {
		//清除记录缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PTrainService.class.getName(),pTrain.getId()));
		//删除数据库记录
		super.delete(pTrain);
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PTrainService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PTrainService.class.getName()));
	}

	@Transactional(readOnly = false)
	public void deleteByLogic(PTrain pTrain) {
		//清除记录缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PTrainService.class.getName(),pTrain.getId()));
		//逻辑删除数据库记录
		super.deleteByLogic(pTrain);
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PTrainService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PTrainService.class.getName()));
	}

}