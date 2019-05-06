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
 * @Description: 类似hibernate的Dialect,但只精简出分页部分
 * @author Caiwb
 * @date 2019年4月29日 上午11:50:21
 */
public interface Dialect {

	/**
	 * 数据库本身是否支持分页当前的分页查询方式
	 * 如果数据库不支持的话，则不进行数据库分页
	 *
	 * @return true：支持当前的分页查询方式
	 */
	boolean supportsLimit();

	/**
	 * 将sql转换为分页SQL，分别调用分页sql
	 *
	 * @param sql    SQL语句
	 * @param offset 开始条数
	 * @param limit  每页显示多少纪录条数
	 * @return 分页查询的sql
	 */
	String getLimitString(String sql, int offset, int limit);

}
