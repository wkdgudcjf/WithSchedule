package action;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dto.CommentInfo;
import dto.MemberInfo;
import dto.ScheduleInfo;

import management.MemberManagement;
import management.ScheduleManagement;

public class CommentShowAction implements ServletAction{
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
				
		int sno = Integer.valueOf(ActionUtil.getInputString(request));
		System.out.println("방번호"+sno);		
		ArrayList<CommentInfo> list = ScheduleManagement.getInstance().getCommentManager().searchCommentList(sno);
		System.out.println("코멘트 메니져:"+ScheduleManagement.getInstance().getCommentManager());
		System.out.println("코멘트 리스트:"+ list);
		JSONArray ary = new JSONArray();
		if(list==null){
			request.setAttribute("result", ary.toString());
			return;
		}
		
		for(CommentInfo comment:list){
			JSONObject commentData = new JSONObject();
			MemberInfo member = MemberManagement.getInstance().getMemberList().searchForId(comment.getMemberId());
			if(member==null) request.setAttribute("result", ScheduleManagement.NO_SEARCH);
			commentData.put("name", member.getName());
			commentData.put("email", member.getEmail());
			commentData.put("no", comment.getNo());
			commentData.put("year",comment.getDate().get(Calendar.YEAR));
			commentData.put("month",comment.getDate().get(Calendar.MONTH));
			commentData.put("day",comment.getDate().get(Calendar.DAY_OF_MONTH));
			commentData.put("hour",comment.getDate().get(Calendar.HOUR_OF_DAY));
			commentData.put("min",comment.getDate().get(Calendar.MINUTE));
			commentData.put("content",comment.getContent());
			ary.add(commentData);
		}
		request.setAttribute("result", ary.toString());			
	}
}
