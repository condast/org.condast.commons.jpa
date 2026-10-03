package org.condast.commons.bugzilla.data;

import org.condast.commons.Utils;
import org.condast.commons.bugzilla.core.BugzillaService;
import org.condast.commons.jpa.messaging.IHttpRequest;
import org.condast.commons.strings.StringUtils;

import com.google.gson.Gson;

@SuppressWarnings("unused")
public class BugData {

	private String[] faults;
	private Bug[] bugs;

	public BugData() {
	}

	public long getId() {
		return Utils.assertNull( bugs )?-1: bugs[0].id;
	}

	public int update() {
		int amount = 0;
		for( Bug bug: bugs ) {
			int temp = bug.getIncrement();
			if( temp > amount )
				amount = temp;
		}
		return amount +1;
	}

	public static BugData fromJson( BugzillaService service ) {
		return fromJson(service.getResponseCode(), service.getResponse());
	}

	public static BugData fromJson( int responsecode, String data ) {
		Gson gson = new Gson();
		String substring = null;
		if( StringUtils.isEmpty(data))
			substring = String.valueOf( IHttpRequest.HttpStatus.getHttpStatus(responsecode));
		else
			substring = data.replaceAll("\\s+","");
        return gson.fromJson( substring, BugData.class );
	}

	private class Bug{

		private String assigned_to;
		private Details assigned_to_detail;
		private String resolution;
		private long id;
		private String qa_contact;
		private String version;
		private String status;
		private String creator;
		private String cf_drop_down;
		private String summary;
		private String last_change_time;
		private String platform;
		private String url;
		private String classification;
		private String[] cc;
		private Details[] cc_detail;
		private String priority;
		private boolean is_confirmed;
		private String creation_time;
		private Flags[] flags;
		private String[] alias;
		private String cf_large_text;
		private String[] groups;
		private String op_sys;
		private long cf_bug_id;
		private String[] depends_on;
		private boolean is_cc_accessible;
		private boolean is_open;
		private String cf_qa_list_4;
		private String[] keywords;
		private String[] see_also;
		private String deadline;
		private boolean is_creator_accessible;
		private String whiteboard;
		private String dupe_of;
		private String arget_milestone;
		private String[] cf_mulitple_select;
		private String component;
		private String severity;
		private String cf_date;
		private String product;
		private Details creator_detail;
		private String cf_free_text;
		private String[] blocks;

		public int getIncrement() {
			int increment = 2;
			for( String keyword: keywords) {
				int start = keyword.indexOf("increment");
				if( start < 0 )
					return increment;
				String subs = keyword.substring(start);
				start = subs.indexOf(":");
				int end = subs.indexOf("]");
				return Integer.parseInt( subs.substring(start, end-1));
			}
			return increment;
		}

	}

	private class Flags{
		private long type_id;
		private String modification_date;
		private String name;
		private String status;
		private long id;
		private String setter;
		private String creation_date;
	}

	private class Details{
		private long id;
		private String real_name;
		private String name;
		private String email;
	}
}