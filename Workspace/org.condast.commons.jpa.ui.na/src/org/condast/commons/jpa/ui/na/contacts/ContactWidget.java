package org.condast.commons.jpa.ui.na.contacts;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.condast.commons.jpa.na.data.ContactData;
import org.condast.commons.jpa.na.model.IContact;
import org.condast.commons.jpa.na.model.IContact.ContactTypes;
import org.condast.commons.jpa.ui.na.NALanguage;
import org.condast.commons.strings.StringUtils;
import org.condast.commons.ui.controller.AbstractEntityComposite;
import org.condast.commons.ui.controller.EditEvent;
import org.condast.commons.ui.controller.EditEvent.EditTypes;
import org.condast.commons.ui.verification.AbstractWidgetVerificationDelegate;
import org.condast.commons.ui.verification.VerificationUtils;
import org.condast.commons.verification.IVerification.VerificationTypes;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;

public class ContactWidget extends AbstractEntityComposite<IContact> {
	private static final long serialVersionUID = 1L;
	
	private Text text;
	private Combo combo;
	private List<ContactTypes> types;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ContactWidget(Composite parent, int style) {
		super(parent, style );
	}
	
	protected void createComposite( Composite parent, int style ){	
		setLayout(new GridLayout(3, false));

		combo = new Combo(this, SWT.NONE);
		combo.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					VerificationDelegate delegate = new VerificationDelegate();
					VerificationTypes type = delegate.getVerificationType(combo);
					if( VerificationUtils.defaultVerificationAction(text.getText(), text, type, "verify"))
						notifyInputEdited( new EditEvent<>( this, EditTypes.SELECTED, getInput()));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				super.widgetSelected(e);
			}		
		});
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		text = new Text(this, SWT.BORDER);
		text.addSelectionListener( getListener() );
		text.addVerifyListener( new VerifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(VerifyEvent arg0) {
				try {
					VerificationDelegate delegate = new VerificationDelegate();
					VerificationTypes type = delegate.getVerificationType(combo);
					if( !VerificationUtils.defaultVerificationAction(arg0, type, "verify"))
						return;
					IContact input = getInput();
					input.setValue(arg0.text);
					notifyInputEdited( new EditEvent<>( this, EditTypes.COMPLETE, input));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			
		});
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		text.setLayoutData( gd);	
	}
	
	public String[] getItems(){
		return this.combo.getItems();
	}
	
	public void setItems( Collection<ContactTypes> types, String[] items ){
		this.types =  new ArrayList<>( types );
		this.combo.setItems(items);
		this.combo.select(0);
	}
	
	public int getSelectionIndex(){
		return this.combo.getSelectionIndex();
	}
	
	public void select( int index ){
		this.combo.select( index );
	}

	
	@Override
	public boolean checkRequiredFields() {
		return !StringUtils.isEmpty( text.getText());
	}

	@Override
	protected IContact onGetInput(IContact contact) {
		if( contact == null )
			contact = new ContactData();
		contact.setContactType( types.get( combo.getSelectionIndex() ));
		contact.setValue(text.getText());
		return contact;
	}

	@Override
	protected void onSetInput(IContact input, boolean overwrite) {
		if( input == null )
			return;
		if( overwrite ){
			combo.select( input.getContactType().ordinal());
			if( !StringUtils.isEmpty( input.getValue() ))
				this.text.setText( input.getValue() );
		}
	}
	
	@Override
	public void dispose() {
		combo.removeSelectionListener( super.getListener() );
		text.removeSelectionListener(  super.getListener());
		super.dispose();
	}	

	public static void createContactTypes( ContactWidget widget, Set<IContact.ContactTypes> selection){
		Collection<String> contacts = new ArrayList<String>();
		for( ContactTypes type: selection ){
			contacts.add( NALanguage.getInstance().getString( type ));
		}
		widget.setItems(selection, contacts.toArray( new String[ contacts.size() ]));
	}

	/**
	 * Special verificatation options
	 * @author Kees
	 *
	 */
	private class VerificationDelegate extends AbstractWidgetVerificationDelegate{
		
		private VerificationTypes getVerificationType( Combo combo ){
			ContactTypes ct = types.get( combo.getSelectionIndex()); 
			VerificationTypes vtype = VerificationTypes.TELEPHONE;
			switch( ct ){
			case TELEPHONE_HOME:
				vtype = VerificationTypes.TELEPHIONE_NO_MOBILE;
				break;
			case EMAIL:
				vtype = VerificationTypes.EMAIL;
				break;
			case MOBILE:
				vtype = VerificationTypes.MOBILE_PHONE;
				break;
			default:
				break;
			}
			return vtype;
		}

		@Override
		protected boolean onVerifyEvent(VerifyEvent event, VerificationTypes type) {
			ContactTypes ct = types.get( combo.getSelectionIndex()); 
			VerificationTypes vtype = getVerificationType( combo );
			String message = NALanguage.getInstance().getMessage( ct.name() );
			if( event.widget.equals( combo ))
				return VerificationUtils.defaultVerificationAction( text.getText(), text, vtype, message );			
			else
				return VerificationUtils.defaultVerificationAction(event, vtype, message );				 
		}
	}
}