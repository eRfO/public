package com.mitobit.camel.component.skebby;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Represents a Skebby endpoint.
 * 
 * @author <a href="mailto:michele.blasi@mitobit.com">Michele Blasi</a>
 * 
 */
@UriEndpoint(title = "Skebby", scheme = "skebby", consumerClass = SkebbyConsumer.class, consumerPrefix = "consumer", syntax = "skebby:protocol:host/uriPattern")
public class SkebbyEndpoint extends DefaultEndpoint {

    private static final String DEFAULT_PROTOCOL = "http";
    private static final String DEFAULT_HOST = "gateway.skebby.it";	
    private static final String DEFAULT_PATH = "/api/send/smseasy/advanced/";	
	
	private SkebbyBinding binding;
	
	private Retrofit retrofit;
	
    @UriPath(enums = "http,https") @Metadata(required = "true")
    private String protocol = DEFAULT_PROTOCOL;
    @UriPath @Metadata(required = "true")
    private String host = DEFAULT_HOST;
    @UriPath @Metadata(required = "true")
    private String uriPath = DEFAULT_PATH;   

	@UriParam
	private SkebbyMethod method = SkebbyMethod.SEND_SMS_BASIC;
	@UriParam
	private String username;
	@UriParam
	private String password;
	@UriParam
	private String senderStr;
	@UriParam
	private String senderNum;
	@UriParam
	private int maxRetry = 3;	
	@UriParam
	private long readTimeout = 60_000;
	@UriParam
	private long writeTimeout = 10_000;	
	@UriParam
	private long connectionTimeout = 60_000;	
	@UriParam
	private List<String> recipients;

	public SkebbyEndpoint() {
	}

	public SkebbyEndpoint(String uri, SkebbyComponent component) {
		super(uri, component);
	}

	@Override
	public Producer createProducer() throws Exception {
		return new SkebbyProducer(this);
	}

	@Override
	public Consumer createConsumer(Processor processor) throws Exception {
		throw new UnsupportedOperationException("Skebby endpoints are not meant to be consumed from.");
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public SkebbyClient createClient() {
		SkebbyClient client;
		try {
			if (retrofit == null) {
				final OkHttpClient okHttpClient = new OkHttpClient.Builder()
				        .readTimeout(getReadTimeout(), TimeUnit.MILLISECONDS)
				        .writeTimeout(getWriteTimeout(), TimeUnit.MILLISECONDS)
				        .connectTimeout(getConnectionTimeout(), TimeUnit.MILLISECONDS)
				        .build();				
				
				retrofit = new Retrofit.Builder()
					.baseUrl(buildBaseUrl())
					.client(okHttpClient)
					.addConverterFactory(SimpleXmlConverterFactory.create())
					
					.build();
			}
			client = retrofit.create(SkebbyClient.class);
		} catch (Exception e) {
			throw new RuntimeCamelException("Failed to instanciate a Skebby Client", e);
		}
		return client;
	}

	// Properties
	// -------------------------------------------------------------------------

	public SkebbyBinding getBinding() {
		if (binding == null) {
			binding = new SkebbyBinding();
		}
		return binding;
	}

	public void setBinding(SkebbyBinding binding) {
		this.binding = binding;
	}

	public Retrofit getRetrofit() {
		return retrofit;
	}

	public void setRetrofit(Retrofit retrofit) {
		this.retrofit = retrofit;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUriPath() {
		return uriPath;
	}

	public void setUriPath(String uriPath) {
		this.uriPath = uriPath;
	}

	public SkebbyMethod getMethod() {
		return method;
	}

	public void setMethod(SkebbyMethod method) {
		this.method = method;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSenderStr() {
		return senderStr;
	}

	public void setSenderStr(String senderStr) {
		this.senderStr = senderStr;
	}

	public String getSenderNum() {
		return senderNum;
	}

	public void setSenderNum(String senderNum) {
		this.senderNum = senderNum;
	}

	public int getMaxRetry() {
		return maxRetry;
	}

	public void setMaxRetry(int maxRetry) {
		this.maxRetry = maxRetry;
	}

	public long getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(long readTimeout) {
		this.readTimeout = readTimeout;
	}

	public long getWriteTimeout() {
		return writeTimeout;
	}

	public void setWriteTimeout(long writeTimeout) {
		this.writeTimeout = writeTimeout;
	}

	public long getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public List<String> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<String> recipients) {
		this.recipients = recipients;
	}

	protected String buildBaseUrl() {
		return getProtocol() + "://" + getHost() + getUriPath();
	}
	
}
