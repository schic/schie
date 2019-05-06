package org.schic.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.schic.common.service.TreeService;
import org.schic.modules.sys.dao.AreaDao;
import org.schic.modules.sys.entity.Area;
import org.schic.modules.sys.utils.UserUtils;

/**
 * 
 * @Description: 区域Service 
 * @author Caiwb
 * @date 2019年5月6日 上午11:10:24
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends TreeService<AreaDao, Area> {

	public List<Area> findAll() {
		return UserUtils.getAreaList();
	}

	@Override
	@Transactional(readOnly = false)
	public void save(Area area) {
		super.save(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Area area) {
		super.delete(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}

}
