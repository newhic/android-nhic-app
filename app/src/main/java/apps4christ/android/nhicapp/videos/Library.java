package apps4christ.android.nhicapp.videos;

import java.io.Serializable;
import java.util.List;

/**
 * This is the 'library' of all the users videos
 * 
 */
public class Library implements Serializable{
	// The username of the owner of the library
	private String user;
	// A list of videos that the user owns
	private List<YTVid> videos;
	
	public Library(String user, List<YTVid> videos) {
		this.user = user;
		this.videos = videos;
	}

	/**
	 * @return the user name
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @return the videos
	 */
	public List<YTVid> getVideos() {
		return videos;
	}
}