package at.dahu4wa.fxclient.modules.homecontrolmodule.plugs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class PlugsView extends GridPane {

	int row = 0;

	public PlugsView() {

		setAlignment(Pos.CENTER);
		setHgap(10);
		setVgap(10);
		setPadding(new Insets(25, 25, 25, 25));

		getStylesheets().add(
				PlugsView.class.getResource("PlugsForm.css")
						.toExternalForm());
		
		Text title = new Text("Manage plugs");
		title.setId("header");
		add(title, 0, 0, 2, 1);
		
	}

	public void addButtonGroup(String name, EventHandler<ActionEvent> onEvent,
			EventHandler<ActionEvent> offEvent) {

		Button on = new Button("Turn on " + name);
		on.setOnAction(onEvent);
		Button off = new Button("Turn off " + name);
		off.setOnAction(offEvent);

		row++;
		add(on, 0, row);
		add(off, 1, row);
	}
}
