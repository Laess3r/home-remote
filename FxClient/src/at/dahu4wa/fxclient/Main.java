package at.dahu4wa.fxclient;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import at.dahu4wa.framex.framework.MainController;
import at.dahu4wa.fxclient.modules.homecontrolmodule.LoginFormController;
import at.dahu4wa.fxclient.modules.homecontrolmodule.plugs.PlugsController;
import at.dahu4wa.fxclient.modules.testmodule.TestController;

/**
 * Main start point of application
 * 
 * @author Stefan Huber
 */
public class Main extends Application {

	public static final String APPLICATION_TITLE = "HomeControlFX 1.0 beta";
	private static final int SIZE_X = 800;
	private static final int SIZE_Y = 650;

	// TODO maybe there is a better way than singleton pattern
	
	private static MainController mainController = null;

	public static MainController getMainController() {
		return mainController;
	}

	public static void main(String[] args) {
		mainController = new MainController();
		launch(args);
	}

	private void registerControllers() {
		LoginFormController loginForm = new LoginFormController();
		//mainController.registerController(loginForm);
		mainController.registerController(new TestController());
		mainController.registerController(new PlugsController(loginForm));
		
		mainController.changeContentTo(loginForm);
		mainController.enableMenuTree(false);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(APPLICATION_TITLE);
		primaryStage.setWidth(SIZE_X);
		primaryStage.setHeight(SIZE_Y);
		primaryStage.setScene(new Scene(mainController.getMainView().getRootPane()));
		primaryStage.show();

		registerControllers();
	}

}
