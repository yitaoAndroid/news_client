package com.example.mynews.beans;

import java.io.Serializable;

public class News implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5673466762899908759L;
	private String title;
	private String message;
	private String imgUri;
	private String bigImageUrl;
	private String time;
	private String detailUri;
	private String description;

	public News() {

	}


	public News(String title, String message, String imgUri,
			String bigImageUrl, String time, String detailUri,
			String description) {
		super();
		this.title = title;
		this.message = message;
		this.imgUri = imgUri;
		this.bigImageUrl = bigImageUrl;
		this.time = time;
		this.detailUri = detailUri;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getImgUri() {
		return imgUri;
	}

	public void setImgUri(String imgUri) {
		this.imgUri = imgUri;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDetailUri() {
		return detailUri;
	}

	public void setDetailUri(String detailUri) {
		this.detailUri = detailUri;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

	public String getBigImageUrl() {
		return bigImageUrl;
	}


	public void setBigImageUrl(String bigImageUrl) {
		this.bigImageUrl = bigImageUrl;
	}


	@Override
	public String toString() {
		return "News [title=" + title + ", message=" + message + ", imgUri="
				+ imgUri + ", bigImageUrl=" + bigImageUrl + ", time=" + time
				+ ", detailUri=" + detailUri + ", description=" + description
				+ "]";
	}

}
