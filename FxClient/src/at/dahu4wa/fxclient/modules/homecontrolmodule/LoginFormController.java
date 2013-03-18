package at.dahu4wa.fxclient.modules.homecontrolmodule;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import at.dahu4wa.framex.framework.IFController;

/**
 * Login form for HomeControlFx
 * 
 * @author Stefan Huber
 */
public class LoginFormController implements IFController {

	private LoginFormView view;
	private HomeConnection homeConnection;

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
		homeConnection = new HomeConnection();
		registerListeners();
	}

	private void registerListeners() {

		final GetCallback callback = new GetCallback() {

			@Override
			public void onResult(HomeControlGetType getType, String result) {

				if (getType == null) {
					view.getActionTarget().setFill(Color.FIREBRICK);
					view.getActionTarget().setText(result);
				} else {
					view.getActionTarget().setFill(Color.GREEN);
					view.getActionTarget().setText("Login successful!");
				}
			}
		};

		view.getBtnLogin().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				homeConnection.doGet(callback, getUserPass(),
						HomeControlGetType.ALL_TEMPS);
			}
		});

		view.getBtnLogin().setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				view.getBtnLogin().setScaleX(1.5);
				view.getBtnLogin().setScaleY(1.5);
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

	public String getUserPass() {
		return view.getUserTextField().getText() + ":"
				+ view.getPwBox().getText();
	}

	@Override
	public void postCreate() {
		// TODO Auto-generated method stub
		
	}
}
