/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.database.dao;

import com.jeespring.common.persistence.InterfaceBaseDao;
import com.schic.schie.modules.database.entity.ExDb;
import org.apache.ibatis.annotations.Mapper;

/**
 * DBDAO接口
 * @author DHP
 * @version 2019-08-07
 */
@Mapper
public interface ExDbDao extends InterfaceBaseDao<ExDb> {
	
}
