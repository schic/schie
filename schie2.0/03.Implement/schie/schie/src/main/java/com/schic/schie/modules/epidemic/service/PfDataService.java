/**
 * *
 */
package com.schic.schie.modules.epidemic.service;

import com.alibaba.fastjson.JSON;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.redis.RedisUtils;
import com.jeespring.common.service.AbstractBaseService;
import com.schic.schie.modules.epidemic.dao.PfDataDao;
import com.schic.schie.modules.epidemic.entity.PfData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 全量数据实体Service
 * @author Leo
 * @version 2020-03-11
 */
@Service
@Transactional(readOnly = true)
public class PfDataService extends AbstractBaseService<PfDataDao, PfData> implements IPfDataService{

	/**
	 * redis caches
	 */
	@Autowired
	private RedisUtils redisUtils;

	public PfData get(String id) {
		//获取数据库数据
		PfData pfData=super.get(id);
		return pfData;
	}

	public PfData getCache(String id) {
		//获取缓存数据
		PfData pfData=(PfData)redisUtils.get(RedisUtils.getIdKey(PfDataService.class.getName(),id));
		if( pfData!=null) return  pfData;
		//获取数据库数据
		pfData=super.get(id);
		//设置缓存数据
		redisUtils.set(RedisUtils.getIdKey(PfDataService.class.getName(),id),pfData);
		return pfData;
	}

	public List<PfData> total(PfData pfData) {
		//获取数据库数据
		List<PfData> pfDataList=super.total(pfData);
		return pfDataList;
	}

	public List<PfData> totalCache(PfData pfData) {
		//获取缓存数据
		String totalKey = RedisUtils.getTotalKey(PfDataService.class.getName(),JSON.toJSONString(pfData));
		List<PfData> pfDataList=(List<PfData>)redisUtils.get(totalKey);
		if(pfDataList!=null) return pfDataList;
		//获取数据库数据
		pfDataList=super.total(pfData);
		//设置缓存数据
		redisUtils.set(totalKey,pfDataList);
		return pfDataList;
	}

	public List<PfData> findList(PfData pfData) {
		//获取数据库数据
		List<PfData> pfDataList=super.findList(pfData);
		//设置缓存数据
		return pfDataList;
	}

	public List<PfData> findListCache(PfData pfData) {
		//获取缓存数据
		String findListKey = RedisUtils.getFindListKey(PfDataService.class.getName(),JSON.toJSONString(pfData));
		List<PfData> pfDataList=(List<PfData>)redisUtils.get(findListKey);
		if(pfDataList!=null) return pfDataList;
		//获取数据库数据
		pfDataList=super.findList(pfData);
		//设置缓存数据
		redisUtils.set(findListKey,pfDataList);
		return pfDataList;
	}

	public PfData findListFirst(PfData pfData) {;
		//获取数据库数据
		List<PfData> pfDataList=super.findList(pfData);
		if(pfDataList.size()>0) pfData=pfDataList.get(0);
		return pfData;
	}

	public PfData findListFirstCache(PfData pfData) {
		//获取缓存数据
		String findListFirstKey = RedisUtils.getFindListFirstKey(PfDataService.class.getName(),JSON.toJSONString(pfData));
		PfData pfDataRedis=(PfData)redisUtils.get(findListFirstKey);
		if(pfDataRedis!=null) return pfDataRedis;
		//获取数据库数据
		List<PfData> pfDataList=super.findList(pfData);
		if(pfDataList.size()>0) pfData=pfDataList.get(0);
		else pfData=new PfData();
		//设置缓存数据
		redisUtils.set(findListFirstKey,pfData);
		return pfData;
	}

	public Page<PfData> findPage(Page<PfData> page, PfData pfData) {
		//获取数据库数据
		Page<PfData> pageReuslt=super.findPage(page, pfData);
		return pageReuslt;
	}

	public Page<PfData> findPageCache(Page<PfData> page, PfData pfData) {
		//获取缓存数据
		String findPageKey =  RedisUtils.getFindPageKey(PfDataService.class.getName(),JSON.toJSONString(page)+JSON.toJSONString(pfData));
		Page<PfData> pageReuslt=(Page<PfData>)redisUtils.get(findPageKey);
		if(pageReuslt!=null) return pageReuslt;
		//获取数据库数据
		pageReuslt=super.findPage(page, pfData);
		//设置缓存数据
		redisUtils.set(findPageKey,pageReuslt);
		return pageReuslt;
	}

	@Transactional(readOnly = false)
	public void save(PfData pfData) {
		//保存数据库记录
		super.save(pfData);
		//设置清除缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PfDataService.class.getName(),pfData.getId()));
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PfDataService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PfDataService.class.getName()));
	}
	
	@Transactional(readOnly = false)
	public void delete(PfData pfData) {
		//清除记录缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PfDataService.class.getName(),pfData.getId()));
		//删除数据库记录
		super.delete(pfData);
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PfDataService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PfDataService.class.getName()));
	}

	@Transactional(readOnly = false)
	public void deleteByLogic(PfData pfData) {
		//清除记录缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PfDataService.class.getName(),pfData.getId()));
		//逻辑删除数据库记录
		super.deleteByLogic(pfData);
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PfDataService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PfDataService.class.getName()));
	}

}