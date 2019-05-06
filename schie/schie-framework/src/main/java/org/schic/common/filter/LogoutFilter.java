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
package org.schic.common.filter;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.schic.common.security.ShiroUtils;
import org.schic.common.utils.StringUtils;
import org.schic.modules.monitor.entity.OnlineSession;
import org.schic.modules.sys.entity.SysUserOnline;
import org.schic.modules.sys.entity.User;
import org.schic.modules.sys.service.SysUserOnlineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 
 * @Description: 退出过滤器
 * @author Caiwb
 * @date 2019年4月29日 上午11:21:41
 */
public class LogoutFilter
		extends
			org.apache.shiro.web.filter.authc.LogoutFilter {
	private SysUserOnlineService sysUserOnlineService;

	private static final Logger log = LoggerFactory
			.getLogger(LogoutFilter.class);

	/**
	 * 退出后重定向的地址
	 */
	private String loginUrl = "/admin/login";

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	@Override
	protected boolean preHandle(ServletRequest request,
			ServletResponse response) throws Exception {
		try {
			Subject subject = getSubject(request, response);
			String redirectUrl = getRedirectUrl(request, response, subject);
			try {
				User user = ShiroUtils.getUser();
				if (StringUtils.isNotNull(user)) {
					SysUserOnline sysUserOnline = new SysUserOnline();
					sysUserOnline.setLoginName(user.getName());
					if (sysUserOnlineService == null) {
						ServletContext context = request.getServletContext();
						ApplicationContext ctx = WebApplicationContextUtils
								.getWebApplicationContext(context);
						sysUserOnlineService = ctx
								.getBean(SysUserOnlineService.class);
					}
					if (sysUserOnlineService != null) {
						sysUserOnline = sysUserOnlineService
								.get(subject.getSession().getId().toString());
						if (sysUserOnline != null) {
							sysUserOnline.setStatus(
									OnlineSession.OnlineStatus.off_line
											.toString());
							sysUserOnlineService.save(sysUserOnline);
						}
					}
					// 记录用户退出日志
					//SystemLogUtils.log(loginName, Constants.LOGOUT, MessageUtils.message("user.logout.success"));
				}
				// 退出登录
				subject.logout();
			} catch (SessionException ise) {
				log.error("logout fail.", ise);
			}
			issueRedirect(request, response, redirectUrl);
		} catch (Exception e) {
			log.error(
					"Encountered session exception during logout.  This can generally safely be ignored.",
					e);
		}
		return false;
	}

	/**
	 * 退出跳转URL
	 */
	@Override
	protected String getRedirectUrl(ServletRequest request,
			ServletResponse response, Subject subject) {
		String url = getLoginUrl();
		if (StringUtils.isNotEmpty(url)) {
			return url;
		}
		return super.getRedirectUrl(request, response, subject);
	}

}
