package com.example.withschedule.dto;



public class MemberInfo {
	private String email;
	private String phoneNum;
	private String name;
	
	public MemberInfo(String phoneNum, String email, String name) {
		super();
		this.email = email;
		this.phoneNum = phoneNum;
		this.name = name;
	}

	
	
	public String getEmail() {
		return email;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public String getName() {
		return name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "MemberInfo [email=" + email + ", phoneNum=" + phoneNum
				+ ", name=" + name + "]";
	}
	
}
