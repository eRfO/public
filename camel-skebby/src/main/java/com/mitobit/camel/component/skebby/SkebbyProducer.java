package com.mitobit.camel.component.skebby;

import java.lang.annotation.Annotation;
import java.net.SocketTimeoutException;

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
		final SkebbyEndpoint endpoint = getEndpoint();
		final Object body = exchange.getIn().getBody();
		if (body instanceof SkebbyRequest) {
			// Body is directly a Message
			req = (SkebbyRequest) body;
		} else {
			// Create a message with exchange data
			req = endpoint.getBinding().createSkebbyRequest(endpoint, exchange);
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("Recived a new request to send message: {}", req);
		}
		
		Call<SkebbyResult> call = null;
		if (endpoint.getSenderStr() != null) {
			call = smsClient.sendWithSenderString(req.getMethod(), req.getUsername(), req.getPassword(), req.getRecipients(), req.getText(), endpoint.getSenderStr());
		} else if (endpoint.getSenderNum() != null) {
			call = smsClient.sendWithSenderNumber(req.getMethod(), req.getUsername(), req.getPassword(), req.getRecipients(), req.getText(), endpoint.getSenderNum());
		} else {
			call = smsClient.send(req.getMethod(), req.getUsername(), req.getPassword(), req.getRecipients(), req.getText());
		}
		
		int retryNum = 0, maxRetry = endpoint.getMaxRetry();
		Response<SkebbyResult> response = null;
		while (response == null && ++retryNum <= maxRetry) {
			try {
				response = call.execute();
			} catch (SocketTimeoutException ste) {
				if (retryNum == maxRetry) {
					LOG.error("Maximum number of retry attempts reached, fail.");
					throw ste;
				} else {
					LOG.warn("There was a connection timeout during the call to Skebby service, retrying...");
					call = call.clone();
				}
			}
		}
		
		if (response != null) {
			SkebbyResult result;
			if (!response.isSuccess() && response.errorBody() != null) {
				Converter<ResponseBody, Object> errorConverter = endpoint.getRetrofit().responseBodyConverter(SkebbyResult.class, new Annotation[0]);
				result = (SkebbyResult) errorConverter.convert(response.errorBody());
			} else {
				result = response.body();
			}
			LOG.debug("Skebby result {}", result);
			// set result to output body
			exchange.getOut().setBody(result);
			// copy headers from IN to OUT to propagate them
			exchange.getOut().setHeaders(exchange.getIn().getHeaders());
		} else {
			log.warn("Something went wrong, the Skebby response is null.");
		}
		
	}

	@Override
	public SkebbyEndpoint getEndpoint() {
		return (SkebbyEndpoint) super.getEndpoint();
	}

}
