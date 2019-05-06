/* 
 * 创建日期 2019年4月29日
 *
 * 四川健康久远科技有限公司
 * 电话： 
 * 传真： 
 * 邮编： 
 * 地址：成都市武侯区
 * 版权所有
 */
package org.schic.common.persistence.proxy;

import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

/**
 * 
 * @Description:  
 * @author Caiwb
 * @date 2019年4月29日 下午2:32:40
 */
public class PaginationMapperRegistry extends MapperRegistry {
	public PaginationMapperRegistry(Configuration config) {
		super(config);
	}

	@Override
	public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
		if (!hasMapper(type)) {
			throw new BindingException(
					"Type " + type + " is not known to the MapperRegistry.");
		}
		try {
			return PaginationMapperProxy.newMapperProxy(type, sqlSession);
		} catch (Exception e) {
			throw new BindingException(
					"Error getting mapper instance. Cause: " + e, e);
		}
	}
}
