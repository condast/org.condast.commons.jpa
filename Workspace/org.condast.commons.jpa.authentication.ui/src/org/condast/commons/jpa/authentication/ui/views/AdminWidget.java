package org.condast.commons.jpa.authentication.ui.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.condast.commons.data.user.IAdmin;
import org.condast.commons.data.user.IAdmin.Roles;
import org.condast.commons.data.util.LoginData;
import org.condast.commons.jpa.authentication.ui.AdminLanguage;
import org.condast.commons.strings.StringUtils;
import org.condast.commons.ui.controller.AbstractEntityComposite;
import org.condast.commons.ui.controller.EditEvent;
import org.condast.commons.ui.controller.EditEvent.EditTypes;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;

public class AdminWidget extends AbstractEntityComposite<LoginData> {
	private static final long serialVersionUID = 1L;
	
	private Text text;
	private Combo combo;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AdminWidget(Composite parent, int style) {
		super(parent, style );
	}
	
	protected void createComposite( Composite parent, int style ){	
		setLayout(new GridLayout(3, false));

		Label roleLabel = new Label(this, SWT.None );
		roleLabel.setText( AdminLanguage.getInstance().getString("ROLE"));
		roleLabel.setLayoutData(new GridData( SWT.FILL, SWT.FILL, false, false));
		combo = new Combo(this, SWT.NONE);
		combo.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					LoginData client = getInput();
					client.setRole( Roles.values()[ combo.getSelectionIndex()]);
						notifyInputEdited( new EditEvent<>( this, EditTypes.CHANGED, getInput()));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				super.widgetSelected(e);
			}		
		});
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		combo.setItems(Roles.getItems());
	}
	
	public String[] getItems(){
		return this.combo.getItems();
	}
		
	public int getSelectionIndex(){
		return this.combo.getSelectionIndex();
	}
	
	public void select( int index ){
		this.combo.select( index );
	}

	
	@Override
	public boolean checkRequiredFields() {
		return !StringUtils.isEmpty( text.getText());
	}

	@Override
	protected LoginData onGetInput(LoginData client) {
		if( client == null )
			client = new LoginData();
		IAdmin.Roles role = IAdmin.Roles.values()[ combo.getSelectionIndex() ];
		client.setRole(role);
		return client;
	}

	@Override
	protected void onSetInput(LoginData input, boolean overwrite) {
		if( input == null )
			return;
		if( overwrite ){
			combo.select( input.getRole().ordinal());
		}
	}
	
	@Override
	public void dispose() {
		combo.removeSelectionListener( super.getListener() );
		text.removeSelectionListener(  super.getListener());
		super.dispose();
	}	
}