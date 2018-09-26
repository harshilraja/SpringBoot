package com.javasampleapproach.resttemplate.service;

import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.JsonObject;
import com.javasampleapproach.resttemplate.model.Customer;
import com.javasampleapproach.util.Utils;

public class RestfulClient {
	RestTemplate restTemplate;
	
	public RestfulClient(){
		restTemplate = new RestTemplate();
	}
	
	/**
	 * post entity
	 */
	public void postEntity(){
		System.out.println("Begin /POST request!");
		String postUrl = "http://localhost:8080/post";
		Customer customer = new Customer(123, "Jack", 23);
		ResponseEntity<String> postResponse = restTemplate.postForEntity(postUrl, customer, String.class);
		System.out.println("Response for Post Request: " + postResponse.getBody());
	}
	
	
	/**
	 * get entity
	 */
	public void getEntity(){
		System.out.println("Begin /GET request!");
		String getUrl = "http://localhost:8080/get?id=1&name='Mary'&age=20";
		ResponseEntity<Customer> getResponse = restTemplate.getForEntity(getUrl, Customer.class);
		if(getResponse.getBody() != null){
			System.out.println("Response for Get Request: " + getResponse.getBody().toString());	
		}else{
			System.out.println("Response for Get Request: NULL");
		}
	}
	
	public void getPostResponse() {
		Customer customer = new Customer(123, "Jack", 23);
		String postUrl = "http://localhost:8080/post";
	      HttpHeaders headers = new HttpHeaders();
	     /* headers.add("Accept", MediaType.APPLICATION_XML_VALUE);
	      headers.setContentType(MediaType.APPLICATION_XML);*/
	      headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
	      headers.setContentType(MediaType.APPLICATION_JSON);
	 
	      RestTemplate restTemplate = new RestTemplate();
	 
	      // Data attached to the request.
	      HttpEntity<Customer> requestBody = new HttpEntity<>(customer, headers);
	 
	      // Send request with POST method.
	      String result = restTemplate.postForObject(postUrl, requestBody, String.class);
	      
	      System.out.println("Get Reponse -->"+result);
	}
	public void getGetResponse() {

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
        
        // RestTemplate
        RestTemplate restTemplate = new RestTemplate();
 
        // Send request with GET method, and Headers.
        ResponseEntity<String> response = restTemplate.exchange(builder.toUriString().toString(), //
                HttpMethod.GET, entity, String.class);
 
        String result = response.getBody();
 
        System.out.println("Get Reponse -->"+result);
        
        JsonObject jsonObject = Utils.convertToJSON(result);
        System.out.println("Id"+jsonObject.get("id"));
        System.out.println("name"+jsonObject.get("name"));
        System.out.println("age"+jsonObject.get("age"));
	
	}
	/**
	 * put entity
	 */
	public void putEntity(){
		System.out.println("Begin /PUT request!");
		String putUrl = "http://localhost:8080/put/2";
		Customer puttCustomer = new Customer("Bush", 23);
		restTemplate.put(putUrl, puttCustomer);
	}
	
	/**
	 * delete entity
	 */
	public void deleteEntity(){
		System.out.println("Begin /DELETE request!");
		String deleteUl = "http://localhost:8080/delete/1";
		restTemplate.delete(deleteUl);
	}
}
