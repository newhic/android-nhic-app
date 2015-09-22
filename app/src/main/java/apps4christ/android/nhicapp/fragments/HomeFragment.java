package apps4christ.android.nhicapp.fragments;

import java.util.ArrayList;

import apps4christ.android.nhicapp.main.WebViewListListener;
import apps4christ.android.nhicapp.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.List;
import java.util.Arrays;
import java.lang.String;

public class HomeFragment extends Fragment {

	private View homeFragView;

	// Variables for nearby category view
	private ListView valuesListView;
	private ArrayList<String> valuesList;
	private ArrayAdapter<String> arrayAdapter;
	private Tracker dbgTracker;

	public HomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.home_main, container, false);
		homeFragView = rootView;

		// Initialize the list of nearby places
		valuesListView = (ListView) homeFragView.findViewById(R.id.valuesList);

		populateValuesList();

		arrayAdapter = new ArrayAdapter<>(getActivity(),
				android.R.layout.simple_list_item_1, valuesList);

		// Set The Adapter
		valuesListView.setAdapter(arrayAdapter);

		// Set list view item click listener
		valuesListView.setOnItemClickListener(new WebViewListListener(
				homeFragView.getResources().getStringArray(
						R.array.homepage_links_array), getActivity()));

        String trackerId =  getResources().getString(R.string.trackingId);

		dbgTracker = GoogleAnalytics.getInstance(homeFragView.getContext())
				.newTracker(trackerId);

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();

		dbgTracker.setScreenName("Home");
		dbgTracker.send(new HitBuilders.AppViewBuilder().build());

	}

	private void populateValuesList() {
        String[] array;
        List<String> list;

        array = getResources().getStringArray(R.array.home_page_values);
        list = Arrays.asList(array);
        valuesList = new ArrayList<>(list);
	}

}