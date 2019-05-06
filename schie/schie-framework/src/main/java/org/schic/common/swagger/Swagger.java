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
package org.schic.common.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * @Description:  swagger
 * @author Caiwb
 * @date 2019年4月29日 下午4:02:52
 */
@Configuration
@EnableSwagger2
public class Swagger {

	@Bean("Schic云接口")
	public Docket createJeeSpringRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("Schic云接口")
				.apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors
						.withMethodAnnotation(ApiOperation.class)) //这里采用包含注解的方式来确定要显示的接口
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Swagger2构建RESTful APIs")
				.description("相关文章").termsOfServiceUrl("http://www.schic.com/")
				.contact("contact").version("1.0").build();
	}

}