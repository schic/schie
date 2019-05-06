package org.schic.modules.sys.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.schic.common.service.AbstractService;
import org.schic.common.utils.DateUtils;
import org.schic.modules.sys.utils.LogUtils;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @Description: 日志拦截器
 * @author Caiwb
 * @date 2019年5月6日 上午10:52:03
 */
@Component("controllerLogInterceptor")
public class LogInterceptor extends AbstractService
		implements
			HandlerInterceptor {

	private static final ThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<>(
			"ThreadLocal StartTime");

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		startTimeThreadLocal.set(System.currentTimeMillis());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if (!"/error".equals(request.getRequestURI())) {
			try {
				logger.info("URI: {},耗时：{}   ",
						request.getRequestURI() + "-" + request.getMethod(),
						DateUtils.formatDateTime(System.currentTimeMillis()
								- startTimeThreadLocal.get()));
			} catch (Exception e) {
				e.toString();
			}
		}
		//删除线程变量中的数据，防止内存泄漏
		startTimeThreadLocal.remove();
		// 保存日志
		LogUtils.saveLog(request, handler, ex, null);
	}
}
