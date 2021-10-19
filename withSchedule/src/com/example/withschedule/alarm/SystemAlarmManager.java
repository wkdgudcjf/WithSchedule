package com.example.withschedule.alarm;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.withschedule.AlarmReceiver;
import com.example.withschedule.AppManager;
import com.example.withschedule.db.ExcuteDB;

public class SystemAlarmManager
{
	public static void reenrollAlarm(Context context,String[] str)
	{
		Calendar cd = Calendar.getInstance();
		switch(Integer.parseInt(str[4]))
		{
			case 1:
				return;
			case 2: 				
				cd.set(Calendar.DATE, cd.get(Calendar.DATE)+7);
				break;
			case 3:
				cd.set(Calendar.MONTH, cd.get(Calendar.MONTH)+1);
				cd.set(Calendar.DATE, cd.getActualMaximum(Calendar.DAY_OF_MONTH));
				break;
			case 4:
				Calendar tempcd = Calendar.getInstance();
				boolean check = true;
				while(check)
				{
					cd.set(Calendar.MONTH, cd.get(Calendar.MONTH)+1);
					if(cd.getActualMinimum(Calendar.DATE)<=tempcd.get(Calendar.DATE)&&tempcd.get(Calendar.DATE)<=cd.getActualMaximum(Calendar.MONTH));
					{
						check = false;
					}
				}
				cd.set(Calendar.DATE, tempcd.get(Calendar.DATE));
				break;
			case 5:
				if(cd.get(Calendar.MONTH)==1&&cd.get(Calendar.DATE)==29)
				{
					cd.set(Calendar.YEAR, cd.get(Calendar.YEAR)+4);
				}
				else
				{
					cd.set(Calendar.YEAR, cd.get(Calendar.YEAR)+1);
				}
				break;
		}
				
		AlarmManager alm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmReceiver.class);
		PendingIntent sender;	
		String str2 = cd.get(Calendar.AM_PM) == 0 ? "오전" : "오후";
		str[1]=cd.get(Calendar.YEAR)+"년-"+(cd.get(Calendar.MONTH)+1)+"월-"+cd.get(Calendar.DATE)+"일 "+str2+" "+cd.get(Calendar.HOUR)+"시 "+cd.get(Calendar.MINUTE)+"분";	
		intent.putExtra("alarmdata", str);
		sender = PendingIntent.getBroadcast(context, Integer.parseInt(str[3]) , intent, PendingIntent.FLAG_UPDATE_CURRENT);
		alm.set(AlarmManager.RTC_WAKEUP, cd.getTimeInMillis(), sender);
		ExcuteDB.insertAlarm(context, Integer.parseInt(str[3]), true, Integer.parseInt(str[2]));
		
	}
	public static void enrollAlarm(Context context,String title,GregorianCalendar gc,int type,int no,int atm)
	{
		AlarmManager alm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent;
		PendingIntent sender;
		String adata[] =new String[5];
		adata[0]=title;
		String str = gc.get(Calendar.AM_PM) == 0 ? "오전" : "오후";
		adata[1]=gc.get(Calendar.YEAR)+"년-"+(gc.get(Calendar.MONTH)+1)+"월-"+gc.get(Calendar.DATE)+"일 "+str+" "+gc.get(Calendar.HOUR)+"시 "+gc.get(Calendar.MINUTE)+"분";
		adata[2]=atm+"";
		adata[3]=no+"";
		adata[4]=type+"";
		intent = new Intent(context,AlarmReceiver.class);
		intent.putExtra("alarmdata", adata);
		sender = PendingIntent.getBroadcast(context,no, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		GregorianCalendar at = (GregorianCalendar) gc.clone();
		at.set(Calendar.MINUTE, gc.get(Calendar.MINUTE)-atm);
		alm.set(AlarmManager.RTC_WAKEUP, at.getTimeInMillis(), sender);
	}
	public static void cancelAlarm(Context context,int sno)
	{
		AlarmManager alm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context,AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, sno , intent, 0);
		alm.cancel(sender);
	}
	public static void typeenrollAlarm(Context context,String title, GregorianCalendar rgc, int type, int sno,int atm) 
	{
		Calendar cd = Calendar.getInstance();
		GregorianCalendar gc = (GregorianCalendar) rgc.clone();
		switch(type)
		{
			case AppManager.GENERAL: 
				break;
			case AppManager.WEEK_REPEAT:
				cd.set(Calendar.MINUTE, cd.get(Calendar.MINUTE)+atm);
				if(gc.compareTo(cd)>0)
				{
					;
				}
				else
				{
					cd.set(Calendar.MINUTE, cd.get(Calendar.MINUTE)-atm);
					int a = (int)((cd.getTimeInMillis()-gc.getTimeInMillis())/(1000*60*60*24))/7;
					gc.set(Calendar.DATE, (gc.get(Calendar.DATE)+7*(a+1)));
				}
				break;
			case AppManager.LASTDAY_REPEAT: 
				cd.set(Calendar.MINUTE, cd.get(Calendar.MINUTE)+atm);
				if(gc.compareTo(cd)>0)
				{
					;
				}
				else
				{
					cd.set(Calendar.MINUTE, cd.get(Calendar.MINUTE)-atm);
					gc.set(Calendar.YEAR, cd.get(Calendar.YEAR));
					gc.set(Calendar.MONTH, cd.get(Calendar.MONTH));
					gc.set(Calendar.DATE, cd.getActualMaximum((Calendar.DAY_OF_MONTH)));
					cd.set(Calendar.MINUTE, cd.get(Calendar.MINUTE)+atm);
					if(gc.compareTo(cd)>0)
					{
						;
					}
					else
					{
						gc.set(Calendar.MONTH, cd.get(Calendar.MONTH)+1);
						gc.set(Calendar.DATE, cd.getActualMaximum((Calendar.MONTH)));
					}
				}
				break;
			case AppManager.MONTH_REPEAT: 
				cd.set(Calendar.MINUTE, cd.get(Calendar.MINUTE)+atm);
				if(gc.compareTo(cd)>0)
				{
					;
				}
				else
				{
					cd.set(Calendar.MINUTE, cd.get(Calendar.MINUTE)-atm);
					gc.set(Calendar.YEAR, cd.get(Calendar.YEAR));
					gc.set(Calendar.MONTH, cd.get(Calendar.MONTH));
					cd.set(Calendar.MINUTE, cd.get(Calendar.MINUTE)+atm);
					if(gc.compareTo(cd)>0)
					{
						;
					}
					else
					{
						cd.set(Calendar.MINUTE, cd.get(Calendar.MINUTE)-atm);
						boolean check = true;;
						while(check)
						{
							cd.set(Calendar.MONTH, cd.get(Calendar.MONTH)+1);
							if(cd.getActualMinimum(Calendar.DATE)<=gc.get(Calendar.DATE)&&gc.get(Calendar.DATE)<=cd.getActualMaximum(Calendar.MONTH));
							{
								check = false;
							}
						}
						gc.set(Calendar.MONTH, cd.get(Calendar.MONTH));
					}
				}
				break;
			case AppManager.YEAR_REPEAT: 
				cd.set(Calendar.MINUTE, cd.get(Calendar.MINUTE)+atm);
				if(gc.compareTo(cd)>0)
				{
					;
				}
				else
				{
					cd.set(Calendar.MINUTE, cd.get(Calendar.MINUTE)-atm);
					gc.set(Calendar.YEAR, cd.get(Calendar.YEAR));
					cd.set(Calendar.MINUTE, cd.get(Calendar.MINUTE)+atm);
					if(gc.compareTo(cd)>0)
					{
						;
					}
					else
					{
						if(gc.get(Calendar.MONTH)==1&&gc.get(Calendar.DATE)==29)
						{
							gc.set(Calendar.YEAR, cd.get(Calendar.YEAR)+4);
						}
						else
						{
							gc.set(Calendar.YEAR, cd.get(Calendar.YEAR)+1);
						}
					}
				}
				break;
		}
		AlarmManager alm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent;
		PendingIntent sender;
		String adata[] =new String[5];
		adata[0]=title;
		String str = gc.get(Calendar.AM_PM) == 0 ? "오전" : "오후";
		adata[1]=gc.get(Calendar.YEAR)+"년-"+(gc.get(Calendar.MONTH)+1)+"월-"+gc.get(Calendar.DATE)+"일 "+str+" "+gc.get(Calendar.HOUR)+"시 "+gc.get(Calendar.MINUTE)+"분";
		adata[2]=atm+"";
		adata[3]=sno+"";
		adata[4]=type+"";
		intent = new Intent(context,AlarmReceiver.class);
		intent.putExtra("alarmdata", adata);
		sender = PendingIntent.getBroadcast(context,sno, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		GregorianCalendar at = (GregorianCalendar) gc.clone();
		at.set(Calendar.MINUTE, gc.get(Calendar.MINUTE)-atm);
		alm.set(AlarmManager.RTC_WAKEUP, at.getTimeInMillis(), sender);
		
	}

}
