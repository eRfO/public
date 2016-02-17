package com.mitobit.camel.component.skebby;

import java.lang.annotation.Annotation;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;


/**
 * The Skebby producer.
 * 
 * @author <a href="mailto:michele.blasi@mitobit.com">Michele Blasi</a>
 */
public class SkebbyProducer extends DefaultProducer {

	private static final transient Logger LOG = LoggerFactory.getLogger(SkebbyProducer.class);

	private SkebbyClient smsClient;

	public SkebbyProducer(SkebbyEndpoint endpoint) {
		super(endpoint);
		this.smsClient = endpoint.createClient();
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		SkebbyRequest req;
		final Object body = exchange.getIn().getBody();
		if (body instanceof SkebbyRequest) {
			// Body is directly a Message
			req = (SkebbyRequest) body;
		} else {
			// Create a message with exchange data
			req = getEndpoint().getBinding().createSkebbyRequest(getEndpoint(), exchange);
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("Sending message: {}", req.getText());
		}
		Call<SkebbyResult> call = smsClient.send(req.getMethod(), req.getUsername(), req.getPassword(), req.getRecipients(), req.getText());
		Response<SkebbyResult> response = call.execute();
		if (response != null) {
			SkebbyResult result;
			if (!response.isSuccess() && response.errorBody() != null) {
				Converter<ResponseBody, Object> errorConverter = getEndpoint().getRetrofit().responseBodyConverter(SkebbyResult.class, new Annotation[0]);
				result = (SkebbyResult) errorConverter.convert(response.errorBody());
			} else {
				result = response.body();
			}
			// set result to output body
			exchange.getOut().setBody(result);
			// copy headers from IN to OUT to propagate them
			exchange.getOut().setHeaders(exchange.getIn().getHeaders());
		}
		
	}

	@Override
	public SkebbyEndpoint getEndpoint() {
		return (SkebbyEndpoint) super.getEndpoint();
	}

}
