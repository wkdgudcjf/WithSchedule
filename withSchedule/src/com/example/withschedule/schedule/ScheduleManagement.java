package com.example.withschedule.schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ScheduleManagement {
	private ScheduleList scheduleList;
	private ScheduleList friendSchedule;
	public ScheduleManagement()
	{
		scheduleList = new ScheduleList();
	}
	
	public ScheduleList getFriendSchedule() {
		return friendSchedule;
	}

	public void setFriendSchedule(ScheduleList friendSchedule) {
		this.friendSchedule = friendSchedule;
	}

	public ScheduleList getScheduleList() {
		return scheduleList;
	}
	
	public void setScheduleList(ScheduleList scheduleList) {
		this.scheduleList = scheduleList;
	}
	public ArrayList<Schedule> getFriendScheduleOfDay(int year,int month,int day)
	{
		ArrayList<Schedule> alist = new ArrayList<Schedule>();
		GregorianCalendar calendar = new GregorianCalendar(year,month,day,23,59,59);
		for(Schedule schedule:this.friendSchedule.getList()){
			switch(schedule.getType()){
			case 1:
				if(schedule.getStartTime().get(Calendar.YEAR)==year 
				&& schedule.getStartTime().get(Calendar.MONTH)==month 
				&&schedule.getStartTime().get(Calendar.DAY_OF_MONTH)==day)
				{ 
					alist.add(schedule);
				}
				break;
			case 2: 				
				if(schedule.getStartTime().get(Calendar.DAY_OF_WEEK)==calendar.get(Calendar.DAY_OF_WEEK)
						&&schedule.getStartTime().getTimeInMillis()<=calendar.getTimeInMillis())
				{ 
					alist.add(schedule);
				}
				break;
			case 3: 
				GregorianCalendar temp2= (GregorianCalendar) schedule.getStartTime().clone();
				temp2.set(Calendar.MONTH, month);
				if(temp2.getActualMaximum(Calendar.DAY_OF_MONTH)==day
						&&schedule.getStartTime().getTimeInMillis()<=calendar.getTimeInMillis())
				{ 
					alist.add(schedule);
				}
				break;
			case 4: 
				if(schedule.getStartTime().get(Calendar.DAY_OF_MONTH)==day
						&&schedule.getStartTime().getTimeInMillis()<=calendar.getTimeInMillis())
				{ 
					alist.add(schedule);
				}
				break;
			case 5:
				if(schedule.getStartTime().get(Calendar.MONTH)==month 
				&&schedule.getStartTime().get(Calendar.DAY_OF_MONTH)==day
				&&schedule.getStartTime().getTimeInMillis()<=calendar.getTimeInMillis())
				{ 
					alist.add(schedule);
				}
				break;
			}
		}
		return alist;
	}
	public ArrayList<Schedule> getScheduleOfDay(int year,int month,int day)
	{
		return this.scheduleList.getScheduleOfDay(year, month, day);
	}
	public void cancel(int no)
	{
		for(int i=0;i<this.scheduleList.getList().size();i++)
		{
			if(this.scheduleList.getList().get(i).getNo()==no)
			{
				this.scheduleList.getList().remove(i);
			}
		}
	}
	
	public boolean modify(int no, String title, String memo, GregorianCalendar startTime,
			int type, boolean isOpen,boolean isPublic)
	{
		for(int i=0;i<this.scheduleList.getList().size();i++)
		{
			if(this.scheduleList.getList().get(i).getNo()==no)
			{
				this.scheduleList.getList().get(i).setTitle(title);
				this.scheduleList.getList().get(i).setMemo(memo);
				this.scheduleList.getList().get(i).setStartTime(startTime);
				this.scheduleList.getList().get(i).setType(type);
				this.scheduleList.getList().get(i).setPublic(isPublic);
				this.scheduleList.getList().get(i).setOpen(isOpen);
				return true;
			}
		}
		return false;
	}

	public void enroll(Schedule schedule) {
		this.scheduleList.add(schedule);
	}
	public Schedule search(int no)
	{
		for(int i=0;i<this.scheduleList.getList().size();i++)
		{
			if(this.scheduleList.getList().get(i).getNo()==no)
			{
				return this.scheduleList.getList().get(i);
			}
		}
		return null;
	}
	
	public void allRemove()
	{
		int size = this.scheduleList.getList().size();
		for(int i=0;i<size;i++)
		{
			this.scheduleList.getList().remove(0);
		}
	}

	public ArrayList<Integer> getMyScheduleEntry() {
		// TODO Auto-generated method stub
		ArrayList<Integer> entryList = new ArrayList<Integer>();
		for(Schedule schedule : this.scheduleList.getList()){
			entryList.add(schedule.getNo());
		}
		return entryList;
	}


	
}
