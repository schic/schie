package org.schic.modules.usercenter.dao;

import org.schic.common.persistence.InterfaceBaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.schic.modules.usercenter.entity.SysUserCenter;

/**
 * 
 * @Description: 用户中心DAO接口
 * @author Caiwb
 * @date 2019年5月6日 下午2:43:41
 */
@Mapper
public interface SysUserCenterDao extends InterfaceBaseDao<SysUserCenter> {

}