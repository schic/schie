package org.schic.modules.monitor.service;

import org.schic.common.service.AbstractBaseService;
import org.schic.modules.monitor.dao.MonitorDao;
import org.schic.modules.monitor.entity.Monitor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @Description: 系统监控Service
 * @author Caiwb
 * @date 2019年5月5日 下午5:25:08
 */
@Service
@Transactional(readOnly = true)
public class MonitorService extends AbstractBaseService<MonitorDao, Monitor> {

	@Override
	@Transactional(readOnly = false)
	public void save(Monitor monitor) {
		super.save(monitor);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Monitor monitor) {
		super.delete(monitor);
	}

}