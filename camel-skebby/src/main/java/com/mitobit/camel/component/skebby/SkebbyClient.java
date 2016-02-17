package com.mitobit.camel.component.skebby;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Skebby client.
 * 
 * @author <a href="mailto:michele.blasi@mitobit.com">Michele Blasi</a>
 *
 */
public interface SkebbyClient {

	String REST_PATH = "rest.php";
	
	String METHOD = "method";
	String USER = "username";
	String PASS = "password";
	String TEXT = "text";
	String RECIPIENTS = "recipients";
	String SENDER_NUMBER = "sender_number";
	String SENDER_STRING = "sender_string";
	
	/**
	 * Sends SMS message.
	 * 
	 * @param method
	 * @param username
	 * @param password
	 * @param recipients
	 * @param text
	 * @return {@link Call} instance
	 */
	@FormUrlEncoded
	@POST(REST_PATH)
	Call<SkebbyResult> send(
		@Field(METHOD) String method, 
		@Field(USER) String username, 
		@Field(PASS) String password, 
		@Field(RECIPIENTS) String[] recipients, 
		@Field(value = TEXT, encoded = true) String text
	);
	
	/**
	 * Sends SMS message specifying sender number.
	 * 
	 * @param method
	 * @param username
	 * @param password
	 * @param recipients
	 * @param text
	 * @param senderNumber
	 * 
	 * @return {@link Call} instance
	 */
	@FormUrlEncoded
	@POST(REST_PATH)
	Call<SkebbyResult> sendWithSenderNumber(
		@Field(METHOD) String method, 
		@Field(USER) String username, 
		@Field(PASS) String password, 
		@Field(RECIPIENTS) String[] recipients, 
		@Field(value = TEXT, encoded = true) String text,
		@Field(SENDER_NUMBER) String senderNumber
	);
	
	/**
	 * Sends SMS message specifying sender text.
	 * 
	 * @param method
	 * @param username
	 * @param password
	 * @param recipients
	 * @param text
	 * @param senderString
	 * 
	 * @return {@link Call} instance
	 */
	@FormUrlEncoded
	@POST(REST_PATH)
	Call<SkebbyResult> sendWithSenderString(
		@Field(METHOD) String method, 
		@Field(USER) String username, 
		@Field(PASS) String password, 
		@Field(RECIPIENTS) String[] recipients, 
		@Field(value = TEXT, encoded = true) String text,
		@Field(SENDER_STRING) String senderString
	);	
	
}
