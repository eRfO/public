package com.mitobit.camel.component.nexmo;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nexmo.messaging.sdk.NexmoSmsClient;
import com.nexmo.messaging.sdk.SmsSubmissionResult;
import com.nexmo.messaging.sdk.messages.Message;

/**
 * The Nexmo producer.
 */
public class NexmoProducer extends DefaultProducer {

	private static final transient Logger LOG = LoggerFactory.getLogger(NexmoProducer.class);

	private NexmoSmsClient smsClient;

	public NexmoProducer(NexmoEndpoint endpoint) {
		super(endpoint);
		this.smsClient = endpoint.createSmsClient();
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		Message message;
		final Object body = exchange.getIn().getBody();
		if (body instanceof Message) {
			// Body is directly a Message
			message = (Message) body;
		} else {
			// Create a message with exchange data
			message = getEndpoint().getBinding().createSmsMessage(getEndpoint(), exchange);
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("Sending Message: {}", message);
		}
		SmsSubmissionResult[] results = smsClient.submitMessage(message);
		exchange.getOut().setBody(results);
		// copy headers from IN to OUT to propagate them
		exchange.getOut().setHeaders(exchange.getIn().getHeaders());
	}

	@Override
	public NexmoEndpoint getEndpoint() {
		return (NexmoEndpoint) super.getEndpoint();
	}

}
