package org.schic.modules.sys.dao;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.web.session.mgt.WebSessionContext;
import org.schic.common.utils.IpUtils;
import org.schic.common.utils.ServletUtils;
import org.schic.common.utils.StringUtils;
import org.schic.modules.monitor.entity.OnlineSession;
import org.schic.modules.sys.entity.SysUserOnline;
import org.springframework.stereotype.Service;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * 
 * @Description: 自定义sessionFactory会话
 * @author Caiwb
 * @date 2019年5月6日 上午10:37:26
 */
@Service
public class OnlineSessionFactory implements SessionFactory {
	public Session createSession(SysUserOnline userOnline) {
		OnlineSession onlineSession = userOnline.getSession();
		if (StringUtils.isNotNull(onlineSession)
				&& onlineSession.getId() == null) {
			onlineSession.setId(userOnline.getId());
		}
		return userOnline.getSession();
	}

	@Override
	public Session createSession(SessionContext initData) {
		OnlineSession session = new OnlineSession();
		if (initData instanceof WebSessionContext) {
			WebSessionContext sessionContext = (WebSessionContext) initData;
			HttpServletRequest request = (HttpServletRequest) sessionContext
					.getServletRequest();
			if (request != null) {
				UserAgent userAgent = UserAgent.parseUserAgentString(
						ServletUtils.getRequest().getHeader("User-Agent"));
				// 获取客户端操作系统
				String os = userAgent.getOperatingSystem().getName();
				// 获取客户端浏览器
				String browser = userAgent.getBrowser().getName();
				session.setHost(IpUtils.getIpAddr(request));
				session.setBrowser(browser);
				session.setOs(os);
			}
		}
		return session;
	}
}
