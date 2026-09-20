package org.condast.commons.jpa.na.model;

public interface IProfessional extends IPerson{

	public abstract IVocation getVocation();

	public abstract void setVocation( IVocation vocation );

	void addApplicationPerson( IApplicationPerson ap);

	void removeApplicationPerson(IApplicationPerson ap);

	public IApplicationPerson[] getApplicationPersons();

	String getHobbies();

	void setHobbies(String hobbies);

	String getEthnicity();

	void setEthnicity(String ethnicity);
}