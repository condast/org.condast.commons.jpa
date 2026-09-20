package org.condast.commons.jpa.authentication.http;

public class AbstractDomainProvider<D extends Object> implements IDomainProvider<D> {

	private String domain;
	private long token;
	private String path;
	
	private D data;
	
	protected AbstractDomainProvider( String domain, String path, long token ) {
		super();
		this.domain = domain;
		this.token = token;
		this.path = path;
	}

	@Override
	public String getDomain() {
		return domain;
	}

	@Override
	public boolean isDomain(String domain) {
		return domain.toLowerCase().equals(domain);
	}

	public D getData() {
		return data;
	}

	public void setData(D data) {
		this.data = data;
	}

	@Override
	public long getToken() {
		return token;
	}

	@Override
	public boolean accept(long userid, long token) {
		return ( this.token == token );
	}

	@Override
	public String getReturnPath() {
		return path;
	}

	@Override
	public int compareTo(IDomainProvider<D> o) {
		return this.domain.compareTo(o.getDomain());
	}
	
	
}
