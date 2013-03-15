package at.dahu4wa.fxclient.framework;

import javafx.scene.control.Menu;

public class TopMenuBarController {

	private final TopMenuBarView view;

	public TopMenuBarController(TopMenuBarView view) {
		this.view = view;
		initMenuBar();
	}

	private void initMenuBar() {

		Menu fileMenu = new Menu("File");
		Menu settingsMenu = new Menu("Settings");
		Menu aboutMenu = new Menu("About");

		view.getMenus().addAll(fileMenu, settingsMenu, aboutMenu);

	}

}
