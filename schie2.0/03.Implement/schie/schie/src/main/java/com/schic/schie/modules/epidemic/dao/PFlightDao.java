/**
 * *
 */
package com.schic.schie.modules.epidemic.dao;

import com.jeespring.common.persistence.InterfaceBaseDao;
import com.schic.schie.modules.epidemic.entity.PFlight;
import org.apache.ibatis.annotations.Mapper;

/**
 * 病患同航班实体类DAO接口
 * @author Leo
 * @version 2020-03-17
 */
@Mapper
public interface PFlightDao extends InterfaceBaseDao<PFlight> {
	
}
