/**
 * *
 */
package com.schic.schie.modules.epidemic.dao;

import com.jeespring.common.persistence.InterfaceBaseDao;
import com.schic.schie.modules.epidemic.entity.PtSequen;
import org.apache.ibatis.annotations.Mapper;

/**
 * 时序高频实体DAO接口
 * @author Leo
 * @version 2020-03-11
 */
@Mapper
public interface PtSequenDao extends InterfaceBaseDao<PtSequen> {
	
}
