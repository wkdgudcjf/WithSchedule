package com.example.withschedule.alarm;

import java.io.Serializable;

public class Alarm implements Serializable{
	
	private static final long serialVersionUID = -6821635771174973852L;
	private int no;
	private boolean setalarm;
	private int alarmtime;

	public Alarm(int no,boolean setalarm,int alarmtime)
	{
		this.no=no;
		this.setalarm=setalarm;
		this.alarmtime=alarmtime;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public boolean isSetalarm() {
		return setalarm;
	}
	public void setSetalarm(boolean setalarm) {
		this.setalarm = setalarm;
	}
	public int getAlarmtime() {
		return alarmtime;
	}
	public void setAlarmtime(int alarmtime) {
		this.alarmtime = alarmtime;
	}
}
