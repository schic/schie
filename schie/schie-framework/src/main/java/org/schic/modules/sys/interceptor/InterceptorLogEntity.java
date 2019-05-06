package org.schic.modules.sys.interceptor;

import org.schic.modules.sys.entity.Log;

/**
 * 
 * @Description:  
 * @author Caiwb
 * @date 2019年5月6日 上午10:51:51
 */
public class InterceptorLogEntity {
	private Log log;
	private Object handler;
	private Exception ex;

	public InterceptorLogEntity(Log log, Object handler, Exception ex) {
		this.log = log;
		this.handler = handler;
		this.ex = ex;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public Object getHandler() {
		return handler;
	}

	public void setHandler(Object handler) {
		this.handler = handler;
	}

	public Exception getEx() {
		return ex;
	}

	public void setEx(Exception ex) {
		this.ex = ex;
	}
}
