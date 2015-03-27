package apps4christ.android.nhicapp.fragments;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import apps4christ.android.nhicapp.announcements.AnnounceAdapter;
import apps4christ.android.nhicapp.main.ConnectionDetector;
import apps4christ.android.nhicapp.podcast.ListListener;
import apps4christ.android.nhicapp.podcast.RssItem;
import apps4christ.android.nhicapp.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class AnnounceFragment extends Fragment {

	private static AnnounceAdapter announceAdapter;
	private static String urlStr = "http://www.newhic.org/feed/";
	private static View announceFragView;
	private static ArrayList<RssItem> rssList;
	private static RssItem currentItem;
	private RSSTask task = null;
	private Tracker dbgTracker;

	public AnnounceFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.announce_main, container,
				false);

		announceFragView = rootView;

		rssList = new ArrayList<RssItem>();
		/* Check for internet connectivity to avoid exceptions */
		ConnectionDetector cd = new ConnectionDetector(
				announceFragView.getContext());

		Boolean isInternetPresent = cd.isConnectingToInternet();

		/*
		 * Since Podcasts rely on internet connectivity, we only search for
		 * Podcasts when it is detected
		 */
		if (isInternetPresent)
			CreateRSSService();
		else
			cd.showAlertDialog();

        String trackerId =  getResources().getString(R.string.trackingId);

		dbgTracker = GoogleAnalytics.getInstance(announceFragView.getContext())
				.newTracker(trackerId);

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();

		dbgTracker.setScreenName("Announcements");
		dbgTracker.send(new HitBuilders.AppViewBuilder().build());
	}

	public void CreateRSSService() {
		task = new RSSTask();
		assert (task != null);

		task.m_activity = getActivity();

		try {
			// Start download RSS task
			task.execute("http://www.newhic.org/feed/");

			// Debug the thread name
			Log.d("AnnounceRss", Thread.currentThread().getName());
		} catch (Exception e) {
			Log.e("AnnounceRss", e.getMessage());
		}
	}

	public static InputStream getInputStream(URL url) {
		try {
			return url.openConnection().getInputStream();
		} catch (IOException e) {
			return null;
		}
	}

	/*
	 * This routine is the way Android recommends us to parse RSS but at this
	 * point March 9, 2014 it seems to run very slow even for a short list. The
	 * SAX way seems faster
	 */
	public static void XMLPullParserRoutine() {
		boolean insideItem = false;
		// Used to reference item while parsing

		try {
			URL url = new URL(urlStr);

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(false);
			XmlPullParser xpp = factory.newPullParser();

			// We will get the XML from an input stream
			xpp.setInput(getInputStream(url), "UTF_8");

			// Returns the type of current event: START_TAG, END_TAG, etc..
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {

					if (xpp.getName().equalsIgnoreCase("item")) {
						currentItem = new RssItem();
						rssList.add(currentItem);
						insideItem = true;

					} else if (xpp.getName().equalsIgnoreCase("title")) {
						if (insideItem)
							currentItem.setTitle(xpp.nextText());
					} else if (xpp.getName().equalsIgnoreCase("description")) {
						if (insideItem)
							currentItem.setContent(xpp.nextText());
					} else if (xpp.getName().equalsIgnoreCase("pubDate")) {
						if (insideItem)
							currentItem.setPubDate(xpp.nextText());
					} else if (xpp.getName().equalsIgnoreCase("link")) {
						if (insideItem)
							currentItem.setEnclosure(xpp.nextText());
					}
				} else if (eventType == XmlPullParser.END_TAG
						&& xpp.getName().equalsIgnoreCase("item")) {
					insideItem = false;
				}

				eventType = xpp.next(); // move to next element
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		task.m_activity = null;
	}

	static class RSSTask extends AsyncTask<String, Void, List<RssItem>> {

		Activity m_activity = null;
		private View progressBar;

		@Override
		protected void onPreExecute() {
			// Set reference to loading bar
			progressBar = (View) announceFragView
					.findViewById(R.id.announceloadingSpinner);
		}

		@Override
		protected List<RssItem> doInBackground(String... urls) {

			XMLPullParserRoutine();

			return null;
		}

		@Override
		protected void onPostExecute(List<RssItem> result) {
			// Get a ListView from main view

			if (m_activity != null) {
				// Hide the loading bar
				progressBar.setVisibility(View.GONE);

				result = rssList;

				ListView nhicItems = (ListView) announceFragView
						.findViewById(R.id.announceListMainView);
				assert (nhicItems != null);

				// Create a list adapter
				announceAdapter = new AnnounceAdapter(m_activity,
						R.layout.announcement_item_row, result);

				// Set list adapter for the ListView
				nhicItems.setAdapter(announceAdapter);
				// nhicItems.setTextFilterEnabled(true);

				// Set list view item click listener
				nhicItems.setOnItemClickListener(new ListListener(result,
						m_activity));
			}

		}
	}
}
