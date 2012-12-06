package at.dahuawa.homecontrolapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import at.dahuawa.homecontrolapp.model.Plug;
import at.dahuawa.homecontrolapp.model.TempSensor;
import at.dahuawa.homecontrolsimple.R;

/**
 * Home Control App Version 2
 * 
 * @author Stefan Huber
 */
public class HomeActivity extends Activity {

	private static final String PATH_TO_PLUG = "/HomeBase/plug/";
	private static final String PATH_TO_TEMPSENSOR = "/HomeBase/tempsensor/";

	TextView textInput;

	String SERVER_IP;
	int SERVER_PORT;

	String USER;
	String PASSWORD;

	Switch button_A;
	Switch button_B;
	Switch button_C;
	Switch button_D;

	TextView tempA;
	TextView tempB;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		textInput = (TextView) findViewById(R.id.textinput);
		button_A = (Switch) findViewById(R.id.buttonA);
		button_B = (Switch) findViewById(R.id.buttonB);
		button_C = (Switch) findViewById(R.id.buttonC);
		button_D = (Switch) findViewById(R.id.buttonD);
		tempA = (TextView) findViewById(R.id.tempA);
		tempB = (TextView) findViewById(R.id.tempB);
		setButtonListeners();
		loadSettings();
		setServerInfoMessage();
		resetAllButtons();
		updateAll();
	}

	@Override
	public void onRestart() {
		super.onRestart();

		updateAll();
	}

	private void setServerInfoMessage() {
		textInput.setText(SERVER_IP + ":" + SERVER_PORT + ",  " + USER);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.update:
			updateAll();
			break;

		case R.id.clear:
			resetAllButtons();
			break;

		case R.id.settings:
			Intent settingsIntent = new Intent(this, SettingsActivity.class);

			settingsIntent.putExtra("ip", SERVER_IP);
			settingsIntent.putExtra("port", SERVER_PORT);
			settingsIntent.putExtra("user", USER);
			settingsIntent.putExtra("password", PASSWORD);

			startActivityForResult(settingsIntent, 0);
			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
			break;

		case R.id.log:
			Intent logIntent = new Intent(this, LogActivity.class);
			startActivityForResult(logIntent, 0);
			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
			break;

		}
		return true;
	}

	private AlertDialog buildDialog(final CompoundButton button, final char plugId) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Confirm");
		builder.setMessage("Do you really want to toggle?");
		builder.setCancelable(true);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				button.setChecked(!button.isChecked());
				togglePlug(button, plugId);
			}
		});

		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

			public void onCancel(DialogInterface dialog) {
			}
		});

		return builder.create();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			Bundle dataFromSetting = data.getExtras();
			SERVER_IP = dataFromSetting.getString("ip");
			SERVER_PORT = dataFromSetting.getInt("port");
			USER = dataFromSetting.getString("user");
			PASSWORD = dataFromSetting.getString("password");
			saveSettings();
			setServerInfoMessage();
		}
	}

	private void setButtonListeners() {

		button_A.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				togglePlugWithWarningMessage(button_A, 'A');
			}
		});

		button_B.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				togglePlug(button_B, 'B');
			}
		});

		button_C.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				togglePlug(button_C, 'C');
			}
		});

		button_D.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				togglePlugWithWarningMessage(button_D, 'D');
			}

		});
	}

	private void togglePlug(CompoundButton button, char plugId) {
		Plug plug = new Plug();
		plug.setId(plugId);
		plug.setEnabled(button.isChecked());

		postPlug(plug);

		button.setChecked(!button.isChecked());
	}

	private void togglePlugWithWarningMessage(CompoundButton button, char plugId) {
		button.setChecked(!button.isChecked());
		buildDialog(button, plugId).show();
	}

	private void toast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}

	// --------------------- JSON HANDLERS -------------------------------------

	public void handleResponse(String response) {

		if (response.equals("")) {
			resetAllButtons();
			toast("ERROR: Could not retrieve data from server!");
			return;
		}

		if (response.startsWith("{\"plug\":[{\"")) {
			handleArrayPlugResponse(response);
		} else {
			handleSinglePlugResponse(response);
		}

		if (response.startsWith("{\"tempSensor\":[{\"")) {
			handleArrayTempSensorResponse(response);
		} else {
			handleSingleTempSensorResponse(response);
		}
		
		if (response.startsWith("{\"tempSensor\":[{\"")) {
			handleArrayTempSensorResponse(response);
		}
	}

	public void handleArrayPlugResponse(String arrayResponse) {

		try {
			JSONObject json = new JSONObject(arrayResponse);

			JSONArray array = json.getJSONArray("plug");

			for (int i = 0; i < array.length(); i++) {
				handleSinglePlugResponse(array.getJSONObject(i));
			}
		} catch (Exception e) {
			toast("ERROR: Could not read array response! (l:187)");
		}
	}

	public void handleArrayTempSensorResponse(String arrayResponse) {

		try {
			JSONObject json = new JSONObject(arrayResponse);

			JSONArray array = json.getJSONArray("tempSensor");

			for (int i = 0; i < array.length(); i++) {
				handleSingleTempSensorResponse(array.getJSONObject(i));
			}
		} catch (Exception e) {
			toast("ERROR: Could not read array response!");
		}
	}

	public void handleSingleTempSensorResponse(String singleResponse) {
		try {
			JSONObject jso = new JSONObject(singleResponse);
			handleSingleTempSensorResponse(jso);

		} catch (Exception e) {
			// toast("ERROR: Could not read single response!");
		}
	}

	public void handleSinglePlugResponse(String singleResponse) {
		try {
			JSONObject jso = new JSONObject(singleResponse);
			handleSinglePlugResponse(jso);

		} catch (Exception e) {
			// toast("ERROR: Could not read single response! (l:216)");
		}
	}

	private void handleSingleTempSensorResponse(JSONObject jso) throws JSONException {

		char id = (char) Integer.parseInt(jso.getString("id"));
		String temp = jso.getString("tempValue");
		String name = jso.getString("name");

		TempSensor received = new TempSensor(id, name, Float.parseFloat(temp));

		updateTemp(received);
	}

	private void handleSinglePlugResponse(JSONObject jso) throws JSONException {

		char id = (char) Integer.parseInt(jso.getString("id"));
		boolean enabled = jso.getBoolean("enabled");
		String name = jso.getString("name");

		Plug received = new Plug(id, name, enabled);

		findPlugAndUpdateButtons(received);
	}

	private void findPlugAndUpdateButtons(Plug plug) {

		switch (plug.getId()) {
		case 'A':
			button_A.setText(plug.getName());
			button_A.setChecked(plug.isEnabled());
			button_A.setEnabled(true);
			break;
		case 'B':
			button_B.setText(plug.getName());
			button_B.setChecked(plug.isEnabled());
			button_B.setEnabled(true);
			break;
		case 'C':
			button_C.setText(plug.getName());
			button_C.setChecked(plug.isEnabled());
			button_C.setEnabled(true);
			break;
		case 'D':
			button_D.setText(plug.getName());
			button_D.setChecked(plug.isEnabled());
			button_D.setEnabled(true);
			break;
		default:
			toast("PLUG ID not found: " + plug.getId());
			break;
		}
	}

	private void updateTemp(TempSensor received) {

		switch (received.getId()) {
		case 'A':
			tempA.setText(received.getName() + ":						 " + received.getTempValue() + "°C");
			break;
		case 'B':
			tempB.setText(received.getName() + ":						   " + received.getTempValue() + "°C");
			break;
		default:
			break;
		}

	}

	private void resetAllButtons() {

		button_A.setText("n/a");
		button_A.setEnabled(false);
		button_A.setChecked(false);

		button_B.setText("n/a");
		button_B.setEnabled(false);
		button_B.setChecked(false);

		button_C.setText("n/a");
		button_C.setEnabled(false);
		button_C.setChecked(false);

		button_D.setText("n/a");
		button_D.setChecked(false);
		button_D.setEnabled(false);

		tempA.setText("n/a");
		tempB.setText("n/a");
	}

	// ------------------ GET AND POST METHODS -----------------------

	@SuppressLint("UseValueOf")
	public void postPlug(Plug plug) {

		String postUrl = "http://" + SERVER_IP + ":" + SERVER_PORT + PATH_TO_PLUG;

		String postText = plug.isEnabled() ? "Turning ON" : "Turning OFF";

		WebServiceTask wst = new WebServiceTask(WebServiceTask.POST_TASK, this, postText);

		wst.addNameValuePair("id", new Character(plug.getId()).toString());
		wst.addNameValuePair("name", plug.getName());
		wst.addNameValuePair("enabled", new Boolean(plug.isEnabled()).toString());

		// the passed String is the URL we will POST to
		wst.execute(new String[] { postUrl });
	}

	public void updateAll() {

		updateAllPlugs();
		updateTempSensors();

	}

	public void updateAllPlugs() {
		String allPlugsUrl = "http://" + SERVER_IP + ":" + SERVER_PORT + PATH_TO_PLUG + "all";

		WebServiceTask wst = new WebServiceTask(WebServiceTask.GET_TASK, this, "Getting plugs from server ...");

		wst.execute(new String[] { allPlugsUrl });
	}

	public void updateTempSensors() {

		String allTempsUrl = "http://" + SERVER_IP + ":" + SERVER_PORT + PATH_TO_TEMPSENSOR + "all";

		WebServiceTask wst = new WebServiceTask(WebServiceTask.GET_TASK, this, "Getting plugs from server ...");

		wst.execute(new String[] { allTempsUrl });
	}

	// ---- SAVE AND LOAD CONNECTION SETTINGS ----

	private void saveSettings() {
		SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("homeControlIp", SERVER_IP);
		editor.putInt("homeControlPort", SERVER_PORT);
		editor.putString("homeControlUser", USER);
		editor.putString("homeControlPassword", PASSWORD);
		editor.commit();
	}

	private void loadSettings() {
		SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
		SERVER_IP = sharedPreferences.getString("homeControlIp", "0.0.0.0");
		SERVER_PORT = sharedPreferences.getInt("homeControlPort", 0);
		USER = sharedPreferences.getString("homeControlUser", "user");
		PASSWORD = sharedPreferences.getString("homeControlPassword", "password");

	}

	// ---------------------- WEBSERVICE TASK --------------------

	private class WebServiceTask extends AsyncTask<String, Integer, String> {

		public static final int POST_TASK = 1;
		public static final int GET_TASK = 2;

		// connection timeout, in milliseconds (waiting to connect)
		private static final int CONN_TIMEOUT = 5000;

		// socket timeout, in milliseconds (waiting for data)
		private static final int SOCKET_TIMEOUT = 50000;

		private int taskType = GET_TASK;
		private Context mContext = null;
		private String processMessage = "DEFINE MESSAGE ! ";

		private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		private ProgressDialog pDlg = null;

		public WebServiceTask(int taskType, Context mContext, String processMessage) {

			this.taskType = taskType;
			this.mContext = mContext;
			this.processMessage = processMessage;
		}

		public void addNameValuePair(String name, String value) {

			params.add(new BasicNameValuePair(name, value));
		}

		@SuppressWarnings("deprecation")
		private void showProgressDialog() {

			pDlg = new ProgressDialog(mContext);
			pDlg.setMessage(processMessage);
			pDlg.setProgressDrawable(mContext.getWallpaper());
			pDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDlg.setCancelable(false);
			pDlg.show();
		}

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}

		protected String doInBackground(String... urls) {

			String url = urls[0];
			String result = "";

			HttpResponse response = doResponse(url);

			if (response == null) {
				return result;
			} else {

				try {

					result = inputStreamToString(response.getEntity().getContent());

				} catch (IOException e) {
					// toast("ERROR: Could not retrieve data from server! (l: 347)");
				}
			}

			return result;
		}

		@Override
		protected void onPostExecute(String response) {

			handleResponse(response);
			pDlg.dismiss();

		}

		// Establish connection and socket (data retrieval) timeouts
		private HttpParams getHttpParams() {

			HttpParams htpp = new BasicHttpParams();

			HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
			HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);

			return htpp;
		}

		private HttpResponse doResponse(String url) {

			// Use our connection and data timeouts as parameters for our
			// DefaultHttpClient
			HttpClient httpclient = new DefaultHttpClient(getHttpParams());

			HttpResponse response = null;

			try {
				switch (taskType) {

				case POST_TASK:
					HttpPost httppost = new HttpPost(url);

					httppost.setHeader("Authorization",
							"Basic " + Base64.encodeToString((USER + ":" + PASSWORD).getBytes(), Base64.NO_WRAP));

					// Add parameters
					httppost.setEntity(new UrlEncodedFormEntity(params));

					response = httpclient.execute(httppost);
					break;
				case GET_TASK:
					HttpGet httpget = new HttpGet(url);
					httpget.setHeader("Authorization", "Basic " + Base64.encodeToString((USER + ":" + PASSWORD).getBytes(), Base64.NO_WRAP));
					response = httpclient.execute(httpget);
					break;
				}
			} catch (Exception e) {
				// toast("ERROR: Could not retrieve data from server! (l:400)");
			}

			return response;
		}

		private String inputStreamToString(InputStream is) {

			String line = "";
			StringBuilder total = new StringBuilder();

			// Wrap a BufferedReader around the InputStream
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			try {
				// Read response until the end
				while ((line = rd.readLine()) != null) {
					total.append(line);
				}
			} catch (IOException e) {
				// toast("ERROR: Could not retrieve data from server! (l:420)");
			}

			// Return full string
			return total.toString();
		}
	}
}
