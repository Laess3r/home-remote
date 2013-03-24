package at.dahuawa.homecontrolng.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import at.dahuawa.homecontrolng.R;
import at.dahuawa.homecontrolng.communication.HomeControlProps;
import at.dahuawa.homecontrolng.communication.WebServiceTask;
import at.dahuawa.homecontrolng.communication.WebServiceTaskFinishedCallback;
import at.dahuawa.homecontrolng.model.LogEntry;
import at.dahuawa.homecontrolng.model.LogFile;

public class LogsFragment extends Fragment {
	private static final String PATH_TO_LOG = "/HomeBase/log/";

	TextView textInput;
	Button loadButton;

	private HomeControlProps data;

	public LogsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		data = (HomeControlProps) getArguments().getSerializable("connectionData");

		View view = inflater.inflate(R.layout.fragment_logs, container, false);

		textInput = (TextView) view.findViewById(R.id.logView);
		loadButton = (Button) view.findViewById(R.id.loadButton);

		loadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadData();
			}
		});

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

			if (response.startsWith("{\"logEntry\":[{")) {

				LogFile logs = new LogFile();

				JSONObject json;
				try {
					json = new JSONObject(response);
					JSONArray array = json.getJSONArray("logEntry");

					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonObject = array.getJSONObject(i);

						String message = jsonObject.getString("text");
						String timestamp = jsonObject.getString("timeStamp");

						logs.getLogFile().add(new LogEntry(message, timestamp));
					}
					textInput.setText(logs.toString());
				} catch (Exception e) {
					toastError(e.toString());
				}
			}
		}

		@Override
		public boolean useEnhancedLogging() {
			return data.isDebugMode();
		}
	};

	public void loadData() {

		String logFileUrl = "http://" + data.getServer_ip() + ":" + data.getServer_port() + PATH_TO_LOG + "all";
		WebServiceTask wst = new WebServiceTask(WebServiceTask.GET_TASK, getActivity(), "Getting LOGS from server ...", data, callback);
		wst.execute(new String[] { logFileUrl });
	}

	private void toastError(String message) {
		if (data.isDebugMode())
			Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
	}

	public void setData(HomeControlProps data) {
		this.data = data;
	}

}
