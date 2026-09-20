package org.condast.commons.jpa.ui.na.person;

import java.util.HashMap;
import java.util.Map;

import org.condast.commons.jpa.na.model.IVocation;
import org.condast.commons.jpa.na.model.IVocation.Nature;
import org.condast.commons.jpa.ui.na.NALanguage;
import org.condast.commons.ui.widgets.combo.AbstractComboComposite;
import org.eclipse.swt.widgets.Composite;

public class SimpleVocationComposite extends AbstractComboComposite<IVocation, IVocation.Nature> {

	private static final long serialVersionUID = 1L;

	//Text fields
	private enum Fields{
		VOCATION,
		UPASS,
		
		UNKNOWN,
		FUNCTION,
		STUDENT,
		RETIRED;
		
		public String toString(){
			return NALanguage.getInstance().getString( this );
		}
	}

	public SimpleVocationComposite(Composite parent, int style ) {
		super(parent, style);
		super.setAlignment(77);
		super.setCenter(170);
		setItems();
		//select( Nature.FUNCTION );
		setLabelText( Fields.VOCATION.toString() );		
	}

	protected void setItems() {
		Map<Nature, String> items = new HashMap<Nature, String>();
		for( Nature nature: Nature.values() ){
			if( Nature.UNKNOWN.equals( nature ))
				continue;
			items.put( nature, NALanguage.getInstance().getString( nature ));
		}	
		super.setItems( items );
	}
	
	/**
	 * Get the nature from the underlying selection index
	 * @return
	 */
	protected Nature getNature(){
		return super.getSelection();
	}
	
	@Override
	protected IVocation onGetInput(IVocation input) {
		IVocation vocation = input;
		if( vocation == null )
			return input;
		vocation.setNature( getNature() );
		vocation.setDescription( super.getText() );
		return vocation;
	}

	@Override
	protected void onSetInput(IVocation input, boolean overwrite) {
		if( overwrite ){
			select( input.getNature());	
			super.setText( input.getDescription());
		}
	}

	@Override
	public boolean checkRequiredFields() {
		return true;
	}
	
}
