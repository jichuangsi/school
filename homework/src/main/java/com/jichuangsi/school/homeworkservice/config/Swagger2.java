/**
 * 
 */
package com.jichuangsi.school.homeworkservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author huangjiajun
 *
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
	
	@Value("${com.jichuangsi.school.swagger.enable}")
	private boolean enableSwagger;
	
	// swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).enable(enableSwagger).apiInfo(apiInfo()).select()
				// 为当前包路径
				.apis(RequestHandlerSelectors.basePackage("com.jichuangsi.school.homeworkservice.controller"))
				.paths(PathSelectors.any()).build();
	}

	// 构建 api文档的详细信息函数,注意这里的注解引用的是哪个
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				// 页面标题
				.title("作业服务信息RESTful API")
				// 版本号
				.version("1.0")
				// 描述
				.description("API 描述").build();
	}
}
