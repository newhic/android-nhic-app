package apps4christ.android.nhicapp.podcast;

import java.util.List;

import apps4christ.android.nhicapp.main.WebActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ListListener implements OnItemClickListener {

	// List item's reference
	List<RssItem> listItems;
	// Calling activity reference
	Activity activity;

	public ListListener(List<RssItem> aListItems, Activity anActivity) {
		listItems = aListItems;
		activity = anActivity;
	}

	/**
	 * Start a browser with url from the rss item.
	 */
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		String url;
		Intent i;

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			// only for KitKat and newer versions ( >= than 4.4)
			i = new Intent(activity, WebActivity.class);
		} else {
			i = new Intent(Intent.ACTION_VIEW);
		}
		assert (i != null);

		// Get the url for the podcast
		url = listItems.get(pos).getEnclosure();

		i.setData(Uri.parse(url));

		activity.startActivity(i);

	}

}
