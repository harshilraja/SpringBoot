package com.javasampleapproach.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.capitalone.auto.lo.common.constants.CommonConstants;
import com.capitalone.auto.lo.framework.context.CLOContextHelper;
import com.capitalone.auto.lo.framework.logging.upstream.CommonLoggerUp;
import com.capitalone.auto.lo.framework.util.SecretProviderUtil;
import com.capitalone.fs.secretprovider.SecretProvider;

public class OAuthTokenService {

	private final static CommonLoggerUp logger = CLOContextHelper.getLoggingHelper();

	private static String CLASSNAME = OAuthTokenService.class.getName();
	private static JSONObject authObject;
	private static OAuthTokenService oAuthToken;
	private static int retryCount = 0;
	private static String url;

	

	private OAuthTokenService() {
		generateToken();
	}

	public synchronized static OAuthTokenService getInstance(String urlAuth) {

		if (oAuthToken == null) {
			url = urlAuth;			
			oAuthToken = new OAuthTokenService();
		}
		return oAuthToken;
	}
	
	
	
	

	/**
	 * This method generates OAuth token
	 */

	private void generateToken() {

		String method = "generateToken";
		ResponseEntity<String> responseEntity = null;
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<String, String>();
		RestTemplate restTemplate = new RestTemplate();
		String result = null;
		try {
			logger.logDebug(CLASSNAME, method, "Token Generation");
			retryCount++;
			SecretProvider secretProvider = SecretProviderUtil.getSecretProviderInstance();
			HttpHeaders headers = new HttpHeaders();
			headers.set(CommonConstants.CONTENT_TYPE, CommonConstants.APPLICATON_X_WWW_FORM_URLENCODED);
			valueMap.add(CommonConstants.GRANT_TYPE, CommonConstants.CLIENT_CREDENTIALS);
			valueMap.add(CommonConstants.CLIENT_ID,
					secretProvider.getSecret(CommonConstants.TURING_OAUTH_CLIENT_ID_EDA));
			valueMap.add(CommonConstants.CLIENT_SECRET,
					secretProvider.getSecret(CommonConstants.TURING_OAUTH_SECRET_ID_EDA));
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(valueMap,
					headers);
			OAuthURL oauthURL = new OAuthURL();
			logger.logDebug(CLASSNAME, method, "Token url1: " + url);
			logger.logDebug(CLASSNAME, method, "Token url2: " + url);
			responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

			if (responseEntity != null) {
				result = responseEntity.getBody();
				logger.logDebug(CLASSNAME, method, "Token Generation Result" + result);
				if (result != null) {
					authObject = (JSONObject) new JSONParser().parse(result);
					retryCount = 0;
				}
			}

		} catch (RestClientException exp) {
			logger.logMonitor(CLASSNAME, method, "Failed to generate token during OAUTH call" + exp.getMessage());
			logger.logError(CLASSNAME, method, "Failed to generate token during OAUTH call" + exp.getMessage());

			if (retryCount <= 3) {
				generateToken();
			}
		} catch (ParseException exp) {
			logger.logMonitor(CLASSNAME, method, "Failed to parse token during OAUTH call" + exp.getMessage());
		} catch (Exception exp) {
			logger.logMonitor(CLASSNAME, method, "Generic error during OAUTH token call" + exp.getMessage());
			logger.logError(CLASSNAME, method, "Generic error during OAUTH token call" + exp.getMessage());
			if (retryCount <= 3) {
				generateToken();
			}
		}

	}

	/**
	 * This method checks if a token is null or expired and generates a new token
	 * based on the result
	 * 
	 * @return
	 */
	public String getToken() {
		String token = null;
		String method = "getToken";
		final long now = System.currentTimeMillis();
		if (authObject != null) {
			if ((authObject.get(CommonConstants.ACCESS_TOKEN) == null)
					|| (now > (((Long) authObject.get(CommonConstants.ISSUED_AT) * 1000L)
							+ (Long) authObject.get(CommonConstants.EXPIRES_IN) - 60000L))) {
				generateToken();
				token = (authObject != null) ? (String) authObject.get(CommonConstants.ACCESS_TOKEN) : null;
				logger.logMonitor(CLASSNAME, method, "OAuth token is expired or null");

			} else {

				token = (String) authObject.get(CommonConstants.ACCESS_TOKEN);
				logger.logDebug(CLASSNAME, method, "Token Value" + token);
			}

		}

		return token;
	}

}
 