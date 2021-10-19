package dto;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ScheduleInfo {
	final static int GENERAL = 1;
	final static int WEEK_REPEAT = 2;
	final static int MONTH_LAST = 3;
	final static int MONTH_REPEAT = 4;
	final static int YEAR_REPEAT = 5;
	private int no;
	private String title;
	private String memo;
	private GregorianCalendar startTime;
	private int type;
	private boolean isOpen;
	private boolean isPublic;
	
	
	public ScheduleInfo(){
		
	}
	
	public ScheduleInfo(int no, String title, String memo, GregorianCalendar startTime,
			int type, boolean isOpen,boolean isPublic) {
		super();
		this.no = no;
		this.title = title;
		this.memo = memo;
		this.startTime = startTime;
		this.type = type;
		this.isOpen = isOpen;
		this.isPublic = isPublic;
	}
	
	public String getTime(){
		int year = this.startTime.get(Calendar.YEAR);
		int month = this.startTime.get(Calendar.MONTH);
		int day = this.startTime.get(Calendar.DAY_OF_MONTH);
		int hour = this.startTime.get(Calendar.HOUR_OF_DAY);
		int min = this.startTime.get(Calendar.MINUTE);
		return year+"/"+month+"/"+day+" "+hour+":"+min;
	}

	public ScheduleInfo(String title, String memo, GregorianCalendar startTime, int type,
			boolean isOpen, boolean isPublic) {
		super();
		this.title = title;
		this.memo = memo;
		this.startTime = startTime;
		this.type = type;
		this.isOpen = isOpen;
		this.isPublic = isPublic;
	}

	public int getNo() {
		return no;
	}

	public String getTitle() {
		return title;
	}

	public String getMemo() {
		return memo;
	}

	public GregorianCalendar getStartTime() {
		return startTime;
	}

	public int getType() {
		return type;
	}

	public boolean getIsOpen() {
		return isOpen;
	}

	public boolean getIsPublic() {
		return isPublic;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setStartTime(GregorianCalendar startTime) {
		this.startTime = startTime;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setIsOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	@Override
	public String toString() {
		return "ScheduleInfo [no=" + no + ", title=" + title + ", memo=" + memo
				+ ", startTime=" + startTime + ", endTime="  
				+ ", alramSet=" +  ", alramTime=" 
				+ ", type=" + type + ", isOpen=" + isOpen + ", isPublic="
				+ isPublic + "]";
	}
}
