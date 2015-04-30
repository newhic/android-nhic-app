package apps4christ.android.nhicapp.podcast;

import java.util.List;

import apps4christ.android.nhicapp.audioplayer.AudioPlayerActivity;
import apps4christ.android.nhicapp.data.RssItem;
import apps4christ.android.nhicapp.main.JSoupActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;

public class ListListener implements OnClickListener {

	// List item's reference
	RssItem listItem;
	// Calling activity reference
	Activity activity;

	public ListListener(RssItem listItem, Activity anActivity) {
		this.listItem = listItem;
		activity = anActivity;
	}

	/**
	 * Start a browser or a media player with url from the rss item.
	 */

	public void onClick(View view) {
		String url;
        String podcastTitle;
		Intent i;

        url = listItem.getEnclosure();

        if(url.contains(".mp3")) {
            i = new Intent(activity, AudioPlayerActivity.class);
            // Get the url for the podcast
            url = listItem.getEnclosure();
            podcastTitle = listItem.getTitle();

            i.putExtra("url", url);
            i.putExtra("title", podcastTitle);
        }else {
            i = new Intent(activity, JSoupActivity.class);
            url = listItem.getEnclosure();
            i.setData(Uri.parse(url));
        }

		activity.startActivity(i);

	}

}
