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
package org.schic.common.service;

import org.schic.common.persistence.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * @Description: Service基类 
 * @author Caiwb
 * @date 2019年4月29日 下午3:47:27 
 * @param <T>
 */
public interface ICommonService<T> {
	/**
	 * 获取单条数据
	 *
	 * @param id
	 * @return
	 */
	T get(String id);

	/**
	 * 获取单条数据
	 *
	 * @param entity
	 * @return
	 */
	T get(T entity);

	/**
	 * 查询列表数据
	 *
	 * @param entity
	 * @return
	 */
	List<T> findList(T entity);

	/**
	 * 查询所有
	 *
	 * @param entity
	 * @return
	 */
	List<T> findAllList(T entity);

	/**
	 * 查询分页数据
	 *
	 * @param page   分页对象
	 * @param entity
	 * @return
	 */
	Page<T> findPage(Page<T> page, T entity);

	/**
	 * 保存数据（插入或更新）
	 *
	 * @param entity
	 */
	@Transactional(readOnly = false)
	void save(T entity);

	/**
	 * 删除数据
	 *
	 * @param entity
	 */
	@Transactional(readOnly = false)
	void delete(T entity);

}
