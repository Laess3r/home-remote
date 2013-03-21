package at.dahuawa.homecontrol.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MediaStatus {
	
	public final static String STREAM_URL = "stream_url";

	private boolean playing; // if false -> stopped ;)
	private String information;

	public MediaStatus() {
		playing = false;
		information = "no info";
	}

	public MediaStatus(boolean playing, String information) {
		this.playing = playing;
		this.information = information;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}
}