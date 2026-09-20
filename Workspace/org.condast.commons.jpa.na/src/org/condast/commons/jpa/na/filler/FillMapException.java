package org.condast.commons.jpa.na.filler;

public class FillMapException extends Exception {
	private static final long serialVersionUID = 1L;

	public FillMapException() {
	}

	public FillMapException(String message) {
		super(message);
	}

	public FillMapException(Throwable cause) {
		super(cause);
	}

	public FillMapException(String message, Throwable cause) {
		super(message, cause);
	}

	public FillMapException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
