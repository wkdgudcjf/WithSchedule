package com.example.withschedule.alarm;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.example.withschedule.AppManager;
import com.example.withschedule.CalendarActivity;
import com.example.withschedule.db.ExcuteDB;


public class AlarmManegement {
	private AlarmList alarmList;
	public AlarmManegement()
	{
		alarmList = new AlarmList();
	}
	
	public AlarmList getAlarmList() {
		return alarmList;
	}
	public void setAlarmList(AlarmList scheduleList) {
		this.alarmList = scheduleList;
	}
	public void enroll(Alarm alarm)
	{
		this.alarmList.add(alarm);
	}
	public void cancel(int no)
	{
		for(int i=0;i<this.alarmList.getList().size();i++)
		{
			if(this.alarmList.getList().get(i).getNo()==no)
			{
				this.alarmList.getList().remove(i);
			}
		}
	}
	public Alarm search(int no)
	{
		for(int i=0;i<this.alarmList.getList().size();i++)
		{
			if(this.alarmList.getList().get(i).getNo()==no)
			{
				return this.alarmList.getList().get(i);
			}
		}
		return null;
	}
	
	public boolean modify(int no,int time,boolean set)
	{
		for(int i=0;i<this.alarmList.getList().size();i++)
		{
			if(this.alarmList.getList().get(i).getNo()==no)
			{
				this.alarmList.getList().get(i).setAlarmtime(time);
				this.alarmList.getList().get(i).setSetalarm(set);
				return true;
			}
		}
		return false;
	}

	public void clear(CalendarActivity calendarActivity) 
	{
		GregorianCalendar gc;
		for(int i=0;i<this.alarmList.getList().size();i++)
		{
			gc = AppManager.getInstance().getSm().search(this.alarmList.getList().get(i).getNo()).getStartTime();
			gc.set(Calendar.MINUTE, gc.get(Calendar.MINUTE)-this.alarmList.getList().get(i).getAlarmtime());
			if(Calendar.getInstance().compareTo(gc)>=0&&AppManager.getInstance().getSm().search(this.alarmList.getList().get(i).getNo()).getType()==1)
			{
				ExcuteDB.deleteAlarm(calendarActivity, this.alarmList.getList().get(i).getNo());
			}
		}
	}
	public void allRemove()
	{
		int size = this.alarmList.getList().size();
		for(int i=0;i<size;i++)
		{
			this.alarmList.getList().remove(0);
		}
	}


}
