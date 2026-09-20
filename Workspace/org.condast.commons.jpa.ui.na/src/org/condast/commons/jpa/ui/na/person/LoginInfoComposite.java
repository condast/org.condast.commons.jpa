package org.condast.commons.jpa.ui.na.person;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.condast.commons.data.util.LoginData;
import org.condast.commons.jpa.ui.na.NALanguage;
import org.condast.commons.strings.StringUtils;
import org.condast.commons.ui.controller.AbstractEntityComposite;
import org.condast.commons.ui.controller.EditEvent;
import org.condast.commons.ui.controller.EditEvent.EditTypes;
import org.condast.commons.ui.widgets.utils.Controls;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;

public class LoginInfoComposite extends AbstractEntityComposite<LoginData>{
	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_LABEL_WIDTH_HINT = 100;

	public enum Fields{
		NICK_NAME,
		PASSWORD,
		EMAIL;
		
		public String toString(){
			return NALanguage.getInstance().getString( this );
		}

		public String getMessage(){
			return NALanguage.getInstance().getMessage( this );
		}
}
	
	private Text text_name;
	private Text text_password;
	private Text text_email;
	
	private int lblColumnwidthHint = 0;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style. If SWT.NORMAL then the calling name is omitted
	 */
	public LoginInfoComposite(Composite parent, int style) {
		super(parent, style);
	}
	
	protected void createComposite( Composite parent, int style ){
		setLayout( new GridLayout(2, false));

		Composite composite = this;
		Label lblNaam = new Label(composite, SWT.NONE);
		GridData gd_name = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_name.widthHint = DEFAULT_LABEL_WIDTH_HINT;
		lblNaam.setLayoutData( gd_name );
		lblNaam.setText( Fields.NICK_NAME.toString());

		text_name = new Text(composite, SWT.BORDER);
		text_name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_name.addModifyListener(new ModifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void modifyText(ModifyEvent arg0) {
				try {
					notifyInputEdited( new EditEvent<LoginData>( this, EditTypes.CHANGED, getInput()));
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
		});

		Label lblPassword = new Label(composite, SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPassword.setText(Fields.PASSWORD.toString());

		text_password = new Text(composite, SWT.BORDER);
		text_password.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_password.setEnabled(false);

		Label lblEmail = new Label(composite, SWT.NONE);
		lblEmail.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEmail.setText(Fields.EMAIL.toString());

		text_email = new Text(composite, SWT.BORDER);
		text_email.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		text_email.addVerifyListener( new VerifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(VerifyEvent arg0) {
				notifyInputEdited( new EditEvent<LoginData>( this, EditTypes.CHANGED, getInput()));		
			}
		});
		text_email.addModifyListener(new ModifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void modifyText(ModifyEvent arg0) {
				notifyInputEdited( new EditEvent<LoginData>( this, EditTypes.CHANGED, getInput()));		
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
		boolean required = !StringUtils.isEmpty( text_name.getText()) &&
				!StringUtils.isEmpty( text_email.getText() );
		return required;
	}

	@Override
	public LoginData onGetInput( LoginData input ) {
		if( input == null )
			return input;
		try {
			input.setNickName( text_name.getText() );
			input.setPassword( text_password.getText() );
			input.setEmail( text_email.getText() );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return input;
	}


	@Override
	public void onSetInput(LoginData input, boolean overwrite) {
		if( overwrite){
			Controls.setText( text_name, input.getNickName() );
			Controls.setText( text_email, input.getEmail() );
			Controls.setText( text_password, input.getPassword());
		}
	}
}