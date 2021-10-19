package com.example.withschedule.schedule;

import java.io.Serializable;
import java.util.GregorianCalendar;

import android.os.Parcel;
import android.os.Parcelable;

public class Schedule implements Serializable,Parcelable{
	private static final long serialVersionUID = 2862024497387228434L;
	
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
	
	
	public Schedule(){
		
	}
	
	public Schedule(int sno, String title, String memo, GregorianCalendar startTime,
			int type, boolean isOpen,boolean isPublic) {
		super();
		this.no = sno;
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

	public boolean isOpen() {
		return isOpen;
	}

	public boolean isPublic() {
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

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	@Override
	public String toString() {
		return "Schedule [no=" + no + ", title=" + title + ", memo=" + memo
				+ ", startTime=" + startTime + ", endTime="  
				+ ", alramSet=" +  ", alramTime=" 
				+ ", type=" + type + ", isOpen=" + isOpen + ", isPublic="
				+ isPublic + "]";
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		// TODO Auto-generated method stub
		
	}

	
		
}
