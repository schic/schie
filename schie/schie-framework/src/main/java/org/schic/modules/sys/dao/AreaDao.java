package org.schic.modules.sys.dao;

import org.schic.common.persistence.TreeDao;
import org.apache.ibatis.annotations.Mapper;
import org.schic.modules.sys.entity.Area;

/**
 * 
 * @Description: 区域DAO接口
 * @author Caiwb
 * @date 2019年5月6日 上午10:35:02
 */
@Mapper
public interface AreaDao extends TreeDao<Area> {

}
