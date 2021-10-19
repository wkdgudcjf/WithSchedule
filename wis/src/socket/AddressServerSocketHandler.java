package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import management.MemberManagement;

public class AddressServerSocketHandler implements Runnable{

	private ServerSocket socket; 
	
	public AddressServerSocketHandler() 
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
		JSONArray ary=null;
		try {
			ary = (JSONArray)parser.parse(data);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JSONObject e =  (JSONObject)ary.get(0);
		JSONArray a = (JSONArray)ary.get(1);
		
		String email = (String)e.get("email");
		
		ArrayList<String> phoneNumList = new ArrayList<String>();
		Iterator it = a.iterator();
		while(it.hasNext()){
			JSONObject obj = (JSONObject)it.next();
			String phoneNum = (String)obj.get("phoneNum");
			if(phoneNum.contains("+82")){
				phoneNum = phoneNum.replace("+82", "0");
			}
			phoneNumList.add(phoneNum);				
		}
		
		return MemberManagement.getInstance().resetFriendList(email, phoneNumList);
	}
}
