package org.condast.commons.jpa.ui.na.contacts;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.condast.commons.jpa.na.model.IContact;
import org.condast.commons.jpa.na.model.IContact.ContactTypes;
import org.condast.commons.jpa.ui.na.NALanguage;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

public class ContactDialog extends TitleAreaDialog {
	private static final long serialVersionUID = 1L;

	
	public enum Fields{
		CONTACT_TITLE,
		RESTRICT,
		OK,
		CANCEL;
		
		public String toString(){
			return NALanguage.getInstance().getString( this );
		}
		
		public String getMessage(){
			return NALanguage.getInstance().getMessage( this );
		}		
	}
	
	protected int result;
	
	private ContactWidget widget;
	
	private IContact contact;
	
	private List<ContactTypes> selection;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ContactDialog( Composite parent ) {
		this( parent, EnumSet.allOf(ContactTypes.class ), null );
	}

	public ContactDialog( Composite parent, Collection<ContactTypes> selection ) {
		this(parent, selection, null );
	}
		
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ContactDialog( Composite parent, Collection<ContactTypes> selection, IContact contact ) {
		super(parent.getDisplay().getActiveShell());
		this.selection = new ArrayList<ContactTypes>( selection );
		this.contact = contact;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText( Fields.CONTACT_TITLE.toString() );
		super.configureShell(newShell);
	}

	protected void createContactTypes(){
		Collection<String> contacts = new ArrayList<String>();
		for( ContactTypes type: selection ){
			contacts.add( NALanguage.getInstance().getString( type ));
		}
		widget.setItems(selection, contacts.toArray( new String[ contacts.size() ]));
	}
		
	/**
	 * Create contents of the dialog.
	 */
	@Override
	protected Control createDialogArea( Composite parent ) {
		setTitle( Fields.CONTACT_TITLE.getMessage() );
		
	    Composite area = (Composite) super.createDialogArea(parent);
		widget = new ContactWidget( area, SWT.NONE );
		widget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createContactTypes();
		widget.select( selection.indexOf( ContactTypes.TELEPHONE_HOME ));
		//widget.addVerifyListener( new VerifyListener(){
		//	private static final long serialVersionUID = 1L;
//
//			@Override
//			public void verifyText(VerifyEvent event) {
//				Button button = getButton(IDialogConstants.OK_ID);	
//				button.setEnabled( Boolean.TRUE.equals( event.data ));
//			}
//		});
		widget.setInput( contact, true);
		return area;
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		Control  buttonBar = super.createButtonBar(parent);
		Button button = super.getButton(IDialogConstants.OK_ID);
		button.setText( Fields.OK.toString() );
		button.setEnabled(false);
		super.getButton(CANCEL).setText( Fields.CANCEL.toString() );
		return buttonBar;
	}

	public IContact getInput(){
		return contact;
	}

	@Override
	protected void okPressed() {
		this.contact = widget.getInput();
		super.okPressed();
	}
}
