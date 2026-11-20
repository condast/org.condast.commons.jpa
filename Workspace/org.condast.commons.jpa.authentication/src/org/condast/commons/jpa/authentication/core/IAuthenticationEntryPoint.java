package org.condast.commons.jpa.authentication.core;

public interface IAuthenticationEntryPoint {

	void addAuthenticationListener(IAuthenticationListener listener);

	void removeAuthenticationListener(IAuthenticationListener listener);

}