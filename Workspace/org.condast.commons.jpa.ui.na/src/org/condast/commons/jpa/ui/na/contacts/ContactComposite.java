package org.condast.commons.jpa.ui.na.contacts;

import org.eclipse.swt.widgets.Composite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.condast.commons.Utils;
import org.condast.commons.jpa.na.data.ContactPersonData;
import org.condast.commons.jpa.na.model.IContact;
import org.condast.commons.jpa.na.model.IContact.ContactTypes;
import org.condast.commons.jpa.ui.na.NALanguage;
import org.condast.commons.ui.controller.AbstractEntityGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;

public class ContactComposite extends AbstractEntityGroup<ContactPersonData> {
	private static final long serialVersionUID = 1L;

	//Text fields
	private enum Fields{
		CONTACT;
		
		public String toSring(){
			return NALanguage.getInstance().getString( this );
		}
	}
	
	private ContactsTableViewer viewer;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ContactComposite(Composite parent, int style, Collection<ContactTypes> media) {
		super(parent, style);
	}
	
	@Override
	protected void createComposite(Composite parent, int style) {
		setText( Fields.CONTACT.toSring() );
		setLayout( new FillLayout() );
		viewer = new ContactsTableViewer( this, SWT.NONE );
		viewer.addSelectionListener( super.getListener());
	}
	
	@Override
	public boolean checkRequiredFields() {
		return !viewer.isEmpty();
	}

	@Override
	protected ContactPersonData onGetInput(ContactPersonData input) {
		if( Utils.assertNull( viewer.getInput() ))
			return input;
		IContact[] contacts = viewer.getInput();
		input.clearContacts();
		for( IContact contact: contacts ){
			input.addContact(contact);
		}
		return input; 
	}

	@Override
	protected void onSetInput( ContactPersonData input , boolean overwrite) {
		if( !overwrite || ( input == null ) )
			return;
		Collection<IContact> contacts = new ArrayList<IContact>( Arrays.asList( input.getContacts() ));
		viewer.setInput( contacts );
	}
	
	@Override
	public void dispose(){
		if( this.viewer != null )
			this.viewer.removeSelectionListener(super.getListener());
	}
}