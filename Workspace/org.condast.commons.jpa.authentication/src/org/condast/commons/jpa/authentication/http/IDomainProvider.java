package org.condast.commons.jpa.authentication.http;

import org.condast.commons.strings.StringStyler;

public interface IDomainProvider<D extends Object> extends Comparable<IDomainProvider<D>>{

	public enum Attributes{
		USER_ID,
		TOKEN,
		DOMAIN,
		PATH,
		SECURITY;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static boolean isValid( String str ) {
			String test = StringStyler.styleToEnum(str);
			for( Attributes attr: values()) {
				if( attr.name().equals(test))
					return true;
			}
			return false;
		}

		public String toAttribute(){
			return StringStyler.xmlStyleString(name());
		}

		public static Attributes getAttribute( String str ){
			return Attributes.valueOf( StringStyler.styleToEnum(str));
		}
	}

	/**
	 * The path to redirect to after the external service is completed. 
	 * Automatically adds the token and domain as rguments
	 * @return
	 */
	public String getReturnPath();
	
	/**
	 * Get the name of the domain;
	 * @return
	 */
	public String getDomain();
	
	/**
	 * returns true if this provider is correct for the given domain name
	 * @param domain
	 * @return
	 */
	public boolean isDomain( String domain );
	
	/**
	 * When first accessing the login user page, create a token for the given domain
	 * @param domain
	 * @return
	 */
	public long getToken( );
	
	/**
	 * returns true if the given user is logged in, under the given token
	 * @param userid
	 * @param token
	 * @return
	 */
	public boolean accept( long userid, long token );
	
	public D getData();
	
	public void setData( D data );
}
