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
package org.schic.common.constant;

/**
 * 
 * @Description: Shiro通用常量
 * @author Caiwb
 * @date 2019年4月29日 上午11:16:52
 */
public interface ShiroConstants {
	/**
	 * 当前登录的用户
	 */
	public static final String CURRENT_USER = "currentUser";

	/**
	 * 用户名
	 */
	public static final String CURRENT_USERNAME = "username";

	/**
	 * 消息key
	 */
	public static String MESSAGE = "message";

	/**
	 * 错误key
	 */
	public static String ERROR = "errorMsg";

	/**
	 * 编码格式
	 */
	public static String ENCODING = "UTF-8";

	/**
	 * 当前在线会话
	 */
	public String ONLINE_SESSION = "online_session";

	/**
	 * 验证码key
	 */
	public static final String CURRENT_CAPTCHA = "captcha";

	/**
	 * 验证码开关
	 */
	public static final String CURRENT_EBABLED = "captchaEbabled";

	/**
	 * 验证码开关
	 */
	public static final String CURRENT_TYPE = "captchaType";

	/**
	 * 验证码
	 */
	public static final String CURRENT_VALIDATECODE = "validateCode";

	/**
	 * 验证码错误
	 */
	public static final String CAPTCHA_ERROR = "captchaError";

}
