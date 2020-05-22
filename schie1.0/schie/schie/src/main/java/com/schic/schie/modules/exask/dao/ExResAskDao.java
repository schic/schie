/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a>
 * All rights reserved..
 */
package com.schic.schie.modules.exask.dao;

import com.jeespring.common.persistence.InterfaceBaseDao;
import com.schic.schie.modules.exask.entity.ExResAsk;

import org.apache.ibatis.annotations.Mapper;

/**
 * 资源申请表DAO接口
 * @author leodeyang
 * @version 2019-08-12
 */
@Mapper
public interface ExResAskDao extends InterfaceBaseDao<ExResAsk> {

    int updateSubNow(ExResAsk resAsk);
}
