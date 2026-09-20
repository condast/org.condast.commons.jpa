package org.condast.commons.jpa.authentication.callback;

import javax.security.auth.callback.Callback;

public class AbstractCallback<D extends Object> implements Callback {

	private D data;

	protected AbstractCallback() {
	}

	protected AbstractCallback( D data ) {
		this.data = data;
	}

	protected D getData() {
		return data;
	}

	public void setData( D data) {
		this.data = data;
	}
}
