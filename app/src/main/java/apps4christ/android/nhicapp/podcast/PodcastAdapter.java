package apps4christ.android.nhicapp.podcast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import apps4christ.android.nhicapp.R;

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
	private List<RssItem> datas; // original list
	public List<RssItem> filteredData;
	private List<RssItem> copyDatas; // copied list used to rebuild after
										// canceling search
	public ArrayList<RssItem> filteredItems;
	private PodcastFilter filter;
	private Context context;

	static class ViewHolder {
		TextView titleView;
		TextView dateView;
		TextView pastorView;
		TextView durationView;
	}

	// Constructor for Podcast Adapter
	public PodcastAdapter(Context context, int textViewResourceId,
			List<RssItem> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		inflater = ((Activity) context).getLayoutInflater();
		datas = objects;

		this.filteredData = new ArrayList<RssItem>();
		this.filteredData.addAll(datas);

		this.copyDatas = new ArrayList<RssItem>();
		this.copyDatas.addAll(datas);
		this.context = context;
	}

	@Override
	public Filter getFilter() {
		if (filter == null)
			filter = new PodcastFilter();

		return filter;
	}

	public void resetData(){
		filteredData = datas;
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
			SimpleDateFormat df = new SimpleDateFormat(context.getString(R.string.dateFormat));
			pubDateString = df.format(pubDate);
		} else {
			pubDateString = "";
			Log.e("PodcastAdapter", "Unable to find pubDate from rss data!");
		}
		viewHolder.dateView.setText(pubDateString);

		viewHolder.pastorView.setText(filteredData.get(position).getAuthor());
		viewHolder.durationView.setText(filteredData.get(position).getDuration());

		return convertView;
	}

	private class PodcastFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {

			constraint = constraint.toString().toLowerCase(Locale.ENGLISH);
			FilterResults result = new FilterResults();
			assert(result != null);

			if (constraint != null && constraint.toString().length() > 0) {
				filteredItems = new ArrayList<RssItem>();

				for (int i = 0, l = copyDatas.size(); i < l; i++) {
					RssItem rssItem = copyDatas.get(i);

					// Check a match against the podcast title or speaker
					if (foundMatch(rssItem, constraint))
						filteredItems.add(rssItem);
				}
				result.count = filteredItems.size();
				result.values = filteredItems;
			} else {
				synchronized (this) {
					//Rebuild the list when search is cancelled.
					Log.d("PodcastAdapter", "search cancelled, rebuilding");
					result.values = copyDatas;
					result.count = copyDatas.size();
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
			} else
				Log.d("foundMatch", "author is " + author);

			return false;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {

			filteredData = (List<RssItem>) results.values;
			notifyDataSetChanged();
			clear();

			for (int i = 0, l = filteredData.size(); i < l; i++)
				add(filteredData.get(i));
			notifyDataSetInvalidated();
		}
	}

}
