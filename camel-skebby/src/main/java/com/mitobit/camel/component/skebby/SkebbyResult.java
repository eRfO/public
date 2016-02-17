package com.mitobit.camel.component.skebby;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Generic Skebby result.
 * 
 * @author <a href="mailto:michele.blasi@mitobit.com">Michele Blasi</a>
 *
 */
@Root
public class SkebbyResult {

	@Attribute
	private String generator;
	@Attribute
	private String version;
	@Element(required = false)
	private String status;	
	@Element(required = false)
	private Response response;	
	@Element(name = "send_sms_basic", required = false)
	private SendSmsBasic basic;

	public String getGenerator() {
		return generator;
	}

	public void setGenerator(String generator) {
		this.generator = generator;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public SendSmsBasic getBasic() {
		return basic;
	}

	public void setBasic(SendSmsBasic basic) {
		this.basic = basic;
	}

	public static class SendSmsBasic {

		@Element(required = false)
		private Response response;
		@Element
		private String status;
		@Element(name = "remaining_sms", required = false)
		private String remainingSms;
		
		public Response getResponse() {
			return response;
		}

		public void setResponse(Response response) {
			this.response = response;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getRemainingSms() {
			return remainingSms;
		}

		public void setRemainingSms(String remainingSms) {
			this.remainingSms = remainingSms;
		}

	}

	public static class Response {

		@Element(required = false)
		private String code;

		@Element
		private String message;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

}
