package org.condast.commons.jpa.ui.na.address;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

import java.util.Collection;
import org.condast.commons.Utils;
import org.condast.commons.jpa.na.address.AddressChecker;
import org.condast.commons.jpa.na.data.AddressData;
import org.condast.commons.jpa.na.model.IAddress;
import org.condast.commons.jpa.na.model.IApplicationPersonController;
import org.condast.commons.jpa.na.model.IAddress.AddressTypes;
import org.condast.commons.jpa.ui.na.NALanguage;
import org.condast.commons.jpa.ui.na.images.NAImages;
import org.condast.commons.jpa.ui.na.images.NAImages.Images;
import org.condast.commons.strings.PostCodeUtils;
import org.condast.commons.strings.StringUtils;
import org.condast.commons.ui.controller.AbstractEntityComposite;
import org.condast.commons.ui.controller.EditEvent;
import org.condast.commons.ui.controller.EditEvent.EditTypes;
import org.condast.commons.ui.image.IImageProvider.ImageSize;
import org.condast.commons.ui.verification.AbstractWidgetVerificationDelegate;
import org.condast.commons.ui.verification.VerificationUtils;
import org.condast.commons.verification.IVerification.VerificationTypes;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;

public class AddressComposite extends AbstractEntityComposite<AddressData>{
	private static final long serialVersionUID = 1L;

	private static final String S_ERR_INVALID_ADDRESS = "ERR_INVALID_ADDRESS";
	
	private static final int DEFAULT_LABEL_WIDTH_HINT = 70;

	public static final String RWT_HIGHLIGHT_COMPOSITE = "highlight";

	//Text fields
	public enum Fields{
		ADDRESS,
		STREET,
		STREET2,
		NUMBER,
		POSTCODE,
		TOWN,
		TOOLTIP;
		
		public String toSring(){
			return NALanguage.getInstance().getString( this );
		}

		public String getMessage(){
			return NALanguage.getInstance().getMessage( this );
		}
	}
	
	private Text text_street;
	private Text text_street1;
	private Text text_number;
	private Text text_postcode;
	private Label lblPlace;
	private Text text_place;
	private Button btn_find;
	
	private Composite entry_composite;
	private Composite addr_composite;
	
	public AddressComposite(Composite parent, int style) {
		super(parent, style);
	}
	
	protected void createComposite( Composite parent, int syle ){
		setLayout(new GridLayout(1, false));

		entry_composite = new Composite(this, SWT.NONE);
		entry_composite.setData( RWT.CUSTOM_VARIANT, RWT_HIGHLIGHT_COMPOSITE);
		entry_composite.setLayout(new GridLayout(5, false));
		entry_composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		Label lbl_postcode= new Label(entry_composite, SWT.NONE);
		GridData gd_lblPostcode = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gd_lblPostcode.widthHint = DEFAULT_LABEL_WIDTH_HINT + 5;
		lbl_postcode.setLayoutData(gd_lblPostcode);
		lbl_postcode.setText( Fields.POSTCODE.toSring());

		text_postcode = new Text(entry_composite, SWT.BORDER);
		GridData gd_txtpostcode = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_txtpostcode.widthHint = 120;
		text_postcode.setLayoutData( gd_txtpostcode );
		text_postcode.addSelectionListener( getController().getListener() );
		text_postcode.addVerifyListener( new VerifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(VerifyEvent arg0) {
				try {
					AddressData input = getInput();
					if( input == null )
						return;
					EditTypes type = checkRequiredFields()?EditTypes.COMPLETE: EditTypes.CHANGED;
					input.setPostcode(arg0.text);
					notifyInputEdited( new EditEvent<AddressData>( this, type, input ));
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
		});
		text_postcode.addModifyListener( new ModifyListener(){
			private static final long serialVersionUID = 1L;

			@Override
			public void modifyText(ModifyEvent event) {
				try {
					boolean result = VerificationUtils.defaultVerificationAction( text_postcode.getText(), text_postcode, VerificationTypes.POSTCODE, Fields.TOOLTIP.getMessage());				 
					if( result )
						result = VerificationUtils.defaultVerificationAction( text_number.getText(), text_number, VerificationTypes.NUMBERS, Fields.TOOLTIP.getMessage());				 
					btn_find.setEnabled(result);
					getController().setDirty( true );
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		});
		
		WidgetVerificationDelegate.setVerificationType( text_postcode, VerificationTypes.POSTCODE );
		
		Label lblNummer = new Label(entry_composite, SWT.RIGHT);
		lblNummer.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		lblNummer.setText( Fields.NUMBER.toSring());
		
		text_number = new Text(entry_composite, SWT.BORDER);
		text_number.addSelectionListener( getController().getListener());
		text_number.addVerifyListener( new VerifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(VerifyEvent arg0) {
				try {
					AddressData input = getInput();
					if( input == null )
						return;
					EditTypes type = checkRequiredFields()?EditTypes.COMPLETE: EditTypes.CHANGED;
					input.setNumber(arg0.text);
					notifyInputEdited( new EditEvent<AddressData>( this, type, input ));
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
		});
		WidgetVerificationDelegate.setVerificationType( text_number, VerificationTypes.HOUSE_NUMBER );
		GridData gd_text_number = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_text_number.widthHint = 85;
		text_number.setLayoutData(gd_text_number);
		text_number.addModifyListener( new ModifyListener(){
			private static final long serialVersionUID = 1L;

			@Override
			public void modifyText(ModifyEvent event) {
				try {
					boolean result = VerificationUtils.defaultVerificationAction( text_postcode.getText(), text_postcode, VerificationTypes.POSTCODE, Fields.TOOLTIP.getMessage());				 
					if( result )
						result = VerificationUtils.defaultVerificationAction( text_number.getText(), text_number, VerificationTypes.HOUSE_NUMBER, Fields.TOOLTIP.getMessage());				 
					btn_find.setEnabled(result);
					getController().setDirty( true );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		btn_find = new Button( entry_composite, SWT.NONE );
		btn_find.setEnabled(false);
		btn_find.setLayoutData(new GridData(32, 32));
		btn_find.setImage( NAImages.getImage( Images.SEARCH_ADDRESS, ImageSize.NORMAL ));
		btn_find.addSelectionListener( new SelectionAdapter(){
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				IApplicationPersonController apc = null;// NAService.getAPController();
				AddressChecker checker = new AddressChecker( apc, null );
				Collection<IAddress> addresses = null;
				try{
					addresses = checker.findAddresses( text_postcode.getText(), text_number.getText(), true );
					if( Utils.assertNull( addresses )){
						String msg = NALanguage.getInstance().getMessage( S_ERR_INVALID_ADDRESS );
						createMessageBox( msg, msg);
						addr_composite.setEnabled(true);
						return;						
					}
				}
				catch( Exception ex ){
					String msg = NALanguage.getInstance().getMessage( S_ERR_INVALID_ADDRESS );
					createMessageBox( msg, msg);
					addr_composite.setEnabled(true);
					return;
				}
				if( !Utils.assertNull(addresses)){
					onAddressFound( new AddressData( addresses.iterator().next() ));
					btn_find.setEnabled(false);
				}else{
					getController().setBlockEntry(true);
					text_street.setText("");
					text_place.setText("");
					getController().setBlockEntry(false);
				}
			}			
		});

		//We use this composite to disable the children
		addr_composite = new Composite(entry_composite, SWT.NONE);
		addr_composite.setEnabled(false);
		addr_composite.setLayout(new GridLayout(3, false));
		addr_composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 5, 1));

		Label lblStreet = new Label(addr_composite, SWT.RIGHT | SWT.CENTER);
		GridData gd_lblStreet = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gd_lblStreet.widthHint = DEFAULT_LABEL_WIDTH_HINT;
		lblStreet.setLayoutData(gd_lblStreet);
		lblStreet.setText( Fields.STREET.toSring());
		
		text_street = new Text( addr_composite, SWT.BORDER);
		text_street.addSelectionListener( getController().getListener() );
		text_street.addVerifyListener( new VerifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(VerifyEvent arg0) {
				try {
					AddressData input = getInput();
					if( input == null )
						return;
					EditTypes type = checkRequiredFields()?EditTypes.COMPLETE: EditTypes.CHANGED;
					input.setStreet(arg0.text);
					notifyInputEdited( new EditEvent<AddressData>( this, type, input ));
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
		});
		text_street.addVerifyListener( new VerifyListener(){
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(final VerifyEvent event) {
				try {
					String[] split = event.text.split("\\s");
					String last = split[split.length - 1];
					if( Character.isDigit( last.charAt(0))){
						String street = text_street.getText();
						street = street.substring(0, street.length() - last.length());
						text_street.setText( street );
						text_number.setText( last );
						layout(false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}					
			}
		});
		WidgetVerificationDelegate.setVerificationType( text_street, VerificationTypes.ADDRESS );
		GridData gd_street = new GridData(SWT.FILL, SWT.FILL, true, false);
		text_street.setLayoutData( gd_street);
		
		Label fillLabel = new Label( addr_composite, SWT.NONE );
		GridData gd_lblFill = new GridData(SWT.FILL, SWT.FILL, false, false);
		gd_lblFill.widthHint = 30;
		fillLabel.setLayoutData( gd_lblFill);
		
		lblPlace = new Label(addr_composite, SWT.RIGHT | SWT.CENTER);
		lblPlace.setText( Fields.TOWN.toSring());
		GridData gd_lblPlace = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		gd_lblPlace.widthHint = DEFAULT_LABEL_WIDTH_HINT;
		lblPlace.setLayoutData(gd_lblPlace);
		
		text_place = new Text(addr_composite, SWT.BORDER);
		text_place.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		text_place.addSelectionListener( getController().getListener() );
		text_place.addVerifyListener( new VerifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(VerifyEvent arg0) {
				try {
					AddressData input = getInput();
					if( input == null )
						return;
					EditTypes type = checkRequiredFields()?EditTypes.COMPLETE: EditTypes.CHANGED;
					input.setTown(arg0.text);
					notifyInputEdited( new EditEvent<AddressData>( this, type, input ));
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
		});
		text_place.addModifyListener( new ModifyListener(){
			private static final long serialVersionUID = 1L;

			@Override
			public void modifyText(ModifyEvent event) {
				try {
					if( getController().isBlocked())
						return;
					getController().setDirty( true );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		WidgetVerificationDelegate.setVerificationType( text_place, VerificationTypes.NAME );		
	}

	protected Composite getEntryComposite() {
		return entry_composite;
	}

	private void onAddressFound( AddressData address ){
		getController().setBlockEntry( true );
		addr_composite.setEnabled(true);
		text_street.setText( address.getStreet());
		text_place.setText( address.getTown());
		address = null;//PersonAddress.create(address, AddressTypes.MAIN );
		setInput( address, true);
		notifyInputEdited( new EditEvent<AddressData>( addr_composite, EditTypes.CHANGED, address ));
		getController().setBlockEntry( false );		
	}
	
	/**
	 * optionally a second street line can be included
	 * @param parent
	 * @param style
	 */
	protected void createSecondStreetLine( Composite parent, int style ){
		new Label( this, SWT.NONE );
		text_street1 = new Text(this, SWT.BORDER);
		GridData gd_street1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd_street1.widthHint = 90;
		text_street1.setLayoutData( gd_street1);
		text_street1.addSelectionListener( getController().getListener() );
		text_street1.addVerifyListener( new VerifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(VerifyEvent arg0) {
				try {
					notifyInputEdited( new EditEvent<AddressData>( this, EditTypes.CHANGED, getInput()));
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
		});
		WidgetVerificationDelegate.setVerificationType( text_street1, VerificationTypes.NAME );
	}
	
	private boolean setText( Text text, String str ){
		if( StringUtils.isEmpty( str ))
			return false;
		text.setText( str );
		return true;
	}
	
	@Override
	public boolean checkRequiredFields() {
		boolean required = !StringUtils.isEmpty( text_street.getText()) &&
				!StringUtils.isEmpty( text_number.getText() ) &&
				!StringUtils.isEmpty( text_postcode.getText() ) &&
				!StringUtils.isEmpty( text_place.getText() );
		return required;
	}

	@Override
	public AddressData onGetInput( AddressData input) {
		if(( input == null ) ||( input.getAddressId() < 0))
			input = new AddressData( AddressTypes.MAIN );
		input.setNumber( this.text_number.getText());
		AddressData address = input;
		address.setStreet( text_street.getText() );
		if( text_street1 != null )
			address.setStreetExtension( text_street1.getText() );
		input.setNumber(this.text_number.getText());
		address.setPostcode( PostCodeUtils.toStyledPostcode( text_postcode.getText() ));
		address.setTown( this.text_place.getText() );
		return input;
	}

	@Override
	public void onSetInput(AddressData input, boolean overwrite) {
		if( input == null )
			return;
		AddressData address = input;
		setText( text_number, input.getHouseNumber());
		if(( address == null ) || !overwrite )
			return;
		setText( text_street, address.getStreet() );
		if( text_street1 != null )
			setText( text_street1, address.getStreetExtension());
		setText( text_postcode, address.getPostcode());
		setText( text_place, address.getTown());
	}
	
	/**
	 * Special verification options
	 * @author Kees
	 *
	 */
	private static class WidgetVerificationDelegate extends AbstractWidgetVerificationDelegate{

		@Override
		protected boolean onVerifyEvent(VerifyEvent event, VerificationTypes type) {
			boolean result = false;
			switch( type ){
			case POSTCODE:
				result = VerificationUtils.defaultVerificationAction(event, VerificationTypes.POSTCODE, Fields.TOOLTIP.getMessage());				 
				break;
			case HOUSE_NUMBER:
				result = VerificationUtils.defaultVerificationAction(event, VerificationTypes.HOUSE_NUMBER, Fields.TOOLTIP.getMessage());				 
				break;
			case CUSTOM:
				result = VerificationUtils.defaultVerificationAction(event, VerificationTypes.ADDRESS, Fields.TOOLTIP.getMessage());				 
				if( !result ){
					
				}
				break;
			default:
				result = VerificationUtils.defaultVerificationAction(event, type, Fields.TOOLTIP.getMessage());				 
				break;
			}
			return result;
		}		
	}
	
	private void createMessageBox( String title, String message ){
		MessageBox messagebox = new MessageBox( getShell(), SWT.OK );
		messagebox.setText( title );
		messagebox.setMessage( message );
		messagebox.open();

	}
}
