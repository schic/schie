/**
 * 
 */
package com.schic.schie.modules.exjob.dao;

import org.apache.ibatis.annotations.Mapper;

import com.jeespring.common.persistence.InterfaceBaseDao;
import com.schic.schie.modules.exjob.entity.ExJobLog;

/**
 * 
* <p>Title: ExJobLogDao</p>  
* <p>Description: 定时任务调度日志表DAO接口</p>  
* @author caiwb 
* @date 2019年8月19日
 */
@Mapper
public interface ExJobLogDao extends InterfaceBaseDao<ExJobLog> {

}
