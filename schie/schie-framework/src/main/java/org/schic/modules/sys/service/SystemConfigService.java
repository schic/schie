package org.schic.modules.sys.service;

import org.schic.common.service.AbstractBaseService;
import org.schic.modules.sys.dao.SystemConfigDao;
import org.schic.modules.sys.entity.SystemConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @Description: 系统配置Service
 * @author Caiwb
 * @date 2019年5月6日 上午11:12:59
 */
@Service
@Transactional(readOnly = true)
public class SystemConfigService
		extends
			AbstractBaseService<SystemConfigDao, SystemConfig> {

	@Override
	@Transactional(readOnly = false)
	public void save(SystemConfig systemConfig) {
		super.save(systemConfig);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(SystemConfig systemConfig) {
		super.delete(systemConfig);
	}

}