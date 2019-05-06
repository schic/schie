package org.schic.modules.sys.dao;

import org.schic.common.persistence.TreeDao;
import org.schic.modules.sys.entity.Office;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @Description: 机构DAO接口
 * @author Caiwb
 * @date 2019年5月6日 上午10:36:22
 */
@Mapper
public interface OfficeDao extends TreeDao<Office> {

	Office getByCode(String code);
}
