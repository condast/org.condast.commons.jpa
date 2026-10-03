package org.condast.commons.bugzilla.data;


import java.util.logging.LogRecord;

import org.apache.commons.codec.binary.Base64;
import org.condast.commons.log.LogUtils;

import com.google.gson.Gson;

@SuppressWarnings("unused")
public class AttachmentData {

	private static final String S_EXCEPTION = "Exception";

	private long[] ids;
	private boolean is_patch;
	private String comment;
	private boolean is_markdown;
	private String summary;
	private String content_type;
	private String data;
	private String file_name;
	private String[] obsoletes;
	private boolean is_private;
	private Flag[] flags;

	public AttachmentData( long bugid, LogRecord record ) {
		this( bugid, record.getSourceClassName(), record.getMessage(), S_EXCEPTION );
		this.data = LogUtils.getStackTrace(record);
		//Error "Base64 cannot be resolved" for next line
		//data = java.utils.Base64.getEncoder().encodeToString( data.getBytes());
		//So this line was set, with import org.apache.commons.codec.binary.Base64;
		data = Base64.encodeBase64String( data.getBytes() );
	}

	protected AttachmentData( long bugid, String name, String comment, String fileName ) {
		this( bugid, name, comment, fileName, null );
	}

	public AttachmentData( long bugid, String name, String comment, String fileName, String data ) {
		ids = new long[1];
		ids[0] = bugid;
		this.is_patch = true;
		this.is_markdown = true;
		this.is_private = false;
		this.file_name = fileName;
		this.comment = comment;
		this.content_type = "text/html";
		this.flags = new Flag[1];
		this.flags[0] = new Flag( name, "+");
	}

	private class Flag{
		private String name;
		private String status;

		public Flag(String name, String status) {
			super();
			this.name = name;
			this.status = status;
		}
	}

	public static String toJson( AttachmentData pbd ) {
		Gson gson = new Gson();
		return gson.toJson( pbd, AttachmentData.class );
	}
}
