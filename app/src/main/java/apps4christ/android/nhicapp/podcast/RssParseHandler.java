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
		} else if ("title".equals(qName)) {
			parsingTitle = false;
		} else if ("enclosure".equals(qName)) {
			parsingEnclosure = false;
		} else if ("pubDate".equals(qName)) {
			parsingPubDate = false;
		} else if ("itunes:author".equals(qName)) {
			parsingAuthor = false;
		} else if ("itunes:duration".equals(qName)) {
			parsingDuration = false;
		} else if ("link".equals(qName)) {
			parsingLink = false;
		} else if ("description".equals(qName)) {
			parsingDesc = false;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		buf.append(ch, start, length);

		if (parsingTitle) {
			if (currentItem != null)
				currentItem.setTitle(buf.toString());
		} else if (parsingEnclosure) {
			if (currentItem != null) {
				currentItem.setEnclosure(buf.toString());
			}
		} else if (parsingPubDate) {
			if (currentItem != null) {
				Date pubDate = null;
				try {
					pubDate = DateUtils.parseDate(buf.toString());
				} catch (DateParseException e) {
					// Think this happens because the buffer flushed before a full date
					// could be parsed.
					Log.e("RssParseHandler", "Could not parse Date.");
				}
				if (pubDate != null) {
					currentItem.setPubDate(pubDate);
				}
			}
		} else if (parsingAuthor) {
			if (currentItem != null) {
				currentItem.setAuthor(buf.toString());
			}
		} else if (parsingDuration) {
			if (currentItem != null) {
				currentItem.setDuration(buf.toString());
			}
		} else if (parsingLink) {
			if (currentItem != null) {
				currentItem.setEnclosure(buf.toString());
			}
		} else if (parsingDesc) {
			if (currentItem != null) {
				currentItem.setContent(buf.toString());
			}
		}
	}

}