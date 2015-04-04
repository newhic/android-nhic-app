package apps4christ.android.nhicapp.podcast;

import android.util.Log;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import apps4christ.android.nhicapp.data.RssItem;

public class RssParseHandler extends DefaultHandler {

	private List<RssItem> rssItems;

	// Used to reference item while parsing
	private RssItem currentItem;
	private StringBuilder buf;

	// Parsing title indicator
	private boolean parsingTitle;
	// Parsing enclosure indicator
	private boolean parsingEnclosure;
	private boolean parsingPubDate;
	private boolean parsingAuthor;
	private boolean parsingDuration;
	private boolean parsingLink;
	private boolean parsingDesc;

	public RssParseHandler() {
		rssItems = new ArrayList<RssItem>();
	}

	public List<RssItem> getItems() {
		return rssItems;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		buf = new StringBuilder();

		if ("item".equals(qName)) {
			currentItem = new RssItem();
		} else if ("title".equals(qName)) {
			parsingTitle = true;
		} else if ("enclosure".equals(qName)) {
			currentItem.setEnclosure(attributes.getValue("url"));
		} else if ("pubDate".equals(qName)) {
			parsingPubDate = true;
		} else if ("itunes:author".equals(qName)) {
			parsingAuthor = true;
		} else if ("itunes:duration".equals(qName)) {
			parsingDuration = true;
		} else if ("link".equals(qName)) {
			parsingLink = true;
		} else if ("description".equals(qName)) {
			parsingDesc = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if ("item".equals(qName)) {
			rssItems.add(currentItem);
			currentItem = null;
		}
		if (currentItem != null) {
			if ("title".equals(qName)) {
				currentItem.setTitle(buf.toString());
				parsingTitle = false;
			} else if ("enclosure".equals(qName)) {
				// Don't think this should be here.
				// currentItem.setEnclosure(buf.toString());
				parsingEnclosure = false;
			} else if ("pubDate".equals(qName)) {
				Date pubDate = null;
				try {
					String parseDateString = buf.toString();
					pubDate = DateUtils.parseDate(buf.toString());
				} catch (DateParseException e) {
					Log.e("RssParseHandler", "Could not parse Date!");
				}
				if (pubDate != null) {
					currentItem.setPubDate(pubDate);
				} else {
					Log.e("RssParseHandler", "Could not parse Date!");
				}
				parsingPubDate = false;
			} else if ("itunes:author".equals(qName)) {
				currentItem.setAuthor(buf.toString());
				parsingAuthor = false;
			} else if ("itunes:duration".equals(qName)) {
				currentItem.setDuration(buf.toString());
				parsingDuration = false;
			} else if ("link".equals(qName)) {
				currentItem.setEnclosure(buf.toString());
				parsingLink = false;
			} else if ("description".equals(qName)) {
				currentItem.setContent(buf.toString());
				parsingDesc = false;
			}
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		buf.append(ch, start, length);
	}

}