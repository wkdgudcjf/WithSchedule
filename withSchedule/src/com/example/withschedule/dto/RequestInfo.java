package com.example.withschedule.dto;

public class RequestInfo {
	private int no;
	private String title;
	private String senderEmail;
	private String senderName;
	private String receiverEmail;
	private String receiverName;
	private int type;
	public RequestInfo(int no, String title, String senderEmail,
			String senderName, String receiverEmail, String receiverName,
			int type) {
		super();
		this.no = no;
		this.title = title;
		this.senderEmail = senderEmail;
		this.senderName = senderName;
		this.receiverEmail = receiverEmail;
		this.receiverName = receiverName;
		this.type = type;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSenderEmail() {
		return senderEmail;
	}
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getReceiverEmail() {
		return receiverEmail;
	}
	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "RequestInfo [no=" + no + ", title=" + title + ", senderEmail="
				+ senderEmail + ", senderName=" + senderName
				+ ", receiverEmail=" + receiverEmail + ", receiverName="
				+ receiverName + ", type=" + type + "]";
	}
	
	
}
