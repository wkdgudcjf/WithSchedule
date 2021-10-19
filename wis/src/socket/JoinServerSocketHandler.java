package socket;

import java.io.IOException;
import java.net.ServerSocket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dto.MemberInfo;

import management.MemberManagement;

public class JoinServerSocketHandler implements Runnable{

	private ServerSocket socket; 
	
	public JoinServerSocketHandler() 
	{ 
		try {
			socket = new ServerSocket(10001);
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
			//로직
			System.out.println("왔다");
			String str = joinByParsingData(data);
			System.out.println(str);
			loginHandler.sendMessage(str);
		}
	}
	
	public String joinByParsingData(String data){
		
		MemberInfo inputMember = parseToMemberInfo(data);
		//System.out.println(data);		
		
		return MemberManagement.getInstance().join(inputMember);
	}
	
	
	private MemberInfo parseToMemberInfo(String data){
		JSONParser parser = new JSONParser();
		
		JSONObject obj=null;
		try {
			obj =  (JSONObject)parser.parse(data);			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Integer id = (Integer)obj.get("id");
		String phoneNum = (String)obj.get("phoneNum");
		if(phoneNum.contains("+82")){
			phoneNum = phoneNum.replace("+82", "0");
		}
		String email = (String)obj.get("email");
		String name = (String)obj.get("name");
		String password = (String)obj.get("password");
	
		if(id==null){
			return new MemberInfo(phoneNum,email,name,password);
		}
		return new MemberInfo(id,phoneNum,email,name,password);
	}
}
