package action;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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

public class UpdateMyEntryScheduleAction implements ServletAction{

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
		
		//ArrayList<Integer> snoList = new ArrayList<Integer>();
		
		String email = (String)ary.get(0);
		MemberInfo member = MemberManagement.getInstance().getMemberList().searchForEmail(email);
		
		
		
		if(member==null){
			request.setAttribute("result", ScheduleManagement.NO_SEARCH);
		}
		
		ArrayList<Integer> entryList = ScheduleManagement.getInstance().getEntryManager().searchScheduleForMemberId(member.getId());

		//새로등록된 스케쥴만 보내는 로직
//		System.out.println("엔트리리스트 : "+entryList.toString());
//		
//		for(int i=1;i<ary.size();i++){
//			JSONObject obj=(JSONObject)ary.get(i);
//			Integer sno = Integer.valueOf(obj.get("sno").toString());
//			snoList.add(sno);
//		}		
//		
//		System.out.println("sno리스트 : "+snoList.toString());
//		//Integer은 객체이기 때문에 == 비교를 하면 안된다.
//		
//		ArrayList<Integer> resultList = new ArrayList<Integer>();
//		
//		for(Integer no : entryList){
//			boolean check=true;
//			for(Integer sno : snoList){
//				if(sno.equals(no)){
//					check=false;
//					break;
//				}
//			}
//			if(check) resultList.add(no);
//		}
//		
//		System.out.println("최종리스트 : "+resultList.toString());
		JSONArray sary = new JSONArray();
		
		if(entryList == null){
			request.setAttribute("result", sary.toString());
			return;
		}
		
		for(Integer no : entryList){
			ScheduleInfo schedule = ScheduleManagement.getInstance().search(no);
			
			JSONObject scheduleData = new JSONObject();
			scheduleData.put("no",schedule.getNo());
			scheduleData.put("title",schedule.getTitle());
			scheduleData.put("memo",schedule.getMemo());
			scheduleData.put("year",schedule.getStartTime().get(Calendar.YEAR));
			scheduleData.put("month",schedule.getStartTime().get(Calendar.MONTH));
			scheduleData.put("day",schedule.getStartTime().get(Calendar.DAY_OF_MONTH));
			scheduleData.put("hour",schedule.getStartTime().get(Calendar.HOUR_OF_DAY));
			scheduleData.put("min",schedule.getStartTime().get(Calendar.MINUTE));
			scheduleData.put("type",schedule.getType());
			scheduleData.put("isopen",schedule.getIsOpen());
			scheduleData.put("ispublic",schedule.getIsPublic());
			sary.add(scheduleData);
		}
		
		request.setAttribute("result", sary.toString());
		return;
	}
}
