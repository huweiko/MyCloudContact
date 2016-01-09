package com.my.cloudcontact;

import com.my.cloudcontact.bean.AccountInfo;

public class ContactsInfo {

	private AccountInfo accountInfo;
	private String id;
	private String mobile;
	private String name;
	private long rawContactId;
	private String address;

	public void setName(String name) {
		this.name = name;
	}

	

	public String getId() {
		return id;
	}

	public AccountInfo getAccountInfo() {
		return accountInfo;
	}

	public String getName() {
		return name;
	}

	public String getMobile() {
		return mobile;
	}



	public void setMobile(String mobile) {
		this.mobile = mobile;
	}



	public long getRawContactId() {
		return rawContactId;
	}



	public String getAddress() {
		return address;
	}



	public void setId(String id) {
		this.id = id;
	}



	public void setAccountInfo(AccountInfo info) {
		this.accountInfo = info;
	}

	public void setRawContactId(long uid) {
		this.rawContactId = uid;
	}

	public long getRaw_contact_id() {
		return this.rawContactId;
	}

	public void setAddress(String string) { 
		this.address = string;
	}

}
