package apps4christ.android.nhicapp.fragments;

import java.util.List;

import apps4christ.android.nhicapp.main.ConnectionDetector;
import apps4christ.android.nhicapp.videos.ParseVideos;
import apps4christ.android.nhicapp.videos.YTVid;
import apps4christ.android.nhicapp.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class VideosFragment extends Fragment {
	public VideosFragment() {
	}

	// private VideoAdapter videoAdapter;
	private View videosFragView;
	private WebView webView;
	private String url;
	private Tracker dbgTracker;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater
				.inflate(R.layout.videos_main, container, false);
		videosFragView = rootView;

		/* Check for internet connectivity to avoid exceptions */
		ConnectionDetector cd = new ConnectionDetector(
				videosFragView.getContext());

		Boolean isInternetPresent = cd.isConnectingToInternet(); // true or
																	// false

		/*
		 * The NHIC YouTube site does not allow you to sort, so we will just
		 * have a WebView pop up
		 */
		if (isInternetPresent) {
			// CreateGetVideoService();
			webView = (WebView) videosFragView.findViewById(R.id.webView1);
			webView.getSettings().setJavaScriptEnabled(true);
			url = "http://www.youtube.com/user/NewHopeIC";
			webView.setWebViewClient(new WebViewClient());
			webView.loadUrl(url);
		} else {
			cd.showAlertDialog();
		}

		dbgTracker = GoogleAnalytics.getInstance(videosFragView.getContext())
				.newTracker("UA-51856239-1");

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();

		dbgTracker.setScreenName("Videos");
		dbgTracker.send(new HitBuilders.AppViewBuilder().build());
	}

}
