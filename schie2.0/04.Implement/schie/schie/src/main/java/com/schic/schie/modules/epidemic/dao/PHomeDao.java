/**
 * *
 */
package com.schic.schie.modules.epidemic.dao;

import com.jeespring.common.persistence.InterfaceBaseDao;
import com.schic.schie.modules.epidemic.entity.PHome;
import org.apache.ibatis.annotations.Mapper;

/**
 * 病患同户实体类DAO接口
 * @author Leo
 * @version 2020-03-17
 */
@Mapper
public interface PHomeDao extends InterfaceBaseDao<PHome> {
	
}
