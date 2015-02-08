package apps4christ.android.nhicapp.videos;

import java.util.List;

import apps4christ.android.nhicapp.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class VideoAdapter extends ArrayAdapter<YTVid> {
	private LayoutInflater inflater;
	private List<YTVid> datas;

	static class ViewHolder {
		TextView titleView;
		TextView dateView;
		TextView urlView;
	}

	// Constructor for Video Adapter
	public VideoAdapter(Context context, int textViewResourceId,
			List<YTVid> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		inflater = ((Activity) context).getLayoutInflater();
		datas = objects;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		Log.d("getView", "Calling getView");

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.videos_item_row, null);

			viewHolder = new ViewHolder();
			assert (viewHolder != null);

			Log.d("VideoAdapter", "Setting the title");
			viewHolder.titleView = (TextView) convertView
					.findViewById(R.id.videoTitle);
			
			viewHolder.dateView = (TextView) convertView
					.findViewById(R.id.videoDate);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.titleView.setText(datas.get(position).getVidTitle());
		viewHolder.dateView.setText(datas.get(position).getDate().toString());
		//viewHolder.urlView.setText(datas.get(position).getVidUrl());

		return convertView;
	}

	public int getCount() {
		return datas.size();
	}

}
