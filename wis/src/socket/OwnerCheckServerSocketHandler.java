package socket;

import java.io.IOException;
import java.net.ServerSocket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import management.MemberManagement;
import management.ScheduleManagement;

public class OwnerCheckServerSocketHandler implements Runnable{

	private ServerSocket socket; 
	
	public OwnerCheckServerSocketHandler() 
	{ 
		try {
			socket = new ServerSocket(10008);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	} 
	
	public void run() { 
		//Do the comms to the remote server }
		MessageHandler loginHandler=null;
		while(true){
			try {
				loginHandler = new MessageHandler(socket.accept());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String data = loginHandler.readMessage();			
			//System.out.println(data);			
			
			String str = isOwnerCheckByParsingData(data);
			//System.out.println(str);
			loginHandler.sendMessage(str);
		}
	}
	
	public String isOwnerCheckByParsingData(String data){
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject)parser.parse(data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		String email = obj.get("email").toString();
		int sno = Integer.valueOf(obj.get("sno").toString()); 	
		
		ScheduleManagement manager = ScheduleManagement.getInstance();
		return manager.isOwnerCheck(email,sno);
	}
}
