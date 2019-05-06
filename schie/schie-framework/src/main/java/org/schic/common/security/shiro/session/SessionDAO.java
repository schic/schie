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
package org.schic.common.security.shiro.session;

import java.util.Collection;

import org.apache.shiro.session.Session;

/**
 * 
 * @Description:  
 * @author Caiwb
 * @date 2019年4月29日 下午3:32:01
 */
public interface SessionDAO
		extends
			org.apache.shiro.session.mgt.eis.SessionDAO {

	/**
	 * 获取活动会话
	 * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
	 * @return
	 */
	Collection<Session> getActiveSessions(boolean includeLeave);

	/**
	 * 获取活动会话
	 * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
	 * @param principal 根据登录者对象获取活动会话
	 * @param filterSession 不为空，则过滤掉（不包含）这个会话。
	 * @return
	 */
	Collection<Session> getActiveSessions(boolean includeLeave,
			Object principal, Session filterSession);

}
