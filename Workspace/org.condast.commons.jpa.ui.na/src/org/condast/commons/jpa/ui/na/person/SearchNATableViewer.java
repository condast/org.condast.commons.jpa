package org.condast.commons.jpa.ui.na.person;

import java.util.ArrayList;
import java.util.Collection;

import org.condast.commons.date.DateUtils;
import org.condast.commons.jpa.na.model.IApplicationPerson;
import org.condast.commons.jpa.na.model.IPersonAddress;
import org.condast.commons.jpa.na.model.IProfessional;
import org.condast.commons.jpa.na.model.IAddress.AddressTypes;
import org.condast.commons.jpa.na.utils.NAUtils;
import org.condast.commons.jpa.ui.na.NALanguage;
import org.condast.commons.ui.widgets.search.ISearchResultsComposite;
import org.condast.commons.ui.widgets.search.ISearchSelectionListener;
import org.condast.commons.ui.widgets.search.SearchEvent;
import org.condast.commons.ui.widgets.table.AbstractViewerComparator;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;

public class SearchNATableViewer extends Composite implements ISearchResultsComposite<IApplicationPerson>{
	private static final long serialVersionUID = 1L;
	
	public static final String SQL_PERSONS_QUERY = "SELECT ap FROM ApplicationPerson ap";
	private static final String RWT_SEARCH = "search";
	
	private enum Columns{
		NAME,
		STREET,
		TOWN,
		BIRTH_DATE;

		@Override
		public String toString() {
			return NALanguage.getInstance().getString( super.toString() );
		}
		
		public static int getWidth( Columns column ){
			switch( column ){
			case NAME:
				return 25;
			case STREET:
				return 40;
			case BIRTH_DATE:
				return 10;
			default:
				return 15;
			}
		}
		
		public static String[] getItems(){
			String[] results = new String[ values().length ];
			for( int i=0; i<results.length; i++  ){
				results[i] = values()[i].toString();
			}
			return results;
		}
	}
	
	private enum Fields{
		SEARCH,
		GO;

		@Override
		public String toString() {
			return NALanguage.getInstance().getString( super.toString() );
		}
	}

	private TableViewer viewer;	
	private TableColumnLayout tableColumnLayout;
		
	private Text text_1;
	private NAViewerComparator comparator;
	
	/**
	 * Store the current query
	 */
	private String currentQuery;;
	
	private Collection<ISearchSelectionListener<IApplicationPerson>> listeners;
	
	public SearchNATableViewer( Composite parent,int style ) {
		super (parent,style );
		this.comparator = new NAViewerComparator();
		//viewerFilter = new StatusFilter();
		createComposite( parent, style );
		listeners = new ArrayList<ISearchSelectionListener<IApplicationPerson>>();
	}
		
	private void createComposite( Composite parent,int style ){
		setLayout( new GridLayout( 1, false ) );
		
		Group gr = new Group( this,SWT.NONE );
		gr.setText( Fields.SEARCH.toString() );
		gr.setLayout( new GridLayout( 3,false ) );
		GridData gd_gr = new GridData( SWT.FILL,SWT.FILL,true,false );
		gd_gr.heightHint = 57;
		gr.setLayoutData( gd_gr );
		gr.setData( RWT.CUSTOM_VARIANT,RWT_SEARCH );
		
		Combo combo = new Combo( gr, SWT.NONE );
		GridData gd_combo = new GridData( SWT.FILL, SWT.CENTER, false, false, 1, 1 );
		gd_combo.widthHint = 112;
		combo.setLayoutData( gd_combo );
		combo.setItems( Columns.getItems() );
		
		text_1 = new Text( gr, SWT.BORDER );
		GridData gd_text_1 = new GridData( SWT.FILL, SWT.FILL, true, false, 1, 1 );
		gd_text_1.widthHint = 267;
		text_1.setLayoutData( gd_text_1 );
		
		final Button btnGo = new Button( gr, SWT.NONE );
		btnGo.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected( SelectionEvent e ) {
				//ApplicationPersonController controller = NAService.getAPController();
				//if( controller == null ){
				//	btnGo.setEnabled( false );
				//	return;
				//}
				performQuery( SQL_PERSONS_QUERY );
			}
		});
		btnGo.setText( Fields.GO.toString() );

		Composite tableComposite = new Composite( this, SWT.NONE );
		tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout( tableColumnLayout );
	    GridData gd_table = new GridData( SWT.FILL, SWT.FILL, true, true, 1, 1 );
		tableComposite.setLayoutData( gd_table );

		viewer = new TableViewer( tableComposite, SWT.BORDER|SWT.MULTI|SWT.FULL_SELECTION );
		//viewer.setComparator( new ApplicationPersonViewerComparator() );
		viewer.setComparator( comparator );
		Table table = viewer.getTable();
		table.setHeaderVisible( true );
	    table.setLinesVisible( true );
	    viewer.setUseHashlookup( true );
	    
	    for( Columns column: Columns.values() ){
	    	createColumn( column, column.ordinal() );
	    }
	    viewer.setContentProvider( ArrayContentProvider.getInstance() );
	    viewer.setLabelProvider( new PersonLabelProvider() );
	    
	   	    
	    //viewer.addFilter( viewerFilter );	
	    
	    viewer.addDoubleClickListener( new IDoubleClickListener() {
			
			@Override
			public void doubleClick( DoubleClickEvent event ) {				
				IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
				IApplicationPerson ap = (IApplicationPerson)selection.getFirstElement();
				for( ISearchSelectionListener<IApplicationPerson> listener: listeners )
					listener.notifySearchResults( new SearchEvent<IApplicationPerson>( event, currentQuery, ap  ) );
			}
		});
	}
		
	public String getCurrentQuery() {
		return currentQuery;
	}

	@Override
	public Collection<IApplicationPerson> performQuery( String query ){
		this.currentQuery = query;
		Collection<IApplicationPerson> results = new ArrayList<IApplicationPerson>();
		/*
		ApplicationPersonController controller = NAService.getAPController();
		if( controller == null ){
			return results;
		}
		try{
			controller.open();
			results = controller.getQuery( query );
			viewer.setInput( results );
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		finally{
			try{
			controller.close();
			}
			catch( Exception ex ){
				ex.printStackTrace();
			}
		}
		*/	
		return results;
	}
	
	private TableViewerColumn createColumn( final Columns column, int index ) {
		TableViewerColumn result = new TableViewerColumn( viewer, SWT.NONE );

		//result.setLabelProvider( labelProvider );
		TableColumn tcolumn = result.getColumn();
		tcolumn.setText( column.toString() );
		tableColumnLayout.setColumnData( tcolumn, new ColumnWeightData( Columns.getWidth( column ), 75, true ) ); 

		tcolumn.setMoveable( true );
		tcolumn.addSelectionListener( getSelectionAdapter( tcolumn, index ) );
		return result;
	}

	private SelectionAdapter getSelectionAdapter( final TableColumn column, final int index ) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected( SelectionEvent e ) {
				comparator.setColumn( index );
				int dir = comparator.getDirection();
				viewer.getTable().setSortDirection( dir );
				viewer.getTable().setSortColumn( column );
				viewer.refresh();
			}
		};
		return selectionAdapter;
	}

	private class PersonLabelProvider extends LabelProvider implements ITableLabelProvider{
		private static final long serialVersionUID = 1L;

		@Override
		public String getColumnText( Object element, int columnIndex ) {
			String retval = "null";
			try{
				Columns column = Columns.values()[ columnIndex ];
				IApplicationPerson ap = (IApplicationPerson)element;
				IProfessional person = ap.getPerson();
				IPersonAddress address = person.getAddress( AddressTypes.MAIN );
				switch( column ){
				case NAME:
					retval = NAUtils.toSurnameFirstnameInfix( person.getName() );
					break;
				case STREET:
					if( address != null )
						retval = address.toString();
					break;
				case TOWN:
					if( address == null )
						return "";
					retval = address.getTown();				
					break;
				case BIRTH_DATE:
					retval = DateUtils.getFormatted( person.getBirthDate() );
					break;
				default:
					//String datum = DateFormatter.convertDateToStringShortFormat( sr.getStatus().getCreatedAt() );
					retval = "Date";
					break;				
				}
			}
			catch( Exception ex ){
				ex.printStackTrace();
			}
			return retval;
		}

		@Override
		public Image getColumnImage( Object arg0, int arg1 ) {
			return null;
		}

	}

	/* (non-Javadoc)
	 * @see nl.eetmee.na.swt.widgets.ISearchResultsComposite#addSelectionListener(org.condast.commons.ui.search.ISearchSelectionListener)
	 */
	@Override
	public void addSelectionListener( ISearchSelectionListener<IApplicationPerson> listener ) {
		listeners.add( listener );
	}

	/* (non-Javadoc)
	 * @see nl.eetmee.na.swt.widgets.ISearchResultsComposite#removeSelectionListener(org.condast.commons.ui.search.ISearchSelectionListener)
	 */
	@Override
	public void removeSelectionListener( ISearchSelectionListener<IApplicationPerson> listener ) {
		listeners.remove( listener );
	}
	
	private static class NAViewerComparator extends AbstractViewerComparator<IApplicationPerson>{
		private static final long serialVersionUID = 1L;

		@Override
		protected int compareColumn( int columnIndex, IApplicationPerson o1, IApplicationPerson o2 ) {
			return 0;
		}

		
	}

	@Override
	public Collection<IApplicationPerson> getInput() {
		// TODO Auto-generated method stub
		return null;
	}
}