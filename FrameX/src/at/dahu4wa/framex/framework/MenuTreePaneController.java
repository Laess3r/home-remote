package at.dahu4wa.framex.framework;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class MenuTreePaneController {

	private final MenuTreePaneView view;

	public MenuTreePaneController(MenuTreePaneView view) {
		this.view = view;
	}

	public void registerMenuPoint(final MainController mainController, final IFController controller) {

		final Button button = new Button(controller.getTitle());

		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (mainController.getCurrentContent() == controller
						.getView()) {
					System.out.println("View already opened");
				} else {
					if (controller.getView() == null) {
						controller.init();
					}
					mainController.changeContentTo(controller);
				}
			}
		});

		view.addButton(button);

	}

}
