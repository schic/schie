package org.schic.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.schic.common.service.TreeService;
import org.schic.common.utils.StringUtils;
import org.schic.modules.sys.entity.SysDictTree;
import org.schic.modules.sys.dao.SysDictTreeDao;

/**
 * 
 * @Description: 数据字典Service
 * @author Caiwb
 * @date 2019年5月6日 上午11:12:44
 */
@Service
@Transactional(readOnly = true)
public class SysDictTreeService
		extends
			TreeService<SysDictTreeDao, SysDictTree> {

	@Override
	public List<SysDictTree> findList(SysDictTree sysDict) {
		if (StringUtils.isNotBlank(sysDict.getParentIds())) {
			sysDict.setParentIds("," + sysDict.getParentIds() + ",");
		}
		return super.findList(sysDict);
	}

	@Override
	@Transactional(readOnly = false)
	public void save(SysDictTree sysDict) {
		super.save(sysDict);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(SysDictTree sysDict) {
		super.delete(sysDict);
	}

}