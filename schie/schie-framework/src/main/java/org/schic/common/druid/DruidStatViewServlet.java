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
package org.schic.common.druid;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import com.alibaba.druid.support.http.StatViewServlet;

/**
 * 
 * @Description: StatViewServlet
 * @author Caiwb
 * @date 2019年4月29日 上午11:18:07
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/druid/*", initParams = {
		@WebInitParam(name = "allow", value = "127.0.0.1"), // IP白名单 (没有配置或者为空，则允许所有访问,逗号分割多个ip)
		@WebInitParam(name = "deny", value = ""), // IP黑名单 (存在共同时，deny优先于allow)
		//WebInitParam(name="loginUsername",value=""),// 用户名
		//WebInitParam(name="loginPassword",value=""),// 密码
		@WebInitParam(name = "resetEnable", value = "false")// 禁用HTML页面上的“Reset All”功能
})
public class DruidStatViewServlet extends StatViewServlet {

}