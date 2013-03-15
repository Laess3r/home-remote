package at.dahu4wa.fxclient;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import at.dahu4wa.fxclient.framework.MainController;
import at.dahu4wa.fxclient.framework.MainView;

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
	private static MainView mainView = null;
	private static MainController mainController = null;

	public static MainController getMainController() {
		return mainController;
	}

	public static void main(String[] args) {
		mainView = new MainView();
		mainController = new MainController(mainView);
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(APPLICATION_TITLE);
		primaryStage.setWidth(SIZE_X);
		primaryStage.setHeight(SIZE_Y);
		primaryStage.setScene(new Scene(mainView.getRootPane()));
		primaryStage.show();
	}

}
