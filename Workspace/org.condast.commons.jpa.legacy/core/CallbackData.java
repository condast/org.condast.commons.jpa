package org.condast.commons.authentication.ui.core;

public class CallbackData {

	private String title;
	private String context;
	private String termsOfUsePath;
	private String privacyPath;

	public CallbackData(String title, String context, String termsOfUsePath, String privacyPath) {
		super();
		this.title = title;
		this.context = context;
		this.termsOfUsePath = termsOfUsePath;
		this.privacyPath = privacyPath;
	}

	public String getTitle() {
		return title;
	}

	public String getContext() {
		return context;
	}

	public String getTermsOfUsePath() {
		return termsOfUsePath;
	}

	public String getPrivacyPath() {
		return privacyPath;
	}
}
