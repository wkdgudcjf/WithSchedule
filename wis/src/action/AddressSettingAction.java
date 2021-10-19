package action;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import management.MemberManagement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dto.MemberInfo;


public class AddressSettingAction implements ServletAction {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		//JSONObject object=(JSONObject)request.getAttribute("keyword");
		//JSONObject obj=(JSONObject)request.getAttribute("newMemberInfo");
		
		JSONParser parse = new JSONParser();
		
		JSONArray ary=(JSONArray)parse.parse(ActionUtil.getInputString(request));
		//System.out.println(ary.toString());
		
		JSONObject e =  (JSONObject)ary.get(0);
		JSONArray a = (JSONArray)ary.get(1);
		
		String email = (String)e.get("email");
		
		ArrayList<String> phoneNumList = new ArrayList<String>();
		Iterator it = a.iterator();
		while(it.hasNext()){
			JSONObject obj = (JSONObject)it.next();
			String phoneNum = (String)obj.get("phoneNum");
			if(phoneNum.contains("+82")){
				phoneNum = phoneNum.replace("+82", "0");
			}
			phoneNumList.add(phoneNum);				
		}
		
		request.setAttribute("result", MemberManagement.getInstance().resetFriendList(email, phoneNumList));
	}
}
