package at.dahuawa.homecontrolapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import at.dahuawa.homecontrolsimple.R;

/**
 * Activity for sending urls to server
 * 
 * @author Stefan Huber
 */
public class MusicActivity extends Activity {

	EditText url;
	Button sendButton, stopButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music);

		url = (EditText) findViewById(R.id.urlEdit);

		sendButton = (Button) findViewById(R.id.sendButton);
		stopButton = (Button) findViewById(R.id.stopButton);

		sendButton.setOnClickListener(sendUrlListener);
		stopButton.setOnClickListener(stopPlaybackListener);
	}

	Button.OnClickListener sendUrlListener = new Button.OnClickListener() {

		public void onClick(View v) {

			Intent intent = new Intent();
			intent.putExtra("url", url.getText().toString());
			intent.putExtra("stopped", false);

			setResult(2, intent);
			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
			finish();
		}
	};

	Button.OnClickListener stopPlaybackListener = new Button.OnClickListener() {

		public void onClick(View v) {
			Intent intent = new Intent();

			intent.putExtra("stopped", true);
			intent.putExtra("url", "");

			setResult(2, intent);
			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
			finish();
		}
	};

}
