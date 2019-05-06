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

import org.schic.common.persistence.InterfaceBaseDao;
import org.schic.common.persistence.AbstractBaseEntity;
import org.schic.common.persistence.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * @Description: Service基类
 * @author Caiwb
 * @date 2019年4月29日 下午3:45:27 
 * @param <Dao>
 * @param <T>
 */
@Transactional(readOnly = true)
public abstract class AbstractBaseService<Dao extends InterfaceBaseDao<T>, T extends AbstractBaseEntity<T>>
		extends
			AbstractService {

	/**
	 * 持久层对象
	 */
	@Autowired
	protected Dao dao;

	/**
	 * 获取单条数据
	 *
	 * @param id
	 * @return
	 */
	public T get(String id) {
		return dao.get(id);
	}

	/**
	 * 获取单条数据
	 *
	 * @param entity
	 * @return
	 */
	public T get(T entity) {
		return dao.get(entity);
	}

	/**
	 * 查询统计数据
	 *
	 * @param entity
	 * @return
	 */
	public List<T> total(T entity) {
		return dao.total(entity);
	}

	/**
	 * 查询列表数据
	 *
	 * @param entity
	 * @return
	 */
	public List<T> findList(T entity) {
		return dao.findList(entity);
	}

	/**
	 * 查询所有
	 *
	 * @param entity
	 * @return
	 */
	public List<T> findAllList(T entity) {
		return dao.findAllList(entity);
	}

	/**
	 * 查询分页数据
	 *
	 * @param page   分页对象
	 * @param entity
	 * @return
	 */
	public Page<T> findPage(Page<T> page, T entity) {
		entity.setPage(page);
		page.setList(dao.findList(entity));
		return page;
	}

	/**
	 * 保存数据（插入或更新）
	 *
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void save(T entity) {
		if (entity.getIsNewRecord()) {
			entity.preInsert();
			dao.insert(entity);
		} else {
			entity.preUpdate();
			dao.update(entity);
		}
	}

	/**
	 * 删除数据
	 *
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void delete(T entity) {
		dao.delete(entity);
	}

	/**
	 * 删除数据（逻辑删除，更新del_flag字段为1,在表包含字段del_flag时，可以调用此方法，将数据隐藏）
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public void deleteByLogic(T entity) {
		dao.deleteByLogic(entity);
	}

}
