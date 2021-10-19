package action;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dto.ScheduleInfo;

import management.MemberManagement;
import management.ScheduleManagement;

public class FriendCalendarAction implements ServletAction{
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");	
		
		ArrayList<ScheduleInfo> scheduleList = ScheduleManagement.getInstance().getMemberSchedule(ActionUtil.getInputString(request));
		JSONArray ary = new JSONArray();
		
		if(scheduleList==null){
			request.setAttribute("result", ary.toString());
			return;
		}
		
		for(ScheduleInfo schedule:scheduleList){
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
			ary.add(scheduleData);
		}
		request.setAttribute("result", ary.toString());
	}
}
