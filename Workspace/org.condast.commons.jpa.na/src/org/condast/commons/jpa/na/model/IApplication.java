package org.condast.commons.jpa.na.model;

import org.condast.commons.strings.StringStyler;

/**
 * Provides information about the application to which the NAW data is assigned
 * @author Kees
 *
 */
public interface IApplication extends ICode{

	public static final String S_MAILING_URL = "https://person.vps66492.public.cloudvps.com/mails_list/";

	public static final String S_APPLICATION = "Application";

	public enum ApplicationTypes{
		UNKNOWN(0),
		ADMIN(1),
		COMPANY(2),
		VASTEGAST(3),
		EETMEE_ONTMOET(4),
		EETMEE_STUDENT(5),
		EETMEE_VLUCHTELING(6);

		private int index;
		
		public int getIndex() {
			return index;
		}

		private ApplicationTypes(int index) {
			this.index = index;
		}


		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		/**
		 * Get the values as String items
		 * @return
		 */
		public static ApplicationTypes getType( int appId){
			for( ApplicationTypes at: values()){
				if( at.getIndex() == appId)
					return at;
			}
			return ApplicationTypes.UNKNOWN;
		}

		/**
		 * Get the values as String items
		 * @return
		 */
		public static String[] getItems(){
			String[] items = new String[values().length];
			for( int i=0; i<items.length; i++ ){
				items[i] = values()[i].toString();
			}
			return items;
		}
	}

	public enum ApplicationRules{
		NONE,
		POSTCODE,
		COMMUNITY,
		LOCATION;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}


		/**
		 * Get the values as String items
		 * @return
		 */
		public static String[] getItems(){
			String[] items = new String[values().length];
			for( int i=0; i<items.length; i++ ){
				items[i] = values()[i].toString();
			}
			return items;
		}
	}

	/**
	 * Return the unique code of the application and refinement combination
	 */
	@Override
	public int getCode();

	/**
	 * Get the application type
	 * @return
	 */
	public ApplicationTypes getType();

	/**
	 * set the application type
	 * @param type
	 */
	public void setType( ApplicationTypes type );

	/**
	 * Every application can be refined to, for instance, a town or area. The refinement
	 * is such that only queries that match the refinement are considered complete
	 * @return
	 */
	public String getRefinement();

	/**
	 * The location of the mail templates
	 * @return
	 */
	public String getMailUrl();

	void setRefinement(String refinememt);

	/**
	 * Every Application (Product) can be constrained by a set of rules,.
	 * for instance in which postcode range the volunteer is allowed to work
	 * These rules can be identified here, and are coupled to business logic
	 * @return
	 */
	public ApplicationRules[] getRules();
}
