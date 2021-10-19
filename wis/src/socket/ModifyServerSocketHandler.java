package socket;

import java.io.IOException;
import java.net.ServerSocket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dto.MemberInfo;

import management.MemberManagement;

public class ModifyServerSocketHandler implements Runnable{

	private ServerSocket socket; 
	
	public ModifyServerSocketHandler() 
	{ 
		try {
			socket = new ServerSocket(10010);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		} 
	
	public void run() { 
		//Do the comms to the remote server }
		MessageHandler modifyHandler=null;
		while(true){
			try {
				modifyHandler = new MessageHandler(socket.accept());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			String data = modifyHandler.readMessage();
			
			//System.out.println(data);
			String str = modifyByParsingData(data);				
			
			//System.out.println(str);
			modifyHandler.sendMessage(str); 
		}
	}
	
	public String modifyByParsingData(String data){
		
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
		
		JSONObject obj=null;
		try { 
			obj = (JSONObject) parser.parse(a.toString());			
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String newEmail = (String) obj.get("email");
		String newName = (String) obj.get("name");
		String newPassword = (String) obj.get("password");
		
		MemberInfo modifyMemberInfo = new MemberInfo("", newEmail, newName, newPassword);
		
		
		return MemberManagement.getInstance().modifyMember(email, modifyMemberInfo);
	}
}
