/**
 * *
 */
package com.schic.schie.modules.epidemic.dao;

import com.jeespring.common.persistence.InterfaceBaseDao;
import com.schic.schie.modules.epidemic.entity.Patients;
import org.apache.ibatis.annotations.Mapper;

/**
 * 病患实体类DAO接口
 * @author Leo
 * @version 2020-03-11
 */
@Mapper
public interface PatientsDao extends InterfaceBaseDao<Patients> {
	
}