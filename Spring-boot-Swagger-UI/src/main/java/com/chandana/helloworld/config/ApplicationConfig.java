package com.chandana.helloworld.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class ApplicationConfig {

    @Bean
    public Docket api() {
    	return new Docket(DocumentationType.SWAGGER_2)
    			.tags(new Tag("Category Entity", "Repository for Category entities"))
    			.tags(new Tag("Pet Entity", "Repository for Pet entities"))
    			.select()
    			.apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
       /* return new Docket(DocumentationType.SWAGGER_2)
        		
        		.tags(new Tag("Pet Entity", "Repository for Address entities"))
                .apiInfo(getApiInfo())
                .select()
//                .apis(RequestHandlerSelectors.any())
               .apis(RequestHandlerSelectors.basePackage("com.chandana.helloworld"))
                .paths(PathSelectors.any())
                .build();*/
    }

    private ApiInfo getApiInfo() {
        Contact contact = new Contact("Chandana Napagoda", "http://blog.napagoda.com", "cnapagoda@gmail.com");
        return new ApiInfoBuilder()
                .title("Example Api Title")
                .description("Example Api Definition")
                .version("1.0.0")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .contact(contact)
                .build();
    }
}
