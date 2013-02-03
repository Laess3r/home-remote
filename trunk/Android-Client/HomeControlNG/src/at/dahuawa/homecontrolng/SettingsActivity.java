package at.dahuawa.homecontrolng;

import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

/**
 * HomeControlNG settings activity, based on android PreferenceActivity
 * 
 * @author DaHu4wA
 */
public class SettingsActivity extends PreferenceActivity {
	private static final boolean ALWAYS_SIMPLE_PREFS = false;

	public static final String HOST_IP = "host_ip";
	public static final String HOST_PORT = "host_port";

	public static final String USER_NAME = "user_name";
	public static final String USER_PASSWORD = "user_password";

	public static final String ENHANCED_LOGGING = "enhanced_logging";
	public static final String CONNECNTION_TIME_OUT = "connection_time_out";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupActionBar();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		setupSimplePreferencesScreen();
	}

	@SuppressWarnings("deprecation")
	private void setupSimplePreferencesScreen() {
		if (!isSimplePreferences(this)) {
			return;
		}

		addPreferencesFromResource(R.xml.pref_general);
		
		PreferenceCategory header = new PreferenceCategory(this);
		header.setTitle(R.string.pref_header_host);
		getPreferenceScreen().addPreference(header);
		addPreferencesFromResource(R.xml.pref_host);

		header = new PreferenceCategory(this);
		header.setTitle(R.string.pref_header_user);
		getPreferenceScreen().addPreference(header);
		addPreferencesFromResource(R.xml.pref_user);

		header = new PreferenceCategory(this);
		header.setTitle(R.string.pref_header_expert);
		getPreferenceScreen().addPreference(header);
		addPreferencesFromResource(R.xml.pref_expert);

		bindPreferenceSummaryToValue(findPreference(HOST_IP));
		bindPreferenceSummaryToValue(findPreference(HOST_PORT));
		bindPreferenceSummaryToValue(findPreference(USER_NAME));
		bindPreferenceSummaryToValue(findPreference(USER_PASSWORD));
		bindPreferenceSummaryToValue(findPreference(ENHANCED_LOGGING));
		bindPreferenceSummaryToValue(findPreference(CONNECNTION_TIME_OUT));
	}

	@Override
	public boolean onIsMultiPane() {
		return isXLargeTablet(this) && !isSimplePreferences(this);
	}

	/**
	 * Helper method to determine if the device has an extra-large screen. For
	 * example, 10" tablets are extra-large.
	 */
	private static boolean isXLargeTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	private static boolean isSimplePreferences(Context context) {
		return ALWAYS_SIMPLE_PREFS || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB || !isXLargeTablet(context);
	}

	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onBuildHeaders(List<Header> target) {
		if (!isSimplePreferences(this)) {
			loadHeadersFromResource(R.xml.pref_headers, target);
		}
	}

	private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();

			if (preference instanceof ListPreference) {
				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(stringValue);

				preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

			} else {
				preference.setSummary(stringValue);
			}
			return true;
		}
	};

	/**
	 * Binds a preference's summary to its value. More specifically, when the
	 * preference's value is changed, its summary (line of text below the
	 * preference title) is updated to reflect the value. The summary is also
	 * immediately updated upon calling this method. The exact display format is
	 * dependent on the type of preference.
	 * 
	 * @see #sBindPreferenceSummaryToValueListener
	 */
	private static void bindPreferenceSummaryToValue(Preference preference) {
		preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

		sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
				PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class HostPreferenceFragmemt extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_host);

			bindPreferenceSummaryToValue(findPreference(HOST_IP));
			bindPreferenceSummaryToValue(findPreference(HOST_PORT));
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class UserPreferenceFragmemt extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_user);

			bindPreferenceSummaryToValue(findPreference(USER_NAME));
			bindPreferenceSummaryToValue(findPreference(USER_PASSWORD));
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class ExpertPreferenceFragmemt extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_expert);

			bindPreferenceSummaryToValue(findPreference(ENHANCED_LOGGING));
			bindPreferenceSummaryToValue(findPreference(CONNECNTION_TIME_OUT));
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
}
