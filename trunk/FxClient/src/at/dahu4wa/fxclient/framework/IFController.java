package at.dahu4wa.fxclient.framework;

import javafx.scene.Node;

/**
 * Interface for all controllers
 * 
 * @author Stefan Huber
 */
public interface IFController {

	/**
	 * Is being called before getView, so the view can be built here
	 */
	void init();

	/**
	 * @returns the title displayed at the left menu bar
	 */
	String getTitle();

	/**
	 * @returns the view that will be opened when the menu entry is selected
	 */
	Node getView();

}
