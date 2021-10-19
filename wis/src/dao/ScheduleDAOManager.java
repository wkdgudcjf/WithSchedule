package dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

import dao.ScheduleDAO.Schedule;
import dto.ScheduleInfo;

public class ScheduleDAOManager {
	private static ScheduleDAOManager manager = new ScheduleDAOManager();
	private ScheduleDAO dao;
	public ScheduleDAOManager() {
		super();
		this.dao = new ScheduleDAO();
	}
	
	public static ScheduleDAOManager getInstance(){
		return manager;
	}
		
	public ArrayList<ScheduleInfo> loadFromDB(){
		dao.connect();
		List<Schedule> slist = dao.searchAll();
		dao.close();
		
		ArrayList<ScheduleInfo> silist = new ArrayList<ScheduleInfo>();
		
		for(Schedule s : slist){
			StringTokenizer st = new StringTokenizer(s.getStartTime(),"/");
			int year = Integer.valueOf(st.nextToken());
			int month = Integer.valueOf(st.nextToken())-1;
			int day = Integer.valueOf(st.nextToken());
			int hour = Integer.valueOf(st.nextToken());
			int min = Integer.valueOf(st.nextToken());
			GregorianCalendar gc = new GregorianCalendar(year,month,day,hour,min);
			ScheduleInfo si = new ScheduleInfo(s.getNo(),s.getTitle(),s.getMemo(),gc,s.getType(),s.getIsOpen().equals("true")?true:false,s.getIsPublic().equals("true")?true:false);
			silist.add(si);
		}
		
		return silist;
	}
	
	public void saveToDB(ArrayList<ScheduleInfo> silist){
		dao.connect();
		for(ScheduleInfo si: silist){
			StringBuilder sb = new StringBuilder();
			GregorianCalendar gc = si.getStartTime();
			sb.append(gc.get(Calendar.YEAR));
			sb.append("/");
			sb.append(gc.get(Calendar.MONTH)+1);
			sb.append("/");
			sb.append(gc.get(Calendar.DAY_OF_MONTH));
			sb.append("/");
			sb.append(gc.get(Calendar.HOUR_OF_DAY));
			sb.append("/");
			sb.append(gc.get(Calendar.MINUTE));
			dao.insert(si.getNo(),si.getTitle(),sb.toString(),si.getMemo(),si.getType(),si.getIsOpen()?"true":"false",si.getIsPublic()?"true":"false");
		}		
		dao.close();
	}
	
	public void deleteAll(){
		dao.connect();
		dao.deleteAll();
		dao.close();
	}
}
