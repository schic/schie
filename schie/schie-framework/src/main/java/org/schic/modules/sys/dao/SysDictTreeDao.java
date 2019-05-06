package org.schic.modules.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import org.schic.common.persistence.TreeDao;
import org.schic.modules.sys.entity.SysDictTree;

/**
 * 
 * @Description: 数据字典DAO接口
 * @author Caiwb
 * @date 2019年5月6日 上午10:38:53
 */
@Mapper
public interface SysDictTreeDao extends TreeDao<SysDictTree> {

}