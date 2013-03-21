package at.dahu4wa.fxclient.modules.homecontrolmodule.plugs;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class PlugsView extends GridPane {

	int row = 0;

	public PlugsView() {

		setAlignment(Pos.CENTER);
		setHgap(10);
		setVgap(10);
		setPadding(new Insets(25, 25, 25, 25));

		getStylesheets().add(
				PlugsView.class.getResource("PlugsForm.css").toExternalForm());

		Text title = new Text("Manage devices");
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
		
		
		Timeline jumpIn = new Timeline();
		int duration = 1000;
		jumpIn.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(
						on.scaleYProperty(), 0)),
				new KeyFrame(new Duration(duration), new KeyValue(on
						.scaleYProperty(), 1)),
						
						new KeyFrame(Duration.ZERO, new KeyValue(
								on.scaleXProperty(), 0)),
						new KeyFrame(new Duration(duration), new KeyValue(on
								.scaleXProperty(), 1))
				
				,
				
				new KeyFrame(Duration.ZERO, new KeyValue(
						off.scaleXProperty(), 0)),
				new KeyFrame(new Duration(duration), new KeyValue(off
						.scaleXProperty(), 1))
				
				,
				
				new KeyFrame(Duration.ZERO, new KeyValue(
						off.scaleYProperty(), 0)),
				new KeyFrame(new Duration(duration), new KeyValue(off
						.scaleYProperty(), 1))
				
				
				);
		jumpIn.play();
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		add(off, 1, row);
	}
}
