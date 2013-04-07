package at.dahu4wa.fxclient.modules.homecontrolmodule.login;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import at.dahu4wa.framex.framework.IFController;
import at.dahu4wa.fxclient.Main;
import at.dahu4wa.fxclient.modules.homecontrolmodule.connection.ConnectionCallback;
import at.dahu4wa.fxclient.modules.homecontrolmodule.connection.HomeConnection;

/**
 * Login form for HomeControlFx
 * 
 * @author Stefan Huber
 */
public class LoginFormController implements IFController, IFLoginDataProvider {

	private LoginFormView view;

	@Override
	public String getTitle() {
		return "Login Form";
	}

	@Override
	public Node getView() {
		return view;
	}

	@Override
	public void init() {
		view = new LoginFormView();
		registerListeners();
	}

	private void doLogin() {

		view.getBtnLogin().setDisable(true);
		view.getUserTextField().setDisable(true);
		view.getPwBox().setDisable(true);

		// TODO show loading circle :)

		HomeConnection connection = new HomeConnection(this);

		connection.testConnection(new ConnectionCallback() {

			@Override
			public void onResult(String result) {
				boolean success = result.startsWith("{\"tempSensor\":");
				if (success) {
					view.getActionTarget().setFill(Color.GREEN);
					view.getActionTarget().setText("Logged in successfully");
				} else {
					if (result.contains("HTTP Status 401")) {
						view.getActionTarget().setText(
								"Wrong username or password!");
					} else {
						view.getActionTarget().setText("Login failed !");
					}
					view.getActionTarget().setFill(Color.FIREBRICK);

				}
				Main.getMainController().enableMenuTree(success);
				view.getBtnLogin().setDisable(success);
				view.getUserTextField().setDisable(success);
				view.getPwBox().setDisable(success);
			}

			@Override
			public void onFail(String errorMessage) {
				view.getActionTarget().setFill(Color.FIREBRICK);
				view.getActionTarget().setText("Login failed: " + errorMessage);
				view.getBtnLogin().setDisable(false);
				view.getUserTextField().setDisable(false);
				view.getPwBox().setDisable(false);

			}
		});
	}

	private void registerListeners() {

		view.getBtnLogin().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				doLogin();
			}
		});

		view.getBtnLogin().setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				view.getBtnLogin().setScaleX(1.2);
				view.getBtnLogin().setScaleY(1.2);
			}
		});

		view.getBtnLogin().setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				view.getBtnLogin().setScaleX(1);
				view.getBtnLogin().setScaleY(1);
			}
		});

		EventHandler<KeyEvent> loginEvent = new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					doLogin();
				}
			}
		};
		view.getPwBox().setOnKeyPressed(loginEvent);
		view.getUserTextField().setOnKeyPressed(loginEvent);
	}

	public String getUsername() {
		if (view == null) {
			return "";
		}
		return view.getUserTextField().getText();
	}

	public String getPassword() {
		if (view == null) {
			return "";
		}
		return view.getPwBox().getText();
	}
}
