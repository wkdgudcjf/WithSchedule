package dto;

import java.util.GregorianCalendar;

public class CommentInfo {
	private int no;
	private int memberId;
	private String content;
	private GregorianCalendar date;
	public CommentInfo(int no, int memberId, String content,
			GregorianCalendar date) {
		super();
		this.no = no;
		this.memberId = memberId;
		this.content = content;
		this.date = date;
	}
	public int getNo() {
		return no;
	}
	public int getMemberId() {
		return memberId;
	}
	public String getContent() {
		return content;
	}
	public GregorianCalendar getDate() {
		return date;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setDate(GregorianCalendar date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "CommentInfo [no=" + no + ", memberId=" + memberId
				+ ", content=" + content + ", date=" + date + "]";
	}
	
}
