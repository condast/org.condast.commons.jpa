package org.condast.commons.jpa.authentication.utils;

import com.google.gson.Gson;
import java.util.Map;
import java.util.Random;

import org.condast.commons.data.user.ILoginUser;

public class AuthenticationUtils {

	public static int generateSecurityCode( ILoginUser user) {
		Random random = new Random();
		int security = random.nextInt( 900000 );
		return 100000 + security;
	}
	
	public static Map.Entry<Long, Long> createDictionary( ILoginUser user ){
		return new Dictionary( user );
	}
	
	public static String createDictionaryString( ILoginUser user ) {
		Dictionary dict = (Dictionary) createDictionary(user);
		Gson gson = new Gson();
		return gson.toJson(dict, Dictionary.class);
	}
	
	public static class Dictionary implements  Map.Entry<Long, Long>{

		private long id, security;
		
		public Dictionary(ILoginUser user) {
			super();
			this.id = user.getId();
			this.security = user.getSecurity();
		}

		@Override
		public Long getKey() {
			return id;
		}

		@Override
		public Long getValue() {
			return security;
		}

		@Override
		public Long setValue(Long value) {
			this.security = value;
			return security;
		}
		
	}
}
