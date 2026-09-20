package org.condast.commons.jpa.na.data;

import java.io.Serializable;
import java.util.Date;

import org.condast.commons.jpa.na.model.Gender;
import org.condast.commons.jpa.na.model.IContactPerson;
import org.condast.commons.jpa.na.model.IName;
import org.condast.commons.jpa.na.model.IProfessional;

/**
 * The persistent class for the eet_tb_persoon database table.
 */
public class PersonData extends ContactPersonData implements IName, IContactPerson, Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private String title;
	private String description;
	
	private Date birthDate;
	
	private Gender gender;

	public PersonData( ){
		super();
	}

	public PersonData( IContactPerson person ){
		super( person);
		this.name = person.getName();
	}

	public PersonData( IName name ){
		super(name);
		this.name = name.getName();
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public void setCallingName(String callingName) {
		this.name = callingName;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Override
	public int compareTo(IName o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * @return
	 */
	public static final String getFullName( PersonData name ) {
		String retval = "";
		IName naam = name;
		if (naam != null) {
			retval = naam.getName();
		}
		return retval;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append( name );
		buffer.append( "\n");
		return buffer.toString();
	}

	/**
	 * @return
	 */
	public static final String getFullName( IProfessional table ) {
		String retval = "";
		IName naam = table.getName();
		if (naam != null) {
			retval = naam.getName();
		}
		return retval;
	}
}