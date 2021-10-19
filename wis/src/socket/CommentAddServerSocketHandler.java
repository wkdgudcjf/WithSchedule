package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import management.MemberManagement;
import management.ScheduleManagement;

public class CommentAddServerSocketHandler implements Runnable{

	private ServerSocket socket; 
	
	public CommentAddServerSocketHandler() 
	{ 
		try {
			socket = new ServerSocket(10004);
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
				
				String str = addCommentByParsingData(data);
				
				//System.out.println(str);
				loginHandler.sendMessage(str);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
	public String addCommentByParsingData(String data){
		
		JSONParser parser = new JSONParser();
		JSONObject obj=null;
		try {
			obj = (JSONObject)parser.parse(data);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String email =  (String)obj.get("email");
		int sno = Integer.valueOf((String)obj.get("sno"));
		String content = (String)obj.get("content");
		int year = Integer.valueOf((String)obj.get("year"));
		int month = Integer.valueOf((String)obj.get("month"));
		int day = Integer.valueOf((String)obj.get("day"));
		int hour = Integer.valueOf((String)obj.get("hour"));
		int min = Integer.valueOf((String)obj.get("min"));
		GregorianCalendar date = new GregorianCalendar(year,month,day,hour,min);
				
		boolean check = ScheduleManagement.getInstance().getCommentManager().addComment(sno, email, content, date);
		if(check==true){
			return ScheduleManagement.COMPLETION;
		}else{
			return ScheduleManagement.NO_SEARCH;
		}
	}
}
