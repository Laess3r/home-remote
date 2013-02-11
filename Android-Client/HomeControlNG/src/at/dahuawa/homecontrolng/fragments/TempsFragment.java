package at.dahuawa.homecontrolng.fragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import at.dahuawa.homecontrolng.R;
import at.dahuawa.homecontrolng.communication.HomeControlProps;
import at.dahuawa.homecontrolng.communication.WebServiceTask;
import at.dahuawa.homecontrolng.communication.WebServiceTaskFinishedCallback;
import at.dahuawa.homecontrolng.model.TempSensor;

public class TempsFragment extends Fragment {
	private static final String PATH_TO_TEMPSENSOR = "/HomeBase/tempsensor/";

	// TODO make a List of this shit!
	TextView tempA;
	TextView tempB;

	private HomeControlProps data;

	public TempsFragment(HomeControlProps data) {
		this.data = data;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_temps, container, false);

		tempA = (TextView) view.findViewById(R.id.tempA);
		tempB = (TextView) view.findViewById(R.id.tempB);

		loadData(); // TODO only do this when view is selected

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	WebServiceTaskFinishedCallback callback = new WebServiceTaskFinishedCallback() {

		@Override
		public void handleResponse(String response) {
			toastError(response);

			if (response.startsWith("{\"tempSensor\":[{\"")) {
				handleArrayTempSensorResponse(response);
			} else {
				handleSingleTempSensorResponse(response);
			}
		}

		@Override
		public boolean useEnhancedLogging() {
			return data.isDebugMode();
		}
	};

	public void handleArrayTempSensorResponse(String arrayResponse) {

		try {
			JSONObject json = new JSONObject(arrayResponse);

			JSONArray array = json.getJSONArray("tempSensor");

			for (int i = 0; i < array.length(); i++) {
				handleSingleTempSensorResponse(array.getJSONObject(i));
			}
		} catch (Exception e) {
			toastError("ERROR: Could not read array response!");
		}
	}

	public void handleSingleTempSensorResponse(String singleResponse) {
		try {
			JSONObject jso = new JSONObject(singleResponse);
			handleSingleTempSensorResponse(jso);

		} catch (Exception e) {
			toastError("ERROR: Could not read single response!");
		}
	}

	private void handleSingleTempSensorResponse(JSONObject jso) throws JSONException {

		char id = (char) Integer.parseInt(jso.getString("id"));
		String temp = jso.getString("tempValue");
		String name = jso.getString("name");

		TempSensor received = new TempSensor(id, name, Float.parseFloat(temp));

		updateTemp(received);
	}

	private void updateTemp(TempSensor received) {
		switch (received.getId()) {
		case 'A':
			tempA.setText(received.getName() + ":					" + received.getTempValue() + "°C");
			break;
		case 'B':
			tempB.setText(received.getName() + ":					" + received.getTempValue() + "°C");
			break;
		default:
			break;
		}
	}

	public void loadData() {

		String allPlugsUrl = "http://" + data.getServer_ip() + ":" + data.getServer_port() + PATH_TO_TEMPSENSOR + "all";
		WebServiceTask wst = new WebServiceTask(WebServiceTask.GET_TASK, getActivity(), "Getting plugs from server ...", data, callback);
		wst.execute(new String[] { allPlugsUrl });
	}

	private void toastError(String message) {
		if (data.isDebugMode())
			Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
	}

	public void setData(HomeControlProps data) {
		this.data = data;
	}

}
