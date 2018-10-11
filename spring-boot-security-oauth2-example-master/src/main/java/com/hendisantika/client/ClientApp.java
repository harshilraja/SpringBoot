package com.hendisantika.client;

import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.web.client.HttpStatusCodeException;

public class ClientApp {

	public static void main(String args[]) {
		System.out.println("Hello");
		registerEmailAddress();
	}
	
	public static JSONObject registerEmailAddress() {
		final String method;
		String result = ""; // Here JSon String
		method = "process";

		String clientId = "hendi-client";
		String clientSecret = "hendi-secret";
		String accessTokenUrl = "http://localhost:8080/oauth/token";
		System.out.println("registerEmailAddress-->After  --> tokenUrl"+accessTokenUrl);
		ResponseEntity<String> response = null;
		try {
			
			HttpHeaders headers = new HttpHeaders();
	        headers.set(Constants.ACCEPT_HEADER_NAME, Constants.APPLICATION_JSON_V1);
	        headers.set(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON_V1);

	       /* ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
	        resourceDetails.setId("1");
	        resourceDetails.setClientId(clientId);
	        resourceDetails.setClientSecret(clientSecret);
	        resourceDetails.setAccessTokenUri(accessTokenUrl);
	        resourceDetails.setGrantType("password");
	        
	        DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();
	        OAuth2RestTemplate template1 = new OAuth2RestTemplate(resourceDetails,clientContext);
	        String parameters = "username=hendi&password=password&grant_type=password";
	        HttpEntity<String> request =  new HttpEntity<String>(parameters,headers);
	        HttpEntity<String> token = template1.postForEntity(accessTokenUrl, request, String.class);
	        
	        System.out.println("Token"+token.getBody());
	        DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();
	        final ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
	        details.setAccessTokenUri(accessTokenUrl);
	        details.setClientId(clientId);
	        details.setClientSecret(clientSecret);
	      //  details.setClientAuthenticationScheme(AuthenticationScheme.header);
	        details.setGrantType("password");
	        final OAuth2RestTemplate template = new OAuth2RestTemplate(details,clientContext);
	        String authToken = template.getAccessToken().getValue();
	        System.out.println(authToken);*/
			
		}catch (HttpStatusCodeException e) {
            System.out.println("Error when calling the REgister Email API: {}."+ e.getStackTrace());

            //ErrorResponse errorResponse = null;

            try {
              //  errorResponse = objectMapper.readValue(e.getResponseBodyAsString(), ErrorResponse.class);
                System.out.println("Register EANil API error: {}."+ e.getResponseBodyAsString());
            } catch (Exception ioException) {
                System.out.println("Error when parsing error response: {}."+ ioException.getStackTrace());
            }

            if (e.getStatusCode().is4xxClientError() ) {
                System.out.println("The request could not be completed because there is Email White Listed");
               // throw new EmailServiceException("Error when parsing response from Email WhiteListing API.");
            }
        }
		JSONObject body = null;
        
        return body;
	}
}
