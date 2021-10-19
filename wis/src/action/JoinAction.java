package action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import management.MemberManagement;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import dto.MemberInfo;


public class JoinAction implements ServletAction {

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
		
		String phoneNum = (String)obj.get("phoneNum");
		if(phoneNum.contains("+82")){
			phoneNum = phoneNum.replace("+82", "0");
		}
		String email = (String)obj.get("email");
		String name = (String)obj.get("name");
		String password = (String)obj.get("password");
		MemberInfo mi = null;
		mi = new MemberInfo(phoneNum,email,name,password);
		
		String result=MemberManagement.getInstance().join(mi);
		System.out.println(result);
		request.setAttribute("result", result);
		//tc.savaPersonalMapping(x, y, width, height, email);
	}
}
