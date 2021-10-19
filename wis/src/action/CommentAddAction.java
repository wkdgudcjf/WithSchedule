package action;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import management.MemberManagement;
import management.ScheduleManagement;

public class CommentAddAction implements ServletAction{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		JSONParser parser = new JSONParser();
		JSONObject obj=null;
		try {
			obj = (JSONObject)parser.parse(ActionUtil.getInputString(request));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String email =  (String)obj.get("email");
		int sno = Integer.valueOf(obj.get("sno").toString());
		String content = (String)obj.get("content");
		int year = Integer.valueOf(obj.get("year").toString());
		int month = Integer.valueOf(obj.get("month").toString());
		int day = Integer.valueOf(obj.get("day").toString());
		int hour = Integer.valueOf(obj.get("hour").toString());
		int min = Integer.valueOf(obj.get("min").toString());
		GregorianCalendar date = new GregorianCalendar(year,month,day,hour,min);
				
		boolean check = ScheduleManagement.getInstance().getCommentManager().addComment(sno, email, content, date);
		if(check==true){
			request.setAttribute("result",ScheduleManagement.COMPLETION);
		}else{
			request.setAttribute("result",ScheduleManagement.NO_SEARCH);
		}
	}
}
