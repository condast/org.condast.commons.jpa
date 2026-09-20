package org.condast.commons.jpa.na.model;

import java.text.ParseException;
import java.util.Collection;
import java.util.Map;

import org.condast.commons.jpa.na.address.AddressException;
import org.condast.commons.jpa.na.model.IAddress.AddressTypes;
import org.condast.commons.jpa.na.questionaire.IQuestionnaire;
import org.condast.commons.persistence.service.IPersistencyController;

public interface IApplicationPersonController extends IPersistencyController<IApplicationPerson, IApplication>{

	/**
	 * Create an application person for the given application and person
	 * @param application
	 * @param person
	 * @return
	 */
	public IApplicationPerson create( IApplication application, IProfessional person );

	/**
	 * Find the persons with the given person ids. return the personId as key of the map
	 * @param personIds
	 * @return
	 */
	Map<Long, IApplicationPerson> findPersons( Collection<Long> personIds );

	/**
	 * Add the given address to the given person
	 * @param person
	 * @param address
	 * @return
	 */
	IPersonAddress addAddress( IPerson person, IPersonAddress address, AddressTypes type );

	/**
	 * Find the addresses with the given postal code
	 * @param personIds
	 * @return
	 */
	Collection<ICommunityAddress> findAddresses( String postcode );

	/**
	 * Find the addresses with the given postcode and number
	 * @param postcode
	 * @param number
	 * @return
	 */
	Collection<IAddress> findAddresses( String postcode, String number ) throws AddressException;

	/**
	 * Create an application person from a questionnaire
	 * @param questionaire
	 * @return
	 * @throws ParseException
	 */
	IApplicationPerson create( IApplication application, IQuestionnaire questionaire ) throws ParseException;

	/**
	 * Create an other person from the given one (e.g. family member or house mate)
	 * @param main
	 * @return
	 */
	IProfessional createOther( IProfessional mainPerson, AddressTypes type );

	/**
	 * Create an other person, based on the given one
	 * @param mainPerson
	 * @param qnr
	 * @return
	 * @throws ParseException
	 * @throws CloneNotSupportedException
	 */
	IApplicationPerson createOther( IApplicationPerson mainPerson, IQuestionnaire questionnaire )
			throws ParseException, CloneNotSupportedException;

	/**
	 * Create an other person from the given mainperson (e.g. family member or house mate)
	 * @param mainPerson
	 * @return memberPerson
	 */
	IPerson createOtherPerson( IApplication application, IPerson mainPerson, AddressTypes type );

	void deletePerson( IProfessional person );

	public IProfessional findPerson( long personId );
}