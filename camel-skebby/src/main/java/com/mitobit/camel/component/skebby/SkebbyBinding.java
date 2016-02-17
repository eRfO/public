package com.mitobit.camel.component.skebby;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.RuntimeCamelException;

/**
 * Skebby Binding
 * 
 * @author <a href="mailto:michele.blasi@mitobit.com">Michele Blasi</a>
 *
 */
public class SkebbyBinding {

	private static final String PARAM_EXCEPTION_MESSAGE = "Detected invalid type for '%s' parameter or is missing";
	
	private static final String[] SPECIAL_CHARS = { "+", "00" };
	private static final String RECIPENTS_SEPS = ";, ";
	
	public SkebbyRequest createSkebbyRequest(SkebbyEndpoint endpoint, Exchange exchange) {
		Message in = exchange.getIn();
		Object method = in.getHeader(SkebbyConstants.SKEBBY_METHOD, endpoint.getMethod());
		String username = in.getHeader(SkebbyConstants.SKEBBY_USERNAME, endpoint.getUsername(), String.class);
		String password = in.getHeader(SkebbyConstants.SKEBBY_PASSWORD, endpoint.getPassword(), String.class);
		Object recipients = in.getHeader(SkebbyConstants.SKEBBY_RECIPIENTS, endpoint.getRecipients());
		// normalise method parameter
		if (method instanceof SkebbyMethod) {
			method = ((SkebbyMethod) method).name().toLowerCase();
		} else if (method instanceof String) {
			method = ((String) method).toLowerCase();
		} else {
			throw new RuntimeCamelException(String.format(PARAM_EXCEPTION_MESSAGE, "method"));
		}
		// normalise recipients parameter
		if (recipients instanceof String) {
			List<String> list = new ArrayList<String>();
			StringTokenizer tokenizer = new StringTokenizer((String) recipients, RECIPENTS_SEPS);
			while (tokenizer.hasMoreTokens()) {
				list.add(normalizeRecipient(tokenizer.nextToken()));
			}
			recipients = list.toArray(new String[list.size()]);
		} else if (recipients instanceof Collection) {
			List<String> list = new ArrayList<String>();
			for (Object recipient : (Collection<?>) recipients) {
				if (recipient instanceof String) {
					list.add(normalizeRecipient((String) recipient));
				} else {
					throw new IllegalArgumentException("The recipients collection contains not String elements.");
				}
			}
			recipients = list.toArray(new String[list.size()]);
		} else if (!(recipients instanceof String[])) {
			throw new RuntimeCamelException(String.format(PARAM_EXCEPTION_MESSAGE, "recipients"));
		}
		
		final Object messageBody = in.getBody();
		if (messageBody instanceof SkebbyRequest) {
			SkebbyRequest request = (SkebbyRequest) messageBody;
			if (request.getMethod() == null) {
				request.setMethod((String) method);
			}
			if (request.getUsername() == null) {
				request.setUsername(username);
			}
			if (request.getPassword() == null) {
				request.setPassword(password);
			}
			return request;
		} else if (messageBody instanceof String) {
			SkebbyRequest request = new SkebbyRequest();
			request.setMethod((String) method);
			request.setUsername(username);
			request.setPassword(password);
			request.setRecipients((String[]) recipients);
			request.setText((String) messageBody);
			return request;
		}
		throw new RuntimeCamelException("Cannot create a Skebby request by this message body " + messageBody);
	}

	protected String normalizeRecipient(String recipient) {
		String text = recipient;
		for (String sc : SPECIAL_CHARS) {
			if (text.startsWith(sc)) {
				text = text.substring(sc.length());
				break;
			}
		}
		return text.trim();
	}
	
}
