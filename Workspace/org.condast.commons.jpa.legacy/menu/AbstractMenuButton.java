package org.condast.commons.authentication.ui.menu;

import java.util.ArrayList;
import java.util.Collection;

import org.condast.commons.jpa.authentication.core.IAuthenticationManager;
import org.condast.commons.jpa.authentication.ui.images.AuthenticationImages;
import org.condast.commons.jpa.authentication.ui.images.AuthenticationImages.Images;
import org.condast.commons.jpa.authentication.user.ILoginUser;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public abstract class AbstractMenuButton< U extends Object> extends Button {
	private static final long serialVersionUID = 1L;

	public static final String S_LOGIN = "Login";

	public static final String S_HELP_URL = "/help/help.html";
	public static final String S_ABOUT_URL = "/help/about.html";

	private Menu menu;
	private IAuthenticationManager<U> manager;
	private SelectionListener selectionListener;
	private boolean disposed;

	private Collection<IMenuDataListener> listeners;

	/**
	 * @wbp.parser.entryPoint
	 */
	public AbstractMenuButton(Composite parent, int style) {
		super(parent, style);
		this.disposed = false;
		this.listeners = new ArrayList<>();
		createButton(parent, style);
	}

	protected void createButton( Composite parent, int style ) {
		setText( S_LOGIN );
		this.selectionListener = selectListener( isLoggedIn() );
		addSelectionListener(selectionListener);
		menu = new Menu(this);
		this.onSetupMenu(menu);
		setMenu(menu);

		MenuItem mntmLogoff = new MenuItem(menu, SWT.NONE);
		mntmLogoff.setText("Log Off");
		mntmLogoff.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				logOut();
				super.widgetSelected(e);
			}
		});
	}

	public void addFieldChangeListener( IMenuDataListener listener ) {
		this.listeners.add(listener);
	}

	public void removeFieldChangeListener( IMenuDataListener listener ) {
		this.listeners.remove(listener);
	}

	protected void notifyListenerChanged( MenuChangeEvent event ) {
		for( IMenuDataListener listener: this.listeners)
			listener.notifyMenuDataChanged(event);
	}

	protected boolean isLoggedIn() {
		return (this.manager == null )? false: this.manager.isLoggedIn();
	}

	protected IAuthenticationManager<U> getManager() {
		return manager;
	}

	public void setInput( IAuthenticationManager<U> input ) {
		this.manager = input;
	}

	protected U getLoginData() {
		return this.manager.getData();
	}

	protected void onSetupLoggedIn() {
		if( isDisposed())
			return;
		getDisplay().asyncExec( new Runnable() {

			@Override
			public void run() {
				setText("");
				setImage( AuthenticationImages.getInstance().getImage( Images.MENU ));
			}

		});
	}

	/**
	 * Set up the menu
	 * @param menu
	 */
	protected abstract void onSetupMenu(Menu menu);

	public void loggedIn( ILoginUser user ) {
		boolean loggedIn = isLoggedIn();
		if( disposed || isDisposed())
			return;
		this.onSetupLoggedIn();
		removeSelectionListener(selectionListener);
		selectionListener = selectListener( loggedIn );
		addSelectionListener( selectionListener);
	}

	protected abstract void onPrepareLogout(U user);

	public void logOut( ) {
		try {
			this.onPrepareLogout(manager.getData());
			this.manager.logout();
			if( disposed || isDisposed())
				return;
			boolean loggedIn = isLoggedIn();
			setText("Login");
			removeSelectionListener(selectionListener);
			selectionListener = selectListener(loggedIn );
			addSelectionListener( selectionListener);
			setImage( null );
			setMenu(null);
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
	}


	@Override
	public void dispose() {
		this.disposed = true;
		super.dispose();
	}

	/**
	 * Selects between two adapters; one for logging in and one
	 * for a logout
	 * @param loggedin
	 * @return
	 */
	protected SelectionListener selectListener( boolean loggedin ) {
		//Replace the selection listener
		return (loggedin )? new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				try {
					Rectangle bounds = getBounds();
					Point point = getParent().toDisplay(bounds.x, bounds.y + bounds.height);
					menu.setLocation(point);
					menu.setVisible(true);
				}
				catch( Exception ex ) {
					ex.printStackTrace();
				}
			}
		}: new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					manager.login(true);
				}
				catch( Exception ex ){
					ex.printStackTrace();
				}
			}
		};
	}
}