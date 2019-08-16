/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.test.dao.tree;

import com.jeespring.common.persistence.TreeDao;
import com.schic.schie.modules.test.entity.tree.TestTree;
import org.apache.ibatis.annotations.Mapper;

/**
 * 树DAO接口
 * @author JeeSpring
 * @version 2018-12-13
 */
@Mapper
public interface TestTreeDao extends TreeDao<TestTree> {
	
}