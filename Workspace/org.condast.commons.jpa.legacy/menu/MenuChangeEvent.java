package org.condast.commons.authentication.ui.menu;

import java.util.EventObject;

import org.condast.commons.authentication.ui.menu.IMenuDataListener.MenuEvents;

public class MenuChangeEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	private MenuEvents menuEvent;

	public MenuChangeEvent(Object source, MenuEvents menuEvent) {
		super(source);
		this.menuEvent = menuEvent;
	}

	public MenuEvents getMenuEvent() {
		return menuEvent;
	}
}
