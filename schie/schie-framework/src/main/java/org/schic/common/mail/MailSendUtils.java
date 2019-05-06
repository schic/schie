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
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * 
 * @Description: 简单邮件（不带附件的邮件）发送器   
 * @author Caiwb
 * @date 2019年4月29日 上午11:26:04
 */
public class MailSendUtils {
	/**   
	  * 以文本格式发送邮件   
	  * @param mailInfo 待发送的邮件的信息   
	  */
	public boolean sendTextMail(MailBody mailInfo) throws Exception {
		// 判断是否需要身份认证    
		MailAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		if (mailInfo.isValidate()) {
			// 如果需要身份认证，则创建一个密码验证器    
			authenticator = new MailAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session    
		Session sendMailSession = Session.getDefaultInstance(pro,
				authenticator);
		// logBefore(logger, "构造一个发送邮件的session");

		// 根据session创建一个邮件消息    
		Message mailMessage = new MimeMessage(sendMailSession);
		// 创建邮件发送者地址    
		Address from = new InternetAddress(mailInfo.getFromAddress());
		// 设置邮件消息的发送者    
		mailMessage.setFrom(from);
		// 创建邮件的接收者地址，并设置到邮件消息中    
		Address to = new InternetAddress(mailInfo.getToAddress());
		mailMessage.setRecipient(Message.RecipientType.TO, to);
		// 设置邮件消息的主题    
		mailMessage.setSubject(mailInfo.getSubject());
		// 设置邮件消息发送的时间    
		mailMessage.setSentDate(new Date());
		// 设置邮件消息的主要内容    
		String mailContent = mailInfo.getContent();
		mailMessage.setText(mailContent);
		// 发送邮件    
		Transport.send(mailMessage);
		System.out.println("发送成功！");
		return true;
	}

	/**   
	  * 以HTML格式发送邮件   
	  * @param mailInfo 待发送的邮件信息   
	  */
	public boolean sendHtmlMail(MailBody mailInfo) throws Exception {
		// 判断是否需要身份认证    
		MailAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		//如果需要身份认证，则创建一个密码验证器     
		if (mailInfo.isValidate()) {
			authenticator = new MailAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session    
		Session sendMailSession = Session.getDefaultInstance(pro,
				authenticator);

		// 根据session创建一个邮件消息    
		Message mailMessage = new MimeMessage(sendMailSession);
		// 创建邮件发送者地址    
		Address from = new InternetAddress(mailInfo.getFromAddress());
		// 设置邮件消息的发送者    
		mailMessage.setFrom(from);
		// 创建邮件的接收者地址，并设置到邮件消息中    
		Address to = new InternetAddress(mailInfo.getToAddress());
		// Message.RecipientType.TO属性表示接收者的类型为TO    
		mailMessage.setRecipient(Message.RecipientType.TO, to);
		// 设置邮件消息的主题    
		mailMessage.setSubject(mailInfo.getSubject());
		// 设置邮件消息发送的时间    
		mailMessage.setSentDate(new Date());
		// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象    
		Multipart mainPart = new MimeMultipart();
		// 创建一个包含HTML内容的MimeBodyPart    
		BodyPart html = new MimeBodyPart();
		// 设置HTML内容    
		html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
		mainPart.addBodyPart(html);
		// 将MiniMultipart对象设置为邮件内容    
		mailMessage.setContent(mainPart);
		// 发送邮件    
		Transport.send(mailMessage);
		return true;
	}

	/**
	 * @param smtp
	 *            邮件服务器
	 * @param port
	 *            端口
	 * @param email
	 *            本邮箱账号
	 * @param paw
	 *            本邮箱密码
	 * @param toEMAIL
	 *            对方箱账号
	 * @param title
	 *            标题
	 * @param content
	 *            内容
	 * @param type
	 *            1：文本格式;2：HTML格式
	 */
	public static boolean sendEmail(String smtp, String port, String email,
			String paw, String toEMAIL, String title, String content,
			String type) {

		// 这个类主要是设置邮件
		MailBody mailInfo = new MailBody();

		mailInfo.setMailServerHost(smtp);
		mailInfo.setMailServerPort(port);
		mailInfo.setValidate(true);
		mailInfo.setUserName(email);
		mailInfo.setPassword(paw);
		mailInfo.setFromAddress(email);
		mailInfo.setToAddress(toEMAIL);
		mailInfo.setSubject(title);
		mailInfo.setContent(content);
		// 这个类主要来发送邮件

		MailSendUtils sms = new MailSendUtils();
		try {
			if ("1".equals(type)) {
				return sms.sendTextMail(mailInfo);
			} else {
				return sms.sendHtmlMail(mailInfo);
			}
		} catch (Exception e) {
			return false;
		}

	}

}
