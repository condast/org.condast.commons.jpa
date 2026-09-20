package org.condast.commons.authentication.ui.dialog;

import org.condast.commons.authentication.ui.core.CallbackData;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;
import org.condast.commons.verification.IVerification;
import org.condast.commons.verification.IVerification.VerificationTypes;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public abstract class AbstractLoginDialogImplemetation extends AbstractLoginDialog{
	private static final long serialVersionUID = 1L;

	private static final String S_AGREEMENT = "By checking this button you agree to our: ";
	private static final String S_HREF = "<a href=\"";
	private static final String S_AGREEMENT_1 = "\">Terms of Use</a>";
	private static final String S_AND_OUR = " and our ";
	private static final String S_AGREEMENT_2 = "\"> Privacy Policy</a>";

	public enum Fields{
		REGISTER,
		CONFIRM;

		@Override
		public String toString() {
			return StringStyler.prettyString(super.toString());
		}
	}

	private boolean registering;
	private Text confirmText;
	private Link registerLink;
	private Text emailText;
	private String email;
	private CallbackData data;
	private Button agreementButton;
	private Link linktos;
	private Label lbl_andours;
	private Link linkpriv;
	private Label confirmLabel;

	protected AbstractLoginDialogImplemetation(Shell parentShell, CallbackData data ) {
		super(parentShell,  data.getTitle());
		this.registering = false;
		this.data = data;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(1000, 700);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite comp = (Composite) super.createDialogArea(parent);
		Text nameText = getNameText();
		nameText.addModifyListener( new ModifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void modifyText(ModifyEvent event) {
				boolean ok = isValidEntry();
				agreementButton.setEnabled( ok);
			}
		});

		Text passwordText = getPasswordText();
		passwordText.addModifyListener( new ModifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void modifyText(ModifyEvent event) {
				boolean ok = isValidEntry();
				agreementButton.setEnabled( ok);
				onModifyText(event);
			}
		});

		confirmLabel = new Label( comp, SWT.NONE );
		confirmLabel.setText( Fields.CONFIRM.toString() + ": ");
		confirmLabel.setLayoutData( new GridData( SWT.LEFT, SWT.FILL, false, false ));
		confirmLabel.setVisible(registering);
		confirmText = new Text( comp, SWT.BORDER );
		confirmText.setVisible(registering);
		confirmText.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false, 3, 1 ));
		confirmText.addModifyListener( new ModifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void modifyText(ModifyEvent event) {
				boolean ok = isValidEntry();
				agreementButton.setEnabled( ok);
				onModifyText(event);
			}
		});

		registerLink = new Link(comp, SWT.NONE);
		registerLink.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					registering = true;
					registerLink.setText("Email:");
					emailText.setVisible(registering);
					confirmLabel.setVisible(registering);
					confirmText.setVisible(registering);
					agreementButton.setVisible(registering);
					linktos.setVisible(registering);
					linkpriv.setVisible(registering);
					lbl_andours.setVisible(registering);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		registerLink.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		registerLink.setText("<a>" + Fields.REGISTER.toString() + "</a>");

		emailText= new Text( comp, SWT.BORDER );
		emailText.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false,3 , 1 ));
		emailText.setVisible(false);
		emailText.addModifyListener( new ModifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void modifyText(ModifyEvent event) {
				boolean ok = isValidEntry();
				agreementButton.setEnabled( ok);
				email = emailText.getText();
				onModifyText(event);
			}
		});

		Composite agreementComposite = new Composite( comp, SWT.NONE);
		agreementComposite.setLayout( new GridLayout(2,false ));
		agreementComposite.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 4, 1 ));
		agreementButton = new Button( agreementComposite, SWT.CHECK );
		agreementButton.setEnabled(false);
		agreementButton.setText( S_AGREEMENT );
		agreementButton.setLayoutData( new GridData( SWT.LEFT, SWT.FILL, false, false, 2, 1 ));
		agreementButton.setVisible(false);
		agreementButton.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean ok = isValidEntry();
				agreementButton.setEnabled( ok);
				getButton( IDialogConstants.OK_ID).setEnabled(ok &&  agreementButton.getSelection());
				super.widgetSelected(e);
			}
		});

		linktos = new Link( agreementComposite, SWT.NONE );
		linktos.setVisible(false);
		linktos.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		linktos.setText( S_HREF + data.getTermsOfUsePath() + S_AGREEMENT_1);
		GridData gd_linktos = new GridData( SWT.FILL, SWT.FILL, true, false );
		gd_linktos.verticalAlignment = SWT.FILL;
		gd_linktos.horizontalAlignment = SWT.FILL;
		linktos.setLayoutData(gd_linktos);

		lbl_andours=  new Label( agreementComposite, SWT.NONE );
		lbl_andours.setVisible(false);
		lbl_andours.setText( S_AND_OUR);
		lbl_andours.setLayoutData(new GridData( SWT.FILL, SWT.FILL ,true, false));

		linkpriv = new Link( agreementComposite, SWT.NONE );
		linkpriv.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		linkpriv.setVisible(false);
		linkpriv.setText( S_HREF + data.getPrivacyPath() + S_AGREEMENT_2);
		linkpriv.setLayoutData(new GridData( SWT.LEFT, SWT.FILL));
		return comp;
	}

	protected boolean isRegistering() {
		return registering;
	}

	protected String getEmail() {
		return email;
	}

	protected Text getConfirmText() {
		return confirmText;
	}

	protected Text getEmailText() {
		return emailText;
	}

	protected String createAgremeentMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(S_AGREEMENT);
		buffer.append(data.getTermsOfUsePath());
		buffer.append(S_AGREEMENT_1);
		buffer.append(data.getPrivacyPath());
		buffer.append(S_AGREEMENT_2);
		return buffer.toString();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		getButton( IDialogConstants.OK_ID).setEnabled(false);
	}

	protected boolean isValidEntry() {
		if( StringUtils.isEmpty( getName() ) || StringUtils.isEmpty( getPassword() ))
			return false;
		if(!registering )
			return true;
		if( StringUtils.isEmpty( confirmText.getText() ) || !getPassword().equals( confirmText.getText()))
			return false;
		boolean valid = IVerification.VerificationTypes.verify( VerificationTypes.EMAIL, emailText.getText() );
		return registering?valid: true;
	}

	@Override
	protected void onModifyText(ModifyEvent event) {
		// TODO Auto-generated method stub

	}
}
