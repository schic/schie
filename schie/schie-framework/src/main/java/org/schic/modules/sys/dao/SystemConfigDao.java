package org.schic.modules.sys.dao;

import org.schic.common.persistence.InterfaceBaseDao;
import org.schic.modules.sys.entity.SystemConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @Description: 系统配置DAO接口
 * @author Caiwb
 * @date 2019年5月6日 上午10:39:08
 */
@Mapper
public interface SystemConfigDao extends InterfaceBaseDao<SystemConfig> {

}