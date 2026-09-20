package org.condast.commons.authentication.ui.callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;

import org.condast.commons.authentication.ui.core.CallbackData;
import org.condast.commons.authentication.ui.dialog.AbstractLoginDialog;
import org.condast.commons.jpa.authentication.def.IAuthenticationEvents.Requests;
import org.condast.commons.messaging.http.AbstractHttpRequest;
import org.condast.commons.messaging.http.IHttpClientListener;
import org.condast.commons.messaging.http.IHttpRequest;
import org.condast.commons.messaging.http.ResponseEvent;
import org.condast.commons.number.NumberUtils;
import org.condast.commons.strings.StringUtils;
import org.condast.commons.verification.IVerification;
import org.condast.commons.verification.IVerification.VerificationTypes;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Handles the callbacks
 */
public abstract class AbstractCallbackHandler implements CallbackHandler{

	private static final String S_AGREEMENT = "By checking this button you agree to our <a href=\"";
	private static final String S_AGREEMENT_1 = "\">Terms of Use</a> and our <a href=\"";
	private static final String S_AGREEMENT_2 = "\"> Privacy Policy</a>";
	private enum Parameters{
		ID,
		NAME,
		PASSWORD,
		EMAIL,
		TOKEN;

		@Override
		public String toString() {
			return this.name().toLowerCase();
		}
	}

	private Display display;
	private LoginDialog dialog;

	private CallbackData data;

	private WebClient client;

	private IHttpClientListener<Requests> listener = new IHttpClientListener<Requests>() {

		@Override
		public void notifyResponse( final ResponseEvent<Requests> event) {
			if( event.getResponseCode() == IHttpRequest.HttpStatus.OK.getStatus()) {
				Display.getCurrent().asyncExec( new Runnable() {

					@Override
					public void run() {
						if( !NumberUtils.isStringNumeric( event.getResponse() ))
							return;
						///UISession session = RWT.getUISession();
						//Object obj = session.getAttribute("handle");
						dialog.close();
					}
				});
			}
		}
	};

	protected AbstractCallbackHandler( CallbackData data ) {
		this.data = data;
		this.client = new WebClient( data.getContext());
		this.client.addListener(listener);
	}

	protected LoginDialog getDialog() {
		return dialog;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.security.auth.callback.CallbackHandler#handle(javax.security.auth
	 * .callback.Callback[])
	 */
	@Override
	public void handle( final Callback[] callbacks ) throws IOException {
		this.display = Display.getDefault();
		display.asyncExec( new Runnable() {

			@Override
			public void run() {
				dialog = new LoginDialog( display.getActiveShell(), data );
				dialog.setBlockOnOpen(true);
				dialog.setCallback(callbacks);
				if( dialog.open() == Window.OK )
					return;
			}
		});
	}

	protected class LoginDialog extends AbstractLoginDialog{
		private static final long serialVersionUID = 1L;

		private boolean registering;
		private Text confirmText;
		private Link registerLink;
		private Text emailText;
		private String email;
		private CallbackData data;
		private Button agreementButton;
		private Label confirmLabel;

		protected LoginDialog(Shell parentShell, CallbackData data ) {
			super(parentShell,  data.getTitle());
			this.registering = false;
			this.data = data;
		}

		@Override
		protected Point getInitialSize() {
			return new Point(600, 700);
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
					getButton( IDialogConstants.OK_ID).setEnabled(ok &&  agreementButton.getSelection());
				}
			});

			Text passwordText = getPasswordText();
			passwordText.addModifyListener( new ModifyListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void modifyText(ModifyEvent event) {
					boolean ok = isValidEntry();
					agreementButton.setEnabled( ok);
					getButton( IDialogConstants.OK_ID).setEnabled(ok &&  agreementButton.getSelection());
					onModifyText(event);
				}
			});

			confirmLabel = new Label( comp, SWT.NONE );
			confirmLabel.setText("Confirm: ");
			confirmLabel.setLayoutData( new GridData( SWT.FILL, SWT.RIGHT, false, false ));
			confirmLabel.setVisible(registering);
			confirmText = new Text( comp, SWT.BORDER );
			confirmText.setVisible(registering);
			confirmText.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ));
			confirmText.addModifyListener( new ModifyListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void modifyText(ModifyEvent event) {
					boolean ok = isValidEntry();
					agreementButton.setEnabled( ok);
					getButton( IDialogConstants.OK_ID).setEnabled(ok &&  agreementButton.getSelection());
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
						getButton( IDialogConstants.OK_ID).setEnabled(false);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
			registerLink.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			registerLink.setText("<a>Register</a>");

			emailText= new Text( comp, SWT.BORDER );
			emailText.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ));
			emailText.setVisible(false);
			emailText.addModifyListener( new ModifyListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void modifyText(ModifyEvent event) {
					try {
						boolean ok = isValidEntry();
						agreementButton.setEnabled( ok);
						email = emailText.getText();
						getButton( IDialogConstants.OK_ID).setEnabled(false);
						onModifyText(event);
					}
					catch( Exception ex ) {
						ex.printStackTrace();
					}
				}
			});

			agreementButton = new Button( comp, SWT.CHECK );
			agreementButton.setEnabled(false);
			agreementButton.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
			agreementButton.setText( createAgremeentMessage() );
			agreementButton.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false, 2, 1 ));
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
			return comp;
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
		protected boolean onHandleLogin(SelectionEvent event) {
			try {
				Requests request = registering?Requests.REGISTER: Requests.LOGIN;
				Map<String, String> parameters = new HashMap<>();
				parameters.put( Parameters.NAME.toString(), getName());
				parameters.put( Parameters.PASSWORD.toString(), getPassword());
				if( registering )
					parameters.put( Parameters.EMAIL.toString(), email);
				client.sendGet( request, parameters);
				return true;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return false;
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

	private class WebClient extends AbstractHttpRequest<Requests>{


		public WebClient(String path) {
			super(path);
		}

		@Override
		protected void sendGet(Requests request, Map<String, String> parameters) throws IOException {
			super.sendGet(request, parameters);
		}

		@Override
		protected String onHandleResponse(ResponseEvent<Requests> event) throws IOException {
			try{
				notifyListeners( event );
				return Responses.OK.name();
			}
			catch( Exception ex ){
				ex.printStackTrace();
			}
			return Responses.BAD.name();
		}
	}
}