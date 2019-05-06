package org.schic.modules.server.dao;

import org.schic.common.persistence.InterfaceBaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.schic.modules.server.entity.SysServer;

/**
 * 
 * @Description: 服务器监控DAO接口
 * @author Caiwb
 * @date 2019年5月6日 上午10:16:39
 */
@Mapper
public interface SysServerDao extends InterfaceBaseDao<SysServer> {

}