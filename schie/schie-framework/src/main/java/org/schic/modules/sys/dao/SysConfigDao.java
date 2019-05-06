package org.schic.modules.sys.dao;

import org.schic.common.persistence.InterfaceBaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.schic.modules.sys.entity.SysConfig;

/**
 * 
 * @Description: 系统配置DAO接口
 * @author Caiwb
 * @date 2019年5月6日 上午10:38:27
 */
@Mapper
public interface SysConfigDao extends InterfaceBaseDao<SysConfig> {

}