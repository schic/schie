/**
 * *
 */
package com.schic.schie.modules.epidemic.dao;

import com.jeespring.common.persistence.InterfaceBaseDao;
import com.schic.schie.modules.epidemic.entity.PTrain;
import org.apache.ibatis.annotations.Mapper;

/**
 * 病患同火车实体类DAO接口
 * @author Leo
 * @version 2020-03-17
 */
@Mapper
public interface PTrainDao extends InterfaceBaseDao<PTrain> {
	
}
