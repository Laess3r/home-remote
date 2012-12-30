package at.dahuawa.homecontrolapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import at.dahuawa.homecontrolapp.model.LogFile;
import at.dahuawa.homecontrolsimple.R;

public class LogActivity extends Activity {
	
	TextView file;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logfile);

		file = (TextView) findViewById(R.id.logtext);

		Bundle bundle = getIntent().getExtras();

		LogFile log = (LogFile) bundle.getSerializable("Log");
		
		file.setText(log.toString());
	}

}
