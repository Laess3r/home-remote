package at.dahu4wa.framex.demo.demomodule;

import javafx.geometry.Insets;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Simple demo view
 * 
 * @author Stefan Huber
 */
public class DemoView extends VBox {

	private Slider rotationSlider;
	private Slider scaleSlider;
	private Rectangle rect;

	public DemoView() {
		rotationSlider = new Slider(0, 360, 0);
		scaleSlider = new Slider(0.1, 3, 1);

		rect = new Rectangle(125, 125, Color.BLUE);
		rect.setTranslateX(200);
		rect.setTranslateY(200);
		
		setPadding(new Insets(20));
		getChildren().addAll(rotationSlider, scaleSlider, rect);
	}

	public Slider getRotationSlider() {
		return rotationSlider;
	}

	public Slider getScaleSlider() {
		return scaleSlider;
	}

	public Rectangle getRect() {
		return rect;
	}

}
