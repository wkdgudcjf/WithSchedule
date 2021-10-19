package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import management.MemberManagement;
import management.ScheduleManagement;

public class MemberDeleteServerSocketHandler implements Runnable{

	private ServerSocket socket; 
	
	public MemberDeleteServerSocketHandler() 
	{ 
		try {
			socket = new ServerSocket(10011);
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
				
				String str = memberDelete(email);
				
				//System.out.println(str);
				loginHandler.sendMessage(str);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
	public String memberDelete(String email){
		
		//System.out.println(data);
		
		return MemberManagement.getInstance().deleteMember(email);
	}
}
