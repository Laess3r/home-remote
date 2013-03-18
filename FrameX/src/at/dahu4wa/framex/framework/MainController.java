package at.dahu4wa.framex.framework;

import javafx.scene.Node;
import javafx.scene.text.Text;

/**
 * The main controller, where the others are being registered
 * 
 * @author Stefan Huber
 */
public class MainController {

	private final MainView mainView;
	private final MenuTreePaneController menuTreePaneController;
	private final TopMenuBarController topMenuBarController; // TODO implement top menu bar

	public MainController() {
		this.mainView = new MainView();
		this.menuTreePaneController = new MenuTreePaneController(
				mainView.getMenuTreePane());
		this.topMenuBarController = new TopMenuBarController(
				mainView.getTopMenuBar());
		init();
	}

	public MainView getMainView() {
		return mainView;
	}

	public void registerController(IFController controller) {
		menuTreePaneController.registerMenuPoint(this, controller);
	}

	private void init() {
		mainView.setContent(new Text(
				"Please select a module from the left menu bar."));

	}

	public void changeContentTo(IFController controller) {
		mainView.setContent(controller.getView());
		controller.postCreate();
	}

	public Node getCurrentContent() {
		return mainView.getContent();
	}
}
