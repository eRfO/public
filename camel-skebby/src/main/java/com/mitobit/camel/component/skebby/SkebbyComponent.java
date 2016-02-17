package com.mitobit.camel.component.skebby;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link SkebbyEndpoint}.
 * 
 * @author <a href="mailto:michele.blasi@mitobit.com">Michele Blasi</a>
 */
public class SkebbyComponent extends DefaultComponent {

	@Override
	protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
		Endpoint endpoint = new SkebbyEndpoint(uri, this);
		setProperties(endpoint, parameters);
		return endpoint;
	}
	
}
