package at.dahuawa.homecontrolapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import at.dahuawa.homecontrolsimple.R;

/**
 * Activity for maintenance of settings
 * 
 * @author Stefan Huber
 */
public class SettingsActivity extends Activity {

	EditText ipEdit, portEdit, user, password;
	Button saveButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		ipEdit = (EditText) findViewById(R.id.ipEditText);
		portEdit = (EditText) findViewById(R.id.portEditText);
		user = (EditText) findViewById(R.id.userEdit);
		password = (EditText) findViewById(R.id.passwordEdit);

		saveButton = (Button) findViewById(R.id.saveButton);

		saveButton.setOnClickListener(saveSettingsListener);
		getSettingsAndSetText();
	}

	Button.OnClickListener saveSettingsListener = new Button.OnClickListener() {

		public void onClick(View v) {
			Intent intent = new Intent();

			intent.putExtra("ip", ipEdit.getText().toString());
			intent.putExtra("port", Integer.parseInt(portEdit.getText().toString()));
			intent.putExtra("user", user.getText().toString());
			intent.putExtra("password", password.getText().toString());

			setResult(1, intent);
			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
			finish();
		}
	};

	private void getSettingsAndSetText() {

		Bundle bundle = getIntent().getExtras();

		String loadedIp = bundle.getString("ip");
		int loadedPort = bundle.getInt("port");
		String userName = bundle.getString("user");
		String passwrd = bundle.getString("password");
		ipEdit.setText(loadedIp);
		portEdit.setText(Integer.toString(loadedPort));
		user.setText(userName);
		password.setText(passwrd);
	}
}
