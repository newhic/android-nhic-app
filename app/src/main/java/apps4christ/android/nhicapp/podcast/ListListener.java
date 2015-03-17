package apps4christ.android.nhicapp.podcast;

import java.util.List;

import apps4christ.android.nhicapp.audioplayer.AudioPlayerActivity;
import apps4christ.android.nhicapp.main.JSoupActivity;

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
	 * Start a browser or a media player with url from the rss item.
	 */
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		String url;
        String podcastTitle;
		Intent i;

        url = listItems.get(pos).getEnclosure();

        if(url.contains(".mp3")) {
            i = new Intent(activity, AudioPlayerActivity.class);
            // Get the url for the podcast
            url = listItems.get(pos).getEnclosure();
            podcastTitle = listItems.get(pos).getTitle();

            i.putExtra("url", url);
            i.putExtra("title", podcastTitle);
        }else {
            i = new Intent(activity, JSoupActivity.class);
            url = listItems.get(pos).getEnclosure();
            i.setData(Uri.parse(url));
        }

		activity.startActivity(i);

	}

}
