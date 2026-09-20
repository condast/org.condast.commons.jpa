package org.condast.commons.jpa.ui.na.person;

import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.layout.GridLayout;
import org.condast.commons.data.util.LoginData;
import org.condast.commons.jpa.na.model.IName;
import org.condast.commons.jpa.na.profile.IProfileData;
import org.condast.commons.jpa.ui.na.NALanguage;
import org.condast.commons.ui.controller.AbstractEntityComposite;
import org.condast.commons.ui.controller.EditEvent;
import org.condast.commons.ui.controller.EditEvent.EditTypes;
import org.condast.commons.ui.controller.IEditListener;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

public class ProfileComposite extends AbstractEntityComposite<IProfileData>{
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
	
	private LoginInfoComposite loginComposite;
	private NameComposite nameComposite;
	
	private IEditListener<LoginData> loginListener = e-> onLoginChanged(e);
	private IEditListener<IName> nameListener = e-> onNameChanged(e);

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ProfileComposite(Composite parent, int style) {
		super(parent, style);
		loginComposite.addEditListener(loginListener);
		nameComposite.addEditListener( nameListener);
	}

	@Override
	protected void createComposite(Composite parent, int style) {
		setLayout(new GridLayout(1, false));
		
		Composite composite = this;

		loginComposite = new LoginInfoComposite( composite, style );
		loginComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		loginComposite.setData( RWT.CUSTOM_VARIANT, RWT_HIGHLIGHT_COMPOSITE);
		loginComposite.setEnabled(false);

		nameComposite = new NameComposite( composite, style );
		nameComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		nameComposite.setData( RWT.CUSTOM_VARIANT, RWT_HIGHLIGHT_COMPOSITE);

		Composite detailsComposite = new Composite( this, SWT.NONE );
		detailsComposite.setLayout( new GridLayout( 1, false ));
		GridData gd_detailsComposite = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_detailsComposite.widthHint = 530;
		detailsComposite.setLayoutData( gd_detailsComposite );
		detailsComposite.setData( RWT.CUSTOM_VARIANT, RWT_HIGHLIGHT_COMPOSITE);
	}

	private void onLoginChanged( EditEvent<LoginData> event ) {
		if( this.checkRequiredFields())
			notifyInputEdited( new EditEvent<IProfileData>( this, EditTypes.COMPLETE, getInput()));	
		else
			notifyInputEdited( new EditEvent<IProfileData>( this, EditTypes.CHANGED, getInput()));				
	}

	private void onNameChanged( EditEvent<IName> event ) {
		if( this.checkRequiredFields())
			notifyInputEdited( new EditEvent<IProfileData>( this, EditTypes.COMPLETE, getInput()));		
		else
			notifyInputEdited( new EditEvent<IProfileData>( this, EditTypes.CHANGED, getInput()));				
	}

	@Override
	public boolean isDirty() {
		boolean dirty = super.isDirty();
		if( dirty )
			return dirty;
		dirty = nameComposite.isDirty();
		return dirty;
	}

	@Override
	public boolean checkRequiredFields() {
		return nameComposite.checkRequiredFields();
	}

	@Override
	protected IProfileData onGetInput(IProfileData input) {
		if( input == null )
			return input;
		input.setName( loginComposite.getInput().getNickName());
		return input;
	}
	
	@Override
	protected void onSetInput(IProfileData input, boolean overwrite) {
		if( input == null )
			return;
		loginComposite.setInput(input.getLoginUser(), overwrite);
		nameComposite.setInput( input, overwrite );
		//IPersonAddress address = input.getAddress( AddressTypes.MAIN );
		//compAddress.setInput( address, overwrite);
	}
	
	@Override
	public void dispose(){
		loginComposite.removeEditListener(loginListener);
		nameComposite.removeEditListener( nameListener );
		super.dispose();
	}
}