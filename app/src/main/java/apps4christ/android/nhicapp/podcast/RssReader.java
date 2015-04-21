package apps4christ.android.nhicapp.podcast;

import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import apps4christ.android.nhicapp.data.RssItem;

public class RssReader {
	
	private String rssUrl;

	/**
	 * Constructor
	 * 
	 * @param rssUrl
	 */
	public RssReader(String rssUrl) {
		this.rssUrl = rssUrl;
	}

	/**
	 * Get RSS items.
	 * 
	 * Unfortunately this is not the Google recommended way of parsing RSS feeds. That
	 * way can be found in the Announcement section of this app. However this does provide
	 * fast access, but sometimes inconsistent result. 
	 * 
	 * Will switch to XMLPullParser way of doing things in the future. 
	 * @return
	 */
	public List<RssItem> getItems() throws Exception {
		// SAX parse RSS data
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		RssParseHandler handler = new RssParseHandler();
		assert(handler != null);
		
		saxParser.parse(rssUrl, handler);

		return handler.getItems();
		
	}

}