package org.condast.commons.jpa.authentication.ui.views;

import org.condast.commons.data.util.LoginData;
import org.condast.commons.strings.StringUtils;
import org.condast.commons.ui.controller.AbstractEntityComposite;
import org.condast.commons.ui.controller.EditEvent;
import org.condast.commons.ui.controller.EditEvent.EditTypes;
import org.condast.commons.ui.swt.InputField;
import org.condast.commons.ui.verification.AbstractWidgetVerificationDelegate;
import org.condast.commons.ui.verification.VerificationUtils;
import org.condast.commons.verification.IVerification;
import org.condast.commons.verification.IVerification.VerificationTypes;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

public class AuthenticationComposite extends AbstractEntityComposite<LoginData>
{
	private static final long serialVersionUID = 4936423612139257363L;

	private static final String S_NAME = "Name";
	private static final String S_PASSWORD = "Password";
	private static final String S_CONFIRM_PASSWORD = "Confirm Password";
	private static final String S_EMAIL = "Email";
	private static final String S_CONFIRM_EMAIL = "Confirm email";

	private static final String S_NICKNAME_INFORMATION_TIP = "Provide a nickname for this link.";
	private static final String S_PASSWORD_INFORMATION_TIP = "Provide a password, and confirm this in the field below.";
	private static final String S_PASSWORD_CONFIRMATION_TIP = "The password and confirmation should be the same.";

	private static final String S_AGREEMENT = "By checking this button you agree to our: ";
	private static final String S_HREF = "<a href=\"";
	private static final String S_AGREEMENT_1 = "\">Terms of Use</a>";
	private static final String S_AND_OUR = " and our ";
	private static final String S_AGREEMENT_2 = "\"> Privacy Policy</a>";

	private static final int DEFAULT_LABEL_SIZE = 115;

	private InputField text_nickname;
	private InputField text_password;
	private InputField text_confirm;
	private InputField text_email;
	private InputField text_confirm_email;

	private Button agreementButton;
	private Link linktos;
	private Label lbl_andours;
	private Link linkpriv;

	private Button okButton;

	private AuthenticationComposite container;

	private boolean register;

	private String licensePath;
	private String privacyPath;

	public AuthenticationComposite(Composite parent, int style) {
		super(parent, style);
		this.container = this;
		this.register = false;
	}

	@Override
	protected void createComposite(Composite parent, int style) {
		setLayout( new GridLayout(1, false ));

		text_nickname = new InputField(this, SWT.NONE);
		text_nickname.setLabel( S_NAME + ": " );
		text_nickname.setLabelWidth(DEFAULT_LABEL_SIZE);
		text_nickname.setToolTipText( S_NICKNAME_INFORMATION_TIP );
		text_nickname.addVerifyListener(new VerifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(VerifyEvent event) {
				try {
					if( !register)
						okButton.setEnabled(isFilled());
					else
						agreementButton.setEnabled(isFilled());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		AbstractWidgetVerificationDelegate.setVerificationType( text_nickname, VerificationTypes.NAME );
		text_nickname.setLayoutData(new GridData( SWT.FILL, SWT.FILL, true, false));

		text_password = new InputField(this, SWT.PASSWORD);
		text_password.setLabel( S_PASSWORD + ": " );
		text_password.setLabelWidth(DEFAULT_LABEL_SIZE);
		text_password.setToolTipText(S_PASSWORD_INFORMATION_TIP);
		text_password.addVerifyListener(new VerifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(VerifyEvent event) {
				try {
					if( !register)
						okButton.setEnabled(isFilled());
					else
						agreementButton.setEnabled(isFilled());
					text_confirm.setEnabled( !StringUtils.isEmpty( text_password.getText() ));
					boolean similar = text_password.getText().equals( text_confirm.getText() );
					VerificationUtils.defaultVerificationAction( similar, text_confirm, S_PASSWORD_CONFIRMATION_TIP );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		text_password.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false));

		Composite registerComposite = new Composite( this, SWT.NONE);
		registerComposite.setLayout( new GridLayout(1,false ));
		registerComposite.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );

		text_confirm = new InputField( registerComposite, SWT.PASSWORD);
		text_confirm.setLabel( S_CONFIRM_PASSWORD + ":");
		text_confirm.setEnabled(false);
		text_confirm.setLabelWidth(DEFAULT_LABEL_SIZE);
		text_confirm.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ));
		text_confirm.addVerifyListener(new VerifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(VerifyEvent event) {
				try {
					agreementButton.setEnabled(isFilled());
					boolean similar = text_password.getText().equals( text_confirm.getText() );
					VerificationUtils.defaultVerificationAction( similar, text_confirm, S_PASSWORD_CONFIRMATION_TIP );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		text_confirm.setVisible(false);

		text_email = new InputField(registerComposite, SWT.NONE);
		text_email.setLabel( S_EMAIL + ": " );
		text_email.setLabelWidth(DEFAULT_LABEL_SIZE);
		text_email.addVerifyListener(new VerifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(VerifyEvent event) {
				try {
					Text text = (Text) event.widget;
					agreementButton.setEnabled(isFilled());
					boolean enabled = !StringUtils.isEmpty( text.getText() );
					text_confirm_email.setEnabled( enabled);
					text_confirm_email.setEditable( enabled);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		text_email.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ));
		text_email.setVisible(this.register);

		text_confirm_email = new InputField(registerComposite, SWT.NONE);
		text_confirm_email.setLabel( S_CONFIRM_EMAIL + ": " );
		text_confirm_email.setLabelWidth(DEFAULT_LABEL_SIZE);
		text_confirm_email.setEditable(false);
		text_confirm_email.addVerifyListener(new VerifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(VerifyEvent event) {
				try {
					boolean result = isFilled();
					agreementButton.setEnabled(result);
					if( result )
						return;
					String text = event.text;
					result = text.equals(text_email.getText());
					agreementButton.setEnabled(result);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		text_confirm_email.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ));
		text_confirm_email.setVisible( this.register);

		Composite agreementComposite = new Composite( this, SWT.NONE);
		agreementComposite.setLayout( new GridLayout(4,false ));
		agreementComposite.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ));
		agreementButton = new Button( agreementComposite, SWT.CHECK );
		agreementButton.setEnabled(false);
		agreementButton.setText( S_AGREEMENT );
		agreementButton.setLayoutData( new GridData( SWT.LEFT, SWT.FILL, false, false ));
		agreementButton.setVisible(false);
		agreementButton.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean ok = isValidEntry( true);
				agreementButton.setEnabled( ok);
				okButton.setEnabled(ok &&  agreementButton.getSelection());
				super.widgetSelected(e);
			}
		});

		linktos = new Link( agreementComposite, SWT.NONE );
		linktos.setVisible(false);
		linktos.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		linktos.setText( S_HREF + licensePath + S_AGREEMENT_1);
		linktos.setLayoutData(new GridData( SWT.FILL, SWT.FILL, false, false ));

		lbl_andours=  new Label( agreementComposite, SWT.NONE );
		lbl_andours.setVisible(false);
		lbl_andours.setText( S_AND_OUR);
		lbl_andours.setLayoutData(new GridData( SWT.FILL, SWT.FILL ,false, false));

		linkpriv = new Link( agreementComposite, SWT.NONE );
		linkpriv.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		linkpriv.setVisible(false);
		linkpriv.setText( S_HREF + privacyPath + S_AGREEMENT_2);
		linkpriv.setLayoutData(new GridData( SWT.FILL, SWT.FILL, false, false));

		Composite buttons = new Composite( this, SWT.BORDER|SWT.RIGHT_TO_LEFT);
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
				LoginData user = new LoginData( text_nickname.getText(), text_password.getText(), text_email.getText(), register );
				setInput(user, true);
				notifyInputEdited( new EditEvent<>( this, EditTypes.COMPLETE, user ));
				super.widgetSelected(e);
			}
		});
		Link registerLink = new Link( buttons, SWT.NONE);
		registerLink.setData( RWT.CUSTOM_VARIANT, "Register" );
		registerLink.setText("<a>Register</a>");
		registerLink.setLayoutData( new GridData( SWT.RIGHT, SWT.FILL, false, true));
		registerLink.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					register = true;
					okButton.setEnabled(false);
					setConfirmation(register);
					agreementButton.setEnabled(false);
					agreementButton.setVisible(register);
					linktos.setVisible(register);
					linkpriv.setVisible(register);
					lbl_andours.setVisible(register);
					super.widgetSelected(e);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	public void setLicensePath(String licensePath) {
		this.licensePath = licensePath;
		linktos.setText( S_HREF + licensePath + S_AGREEMENT_1);
	}

	public void setPrivacyPath(String privacyPath) {
		this.privacyPath = privacyPath;
		linkpriv.setText( S_HREF + privacyPath + S_AGREEMENT_2);
	}

	public void setConfirmation(boolean confirmation) {
		text_confirm.setVisible(confirmation);
		text_confirm.setEnabled( isValidEntry(false));
		text_email.setVisible(confirmation);
		text_confirm_email.setVisible(confirmation);
		this.notifyInputEdited( new EditEvent<LoginData>( container, EditTypes.CHANGED));
	}

	public InputField getTextNickname() {
		return text_nickname;
	}

	public InputField getTextPassword() {
		return text_password;
	}

	@Override
	protected LoginData onGetInput(LoginData input) {
		return input;
	}

	@Override
	protected void onSetInput( LoginData input, boolean overwrite) {
	}

	/**
	 * Response to a changed attribute
	 * @param event
	 * @param attribute
	 */
	protected void onVerifyText( VerifyEvent event ){
		//notifyInputEdited( new EditEvent<ILoginUser>( container, EditTypes.CHANGED));
		if( StringUtils.isEmpty( this.text_nickname.getText() ) ||
				StringUtils.isEmpty( this.text_nickname.getText())	||
				StringUtils.isEmpty( this.text_confirm.getText() ) ||
				StringUtils.isEmpty( this.text_email.getText() ) ||
				StringUtils.isEmpty( this.text_confirm_email.getText() ))
			return;
		try {
			if( isFilled())
				notifyInputEdited( new EditEvent<LoginData>( container, EditTypes.FILLED));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected boolean isValidEntry( boolean register ) {
		String password = this.text_password.getText() ;
		if( StringUtils.isEmpty( this.text_nickname.getText() ) || StringUtils.isEmpty( password ))
			return false;
		if(!register )
			return true;
		if( StringUtils.isEmpty( this.text_confirm.getText() ) || !password.equals( this.text_confirm.getText()))
			return false;
		boolean valid = IVerification.VerificationTypes.verify( VerificationTypes.EMAIL, this.text_email.getText()  );
		return register? valid: true;
	}

	public boolean isFilled(){
		String str =  text_nickname.getText();
		boolean filled = !StringUtils.isEmpty(str);
		if( !filled )
			return false;
		str = text_password.getText();
		filled = !StringUtils.isEmpty( str);
		if( !filled )
			return false;
		if( !register)
			return filled;

		str = text_email.getText();
		filled = !StringUtils.isEmpty(str);
		String confirmed = text_confirm_email.getText();
		filled = !StringUtils.isEmpty( confirmed );
		if( !filled || !confirmed.equals(str))
			return false;
		confirmed = text_confirm.getText();
		str = text_password.getText();
		return ( filled && str.equals(confirmed));
	}

	@Override
	public boolean checkRequiredFields() {
		return false;
	}

	public boolean refresh() {
		boolean result = isFilled();
		agreementButton.setEnabled(result);
		okButton.setEnabled(result);
		return result;
	}

	@Override
	public void dispose() {
		try {
			LoginData input = super.getInput();
			if( input == null )
				return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.dispose();
	}
}