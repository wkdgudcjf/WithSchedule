package action;

import java.io.IOException;
import java.net.ServerSocket;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dto.MemberInfo;

import management.MemberManagement;

public class ModifyAction implements ServletAction{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");	
		
		JSONParser parser = new JSONParser();
		JSONArray ary=null;
		try {
			ary = (JSONArray)parser.parse(ActionUtil.getInputString(request));
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
		request.setAttribute("result", MemberManagement.getInstance().modifyMember(email, modifyMemberInfo));
	}
}
