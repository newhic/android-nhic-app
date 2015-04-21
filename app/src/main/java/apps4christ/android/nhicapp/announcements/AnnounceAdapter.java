package apps4christ.android.nhicapp.announcements;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import apps4christ.android.nhicapp.data.RssItem;
import apps4christ.android.nhicapp.R;

public class AnnounceAdapter extends ArrayAdapter<RssItem> {
	
	private LayoutInflater inflater;
	private List<RssItem> datas;
	private Context context;
	
	static class ViewHolder {
		TextView titleView;
		TextView dateView;
		TextView descView;
	}
	
	// Constructor for Announcement Adapter
	public AnnounceAdapter(Context context, int textViewResourceId,
			List<RssItem> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		inflater = ((Activity) context).getLayoutInflater();
		datas = objects;
		this.context = context;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.announcement_item_row, null);

			viewHolder = new ViewHolder();
			assert(viewHolder != null);
			
			viewHolder.titleView = (TextView) convertView
					.findViewById(R.id.announcementTitle);
			viewHolder.dateView = (TextView) convertView
					.findViewById(R.id.announcementDate);
			viewHolder.descView = (TextView) convertView
					.findViewById(R.id.announcementDesc);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.titleView.setText(datas.get(position).getTitle());

		Date pubDate = datas.get(position).getPubDate();
		String pubDateString;
		if (pubDate != null) {
			SimpleDateFormat df = new SimpleDateFormat(context.getString(R.string.dateFormat));
			pubDateString = df.format(pubDate);
		} else {
			pubDateString = "";
			Log.e("AnnounceAdapter", "Error in parsing date!");
		}
		viewHolder.dateView.setText(pubDateString);
		
		viewHolder.descView.setText(datas.get(position).getAuthor());

		return convertView;
	}

}
