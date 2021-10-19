package action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import management.MemberManagement;
import management.ScheduleManagement;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import dto.MemberInfo;


public class pushSettingAction implements ServletAction {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception
		{
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		System.out.println("여긴오냐");
		//JSONObject object=(JSONObject)request.getAttribute("keyword");
		//JSONObject obj=(JSONObject)request.getAttribute("newMemberInfo");
		
		JSONParser parse = new JSONParser();
		
		JSONObject obj=(JSONObject)parse.parse(ActionUtil.getInputString(request));
		
		//System.out.println(obj.toString());
		String email = (String)obj.get("email");
		String key = (String)obj.get("key");
		System.out.println(email+"과"+key+"가 들어왓다.");
		String result=MemberManagement.getInstance().addId(email, key);
		System.out.println(result);
		request.setAttribute("result", result);
		//tc.savaPersonalMapping(x, y, width, height, email);
	}
}
