package apps4christ.android.nhicapp.main;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/* This class is used when you have a ListView of items that when clicked
 * should lead to a WebView.
 */
public class WebViewListListener implements OnItemClickListener {

	// List item's reference
	String[] listItems;
	// Calling activity reference
	Activity activity;
	
	public WebViewListListener(String[] aListItems, Activity anActivity) {
		listItems = aListItems;
		activity  = anActivity;
	}
	
	/**
	 * Start a WebView linking to a particular website without opening a browser.
	 */
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		String url;
		Intent i;
		
		i = new Intent(activity, JSoupActivity.class);
		assert(i != null);
		
		// Get the url for the podcast
		url = listItems[pos];
		
		i.setData(Uri.parse(url));
		
		activity.startActivity(i);
		
	}
	
}
