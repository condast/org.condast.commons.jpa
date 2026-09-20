package org.condast.commons.jpa.authentication.user;

public class AnonymousUser extends AbstractLoginUser {

	public static final String S_ANONYMOUS ="Anonymous";
	public static final String S_EMAIL ="info@condast.com";

	public AnonymousUser() {
		super( S_ANONYMOUS, S_ANONYMOUS, S_EMAIL );
	}

	public AnonymousUser(String userName, String password ) {
		super(userName, password, S_EMAIL);
	}


}