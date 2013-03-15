package at.dahu4wa.fxclient.demos;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Login form for HomeControlFx
 * 
 * @author Stefan Huber
 */
public class LoginForm extends Application {

	private static final int SIZE_X = 500;
	private static final int SIZE_Y = 400;

	public static void main(String[] args) {
		// needed to start the application
		launch(args);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void start(Stage primaryStage) {

		// primaryStage is the main window
		primaryStage.setTitle("HomeControl FX.beta");

		// gridPane layout
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		// add text and labels, including some css
		Text title = new Text("Welcome to HomeControl");
		title.setId("welcome-text");
		grid.add(title, 0, 0, 2, 1);

		Label userName = new Label("Username");
		userName.setRotate(180);
		grid.add(userName, 0, 1);

		final TextField userTextField = new TextField();

		// TODO bin loginProperty

		grid.add(userTextField, 1, 1);

		Label password = new Label("Password");
		password.setRotate(180);
		grid.add(password, 0, 2);

		PasswordField pwBox = new PasswordField();
		grid.add(pwBox, 1, 2);

		final Button btnLogin = new Button("Log in");
		// HBox pane sets alignment for button
		HBox hboxButton = new HBox(10);
		hboxButton.setAlignment(Pos.BOTTOM_RIGHT);
		hboxButton.getChildren().add(btnLogin);
		grid.add(hboxButton, 1, 4);

		final Text actionTarget = new Text();
		grid.add(actionTarget, 1, 6);

		btnLogin.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				actionTarget.setFill(Color.FIREBRICK);
				actionTarget.setText("Error loggin in user "
						+ userTextField.getText() + ".");
			}
		});

		Scene scene = new Scene(grid, SIZE_X, SIZE_Y);

		scene.setFill(Color.AZURE);

		scene.getStylesheets().add(
				LoginForm.class.getResource("LoginForm.css").toExternalForm());

		primaryStage.setScene(scene);

		// show the main window
		primaryStage.show();

		btnLogin.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				btnLogin.setScaleX(1.5);
				btnLogin.setScaleY(1.5);
			}
		});

		btnLogin.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				btnLogin.setScaleX(1);
				btnLogin.setScaleY(1);
			}
		});

	}
}
