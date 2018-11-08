package com.javasampleapproach.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class OAuthURL {
	
	@Value("${whitelist.api.token.url}")
	private String whitelist_api_token_url;
	
	
	public  String getURL() {
		return whitelist_api_token_url;
	}

}