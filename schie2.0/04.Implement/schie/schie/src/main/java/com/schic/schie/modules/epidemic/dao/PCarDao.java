/**
 * *
 */
package com.schic.schie.modules.epidemic.dao;

import com.jeespring.common.persistence.InterfaceBaseDao;
import com.schic.schie.modules.epidemic.entity.PCar;
import org.apache.ibatis.annotations.Mapper;

/**
 * 病患同汽车实体类DAO接口
 * @author Leo
 * @version 2020-03-17
 */
@Mapper
public interface PCarDao extends InterfaceBaseDao<PCar> {
	
}
