package org.condast.commons.jpa.ui.na.person;

import org.eclipse.swt.widgets.Composite;

import java.util.HashMap;
import java.util.Map;

import org.condast.commons.jpa.na.model.IVocation;
import org.condast.commons.jpa.na.model.IVocation.Nature;
import org.condast.commons.jpa.ui.na.NALanguage;
import org.condast.commons.ui.widgets.combo.AbstractComboComposite;
import org.eclipse.swt.SWT;

import org.eclipse.swt.widgets.Button;

public class VocationComposite extends AbstractComboComposite<IVocation, Nature>{
	private static final long serialVersionUID = 1L;

	//Text fields
	private enum Fields{
		VOCATION,
		UPASS;
		
		public String toSring(){
			return NALanguage.getInstance().getString( this );
		}
	}
	private Button btnUpas;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public VocationComposite(Composite parent, int style) {
		super(parent, style);
		super.setAlignment(77);
		setItems();
		//select(1);
		setLabelText( Fields.VOCATION.toSring() );		
		boolean full = (( style & SWT.FULL_SELECTION) > 0 );
		if(!full )
			return;
		btnUpas = new Button(this, SWT.CHECK | SWT.LEFT );
		btnUpas.setOrientation( SWT.LEFT_TO_RIGHT );
		btnUpas.setText( Fields.UPASS.toSring() );
	}

	protected void setItems() {
		Map<Nature, String> items = new HashMap<Nature, String>();
		for( Nature nature: Nature.values() ){
			items.put( nature, NALanguage.getInstance().getString( nature ));
		}	
		super.setItems( items );
	}
	
	@Override
	protected IVocation onGetInput(IVocation input) {
		IVocation vocation = input;
		vocation.setNature( super.getSelection() );
		vocation.setDescription( super.getText() );
		if( btnUpas != null )
			vocation.setUPas( btnUpas.getSelection() );
		return vocation;
	}

	@Override
	protected void onSetInput(IVocation input, boolean overwrite) {
		if( overwrite ){
			super.select( input.getNature());	
			super.setText( input.getDescription());
			if( btnUpas != null )
				btnUpas.setSelection( input.hasUPas());
		}
	}

	@Override
	public boolean checkRequiredFields() {
		return true;
	}
}