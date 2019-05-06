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
package org.schic.common.persistence;

import java.util.List;

/**
 * 
 * @Description: DAO支持类实现
 * @author Caiwb
 * @date 2019年4月29日 下午2:56:57 
 * @param <T>
 */
public interface TreeDao<T extends TreeEntity<T>> extends InterfaceBaseDao<T> {

	/**
	 * 找到所有子节点
	 * @param entity
	 * @return
	 */
	List<T> findByParentIdsLike(T entity);

	/**
	 * 更新所有父节点字段
	 * @param entity
	 * @return
	 */
	int updateParentIds(T entity);

}