package org.condast.commons.jpa.rest;

import org.condast.commons.strings.StringStyler;

public interface IRestPages {

	//Same as the alias in plugin.xml
	public static final String S_CONDAST = "condast";
	public static final String S_CONDAST_REST_PATH = S_CONDAST + "-rest";

	public static enum Pages{
		AUTH;

		@Override
		public String toString() {
			return StringStyler.xmlStyleString( name());
		}

		public String toPath() {
			StringBuilder builder = new StringBuilder();
			builder.append( S_CONDAST_REST_PATH ); 
			builder.append( "/" );
			switch( this ) {
			case AUTH:
				builder.append( Pages.AUTH.toString());
				break;
			default:
				break;
			}
			return builder.toString();
		}
	}
}
