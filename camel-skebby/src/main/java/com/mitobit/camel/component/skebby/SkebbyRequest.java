package com.mitobit.camel.component.skebby;

/**
 * Models a Skebby request.
 * 
 * @author <a href="mailto:michele.blasi@mitobit.com">Michele Blasi</a>
 *
 */
public class SkebbyRequest {

	private String method;
	
	private String username;
	
	private String password;
	
	private String[] recipients;
	
	private String text;

	public SkebbyRequest() {
		super();
	}

	public SkebbyRequest(String method) {
		this(method, null, null);
	}
	
	public SkebbyRequest(String method, String username, String password) {
		super();
		this.method = method;
		this.username = username;
		this.password = password;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String[] getRecipients() {
		return recipients;
	}

	public void setRecipients(String[] recipients) {
		this.recipients = recipients;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
