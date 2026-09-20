package org.condast.commons.jpa.authentication.ui;

import org.condast.commons.i18n.Language;

public class AdminLanguage extends Language {

	private static final String S_LANGUAGE = "AdminLanguage";

	private static AdminLanguage language = new AdminLanguage();

	private AdminLanguage() {
		super( S_LANGUAGE, "NL", "nl");
	}

	public static AdminLanguage getInstance(){
		return language;
	}
}
