package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dto.CommentInfo;
import dto.MemberInfo;
import dto.ScheduleInfo;

import management.MemberManagement;
import management.ScheduleManagement;

public class CommentShowServerSocketHandler implements Runnable{

	private ServerSocket socket; 
	
	public CommentShowServerSocketHandler() 
	{ 
		try {
			socket = new ServerSocket(10021);
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
				
				String str = resetFriendArrayListByParsingData(data);
				
				//System.out.println(str);
				loginHandler.sendMessage(str);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
	public String resetFriendArrayListByParsingData(String data){
		
		//System.out.println(data);
		
		JSONParser parser = new JSONParser();
		JSONObject obj=null;
		try {
			obj = (JSONObject)parser.parse(data);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		int sno = Integer.valueOf((String)obj.get("sno"));
				
		ArrayList<CommentInfo> list = ScheduleManagement.getInstance().getCommentManager().searchCommentList(sno);
		
		JSONArray ary = new JSONArray();
		if(list==null) return ScheduleManagement.NO_SEARCH;
		
		for(CommentInfo comment:list){
			JSONObject commentData = new JSONObject();
			MemberInfo member = MemberManagement.getInstance().getMemberList().searchForId(comment.getMemberId());
			if(member==null) return ScheduleManagement.NO_SEARCH;
			commentData.put("name", member.getName());
			commentData.put("email", member.getEmail());
			commentData.put("no", comment.getNo());
			commentData.put("year",comment.getDate().get(Calendar.YEAR));
			commentData.put("month",comment.getDate().get(Calendar.MONTH));
			commentData.put("day",comment.getDate().get(Calendar.DAY_OF_MONTH));
			commentData.put("hour",comment.getDate().get(Calendar.HOUR_OF_DAY));
			commentData.put("min",comment.getDate().get(Calendar.MINUTE));
			commentData.put("content",comment.getContent());
			ary.add(commentData);
		}
		return ary.toString();			
	}
}
