package org.schic.modules.sys.dao;

import java.util.List;

import org.schic.common.persistence.InterfaceBaseDao;
import org.schic.modules.sys.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @Description: 菜单DAO接口
 * @author Caiwb
 * @date 2019年5月6日 上午10:36:10
 */
@Mapper
public interface MenuDao extends InterfaceBaseDao<Menu> {

	List<Menu> findByParentIdsLike(Menu menu);

	List<Menu> findByUserId(Menu menu);

	int updateParentIds(Menu menu);

	int updateSort(Menu menu);

}
