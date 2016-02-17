package com.mitobit.camel.component.nexmo;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriParam;

import com.nexmo.messaging.sdk.NexmoSmsClient;
import com.nexmo.messaging.sdk.NexmoSmsClientSSL;

/**
 * Represents a Nexmo endpoint.
 */
public class NexmoEndpoint extends DefaultEndpoint {

	private SmsBinding binding;
	@UriParam
	private String apiKey;
	@UriParam
	private String apiSecret;
	@UriParam
	private String from;
	@UriParam
	private String to;
	@UriParam
	private boolean ssl = true;

	public NexmoEndpoint() {
	}

	public NexmoEndpoint(String uri, NexmoComponent component) {
		super(uri, component);
	}

	@Override
	public Producer createProducer() throws Exception {
		return new NexmoProducer(this);
	}

	@Override
	public Consumer createConsumer(Processor processor) throws Exception {
		throw new UnsupportedOperationException("Nexmo endpoints are not meant to be consumed from.");
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public NexmoSmsClient createSmsClient() {
		NexmoSmsClient client;
		try {
			if (ssl) {
				client = new NexmoSmsClientSSL(apiKey, apiSecret);
			} else {
				client = new NexmoSmsClient(apiKey, apiSecret);
			}
		} catch (Exception e) {
			throw new RuntimeCamelException("Failed to instanciate a Nexmo Client", e);
		}
		return client;
	}

	// Properties
	// -------------------------------------------------------------------------

	public SmsBinding getBinding() {
		if (binding == null) {
			binding = new SmsBinding();
		}
		return binding;
	}

	public void setBinding(SmsBinding binding) {
		this.binding = binding;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiSecret() {
		return apiSecret;
	}

	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public boolean isSsl() {
		return ssl;
	}

	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}

}
