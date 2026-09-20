package org.condast.commons.jpa.ui.na.person;

import java.util.HashMap;
import java.util.Map;

import org.condast.commons.jpa.na.model.Gender;
import org.condast.commons.jpa.na.model.IProfessional;
import org.condast.commons.jpa.ui.na.NALanguage;
import org.condast.commons.strings.StringUtils;
import org.condast.commons.ui.widgets.combo.AbstractComboComposite;
import org.eclipse.swt.widgets.Composite;

public class TitleComposite extends AbstractComboComposite<IProfessional, String> {
	private static final long serialVersionUID = 1L;

	//Text fields
	private enum Fields{
		BIRTH_DATE,
		TITLE,
		TOOLTIP;
		
		public String toSring(){
			return NALanguage.getInstance().getString( this );
		}

		@SuppressWarnings("unused")
		public String getMessage(){
			return NALanguage.getInstance().getMessage( this );
		}
	}

	//Text fields
	private enum Titles{
		MISTER,
		MISSUS,
		MISS,
		DOCTOR;
		
		public String toSring(){
			return NALanguage.getInstance().getString( this );
		}
			
		public static String[] getTitles(){
			String[] results = new String[ values().length];
			for( int i=0; i< results.length; i++ ){
				results[i] = values()[i].toSring();
			}
			return results;
		}
	}

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TitleComposite(Composite parent, int style) {
		super(parent, style);
		setLabelText( Fields.TITLE.toSring() );
		Map<String, String> items = new HashMap<String, String>();
		for( String title: Titles.getTitles() ) {
			items.put( title, title );
		}
		setItems( items);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public Titles getTitle( Gender gender ){
		Titles title = Titles.values()[ super.getCombo().getSelectionIndex()];
		if( Titles.DOCTOR.equals( title ))
			return Titles.DOCTOR;
		title = getDefaultTitle(title, gender);
		getCombo().select( title.ordinal() );
		return title;		
	}
	
	@Override
	protected IProfessional onGetInput(IProfessional input) {
		Titles title = Titles.values()[ super.getCombo().getSelectionIndex() ];
		input.setTitle( title.name() ) ;
		return input;
	}

	@Override
	protected void onSetInput(IProfessional input, boolean overwrite) {
		if(!overwrite )
			return;
		if( !StringUtils.isEmpty( input.getTitle() )){
			Titles title = Titles.valueOf( input.getTitle());
			getCombo().select( title.ordinal() );	
		}
	}


	/**
	 * Get the default title from the gender
	 * @param current
	 * @param gender
	 * @return
	 */
	public static Titles getDefaultTitle( Titles current, Gender gender ){
		if( Titles.DOCTOR.equals( current ))
			return current;
		switch( gender ){
		case MALE:
			return Titles.MISTER;
		default:
			 return Titles.MISSUS;	
		}
	}

	/**
	 * Get the default title from the gender
	 * @param current
	 * @param gender
	 * @return
	 */
	public static Gender getDefaultGender( Titles title ){
		switch( title ){
		case MISTER:
			return Gender.MALE;
		default:
			return Gender.FEMALE;				
		}
	}

	@Override
	public boolean checkRequiredFields() {
		return true;
	}
}
