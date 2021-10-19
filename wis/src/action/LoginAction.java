package action;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Calendar;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dto.MemberInfo;
import dto.ScheduleInfo;

import management.MemberManagement;
import management.ScheduleManagement;

public class LoginAction implements ServletAction{
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");	
		
		MemberInfo inputMember = parseToMemberInfo(ActionUtil.getInputString(request));
		//System.out.println(data);		
		JSONArray ary = new JSONArray();
		
		JSONObject o = MemberManagement.getInstance().login(inputMember);
		JSONArray a = mySchedule(inputMember.getEmail());
		
		ary.add(o);
		if(a!=null)	ary.add(a);
		
		//System.out.println("12332123123 : "+o.toString());
		
		request.setAttribute("result", ary.toString());
	}	
	
	private MemberInfo parseToMemberInfo(String data){
		JSONParser parser = new JSONParser();
		
		JSONObject obj=null;
		try {
			obj =  (JSONObject)parser.parse(data);			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Integer id = (Integer)obj.get("id");
		String phoneNum = (String)obj.get("phoneNum");
		if(phoneNum.contains("+82")){
			phoneNum = phoneNum.replace("+82", "0");
		}
		String email = (String)obj.get("email");
		String name = (String)obj.get("name");
		String password = (String)obj.get("password");
	
		if(id==null){
			return new MemberInfo(phoneNum,email,name,password);
		}
		return new MemberInfo(id,phoneNum,email,name,password);
	}

	public JSONArray mySchedule(String email){		
		//System.out.println(data);

		ArrayList<ScheduleInfo> scheduleArrayList = ScheduleManagement.getInstance().getMemberSchedule(email);
		if(scheduleArrayList==null) return null;
		
		JSONArray ary = new JSONArray();
		for(ScheduleInfo schedule:scheduleArrayList){
			JSONObject scheduleData = new JSONObject();
			scheduleData.put("sno",schedule.getNo());
			scheduleData.put("title",schedule.getTitle());
			scheduleData.put("memo",schedule.getTitle());
			scheduleData.put("year",schedule.getStartTime().get(Calendar.YEAR));
			scheduleData.put("month",schedule.getStartTime().get(Calendar.MONTH));
			scheduleData.put("day",schedule.getStartTime().get(Calendar.DAY_OF_MONTH));
			scheduleData.put("hour",schedule.getStartTime().get(Calendar.HOUR_OF_DAY));
			scheduleData.put("min",schedule.getStartTime().get(Calendar.MINUTE));
			scheduleData.put("type",schedule.getType());
			scheduleData.put("isopen",schedule.getIsOpen());
			scheduleData.put("ispublic",schedule.getIsPublic());
			ary.add(scheduleData);
		}
		return ary;			
	}
}
