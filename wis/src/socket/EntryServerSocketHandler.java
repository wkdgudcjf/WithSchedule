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

import dto.EntryInfo;
import dto.MemberInfo;

import management.MemberManagement;
import management.ScheduleManagement;

public class EntryServerSocketHandler implements Runnable{

	private ServerSocket socket; 
	
	public EntryServerSocketHandler() 
	{ 
		try {
			socket = new ServerSocket(10015);
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
				
				String str = entryArrayListByParsingData(data);
				
				System.out.println(str);
				loginHandler.sendMessage(str);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
	public String entryArrayListByParsingData(String data){
		
		int sno = Integer.valueOf(data);
		ArrayList<EntryInfo> eArrayList = ScheduleManagement.getInstance().getEntryManager().searchMemberForScheduleNo(sno);
		JSONArray ary = new JSONArray();
		
		for(EntryInfo ei : eArrayList){
			MemberInfo mi = MemberManagement.getInstance().getMemberList().searchForId(ei.getMemberId());
			if(mi!=null){
				JSONObject obj = new JSONObject();
				obj.put("name", mi.getName());
				obj.put("email", mi.getEmail());
				obj.put("isowner",ei.getIsOwner());
				
				ary.add(obj);
			}
		}
		
		return ary.toString();
	}
}
