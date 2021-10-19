package com.example.withschedule.schedule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ScheduleList implements Serializable{
	private static final long serialVersionUID = -7993747611252208139L;
	
	private List<Schedule> list;

	public ScheduleList(List<Schedule> list) {
		this.list = list;
	}
	
	public ScheduleList(){
		list = new ArrayList<Schedule>();
	}

	public List<Schedule> getList() {
		return list;
	}

	public void setList(List<Schedule> list) {
		this.list = list;
	}

	public void add(Schedule schedule) {
		// TODO Auto-generated method stub
		this.list.add(schedule);
	}
	
	public void remove(Schedule schedule)
	{
		this.list.remove(schedule);
	}
	
	public ArrayList<Schedule> getScheduleOfDay(int year,int month,int day){
		ArrayList<Schedule> alist = new ArrayList<Schedule>();
		GregorianCalendar calendar = new GregorianCalendar(year,month,day,23,59,59);
		for(Schedule schedule:this.list){
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
	
	public Schedule search(int no)
	{
		for(int i=0;i<this.list.size();i++)
		{
			if(this.list.get(i).getNo()==no)
			{
				return this.list.get(i);
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "ScheduleList [list=" + list + "]";
	}	
	
}
