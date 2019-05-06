package org.schic.modules.monitor.dao;

import org.schic.common.persistence.InterfaceBaseDao;
import org.schic.modules.monitor.entity.Monitor;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @Description: 系统监控DAO接口
 * @author Caiwb
 * @date 2019年5月5日 下午5:23:50
 */
@Mapper
public interface MonitorDao extends InterfaceBaseDao<Monitor> {

}