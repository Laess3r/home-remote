package at.dahu4wa.framex.framework;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MenuTreePaneView extends HBox {

	VBox buttonBox = null;

	public MenuTreePaneView() {
		VBox vBox = new VBox(10);

		vBox.setPadding(new Insets(20));
		vBox.setTranslateX(5);

		Text label = new Text("Modules");
		label.setFont(Font.font("Arial", FontWeight.BOLD, 20));

		vBox.getChildren().add(label);

		buttonBox = new VBox(5);

		buttonBox.setTranslateX(5);

		vBox.getChildren().add(buttonBox);

		getChildren().addAll(vBox, new Separator(Orientation.VERTICAL));
	}

	public void addButton(Button button) {
		buttonBox.getChildren().add(button);
	}

}
