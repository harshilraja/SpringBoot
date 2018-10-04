package com.javasampleapproach.resttemplate;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;

import com.javasampleapproach.resttemplate.service.RestfulClient;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {EmbeddedServletContainerAutoConfiguration.class, WebMvcAutoConfiguration.class})
public class SpringbootResttemplateApplication {

	public static void main(String[] args) {
		RestfulClient restfulClient = new RestfulClient();
		restfulClient.getGetResponseById();
		/*restfulClient.getPostResponse();
		
		restfulClient.getGetResponse();*/
		
		/*SpringRestClient springRestClient = new SpringRestClient();
		springRestClient.getGetResponse();*/
		/*
		 * GET ENTITY
		 */
		//restfulClient.getEntity();
		
		/*
		 * PUT ENTITY
		 */
		//restfulClient.putEntity();
		
		/*
		 * DELETE ENTITY 
		 */
		//restfulClient.deleteEntity();
		
	}
}