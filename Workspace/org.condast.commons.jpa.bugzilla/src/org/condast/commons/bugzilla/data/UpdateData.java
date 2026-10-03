package org.condast.commons.bugzilla.data;

import org.condast.commons.bugzilla.core.AbstractData;

@SuppressWarnings("unused")
public class UpdateData extends AbstractData{

	private long[] ids;
	private String status;
	private KeyWord[] keywords;


	public UpdateData( int increment ) {
		this.status = "IN_PROGRESS";
		keywords = new KeyWord[1];
		keywords[0] = new KeyWord(increment);
	}

	private class KeyWord{
		private String[] add;

		public KeyWord( int increment ){
			super();
			add = new String[2];
			add[0] = "increment";
			add[1] = String.valueOf(increment);
		}
	}
}
