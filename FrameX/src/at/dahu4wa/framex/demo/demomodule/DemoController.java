package at.dahu4wa.framex.demo.demomodule;

import javafx.scene.Node;
import at.dahu4wa.framex.framework.IFController;

/**
 * Simple demo controller
 * 
 * @author DaHu4wA (Stefan Huber)
 */
public class DemoController implements IFController {

	private static final String MODULE_NAME = "Demo Module";
	private DemoView view;

	@Override
	public void init() {
		view = new DemoView();

		view.getRect().scaleXProperty()
				.bind(view.getScaleSlider().valueProperty());
		view.getRect().scaleYProperty()
				.bind(view.getScaleSlider().valueProperty());
		view.getRect().rotateProperty()
				.bind(view.getRotationSlider().valueProperty());
	}

	@Override
	public String getTitle() {
		return MODULE_NAME;
	}

	@Override
	public Node getView() {
		return view;
	}

}
