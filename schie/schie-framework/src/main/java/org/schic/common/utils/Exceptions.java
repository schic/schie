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
package org.schic.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @Description: 关于异常的工具类. 
 * @author Caiwb
 * @date 2019年4月29日 下午4:35:52
 */
public class Exceptions {

	private Exceptions() {

	}

	/**
	 * 将CheckedException转换为UncheckedException.
	 */
	public static RuntimeException unchecked(Exception e) {
		if (e instanceof RuntimeException) {
			return RuntimeException.class.cast(e);
		} else {
			return new RuntimeException(e);
		}
	}

	/**
	 * 将ErrorStack转化为String.
	 */
	public static String getStackTraceAsString(Throwable e) {
		if (e == null) {
			return "";
		}
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}

	/**
	 * 判断异常是否由某些底层的异常引起.
	 */
	@SuppressWarnings("unchecked")
	public static boolean isCausedBy(Exception ex,
			Class<? extends Exception>... causeExceptionClasses) {
		Throwable cause = ex.getCause();
		while (cause != null) {
			for (Class<? extends Exception> causeClass : causeExceptionClasses) {
				if (causeClass.isInstance(cause)) {
					return true;
				}
			}
			cause = cause.getCause();
		}
		return false;
	}

	/**
	 * 在request中获取异常类
	 *
	 * @param request
	 * @return
	 */
	public static Throwable getThrowable(HttpServletRequest request) {
		Throwable ex = null;
		try {
			if (request.getAttribute("exception") != null) {
				ex = Throwable.class.cast(request.getAttribute("exception"));
			} else if (request
					.getAttribute("javax.servlet.error.exception") != null) {
				ex = Throwable.class.cast(
						request.getAttribute("javax.servlet.error.exception"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ex;
	}

}
