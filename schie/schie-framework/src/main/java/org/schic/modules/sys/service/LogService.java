package org.schic.modules.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.schic.modules.sys.dao.LogDao;
import org.schic.modules.sys.entity.Log;
import org.schic.common.persistence.Page;
import org.schic.common.service.AbstractBaseService;
import org.schic.common.utils.DateUtils;

/**
 * 
 * @Description: 日志Service
 * @author Caiwb
 * @date 2019年5月6日 上午11:10:48
 */
@Service
@Transactional(readOnly = true)
public class LogService extends AbstractBaseService<LogDao, Log> {

	@Autowired
	private LogDao logDao;

	@Override
	public Page<Log> findPage(Page<Log> page, Log log) {

		// 设置默认时间范围，默认当前月
		if (log.getBeginDate() == null) {
			log.setBeginDate(DateUtils
					.setDays(DateUtils.parseDate(DateUtils.getDate()), 1));
		}
		if (log.getEndDate() == null) {
			log.setEndDate(DateUtils.addMonths(log.getBeginDate(), 1));
		}

		return super.findPage(page, log);

	}

	/**
	 * 删除全部数据
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void empty() {

		logDao.empty();
	}

}
