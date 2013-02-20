package at.dahuawa.homecontrolng.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import at.dahu4wa.homecontrol.model.TimedTask;
import at.dahu4wa.homecontrol.model.TimedTask.TimerType;
import at.dahuawa.homecontrolng.R;
import at.dahuawa.homecontrolng.communication.HomeControlProps;
import at.dahuawa.homecontrolng.communication.WebServiceTask;
import at.dahuawa.homecontrolng.communication.WebServiceTaskFinishedCallback;

public class TimingFragment extends Fragment {
	public static final String PATH_TO_TIMING = "/HomeBase/timing/";
	
	// ONLY DRAFT !

	private HomeControlProps data;

	Button submit;
	EditText relTime;
	boolean statusToSet = false;

	public TimingFragment(HomeControlProps data) {
		this.data = data;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_timing, container, false);

		relTime = (EditText) view.findViewById(R.id.relText);
		submit = (Button) view.findViewById(R.id.submit);

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Long relVal = Long.parseLong(relTime.getText().toString());
				TimedTask t = new TimedTask(null, TimerType.RELATIVE_TIME, relVal, 'D', statusToSet);
				postTimedTask(t);
				statusToSet = !statusToSet;
			}
		});

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@SuppressLint("UseValueOf")
	public void postTimedTask(TimedTask timedTask) {

		String postUrl = "http://" + data.getServer_ip() + ":" + data.getServer_port() + PATH_TO_TIMING;

		String postText = "Posting timer";

		WebServiceTask wst = new WebServiceTask(WebServiceTask.POST_TASK, getActivity(), postText, data, callback);

		wst.addNameValuePair(TimedTask.UID, timedTask.getTime().toString());
		wst.addNameValuePair(TimedTask.PLUG, new Character(timedTask.getPlug()).toString());
		wst.addNameValuePair(TimedTask.FINISHED, new Boolean(timedTask.isFinished()).toString());
		wst.addNameValuePair(TimedTask.STATUSTOSET, new Boolean(timedTask.isStatusToSet()).toString());
		wst.addNameValuePair(TimedTask.TIME, timedTask.getTime().toString());
		wst.addNameValuePair(TimedTask.TIMERTYPE, timedTask.getTimerType().toString());

		wst.execute(new String[] { postUrl });
	}

	WebServiceTaskFinishedCallback callback = new WebServiceTaskFinishedCallback() {

		@Override
		public void handleResponse(String response) {
			Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
		}

		@Override
		public boolean useEnhancedLogging() {
			return data.isDebugMode();
		}
	};

	public void setData(HomeControlProps data) {
		this.data = data;
	}

}
