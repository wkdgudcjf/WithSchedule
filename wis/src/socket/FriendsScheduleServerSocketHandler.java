package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dto.ScheduleInfo;



import management.MemberManagement;
import management.ScheduleManagement;

public class FriendsScheduleServerSocketHandler implements Runnable{

	private ServerSocket socket; 
	
	public FriendsScheduleServerSocketHandler() 
	{ 
		try {
			socket = new ServerSocket(10020);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	} 
	
	public void run() { 
		//Do the comms to the remote server }
		MessageHandler loginHandler;
		while(true){
			try {
				loginHandler = new MessageHandler(socket.accept());
				String data = loginHandler.readMessage();
				
				String str = friendsScheduleListByParsingData(data);
				
				//System.out.println(str);
				loginHandler.sendMessage(str);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
	public String friendsScheduleListByParsingData(String data){		
		//System.out.println(data);
		
		JSONParser parser = new JSONParser();
		JSONArray ary=null;
		try {
			ary = (JSONArray)parser.parse(data);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JSONObject e =  (JSONObject)ary.get(0);
		JSONArray a = (JSONArray)ary.get(1);
		
		int year = Integer.valueOf((String)e.get("year"));
		int month = Integer.valueOf((String)e.get("month"));
		int day = Integer.valueOf((String)e.get("day"));
		
		GregorianCalendar calendar = new GregorianCalendar(year,month,day);
				
		ArrayList<String> emailList = new ArrayList<String>();
		Iterator<JSONObject> it = a.iterator();
		while(it.hasNext()){
			JSONObject obj = (JSONObject)it.next();
			String email = (String)obj.get("email");
			if(email==null) continue;
			emailList.add(email);				
		}
		
		JSONObject fsobj = new JSONObject();
		
		for(String email : emailList){
			JSONArray sary = new JSONArray(); 
			ArrayList<ScheduleInfo> list = ScheduleManagement.getInstance().searchFriendsScheduleList(calendar, email);
			for(ScheduleInfo schedule:list){
				JSONObject scheduleData = new JSONObject();
				scheduleData.put("no",schedule.getNo());
				scheduleData.put("title",schedule.getTitle());
				scheduleData.put("memo",schedule.getTitle());
				scheduleData.put("year",schedule.getStartTime().get(Calendar.YEAR));
				scheduleData.put("month",schedule.getStartTime().get(Calendar.MONTH));
				scheduleData.put("day",schedule.getStartTime().get(Calendar.DAY_OF_MONTH));
				scheduleData.put("hour",schedule.getStartTime().get(Calendar.HOUR_OF_DAY));
				scheduleData.put("min",schedule.getStartTime().get(Calendar.MINUTE));
				scheduleData.put("type",schedule.getType());
				scheduleData.put("isopen",schedule.getIsOpen());
				scheduleData.put("ispublic",schedule.getIsPublic());
				sary.add(scheduleData);
			}
			fsobj.put(email, sary);
		}
		
		return fsobj.toString();
	}
}
