/**
 * *
 */
package com.schic.schie.modules.epidemic.service;

import com.alibaba.fastjson.JSON;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.redis.RedisUtils;
import com.jeespring.common.service.AbstractBaseService;
import com.schic.schie.modules.epidemic.dao.PtSequenDao;
import com.schic.schie.modules.epidemic.entity.PtSequen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 时序高频实体Service
 * @author Leo
 * @version 2020-03-11
 */
@Service
@Transactional(readOnly = true)
public class PtSequenService extends AbstractBaseService<PtSequenDao, PtSequen> implements IPtSequenService{

	/**
	 * redis caches
	 */
	@Autowired
	private RedisUtils redisUtils;

	public PtSequen get(String id) {
		//获取数据库数据
		PtSequen ptSequen=super.get(id);
		return ptSequen;
	}

	public PtSequen getCache(String id) {
		//获取缓存数据
		PtSequen ptSequen=(PtSequen)redisUtils.get(RedisUtils.getIdKey(PtSequenService.class.getName(),id));
		if( ptSequen!=null) return  ptSequen;
		//获取数据库数据
		ptSequen=super.get(id);
		//设置缓存数据
		redisUtils.set(RedisUtils.getIdKey(PtSequenService.class.getName(),id),ptSequen);
		return ptSequen;
	}

	public List<PtSequen> total(PtSequen ptSequen) {
		//获取数据库数据
		List<PtSequen> ptSequenList=super.total(ptSequen);
		return ptSequenList;
	}

	public List<PtSequen> totalCache(PtSequen ptSequen) {
		//获取缓存数据
		String totalKey = RedisUtils.getTotalKey(PtSequenService.class.getName(),JSON.toJSONString(ptSequen));
		List<PtSequen> ptSequenList=(List<PtSequen>)redisUtils.get(totalKey);
		if(ptSequenList!=null) return ptSequenList;
		//获取数据库数据
		ptSequenList=super.total(ptSequen);
		//设置缓存数据
		redisUtils.set(totalKey,ptSequenList);
		return ptSequenList;
	}

	public List<PtSequen> findList(PtSequen ptSequen) {
		//获取数据库数据
		List<PtSequen> ptSequenList=super.findList(ptSequen);
		//设置缓存数据
		return ptSequenList;
	}

	public List<PtSequen> findListCache(PtSequen ptSequen) {
		//获取缓存数据
		String findListKey = RedisUtils.getFindListKey(PtSequenService.class.getName(),JSON.toJSONString(ptSequen));
		List<PtSequen> ptSequenList=(List<PtSequen>)redisUtils.get(findListKey);
		if(ptSequenList!=null) return ptSequenList;
		//获取数据库数据
		ptSequenList=super.findList(ptSequen);
		//设置缓存数据
		redisUtils.set(findListKey,ptSequenList);
		return ptSequenList;
	}

	public PtSequen findListFirst(PtSequen ptSequen) {;
		//获取数据库数据
		List<PtSequen> ptSequenList=super.findList(ptSequen);
		if(ptSequenList.size()>0) ptSequen=ptSequenList.get(0);
		return ptSequen;
	}

	public PtSequen findListFirstCache(PtSequen ptSequen) {
		//获取缓存数据
		String findListFirstKey = RedisUtils.getFindListFirstKey(PtSequenService.class.getName(),JSON.toJSONString(ptSequen));
		PtSequen ptSequenRedis=(PtSequen)redisUtils.get(findListFirstKey);
		if(ptSequenRedis!=null) return ptSequenRedis;
		//获取数据库数据
		List<PtSequen> ptSequenList=super.findList(ptSequen);
		if(ptSequenList.size()>0) ptSequen=ptSequenList.get(0);
		else ptSequen=new PtSequen();
		//设置缓存数据
		redisUtils.set(findListFirstKey,ptSequen);
		return ptSequen;
	}

	public Page<PtSequen> findPage(Page<PtSequen> page, PtSequen ptSequen) {
		//获取数据库数据
		Page<PtSequen> pageReuslt=super.findPage(page, ptSequen);
		return pageReuslt;
	}

	public Page<PtSequen> findPageCache(Page<PtSequen> page, PtSequen ptSequen) {
		//获取缓存数据
		String findPageKey =  RedisUtils.getFindPageKey(PtSequenService.class.getName(),JSON.toJSONString(page)+JSON.toJSONString(ptSequen));
		Page<PtSequen> pageReuslt=(Page<PtSequen>)redisUtils.get(findPageKey);
		if(pageReuslt!=null) return pageReuslt;
		//获取数据库数据
		pageReuslt=super.findPage(page, ptSequen);
		//设置缓存数据
		redisUtils.set(findPageKey,pageReuslt);
		return pageReuslt;
	}

	@Transactional(readOnly = false)
	public void save(PtSequen ptSequen) {
		//保存数据库记录
		super.save(ptSequen);
		//设置清除缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PtSequenService.class.getName(),ptSequen.getId()));
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PtSequenService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PtSequenService.class.getName()));
	}
	
	@Transactional(readOnly = false)
	public void delete(PtSequen ptSequen) {
		//清除记录缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PtSequenService.class.getName(),ptSequen.getId()));
		//删除数据库记录
		super.delete(ptSequen);
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PtSequenService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PtSequenService.class.getName()));
	}

	@Transactional(readOnly = false)
	public void deleteByLogic(PtSequen ptSequen) {
		//清除记录缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PtSequenService.class.getName(),ptSequen.getId()));
		//逻辑删除数据库记录
		super.deleteByLogic(ptSequen);
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PtSequenService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PtSequenService.class.getName()));
	}

}