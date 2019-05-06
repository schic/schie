package org.schic.modules.sys.dao;

import org.apache.ibatis.annotations.Mapper;

import org.schic.common.persistence.InterfaceBaseDao;
import org.schic.modules.sys.entity.Log;

/**
 * 
 * @Description: 日志DAO接口
 * @author Caiwb
 * @date 2019年5月6日 上午10:35:26
 */
@Mapper
public interface LogDao extends InterfaceBaseDao<Log> {

	void empty();
}
