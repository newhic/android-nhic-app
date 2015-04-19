package apps4christ.android.nhicapp.podcast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import apps4christ.android.nhicapp.R;
import apps4christ.android.nhicapp.data.RssItem;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Filter;

/**
 * PodcastAdapter transforms the RssItem objects into View objects to be
 * rendered.
 *
 * It does this by storing an array of RssItems (This is handled by
 * superclass ArrayAdapter). This store from ArrayAdapter is then modified
 * by PodcastFilter to publish results.
 */
public class PodcastAdapter extends ArrayAdapter<RssItem> {

	private LayoutInflater inflater;
	private List<RssItem> originalRssItems;

	private PodcastFilter filter;

	static class ViewHolder {
		TextView titleView;
		TextView dateView;
		TextView pastorView;
		TextView durationView;
	}

	// Constructor for Podcast Adapter
	public PodcastAdapter(Context context, int textViewResourceId,
			List<RssItem> rssItems) {
		// The super constructor mutates originalRssItems
		super(context, textViewResourceId, new ArrayList<>(rssItems));
		inflater = ((Activity) context).getLayoutInflater();
		this.originalRssItems = new ArrayList<>(rssItems);
	}

	@Override
	public Filter getFilter() {
		if (this.filter == null)
			this.filter = new PodcastFilter();

		return this.filter;
	}

	public void resetData(){
		this.clear();
		if (originalRssItems != null) {
			this.addAll(originalRssItems);
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.podcast_item_row, null);

			viewHolder = new ViewHolder();

			Log.d("PodcastAdapter", "Setting the variables");
			viewHolder.titleView = (TextView) convertView
					.findViewById(R.id.sermonTitle);
			viewHolder.dateView = (TextView) convertView
					.findViewById(R.id.podcastDate);
			viewHolder.pastorView = (TextView) convertView
					.findViewById(R.id.pastorName);
			viewHolder.durationView = (TextView) convertView
					.findViewById(R.id.duration);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.titleView.setText(this.getItem(position).getTitle());

		Date pubDate = this.getItem(position).getPubDate();
		String pubDateString;
		if (pubDate != null) {
			pubDateString = DateFormat.getDateInstance().format(pubDate);
		} else {
			pubDateString = "";
			Log.e("PodcastAdapter", "Unable to find pubDate from rss data!");
		}
		viewHolder.dateView.setText(pubDateString);

		viewHolder.pastorView.setText(this.getItem(position).getAuthor());
		viewHolder.durationView.setText(this.getItem(position).getDuration());

		return convertView;
	}

	/*
		This filter class:

		1. Filters the data received from the originalRssItems field from the
		PodcastAdapter asynchronously (i.e. separate thread)

		2. *mutates* the filteredData attribute from PodcastAdapter
		to pass information back to PodcastAdapter for rendering
		purposes

		Other properties:
		Does NOT mutate originalRssItems
		Mutates filteredData
	 */
	private class PodcastFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {

			constraint = constraint.toString().toLowerCase(Locale.ENGLISH);
			FilterResults result = new FilterResults();

			if (constraint.toString().length() > 0) {
				List<RssItem> filteredItems = new ArrayList<>();

				synchronized (this) {
					for (RssItem rssItem : originalRssItems) {
						if (foundMatch(rssItem, constraint)) {
							filteredItems.add(rssItem);
						}
					}
					result.count = filteredItems.size();
					result.values = filteredItems;
				}


			} else {
				synchronized (this) {
					//Rebuild the list when search is cancelled.
					Log.d("PodcastAdapter", "search cancelled, rebuilding");
					result.values = new ArrayList<>(originalRssItems);
					result.count = originalRssItems.size();
				}
			}
			return result;
		}

		public boolean foundMatch(RssItem rssItem, CharSequence constraint) {
			String title;
			String author;

			title = rssItem.getTitle().toLowerCase(Locale.ENGLISH);

			if (title.contains(constraint))
				return true;

			author = rssItem.getAuthor();

			if (author != null) {
				author = author.toLowerCase(Locale.ENGLISH);
				if (author.contains(constraint))
					return true;
			} else {
				Log.d("foundMatch", "author is null");
			}
			return false;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			clear();
			addAll((List<RssItem>) results.values);
			notifyDataSetChanged();
		}
	}

}
