/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.epidemic.service;

import com.alibaba.fastjson.JSON;
import com.jeespring.common.persistence.Page;
import com.jeespring.common.redis.RedisUtils;
import com.jeespring.common.service.AbstractBaseService;
import com.schic.schie.modules.epidemic.dao.PatientsDao;
import com.schic.schie.modules.epidemic.entity.Patients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 病患实体类Service
 * @author Leo
 * @version 2020-03-11
 */
@Service
@Transactional(readOnly = true)
public class PatientsService extends AbstractBaseService<PatientsDao, Patients> implements IPatientsService {

	/**
	 * redis caches
	 */
	@Autowired
	private RedisUtils redisUtils;

	public Patients get(String id) {
		//获取数据库数据
		Patients patients=super.get(id);
		return patients;
	}

	public Patients getCache(String id) {
		//获取缓存数据
		Patients patients=(Patients)redisUtils.get(RedisUtils.getIdKey(PatientsService.class.getName(),id));
		if( patients!=null) {return  patients;}
		//获取数据库数据
		patients=super.get(id);
		//设置缓存数据
		redisUtils.set(RedisUtils.getIdKey(PatientsService.class.getName(),id),patients);
		return patients;
	}

	public List<Patients> total(Patients patients) {
		//获取数据库数据
		List<Patients> patientsList=super.total(patients);
		return patientsList;
	}

	public List<Patients> totalCache(Patients patients) {
		//获取缓存数据
		String totalKey = RedisUtils.getTotalKey(PatientsService.class.getName(),JSON.toJSONString(patients));
		List<Patients> patientsList=(List<Patients>)redisUtils.get(totalKey);
		if(patientsList!=null) {return patientsList;}
		//获取数据库数据
		patientsList=super.total(patients);
		//设置缓存数据
		redisUtils.set(totalKey,patientsList);
		return patientsList;
	}

	public List<Patients> findList(Patients patients) {
		//获取数据库数据
		List<Patients> patientsList=super.findList(patients);
		//设置缓存数据
		return patientsList;
	}

	public List<Patients> findListCache(Patients patients) {
		//获取缓存数据
		String findListKey = RedisUtils.getFindListKey(PatientsService.class.getName(),JSON.toJSONString(patients));
		List<Patients> patientsList=(List<Patients>)redisUtils.get(findListKey);
		if(patientsList!=null) {return patientsList;}
		//获取数据库数据
		patientsList=super.findList(patients);
		//设置缓存数据
		redisUtils.set(findListKey,patientsList);
		return patientsList;
	}

	public Patients findListFirst(Patients patients) {;
		//获取数据库数据
		List<Patients> patientsList=super.findList(patients);
		if(patientsList.isEmpty()) {patients=patientsList.get(0);}
		return patients;
	}

	public Patients findListFirstCache(Patients patients) {
		//获取缓存数据
		String findListFirstKey = RedisUtils.getFindListFirstKey(PatientsService.class.getName(),JSON.toJSONString(patients));
		Patients patientsRedis=(Patients)redisUtils.get(findListFirstKey);
		if(patientsRedis!=null) {return patientsRedis;}
		//获取数据库数据
		List<Patients> patientsList=super.findList(patients);
		if(patientsList.isEmpty()) {patients=patientsList.get(0);}
		else {patients=new Patients();}
		//设置缓存数据
		redisUtils.set(findListFirstKey,patients);
		return patients;
	}

	public Page<Patients> findPage(Page<Patients> page, Patients patients) {
		//获取数据库数据
		Page<Patients> pageReuslt=super.findPage(page, patients);
		return pageReuslt;
	}

	public Page<Patients> findPageCache(Page<Patients> page, Patients patients) {
		//获取缓存数据
		String findPageKey =  RedisUtils.getFindPageKey(PatientsService.class.getName(),JSON.toJSONString(page)+JSON.toJSONString(patients));
		Page<Patients> pageReuslt=(Page<Patients>)redisUtils.get(findPageKey);
		if(pageReuslt!=null) {return pageReuslt;}
		//获取数据库数据
		pageReuslt=super.findPage(page, patients);
		//设置缓存数据
		redisUtils.set(findPageKey,pageReuslt);
		return pageReuslt;
	}

	@Transactional(readOnly = false)
	public void save(Patients patients) {
		//保存数据库记录
		super.save(patients);
		//设置清除缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PatientsService.class.getName(),patients.getId()));
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PatientsService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PatientsService.class.getName()));
	}
	
	@Transactional(readOnly = false)
	public void delete(Patients patients) {
		//清除记录缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PatientsService.class.getName(),patients.getId()));
		//删除数据库记录
		super.delete(patients);
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PatientsService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PatientsService.class.getName()));
	}

	@Transactional(readOnly = false)
	public void deleteByLogic(Patients patients) {
		//清除记录缓存数据
		redisUtils.remove(RedisUtils.getIdKey(PatientsService.class.getName(),patients.getId()));
		//逻辑删除数据库记录
		super.deleteByLogic(patients);
		//清除列表和页面缓存数据
		redisUtils.removePattern(RedisUtils.getFindListKeyPattern(PatientsService.class.getName()));
		redisUtils.removePattern(RedisUtils.getFinPageKeyPattern(PatientsService.class.getName()));
	}

}
