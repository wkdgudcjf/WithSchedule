package com.example.withschedule.friend;

public class Friend 
{
	private String nickname;
	private String email;
	private String PhoneNo;
	public Friend()
	{
		;
	}
	public Friend(String nickname, String email, String phoneNo) 
	{
		this.nickname = nickname;
		this.email = email;
		this.PhoneNo = phoneNo;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNo() {
		return PhoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		PhoneNo = phoneNo;
	}
	
}
