package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dto.ScheduleInfo;

import management.MemberManagement;
import management.ScheduleManagement;

public class FriendCalendarServerSocketHandler implements Runnable{

	private ServerSocket socket; 
	
	public FriendCalendarServerSocketHandler() 
	{ 
		try {
			socket = new ServerSocket(10009);
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
				String email = loginHandler.readMessage();
				
				System.out.println(email);
				
				String str = friendSchedule(email);
				
				System.out.println(str);
				loginHandler.sendMessage(str);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
	public String friendSchedule(String email){
		
		//System.out.println(data);

		ArrayList<ScheduleInfo> scheduleList = ScheduleManagement.getInstance().getMemberSchedule(email);
		JSONArray ary = new JSONArray();
		if(scheduleList==null) return ary.toString();
		
		for(ScheduleInfo schedule:scheduleList){
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
			ary.add(scheduleData);
		}
		return ary.toString();						
	}
}
