package org.schic.modules.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import org.schic.common.persistence.TreeDao;
import org.schic.modules.sys.entity.SysConfigTree;

/**
 * 
 * @Description: 系统配置DAO接口
 * @author Caiwb
 * @date 2019年5月6日 上午10:38:38
 */
@Mapper
public interface SysConfigTreeDao extends TreeDao<SysConfigTree> {

}