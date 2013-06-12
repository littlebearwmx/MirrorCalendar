package edu.ustb.mirror.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class LikeBean implements Serializable {

	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	private int pid;
	private String title = "";
	private String description = "";
	private String date;

}
