package at.dahu4wa.fxclient.framework;

import javafx.scene.Node;
import javafx.scene.text.Text;
import at.dahu4wa.fxclient.modules.loginmodule.LoginFormController;
import at.dahu4wa.fxclient.modules.testmodule.TestController;

/**
 * The main controller, where the others are being registered
 * 
 * @author Stefan Huber
 */
public class MainController {

	private final MainView mainView;
	private final MenuTreePaneController menuTreePaneController;
	private final TopMenuBarController topMenuBarController;

	public MainController(MainView mainView) {
		this.mainView = mainView;
		this.menuTreePaneController = new MenuTreePaneController(
				mainView.getMenuTreePane());
		this.topMenuBarController = new TopMenuBarController(
				mainView.getTopMenuBar());
		registerControllers();
		init();
	}

	public void registerControllers() {
		// TODO how do I register the controllers automatically??
		menuTreePaneController.registerMenuPoint(new LoginFormController());
		menuTreePaneController.registerMenuPoint(new TestController());

	}

	private void init() {
		mainView.setContent(new Text(
				"This is the initial main content, set by the MainController. \nTODO: make listeners for dynamic menu list"));

	}

	public void changeContentTo(IFController controller) {
		mainView.setContent(controller.getView());
	}

	public Node getCurrentContent() {
		return mainView.getContent();
	}
}
