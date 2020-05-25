/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.resource.dao;

import com.jeespring.common.persistence.InterfaceBaseDao;
import com.schic.schie.modules.resource.entity.ExResources;
import org.apache.ibatis.annotations.Mapper;

/**
 * 资源管理DAO接口
 * @author daihp
 * @version 2019-07-24
 */
@Mapper
public interface ExResourcesDao extends InterfaceBaseDao<ExResources> {
    
}
