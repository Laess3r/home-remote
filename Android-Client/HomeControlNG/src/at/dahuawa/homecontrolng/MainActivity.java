package at.dahuawa.homecontrolng;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import at.dahuawa.homecontrolng.communication.CommuinicationUtils;
import at.dahuawa.homecontrolng.communication.ConnectionData;
import at.dahuawa.homecontrolng.fragments.PlugsFragment;

public class MainActivity extends FragmentActivity {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	CommuinicationUtils utils = CommuinicationUtils.getInstance();
	
	PlugsFragment plugsFragement = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}
	
	@Override
	public void onRestart() {
		super.onRestart();

		if(plugsFragement != null){
			plugsFragement.loadData();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_settings:
			Intent settingsIntent = new Intent(this, SettingsActivity.class);
			startActivityForResult(settingsIntent, 0);
			finish();
			break;
		
		
	case R.id.menu_update:
		plugsFragement.setData(getConnectionData());
		plugsFragement.loadData();
		break;
		}
	
		return true;
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			if (position == 1) {
				plugsFragement = new PlugsFragment(getConnectionData());
				return plugsFragement;
			}

			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_dashboard).toUpperCase();
			case 1:
				return getString(R.string.title_plugs).toUpperCase();
			case 2:
				return getString(R.string.title_temps).toUpperCase();
			}
			return "TODO";
		}
	}

	public static class DummySectionFragment extends Fragment {

		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			textView.setText("Tab "+Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER))+ " \nCOMING SOON!");
			return textView;
		}
	}

	private ConnectionData getConnectionData() {

		ConnectionData data = new ConnectionData();

		SharedPreferences prefs = getSharedPreferences("SettingsActivity", MODE_PRIVATE);
		data.setServer_ip(prefs.getString(SettingsActivity.HOST_IP, ""));
		String port = prefs.getString(SettingsActivity.HOST_PORT, "0");
		data.setServer_port(Integer.parseInt(port.isEmpty() ? "0" : port));
		String timeout = prefs.getString(SettingsActivity.CONNECNTION_TIME_OUT, "0");
		data.setTimeout(Integer.parseInt(timeout.isEmpty() ? "0" : timeout));
		data.setUser(prefs.getString(SettingsActivity.USER_NAME, ""));
		data.setPassword(prefs.getString(SettingsActivity.USER_PASSWORD, ""));

		return data;
	}

}
