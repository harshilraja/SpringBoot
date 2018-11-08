package com.javasampleapproach.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.capitalone.auto.lo.common.constants.CommonConstants;
import com.capitalone.auto.lo.framework.configuration.engine.CommonConfigurationHelper;
import com.capitalone.auto.lo.framework.context.CLOContextHelper;
import com.capitalone.auto.lo.framework.logging.LoggingConstants;
import com.capitalone.auto.lo.framework.logging.audit.entities.AuditDetail;
import com.capitalone.auto.lo.framework.logging.upstream.CommonLoggerUp;
import com.eclipsesource.json.ParseException;

@Service
public class EmailWhiteListService {

	public static final int REGISTER = 1001;
	public static final int CHECK_WHITELIST = 1002;
	public static final int UPDATE_WHITELIST = 1003;

	@Value("${whitelist.email.status}")
	private String whitelist_email_status;

	@Value("${whitelist.email.register}")
	private String whitelist_email_register;

	@Value("${whitelist.email.updateemail}")
	private String whitelist_email_updateemail;
	
	@Autowired
	OAuthURL outhUrl;

	protected static CommonLoggerUp logger = CLOContextHelper.getLoggingHelper();
	private static String CLASSNAME = EmailWhiteListService.class.getName();

	public void whiteListEmailAddress(long appId, List<String> emailIdList) {
		String method = "whiteListEmailAddress";

		logger.logDebug(CLASSNAME, method, "List of Emails" + emailIdList);

		for (String emailId : emailIdList) {
			logger.logDebug(CLASSNAME, method, "EmailId processed" + emailId);
			int retryCount = 0;
			getWhitelistStatus(appId, emailId, retryCount);

		}

	}
	/**
	 * This method checks email whitelist status 
	 * It also makes subsequent call for update or register depending on the response 
	 * @param appId
	 * @param emailId
	 * @param retryCount
	 */

	private void getWhitelistStatus(long appId, String emailId, int retryCount) {

		String method = "getWhitelistStatus";
		ResponseEntity<String> response = null;
		int respondStatus = 0;
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = generateHeaders();
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			Map<String, String> params = new HashMap<String, String>();
			params.put(CommonConstants.EMAIL_ADDRESS, emailId);
			response = restTemplate.exchange(whitelist_email_status, HttpMethod.GET, entity, String.class, params);
			logger.logMonitor(CLASSNAME, method, "getWhitelistStatus method invoked: "+emailId);
			if (response != null) {
				if (HttpStatus.OK == response.getStatusCode()) {
					JSONObject body = (JSONObject) new JSONParser().parse(response.getBody());
					if (body != null) {
						if (CommonConstants.GOOD_TO_SEND.equals(body.get(CommonConstants.WHITELIST_STATUS))) {

							logger.logDebug(CLASSNAME, method, "Email already Exists: " + emailId);
							logger.logMonitor(CLASSNAME, method, "Email already Exists: "+emailId);
							logAuditforWhiteList(appId, CommonConstants.AUDT_CREATE,
									"EmailId is already whiteListed: " + emailId);
							return;

						} else if (!CommonConstants.GOOD_TO_SEND.equals(body.get(CommonConstants.WHITELIST_STATUS))) {
							retryCount = 0;
							respondStatus = UPDATE_WHITELIST;
							logger.logDebug(CLASSNAME, method, "Update whitelist status initiated for: " + emailId);
							logger.logMonitor(CLASSNAME, method, "Update whitelist status initiated for: "+emailId);
							

						}
					}

				}
			}

		} catch (HttpClientErrorException exp) {
			if (HttpStatus.NOT_FOUND == exp.getStatusCode()) {
				logger.logError(CLASSNAME, method, "No data available in whitelist table" + exp.getMessage());
				logger.logMonitor(CLASSNAME, method, "No data available in whitelist table"+emailId);
				logAuditforWhiteList(appId, CommonConstants.AUDT_CREATE,
						"No data available in whitelist table: " + emailId);
				retryCount = 0;
				respondStatus = REGISTER;
			} else if (HttpStatus.FORBIDDEN == exp.getStatusCode()
					|| HttpStatus.INTERNAL_SERVER_ERROR == exp.getStatusCode()
					|| HttpStatus.BAD_GATEWAY == exp.getStatusCode()) {
				respondStatus = CHECK_WHITELIST;
				logAuditforWhiteList(appId, CommonConstants.AUDT_CREATE,
						"Error occured during getWhitelistStatus : " + emailId);
				logger.logMonitor(CLASSNAME, method, "Error occured during getWhitelistStatus : "+emailId);
			}

		} catch (ParseException e) {

			logger.logError(CLASSNAME, method, "Error occured during JSON parse " + e.getMessage());
			logger.logMonitor(CLASSNAME, method, "Error occured during JSON parse "+e.getMessage());
			return;
		} catch (Exception exp) {
			logger.logError(CLASSNAME, method, "Error occured during the getWhitelistStatus call " + exp.getMessage());
			logger.logMonitor(CLASSNAME, method, "Error occured during the getWhitelistStatus call "+emailId);
			logAuditforWhiteList(appId, CommonConstants.AUDT_CREATE,
					"Following error occured during getWhitelistStatus() call  " + emailId+exp.getMessage());

			return;
		}

		invokeRespondHandler(appId, respondStatus, emailId, retryCount);

	}
	
	
   /**
    * This method add an email to whitelist table with whitelist status as GoodToSend
    * @param appId
    * @param emailId
    * @param retryCount
    */
	public void addToWhitelist(long appId, String emailId, int retryCount) {

		String method = "addToWhitelist";
		ResponseEntity<String> response = null;
		int respondStatus = 0;

		try {
			logger.logDebug(CLASSNAME, method, "Entered addToWhitelist: " + emailId);
			logger.logMonitor(CLASSNAME, method, "Entered addToWhitelist: "+emailId);
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = generateHeaders();
			MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
			Date date = new Date();
			String dateF = format.format(date);
			params.add(CommonConstants.EMAIL_ADDRESS, emailId);
			params.add(CommonConstants.EMAIL_SOURCE, CommonConstants.COAF_LOANORIGINATION);
			params.add(CommonConstants.EMAIL_CAPTURE_TIMESTAMP, dateF);
			params.add(CommonConstants.WHITELIST_STATUS, CommonConstants.GOOD_TO_SEND);
			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(params,
					headers);
			logger.logDebug(CLASSNAME, method, "Before API call to addToWhitelist: " + params + " Headers" + headers);

			response = restTemplate.exchange(whitelist_email_register, HttpMethod.POST, entity, String.class);
			logger.logDebug(CLASSNAME, method, "After API call to addToWhitelist:" + emailId);
			logger.logMonitor(CLASSNAME, method, "API call to addToWhitelist: "+emailId);
			if (response != null) {
				if (HttpStatus.OK == response.getStatusCode()) {
					logger.logDebug(CLASSNAME, method, "Email is registered in whitelist table: " + emailId);
					logger.logMonitor(CLASSNAME, method, "Email is registered in whitelist table: "+emailId);
					logAuditforWhiteList(appId, CommonConstants.AUDT_CREATE,
							"Email is created in whitelist table: " + emailId);
					return;
				}
			}

		} catch (HttpClientErrorException exp) {
			logger.logError(CLASSNAME, method, "Error during the api call to addToWhitelist " + exp.getMessage());
			if (HttpStatus.FORBIDDEN == exp.getStatusCode() || HttpStatus.INTERNAL_SERVER_ERROR == exp.getStatusCode()
					|| HttpStatus.BAD_GATEWAY == exp.getStatusCode()) {
				respondStatus = REGISTER;
				logAuditforWhiteList(appId, CommonConstants.AUDT_CREATE,
						"Error occured during email registration " + emailId+ exp.getStatusText());
				logger.logMonitor(CLASSNAME, method, "Error occured during email registration "+emailId+ exp.getStatusText());
			}

		} catch (Exception exp) {
			logger.logError(CLASSNAME, method, "Email not added to whitelist: " + exp.getMessage());
			logger.logMonitor(CLASSNAME, method, "Email not added to whitelist: "+emailId+exp.getMessage());
			logAuditforWhiteList(appId, CommonConstants.AUDT_CREATE,
					"Email not added to whitelist: " + emailId);
			
		}

		invokeRespondHandler(appId, respondStatus, emailId, retryCount);

	}
	/**
	 * This method updates the whitelist status of existing emails
	 * @param appId
	 * @param emailId
	 * @param retryCount
	 */

	public void updateWhitelist(long appId, String emailId, int retryCount) {
		String method = "updateWhitelist";
		ResponseEntity<String> response = null;
		int errorStatus = 0;
		try {
			logger.logDebug(CLASSNAME, method, "Entered updateWhitelist: " + emailId);
			logger.logMonitor(CLASSNAME, method, "Entered updateWhitelist: "+emailId);
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = generateHeaders();
			MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			params.add(CommonConstants.EMAIL_ADDRESS, emailId);
			params.add(CommonConstants.EMAIL_SOURCE, CommonConstants.COAF_LOANORIGINATION);
			params.add(CommonConstants.REASON_FOR_UPDATE, CommonConstants.REHYDRATION);
			params.add(CommonConstants.WHITELIST_STATUS, CommonConstants.GOOD_TO_SEND);
			logger.logDebug(CLASSNAME, method, "EmailWhitelistService >> updateWhitelist >> map -->" + params);
			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(params,
					headers);

			response = restTemplate.exchange(whitelist_email_updateemail, HttpMethod.POST, entity, String.class);
			logger.logDebug(CLASSNAME, method, "After API call to updateWhitelist:" + emailId);
			if (response != null) {
				if (HttpStatus.OK == response.getStatusCode()) {
					logger.logDebug(CLASSNAME, method, "Successfully updated whitelist status to GoodToSend: " + emailId);
					logAuditforWhiteList(appId, CommonConstants.AUDT_CREATE,
							"Successfully updated whitelist status to GoodToSend: " + emailId);
					return;
				}
			}

		} catch (HttpClientErrorException exp) {
			logger.logError(CLASSNAME, method, "Error during the api call to addToWhitelist " + exp.getMessage());
			if (HttpStatus.FORBIDDEN == exp.getStatusCode() || HttpStatus.INTERNAL_SERVER_ERROR == exp.getStatusCode()
					|| HttpStatus.BAD_GATEWAY == exp.getStatusCode()) {
				errorStatus = UPDATE_WHITELIST;
				logAuditforWhiteList(appId, CommonConstants.AUDT_CREATE,
						"API Error for whitlelist registration " + emailId);
				logger.logMonitor(CLASSNAME, method, "Email not added to whitelist: "+emailId+exp.getMessage());
			}

		} catch (Exception exp) {
			logger.logError(CLASSNAME, method, "Error occured api call to addToWhitelist: Email not added: "+emailId + exp.getMessage());
			logAuditforWhiteList(appId, CommonConstants.AUDT_CREATE,
					"Email not added to whitelist: " + emailId);
			logger.logMonitor(CLASSNAME, method, "Error occured api call to addToWhitelist:Email not added: "+emailId+exp.getMessage());
		}

		invokeRespondHandler(appId, errorStatus, emailId, retryCount);

	}
	/**
	 * 
	 * @param appId
	 * @param respondStatus
	 * @param emailId
	 * @param retryCount
	 * @throws InterruptedException 
	 */

	private void invokeRespondHandler(long appId, int respondStatus, String emailId, int retryCount)  {

		String method = "invokeRespondHandler";
		
		
		logger.logDebug(CLASSNAME, method, "Invoking Error Handler " + emailId);
		switch (respondStatus) {
		case REGISTER:
			logger.logDebug(CLASSNAME, method, "Registering emailID " + emailId);
			retryCount++;
			if (retryCount <= 3) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					logger.logDebug(CLASSNAME, method, "Error occured during thread sleep for REGISTER: "+e.getMessage());
				}
				logger.logDebug(CLASSNAME, method, "Before Registering emailID " + emailId);
				addToWhitelist(appId, emailId, retryCount);
				logger.logDebug(CLASSNAME, method, "After Registering emailID " + emailId);
			}
			break;
		case CHECK_WHITELIST:
			retryCount++;
			if (retryCount <= 3) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					logger.logDebug(CLASSNAME, method, "Error occured during thread sleep for UPDATE_WHITELIST: "+e.getMessage());
				}
				logger.logDebug(CLASSNAME, method, "Before Check whitelist status " + emailId);
				getWhitelistStatus(appId, emailId, retryCount);
				logger.logDebug(CLASSNAME, method, "After Check whitelist status " + emailId);

			}
			break;
		case UPDATE_WHITELIST:
			retryCount++;
			if (retryCount <= 3) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					logger.logDebug(CLASSNAME, method, "Error occured during thread sleep for UPDATE_WHITELIST: "+e.getMessage());
				}
				logger.logDebug(CLASSNAME, method, "Before updating whitelist status " + emailId);
				updateWhitelist(appId, emailId, retryCount);
				logger.logDebug(CLASSNAME, method, "After updating whitelist status " + emailId);
			}
			break;

		default:

		}

	}
	/**
	 * This method returns common header for emailwhitelist api calls
	 * @return
	 */

	private HttpHeaders generateHeaders() {
		String key = "Bearer " + OAuthTokenService.getInstance(outhUrl.getURL()).getToken();
		HttpHeaders headers = new HttpHeaders();
		headers.set(CommonConstants.ACCEPT_HEADER_NAME, CommonConstants.APPLICATION_JSON_V1);
		headers.set(CommonConstants.AUTHORIZATION_HEADER_NAME, key);
		headers.set(CommonConstants.CONTENT_TYPE, CommonConstants.APPLICATION_JSON_V1);
		headers.set(CommonConstants.API_KEY, CommonConstants.API_KEY_VALUE);
		return headers;

	}

	public static void logAuditforWhiteList(long appId, String action, String comments) {
		String method = "logAuditforWhiteList";
		logger.logDebug(CLASSNAME, method, LoggingConstants.METHOD_IN);
		AuditDetail auditDetail = new AuditDetail();
		auditDetail.setAppId(appId);
		auditDetail.setAuditActionCode(action);
		auditDetail.setAuditTime(new Date());
		auditDetail.setCommentText(comments);
		auditDetail.setSourceSysLookUpId(CommonConstants.AUDT_CLO);
		auditDetail.setUserId(CommonConfigurationHelper.getSystemUserID());
		logger.logTrace(CLASSNAME, method, "Before logging audit trail");
		logger.logAudit(auditDetail);
		logger.logTrace(CLASSNAME, method, LoggingConstants.METHOD_OUT);
	}

}
 