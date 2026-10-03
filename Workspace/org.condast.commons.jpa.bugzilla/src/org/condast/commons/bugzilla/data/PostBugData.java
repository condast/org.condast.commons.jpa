package org.condast.commons.bugzilla.data;

import org.condast.commons.strings.StringStyler;

import com.google.gson.Gson;

@SuppressWarnings("unused")
public class PostBugData {

	public enum DefaultValues{
		ALL,
		HIGHEST,
		UNSPECIFIED;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	private String product;
	private String component;
	private String version;
	private String summary;
	private String alias;
	private String priority;
	private String op_sys;
	private String rep_platform;
	private KeyWord[] keywords;


	public PostBugData(String product, String component, String alias, long amount) {
		this( product, component, DefaultValues.UNSPECIFIED.toString(),
				String.valueOf( amount ), alias,
				DefaultValues.HIGHEST.toString(),
				DefaultValues.ALL.toString(),
				DefaultValues.ALL.toString());
	}


	public PostBugData(String product, String component, String version, String summary, String alias, String priority,
			String op_sys, String rep_platform) {
		super();
		this.product = product;
		this.component = component;
		this.version = version;
		this.summary = summary;
		this.alias = alias.replaceAll("\\s+", "_");
		this.priority = priority;
		this.op_sys = op_sys;
		this.rep_platform = rep_platform;
		this.keywords = new KeyWord[1];
		this.keywords[0] = new KeyWord();
	}

	public static PostBugData fromJson( String data ) {
		Gson gson = new Gson();
		return gson.fromJson(data, PostBugData.class );
	}

	public static String toJson( PostBugData pbd ) {
		Gson gson = new Gson();
		return gson.toJson( pbd, PostBugData.class );
	}

	public static String toCorrectAlias( String alias ) {
		return alias.replaceAll("\\s+", "_");
	}

	private class KeyWord{
		private String[] add;

		public KeyWord(){
			super();
			add = new String[2];
			add[0] = "increment";
			add[1] = "1";
		}
	}

}
