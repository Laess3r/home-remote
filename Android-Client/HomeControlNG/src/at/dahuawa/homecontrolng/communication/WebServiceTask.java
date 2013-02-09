package at.dahuawa.homecontrolng.communication;

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

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;

public class WebServiceTask extends AsyncTask<String, Integer, String> {

	private final HomeControlProps connectionData;
	private final WebServiceTaskFinishedCallback callback;

	public static final int POST_TASK = 1;
	public static final int GET_TASK = 2;

	private int taskType = GET_TASK;
	private Context mContext = null;
	private String processMessage = "DEFINE MESSAGE ! ";

	private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

	private ProgressDialog pDlg = null;

	public WebServiceTask(int taskType, Context mContext, String processMessage, HomeControlProps connectionData,
			WebServiceTaskFinishedCallback callback) {

		this.connectionData = connectionData;
		this.taskType = taskType;
		this.mContext = mContext;
		this.processMessage = processMessage;
		this.callback = callback;
	}

	public void addNameValuePair(String name, String value) {

		params.add(new BasicNameValuePair(name, value));
	}

	@SuppressWarnings("deprecation")
	private void showProgressDialog() {

		if (mContext != null) {
			pDlg = new ProgressDialog(mContext);
			pDlg.setMessage(processMessage);
			pDlg.setProgressDrawable(mContext.getWallpaper());
			pDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDlg.setCancelable(false);
			pDlg.show();
		}

	}

	@Override
	protected void onPreExecute() {
		if(callback.useEnhancedLogging())
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
			}
		}

		return result;
	}

	@Override
	protected void onPostExecute(String response) {
		callback.handleResponse(response);
		if (pDlg != null)
			pDlg.dismiss();
	}

	private HttpParams getHttpParams() {

		HttpParams htpp = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(htpp, connectionData.getTimeout() * 1000);
		HttpConnectionParams.setSoTimeout(htpp, connectionData.getTimeout() * 5000);

		return htpp;
	}

	private HttpResponse doResponse(String url) {

		HttpClient httpclient = new DefaultHttpClient(getHttpParams());
		HttpResponse response = null;

		try {
			switch (taskType) {

			case POST_TASK:
				HttpPost httppost = new HttpPost(url);

				httppost.setHeader(
						"Authorization",
						"Basic "
								+ Base64.encodeToString((connectionData.getUser() + ":" + connectionData.getPassword()).getBytes(),
										Base64.NO_WRAP));

				// Add parameters
				httppost.setEntity(new UrlEncodedFormEntity(params));

				response = httpclient.execute(httppost);
				break;
			case GET_TASK:
				HttpGet httpget = new HttpGet(url);
				httpget.setHeader(
						"Authorization",
						"Basic "
								+ Base64.encodeToString((connectionData.getUser() + ":" + connectionData.getPassword()).getBytes(),
										Base64.NO_WRAP));
				response = httpclient.execute(httpget);
				break;
			}
		} catch (Exception e) {
			
			e.printStackTrace();
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