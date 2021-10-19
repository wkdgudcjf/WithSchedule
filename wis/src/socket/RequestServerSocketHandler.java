package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dto.RequestInfo;

import list.MemberList;
import management.MemberManagement;
import management.ScheduleManagement;

public class RequestServerSocketHandler implements Runnable{

	private ServerSocket socket; 
	
	public RequestServerSocketHandler() 
	{ 
		try {
			socket = new ServerSocket(10017);
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
				
				String str = requestArrayListByParsingData(data);
				
				//System.out.println(str);
				loginHandler.sendMessage(str);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
	public String requestArrayListByParsingData(String email){
		
		HashMap<Integer, ArrayList<RequestInfo>> rhashMap = ScheduleManagement.getInstance().getRequestManager().searchRequestMap(email);
		
		JSONArray ary = new JSONArray();
		
		Set<Integer> key = rhashMap.keySet();
		Iterator<Integer> it = key.iterator();
		MemberList memberManager = MemberManagement.getInstance().getMemberList();
		while(it.hasNext()){
			Integer no = it.next();
			ArrayList<RequestInfo> rlist = rhashMap.get((Object)no);
			if(rlist==null) continue;
			for(RequestInfo ri : rlist){
				JSONObject info = new JSONObject();
				info.put("sno", no);
				info.put("sender", memberManager.searchForId(ri.getSenderId()).getName());
				info.put("receiver", memberManager.searchForId(ri.getReceiverId()).getName());
				info.put("type", ri.getRequestType());
				ary.add(info);
			}
		}				
		return ary.toString();
	}
}
