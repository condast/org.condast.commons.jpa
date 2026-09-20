package org.condast.commons.jpa.ui.na;

import org.condast.commons.i18n.Language;

public class NALanguage extends Language {

	private static final String S_LANGUAGE = "NALanguage";

	private static NALanguage language = new NALanguage();
	
	private NALanguage() {
		super( S_LANGUAGE, "NL", "nl");
	}
	
	public static NALanguage getInstance(){
		return language;
	}	
	
	
}
