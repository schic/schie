package org.schic.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.schic.common.utils.CacheUtils;
import org.schic.common.service.AbstractBaseService;
import org.schic.modules.sys.dao.DictDao;
import org.schic.modules.sys.entity.Dict;
import org.schic.modules.sys.utils.DictUtils;

/**
 * 
 * @Description: 字典Service
 * @author Caiwb
 * @date 2019年5月6日 上午11:10:37
 */
@Service
@Transactional(readOnly = true)
public class DictService extends AbstractBaseService<DictDao, Dict> {

	/**
	 * 查询字段类型列表
	 * @return
	 */
	public List<String> findTypeList() {
		return dao.findTypeList(new Dict());
	}

	@Override
	@Transactional(readOnly = false)
	public void save(Dict dict) {
		super.save(dict);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Dict dict) {
		super.delete(dict);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}

}
