package at.dahu4wa.fxclient.modules.homecontrolmodule.login;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class LoginFormView extends GridPane {

	private TextField userTextField;
	private PasswordField pwBox;
	private Button btnLogin;
	private Text actionTarget;

	public LoginFormView() {

		setAlignment(Pos.CENTER);
		setHgap(10);
		setVgap(10);
		setPadding(new Insets(25, 25, 25, 25));

		getStylesheets().add(
				LoginFormView.class.getResource("LoginForm.css")
						.toExternalForm());

		Text title = new Text("Welcome to HomeControl");
		title.setId("welcome-text");
		add(title, 0, 0, 2, 1);

		Label userName = new Label("Username");
		add(userName, 0, 1);

		userTextField = new TextField("DaHu4wa");
		add(userTextField, 1, 1);

		Label password = new Label("Password");
		add(password, 0, 2);

		pwBox = new PasswordField();
		add(pwBox, 1, 2);

		btnLogin = new Button("Log in");
		HBox hboxButton = new HBox(10);
		hboxButton.setAlignment(Pos.BOTTOM_RIGHT);
		hboxButton.getChildren().add(btnLogin);
		add(hboxButton, 1, 4);

		actionTarget = new Text();
		add(actionTarget, 1, 6);

	}

	public TextField getUserTextField() {
		return userTextField;
	}

	public PasswordField getPwBox() {
		return pwBox;
	}

	public Button getBtnLogin() {
		return btnLogin;
	}

	public Text getActionTarget() {
		return actionTarget;
	}

}
