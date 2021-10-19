package action;

import java.io.IOException;
import java.net.ServerSocket;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import management.MemberManagement;
import management.ScheduleManagement;

public class OwnerCheckAction implements ServletAction{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");	
		
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject)parser.parse(ActionUtil.getInputString(request));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		String email = obj.get("email").toString();
		int sno = Integer.valueOf(obj.get("sno").toString()); 	
						
		ScheduleManagement manager = ScheduleManagement.getInstance();
		request.setAttribute("result", manager.isOwnerCheck(email,sno));
	}
}
