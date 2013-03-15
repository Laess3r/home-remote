package at.dahu4wa.fxclient.modules.testmodule;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class TestView extends VBox {
	
	final Rectangle r = new Rectangle(200, 200, Color.BROWN);

	public TestView(){
		
		Text testText = new Text("TestText inside the test View");
		setAlignment(Pos.CENTER);
		getChildren().add(r);
		getChildren().add(testText);
	}

	public Rectangle getR() {
		return r;
	}

	
}
