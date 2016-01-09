package com.my.cloudcontact.bean;

import java.util.List;

public class CycleFriend {
	private String text;//内容
	private List<String> picture;//图片
	private String remarks;//备注
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<String> getPicture() {
		return picture;
	}
	public void setPicture(List<String> picture) {
		this.picture = picture;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
