package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.GregorianCalendar;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dto.ScheduleInfo;

import management.ScheduleManagement;

public class AddScheduleServerSocketHandler implements Runnable{

	private ServerSocket socket; 
	
	public AddScheduleServerSocketHandler() 
	{ 
		try {
			socket = new ServerSocket(10005);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	} 
	
	public void run() { 
		//Do the comms to the remote server }
		MessageHandler loginHandler = null;
		while(true){
			try {
				loginHandler = new MessageHandler(socket.accept());				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String data = loginHandler.readMessage();			
			//System.out.println(data);
			String str = enrollByParsingData(data);							
			//System.out.println(str);
			loginHandler.sendMessage(str); 
		}
	}
	public String enrollByParsingData(String data){
		
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
		JSONObject a = (JSONObject)ary.get(1);
		
		String email = (String)e.get("email");
		String newScheduleInfo = a.toString();
		ScheduleInfo inputSchedule = parseToScheduleInfo(newScheduleInfo);
				
		return ScheduleManagement.getInstance().enroll(email, inputSchedule);
	}
	
	private ScheduleInfo parseToScheduleInfo(String data){
		JSONParser parser = new JSONParser();
		
		JSONObject obj=null;
		try {
			obj =  (JSONObject)parser.parse(data);			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String title  = (String)obj.get("title");
		String memo  = (String)obj.get("memo");
		boolean isPublic  = (Boolean)obj.get("ispublic");
		boolean isOpen  = (Boolean)obj.get("isopen");
		int startYear = Integer.valueOf(obj.get("startyear").toString());
		int startMonth = Integer.valueOf(obj.get("startmonth").toString());
		System.out.println(startMonth);
		int startDay = Integer.valueOf(obj.get("startday").toString());
		int startHour = Integer.valueOf(obj.get("starthour").toString());
		int startMin = Integer.valueOf(obj.get("startmin").toString());
		int type = Integer.valueOf(obj.get("type").toString());
			
		if(obj.get("sno")==null){
			return new ScheduleInfo(title, memo, new GregorianCalendar(startYear,startMonth,startDay,startHour,startMin), type,
					isOpen, isPublic);
		}
		Integer sno = Integer.valueOf(obj.get("sno").toString());
		return new ScheduleInfo(sno,title, memo, new GregorianCalendar(startYear,startMonth,startDay,startHour,startMin), type,
				isOpen, isPublic);
	}
}
