package org.schic.modules.sys.dao;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.schic.modules.monitor.entity.OnlineSession;
import org.schic.modules.sys.entity.SysUserOnline;
import org.schic.modules.sys.service.SysUserOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 
 * @Description: 针对自定义的ShiroSession的db操作
 * @author Caiwb
 * @date 2019年5月6日 上午10:36:37
 */

@Service
public class OnlineSessionDAO extends EnterpriseCacheSessionDAO {
	/**
	 * 同步session到数据库的周期 单位为毫秒（默认1分钟）
	 */
	@Value("${shiro.session.dbSyncPeriod}")
	private int dbSyncPeriod = 1;

	@Autowired
	private SysUserOnlineService sysUserOnlineService;

	@Autowired
	private OnlineSessionFactory onlineSessionFactory;

	public OnlineSessionDAO() {
		super();
	}

	public OnlineSessionDAO(long expireTime) {
		super();
	}

	/**
	 * 根据会话ID获取会话
	 *
	 * @param sessionId 会话ID
	 * @return ShiroSession
	 */
	@Override
	protected Session doReadSession(Serializable sessionId) {
		SysUserOnline sysUserOnline = new SysUserOnline();
		if (sysUserOnlineService == null) {
			sysUserOnline.setId(String.valueOf(sessionId));
		} else {
			sysUserOnline = sysUserOnlineService.get(String.valueOf(sessionId));
		}
		if (sysUserOnline == null) {
			return null;
		}
		return onlineSessionFactory.createSession(sysUserOnline);
	}
	/**
	 * 当会话过期/停止（如用户退出时）属性等会调用
	 */
	@Override
	protected void doDelete(Session session) {
		OnlineSession onlineSession = (OnlineSession) session;
		if (null == onlineSession) {
			return;
		}
		onlineSession.setStatus(OnlineSession.OnlineStatus.off_line);
		SysUserOnline sysUserOnline = new SysUserOnline();
		sysUserOnline.setId(String.valueOf(onlineSession.getId()));
		sysUserOnlineService.delete(sysUserOnline);
	}
}
