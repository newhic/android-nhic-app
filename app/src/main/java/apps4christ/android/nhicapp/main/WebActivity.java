package apps4christ.android.nhicapp.main;

import apps4christ.android.nhicapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;

public class WebActivity extends Activity {
	private WebView webView;
	private String url;
	private Intent intent;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_webview);
		
		intent = getIntent();
		url = intent.getDataString();
		
		webView = (WebView) findViewById(R.id.webview1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(url);
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // TODO Auto-generated method stub
	    if(keyCode==event.KEYCODE_BACK)
	    {
	        webView.loadUrl("");
	        webView.stopLoading();

	        finish();

	    }
	    return super.onKeyDown(keyCode, event);
	}
	
}
