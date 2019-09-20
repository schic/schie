/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.exask.dao;

import com.jeespring.common.persistence.InterfaceBaseDao;
import com.schic.schie.modules.exask.entity.ExBatchLog;

import org.apache.ibatis.annotations.Mapper;

/**
 * 保存日志DAO接口
 * @author leodeyang
 * @version 2019-08-09
 */
@Mapper
public interface ExBatchLogDao extends InterfaceBaseDao<ExBatchLog> {
	
}