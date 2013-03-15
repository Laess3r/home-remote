package at.dahu4wa.fxclient.framework;

import javafx.scene.Node;

/**
 * Interface for all controllers
 * 
 * @author Stefan Huber
 */
public interface IFController {

	void init();

	String getTitle();

	Node getView();

}
