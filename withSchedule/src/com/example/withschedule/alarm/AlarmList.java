package com.example.withschedule.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AlarmList implements Serializable
{
	private static final long serialVersionUID = -752250407115183674L;
	private List<Alarm> list;

	public AlarmList(List<Alarm> list) {
		this.list = list;
	}
	
	public AlarmList(){
		list = new ArrayList<Alarm>();
	}

	public List<Alarm> getList() {
		return list;
	}

	public void setList(List<Alarm> list) {
		this.list = list;
	}

	public void add(Alarm alarm) {
		// TODO Auto-generated method stub
		this.list.add(alarm);
	}
	
	public void remove(Alarm alarm)
	{
		this.list.remove(alarm);
	}
}
