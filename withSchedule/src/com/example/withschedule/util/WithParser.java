package com.example.withschedule.util;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

import com.example.withschedule.db.ExcuteDB;
import com.example.withschedule.friend.Friend;

public class WithParser 
{
	public static String addressBookParse(Context context,String email)
	{
		List<String> str = ExcuteDB.getAddressBook(context);
		
        JSONArray join = new JSONArray();
        JSONArray phonebook = new JSONArray();
		try
		{
			join.put(new JSONObject().put("email", email));
			for(int i=0;i<str.size();i++)
			{
				phonebook.put(str.get(i));
			}
			join.put(phonebook);
		} 
		catch (JSONException e1)
		{
			e1.printStackTrace();
		}					
		return join.toString();
	}

	public static void friendsetting(Context context,String readMessage) 
	{
		JSONTokener jtk = new JSONTokener(readMessage);
		try 
		{
			JSONArray ja = (JSONArray) jtk.nextValue();
			List<Friend> list = new ArrayList<Friend>();
			for (int i = 0; i < ja.length(); i++)
			{
				JSONObject job = ja.getJSONObject(i);
				String phoneNum = job.getString("phoneNum");
				String nickname = job.getString("name");
				String email = job.getString("email");
				if (phoneNum.equals("null")) {
					phoneNum = "x";
				}
				list.add(new Friend(nickname, email, phoneNum));
			}
			ExcuteDB.deleteAllfreind(context);
			ExcuteDB.insertFriend(context, list);
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}		
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, Boolean> ownerCheckParse(String result) {
		// TODO Auto-generated method stub
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		JSONTokener parser = new JSONTokener(result);
		try {
			JSONObject obj = (JSONObject)parser.nextValue();
			
			Iterator<String> it = obj.keys();
			while(it.hasNext()){
				String key = it.next();
				map.put(key, obj.getBoolean(key));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}
}
