package action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import management.MemberManagement;
import management.ScheduleManagement;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import dto.MemberInfo;


public class CancelEnterRequestAction implements ServletAction {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		//JSONObject object=(JSONObject)request.getAttribute("keyword");
		//JSONObject obj=(JSONObject)request.getAttribute("newMemberInfo");
		
		JSONParser parse = new JSONParser();
		
		JSONObject obj=(JSONObject)parse.parse(ActionUtil.getInputString(request));
		
		//System.out.println(obj.toString());
		int sno = Integer.valueOf(obj.get("sno").toString());
		String receiverEmail = (String)obj.get("receiver");
		String senderEmail = (String)obj.get("sender");
		
		String result=ScheduleManagement.getInstance().getRequestManager().cancelEnterRequest(sno,senderEmail,receiverEmail);
		System.out.println(result);
		request.setAttribute("result", result);
		//tc.savaPersonalMapping(x, y, width, height, email);
	}
}
