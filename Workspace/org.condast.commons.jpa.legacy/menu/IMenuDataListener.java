package org.condast.commons.authentication.ui.menu;

public interface IMenuDataListener {

	public enum MenuEvents{
		ITEM_SELECT,
		CLOSE_DIALOG,
		USER_DATA,
		LOGOUT;
	}

	public void notifyMenuDataChanged( MenuChangeEvent event );
}
