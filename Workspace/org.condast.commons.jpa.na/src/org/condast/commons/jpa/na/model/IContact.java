package org.condast.commons.jpa.na.model;

import org.condast.commons.strings.StringStyler;
import org.condast.commons.verification.IVerification;
import org.condast.commons.verification.IVerification.VerificationTypes;

public interface IContact {

	public enum ContactTypes{
		UNKNOWN,
		MOBILE,
		EMAIL,
		TELEPHONE_HOME,
		TELEPHONE_WORK,
		FAX;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		/**
		 * Get the displayable texts for this enumeration
		 * @return
		 */
		public static String[] getValues(){
			String[] retval = new String[ values().length ];
			for( int i=0; i<values().length; i++ ){
				retval[i] = values()[i].name();
			}
			return retval;
		}

		/**
		 * Get the displayable texts for this enumeration
		 * @return
		 */
		public static String[] getPrettyText(){
			String[] retval = new String[ values().length ];
			for( int i=0; i<values().length; i++ ){
				retval[i] = values()[i].toString();
			}
			return retval;
		}
		
		public static boolean verify( ContactTypes type, String value ) {
			boolean result = false;
			switch( type ) {
			case MOBILE:
				result = IVerification.VerificationTypes.verify(VerificationTypes.MOBILE_PHONE, value);
				break;
			case EMAIL:
				result = IVerification.VerificationTypes.verify(VerificationTypes.EMAIL, value);
				break;
			default:
				result = true;
			}
			return result;
		}
	}

	public ContactTypes getContactType();

	public String getValue();

	/**
	 * A contact can be restricted to a certain application.
	 * If the value is negative, the contact details are known to
	 * all applications
	 * @return
	 */
	public int getApplication();

	void setContactType(ContactTypes contactType);

	void setValue(String value);

	boolean isRestricted();

	void setRestricted(boolean restricted);

	long getId();
}
