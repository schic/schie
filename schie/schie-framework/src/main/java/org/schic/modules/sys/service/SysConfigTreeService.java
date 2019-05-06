package org.schic.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.schic.common.service.TreeService;
import org.schic.common.utils.StringUtils;
import org.schic.modules.sys.entity.SysConfigTree;
import org.schic.modules.sys.dao.SysConfigTreeDao;

/**
 * 
 * @Description: 系统配置Service
 * @author Caiwb
 * @date 2019年5月6日 上午11:12:24
 */
@Service
@Transactional(readOnly = true)
public class SysConfigTreeService
		extends
			TreeService<SysConfigTreeDao, SysConfigTree> {

	@Override
	public List<SysConfigTree> findList(SysConfigTree sysConfig) {
		if (StringUtils.isNotBlank(sysConfig.getParentIds())) {
			sysConfig.setParentIds("," + sysConfig.getParentIds() + ",");
		}
		return super.findList(sysConfig);
	}

	@Override
	@Transactional(readOnly = false)
	public void save(SysConfigTree sysConfig) {
		super.save(sysConfig);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(SysConfigTree sysConfig) {
		super.delete(sysConfig);
	}

}