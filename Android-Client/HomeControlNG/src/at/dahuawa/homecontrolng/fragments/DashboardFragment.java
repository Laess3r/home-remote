package at.dahuawa.homecontrolng.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import at.dahuawa.homecontrolng.R;
import at.dahuawa.homecontrolng.communication.HomeControlProps;

public class DashboardFragment extends Fragment {

	private HomeControlProps data;

	public DashboardFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		data = (HomeControlProps) getArguments().getSerializable(
				"connectionData");

		View view = inflater.inflate(R.layout.fragment_dashboard, container,
				false);

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();

	}

}
