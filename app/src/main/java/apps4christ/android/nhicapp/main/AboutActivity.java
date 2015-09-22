package apps4christ.android.nhicapp.main;

import apps4christ.android.nhicapp.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class AboutActivity extends Activity {
	private TextView aboutDev;
	private TextView aboutApp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set view
		setContentView(R.layout.about_page);
		
		aboutDev = (TextView) findViewById(R.id.aboutPageDeveloperText);
		aboutDev.setMovementMethod(new ScrollingMovementMethod());

		aboutApp = (TextView) findViewById(R.id.aboutPageAppText);
		aboutApp.setMovementMethod(new ScrollingMovementMethod());
		
	}

}
