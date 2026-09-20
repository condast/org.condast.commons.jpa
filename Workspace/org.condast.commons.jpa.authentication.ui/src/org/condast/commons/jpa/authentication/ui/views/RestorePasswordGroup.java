package org.condast.commons.jpa.authentication.ui.views;

import org.condast.commons.strings.StringUtils;
import org.condast.commons.ui.controller.AbstractEntityGroup;
import org.condast.commons.ui.controller.EditEvent;
import org.condast.commons.ui.controller.EditEvent.EditTypes;
import org.condast.commons.ui.swt.InputField;
import org.condast.commons.ui.verification.VerificationUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class RestorePasswordGroup extends AbstractEntityGroup<String>
{
	private static final long serialVersionUID = 4936423612139257363L;

	private static final String S_NEW_PASSWORD = "Please enter a new password:";
	private static final String S_PASSWORD = "Password";
	private static final String S_CONFIRM_PASSWORD = "Confirm Password";

	private static final String S_PASSWORD_INFORMATION_TIP = "Provide a password, and confirm this in the field below.";
	private static final String S_PASSWORD_CONFIRMATION_TIP = "The password and confirmation should be the same.";

	private static final int DEFAULT_LABEL_SIZE = 115;

	private InputField text_password;
	private InputField text_confirm;

	private Button okButton;

	private RestorePasswordGroup container;

	public RestorePasswordGroup(Composite parent, int style) {
		super(parent, style);
		this.container = this;
	}

	@Override
	protected void createComposite(Composite parent, int style) {
		setLayout( new GridLayout(1, false ));

		setText( S_NEW_PASSWORD + ": " );

		text_password = new InputField(this, SWT.PASSWORD);
		text_password.setLabel( S_PASSWORD + ": " );
		text_password.setLabelWidth(DEFAULT_LABEL_SIZE);
		text_password.setToolTipText(S_PASSWORD_INFORMATION_TIP);
		text_password.addVerifyListener(new VerifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(VerifyEvent event) {
				try {
					text_confirm.setEnabled( !StringUtils.isEmpty( text_password.getText() ));
					boolean similar = text_password.getText().equals( text_confirm.getText() );
					VerificationUtils.defaultVerificationAction( similar, text_confirm, S_PASSWORD_CONFIRMATION_TIP );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		text_password.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false));

		text_confirm = new InputField( this, SWT.PASSWORD);
		text_confirm.setLabel( S_CONFIRM_PASSWORD + ":");
		text_confirm.setEnabled(false);
		text_confirm.setLabelWidth(DEFAULT_LABEL_SIZE);
		text_confirm.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ));
		text_confirm.addVerifyListener(new VerifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(VerifyEvent event) {
				try {
					boolean similar = text_password.getText().equals( text_confirm.getText() );
					VerificationUtils.defaultVerificationAction( similar, text_confirm, S_PASSWORD_CONFIRMATION_TIP );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});


		Composite buttons = new Composite( this, SWT.BORDER|SWT.LEFT_TO_RIGHT);
		buttons.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false));
		buttons.setLayout(new GridLayout(10, true ));
		okButton = new Button( buttons, SWT.BORDER);
		okButton.setText("OK");
		okButton.setLayoutData( new GridData( SWT.RIGHT, SWT.FILL, false, true));
		okButton.setEnabled(false);
		okButton.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				String user = text_password.getText();
				setInput(user, true);
				notifyInputEdited( new EditEvent<>( this, EditTypes.SELECTED, user ));
				super.widgetSelected(e);
			}
		});
		
		Composite fill = new Composite( this, SWT.LEFT_TO_RIGHT);
		fill.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true));
	}

	public void setConfirmation(boolean confirmation) {
		text_confirm.setVisible(confirmation);
		text_confirm.setEnabled( isValidEntry());
		this.notifyInputEdited( new EditEvent<String>( container, EditTypes.CHANGED));
	}

	public InputField getTextPassword() {
		return text_password;
	}

	@Override
	protected String onGetInput(String input) {
		return input;
	}

	@Override
	protected void onSetInput( String input, boolean overwrite) {
	}

	/**
	 * Response to a changed attribute
	 * @param event
	 * @param attribute
	 */
	protected void onVerifyText( VerifyEvent event ){
		//notifyInputEdited( new EditEvent<ILoginUser>( container, EditTypes.CHANGED));
		if( StringUtils.isEmpty( this.text_password.getText())	||
				StringUtils.isEmpty( this.text_confirm.getText() ))
			return;
		try {
			if( isFilled())
				notifyInputEdited( new EditEvent<String>( container, EditTypes.FILLED));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected boolean isValidEntry( ) {
		String password = this.text_password.getText() ;
		return( !StringUtils.isEmpty( this.text_confirm.getText() ) && password.equals( this.text_confirm.getText()));
	}

	public boolean isFilled(){
		String str = text_password.getText();
		boolean filled = !StringUtils.isEmpty( str);
		if( !filled )
			return false;
		String confirmed = text_confirm.getText();
		str = text_password.getText();
		return ( filled && str.equals(confirmed));
	}

	@Override
	public boolean checkRequiredFields() {
		return false;
	}

	public boolean refresh() {
		boolean result = isFilled();
		okButton.setEnabled(result);
		return result;
	}
}