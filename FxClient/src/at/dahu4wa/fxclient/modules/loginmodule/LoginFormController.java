package at.dahu4wa.fxclient.modules.loginmodule;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import at.dahu4wa.fxclient.framework.IFController;

/**
 * Login form for HomeControlFx
 * 
 * @author Stefan Huber
 */
public class LoginFormController implements IFController {

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

	private void registerListeners() {
		view.getBtnLogin().setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				view.getActionTarget().setFill(Color.FIREBRICK);
				view.getActionTarget().setText(
						"Error loggin in user "
								+ view.getUserTextField().getText() + ".");
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
}
