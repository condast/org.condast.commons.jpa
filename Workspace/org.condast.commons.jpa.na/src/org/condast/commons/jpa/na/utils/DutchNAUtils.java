/*******************************************************************************
 * Copyright (c) 2016 Condast and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Condast                - EetMee
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.condast.commons.jpa.na.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validation of NA data
 * @see: http://www.molenaar-technologies.nl/regex-op-zn-nederlands/
 * @author Kees
 *
 */
public class DutchNAUtils {

	/**
	 * Validates:
	 * Straat 12
	 * Straat 12-14
	 * Straat 12b
	 * Straat 12 II
	 * Dr. J. Straat 12
	 * Dr. J. Straat 12 a
	 * Dr. J. Straat 12-14
	 */
	//private static final String REGEX_ADDRESS = "/^([1-9][e][\\s])*([a-zA-Z]+(([\\.][\\s])|([\\s]))?)+[1-9][0-9]*(([-][1-9][0-9]*)|([\\s]?[a-zA-Z]+))?$/i";

	/**
	 * Validates:
	 * 1000 AB
	 * 1000AB
	 */
	private static final String REGEX_POSTCODE = "/^[1-9][0-9]{3}[\\s]?[A-Za-z]{2}$/i";

	private static final String REGEX_POSTCODE_IN_STRING = "[1-9][0-9]{3}[\\s]?[A-Za-z]{2}";

	//private static final String REGEX_TOWNS = "/^(([2][e][[:space:]]|['][ts][-[:space:]]))?[����a-zA-Z]{2,}((\\s|[-](\\s)?)[����a-zA-Z]{2,})*$/i";

	//private static final String S_TEL_NR = "/^(((0)[1-9]{2}[0-9][-]?[1-9][0-9]{5})|((\\+31|0|0031)[1-9][0-9][-]?[1-9][0-9]{6}))$/";

	//private static final String S_MOBILE_TEL_NR = "/^(((\\+31|0|0031)6){1}[1-9]{1}[0-9]{7})$/i";

	public DutchNAUtils() {

	}

	public static boolean validatePostCode(String postcode){
		Pattern pattern = Pattern.compile(REGEX_POSTCODE);
		Matcher matcher = pattern.matcher(postcode);

		return matcher.find();

	}

	public static boolean validatePostCodeInString(String postcodeString){
		Pattern pattern = Pattern.compile(REGEX_POSTCODE_IN_STRING);
		Matcher matcher = pattern.matcher(postcodeString);

		return matcher.find();

	}

	public static String extractPostcode(String withPostcode){

		Pattern pattern = Pattern.compile(REGEX_POSTCODE_IN_STRING);
		Matcher matcher = pattern.matcher(withPostcode);
		String postcode = "";
		while(matcher.find()){
			postcode = matcher.group();
			break;
		}

		return postcode;
	}



}
