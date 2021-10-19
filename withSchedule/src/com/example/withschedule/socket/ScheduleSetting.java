package com.example.withschedule.socket;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.withschedule.AppManager;
import com.example.withschedule.dto.CntInfo;
import com.example.withschedule.dto.CommentInfo;
import com.example.withschedule.dto.EntryInfo;
import com.example.withschedule.dto.RequestInfo;
import com.example.withschedule.alarm.Alarm;
import com.example.withschedule.alarm.SystemAlarmManager;
import com.example.withschedule.db.ExcuteDB;
import com.example.withschedule.schedule.Schedule;
import com.example.withschedule.schedule.ScheduleList;

public class ScheduleSetting {

	public static String enroll(Context context, boolean alarm,int id,int atime,String title,String memo,int type,boolean isOpen,boolean isPublic,GregorianCalendar startdate)
	{
		String scno = null;
		SharedPreferences pref = context.getSharedPreferences("withschedule", 0);
		String email = pref.getString("email", "");
		JSONArray sa = new JSONArray();
		JSONObject scheduleA = new JSONObject();
		JSONObject emailO = new JSONObject();
		try
		{
			emailO.put("email", email);
			scheduleA.put("title", title);
			scheduleA.put("memo", memo);
			scheduleA.put("isopen", isOpen);
			scheduleA.put("ispublic", isPublic);
			scheduleA.put("startyear", startdate.get(Calendar.YEAR));
			scheduleA.put("startmonth", startdate.get(Calendar.MONTH));
			scheduleA.put("startday", startdate.get(Calendar.DAY_OF_MONTH));
			scheduleA.put("starthour", startdate.get(Calendar.HOUR_OF_DAY));
			scheduleA.put("startmin", startdate.get(Calendar.MINUTE));
			scheduleA.put("type", type);
			sa.put(emailO);
			sa.put(scheduleA);
		} 
		catch (JSONException e1)
		{
			e1.printStackTrace();
		}			
		scno = excuteHttpPostMethod("addschedule.do", sa.toString());
		
		if(scno.equals(AppManager.NO_SEARCH)){
			return AppManager.NO_SEARCH;
		}
		else if(scno.equals(AppManager.HTTP_ERROR)){
			return AppManager.HTTP_ERROR;
		}
		
		Integer sno = Integer.parseInt(scno);
		if(alarm)
		{
			SystemAlarmManager.enrollAlarm(context,title, startdate, type,sno, atime);
			AppManager.getInstance().getAm().enroll(new Alarm(sno,true,atime));
			ExcuteDB.insertAlarm(context, sno, true, atime);
		}		
		ExcuteDB.insertSchedule(context,sno, title, startdate, type, isOpen, isPublic, memo);
		AppManager.getInstance().getSm().enroll(new Schedule(sno, title, memo,startdate , type, isOpen, isPublic));
		return AppManager.COMPLETION;
	}

	
	public static String modify(int sno,String title,String memo,int type,boolean isOpen,boolean isPublic,GregorianCalendar startdate)
	{
		JSONObject scheduleA = new JSONObject();
		try
		{
			scheduleA.put("sno", sno);
			scheduleA.put("title", title);
			scheduleA.put("memo", memo);
			scheduleA.put("isopen", isOpen);
			scheduleA.put("ispublic", isPublic);
			scheduleA.put("startyear", startdate.get(Calendar.YEAR));
			scheduleA.put("startmonth", startdate.get(Calendar.MONTH));
			scheduleA.put("startday", startdate.get(Calendar.DATE));
			scheduleA.put("starthour", startdate.get(Calendar.HOUR_OF_DAY));
			scheduleA.put("startmin", startdate.get(Calendar.MINUTE));
			scheduleA.put("type", type);
		} 
		catch (JSONException e1)
		{
			e1.printStackTrace();
		}					
		
		return excuteHttpPostMethod("schedulemodify.do", scheduleA.toString());
	}
	
	public static String delete(Context context, int sno)
	{
		SharedPreferences pref = context.getSharedPreferences(
				"withschedule", 0);
		String email = pref.getString("email", "");
		
		JSONObject info = new JSONObject();
		
		try
		{
			info.put("email", email);
			info.put("sno", sno);
		} 
		catch (JSONException e1)
		{
			e1.printStackTrace();
		}
		
		return excuteHttpPostMethod("scheduleout.do", info.toString());
	}

	public static String ownerCheck(Context context, int sno) {
		// TODO Auto-generated method stub
		SharedPreferences pref = context.getSharedPreferences("withschedule", 0);
		String email = pref.getString("email", "");
	
		JSONObject info = new JSONObject();		
		JSONArray arrNo = new JSONArray();
		
		arrNo.put(sno);
		
		try
		{
			info.put("email", email);
			info.put("snolist", arrNo);
		} 
		catch (JSONException e1)
		{
			e1.printStackTrace();
		}
		return excuteHttpPostMethod("ownercheck.do", info.toString());
	}
	
	public static String ownerCheck(Context context,ArrayList<Schedule> sl) {
		// TODO Auto-generated method stub
		SharedPreferences pref = context.getSharedPreferences("withschedule", 0);
		String email = pref.getString("email", "");
	
		JSONObject info = new JSONObject();		
		JSONArray arrNo = new JSONArray();
		
		for(Schedule s : sl){
			arrNo.put(s.getNo());
		}
		
		try
		{
			info.put("email", email);
			info.put("snolist", arrNo);
		} 
		catch (JSONException e1)
		{
			e1.printStackTrace();
		}					
		return excuteHttpPostMethod("ownercheck.do", info.toString());
	}


	//여기부터확인////////////////////////
	
	
	public static ScheduleList friendSchedule(String email)
	{
		// TODO Auto-generated method stub
		
		String result = excuteHttpPostMethod("friendcalendar.do", email);
		if(result.equals(AppManager.HTTP_ERROR)){
			return null;
		}
		
		List<Schedule> list = ParsingSchedule(result);
		ScheduleList slist = new ScheduleList(list);
		
		return slist;
	}
	
	
	public static ScheduleList updateMyEntrySchedule(Context context)
	{
		// TODO Auto-generated method stub
		SharedPreferences pref = context.getSharedPreferences(
				"withschedule", 0);
		String email = pref.getString("email", "");
		
		JSONArray ary = new JSONArray();
		List<Schedule> slist = AppManager.getInstance().getSm().getScheduleList().getList();
		ary.put(email);
		try {
			for(Schedule s : slist){
				JSONObject obj = new JSONObject();				
				obj.put("sno", s.getNo());
				ary.put(obj);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String result = excuteHttpPostMethod("update_myentry_schedule.do", ary.toString());
		if(result.equals(AppManager.HTTP_ERROR)){
			return null;
		}
		
		List<Schedule> list = ParsingSchedule(result);
		
		for(Schedule schedule : list){
			Schedule s = AppManager.getInstance().getSm().search(schedule.getNo());
			if(s!=null){
				ExcuteDB.updateSchedule(context,schedule.getNo(), schedule.getTitle(), schedule.getStartTime(), schedule.getType(), schedule.isOpen(), schedule.isPublic(), schedule.getMemo());
				AppManager.getInstance().getSm().modify(schedule.getNo(), schedule.getTitle(), schedule.getMemo(),  schedule.getStartTime(), schedule.getType(), schedule.isOpen(), schedule.isPublic());
			}else{
				ExcuteDB.insertSchedule(context,schedule.getNo(), schedule.getTitle(), schedule.getStartTime(), schedule.getType(), schedule.isOpen(), schedule.isPublic(), schedule.getMemo());
				AppManager.getInstance().getSm().enroll(schedule);
			}
		}		
		return new ScheduleList(list);
	}
	
	public static ArrayList<EntryInfo> entryCheck(int scheduleNo)
	{
		String result = excuteHttpPostMethod("entry.do", String.valueOf(scheduleNo));
		if(result.equals(AppManager.HTTP_ERROR)){
			return null;
		}		
		return parseOfEntryInfo(result);
	}
	
	public static ArrayList<CommentInfo> commentCheck(int scheduleNo)
	{
		String result = excuteHttpPostMethod("showcomment.do", String.valueOf(scheduleNo));
		if(result.equals(AppManager.HTTP_ERROR)){
			return null;
		}	  	   
		return parseOfCommentInfo(result);
	}
	
	public static ArrayList<ScheduleList> setFriendsScheduleList(GregorianCalendar calendar,ArrayList<String> friendsEmailList)
	{
		JSONArray ary = new JSONArray();
		try {
			JSONObject obj = new JSONObject();
			obj.put("year", String.valueOf(calendar.get(Calendar.YEAR)));
			obj.put("month", String.valueOf(calendar.get(Calendar.MONTH)));
			obj.put("day", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
			ary.put(obj);
			
			JSONArray a = new JSONArray();
			for(String email : friendsEmailList){
				a.put(email);
			}
			ary.put(a);			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		String result = excuteHttpPostMethod("friendschedule.do", ary.toString());
		if(result.equals(AppManager.HTTP_ERROR)){
			return null;
		}	  	  
	    
		return friendScheduleSetting(result,friendsEmailList); 
	}

	public static HashMap<String,ArrayList<Schedule>> setScheduleList2(GregorianCalendar calendar,ArrayList<String> friendsEmailList)
	{
		JSONArray ary = new JSONArray();
		try {
			JSONObject obj = new JSONObject();
			obj.put("year", String.valueOf(calendar.get(Calendar.YEAR)));
			obj.put("month", String.valueOf(calendar.get(Calendar.MONTH)));
			obj.put("day", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
			ary.put(obj);
			
			JSONArray a = new JSONArray();
			for(String email : friendsEmailList){
				a.put(email);
			}
			ary.put(a);			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String result = excuteHttpPostMethod("friendschedule.do", ary.toString());
		if(result.equals(AppManager.HTTP_ERROR)){
			return null;
		}	  	    
		return friendScheduleSetting2(result,friendsEmailList); 
	}

	public static String enrollcomment(int scheduleNo,String content,String email,GregorianCalendar gc,boolean gcm) 
	{
		JSONObject comment = new JSONObject();
		try
		{
			comment.put("sno",String.valueOf(scheduleNo));
			comment.put("email",email);
			comment.put("content",content);
			comment.put("year", String.valueOf(gc.get(Calendar.YEAR)));
			comment.put("month", String.valueOf(gc.get(Calendar.MONTH)));
			comment.put("day", String.valueOf(gc.get(Calendar.DATE)));
			comment.put("hour", String.valueOf(gc.get(Calendar.HOUR_OF_DAY)));
			comment.put("min",String.valueOf( gc.get(Calendar.MINUTE)));
			comment.put("gcm", gcm);
		} 
		catch (JSONException e1)
		{
			e1.printStackTrace();
		}					
		return excuteHttpPostMethod("addcomment.do", comment.toString());
	}

	public static String enterRequest(int sno, String senderEmail, String receiverEmail) {
		// TODO Auto-generated method stub
		JSONObject requestInfo = new JSONObject();
		try
		{
			requestInfo.put("sno", sno);
			requestInfo.put("sender", senderEmail);
			requestInfo.put("receiver", receiverEmail);
			requestInfo.put("type", 1);
		} 
		catch (JSONException e1)
		{
			e1.printStackTrace();
		}					
		
		return excuteHttpPostMethod("enter_request.do", requestInfo.toString());
	}

	public static ArrayList<RequestInfo> requestCheck(Context context) {
		// TODO Auto-generated method stub
		String email = context.getSharedPreferences("withschedule",0).getString("email", "null");	
		
		String result = excuteHttpPostMethod("request.do", email);
		if(result.equals(AppManager.HTTP_ERROR)){
			return null;
		}
	    return parseRequestInfo(result);
	}

	public static String acceptEnterRequest(int no, String senderEmail) {
		// TODO Auto-generated method stub	
		JSONObject requestInfo = new JSONObject();
		try
		{
			requestInfo.put("sno", no);
			requestInfo.put("sender", senderEmail);
		} 
		catch (JSONException e1)
		{
			e1.printStackTrace();
		}					
		
		return excuteHttpPostMethod("accept_enter_request.do", requestInfo.toString());
	}

	public static String cancleEnterRequest(int no, String senderEmail,
			String receiverEmail) {
		// TODO Auto-generated method stub
		JSONObject requestInfo = new JSONObject();
		try
		{
			requestInfo.put("sno", no);
			requestInfo.put("sender", senderEmail);
			requestInfo.put("receiver", receiverEmail);
		} 
		catch (JSONException e1)
		{
			e1.printStackTrace();
		}					
		
		return excuteHttpPostMethod("cancel_enter_request.do", requestInfo.toString());
	}
	
	public static ArrayList<CntInfo> checkScheduleCNT(ArrayList<Schedule> list) {
		// TODO Auto-generated method stub
		JSONArray requestInfo = new JSONArray();
		for(Schedule i : list)
		{
			requestInfo.put(i.getNo());
		}					
		String str = excuteHttpPostMethod("entrycommentcnt.do", requestInfo.toString());
		return parsecnt(str);
	}
	public static String excuteHttpPostMethod(String url, String param){
		HttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
	    HttpConnectionParams.setSoTimeout(httpParams, 5000);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParams);

		HttpPost httpost = new HttpPost("http://hysick2065.cafe24.com/"+url);
		if(AppManager.DEBUG_MODE){
			httpost = new HttpPost("http://192.168.0.143:8089/wis/"+url);
		}
		
		try {
			StringEntity se  =new StringEntity( param , "UTF-8");
            se.setContentType("application/json");
            se.setContentEncoding( "UTF-8" );
			httpost.setEntity(se);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    httpost.setHeader("Accept", "application/json");
	    httpost.setHeader("Content-type", "application/json;charset=UTF-8");
	    
	    String result = null;
	    try {
			HttpResponse response = httpclient.execute(httpost);
			if(response.getStatusLine().getStatusCode()==HttpURLConnection.HTTP_OK){
				result = EntityUtils.toString(response.getEntity());
			}
			else if(response.getStatusLine().getStatusCode()==HttpURLConnection.HTTP_NOT_FOUND){
				result = AppManager.HTTP_ERROR;			
			}
			else if(response.getStatusLine().getStatusCode()==HttpURLConnection.HTTP_UNAUTHORIZED){
				result = AppManager.HTTP_ERROR;
			}
			else{
				result = AppManager.HTTP_ERROR;
			}			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return result;
	}


	public static String cancelcomment(int scheduleNo, int cno) {
		// TODO Auto-generated method stub
		JSONObject requestInfo = new JSONObject();
		try
		{
			requestInfo.put("sno", scheduleNo);
			requestInfo.put("cno", cno);
		} 
		catch (JSONException e1)
		{
			e1.printStackTrace();
		}					
		
		return excuteHttpPostMethod("commentdelete.do", requestInfo.toString());
	}


	public static String rejectEnterRequest(int no, String senderEmail) {
		// TODO Auto-generated method stub	
		JSONObject requestInfo = new JSONObject();
		try
		{
			requestInfo.put("sno", no);
			requestInfo.put("sender", senderEmail);
		} 
		catch (JSONException e1)
		{
			e1.printStackTrace();
		}					
		
		return excuteHttpPostMethod("reject_enter_request.do", requestInfo.toString());
	}


	private static List<Schedule> ParsingSchedule(String str)
	{
		ArrayList<Schedule> list = new ArrayList<Schedule>();
		if(str!=null)
		{
		JSONTokener jtk = new JSONTokener(str);
			try 
			{
				JSONArray ja = (JSONArray) jtk.nextValue();
				for (int i = 0; i < ja.length(); i++)
				{
					JSONObject job = ja.getJSONObject(i);
					int sno = job.getInt("no");
					String title = job.getString("title");
					String memo = job.getString("memo");
					boolean isOpen = job.getBoolean("isopen");
					boolean isPublic = job.getBoolean("ispublic");
					int type = job.getInt("type");
					int startYear = job.getInt("year");
					int startMonth = job.getInt("month");
					int startDay = job.getInt("day");
					int startHour = job.getInt("hour");
					int startMin = job.getInt("min");
					
					list.add(new Schedule(sno, title, memo, new GregorianCalendar(startYear,startMonth,startDay,startHour,startMin), type, isOpen, isPublic));
				}
			}
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
		}
		return list;
	}


	private static ArrayList<EntryInfo> parseOfEntryInfo(String entryInfo) {
		// TODO Auto-generated method stub
		JSONTokener jtk = new JSONTokener(entryInfo);
		JSONArray ja = null;
		try 
		{
			ja = (JSONArray) jtk.nextValue();
			
			ArrayList<EntryInfo> list = new ArrayList<EntryInfo>();
			for (int i = 0; i < ja.length(); i++)
			{
				JSONObject job = ja.getJSONObject(i);
				String email = job.getString("email");
				String name = job.getString("name");
				boolean isOwner = job.getBoolean("isowner");
				
				list.add(new EntryInfo(name, email, isOwner));
			}
			
			return list;
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}	
		return null;
	}


	private static ArrayList<CommentInfo> parseOfCommentInfo(String commenti) {
		// TODO Auto-generated method stub
		JSONTokener jtk = new JSONTokener(commenti);
		JSONArray ja = null;
		ArrayList<CommentInfo> list = null;
		try 
		{
			ja = (JSONArray) jtk.nextValue();
			list = new ArrayList<CommentInfo>();
			for(int i = 0; i<ja.length(); i++)
			{
				JSONObject job = ja.getJSONObject(i);
				int cno = job.getInt("no");
				String comment = job.getString("content");
				String wirtern = job.getString("name");
				String writere = job.getString("email");
				int startYear = job.getInt("year");
				int startMonth = job.getInt("month");
				int startDay = job.getInt("day");
				int startHour = job.getInt("hour");
				int startMin = job.getInt("min");
				list.add(new CommentInfo(cno,comment,wirtern,writere,new GregorianCalendar(startYear,startMonth,startDay,startHour,startMin)));
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}	
		return list;
	}


	private static ArrayList<ScheduleList> friendScheduleSetting(String str,ArrayList<String> friendsEmailList)
	{
		// TODO Auto-generated method stub
		ArrayList<ScheduleList> list = new ArrayList<ScheduleList>();
		try {
			JSONTokener jtk = new JSONTokener(str);
			JSONObject jo = (JSONObject) jtk.nextValue();
	
			for (String email : friendsEmailList) {
				JSONArray ja = jo.getJSONArray(email);
				if (ja == null)
					continue;
	
				ArrayList<Schedule> fslist = new ArrayList<Schedule>();
				for (int i = 0; i < ja.length(); i++) {
					JSONObject job = ja.getJSONObject(i);
					int sno = job.getInt("no");
					String title = job.getString("title");
					String memo = job.getString("memo");
					boolean isOpen = job.getBoolean("isopen");
					boolean isPublic = job.getBoolean("ispublic");
					int type = job.getInt("type");
					int startYear = job.getInt("year");
					int startMonth = job.getInt("month");
					int startDay = job.getInt("day");
					int startHour = job.getInt("hour");
					int startMin = job.getInt("min");
	
					fslist.add(new Schedule(sno, title, memo,
							new GregorianCalendar(startYear, startMonth,
									startDay, startHour, startMin), type,
							isOpen, isPublic));
				}
				list.add(new ScheduleList(fslist));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}


	private static HashMap<String,ArrayList<Schedule>> friendScheduleSetting2(String str,ArrayList<String> friendsEmailList)
	{
		// TODO Auto-generated method stub
		HashMap<String,ArrayList<Schedule>> list = new HashMap<String,ArrayList<Schedule>>();
		try {
			JSONTokener jtk = new JSONTokener(str);
			JSONObject jo = (JSONObject) jtk.nextValue();
	
			for (String email : friendsEmailList) {
				JSONArray ja = jo.getJSONArray(email);
				if (ja == null)
					continue;
				if(ja.length()==0)
				{
					continue;
				}
				ArrayList<Schedule> fslist = new ArrayList<Schedule>();
				for (int i = 0; i < ja.length(); i++) {
					
					JSONObject job = ja.getJSONObject(i);
					int sno = job.getInt("no");
					String title = job.getString("title");
					String memo = job.getString("memo");
					boolean isOpen = job.getBoolean("isopen");
					boolean isPublic = job.getBoolean("ispublic");
					int type = job.getInt("type");
					int startYear = job.getInt("year");
					int startMonth = job.getInt("month");
					int startDay = job.getInt("day");
					int startHour = job.getInt("hour");
					int startMin = job.getInt("min");
	
					fslist.add(new Schedule(sno, title, memo,
							new GregorianCalendar(startYear, startMonth,
									startDay, startHour, startMin), type,
							isOpen, isPublic));
				}
				list.put(email,fslist);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}


	private static ArrayList<RequestInfo> parseRequestInfo(String data) {
		// TODO Auto-generated method stub
		ArrayList<RequestInfo> list = new ArrayList<RequestInfo>();
		JSONTokener jtk = new JSONTokener(data);
		
			JSONArray jar;
			try {
				jar = (JSONArray) jtk.nextValue();
				for(int i=0;i<jar.length();i++)
		        {
		        	JSONObject jo = jar.getJSONObject(i);
		        	int no = jo.getInt("sno");
		        	String title = jo.getString("title");
		        	String senderEmail = jo.getString("senderemail");
		        	String senderName = jo.getString("sendername");
		        	String receiverEmail = jo.getString("receiveremail");
		        	String receiverName = jo.getString("receivername");
		        	int type = jo.getInt("type");
		        	
		        	list.add(new RequestInfo(no,title, senderEmail, senderName, receiverEmail, receiverName, type));
		        }
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		
		return list;
	}


	private static ArrayList<CntInfo> parsecnt(String str)
	{
		ArrayList<CntInfo> list = new ArrayList<CntInfo>();
		JSONTokener jtk = new JSONTokener(str);
		
		JSONArray jar;
			try {
				jar = (JSONArray) jtk.nextValue();
				for(int i=0;i<jar.length();i++)
		        {
		        	JSONObject jo = jar.getJSONObject(i);
		        	int sno = jo.getInt("sno");
		        	int ccnt = jo.getInt("commentCnt");
		           	int ecnt = jo.getInt("entryCnt");
		        	list.add(new CntInfo(sno,ccnt,ecnt));
		        }
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		
		return list;
	}
 }

