package at.dahuawa.homecontrolapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import at.dahuawa.homecontrolsimple.R;

public class LogActivity extends Activity {
	
	EditText file;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logfile);

		file = (EditText) findViewById(R.id.log);

		file.setText("Coming soon!");
	}

}
