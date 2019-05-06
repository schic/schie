package org.schic.modules.sys.dao;

import org.apache.ibatis.annotations.Mapper;

import org.schic.common.persistence.InterfaceBaseDao;
import org.schic.modules.sys.entity.Role;

/**
 * 
 * @Description: 角色DAO接口
 * @author Caiwb
 * @date 2019年5月6日 上午10:38:12
 */
@Mapper
public interface RoleDao extends InterfaceBaseDao<Role> {

	Role getByName(Role role);

	Role getByEnname(Role role);

	/**
	 * 维护角色与菜单权限关系
	 * @param role
	 * @return
	 */
	int deleteRoleMenu(Role role);

	int insertRoleMenu(Role role);

	/**
	 * 维护角色与公司部门关系
	 * @param role
	 * @return
	 */
	int deleteRoleOffice(Role role);

	int insertRoleOffice(Role role);

}
