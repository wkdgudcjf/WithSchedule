package list;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import dto.ScheduleInfo;

public class ScheduleList {
	private ArrayList<ScheduleInfo> list;

	public ScheduleList(ArrayList<ScheduleInfo> list) {
		this.list = list;
	}
	
	public ScheduleList(){
		this(new ArrayList<ScheduleInfo>());
	}

	public ArrayList<ScheduleInfo> getList() {
		return list;
	}
	
	public void setList(ArrayList<ScheduleInfo> list) {
		this.list = list;
	}

	public void add(ScheduleInfo ScheduleInfo) {
		// TODO Auto-generated method stub
		this.list.add(ScheduleInfo);
	}
	
	public void remove(ScheduleInfo ScheduleInfo)
	{
		this.list.remove(ScheduleInfo);
	}
	
	public ScheduleInfo search(int no)
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
	
	public ArrayList<ScheduleInfo> getScheduleInfoOfDay(int year,int month,int day){
		ArrayList<ScheduleInfo> alist = new ArrayList<ScheduleInfo>();
		GregorianCalendar calendar = new GregorianCalendar(year,month,day,23,59,59);
		for(ScheduleInfo ScheduleInfo:this.list){
			switch(ScheduleInfo.getType()){
			case 1:
				if(ScheduleInfo.getStartTime().get(Calendar.YEAR)==year 
				&&ScheduleInfo.getStartTime().get(Calendar.MONTH)==month 
				&&ScheduleInfo.getStartTime().get(Calendar.DAY_OF_MONTH)==day)
				{ 
					alist.add(ScheduleInfo);
				}
				break;
			case 2: 				
				if(ScheduleInfo.getStartTime().get(Calendar.DAY_OF_WEEK)==calendar.get(Calendar.DAY_OF_WEEK)
						&&ScheduleInfo.getStartTime().getTimeInMillis()<=calendar.getTimeInMillis())
				{
					alist.add(ScheduleInfo);
				}
				break;
			case 3: 
				if(ScheduleInfo.getStartTime().getMaximum(Calendar.DAY_OF_MONTH)==day
						&&ScheduleInfo.getStartTime().getTimeInMillis()<=calendar.getTimeInMillis())
				{ 
					alist.add(ScheduleInfo);
				}
				break;
			case 4: 
				if(ScheduleInfo.getStartTime().get(Calendar.DAY_OF_MONTH)==day
						&&ScheduleInfo.getStartTime().getTimeInMillis()<=calendar.getTimeInMillis())
				{ 
					alist.add(ScheduleInfo);
				}
				break;
			case 5:
				if(ScheduleInfo.getStartTime().get(Calendar.MONTH)==month 
				&&ScheduleInfo.getStartTime().get(Calendar.DAY_OF_MONTH)==day
				&&ScheduleInfo.getStartTime().getTimeInMillis()<=calendar.getTimeInMillis())
				{ 
					alist.add(ScheduleInfo);
				}
				break;
			}
		}
		return alist;
	}
	
	@Override
	public String toString() {
		return "ScheduleInfolist [list=" + list + "]";
	}	
	
}
