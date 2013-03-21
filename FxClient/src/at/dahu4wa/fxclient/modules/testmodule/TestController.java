package at.dahu4wa.fxclient.modules.testmodule;

import javafx.scene.Node;
import at.dahu4wa.framex.framework.IFController;

/**
 * Test module
 * 
 * @author Stefan Huber
 */
public class TestController implements IFController {
	private TestView scene;
	volatile boolean isRunning = false;

	@Override
	public String getTitle() {
		return "Test Module";
	}

	@Override
	public Node getView() {
		return scene;
	}

	@Override
	public void init() {
		if (scene == null)
			scene = new TestView();

		Runnable run = new Runnable() {
			@Override
			public void run() {
				isRunning = true;
				for (double i = 0; i <= 10000; i++) {
					scene.getR().setRotate(i);
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						System.err.println("thread interrupted");
					}
				}
				isRunning = false;
			}
		};
		Thread t = new Thread(run);
		if (!isRunning)
			t.start();
	}

}
