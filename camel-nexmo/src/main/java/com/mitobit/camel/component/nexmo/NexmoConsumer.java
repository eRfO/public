package com.mitobit.camel.component.nexmo;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;

/**
 * The Nexmo consumer.
 */
public class NexmoConsumer extends ScheduledPollConsumer {

	private final NexmoEndpoint endpoint;

	public NexmoConsumer(NexmoEndpoint endpoint, Processor processor) {
		super(endpoint, processor);
		this.endpoint = endpoint;
	}

	@Override
	protected int poll() throws Exception {
		Exchange exchange = endpoint.createExchange();
		try {
			// send message to next processor in the route
			getProcessor().process(exchange);
			return 1; // number of messages polled
		} finally {
			// log exception if an exception occurred and was not handled
			if (exchange.getException() != null) {
				getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
			}
		}
	}
}
