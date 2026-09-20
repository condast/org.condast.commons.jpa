package org.condast.commons.jpa.authentication.ui.views;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.condast.commons.strings.StringUtils;
import org.condast.commons.ui.controller.AbstractEntityComposite;
import org.condast.commons.ui.controller.EditEvent;
import org.condast.commons.ui.controller.EditEvent.EditTypes;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SecurityCodeComposite extends AbstractEntityComposite<Integer> {
	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_CODE_SIZE = 6;

	private Text[] numbers;

	private int size;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SecurityCodeComposite(Composite parent, int style) {
		this( parent, DEFAULT_CODE_SIZE, style );
	}

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SecurityCodeComposite(Composite parent, int size, int style) {
		super(parent, style);
	}


	
	@Override
	protected void createComposite(Composite parent, int style) {
		this.createComposite(parent, DEFAULT_CODE_SIZE, style);
	}

	protected void createComposite(Composite parent, int size, int style) {
		this.size = size;
		setLayout(new GridLayout( size+1, false));
		
		Label lblCodeLabel = new Label(this, SWT.NONE);
		lblCodeLabel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ));
		lblCodeLabel.setText("Enter code:");
		numbers = new Text[size];
		for( int i=0; i<size;i++ ) {
			numbers[i] = new Text(this, SWT.BORDER);
			numbers[i].setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ));
		}
		Composite composite = new Composite( this, SWT.BORDER );
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false, size+1, 1));
		Button button = new Button( composite, SWT.BORDER);
		button.setText("OK");
		button.setLayoutData( new GridData( SWT.RIGHT_TO_LEFT, SWT.CENTER, true, true ));
		button.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				if( checkRequiredFields()){
					notifyInputEdited( new EditEvent<Integer>( this, EditTypes.COMPLETE, getInput()));
				}
				super.widgetSelected(e);
			}
		});
	}

	public int getCodeSize() {
		return size;
	}

	@Override
	protected Integer onGetInput(Integer input) {
		return input;
	}

	@Override
	protected void onSetInput(Integer input, boolean overwrite) {
		super.setInput( input, overwrite  );
		if(!overwrite)
			return;
		String str = String.valueOf( input );
		if( StringUtils.isEmpty(str))
			return;
		int amount = ( str.length()< size)?str.length(): size;
		for( int i=size; i>( size-amount); i-- )
			numbers[i].setText( str.substring(i, i+1)); 
		return;
	}
	
	@Override
	public boolean checkRequiredFields() {
		int result = 0;
		int decimal = 1;
		for( int i=size; i>=0; i-- ) {
			result = decimal * Integer.parseInt( numbers[i].getText());
			decimal *=10;
		}
		return ( getInput() - result)==0;
	}
	
	

}
