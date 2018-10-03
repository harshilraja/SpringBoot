package com.javasampleapproach.resttemplate.service;

import java.net.URISyntaxException;
import java.util.Arrays;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.JsonObject;
import com.javasampleapproach.util.Utils;

public class SpringRestClient {
	 private final RestTemplate restTemplate;
	 
	    public SpringRestClient() {
	        ClientHttpRequestFactory requestFactory = getClientHttpRequestFactory();
	        this.restTemplate = new RestTemplate(requestFactory);
	        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
	    }
	 
	    public void sendRequest2() throws URISyntaxException {
	        String requestJsonString = "{ \"name\": \"user1\"}";
	 
	        String requestUrl = "http://localhost:8080/employee";
	 
	        // set headers
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        HttpEntity<String> entity = new HttpEntity<String>(requestJsonString, headers);
	 
	        // send request and parse result
	        ResponseEntity<String> restResponse = restTemplate
	                .exchange(requestUrl, HttpMethod.POST, entity, String.class);
	 
	        System.err.println("response string: " + restResponse.getBody());
	    }
	 
	    public void getGetResponse() {

			try {
				String baseUrl = "http://localhost:8080/get";
				
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("my_other_key", "my_other_value");
				
				// HttpEntity<String>: To get result as String.
				HttpEntity<String> entity = new HttpEntity<String>(headers);
				UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
					    .queryParam("id", "1")
					    .queryParam("name", "Siva")
					    .queryParam("age", "34");
				
				System.out.println("Url -->"+builder.toUriString().toString());

				ResponseEntity<String> response = restTemplate.exchange(builder.toUriString().toString(), //
				        HttpMethod.GET, entity, String.class);
	 
				String result = response.getBody();
	 
				System.out.println("Get Reponse -->123XYZ"+result);
				
				JsonObject jsonObject = Utils.convertToJSON(result);
				System.out.println("Id"+jsonObject.get("id"));
				System.out.println("name"+jsonObject.get("name"));
				System.out.println("age"+jsonObject.get("age"));
			} catch (HttpClientErrorException e) {
		        System.out.println("HttpClientErrorException");
		    } catch (ResourceAccessException e) {
		        System.out.println("ResourceAccessException");
		    }
		}
	    private ClientHttpRequestFactory getClientHttpRequestFactory() {
	        int timeout = 60000;
	        RequestConfig config = RequestConfig.custom()
	                .setConnectTimeout(timeout)
	                .setConnectionRequestTimeout(timeout)
	                .setSocketTimeout(timeout)
	                .build();
	        CloseableHttpClient client = HttpClientBuilder
	                .create()
	                .setDefaultRequestConfig(config)
	                .build();
	        return new HttpComponentsClientHttpRequestFactory(client);
	    }
}
