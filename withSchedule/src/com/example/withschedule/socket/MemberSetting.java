package com.example.withschedule.socket;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.apache.http.*;
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

import com.example.withschedule.*;
import com.example.withschedule.db.ExcuteDB;
import com.example.withschedule.dto.*;
import com.example.withschedule.util.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.*;

public class MemberSetting 
{	
	public static String login(Context context, String email, String pw)
	{
		TelephonyManager telManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
		String phoneNum = telManager.getLine1Number();
		
		if(phoneNum==null) phoneNum="01063722066";
		
		if(phoneNum.startsWith("+82")){
			phoneNum = phoneNum.replace("+82", "0");
		}
		
		
		JSONObject memberInfo = new JSONObject();
		try {
			memberInfo.put("phoneNum", phoneNum);
			memberInfo.put("email", email);
			memberInfo.put("password",pw);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}					
		
		String result = excuteHttpPostMethod("login.do", memberInfo.toString());
		if(result.equals(AppManager.HTTP_ERROR)){
			return AppManager.HTTP_ERROR;
		}
		
		String check = initScheduleData(context,result);
		
		if(check.equals(AppManager.COMPLETION)){
			SharedPreferences pref = context.getSharedPreferences("withschedule",0);
			SharedPreferences.Editor edit = pref.edit();
			edit.putString("login", "true");//스트링을 넣는다 첫번째는 키값 두번째는 실제값
			edit.putString("email", email);
			edit.putString("password", pw);
	        edit.commit();//저장 시작
	       return AppManager.COMPLETION;
		}
		else if(check.equals(AppManager.NO_SEARCH)){
			return AppManager.NO_SEARCH;
		}
		else{
			return AppManager.MISSMATCH_PASSWORD;
		}		
	}
	
	private static String initScheduleData(Context context, String result){
		JSONTokener jtk = new JSONTokener(result);
		
		JSONArray jar;
		try {
			jar = (JSONArray) jtk.nextValue();		
		
			JSONObject obj = (JSONObject)jar.get(0);
			String check=obj.getString("result");
			if(check.equals(AppManager.COMPLETION)){
				String name=obj.getString("name");				
				SharedPreferences pref = context.getSharedPreferences("withschedule",0);
				SharedPreferences.Editor edit = pref.edit();
				edit.putString("login", "true");//스트링을 넣는다 첫번째는 키값 두번째는 실제값
				edit.putString("name", name);
		        edit.commit();//저장 시작
		        
		        JSONArray ja = (JSONArray)jar.get(1);
		        for(int i=0;i<ja.length();i++)
		        {
		        	JSONObject jo = ja.getJSONObject(i);
		        	int sno = jo.getInt("sno");
		        	String title = jo.getString("title");
		        	String memo = jo.getString("memo");
		        	int type = jo.getInt("type");
		        	boolean isopen = jo.getBoolean("isopen");
		        	boolean ispublic = jo.getBoolean("ispublic");
		        	int year = jo.getInt("year");
		        	int mon = jo.getInt("month");
		        	int day = jo.getInt("day");
		        	int hour = jo.getInt("hour");
		        	int min = jo.getInt("min");
		        	ExcuteDB.insertSchedule(context, sno, title, new GregorianCalendar(year,mon,day,hour,min), type, isopen, ispublic, memo);
		        }
		        return AppManager.COMPLETION;
			}
			else if(check.equals(AppManager.NO_SEARCH)){
				return AppManager.NO_SEARCH; 
			}
			else if(check.equals(AppManager.MISSMATCH_PASSWORD)){
				return AppManager.MISSMATCH_PASSWORD;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return AppManager.SYSTEM_ERROR;
	}

	public static String memberModify(Context context, String preemial, String afteremail,String aftername,String apw)
	{		
		JSONArray MArray = new JSONArray();
		JSONObject EmailInfo = new JSONObject();
		JSONObject memberInfo = new JSONObject();
		try
		{
			EmailInfo.put("email", preemial);
			memberInfo.put("email", afteremail);
			memberInfo.put("name", aftername);
			memberInfo.put("password", apw);
			MArray.put(EmailInfo);
			MArray.put(memberInfo);
		} 
		catch (JSONException e1)
		{
			e1.printStackTrace();
		}					
		
		String result = excuteHttpPostMethod("membermodify.do", MArray.toString());
		
		if(result.equals(AppManager.COMPLETION))
		{
			SharedPreferences pref = context.getSharedPreferences("withschedule",0);
			SharedPreferences.Editor edit = pref.edit();
			edit.putString("email", afteremail);
			edit.putString("name", aftername);
			edit.putString("password", apw);
	        edit.commit();//저장 시작
			return AppManager.COMPLETION;
		}
		else if(result.equals(AppManager.NO_SEARCH)){
			return AppManager.NO_SEARCH;
		}
		else if(result.equals(AppManager.DOUBLE_EMAIL)){
			return AppManager.DOUBLE_EMAIL;
		}
		else if(result.equals(AppManager.HTTP_ERROR)){
			return AppManager.HTTP_ERROR;
		}
		else
		{
			return AppManager.NO_SEARCH;
		}
	}
	
	public static String remove(Context context, String email)
	{		
		String result = excuteHttpPostMethod("memberdelete.do", email);
		
		if(result.equals(AppManager.COMPLETION))
		{
			SharedPreferences pref = context.getSharedPreferences("withschedule",0);
			SharedPreferences.Editor edit = pref.edit();
			edit.putString("login", "false");
			edit.clear();
			edit.commit();//저장 시작
			ExcuteDB.clear(context);
			return AppManager.COMPLETION;
		}
		else if(result.equals(AppManager.NO_SEARCH))
		{
			return AppManager.NO_SEARCH;
		}
		return AppManager.SYSTEM_ERROR;
	}


	public static String addFriend(Context context, String myemail,String friendemail)
	{
		JSONObject memberInfo = new JSONObject();
		try {
			memberInfo.put("myemail", myemail);
			memberInfo.put("friendemail", friendemail);;
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}					
		String result = excuteHttpPostMethod("addfriend.do", memberInfo.toString());
   		
		if(result.equals(AppManager.COMPLETION)){
			return AppManager.COMPLETION;
		}
		else if(result.equals(AppManager.DUPLICATE)){
			return AppManager.DUPLICATE;
		}
		else if(result.equals(AppManager.HTTP_ERROR)){
			return AppManager.HTTP_ERROR;
		}
		else{
			return AppManager.SYSTEM_ERROR;
		}
	}
	
	public static ArrayList<FriendInfo> friendRequest(String myemail)
	{
		String result=null;
		result = excuteHttpPostMethod("friendrequest.do", myemail);
		if(result.equals(AppManager.HTTP_ERROR)){
			return null;
		}
		return parsefriendRequest(result);
	}
	
	private static ArrayList<FriendInfo> parsefriendRequest(String result)
	{
		ArrayList<FriendInfo> list = new ArrayList<FriendInfo>();
		try {
			JSONTokener jtk = new JSONTokener(result);
			JSONArray ja = (JSONArray) jtk.nextValue();
			for (int i = 0; i < ja.length(); i++) {
				JSONObject job = ja.getJSONObject(i);
				String email = job.getString("email");
				String name = job.getString("name");
				list.add(new FriendInfo(email,name));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public static String join(Context context, String email,String name,String pw)
	{
		String check=null;				
		
		TelephonyManager telManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
		String phoneNum = telManager.getLine1Number();
		if(phoneNum==null) phoneNum="01063722066";
		if(phoneNum.contains("+82"))
		{
			phoneNum = phoneNum.replace("+82", "0");
		}
		
		JSONObject memberInfo = new JSONObject();
		try
		{
			memberInfo.put("phoneNum", phoneNum);
			memberInfo.put("email", email);
			memberInfo.put("name", name);
			memberInfo.put("password", pw);
		} 
		catch (JSONException e1)
		{
			e1.printStackTrace();
		}					
		
		check = excuteHttpPostMethod("join.do", memberInfo.toString());
	    
		if(check.equals(AppManager.COMPLETION)){
			return AppManager.COMPLETION;
		}	      
		else if(check.equals(AppManager.DOUBLE_EMAIL)){
			return AppManager.DOUBLE_EMAIL;
		}
		else if(check.equals(AppManager.HTTP_ERROR)){
			return AppManager.HTTP_ERROR;
		}
		else{
			return "-";
		}
	}
	
	public static String friendSetting(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences pref = context.getSharedPreferences("withschedule", 0);
		String email = pref.getString("email", "");
		
		String result = excuteHttpPostMethod("setting.do", WithParser.addressBookParse(context,email));
		
		if(result.equals(AppManager.HTTP_ERROR)){
			return AppManager.HTTP_ERROR;
		}
		
		WithParser.friendsetting(context,result);	
		AppManager.getInstance().getFm().getFriendList().setList(ExcuteDB.selectFriend(context));
		return AppManager.COMPLETION;
	}
	
	public static void initSetting(Context context, String email,String name,String pw)
	{
		SharedPreferences pref = context.getSharedPreferences("withschedule",0);
		SharedPreferences.Editor edit = pref.edit();
		edit.putString("login", "true");//스트링을 넣는다 첫번째는 키값 두번째는 실제값
		edit.putString("email", email);
		edit.putString("name", name);
		edit.putString("password", pw);
        edit.commit();//저장 시작
	}	
	
	public static void gcmregister(Context context,String regId)
	{
		JSONObject memberInfo = new JSONObject();
		SharedPreferences pref = context
				.getSharedPreferences("withschedule", 0);
		String email = pref.getString("email", "");

		try {
			memberInfo.put("email", email);
			memberInfo.put("key", regId);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String result = excuteHttpPostMethod("pushsetting.do", memberInfo.toString());
		
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
}
