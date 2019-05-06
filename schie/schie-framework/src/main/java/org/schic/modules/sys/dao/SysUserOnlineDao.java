package org.schic.modules.sys.dao;

import org.schic.common.persistence.InterfaceBaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.schic.modules.sys.entity.SysUserOnline;

/**
 * 
 * @Description: 在线用户记录DAO接口
 * @author Caiwb
 * @date 2019年5月6日 上午10:39:21
 */
@Mapper
public interface SysUserOnlineDao extends InterfaceBaseDao<SysUserOnline> {

}