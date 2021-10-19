package com.example.withschedule.dto;

public class InvitationInfo {
	private long no;
	private String sender;
	public InvitationInfo(long no, String sender) {
		super();
		this.no = no;
		this.sender = sender;
	}
	public long getNo() {
		return no;
	}
	public String getSender() {
		return sender;
	}
	public void setNo(long no) {
		this.no = no;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	@Override
	public String toString() {
		return "Entry [no=" + no + ", sender=" + sender + "]";
	}
	
	
}
