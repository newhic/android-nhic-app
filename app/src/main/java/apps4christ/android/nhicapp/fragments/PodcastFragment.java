package apps4christ.android.nhicapp.fragments;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.app.ActionBar.OnNavigationListener;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar.*;

import apps4christ.android.nhicapp.main.ConnectionDetector;
import apps4christ.android.nhicapp.podcast.ListListener;
import apps4christ.android.nhicapp.podcast.PodcastAdapter;
import apps4christ.android.nhicapp.podcast.RssItem;
import apps4christ.android.nhicapp.podcast.RssReader;
import apps4christ.android.nhicapp.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/* This code represents the Podcast page of the NHIC App
 */
public class PodcastFragment extends Fragment {

	private static PodcastAdapter podcastAdapter;
	private EditText inputSearch;
	private static View podcastFragView;
	private String RssUrl;
	private Boolean isInternetPresent;
	private ArrayAdapter<String> langSel;
	private ActionBar bar;
	private RSSTask task = null;
	private Tracker dbgTracker;
    private View spinnerView;

	public PodcastFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.podcast_main, container,
				false);
		podcastFragView = rootView;

        spinnerView = (View) podcastFragView
                .findViewById(R.id.podcastloadingSpinner);

		/* Check for internet connectivity to avoid exceptions */
		ConnectionDetector cd = new ConnectionDetector(
				podcastFragView.getContext());

		isInternetPresent = cd.isConnectingToInternet(); // true or
		// false

		if (isInternetPresent) {
            CreateLanguageDropDown();
			EnableSearch();
		} else {
			cd.showAlertDialog();
            spinnerView.setVisibility(View.GONE);
		}

        String trackerId =  getResources().getString(R.string.trackingId);

		dbgTracker = GoogleAnalytics.getInstance(podcastFragView.getContext())
				.newTracker(trackerId);

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();

		dbgTracker.setScreenName("Podcasts");
		dbgTracker.send(new HitBuilders.AppViewBuilder().build());
	}

	/*
	 * Create an array adapter to populate DropDown list to select language for
	 * Podcast. This is also where the initial podcast RSS service is made.
	 */
	public void CreateLanguageDropDown() {

        SharedPreferences prefs;
        String strPos;
        int position;

        bar = getActivity().getActionBar();

        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        langSel = new ArrayAdapter<String>(bar.getThemedContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources()
                .getStringArray(R.array.podcast_lang_select));

		/* Defining Navigation listener */
        ActionBar.OnNavigationListener navigationListener = new OnNavigationListener() {

            @Override
            public boolean onNavigationItemSelected(int itemPosition,
                                                    long itemId) {
                String[] menu;

                menu = getResources().getStringArray(
                        R.array.podcast_lang_url_sel);

                RssUrl = menu[itemPosition];

                if (isInternetPresent) {
                    CreateRSSService(RssUrl);
                }

                return false;
            }
        };

        /**
         * Setting DropDown items and item navigation listener for the ActionBar
         */
        bar.setListNavigationCallbacks(langSel, navigationListener);

        // Set the default position of our language selector bar


        prefs = PreferenceManager.getDefaultSharedPreferences(podcastFragView
                .getContext());
        strPos = prefs.getString("podcastLanguage", "0");
        position = Integer.parseInt(strPos);
        bar.setSelectedNavigationItem(position);
    }

	/*
	 * Enable the search bar of the NHIC podcast fragment
	 * 
	 * Enable the EditText inputSearch in podcastFragView.xml by hooking up an
	 * addTextChangedListener property to it. The addTextChangedListener
	 * executes the getFilter() routine of the podcastAdapter.
	 */
	public void EnableSearch() {
		inputSearch = (EditText) podcastFragView.findViewById(R.id.inputSearch);

		assert (inputSearch != null);

		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int start, int before,
					int count) {

				if (count < before) {
					// On backspace.
					Log.d("counttracker", "count is < before");
					PodcastFragment.this.podcastAdapter.resetData();
				}

				// When user changed the Text
				PodcastFragment.this.podcastAdapter.getFilter().filter(cs);

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	/*
	 * Create the RSS Service task
	 * 
	 * This is a wrapper routine used to execute the asynchronous thread in
	 * charge of calling the RSS feed parser.
	 */
	public void CreateRSSService(String url) {

		task = new RSSTask();

		task.m_activity = getActivity();

		// Execute the GetRssDataTask using the following url
		task.execute(url);

		// Debug the thread name
		Log.d("PodcastRss", Thread.currentThread().getName());
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
			// Set References to loading bar
			progressBar = (View) podcastFragView
					.findViewById(R.id.podcastloadingSpinner);
		}

		@Override
		protected List<RssItem> doInBackground(String... urls) {

			// Debug the task thread name
			Log.d("GetRSSDataTask", Thread.currentThread().getName());

			try {
				// Create RSS reader
				RssReader rssReader = new RssReader(urls[0]);
				assert (rssReader != null);

				// Parse RSS, get items

				return rssReader.getItems();

			} catch (Exception e) {
				Log.e("GetRSSDataTask", e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<RssItem> result) {

			if (m_activity != null) {
				// Hide loading bar
				progressBar.setVisibility(View.GONE);

				// Get a ListView from main view
				ListView nhicItems = (ListView) podcastFragView
						.findViewById(R.id.listMainView);
				assert (nhicItems != null);

				// Create a list adapter
				podcastAdapter = new PodcastAdapter(m_activity,
						R.layout.podcast_item_row, result);

				// Set list adapter for the ListView
				nhicItems.setAdapter(podcastAdapter);
				// Allow the nhicItems listView to be text filtered
				nhicItems.setTextFilterEnabled(true);

				// Set list view item click listener
				nhicItems.setOnItemClickListener(new ListListener(result,
						m_activity));

			}

		}
	}

}
