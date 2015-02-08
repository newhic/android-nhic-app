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

	public void CreateGetVideoService() {
		GetVideoTask task = new GetVideoTask();

		assert (task != null);

		// Start download RSS task
		task.execute();

		// Debug the thread name
		Log.d("NHICRssReader", Thread.currentThread().getName());
	}

	// This is the XML onClick listener to retreive a users video feed
	public void getUserYouTubeFeed(View v) {
		// We start a new task that does its work on its own thread
		// We pass in a handler that will be called when the task has finished
		// We also pass in the name of the user we are searching YouTube for
		new ParseVideos(responseHandler, "NewHopeIC").run();
	}

	// This is the handler that receives the response when the YouTube task has
	// finished
	Handler responseHandler = new Handler() {
		public void handleMessage(Message msg) {
			populateListWithVideos(msg);
		};
	};

	/**
	 * This method retrieves the Library of videos from the task and passes them
	 * to our ListView. This method is commented out as grabbing a list of
	 * videos does not seem useful at this point
	 * 
	 * @param msg
	 */
	private void populateListWithVideos(Message msg) {
		// // Retrieve the videos are task found from the data bundle sent back
		// Library lib = (Library) msg.getData().get(ParseVideos.LIBRARY);
		// // Because we have created a custom ListView we don't have to worry
		// about setting the adapter in the activity
		// // we can just call our custom method with the list of items we want
		// to display
		//
		// List<YTVid> videoList;
		// ListView nhicItems = (ListView) videosFragView
		// .findViewById(R.id.videosListView);
		// assert (nhicItems != null);
		//
		// videoList = lib.getVideos();
		//
		// // Create a list adapter
		// videoAdapter = new VideoAdapter(getActivity(),
		// R.layout.videos_item_row, videoList);
		//
		// Log.d("onPostExecute", "Created the videoadapter");
		//
		// // Set list adapter for the ListView
		// nhicItems.setAdapter(videoAdapter);
		//
		// // Set list view item click listener
		// nhicItems.setOnItemClickListener(new VideoListener(videoList,
		// getActivity()));
	}

	private class GetVideoTask extends AsyncTask<String, Void, List<YTVid>> {

		@Override
		protected List<YTVid> doInBackground(String... urls) {

			getUserYouTubeFeed(videosFragView);

			return null;
		}

		@Override
		protected void onPostExecute(List<YTVid> result) {

		}
	}

}
