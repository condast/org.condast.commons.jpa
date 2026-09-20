package org.condast.commons.jpa.authentication.ui;

import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

public abstract class AbstractAuthenticationComposite extends Composite {
	private static final long serialVersionUID = 1L;

	private Text textName;
	private Text textPassword;

	private ILoginContext module;

	public enum Requests{
		ACTIVATE,
		REGISTER,
		LOGIN,
		LOGOUT,
		UNREGISTER;

		@Override
		public String toString() {
			String str = this.name().toLowerCase();
			return str;
		}
	}

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AbstractAuthenticationComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		Label lblName = new Label(this, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		lblName.setText("LoginName:");

		textName = new Text(this, SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setText("Password:");
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		textPassword = new Text(this, SWT.BORDER);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Link registerLink = new Link(this, SWT.NONE);
		registerLink.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				onRegister(e);
			}
		});
		registerLink.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		registerLink.setText("<a>Register</a>");
	}

	protected Text getTextName() {
		return textName;
	}

	protected Text getTextPassword() {
		return textPassword;
	}

	public ILoginContext getInput() {
		return module;
	}

	public void setInput( ILoginContext module) {
		this.module = module;
	}

	protected abstract void onRegister( SelectionEvent e );

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
