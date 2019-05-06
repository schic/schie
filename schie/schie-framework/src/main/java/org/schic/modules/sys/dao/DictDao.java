package org.schic.modules.sys.dao;

import java.util.List;

import org.schic.common.persistence.InterfaceBaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.schic.modules.sys.entity.Dict;

/**
 * 
 * @Description: 字典DAO接口
 * @author Caiwb
 * @date 2019年5月6日 上午10:35:14
 */
@Mapper
public interface DictDao extends InterfaceBaseDao<Dict> {

	List<String> findTypeList(Dict dict);

}
