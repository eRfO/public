package com.mitobit.camel.component.nexmo;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.spi.UriEndpoint;

/**
 * Represents the component that manages {@link NexmoEndpoint}.
 */
@UriEndpoint(title = "Nexmo", scheme = "nexmo", consumerClass = NexmoConsumer.class, consumerPrefix = "consumer", syntax = "nexmo")
public class NexmoComponent extends DefaultComponent {

	@Override
	protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
		Endpoint endpoint = new NexmoEndpoint(uri, this);
		setProperties(endpoint, parameters);
		return endpoint;
	}
}
