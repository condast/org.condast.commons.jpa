package org.condast.commons.jpa.authentication.service;

public interface IAuthenticator {

	/**
	 * Can never be accessed directly, so always returns false
	 */
	boolean verify(String id, String token);

	boolean supportPath(String path);

	boolean filter(String path);

}
