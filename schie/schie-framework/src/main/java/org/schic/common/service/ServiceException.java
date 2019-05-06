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
package org.schic.common.service;

/**
 * 
 * @Description: Service层公用的Exception, 从由Spring管理事务的函数中抛出时会触发事务回滚.
 * @author Caiwb
 * @date 2019年4月29日 下午3:48:17
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

}
