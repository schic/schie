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

import org.schic.common.utils.StringUtils;
import org.schic.modules.sys.entity.Office;
import org.schic.modules.sys.utils.UserUtils;

/**
 * 
 * @Description: 字段类型转换 
 * @author Caiwb
 * @date 2019年4月29日 下午4:16:37
 */
public class OfficeType {

	private OfficeType() {

	}

	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		for (Office e : UserUtils.getOfficeList()) {
			if (StringUtils.trimToEmpty(val).equals(e.getName())) {
				return e;
			}
		}
		return null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null && ((Office) val).getName() != null) {
			return ((Office) val).getName();
		}
		return "";
	}
}
