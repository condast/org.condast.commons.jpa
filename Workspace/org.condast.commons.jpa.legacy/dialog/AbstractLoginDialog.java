/*******************************************************************************
 * Copyright (c) 2005, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.condast.commons.authentication.ui.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public abstract class AbstractLoginDialog extends TitleAreaDialog{
	private static final long serialVersionUID = -3879165661096480143L;

	public static final String S_LOGIN = "Login";

	boolean processCallbacks = false;
	boolean isCancelled = false;
	Collection<Callback> callbacks;

	private String title;
	private Text nameText;
	private Text passwordText;
	private boolean success;

	protected AbstractLoginDialog( Shell parentShell, String title ) {
		this( parentShell, title, new ArrayList<Callback>());
	}

	protected AbstractLoginDialog( Shell parentShell, String title, Collection<Callback> callbacks ) {
		super( parentShell );
		setTitle( title );
		this.title = title;
		this.callbacks = callbacks;
	}

	protected String getName() {
		for ( Callback callback: callbacks) {
			if (callback instanceof NameCallback) {
				NameCallback ncallback = (NameCallback) callback;
				return ncallback.getName();
			}
		}
		return null;
	}

	protected String getPassword() {
		for ( Callback callback: callbacks) {
			if (callback instanceof PasswordCallback) {
				PasswordCallback ncallback = (PasswordCallback) callback;
				return ( ncallback.getPassword() == null )? null: new String( ncallback.getPassword());
			}
		}
		return null;
	}

	protected Text getNameText() {
		return nameText;
	}

	protected Text getPasswordText() {
		return passwordText;
	}

	@Override
	protected void configureShell( Shell shell ) {
		super.configureShell( shell );
		shell.setText( title );
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite dialogarea = (Composite) super.createDialogArea(parent);
		dialogarea.setLayoutData(new GridData(GridData.FILL_BOTH));
		Composite composite = new Composite(dialogarea, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));
		composite.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ));
		createCallbackHandlers(composite);
		return composite;
	}

	protected abstract boolean onHandleLogin( SelectionEvent event );

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);

		final Button okButton = getButton( IDialogConstants.OK_ID );
		//okButton.setText( "Login" );
		okButton.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected( final SelectionEvent event ) {
				try {
					processCallbacks = true;
					success = onHandleLogin(event);
				}
				catch( Exception ex ) {
					ex.printStackTrace();
				}
			}
		} );

		final Button cancel = getButton( IDialogConstants.CANCEL_ID );
		cancel.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected( final SelectionEvent event ) {
				isCancelled = true;
				processCallbacks = true;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.security.auth.callback.CallbackHandler#handle(javax.security.auth
	 * .callback.Callback[])
	 */

	protected boolean isSuccess() {
		return success;
	}

	public void setCallback( Callback[] callbacks ){
		this.callbacks.clear();
		this.callbacks.addAll( Arrays.asList( callbacks));
	}

	private void createCallbackHandlers(Composite composite) {
		for ( Callback callback: callbacks) {
			if (callback instanceof NameCallback) {
				createNameHandler(composite, (NameCallback) callback);
			} else if (callback instanceof PasswordCallback) {
				createPasswordHandler(composite,
						(PasswordCallback) callback);
			}else if (callback instanceof TextOutputCallback) {
				createTextOutputHandler(composite,
						(TextOutputCallback) callback);
			}
		}
	}

	protected abstract void onModifyText( ModifyEvent event );

	protected final void createNameHandler(Composite composite, NameCallback callback) {
		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData( new GridData( SWT.LEFT, SWT.FILL, false, false, 1, 1 ));
		label.setText(callback.getPrompt());
		nameText = new Text(composite, SWT.SINGLE | SWT.LEAD
				| SWT.BORDER);
		nameText.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false, 3, 1 ));
		nameText.setData(callback);
		nameText.addModifyListener(new ModifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void modifyText(ModifyEvent event) {
				try {
					Text nameText = (Text) event.widget;
						NameCallback callback = (NameCallback) nameText.getData();
					callback.setName( nameText.getText());
					onModifyText(event);
				}
				catch( Exception ex ) {
					ex.printStackTrace();
				}
			}
		});
	}

	protected final void createPasswordHandler(Composite composite,
			final PasswordCallback callback) {
		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData( new GridData( SWT.LEFT, SWT.FILL, false, false, 1, 1 ));
		label.setText(callback.getPrompt());
		passwordText = new Text(composite, SWT.SINGLE | SWT.LEAD
				| SWT.PASSWORD | SWT.BORDER);
		passwordText.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false, 3, 1 ));
		passwordText.addModifyListener(new ModifyListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void modifyText(ModifyEvent event) {
				try {
					callback.setPassword(passwordText.getText().toCharArray());
					onModifyText(event);
				}
				catch( Exception ex ) {
					ex.printStackTrace();
				}
			}
		});
	}

	protected final void createTextOutputHandler(Composite composite,
			TextOutputCallback callback) {
		int messageType = callback.getMessageType();
		int dialogMessageType = IMessageProvider.NONE;
		switch (messageType) {
		case TextOutputCallback.INFORMATION:
			dialogMessageType = IMessageProvider.INFORMATION;
			break;
		case TextOutputCallback.WARNING:
			dialogMessageType = IMessageProvider.WARNING;
			break;
		case TextOutputCallback.ERROR:
			dialogMessageType = IMessageProvider.ERROR;
			break;
		}
		setMessage(callback.getMessage(), dialogMessageType);
	}

	protected Collection<Callback> getCallbacks() {
		return this.callbacks;
	}

	public boolean isCancelled() {
		return isCancelled;
	}
}
