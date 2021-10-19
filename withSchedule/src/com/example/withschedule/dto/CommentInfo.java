package com.example.withschedule.dto;

import java.util.GregorianCalendar;

public class CommentInfo {
	private int cno;
	private String comment;
	private String writerName;
	private String writerEmail;
	private GregorianCalendar date;

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public GregorianCalendar getDate() {
		return date;
	}
	public void setDate(GregorianCalendar date) {
		this.date = date;
	}
	public int getCno() {
		return cno;
	}
	public void setCno(int cno) {
		this.cno = cno;
	}
	public String getWriterName() {
		return writerName;
	}
	public void setWriterName(String writerName) {
		this.writerName = writerName;
	}
	public String getWriterEmail() {
		return writerEmail;
	}
	public void setWriterEmail(String writerEmail) {
		this.writerEmail = writerEmail;
	}
	public CommentInfo(int cno, String comment, String writerName,
			String writerEmail, GregorianCalendar date) {
		super();
		this.cno = cno;
		this.comment = comment;
		this.writerName = writerName;
		this.writerEmail = writerEmail;
		this.date = date;
	}


}
