package org.schic.modules.sys.utils;

import java.io.BufferedReader;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.schic.common.utils.StringUtils;
import org.schic.modules.sys.entity.Log;
import org.schic.modules.sys.entity.User;
import org.schic.modules.sys.interceptor.InterceptorLogEntity;
import org.schic.modules.sys.interceptor.LogThread;

/**
 * 
 * @Description: 字典工具类
 * @author Caiwb
 * @date 2019年5月6日 上午11:20:57
 */
public class LogUtils {

	public static final String CACHE_MENU_NAME_PATH_MAP = "menuNamePathMap";

	private LogUtils() {

	}

	/**
	 * 保存日志
	 */
	public static void saveLog(HttpServletRequest request, String title) {
		saveLog(request, null, null, title);
	}

	/**
	 * 保存日志
	 */
	public static void saveLog(HttpServletRequest request, Object handler,
			Exception ex, String title) {
		User user = UserUtils.getUser();
		if (user != null && user.getId() != null) {
			Log log = new Log();
			log.setTitle(title);
			log.setType(ex == null ? Log.TYPE_ACCESS : Log.TYPE_EXCEPTION);
			log.setRemoteAddr(StringUtils.getRemoteAddr(request));
			log.setUserAgent(request.getHeader("user-agent"));
			log.setRequestUri(request.getRequestURI());
			log.setParams(getParameterString(request));
			log.setMethod(request.getMethod());
			log.setCreateBy(user);
			log.setUpdateBy(user);
			log.setUpdateDate(new Date());
			log.setCreateDate(new Date());
			// 异步保存日志
			try {
				InterceptorLogEntity entiry = new InterceptorLogEntity(log,
						handler, ex);
				LogThread.interceptorLogQueue.put(entiry);
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
	}
	public static String getParameterString(HttpServletRequest request) {
		if (request.getQueryString() != null
				&& request.getQueryString().length() > 0) {
			return request.getQueryString();
		}
		Enumeration<String> enumx = request.getParameterNames();
		StringBuilder result = new StringBuilder();
		while (enumx.hasMoreElements()) {
			String paramName = enumx.nextElement();
			String[] values = request.getParameterValues(paramName);
			for (int i = 0; i < values.length; i++) {
				result.append(paramName).append("=").append(values[i])
						.append("&");
			}
		}
		if (result.length() == 0) {
			try {
				BufferedReader br = request.getReader();
				String str = "";
				while ((str = br.readLine()) != null) {
					result.append(str);
				}
			} catch (Exception e) {
			}
		}
		return result.toString();
	}
}
