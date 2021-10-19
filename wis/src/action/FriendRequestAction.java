package action;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.tribes.membership.MemberImpl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dto.MemberInfo;
import dto.RequestInfo;

import list.MemberList;
import management.MemberManagement;
import management.ScheduleManagement;

public class FriendRequestAction implements ServletAction{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");	
		
		ArrayList<MemberInfo> frlist = MemberManagement.getInstance().getFriendRequestManager().searchFriendRequestList(ActionUtil.getInputString(request));
		
		JSONArray ary = new JSONArray();
		
		
		for(MemberInfo fri : frlist){
			JSONObject info = new JSONObject();
			info.put("name", fri.getName());
			info.put("email",fri.getEmail());
			
			ary.add(info);
		}
		
		request.setAttribute("result",ary.toString());
	}
}
