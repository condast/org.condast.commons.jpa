/*******************************************************************************
 * Copyright (c) 2016 Condast and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Condast                - EetMee
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
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
import org.condast.commons.jpa.authentication.ui.AdminLanguage;
import org.condast.commons.jpa.authentication.ui.def.IAuthenticationControl;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

@Deprecated
public class AuthenticationGroup extends Group implements IAuthenticationControl{
	private static final long serialVersionUID = 1L;

	public static final String S_OMP_AUTHENTICATION = "Authentication";
	public static final String S_CHAR_X = "X";
	public static final String S_EMPTY = "";

	public enum Fields{
		LOGIN,
		PROVIDE_IMAGE,
		NAME_PASSWORD,
		NAME,
		PASSWORD,
		REGISTER,
		INVALID_NAME,
		INVALID_PASSWORD;

		@Override
		public String toString() {
			return AdminLanguage.getInstance().getString( this );
		}

		public String getMessage() {
			return AdminLanguage.getInstance().getMessage( this );
		}


	}
	private Text text_name;
	private Text text_password;
	private Button chkButton;

	private IAuthenticationManager<ILoginUser> manager;
	private Label labelNameChk;
	private Label labelPasswordChk;
	private Label lblTitle;
	private List linkRegister;

	private Collection<IAuthenticationListener> listeners;
	private Group aGroup;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AuthenticationGroup( final Composite parent, int style) {
		super(parent, style);
		aGroup = this;
		this.listeners = new ArrayList<>();
		this.createComposite(parent, style);
	}

	protected void createComposite( Composite parent, int style){
		setText( Fields.LOGIN.toString() );
		setData(RWT.CUSTOM_VARIANT, S_OMP_AUTHENTICATION);
		setLayout(new GridLayout(4, false));

		lblTitle = new Label(this, SWT.NONE);
		GridData gd_lblTitle = new GridData(SWT.LEFT, SWT.FILL, false, false, 4, 1);//in entire row 2
		lblTitle.setLayoutData( gd_lblTitle );
		lblTitle.setText( Fields.NAME_PASSWORD.getMessage() );

		Label lblName = new Label(this, SWT.NONE);
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
		labelNameChk.setLayoutData(gd_labelNameChk);

		Label lblPassword = new Label(this, SWT.NONE);
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
		GridData tp_griddata = new GridData(SWT.LEFT, SWT.FILL, true, false, 2, 1);
		text_password.setLayoutData( tp_griddata );

		labelPasswordChk = new Label(this, SWT.NONE);
		GridData gd_labelPasswordChk = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		labelPasswordChk.setLayoutData(gd_labelPasswordChk);
		labelPasswordChk.setText(" ");

		if(( SWT.FULL_SELECTION & style ) > 0 ){
			linkRegister = new List(this, SWT.NONE);
			linkRegister.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
			linkRegister.setEnabled(false);
			linkRegister.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 2, 1));
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
			lbl.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 2, 1));
		}

		chkButton = new Button(this, SWT.RIGHT_TO_LEFT);
		chkButton.setEnabled(false);
		chkButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					manager.login( true );
				}
				catch( Exception ex ) {
					ex.printStackTrace();
				}
			}
		});

		chkButton.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));//in cell(5.3)
		//in the same row as the register-link
		chkButton.setText("OK");
		chkButton.setData(RWT.CUSTOM_VARIANT, S_OMP_AUTHENTICATION);
	}

	@Override
	public void addListener( IAuthenticationListener listener ){
		this.listeners.add( listener );
	}

	@Override
	public void removeListener( IAuthenticationListener listener ){
		this.listeners.remove( listener );
	}

	protected void notifyListeners( AuthenticationEvent event ){
		for( IAuthenticationListener listener: this.listeners ){
			listener.notifyLoginChanged(event);
		}
	}

	@Override
	public Image getImage(){
		return null;//lblImageLabel.getImage();
	}

	@Override
	public void setImage( Image image ){
		//lblImageLabel.setImage(image);
	}

	@Override
	public void setManager( IAuthenticationManager<ILoginUser> manager ){
		this.manager = manager;
	}

	/**
	 * Set the result message
	 * @param result
	 */
	@Override
	public void setResultMessage( AuthenticationResults result, String info  ){
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

	@Override
	public Text getNameWidget(){
		return this.text_name;
	}

	@Override
	public Text getPasswordWidget(){
		return this.text_password;
	}

	@Override
	protected void checkSubclass() {
		//Disable the check that prevents subclassing of SWT components
	}
}
