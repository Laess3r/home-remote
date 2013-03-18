package at.dahu4wa.framex.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import at.dahu4wa.framex.demo.demomodule.DemoController;
import at.dahu4wa.framex.framework.MainController;

/**
 * Main start point of application
 * 
 * @author Stefan Huber
 */
public class Main extends Application {

	public static final String APPLICATION_TITLE = Main.class.getSimpleName();
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
		mainController.registerController(new DemoController());
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
