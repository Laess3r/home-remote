package at.dahu4wa.fxclient.modules.homecontrolmodule;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
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

		HomeConnection connection = new HomeConnection(this);

		connection.testConnection(new ConnectionCallback() {

			@Override
			public void onResult(String result) {
				boolean success = result.startsWith("{\"tempSensor\":");
				if (success) {
					view.getActionTarget().setFill(Color.GREEN);
					view.getActionTarget().setText(
							"Logged in successfully");
				} else {
					view.getActionTarget().setFill(Color.FIREBRICK);
					view.getActionTarget().setText("Login failed !");
				}
				Main.getMainController().enableMenuTree(success);
				view.getBtnLogin().setDisable(success);
				view.getUserTextField().setDisable(success);
				view.getPwBox().setDisable(success);
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
