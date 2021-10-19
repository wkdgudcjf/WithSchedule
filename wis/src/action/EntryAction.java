package action;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dto.EntryInfo;
import dto.MemberInfo;

import management.MemberManagement;
import management.ScheduleManagement;

public class EntryAction implements ServletAction{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");	
		
		int sno = Integer.valueOf(ActionUtil.getInputString(request));
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
		
		request.setAttribute("result", ary.toString());
	}
}
