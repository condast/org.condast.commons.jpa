package org.condast.commons.jpa.ui.na.person;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.condast.commons.jpa.na.data.NameData;
import org.condast.commons.jpa.na.model.IName;
import org.condast.commons.jpa.ui.na.NALanguage;
import org.condast.commons.strings.StringUtils;
import org.condast.commons.ui.controller.AbstractEntityComposite;
import org.condast.commons.ui.controller.EditEvent;
import org.condast.commons.ui.controller.EditEvent.EditTypes;
import org.condast.commons.ui.widgets.utils.Controls;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;

public class NameComposite extends AbstractEntityComposite<IName>{
	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_LABEL_WIDTH_HINT = 70;

	public enum Fields{
		NAME,
		FIRST_NAME,
		CALLING_NAME,
		PREFIX,
		SURNAME,
		TOOLTIP;
		
		public String toSring(){
			return NALanguage.getInstance().getString( this );
		}

		public String getMessage(){
			return NALanguage.getInstance().getMessage( this );
		}
}
	
	private Text text_firstname;
	private Text text_calling_name;
	private Text text_prefix;
	private Text text_surname;
	
	private int lblColumnwidthHint = 0;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style. If SWT.NORMAL then the calling name is omitted
	 */
	public NameComposite(Composite parent, int style) {
		super(parent, style);
	}
	
	protected void createComposite( Composite parent, int style ){
		boolean full = (( style & SWT.FULL_SELECTION) > 0 );
		setLayout( new GridLayout(2, false));

		Composite composite = this;
		Label lblNaam = new Label(composite, SWT.NONE);
		GridData gd_name = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_name.widthHint = DEFAULT_LABEL_WIDTH_HINT;
		lblNaam.setLayoutData( gd_name );
		lblNaam.setText( Fields.FIRST_NAME.toSring());

		text_firstname = new Text(composite, SWT.BORDER);
		GridData gd_txtfn = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtfn.widthHint = 175;
		text_firstname.setLayoutData( gd_txtfn);
		text_firstname.addVerifyListener( new VerifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(VerifyEvent arg0) {
				try {
					IName input = getInput();
					if( input == null )
						return;
					input.setFirstName(arg0.text);
					EditTypes type = checkRequiredFields()?EditTypes.COMPLETE: EditTypes.CHANGED;
					notifyInputEdited( new EditEvent<IName>( this, type, input));
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
		});

		if( full ){
			Label lblRoepnaam = new Label(composite, SWT.NONE);
			lblRoepnaam.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblRoepnaam.setText(Fields.CALLING_NAME.toSring());

			text_calling_name = new Text(composite, SWT.BORDER);
			text_calling_name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			text_calling_name.addVerifyListener( new VerifyListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void verifyText(VerifyEvent arg0) {
					IName input = getInput();
					if( input == null )
						return;
					input.setCallingName(arg0.text);
					EditTypes type = checkRequiredFields()?EditTypes.COMPLETE: EditTypes.CHANGED;
					notifyInputEdited( new EditEvent<IName>( this, type, input));
				}
			});
		}
			
		Label lblVoorvoegsel = new Label(composite, SWT.NONE);
		lblVoorvoegsel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblVoorvoegsel.setText(Fields.PREFIX.toSring());
		
		text_prefix = new Text(composite, SWT.BORDER);
		text_prefix.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		text_prefix.addVerifyListener( new VerifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(VerifyEvent arg0) {
				IName input = getInput();
				if( input == null )
					return;
				input.setPrefix( arg0.text);
				EditTypes type = checkRequiredFields()?EditTypes.COMPLETE: EditTypes.CHANGED;
				notifyInputEdited( new EditEvent<IName>( this, type, input));
			}
		});
		
		Label lblSurname = new Label(composite, SWT.NONE);
		lblSurname.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSurname.setText( Fields.SURNAME.toSring());
		
		text_surname = new Text(composite, SWT.BORDER);
		text_surname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_surname.addVerifyListener( new VerifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(VerifyEvent arg0) {
				IName input = getInput();
				if( input == null )
					return;
				input.setSurname(arg0.text);
				EditTypes type = checkRequiredFields()?EditTypes.COMPLETE: EditTypes.CHANGED;
				notifyInputEdited( new EditEvent<IName>( this, type, input));
			}
		});
	}

	public int getLblColumnwidthHint() {
		return lblColumnwidthHint;
	}

	public void setLblColumnwidthHint(int widthHint ) {
		for( Control control: super.getChildren() ){
			if( control instanceof Label ){
				control.setSize( new Point( widthHint, control.getSize().y ));
			}
		}
		this.lblColumnwidthHint = widthHint;
	}
		
	@Override
	public boolean checkRequiredFields() {
		boolean required = !StringUtils.isEmpty( text_firstname.getText()) &&
				!StringUtils.isEmpty( text_surname.getText() );
		return required;
	}

	@Override
	public IName onGetInput( IName input ) {
		if( input == null )
			input = new NameData( );
		try {
			input.setFirstName( text_firstname.getText() );
			if( text_calling_name != null )
				input.setName( text_calling_name.getText() );
			input.setPrefix( text_prefix.getText() );
			input.setSurname( text_surname.getText() );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return input;
	}


	@Override
	public void onSetInput(IName input, boolean overwrite) {
		if( overwrite){
			Controls.setText( text_firstname, input.getFirstName() );
			Controls.setText( text_prefix, input.getPrefix() );
			Controls.setText( text_surname, input.getSurname() );
			if( this.text_calling_name != null )
				Controls.setText( text_calling_name, input.getName());
		}
	}
}