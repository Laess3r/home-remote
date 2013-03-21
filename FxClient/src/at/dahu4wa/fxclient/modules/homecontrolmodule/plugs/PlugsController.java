package at.dahu4wa.fxclient.modules.homecontrolmodule.plugs;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import at.dahu4wa.framex.framework.IFController;
import at.dahu4wa.fxclient.modules.homecontrolmodule.connection.ConnectionCallback;
import at.dahu4wa.fxclient.modules.homecontrolmodule.connection.HomeConnection;
import at.dahu4wa.fxclient.modules.homecontrolmodule.connection.HomeControlGetType;
import at.dahu4wa.fxclient.modules.homecontrolmodule.connection.HomeControlPostType;
import at.dahu4wa.fxclient.modules.homecontrolmodule.connection.JsonConverter;
import at.dahu4wa.fxclient.modules.homecontrolmodule.connection.ServerResponseException;
import at.dahu4wa.fxclient.modules.homecontrolmodule.login.IFLoginDataProvider;
import at.dahuawa.homecontrol.model.Plug;

public class PlugsController implements IFController {

	private PlugsView view;
	private final IFLoginDataProvider loginDataProvider;

	public PlugsController(IFLoginDataProvider loginDataProvider) {
		this.loginDataProvider = loginDataProvider;
	}

	@Override
	public String getTitle() {
		return "MANAGE PLUGS";
	}

	@Override
	public Node getView() {
		return view;
	}

	@Override
	public void init() {
		view = new PlugsView();
		
		HomeConnection connection = new HomeConnection(loginDataProvider);

		connection.execute(HomeControlGetType.ALL_PLUGS,
				new ConnectionCallback() {

					@Override
					public void onResult(String result) {
						List<Plug> plugs = new ArrayList<Plug>();
						try {
							plugs = JsonConverter.getPlugs(result);
						} catch (ServerResponseException e) {
							showExceptionWindow(e);
						}

						for (Plug plug : plugs) {
							EventHandler<ActionEvent> onEvent = createOnEvent(plug);
							EventHandler<ActionEvent> offEvent = createOffEvent(plug);

							view.addButtonGroup(plug.getName(), onEvent,
									offEvent);
						}
					}

					@Override
					public void onFail(String errorMessage) {
						// TODO Auto-generated method stub
					}

				});

	}

	private EventHandler<ActionEvent> createOffEvent(final Plug plug) {
		EventHandler<ActionEvent> offEvent = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				HomeConnection connection = new HomeConnection(
						loginDataProvider);
				plug.setEnabled(false);
				connection.addNameValuePairs(JsonConverter.getParams(plug));
				connection.execute(HomeControlPostType.PLUG,
						new ConnectionCallback() {

							@Override
							public void onResult(String result) {
								System.out.println(JsonConverter
										.getPlug(result));
							}

							@Override
							public void onFail(String errorMessage) {
								// TODO Auto-generated method stub
								
							}
						});
			}
		};
		return offEvent;
	}

	private EventHandler<ActionEvent> createOnEvent(final Plug plug) {
		EventHandler<ActionEvent> onEvent = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				HomeConnection connection = new HomeConnection(
						loginDataProvider);
				plug.setEnabled(true);
				connection.addNameValuePairs(JsonConverter.getParams(plug));
				connection.execute(HomeControlPostType.PLUG,
						new ConnectionCallback() {

							@Override
							public void onResult(String result) {
								System.out.println(JsonConverter
										.getPlug(result));
							}

							@Override
							public void onFail(String errorMessage) {
								// TODO Auto-generated method stub
								
							}
						});
			}
		};
		return onEvent;
	}


	private void showExceptionWindow(ServerResponseException e) {

		WebView browser = new WebView();  
		WebEngine webEngine = browser.getEngine();  
		webEngine.loadContent(e.getResponse());
		
		view.add(browser, 0, 1, 4, 4);
		
	}
}
