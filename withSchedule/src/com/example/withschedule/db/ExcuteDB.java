package com.example.withschedule.db;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.example.withschedule.alarm.Alarm;
import com.example.withschedule.friend.Friend;
import com.example.withschedule.schedule.Schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

public class ExcuteDB 
{
	private static WithsDB wdb;
		
/*	static public void insertPhoneBook(Context content,List<String> list)
	{
		wdb = new WithsDB(content);
		SQLiteDatabase db = wdb.getWritableDatabase();
		for(int i=0;i<list.size();i++)
		{
			ContentValues row = new ContentValues();
			row.put("phonenum", list.get(i));
			db.insert("phonebook_tb", null, row);
		}
		wdb.close();
	}*/
	static public void insertAlarm(Context content,int ano,boolean set,int time)
	{
		wdb = new WithsDB(content);
		SQLiteDatabase db = wdb.getWritableDatabase();
		ContentValues row = new ContentValues();
		int ip = set == true ? 0 : 1;
		row.put("ano", ano);
		row.put("isset",ip);
		row.put("time", time);
		db.insert("alarm_tb", null, row);
		wdb.close();
	}
	static public void deleteAlarm(Context context,int ano)
	{
		wdb = new WithsDB(context);
		SQLiteDatabase db = wdb.getWritableDatabase();
		db.delete("alarm_tb", "ano = '"+ano+"'", null);
		wdb.close();
	}
	static public void updateAlarm(Context content,int ano,boolean set,int time)
	{
		wdb = new WithsDB(content);
		SQLiteDatabase db = wdb.getWritableDatabase();
		ContentValues row = new ContentValues();
		int ip = set == true ? 0 : 1;
		row.put("ano", ano);
		row.put("isset",ip);
		row.put("time", time);
		db.update("alarm_tb", row, "ano = '"+ano+"'", null);
		wdb.close();
	}
	static public void insertSchedule(Context content,int sno,String title,GregorianCalendar starttime,int type,boolean isopen,boolean ispublic,String memo)
	{
		wdb = new WithsDB(content);
		SQLiteDatabase db = wdb.getWritableDatabase();
		ContentValues row = new ContentValues();
		row.put("sno", sno);
		row.put("title", title);
		row.put("starttime", starttime.getTimeInMillis()+"");
		row.put("type", type);
		row.put("memo", memo);
		int iop = isopen == true ? 0 : 1;
		row.put("isopen", iop);
		int ipb = ispublic == true ? 0 : 1;
		row.put("ispublic", ipb);
		db.insert("schedule_tb", null, row);
		wdb.close();
	}
	static public void deleteSchedule(Context content,int sno)
	{
		wdb = new WithsDB(content);
		SQLiteDatabase db = wdb.getWritableDatabase();
		db.delete("schedule_tb", "sno = '"+sno+"'", null);
		wdb.close();
	}
	static public void updateSchedule(Context content,int sno,String title,GregorianCalendar starttime,int type,boolean isopen,boolean ispublic,String memo)
	{
		wdb = new WithsDB(content);
		SQLiteDatabase db = wdb.getWritableDatabase();
		ContentValues row = new ContentValues();
		row.put("sno", sno);
		row.put("title", title);
		row.put("starttime", starttime.getTimeInMillis()+"");
		row.put("type", type);
		row.put("memo", memo);
		int iop = isopen == true ? 0 : 1;
		row.put("isopen", iop);
		int ipb = ispublic == true ? 0 : 1;
		row.put("ispublic", ipb);
		db.update("schedule_tb", row, "sno = '"+sno+"'", null);
		wdb.close();
	}
	static public List<Schedule> selectSchedule(Context content)
	{
		ArrayList<Schedule> list = new ArrayList<Schedule>();
		WithsDB wdb = new WithsDB(content);
		Cursor cursor;
		SQLiteDatabase db = wdb.getReadableDatabase();
		cursor = db.rawQuery("SELECT * FROM schedule_tb",null);
		boolean isOpen = true;
		boolean isPublic = true;
		String title = null;
		String memo = null;
		int type=1;
		int sno =0;
		while(cursor.moveToNext())
		{
			GregorianCalendar gc = new GregorianCalendar();
			isOpen = cursor.getInt(cursor.getColumnIndex("isopen")) == 0 ? true : false;
			isPublic = cursor.getInt(cursor.getColumnIndex("ispublic")) == 0 ? true : false;
			type = cursor.getInt(cursor.getColumnIndex("type"));
			sno = cursor.getInt(cursor.getColumnIndex("sno"));
			title = cursor.getString(cursor.getColumnIndex("title"));
			memo = cursor.getString(cursor.getColumnIndex("memo"));
			gc.setTimeInMillis(Long.parseLong(cursor.getString(cursor.getColumnIndex("starttime"))));
			list.add(new Schedule(sno, title, memo, gc, type, isOpen, isPublic));
		}
		
		cursor.close();
		wdb.close();
		return list;
		
	}
	static public List<Alarm> selectAlarm(Context content)
	{
		ArrayList<Alarm> list = new ArrayList<Alarm>();
		WithsDB wdb = new WithsDB(content);
		Cursor cursor;
		SQLiteDatabase db = wdb.getReadableDatabase();
		cursor = db.rawQuery("SELECT * FROM alarm_tb",null);
		
		boolean isset = true;
		int time=1;
		int ano =0;
		while(cursor.moveToNext())
		{
			isset = cursor.getInt(cursor.getColumnIndex("isset")) == 0 ? true : false;
			time = cursor.getInt(cursor.getColumnIndex("time"));
			ano = cursor.getInt(cursor.getColumnIndex("ano"));
			list.add(new Alarm(ano, isset, time));
		}
			
		cursor.close();
		wdb.close();
		return list;
		
	}
	/*static public List<String> selectPhoneBook(Context content)
	{
		ArrayList<String> list = new ArrayList<String>();
		WithsDB wdb = new WithsDB(content);
		Cursor cursor;
		SQLiteDatabase db = wdb.getReadableDatabase();
		cursor = db.rawQuery("SELECT * FROM phonebook_tb",null);
		
		while(cursor.moveToNext())
		{
			list.add(cursor.getString(cursor.getColumnIndex("phonenum")));
		}
			
		cursor.close();
		wdb.close();
		return list;
		
	}*/
	static public ArrayList<Friend> selectFriend(Context content)
	{
		ArrayList<Friend> list = new ArrayList<Friend>();
		WithsDB wdb = new WithsDB(content);
		Cursor cursor;
		SQLiteDatabase db = wdb.getReadableDatabase();
		cursor = db.rawQuery("SELECT * FROM friend_tb",null);
		
		String email = null;
		String phonenum=null;
		String nickname =null;
		while(cursor.moveToNext())
		{
			phonenum = cursor.getString(cursor.getColumnIndex("phonenum"));
			email = cursor.getString(cursor.getColumnIndex("email"));
			nickname = cursor.getString(cursor.getColumnIndex("nickname"));
			list.add(new Friend(nickname, email, phonenum));
		}
			
		cursor.close();
		wdb.close();
		return list;
		
	}
	@SuppressWarnings("static-access")
	public static List<String> getAddressBook(Context context)
	{
		ArrayList<String> result = new ArrayList<String>();
		
	    //Map<String, String> result = new HashMap<String, String>();
		TelephonyManager telManager = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE); 
		String phoneNum = telManager.getLine1Number();
		
		
		if(phoneNum==null) phoneNum="01063722066";
		if(phoneNum.contains("+82"))
	    {
			phoneNum = phoneNum.replace("+82", "0");
		}
	    Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
	    while(cursor.moveToNext()) 
	    {
	        int phone_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
	      //  int name_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
	        String phone = cursor.getString(phone_idx);
	      //  String name = cursor.getString(name_idx);	 
	        phone = phone.replaceAll("-", "");
	        
	        if(phone.contains("+82"))
	        {
	        	phone = phone.replace("+82", "0");
			}
	        if(phone.equals(phoneNum))
	        {
	        	continue;
	        }
	       
	        if(result.size()==0)
	        {
	        	result.add(phone);
	       	}
	        else if(result.get(result.size()-1).equals(phone))
	       	{
	        	continue;
	       	}
	        else
	        {
	        	result.add(phone);
	        }	      	
	       
	       
	    }
	    
	    return result;
	}
	
	public static void insertFriend(Context context, List<Friend> list)
	{
		wdb = new WithsDB(context);
		SQLiteDatabase db = wdb.getWritableDatabase();
		for(int i=0;i<list.size();i++)
		{
			ContentValues row = new ContentValues();
			row.put("email", list.get(i).getEmail());
			row.put("phonenum", list.get(i).getPhoneNo());
			row.put("nickname", list.get(i).getNickname());
			
			db.insert("friend_tb", null, row);
		}
		wdb.close();
	}
	public static void insertFriend(Context context, Friend friend)
	{
		wdb = new WithsDB(context);
		SQLiteDatabase db = wdb.getWritableDatabase();
		ContentValues row = new ContentValues();
		row.put("email", friend.getEmail());
		row.put("phonenum", friend.getPhoneNo());
		row.put("nickname", friend.getNickname());
		db.insert("friend_tb", null, row);
		wdb.close();
	}
	/*public static void deleteAllphoneBook(Context context)
	{
		wdb = new WithsDB(context);
		SQLiteDatabase db = wdb.getWritableDatabase();
		db.delete("phonebook_tb", null, null);
		wdb.close();
	}*/
	public static void deleteAllfreind(Context context)
	{
		wdb = new WithsDB(context);
		SQLiteDatabase db = wdb.getWritableDatabase();
		db.delete("friend_tb", null, null);
		wdb.close();
	}
	
	public static void clear(Context context)
	{
		wdb = new WithsDB(context);
		SQLiteDatabase db = wdb.getWritableDatabase();
		db.delete("friend_tb", null, null);
		db.delete("alarm_tb", null, null);
	    db.delete("schedule_tb", null, null);
		wdb.close();
	}
}
