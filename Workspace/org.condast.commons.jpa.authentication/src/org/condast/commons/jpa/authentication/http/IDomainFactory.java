package org.condast.commons.jpa.authentication.http;

/**
 * Returns all the necessary data to continue a web service that was taken over by another bundle.
 * for instance, when logging in, the authentication bundle carries out the actual tasks, and returns the
 * login data upon completion
 * @author Kees
 *
 * @param <D>
 */
public interface IDomainFactory<D extends Object> {

	public IDomainProvider<D> getDomain( String domain, long token );
}
