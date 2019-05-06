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
package org.schic.common.mail;
import javax.mail.*;

/**
 * 
 * @Description: 
 * @author Caiwb
 * @date 2019年4月29日 上午11:25:01
 */
public class MailAuthenticator extends Authenticator {
	String userName = null;
	String password = null;

	public MailAuthenticator() {
	}
	public MailAuthenticator(String username, String password) {
		this.userName = username;
		this.password = password;
	}
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}
}
