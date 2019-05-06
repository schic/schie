package org.schic.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.schic.common.service.TreeService;
import org.schic.modules.sys.dao.OfficeDao;
import org.schic.modules.sys.entity.Office;
import org.schic.modules.sys.utils.UserUtils;

/**
 * 
 * @Description: 机构Service
 * @author Caiwb
 * @date 2019年5月6日 上午11:11:00
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends TreeService<OfficeDao, Office> {

	public List<Office> findAll() {
		return UserUtils.getOfficeList();
	}

	public List<Office> findList(Boolean isAll) {
		if (isAll != null && isAll) {
			return UserUtils.getOfficeAllList();
		} else {
			return UserUtils.getOfficeList();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Office> findList(Office office) {
		office.setParentIds(office.getParentIds() + "%");
		return dao.findByParentIdsLike(office);
	}

	@Transactional(readOnly = true)
	public Office getByCode(String code) {
		return dao.getByCode(code);
	}

	@Override
	@Transactional(readOnly = false)
	public void save(Office office) {
		super.save(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Office office) {
		super.delete(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}

}
