package apps4christ.android.nhicapp.fragments;

import apps4christ.android.nhicapp.main.ConnectionDetector;
import apps4christ.android.nhicapp.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class GivingFragment extends Fragment {

	public GivingFragment() {
	}

	private View givingFragView;
	private WebView webView;
	private String url;
	private Tracker dbgTracker;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater
				.inflate(R.layout.giving_main, container, false);
		givingFragView = rootView;

		/* Check for internet connectivity to avoid exceptions */
		ConnectionDetector cd = new ConnectionDetector(
				givingFragView.getContext());

		Boolean isInternetPresent = cd.isConnectingToInternet(); // true or
																	// false

		if (isInternetPresent) {
			// CreateGetVideoService();
			webView = (WebView) givingFragView.findViewById(R.id.webview1);
			webView.getSettings().setJavaScriptEnabled(true);
			url = "http://www.newhic.org/amazon-online-giving/";
			webView.setWebViewClient(new WebViewClient());
			webView.loadUrl(url);
		} else {
			cd.showAlertDialog();
		}

		dbgTracker = GoogleAnalytics.getInstance(givingFragView.getContext())
				.newTracker("UA-51856239-1");

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();

		dbgTracker.setScreenName("Giving");
		dbgTracker.send(new HitBuilders.AppViewBuilder().build());

	}

}
