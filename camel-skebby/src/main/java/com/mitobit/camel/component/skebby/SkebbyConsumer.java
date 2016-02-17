package com.mitobit.camel.component.skebby;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;

/**
 * The Skebby consumer.
 * 
 * @author <a href="mailto:michele.blasi@mitobit.com">Michele Blasi</a>
 */
public class SkebbyConsumer extends ScheduledPollConsumer {

	private final SkebbyEndpoint endpoint;

	public SkebbyConsumer(SkebbyEndpoint endpoint, Processor processor) {
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
