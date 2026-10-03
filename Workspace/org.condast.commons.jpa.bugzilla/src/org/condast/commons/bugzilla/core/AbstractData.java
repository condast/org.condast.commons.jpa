package org.condast.commons.bugzilla.core;

import com.google.gson.Gson;

public abstract class AbstractData {

	protected AbstractData() {
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.bugzilla.core.IBugData#fromJson(java.lang.String)
	 */
	public AbstractData fromJson( String data ) {
		Gson gson = new Gson();
		return gson.fromJson(data, this.getClass() );
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.bugzilla.core.IBugData#toJson()
	 */
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson( this, getClass());
	}

}
