/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.nodes.dao;

import com.jeespring.common.persistence.InterfaceBaseDao;
import com.jeespring.modules.sys.entity.Office;
import com.schic.schie.modules.nodes.entity.ExNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 节点管理DAO接口
 * @author DHP
 * @version 2019-08-07
 */
@Mapper
public interface ExNodeDao extends InterfaceBaseDao<ExNode> {
}