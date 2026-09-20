package org.condast.commons.jpa.na.data;

import org.condast.commons.jpa.na.model.IName;

public class NameData implements IName {

	private String name;
	private String firstName;
	private String callingName;
	private String prefix;
	private String surName;
		
	public NameData() {
		super();
	}

	public NameData(String name, String firstName, String callingName, String prefix, String surName) {
		super();
		this.name = name;
		this.firstName = firstName;
		this.callingName = callingName;
		this.prefix = prefix;
		this.surName = surName;
	}

	@Override
	public String getFirstName() {
		return this.firstName;
	}

	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public String getCallingName() {
		return callingName;
	}

	@Override
	public void setCallingName(String callingName) {
		this.callingName = callingName;
	}

	@Override
	public String getPrefix() {
		return this.prefix;
	}

	@Override
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public String getSurname() {
		return this.surName;
	}

	@Override
	public void setSurname(String surName) {
		this.surName = surName;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(IName o) {
		// TODO Auto-generated method stub
		return 0;
	}


}
