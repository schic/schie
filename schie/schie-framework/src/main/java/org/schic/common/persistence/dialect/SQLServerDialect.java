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
package org.schic.common.persistence.dialect;

/**
 * 
 * @Description: MSSQLServer 数据库实现分页方言
 * @author Caiwb
 * @date 2019年4月29日 上午11:53:04
 */
public class SQLServerDialect implements Dialect {

	@Override
	public boolean supportsLimit() {
		return true;
	}

	static int getAfterSelectInsertPoint(String sql) {
		int selectIndex = sql.toLowerCase().indexOf("select");
		final int selectDistinctIndex = sql.toLowerCase()
				.indexOf("select distinct");
		return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
	}

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		return getLimit(sql, offset, limit);
	}

	/**
	 * 将sql变成分页sql语句,提供将offset及limit使用占位符号(placeholder)替换.
	 * <pre>
	 * 如mysql
	 * dialect.getLimitString("select * from user", 12, ":offset",0,":limit") 将返回
	 * select * from user limit :offset,:limit
	 * </pre>
	 *
	 * @param sql    实际SQL语句
	 * @param offset 分页开始纪录条数
	 * @param limit  分页每页显示纪录条数
	 * @return 包含占位符的分页sql
	 */
	public String getLimit(String sql, int offset, int limit) {
		if (offset > 0) {
			throw new UnsupportedOperationException("sql server has no offset");
		}
		return new StringBuffer(sql.length() + 8).append(sql)
				.insert(getAfterSelectInsertPoint(sql), " top " + limit)
				.toString();
	}

}
