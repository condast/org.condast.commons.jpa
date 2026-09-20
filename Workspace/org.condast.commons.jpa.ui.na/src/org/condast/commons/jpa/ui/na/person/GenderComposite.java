package org.condast.commons.jpa.ui.na.person;

import java.util.HashMap;
import java.util.Map;

import org.condast.commons.jpa.na.model.Gender;
import org.condast.commons.jpa.ui.na.NALanguage;
import org.condast.commons.ui.widgets.combo.AbstractComboComposite;
import org.eclipse.swt.widgets.Composite;

public class GenderComposite extends AbstractComboComposite<Gender, Gender>{
	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_LABEL_WIDTH_HINT = 100;

	//Text fields
	private enum Fields{
		GENDER;
		
		public String toSring(){
			return NALanguage.getInstance().getString( this );
		}
	}
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public GenderComposite(Composite parent, int style) {
		super(parent, style, true );
		super.setAlignment(DEFAULT_LABEL_WIDTH_HINT);
		setLabelText( Fields.GENDER.toSring() );
		setItems();
		setGender( Gender.FEMALE );
	}

	protected void setItems() {
		Map<Gender, String> items = new HashMap<Gender, String>();
		for( Gender gender: Gender.values() ){
			switch( gender ){
			case MALE:
			case FEMALE:
				items.put( gender, NALanguage.getInstance().getString( gender ));
				break;
			default:
				items.put( gender, NALanguage.getInstance().getString( gender ));
				break;
			}
		}	
		super.setItems( items);
	}

	protected Gender getGender(){
		return super.getSelection();
	}
	
	protected void setGender( Gender gender ){
		super.select(gender);
	}

	@Override
	protected Gender onGetInput( Gender input) { 
		Gender gender = getGender();
		super.getController().setInput(gender);
		return gender;
	}

	@Override
	protected void onSetInput( Gender input, boolean overwrite) {
		if( overwrite )
			setGender( input );	
	}


	@Override
	public boolean checkRequiredFields() {
		return false;
	}
}