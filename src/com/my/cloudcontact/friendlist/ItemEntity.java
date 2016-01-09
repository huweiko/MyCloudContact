package com.my.cloudcontact.friendlist;

import java.util.ArrayList;

public class ItemEntity {
	
	private String content; // 内容
	private String time; // 时间
	private String memo; // 备注
	private ArrayList<String> imageUrls; // 九宫格图片的URL集合

	public ItemEntity(String time, String memo, String content, ArrayList<String> imageUrls) {
		super();
		this.time = time;
		this.memo = memo;
		this.content = content;
		this.imageUrls = imageUrls;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public String getMemo() {
		return memo;
	}


	public void setMemo(String memo) {
		this.memo = memo;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ArrayList<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(ArrayList<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

}
