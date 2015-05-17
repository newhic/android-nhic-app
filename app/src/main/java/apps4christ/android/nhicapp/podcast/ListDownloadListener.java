package apps4christ.android.nhicapp.podcast;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;

import apps4christ.android.nhicapp.R;
import apps4christ.android.nhicapp.audioplayer.AudioPlayerActivity;
import apps4christ.android.nhicapp.data.RssItem;
import apps4christ.android.nhicapp.main.JSoupActivity;

public class ListDownloadListener implements OnClickListener {

	// List item's reference
	private RssItem listItem;
	// Calling activity reference
	private Activity activity;
	private ArrayAdapter<RssItem> rssAdapter;

	public ListDownloadListener(RssItem listItem, Activity activity, ArrayAdapter<RssItem> rssAdapter) {
		this.listItem = listItem;
		this.activity = activity;
		this.rssAdapter = rssAdapter;
	}

	/**
	 * Start a browser or a media player with url from the rss item.
	 */

	public void onClick(View view) {
		String url;
		String podcastTitle;
		Intent i;

		// Get the url for the podcast

		url = listItem.getEnclosure();

		DownloadManager dm = (DownloadManager) this.activity.getSystemService(Context.DOWNLOAD_SERVICE);
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

		//Restrict the types of networks over which this download may proceed.
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
		// TODO: Add | DownloadManager.Request.NETWORK_MOBILE
		//Set whether this download may proceed over a roaming connection.
		request.setAllowedOverRoaming(false);
		//Set the title of this download, to be displayed in notifications (if enabled).
		request.setTitle(listItem.getTitle());
		//Set a description of this download, to be displayed in notifications (if enabled)
		request.setDescription("Downloading podcast...");
		//Set the local destination for the downloaded file to a path within the application's external files directory
		String fileName = listItem.getTitle(); // TODO: Gotta parse the filename

		if (canWriteExternal()) {
			request.setDestinationInExternalFilesDir(this.activity, Environment.DIRECTORY_DOWNLOADS, fileName);
		} else {
			new AlertDialog.Builder((Context) this.activity)
					.setTitle("SD Card error")
					.setMessage("Your SD card is not mounted!")
					.show();
		}



		IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		final long downloadReference = dm.enqueue(request);
		this.activity.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);
				if (id == downloadReference) {
					// Download complete! We need to the list
					// that the dataset has changed since the download button
					// needs to be suppressed
					rssAdapter.notifyDataSetChanged();
				}
			}
		}, filter);



	}

	private boolean canWriteExternal() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		} else {
			//TODO: Handle Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)
			return false;
		}
	}

}
