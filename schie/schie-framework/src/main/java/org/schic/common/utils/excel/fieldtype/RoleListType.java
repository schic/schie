/* 
 * 创建日期 2019年4月29日
 *
 * 四川健康久远科技有限公司
 * 电话： 
 * 传真： 
 * 邮编： 
 * 地址：成都市武侯区
 * 版权所有
 */
package org.schic.common.utils.excel.fieldtype;

import java.util.List;

import com.google.common.collect.Lists;
import org.schic.common.utils.Collections3;
import org.schic.common.utils.SpringContextHolder;
import org.schic.common.utils.StringUtils;
import org.schic.modules.sys.entity.Role;
import org.schic.modules.sys.service.SystemService;

/**
 * 
 * @Description: 字段类型转换 
 * @author Caiwb
 * @date 2019年4月29日 下午4:17:03
 */
public class RoleListType {

	private static SystemService systemService = SpringContextHolder
			.getBean(SystemService.class);

	private RoleListType() {

	}

	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		List<Role> roleList = Lists.newArrayList();
		List<Role> allRoleList = systemService.findAllRole();
		for (String s : StringUtils.split(val, ",")) {
			for (Role e : allRoleList) {
				if (StringUtils.trimToEmpty(s).equals(e.getName())) {
					roleList.add(e);
				}
			}
		}
		return roleList.isEmpty() ? null : roleList;
	}

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null) {
			@SuppressWarnings("unchecked")
			List<Role> roleList = (List<Role>) val;
			return Collections3.extractToString(roleList, "name", ", ");
		}
		return "";
	}

}
