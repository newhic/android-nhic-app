package apps4christ.android.nhicapp.videos;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class VideoListener implements OnItemClickListener {
	// List item's reference
		List<YTVid> listItems;
		// Calling activity reference
		Activity activity;
		
		public VideoListener(List<YTVid> aListItems, Activity anActivity) {
			listItems = aListItems;
			activity  = anActivity;
		}
		
		/**
		 * Start a browser with url from the rss item.
		 */
		public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
			String url;
			Intent i = new Intent(Intent.ACTION_VIEW);
			assert(i != null);
			
			// Get the url for the podcast
			url = listItems.get(pos).getVidUrl();
			
			i.setData(Uri.parse(url));
		
			activity.startActivity(i);
			
		}

}
