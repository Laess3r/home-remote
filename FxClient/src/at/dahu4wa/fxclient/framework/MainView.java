package at.dahu4wa.fxclient.framework;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class MainView {

	BorderPane rootPane = new BorderPane();
	TopMenuBarView topMenuBar = new TopMenuBarView();
	MenuTreePaneView menuTreePane = new MenuTreePaneView();

	private Node content = null;

	public MainView() {
		rootPane.setTop(topMenuBar);
		rootPane.setLeft(menuTreePane);
	}

	public void setContent(final Node newContent) {
		if (content != null) {
			Timeline tlOldContent = new Timeline();
			tlOldContent.getKeyFrames().addAll(
					new KeyFrame(Duration.ZERO, new KeyValue(
							content.opacityProperty(), 1)),
					new KeyFrame(new Duration(500), new KeyValue(content
							.opacityProperty(), 0)));
			tlOldContent.play();
			tlOldContent.setOnFinished(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					setNewContent(newContent);
				}
			});
		} else {
			setNewContent(newContent);
		}
	}

	private void setNewContent(Node newContent) {
		newContent.setOpacity(0);
		Timeline tlNewContent = new Timeline();
		tlNewContent.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(
						newContent.opacityProperty(), 0)),
				new KeyFrame(new Duration(750), new KeyValue(newContent
						.opacityProperty(), 1)));
		tlNewContent.play();

		content = newContent;
		rootPane.setCenter(content);
	}

	public Node getContent() {
		return content;
	}

	public TopMenuBarView getTopMenuBar() {
		return topMenuBar;
	}

	public MenuTreePaneView getMenuTreePane() {
		return menuTreePane;
	}

	public Parent getRootPane() {
		return rootPane;
	}

}
