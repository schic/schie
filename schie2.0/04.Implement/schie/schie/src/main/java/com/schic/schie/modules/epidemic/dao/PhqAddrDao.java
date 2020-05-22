/**
 *
 */
package com.schic.schie.modules.epidemic.dao;

import com.jeespring.common.persistence.InterfaceBaseDao;
import com.schic.schie.modules.epidemic.entity.PhqAddr;
import org.apache.ibatis.annotations.Mapper;

/**
 * 高频地址实体DAO接口
 * @author leo
 * @version 2020-03-11
 */
@Mapper
public interface PhqAddrDao extends InterfaceBaseDao<PhqAddr> {
	
}
