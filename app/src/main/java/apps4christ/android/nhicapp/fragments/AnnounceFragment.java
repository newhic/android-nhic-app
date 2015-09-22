package apps4christ.android.nhicapp.fragments;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import apps4christ.android.nhicapp.announcements.AnnounceAdapter;
import apps4christ.android.nhicapp.main.ConnectionDetector;
import apps4christ.android.nhicapp.podcast.ListListener;
import apps4christ.android.nhicapp.data.RssItem;
import apps4christ.android.nhicapp.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class AnnounceFragment extends Fragment {

	private static AnnounceAdapter announceAdapter;
	private static String urlStr = "http://www.newhic.org/feed/";
	private static View announceFragView;
	private static RssItem currentItem;
	private RSSTask task = null;
	private Tracker dbgTracker;
    private View spinnerView;

	// State that should be saved using Parcelable
	private List<RssItem> rssList;

	public AnnounceFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.announce_main, container,
				false);

		announceFragView = rootView;

        spinnerView = announceFragView
                .findViewById(R.id.announceloadingSpinner);

		/* Check for internet connectivity to avoid exceptions */
		ConnectionDetector cd = new ConnectionDetector(
				announceFragView.getContext());

		Boolean isInternetPresent = cd.isConnectingToInternet();

		/*
		 * Since Podcasts rely on internet connectivity, we only search for
		 * Podcasts when it is detected
		 */
		if (savedInstanceState != null && savedInstanceState.containsKey("rssList")) {
			List<Parcelable> parcelList = savedInstanceState.getParcelableArrayList("rssList");
			ArrayList<RssItem> rssList = new ArrayList();
			for (Parcelable p: parcelList) {
				rssList.add((RssItem) p);
			}
			renderFragment(rssList);
			spinnerView.setVisibility(View.GONE);
		} else if (isInternetPresent) {
			CreateRSSService();
		} else {
            cd.showAlertDialog();
            spinnerView.setVisibility(View.GONE);
        }

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

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (rssList != null) {
			ArrayList<RssItem> parcelableRssItems = new ArrayList(rssList);
			outState.putParcelableArrayList("rssList", parcelableRssItems);
		}
		super.onSaveInstanceState(outState);
	}

	private void CreateRSSService() {
		task = new RSSTask(this);
		assert (task != null);

		try {
			// Start download RSS task
			task.execute("http://www.newhic.org/feed/");

			// Debug the thread name
			Log.d("AnnounceRss", Thread.currentThread().getName());
		} catch (Exception e) {
			Log.e("AnnounceRss", e.getMessage());
		}
	}

	private static InputStream getInputStream(URL url) {
		try {
			return url.openConnection().getInputStream();
		} catch (IOException e) {
			return null;
		}
	}

	private void renderFragment(List<RssItem> result) {
		rssList = result;
		// Set reference to loading bar
		View progressBar = announceFragView
				.findViewById(R.id.announceloadingSpinner);
		// Hide the loading bar
		progressBar.setVisibility(View.GONE);

		if (rssList != null) {
			// Get a ListView from main view
			ListView nhicItems = (ListView) announceFragView
					.findViewById(R.id.announceListMainView);
			assert (nhicItems != null);
			// Create a list adapter
			announceAdapter = new AnnounceAdapter(getActivity(),
					R.layout.announcement_item_row, result);

			// Set list adapter for the ListView
			nhicItems.setAdapter(announceAdapter);
			// nhicItems.setTextFilterEnabled(true);

			// Set list view item click listener
			nhicItems.setOnItemClickListener(new ListListener(result,
					getActivity()));
		} else {
			new AlertDialog.Builder(this.getActivity())
					.setTitle(R.string.noInternetConnectionTitle)
					.setMessage(R.string.pleaseConnectToInternetMessage)
					.show();
		}
	}

	/*
	 * This routine is the way Android recommends us to parse RSS but at this
	 * point March 9, 2014 it seems to run very slow even for a short list. The
	 * SAX way seems faster
	 *
	 * TODO: Should probably extract this into a separate file
	 * TODO: Push this into a queue that is consumed by the view
	 *
	 */
	private static List<RssItem> XMLPullParserRoutine() {

		boolean insideItem = false;
		// Used to reference item while parsing

		try {
			ArrayList<RssItem> rssList = new ArrayList<>();
			URL url = new URL(urlStr);

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(false);
			XmlPullParser xpp = factory.newPullParser();

			// We will get the XML from an input stream
			InputStream response = getInputStream(url);
			if (response != null) {
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
							if (insideItem) {
								Date pubDate = null;
								String nextText = xpp.nextText();
								try {
									pubDate = DateUtils.parseDate(nextText);
								} catch (DateParseException e) {
									Log.e("AnnounceFragment", "Error in parsing date!");
								}
								currentItem.setPubDate(pubDate);
							}
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
				return rssList;
			} else {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(task != null) {
			if (!task.isCancelled()) {
				task.cancel(true);
			}
			task.parent = null;
		}
	}

	static class RSSTask extends AsyncTask<String, Void, List<RssItem>> {
		AnnounceFragment parent;

		public RSSTask(AnnounceFragment parent) {
			super();
			this.parent = parent;
		}

		@Override
		protected List<RssItem> doInBackground(String... urls) {
			return XMLPullParserRoutine();
		}

		@Override
		protected void onPostExecute(List<RssItem> result) {
			parent.renderFragment(result);
		}
	}
}
