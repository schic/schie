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

import com.ckfinder.connector.FileUploadFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * 
 * @Description: 
 * @author Caiwb
 * @date 2019年4月29日 上午11:20:26
 */
@WebFilter(urlPatterns = "/static/ckfinder/core/connector/java/connector.java", initParams = {
		@WebInitParam(name = "sessionCookieName", value = "JSESSIONID"),
		@WebInitParam(name = "sessionParameterName", value = "jsessionid")})
public class SchicsiteFileUploadFilter extends FileUploadFilter {
}
