package org.condast.commons.jpa.ui.na.person;

import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.layout.GridLayout;

import java.util.Date;

import org.condast.commons.jpa.na.data.AddressData;
import org.condast.commons.jpa.na.data.PersonData;
import org.condast.commons.jpa.na.model.Gender;
import org.condast.commons.jpa.na.model.IAddress;
import org.condast.commons.jpa.na.model.IName;
import org.condast.commons.jpa.na.model.IVocation;
import org.condast.commons.jpa.ui.na.NALanguage;
import org.condast.commons.jpa.ui.na.address.AddressComposite;
import org.condast.commons.ui.controller.AbstractEntityComposite;
import org.condast.commons.ui.controller.EditEvent;
import org.condast.commons.ui.controller.EditEvent.EditTypes;
import org.condast.commons.ui.controller.IEditListener;
import org.condast.commons.ui.swt.DateWidget;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

public class PersonComposite extends AbstractEntityComposite<PersonData>{
	private static final long serialVersionUID = 1L;

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
	
	private AddressComposite compAddress;

	private DateWidget birthDate;
	private NameComposite nameComposite;
	private GenderComposite genderComposite;
	private SimpleVocationComposite vocationComposite;
	
	private IEditListener<IName> nameListener = e-> onNameChanged(e);
	private IEditListener<AddressData> addressListener = e-> onAddressChanged(e);
	private IEditListener<Gender> genderListener = e-> onGenderChanged(e);
	private IEditListener<Date> dateListener = e-> onDateChanged(e);
	private IEditListener<IVocation> vocationListener = e-> onVocationChanged(e);

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public PersonComposite(Composite parent, int style) {
		super(parent, style);
		nameComposite.addEditListener( nameListener);
		compAddress.addEditListener( this.addressListener );
		genderComposite.addEditListener( this.genderListener );
		birthDate.addEditListener( dateListener );
		vocationComposite.addEditListener( this.vocationListener );
	}

	@Override
	protected void createComposite(Composite parent, int style) {
		setLayout(new GridLayout(1, false));
		
		Composite composite = this;

		nameComposite = new NameComposite( composite, style );
		nameComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		nameComposite.setData( RWT.CUSTOM_VARIANT, RWT_HIGHLIGHT_COMPOSITE);

        compAddress = new AddressComposite( this, SWT.NONE );
        compAddress.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Composite detailsComposite = new Composite( this, SWT.NONE );
		detailsComposite.setLayout( new GridLayout( 1, false ));
		GridData gd_detailsComposite = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_detailsComposite.widthHint = 530;
		detailsComposite.setLayoutData( gd_detailsComposite );
		detailsComposite.setData( RWT.CUSTOM_VARIANT, RWT_HIGHLIGHT_COMPOSITE);

		genderComposite = new GenderComposite( detailsComposite, SWT.READ_ONLY );
		genderComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		birthDate = new DateWidget( detailsComposite, SWT.NONE);
		birthDate.setText( Fields.BIRTH_DATE.toSring() );
		birthDate.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		vocationComposite = new SimpleVocationComposite( detailsComposite, SWT.FULL_SELECTION | SWT.READ_ONLY );
		vocationComposite.setAlignment( 60 );
		vocationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		
		//birthDate.setInput( DefaultEntry.createDefaultDate(), true);
	}

	private void onNameChanged( EditEvent<?> event ) {
		if( this.checkRequiredFields())
			notifyInputEdited( new EditEvent<PersonData>( this, EditTypes.COMPLETE, getInput()));		
	}

	private void onAddressChanged( EditEvent<AddressData> event ) {
		if( this.checkRequiredFields())
			notifyInputEdited( new EditEvent<PersonData>( this, EditTypes.COMPLETE, getInput()));		
	}

	private void onGenderChanged( EditEvent<Gender> event ) {
		if( this.checkRequiredFields())
			notifyInputEdited( new EditEvent<PersonData>( this, EditTypes.COMPLETE, getInput()));		
	}

	private void onDateChanged( EditEvent<Date> event ) {
		if( this.checkRequiredFields())
			notifyInputEdited( new EditEvent<PersonData>( this, EditTypes.COMPLETE, getInput()));		
	}

	private void onVocationChanged( EditEvent<IVocation> event ) {
		if( this.checkRequiredFields())
			notifyInputEdited( new EditEvent<PersonData>( this, EditTypes.COMPLETE, getInput()));		
	}
	
	@Override
	public boolean isDirty() {
		boolean dirty = super.isDirty();
		if( dirty )
			return dirty;
		dirty = nameComposite.isDirty() | compAddress.isDirty() | 
				genderComposite.isDirty()| vocationComposite.isDirty();
		return dirty;
	}

	@Override
	public boolean checkRequiredFields() {
		return nameComposite.checkRequiredFields() && 
				compAddress.checkRequiredFields();
	}

	@Override
	protected PersonData onGetInput(PersonData input) {
		IName name = nameComposite.getInput();
		IAddress address = compAddress.getInput();
		//input.addAddress(AddressTypes.MAIN, address);

		input.setBirthDate( birthDate.getInput());
		input.setGender( genderComposite.getInput());
		return input;
	}
	
	@Override
	protected void onSetInput(PersonData input, boolean overwrite) {
		if( input == null )
			return;
		nameComposite.setInput( input, overwrite );
		//IPersonAddress address = input.getAddress( AddressTypes.MAIN );
		//compAddress.setInput( address, overwrite);
		genderComposite.setInput( input.getGender(), overwrite);
		if( input.getBirthDate() != null )
			birthDate.setInput(input.getBirthDate(), overwrite);
	}

	@Override
	public void dispose(){
		birthDate.removeEditListener( dateListener );
		nameComposite.removeEditListener( nameListener );
		compAddress.removeEditListener(addressListener);
		genderComposite.removeEditListener( genderListener );
		vocationComposite.removeEditListener( vocationListener );
		super.dispose();
	}
}