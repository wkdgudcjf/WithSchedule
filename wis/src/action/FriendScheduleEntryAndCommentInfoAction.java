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

import dto.*;

import list.*;
import management.MemberManagement;
import management.ScheduleManagement;

public class FriendScheduleEntryAndCommentInfoAction implements ServletAction{
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");	
		
		JSONParser parse = new JSONParser();
		
		JSONArray ary =(JSONArray)parse.parse(ActionUtil.getInputString(request));
		Iterator it = ary.iterator();
		
		ScheduleManagement sm = ScheduleManagement.getInstance();
		
		JSONArray cntAry = new JSONArray();
		
		while(it.hasNext()){			
			int sno = Integer.valueOf(((Long)it.next()).toString());
			System.out.println(sno);
			
			ArrayList<EntryInfo> elist = sm.getEntryManager().searchMemberForScheduleNo(sno);
			ArrayList<CommentInfo> clist = sm.getCommentManager().searchCommentList(sno);	
			if(clist==null){
				clist=new ArrayList<CommentInfo>();
			}
			if(elist==null){
				request.setAttribute("result", ScheduleManagement.NO_SEARCH);
				return;
			}
			
			JSONObject cntObj = new JSONObject();
			cntObj.put("sno",sno);
			cntObj.put("entryCnt",elist.size());
			cntObj.put("commentCnt",clist.size());
			
			cntAry.add(cntObj);
		}
		
		request.setAttribute("result", cntAry.toString());
	}
}
