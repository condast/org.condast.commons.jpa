package org.condast.commons.jpa.authentication.ui.group;

import java.util.ArrayList;
import java.util.Collection;

import org.condast.commons.Utils;
import org.condast.commons.data.user.ILoginUser;
import org.condast.commons.jpa.authentication.core.AuthenticationEvent;
import org.condast.commons.jpa.authentication.core.IAuthenticationListener;
import org.condast.commons.jpa.authentication.core.IAuthenticationListener.AuthenticationEvents;
import org.condast.commons.jpa.authentication.core.IAuthenticationManager;
import org.condast.commons.jpa.authentication.core.IAuthenticationManager.AuthenticationResults;
import org.condast.commons.jpa.authentication.user.AnonymousUser;
import org.condast.commons.jpa.authentication.ui.def.IAuthenticationControl;
import org.condast.commons.jpa.authentication.ui.group.AuthenticationGroup.Fields;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

@Deprecated
public abstract class AbstractAuthenticationGroup extends Composite implements IAuthenticationControl{
	private static final long serialVersionUID = 7949064968455737268L;

	public static final String S_CHAR_X = "X";
	public static final String S_EMPTY = "";

	private Collection<IAuthenticationListener> listeners;
	private Composite aGroup;
	private IAuthenticationManager<ILoginUser> manager;

	protected Text text_name;
	protected Text text_password;
	private Button chkButton;

	private Label lblImageLabel;

	protected Label lblTitle;

	protected Label labelNameChk;

	protected Label labelPasswordChk;
	private List linkRegister;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AbstractAuthenticationGroup( final Composite parent, int style) {
		super(parent, style);
		aGroup = this;
		this.listeners = new ArrayList<>();
		this.createComposite(parent, style);
	}

	protected abstract void createComposite(Composite parent,int style);

	/* (non-Javadoc)
	 * @see org.condast.commons.authentication.composite.IAuthenticationControl#getImage()
	 */
	@Override
	public Image getImage(){
		return lblImageLabel.getImage();
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.authentication.composite.IAuthenticationControl#setImage(org.eclipse.swt.graphics.Image)
	 */
	@Override
	public void setImage( Image image ){
		lblImageLabel.setImage(image);
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.authentication.composite.IAuthenticationControl#addListener(org.condast.commons.authentication.core.IAuthenticationListener)
	 */
	@Override
	public void addListener( IAuthenticationListener listener ){
		this.listeners.add( listener );
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.authentication.composite.IAuthenticationControl#removeListener(org.condast.commons.authentication.core.IAuthenticationListener)
	 */
	@Override
	public void removeListener( IAuthenticationListener listener ){
		this.listeners.remove( listener );
	}

	protected void notifyListeners( AuthenticationEvent event ){
		for( IAuthenticationListener listener: this.listeners ){
			listener.notifyLoginChanged(event);
		}
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.authentication.composite.IAuthenticationControl#setManager(org.condast.commons.authentication.core.IAuthenticationManager)
	 */
	@Override
	public void setManager( IAuthenticationManager<ILoginUser> manager ){
		this.manager = manager;
	}

	@Override
	public void setResultMessage(AuthenticationResults result, String info) {
		switch( result ){
		case INVALID_EMAIL:
			this.lblTitle.setText( Fields.INVALID_NAME.getMessage());
			this.labelNameChk.setText(S_CHAR_X);
			this.labelPasswordChk.setText(S_EMPTY);
			text_name.setFocus();
			text_name.setText(S_EMPTY);
			text_name.setToolTipText( Fields.INVALID_NAME.getMessage() );
			break;
		case INVALID_PASSWORD:
			this.lblTitle.setText( Fields.INVALID_PASSWORD.getMessage());
			this.labelNameChk.setText(S_EMPTY);
			this.labelPasswordChk.setText(S_CHAR_X);
			text_password.setFocus();
			text_password.setText(S_EMPTY);
			text_password.setToolTipText( Fields.INVALID_PASSWORD.getMessage() );
			break;
		default:
			this.lblTitle.setText( info );
			this.labelNameChk.setText(S_EMPTY);
			this.labelPasswordChk.setText(S_EMPTY);
			break;
		}
	}
	/**
	 * Returns true if the button can be enabled
	 * @return
	 */
	protected boolean enableButton(){
		return !Utils.assertNull( text_name.getText() ) && !Utils.assertNull( text_password.getText() );
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.authentication.composite.IAuthenticationControl#getPasswordWidget()
	 */
	@Override
	public Text getPasswordWidget() {
		return this.text_password;
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.authentication.composite.IAuthenticationControl#getNameWidget()
	 */
	@Override
	public Text getNameWidget() {
		return this.text_name;
	}

	public void createDefaultAuthenticationGroup(Composite parent, int style){
		setLayout(new GridLayout(4, false));

		lblImageLabel = new Label(this, SWT.NONE);
		lblImageLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 4, 1));//in entire row 1
		lblImageLabel.setText( Fields.PROVIDE_IMAGE.toString());

		lblTitle = new Label(this, SWT.NONE);
		GridData gd_lblTitle = new GridData(SWT.LEFT, SWT.FILL, false, false, 4, 1);
		gd_lblTitle.widthHint = 263;
		lblTitle.setLayoutData( gd_lblTitle );
		lblTitle.setText( Fields.NAME_PASSWORD.getMessage() );

		Label lblName = new Label(this, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));//in cell(3,1)
		lblName.setText( Fields.NAME.toString() );

		text_name = new Text(this, SWT.SINGLE | SWT.LEAD | SWT.BORDER );
		text_name.addVerifyListener(new VerifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(VerifyEvent arg0) {
				getDisplay().asyncExec( new Runnable(){

					@Override
					public void run() {
						chkButton.setEnabled( enableButton() );
						if( linkRegister != null )
							linkRegister.setEnabled( enableButton());
					}
				});
			}
		});
		text_name.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));//in cell(3,2) and cell(3,3)
		text_name.setFocus(); // So one can start typing immediately in this textfield.

		labelNameChk = new Label(this, SWT.NONE);
		GridData gd_labelNameChk = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);//in cell(3,4)
		gd_labelNameChk.widthHint = 10;// Minimal length to show S_X_CHAR.
		labelNameChk.setLayoutData(gd_labelNameChk);

		Label lblPassword = new Label(this, SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));//in cell(4,1)
		lblPassword.setText( Fields.PASSWORD.toString() );

		text_password = new Text(this, SWT.SINGLE
				| SWT.LEAD
				| SWT.PASSWORD
				| SWT.BORDER );
		text_password.addVerifyListener(new VerifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(VerifyEvent arg0) {
				getDisplay().asyncExec( new Runnable(){

					@Override
					public void run() {
						chkButton.setEnabled( enableButton() );
						if( linkRegister != null )
							linkRegister.setEnabled( enableButton());
					}
				});
			}
		});
		GridData tp_griddata = new GridData(SWT.LEFT, SWT.FILL, true, false, 2, 1);//in cell(4,2) and cell(4,3)
		tp_griddata.widthHint = 160;// Minimal length to show all the dots for the long password that is set in this program.
		text_password.setLayoutData( tp_griddata );

		labelPasswordChk = new Label(this, SWT.NONE);
		GridData gd_labelPasswordChk = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);//in cell(4,4)
		gd_labelPasswordChk.widthHint = 10;//Minimal length to show S_X_CHAR.
		labelPasswordChk.setLayoutData(gd_labelPasswordChk);
		labelPasswordChk.setText(" ");
	}

	protected void createButtons( int style ) {
		if(( SWT.FULL_SELECTION & style ) > 0 ){
			linkRegister = new List(this, SWT.NONE);
			linkRegister.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
			linkRegister.setEnabled(false);
			linkRegister.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 2, 1));//in cell(5,1) and cell(5,2)
			linkRegister.add( Fields.REGISTER.toString() + "<a href='http://eclipse.org/rap' target='_rwt'");
			linkRegister.addListener( SWT.Selection, new Listener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void handleEvent( Event e) {
					ILoginUser user = new AnonymousUser(text_name.getText(), text_password.getText() );
					notifyListeners( new AuthenticationEvent( aGroup, AuthenticationEvents.REGISTER, user ));
				}
			});
		}else{
			Label lbl = new Label(this, SWT.NONE );
			lbl.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 2, 1));//in cell(5,1) and cell(5,2)
		}

		chkButton = new Button(this, SWT.RIGHT_TO_LEFT);
		chkButton.setEnabled(false);
		chkButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					manager.login( true );
				}
				catch( Exception ex ){
					ex.printStackTrace();
				}
			}
		});

		chkButton.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));//in cell(5.3)
		chkButton.setText("OK");
	}
}