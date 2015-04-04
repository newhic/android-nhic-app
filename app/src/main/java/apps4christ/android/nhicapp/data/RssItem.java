package apps4christ.android.nhicapp.data;

/* This class stores the contents extracted from the NHIC Rss Feed. */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class RssItem implements Parcelable {
	private String title;
	private String enclosure; /* link, url */
	private Date pubDate;
	private String author;
	private String duration;
	private String content;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getPubDate() {
		return pubDate;
	}
	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEnclosure() {
		return enclosure;
	}
	public void setEnclosure(String enclosure) {
		this.enclosure = enclosure;
	}
	
	@Override
	public String toString() {
		return title;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(enclosure);
		dest.writeLong(pubDate.getTime());
		dest.writeString(author);
		dest.writeString(duration);
		dest.writeString(content);
	}

	public static final Parcelable.Creator<RssItem> CREATOR
			= new Parcelable.Creator<RssItem>() {
		public RssItem createFromParcel(Parcel in) {
			RssItem item = new RssItem();
			item.enclosure = in.readString();
			item.title = in.readString();
			item.pubDate = new Date(in.readLong());
			item.author = in.readString();
			item.duration = in.readString();
			item.content = in.readString();
			return item;
		}

		public RssItem[] newArray(int size) {
			return new RssItem[size];
		}
	};

}
