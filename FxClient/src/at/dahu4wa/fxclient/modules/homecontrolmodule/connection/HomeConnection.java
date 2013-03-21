package at.dahu4wa.fxclient.modules.homecontrolmodule.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.NoRouteToHostException;
import java.util.ArrayList;

import javafx.application.Platform;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import at.dahu4wa.fxclient.modules.homecontrolmodule.login.IFLoginDataProvider;

public class HomeConnection {

	private int taskType;
	private final IFLoginDataProvider loginDataProvider;
	private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

	public HomeConnection(IFLoginDataProvider loginDataProvider) {
		this.loginDataProvider = loginDataProvider;
	}

	public void addNameValuePairs(ArrayList<NameValuePair> pairs) {
		params.addAll(pairs);
	}

	public void addNameValuePair(String name, String value) {
		params.add(new BasicNameValuePair(name, value));
	}

	/**
	 * Executes the POST
	 */
	public void execute(HomeControlPostType type, ConnectionCallback callback) {

		String url = IP.BASE_PATH + type.getPath();
		taskType = HomeControlPostType.TYPE;
		execute(url, callback);
	}

	/**
	 * Executes the GET
	 */
	public void execute(HomeControlGetType type, ConnectionCallback callback) {

		String url = IP.BASE_PATH + type.getPath();
		taskType = HomeControlGetType.TYPE;
		execute(url, callback);
	}

	public void testConnection(ConnectionCallback callback) {
		String url = IP.BASE_PATH + HomeControlGetType.ALL_TEMPS.getPath();
		taskType = HomeControlGetType.TYPE;
		execute(url, callback);
	}

	private void execute(final String url, final ConnectionCallback callback) {

		Runnable r = new Runnable() {

			@Override
			public void run() {

				String result = "";

				HttpResponse response = null;
				try {
					response = doResponse(url);
				} catch (NoRouteToHostException e1) {
					e1.printStackTrace();
					callback.onFail(e1.getMessage());
				}

				if (response == null) {
					return;
				} else {

					try {
						result = inputStreamToString(response.getEntity()
								.getContent());
					} catch (IOException e) {
					}
				}

				final String res = result;
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						callback.onResult(res);
					}
				});
			}
		};

		Thread t = new Thread(r);
		t.start();
	}

	private HttpParams getHttpParams() {

		HttpParams htpp = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(htpp, 10000);
		HttpConnectionParams.setSoTimeout(htpp, 50000);

		return htpp;
	}

	private HttpResponse doResponse(String url) throws NoRouteToHostException {

		HttpClient httpclient = new DefaultHttpClient(getHttpParams());
		HttpResponse response = null;

		try {
			byte[] encoded = Base64.encodeBase64((loginDataProvider
					.getUsername() + ":" + loginDataProvider.getPassword())
					.getBytes());
			String userPass = new String(encoded, "UTF-8");

			switch (taskType) {

			case HomeControlPostType.TYPE:
				HttpPost httppost = new HttpPost(url);

				httppost.setHeader("Authorization", "Basic " + userPass);

				// Add parameters
				httppost.setEntity(new UrlEncodedFormEntity(params));

				response = httpclient.execute(httppost);
				break;
			case HomeControlGetType.TYPE:
				HttpGet httpget = new HttpGet(url);
				httpget.setHeader("Authorization", "Basic " + userPass);
				response = httpclient.execute(httpget);
				break;
			}
		} catch (NoRouteToHostException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return response;
	}

	private String inputStreamToString(InputStream is) {

		String line = "";
		StringBuilder total = new StringBuilder();

		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		try {
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
		} catch (IOException e) {
			// toast("ERROR: Could not retrieve data from server! (l:420)");
			e.printStackTrace();
		}
		return total.toString();
	}
}