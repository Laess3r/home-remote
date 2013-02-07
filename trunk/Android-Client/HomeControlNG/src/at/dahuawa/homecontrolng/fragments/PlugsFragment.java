package at.dahuawa.homecontrolng.fragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import at.dahuawa.homecontrolng.R;
import at.dahuawa.homecontrolng.communication.ConnectionData;
import at.dahuawa.homecontrolng.communication.WebServiceTask;
import at.dahuawa.homecontrolng.communication.WebServiceTaskFinishedCallback;
import at.dahuawa.homecontrolng.model.Plug;

public class PlugsFragment extends Fragment {
	public static final String PATH_TO_PLUG = "/HomeBase/plug/";

	// TODO make a List of this shit!
	Switch button_A;
	Switch button_B;
	Switch button_C;
	Switch button_D;
	Switch button_E;
	Switch button_X;

	private ConnectionData data;
	//private List<Plug> plugs = new ArrayList<Plug>();

	public PlugsFragment(ConnectionData data) {
		this.data = data;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_plugs, container, false);
		
		button_A = (Switch) view.findViewById(R.id.buttonA);
		button_B = (Switch) view.findViewById(R.id.buttonB);
		button_C = (Switch) view.findViewById(R.id.buttonC);
		button_D = (Switch) view.findViewById(R.id.buttonD);
		button_E = (Switch) view.findViewById(R.id.buttonE);
		button_X = (Switch) view.findViewById(R.id.buttonX);

		setButtonListeners();
		
		loadData();
		
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();



	}
	
	WebServiceTaskFinishedCallback callback = new WebServiceTaskFinishedCallback() {
		
		@Override
		public void handleResponse(String response) {
			
			if (response.startsWith("{\"plug\":[{\"")) {
				handleArrayPlugResponse(response);
			} else {
				handleSinglePlugResponse(response);
			}
			
		}
	};
	
	public void handleSinglePlugResponse(String singleResponse) {
		try {
			JSONObject jso = new JSONObject(singleResponse);
			handleSinglePlugResponse(jso);

		} catch (Exception e) {
			 toast("ERROR: Could not read single response! ");
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
			toast("ERROR: Could not read array response!");
		}
	}
	
	private void handleSinglePlugResponse(JSONObject jso) throws JSONException {

		char id = (char) Integer.parseInt(jso.getString("id"));
		boolean enabled = jso.getBoolean("enabled");
		String name = jso.getString("name");

		Plug received = new Plug(id, name, enabled);

		findPlugAndUpdateButtons(received);
	}

	public void loadData() {
		
		String allPlugsUrl = "http://" + data.getServer_ip() + ":" + data.getServer_port() + PATH_TO_PLUG + "all";
		WebServiceTask wst = new WebServiceTask(WebServiceTask.GET_TASK, getActivity(), "Getting plugs from server ...", data, callback);
		wst.execute(new String[] { allPlugsUrl });
	}
	
	@SuppressLint("UseValueOf")
	public void postPlug(Plug plug) {

		String postUrl = "http://" + data.getServer_ip() + ":" + data.getServer_port() + PATH_TO_PLUG;

		String postText = plug.isEnabled() ? "Turning ON" : "Turning OFF";

		WebServiceTask wst = new WebServiceTask(WebServiceTask.POST_TASK, getActivity(), postText, data, callback);

		wst.addNameValuePair("id", new Character(plug.getId()).toString());
		wst.addNameValuePair("name", plug.getName());
		wst.addNameValuePair("enabled", new Boolean(plug.isEnabled()).toString());

		wst.execute(new String[] { postUrl });
	}
	
	

	public ConnectionData getData() {
		return data;
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

		button_E.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				togglePlug(button_E, 'E');
			}

		});

		button_X.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				togglePlug(button_X, 'X');
			}

		});
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
		case 'E':
			button_E.setText(plug.getName());
			button_E.setChecked(plug.isEnabled());
			button_E.setEnabled(true);
			break;
		case 'X':
			button_X.setText(plug.getName());
			button_X.setChecked(plug.isEnabled());
			button_X.setEnabled(true);
			break;
		default:
			toast("PLUG ID not found: " + plug.getId());
			break;
		}
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
		Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
	}
	
	private AlertDialog buildDialog(final CompoundButton button, final char plugId) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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

	public void setData(ConnectionData data) {
		this.data = data;
	}

}
