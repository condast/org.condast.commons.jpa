package org.condast.commons.jpa.ui.na.person;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import org.condast.commons.Utils;
import org.condast.commons.jpa.na.data.ContactData;
import org.condast.commons.jpa.na.data.ContactPersonData;
import org.condast.commons.jpa.na.model.IContact;
import org.condast.commons.jpa.na.model.IName;
import org.condast.commons.jpa.na.model.IContact.ContactTypes;
import org.condast.commons.jpa.ui.na.NALanguage;
import org.condast.commons.jpa.ui.na.contacts.ContactsTableViewer;
import org.condast.commons.strings.StringUtils;
import org.condast.commons.ui.controller.AbstractEntityComposite;
import org.condast.commons.ui.controller.EditEvent;
import org.condast.commons.ui.controller.EditEvent.EditTypes;
import org.condast.commons.ui.controller.IEditListener;
import org.condast.commons.ui.swt.InputField;
import org.condast.commons.verification.IVerification;
import org.condast.commons.verification.IVerification.VerificationTypes;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;

public class ContactPersonComposite extends AbstractEntityComposite<ContactPersonData>{
	private static final long serialVersionUID = 1L;

	private static final String S_EMAIL = "Email";
	private static final String S_CONFIRM_EMAIL = "Confirm email";
	private static final int DEFAULT_LABEL_SIZE = 115;

	public static final String RWT_HIGHLIGHT_COMPOSITE = "highlight";

	//Text fields
	public enum Fields{
		BIRTH_DATE,
		TITLE,
		TOOLTIP;
		
		public String toSring(){
			return NALanguage.getInstance().getString( this );
		}

		public String getMessage(){
			return NALanguage.getInstance().getMessage( this );
		}
	}
	
	private NameComposite nameComposite;
	private InputField text_email;
	private InputField text_confirm_email;
	
	//We need these to check confirmation, because the control's text during verification always has
	//one character less
	private String email, confirm;
	
	private ContactsTableViewer viewer;
	
	private IEditListener<IName> nameListener = e-> onNameChanged(e);
	private IEditListener<IContact> viewerListener = e -> onViewerEvent( e );

	private Collection<IEditListener<IContact>> contactListeners;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ContactPersonComposite(Composite parent, int style) {
		super(parent, style);
		this.contactListeners = new ArrayList<>();
		nameComposite.addEditListener( nameListener);
		viewer.addEditListener(viewerListener);
	}

	@Override
	protected void createComposite(Composite parent, int style) {
		setLayout(new GridLayout(1, false));
		
		Composite composite = this;

		nameComposite = new NameComposite( composite, style );
		nameComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		nameComposite.setData( RWT.CUSTOM_VARIANT, RWT_HIGHLIGHT_COMPOSITE);

		Composite emailComposite = new Composite( composite, SWT.BORDER);
		emailComposite.setLayout(new GridLayout(1, false));
		emailComposite.setData( RWT.CUSTOM_VARIANT, RWT_HIGHLIGHT_COMPOSITE);
		emailComposite.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ));

		text_email = new InputField( emailComposite, SWT.NONE);
		text_email.setLabel( S_EMAIL + ": " );
		text_email.setLabelWidth(DEFAULT_LABEL_SIZE);
		text_email.addVerifyListener(new VerifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(VerifyEvent event) {
				try {
					Text text = (Text) event.widget;
					boolean enabled = !StringUtils.isEmpty( text.getText() );
					text_confirm_email.setEnabled( enabled);
					text_confirm_email.setEditable( enabled);
					email = event.text;
					boolean complete = checkRequiredFields();
					viewer.getAddButton().setEnabled(complete);
					EditTypes type = complete?EditTypes.COMPLETE: EditTypes.CHANGED;
					ContactPersonData input = getInput();
					notifyInputEdited( new EditEvent<ContactPersonData>( this, type, input));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		text_email.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ));
		text_email.setVisible(true);

		text_confirm_email = new InputField( emailComposite, SWT.NONE);
		text_confirm_email.setLabel( S_CONFIRM_EMAIL + ": " );
		text_confirm_email.setLabelWidth(DEFAULT_LABEL_SIZE);
		text_confirm_email.setEditable(false);
		text_confirm_email.addVerifyListener(new VerifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(VerifyEvent event) {
				try {
					confirm = event.text;
					boolean complete = checkRequiredFields();
					viewer.getAddButton().setEnabled(complete);
					EditTypes type = complete?EditTypes.COMPLETE: EditTypes.CHANGED;
					ContactPersonData input = getInput();
					notifyInputEdited( new EditEvent<ContactPersonData>( this, type, input));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		text_confirm_email.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ));
		text_confirm_email.setEnabled( false);
		
        Set<IContact.ContactTypes> media = EnumSet.allOf( IContact.ContactTypes.class);
        media.remove( IContact.ContactTypes.UNKNOWN );
        media.remove( IContact.ContactTypes.FAX );
		viewer = new ContactsTableViewer( this, SWT.NONE );
		viewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		viewer.getAddButton().setEnabled(false);
		viewer.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				EditTypes type = (EditTypes) e.data;
				notifyInputEdited( new EditEvent<ContactPersonData>( this, type, getInput()));
				super.widgetSelected(e);
			}
		});
	}

	public void addContactsListener( IEditListener<IContact> listener) {
		this.contactListeners.add(listener);
	}

	public void removeContactsListener( IEditListener<IContact> listener) {
		this.contactListeners.remove(listener);
	}

	protected void onViewerEvent(EditEvent<IContact> event ) {
		this.contactListeners.forEach( l-> l.notifyInputEdited(event));
	}

	private void onNameChanged( EditEvent<IName> event ) {
		boolean complete = checkRequiredFields();
		viewer.getAddButton().setEnabled(complete);
		if( complete) {
			notifyInputEdited( new EditEvent<ContactPersonData>( this, EditTypes.COMPLETE, getInput()));
		}
	}

	@Override
	public boolean isDirty() {
		boolean dirty = super.isDirty();
		if( dirty )
			return dirty;
		dirty = nameComposite.isDirty();
		return dirty;
	}

	protected boolean isValidEntry( String email ) {
		return StringUtils.isEmpty(email)?false: IVerification.VerificationTypes.verify( VerificationTypes.EMAIL, email  );
	}

	private boolean isFilled(){
		String str = email;
		boolean filled = isValidEntry(str);
		if(!filled )
			return false;
		str = str.trim();
		String confirmed = confirm;
		filled = isValidEntry( confirmed );
		if( !filled || !confirmed.equals(str))
			return false;
		confirmed = confirmed.trim();
		return ( filled && str.equals(confirmed));
	}

	@Override
	public boolean checkRequiredFields() {
		return nameComposite.checkRequiredFields() && isFilled();
	}

	@Override
	protected ContactPersonData onGetInput(ContactPersonData input) {
		IName name = nameComposite.getInput();
		if( input == null ) {
			input = new ContactPersonData( name );
		}
		ContactData contactData = new ContactData(ContactTypes.EMAIL, this.text_email.getText());
		input.addContact(contactData);
		IContact[] contacts = viewer.getInput();
		if( Utils.assertNull(contacts))
			return input;
		input.clearContacts();
		for( IContact contact: contacts ){
			input.addContact(contact);
		}
		return input;
	}
	
	@Override
	protected void onSetInput(ContactPersonData input, boolean overwrite) {
		if( input == null )
			return;
		nameComposite.setInput( input, overwrite );
		if( !StringUtils.isEmpty(input.getEmail())) {
			this.text_email.setText( input.getEmail());
			this.text_confirm_email.setText( input.getEmail());
		}
		Collection<IContact> contacts = new ArrayList<IContact>( Arrays.asList( input.getContacts() ));
		viewer.setInput( contacts );
	}

	public void refresh() {
		boolean complete = checkRequiredFields();
		viewer.getAddButton().setEnabled(complete);
		if( complete)
			notifyInputEdited( new EditEvent<ContactPersonData>( this, EditTypes.COMPLETE, getInput()));		
	}
	
	@Override
	public void dispose(){
		nameComposite.removeEditListener( nameListener );
		this.viewer.removeSelectionListener(super.getListener());
		this.viewer.removeEditListener(viewerListener);
		super.dispose();
	}
}