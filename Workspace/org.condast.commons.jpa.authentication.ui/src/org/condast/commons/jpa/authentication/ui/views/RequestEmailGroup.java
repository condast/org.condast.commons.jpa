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
package org.condast.commons.jpa.authentication.ui.views;

import org.condast.commons.jpa.authentication.core.IAuthenticationManager.AuthenticationResults;
import org.condast.commons.jpa.authentication.ui.AdminLanguage;
import org.condast.commons.strings.StringUtils;
import org.condast.commons.ui.controller.AbstractEntityGroup;
import org.condast.commons.ui.controller.EditEvent;
import org.condast.commons.ui.controller.EditEvent.EditTypes;
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
import org.eclipse.swt.widgets.Text;

public class RequestEmailGroup extends AbstractEntityGroup<String>{
	private static final long serialVersionUID = 1L;

	public static final String S_OMP_AUTHENTICATION = "Authentication";

	public enum Fields{
		EMAIL,
		INVALID_EMAIL;

		@Override
		public String toString() {
			return AdminLanguage.getInstance().getString( this );
		}

		public String getMessage() {
			return AdminLanguage.getInstance().getMessage( this );
		}


	}
	private Text text_email;

	private Button okButton;

	private RequestEmailGroup container;
	
	private boolean valid ;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public RequestEmailGroup( final Composite parent, int style) {
		super(parent, style);
		this.container = this;
		this.valid = false;
	}

	protected void createComposite( Composite parent, int style){
		setText( Fields.EMAIL.toString() );
		setData(RWT.CUSTOM_VARIANT, S_OMP_AUTHENTICATION);
		setLayout(new GridLayout(2, false));

		Label lblTitle = new Label(this, SWT.NONE);
		lblTitle.setLayoutData( new GridData(SWT.LEFT, SWT.FILL, false, false));
		lblTitle.setText( Fields.EMAIL.getMessage() );

		text_email = new Text(this, SWT.SINGLE | SWT.LEAD | SWT.BORDER );
		text_email.setLayoutData( new GridData(SWT.FILL, SWT.FILL, true, false));
		text_email.addVerifyListener(new VerifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void verifyText(VerifyEvent event) {
				try {
					Text text = (Text) event.widget;
					valid = IVerification.VerificationTypes.verify( VerificationTypes.EMAIL, text.getText() );
					okButton.setEnabled(valid);
					if( valid ) {
						setInput(text.getText(), valid);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		Composite buttons = new Composite( this, SWT.BORDER|SWT.LEFT_TO_RIGHT);
		buttons.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false,2,1));
		buttons.setLayout(new GridLayout(10, true ));
		okButton = new Button( buttons, SWT.BORDER);
		okButton.setText("OK");
		okButton.setLayoutData( new GridData( SWT.RIGHT, SWT.FILL, false, true));
		okButton.setEnabled(false);
		okButton.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				notifyInputEdited( new EditEvent<String>( container, EditTypes.SELECTED, getInput()));
				super.widgetSelected(e);
			}
		});
	}

	
	@Override
	public boolean checkRequiredFields() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	protected String onGetInput(String input) {
		return input;
	}


	@Override
	protected void onSetInput(String input, boolean overwrite) {
		
	}

	public void refresh() {
		okButton.setEnabled(valid);
	}
	/**
	 * Set the result message
	 * @param result
	 */
	public void setResultMessage( AuthenticationResults result, String info  ){
		switch( result ){
		case INVALID_EMAIL:
			text_email.setFocus();
			text_email.setToolTipText( Fields.INVALID_EMAIL.getMessage() );
			break;
		default:
			break;
		}
	}

	/**
	 * Returns true if the button can be enabled
	 * @return
	 */
	protected boolean enableButton(){
		return !StringUtils.isEmpty( text_email.getText() );
	}

	public Text getEmailWidget(){
		return this.text_email;
	}

	@Override
	protected void checkSubclass() {
		//Disable the check that prevents subclassing of SWT components
	}
}
