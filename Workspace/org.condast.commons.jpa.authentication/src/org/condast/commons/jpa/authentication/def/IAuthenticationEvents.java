package org.condast.commons.jpa.authentication.def;

import java.net.URL;

public interface IAuthenticationEvents {

	public enum Requests{
		ACTIVATE,
		REGISTER,
		LOGIN,
		LOGOUT,
		UNREGISTER;

		public String getPath( String path ){
			return path + this.name().toLowerCase();
		}

		@Override
		public String toString() {
			String str = this.name().toLowerCase();
			return str;
		}

		public static Requests getRequest( URL url ){
			for( Requests request: values()){
				String path = url.toExternalForm();
				if( path.contains(request.toString()))
					return request;
			}
			return null;
		}
	}

}
