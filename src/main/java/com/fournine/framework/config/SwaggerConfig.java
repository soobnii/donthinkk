package com.fournine.framework.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2 //swagger에 해당하는 어노테이션을 작성한다.
@EnableWebMvc //이것도 함께 작성
public class SwaggerConfig implements WebMvcConfigurer {

	// http://localhost:8080/swagger-ui/index.html
	// https://www.baeldung.com/spring-boot-swagger-jwt
	// http://localhost:8080/swagger-ui.html
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) { //spring-security와 연결할 때 이 부분을 작성하지 않으면 404에러가 뜬다.
		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.consumes(getConsumeContentTypes())
				.produces(getProduceContentTypes())
				.apiInfo(swaggerInfo())
				.securityContexts(Arrays.asList(securityContext()))
				.securitySchemes(apiKey())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.fournine.app"))
				.paths(PathSelectors.any())
				.build()
				.useDefaultResponseMessages(false);
	}

	private ApiInfo swaggerInfo() {
		return new ApiInfoBuilder().title("Fournine API")
				.description("Fournine API Docs").build();
	}
	
	private Set<String> getConsumeContentTypes() {
		Set<String> consumes = new HashSet<>();
		consumes.add("application/json;charset=UTF-8");
		consumes.add("application/x-www-form-urlencoded");
		return consumes;
	}

	private Set<String> getProduceContentTypes() {
		Set<String> produces = new HashSet<>();
		produces.add("application/json;charset=UTF-8");
		return produces;
	}
	
	// First, we need to define our ApiKey to include JWT as an authorization header
	private ArrayList<ApiKey> apiKey() {
			ArrayList<ApiKey> apiKeyList = new ArrayList<ApiKey>();
			apiKeyList.add(new ApiKey("JWT", "Authorization", "header"));
			return apiKeyList; 
		}
	
	// Next, let's configure the JWT SecurityContext with a global AuthorizationScope:
	private SecurityContext securityContext() { 
		return SecurityContext.builder().securityReferences(defaultAuth()).build(); 
	} 
	
	private List<SecurityReference> defaultAuth() { 
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything"); 
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1]; 
		authorizationScopes[0] = authorizationScope; 
		return Arrays.asList(new SecurityReference("JWT", authorizationScopes)); 
	}
}
