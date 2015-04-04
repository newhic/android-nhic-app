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

public class PodcastAdapter extends ArrayAdapter<RssItem> {

	private LayoutInflater inflater;
	private List<RssItem> rssItems;
	private List<RssItem> filteredData;

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
		// The super constructor mutates rssItems
		super(context, textViewResourceId, new ArrayList<RssItem>(rssItems));
		// TODO Auto-generated constructor stub
		inflater = ((Activity) context).getLayoutInflater();

		this.rssItems = new ArrayList<RssItem>(rssItems);

		this.filteredData = new ArrayList<RssItem>(rssItems);

	}

	@Override
	public Filter getFilter() {
		if (filter == null)
			filter = new PodcastFilter();

		return filter;
	}

	public void resetData(){
		filteredData = new ArrayList<>();
		if (rssItems != null) {
			filteredData.addAll(rssItems);
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.podcast_item_row, null);

			viewHolder = new ViewHolder();
			assert(viewHolder != null);

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

		viewHolder.titleView.setText(filteredData.get(position).getTitle());

		Date pubDate = filteredData.get(position).getPubDate();
		String pubDateString;
		if (pubDate != null) {
			pubDateString = DateFormat.getDateInstance().format(pubDate);
		} else {
			pubDateString = "";
			Log.e("PodcastAdapter", "Unable to find pubDate from rss data!");
		}
		viewHolder.dateView.setText(pubDateString);

		viewHolder.pastorView.setText(filteredData.get(position).getAuthor());
		viewHolder.durationView.setText(filteredData.get(position).getDuration());

		return convertView;
	}

	/*
		This filter class:

		1. Filters the data received from the rssItems field from the
		PodcastAdapter asynchronously (i.e. separate thread)

		2. *mutates* the filteredData attribute from PodcastAdapter
		to pass information back to PodcastAdapter for rendering
		purposes

		Other properties:
		Does NOT mutate rssItems
		Mutates filteredData
	 */
	private class PodcastFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {

			constraint = constraint.toString().toLowerCase(Locale.ENGLISH);
			FilterResults result = new FilterResults();
			assert(result != null);

			if (constraint != null && constraint.toString().length() > 0) {
				List<RssItem> filteredItems = new ArrayList<RssItem>();

				synchronized (this) {
					for (RssItem rssItem : rssItems) {
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
					result.values = new ArrayList<RssItem>(rssItems);
					result.count = rssItems.size();
				}
			}
			return result;
		}

		public boolean foundMatch(RssItem rssItem, CharSequence constraint) {
			String title;
			String author;
			Date pubDate;
			String pubDateString;

			title = rssItem.getTitle().toLowerCase(Locale.ENGLISH);
			pubDate = rssItem.getPubDate();
			pubDateString = DateFormat.getDateInstance().format(pubDate);

			if (title.contains(constraint))
				return true;

			author = rssItem.getAuthor();

			if (author != null) {
				author = author.toLowerCase(Locale.ENGLISH);
				if (author.contains(constraint))
					return true;
			} else
				Log.d("foundMatch", "author is " + author);

			return false;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			filteredData = (List<RssItem>) results.values;
			clear();
			addAll(filteredData);

			notifyDataSetChanged();
		}
	}

}
