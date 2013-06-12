package edu.ustb.mirror.bean;

import java.io.Serializable;

public class FeedBean implements Serializable {

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getEmotion() {
		return emotion;
	}

	public void setEmotion(int emotion) {
		this.emotion = emotion;
	}

	private String content;
	private String date;
	private int emotion;

}
