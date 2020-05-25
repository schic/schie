/**
 *
 */
package com.schic.schie.modules.epidemic.dao;

import com.jeespring.common.persistence.InterfaceBaseDao;
import com.schic.schie.modules.epidemic.entity.PfData;
import org.apache.ibatis.annotations.Mapper;

/**
 * 全量数据实体DAO接口
 * @author Leo
 * @version 2020-03-11
 */
@Mapper
public interface PfDataDao extends InterfaceBaseDao<PfData> {
	
}
