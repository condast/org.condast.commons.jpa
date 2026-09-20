package org.condast.commons.jpa.ui.na.contacts;

import java.util.ArrayList;
import java.util.Collection;

import org.condast.commons.Utils;
import org.condast.commons.jpa.na.model.IContact;
import org.condast.commons.jpa.ui.na.NALanguage;
import org.condast.commons.ui.controller.EditEvent.EditTypes;
import org.condast.commons.ui.widgets.table.AbstractTableViewerWithDelete;
import org.condast.commons.ui.wtk.IStoreWithDelete;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class ContactsTableViewer extends AbstractTableViewerWithDelete<IContact>{
	private static final long serialVersionUID = 1L;

	private enum Columns{
		CONTACT_TYPE,
		IDENTIFIER;

		@Override
		public String toString() {
			return NALanguage.getInstance().getString( this );
		}

		public static int getWeight( Columns column ){
			switch( column ){
			case IDENTIFIER:
			case CONTACT_TYPE:
				return 30;
			default:
				return 10;
			}
		}
	}

	private Button addbutton;

	public ContactsTableViewer(Composite parent,int style ) {
		super(parent,style, true );
	}

	@Override
	protected void createContentComposite( Composite parent,int style ){
		super.createContentComposite(parent, style);
		TableViewer viewer = super.getViewer();
		for( Columns column: Columns.values() ){
			createColumn( column );
		}
		String deleteStr = NALanguage.getInstance().getString( Buttons.DELETE );
		super.createDeleteColumn( Columns.values().length, deleteStr, 10 );	
		viewer.setLabelProvider( new ContactsLabelProvider() );
	}
	
	public Button getAddButton() {
		return addbutton;
	}

	public IContact[] getInput(){
		Collection<IContact> contacts = new ArrayList<IContact>();
		if( Utils.assertNull( super.getInput() ))
			return null;
		for( Object obj: super.getInput() ){
			contacts.add( (IContact) obj );				
		}
		return contacts.toArray( new IContact[ contacts.size() ]);
	}
	
	public void setInput( Collection<IContact> contacts ){
		super.setInput( contacts );
	}
	
	@Override
	protected void onRowDoubleClick(IContact selection) {
		/* NOTHING */
	}

	@Override
	protected void onButtonCreated(Buttons type, Button button) {
		switch( type ) {
		case ADD:
			this.addbutton = button;
			this.addbutton.setEnabled(false);
			break;
		default:
			break;
		}
		GridData gd_button = new GridData(32, 32);
		gd_button.horizontalAlignment = SWT.RIGHT;
		button.setLayoutData(gd_button);
		button.setText("");
	}

	@Override
	protected boolean onButtonSelected(Buttons buttontype, SelectionEvent e) {
		boolean result = false;
		try {
			e.data = EditTypes.ADDED;
			notifyWidgetSelected( e );
			result = true;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return result;
	}
	
	@Override
	protected boolean onDeleteButton( Collection<IContact> deleted ) {
		return true;
	}

	private TableViewerColumn createColumn( final Columns column ) {
		TableViewerColumn result = super.createColumn( column.toString(), column.ordinal(), Columns.getWeight(column) );
		return result;
	}
	
	@Override
	protected void onRefresh() {
		//setInput(ap);
	}
	
	private class ContactsLabelProvider extends DeleteLabelProvider implements ITableLabelProvider{
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		public String getColumnText( Object element, int columnIndex ) {
			String retval = super.getColumnText(element, columnIndex);
			if( retval != null )
				return retval;
			Columns column = Columns.values()[ columnIndex ];
			IStoreWithDelete<IContact> swd = (IStoreWithDelete<IContact>) element;
			IContact contact = swd.getStore();
			switch( column){
			case CONTACT_TYPE:
				retval = NALanguage.getInstance().getString( contact.getContactType());
				break;
			case IDENTIFIER:
				retval = contact.getValue();
				break;
			default:
				break;				
			}
			swd.addText(retval);
			return retval;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Image getColumnImage(Object arg0, int columnIndex) {
			if( columnIndex == getDeleteColumnindex() ){
				IStoreWithDelete<IContact> swd = (IStoreWithDelete<IContact>) arg0;
				if( swd.getCount() == 1 )
					return null;
			}
			return super.getColumnImage(arg0, columnIndex);
		}
	}
}