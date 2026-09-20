package org.condast.commons.jpa.na.model;

import java.util.Date;

import org.condast.commons.jpa.na.model.IAddress.AddressTypes;

public interface IPerson{

	public long getPersonId();

	/**
	 * @return the naam
	 */
	public abstract IName getName();

	/**
	 * @param naam the naam to set
	 */
	public abstract void setName(IName naam);

	public abstract String getFullName();

	public abstract String getTitle();

	public abstract void setTitle( String title );

	public abstract Gender getGender();

	public abstract void setGender( Gender gender);

	/**
	 * @return the date of birth
	 */
	public abstract Date getBirthDate();

	/**
	 * @param dateOfBirth the date of birth to set
	 */
	public abstract void setBirthDate( Date birthDate);

	public Integer getAge();

	@Override
	public abstract String toString();

	public abstract Object clone() throws CloneNotSupportedException;

	public IAddress[] getAddresses();

	/**
	 * Get the address for the given address type
	 * @param type
	 * @return
	 */
	public IPersonAddress getAddress( AddressTypes type );

	public IPersonAddress[] getPersonAddresses();

	public void removeAddress( IAddress address );

	void addAddress(AddressTypes type, IPersonAddress address);
}