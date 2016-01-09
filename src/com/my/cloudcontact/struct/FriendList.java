package com.my.cloudcontact.struct;

public class FriendList {
	private String imgurl; // 用户头像URL
	private String memo; // 备注
	private String currtime; // 时间
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getCurrtime() {
		return currtime;
	}
	public void setCurrtime(String currtime) {
		this.currtime = currtime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	private String content; // 内容
}
