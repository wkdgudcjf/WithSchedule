package action;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dto.RequestInfo;

import list.MemberList;
import management.MemberManagement;
import management.ScheduleManagement;

public class RequestAction implements ServletAction{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");	
		
		HashMap<Integer, ArrayList<RequestInfo>> rhashMap = ScheduleManagement.getInstance().getRequestManager().searchRequestMap(ActionUtil.getInputString(request));
		
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
				info.put("title", ScheduleManagement.getInstance().search(no).getTitle());
				info.put("sendername", memberManager.searchForId(ri.getSenderId()).getName());
				info.put("senderemail", memberManager.searchForId(ri.getSenderId()).getEmail());
				info.put("receivername", memberManager.searchForId(ri.getReceiverId()).getName());
				info.put("receiveremail", memberManager.searchForId(ri.getReceiverId()).getEmail());
				info.put("type", ri.getRequestType());
				ary.add(info);
			}
		}		
		request.setAttribute("result",ary.toString());
	}
}
