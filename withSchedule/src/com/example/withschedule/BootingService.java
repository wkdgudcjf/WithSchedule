package com.example.withschedule;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.example.withschedule.alarm.Alarm;
import com.example.withschedule.db.ExcuteDB;

import com.example.withschedule.schedule.Schedule;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class BootingService extends Service
{
	private List<Schedule> sList;
	private List<Alarm> aList;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() 
	{
		super.onCreate();
		sList =	ExcuteDB.selectSchedule(BootingService.this);
		aList = ExcuteDB.selectAlarm(BootingService.this);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {

		super.onStart(intent, startId);
		
		AlarmThread thread = new AlarmThread(sList,aList);
		thread.start();
	}
	class AlarmThread extends Thread
	{
		private List<Schedule> sList;
		private List<Alarm> aList;
		public AlarmThread(List<Schedule> slist,List<Alarm> alist)
		{
			this.sList=slist;
			this.aList=alist;
		}
		public void run()
		{
			for(int i=0;i<this.aList.size();i++)
			{
				for(int j=0;j<this.sList.size();j++)
				{
					if(sList.get(j).getNo()==aList.get(i).getNo())
					{
						GregorianCalendar startdate = sList.get(j).getStartTime();
						
						Calendar nowdate = Calendar.getInstance();
						switch(sList.get(j).getType())
						{
							case AppManager.GENERAL:
								startdate.set(Calendar.MINUTE, startdate.get(Calendar.MINUTE)-aList.get(i).getAlarmtime());
								if(startdate.compareTo(Calendar.getInstance())>=0)
								{
									startdate.set(Calendar.MINUTE, startdate.get(Calendar.MINUTE)+aList.get(i).getAlarmtime());
								}
								else
								{
									continue;
								}
								break;
							case AppManager.WEEK_REPEAT:
								nowdate.set(Calendar.MINUTE, nowdate.get(Calendar.MINUTE)+aList.get(i).getAlarmtime());
								if(startdate.compareTo(nowdate)>0)
								{
									;
								}
								else
								{
									nowdate.set(Calendar.MINUTE, nowdate.get(Calendar.MINUTE)-aList.get(i).getAlarmtime());
									int a = (int)((nowdate.getTimeInMillis()-startdate.getTimeInMillis())/(1000*60*60*24))/7;
									startdate.set(Calendar.DATE, (startdate.get(Calendar.DATE)+7*(a+1)));
								}
								break;
							case AppManager.LASTDAY_REPEAT: 
								nowdate.set(Calendar.MINUTE, nowdate.get(Calendar.MINUTE)+aList.get(i).getAlarmtime());
								if(startdate.compareTo(nowdate)>0)
								{
									;
								}
								else
								{
									nowdate.set(Calendar.MINUTE, nowdate.get(Calendar.MINUTE)-aList.get(i).getAlarmtime());
									startdate.set(Calendar.YEAR, nowdate.get(Calendar.YEAR));
									startdate.set(Calendar.MONTH, nowdate.get(Calendar.MONTH));
									startdate.set(Calendar.DATE, nowdate.getActualMaximum((Calendar.DAY_OF_MONTH)));
									if(startdate.compareTo(nowdate)>0)
									{
										;
									}
									else
									{
										startdate.set(Calendar.MONTH, nowdate.get(Calendar.MONTH)+1);
										startdate.set(Calendar.DATE, nowdate.getActualMaximum((Calendar.DAY_OF_MONTH)));
									}
								}
								break;
							case AppManager.MONTH_REPEAT: 
								nowdate.set(Calendar.MINUTE, nowdate.get(Calendar.MINUTE)+aList.get(i).getAlarmtime());
								if(startdate.compareTo(nowdate)>0)
								{
									;
								}
								else
								{
									nowdate.set(Calendar.MINUTE, nowdate.get(Calendar.MINUTE)-aList.get(i).getAlarmtime());
									startdate.set(Calendar.YEAR, nowdate.get(Calendar.YEAR));
									startdate.set(Calendar.MONTH, nowdate.get(Calendar.MONTH));
									nowdate.set(Calendar.MINUTE, nowdate.get(Calendar.MINUTE)+aList.get(i).getAlarmtime());
									if(startdate.compareTo(nowdate)>0)
									{
										;
									}
									else
									{
										nowdate.set(Calendar.MINUTE, nowdate.get(Calendar.MINUTE)-aList.get(i).getAlarmtime());
										boolean check = true;;
										while(check)
										{
											nowdate.set(Calendar.MONTH, nowdate.get(Calendar.MONTH)+1);
											if(nowdate.getActualMinimum(Calendar.DATE)<=startdate.get(Calendar.DATE)&&startdate.get(Calendar.DATE)<=nowdate.getActualMaximum(Calendar.MONTH));
											{
												check = false;
											}
										}
										startdate.set(Calendar.MONTH, nowdate.get(Calendar.MONTH));
									}
								}
								break;
							case AppManager.YEAR_REPEAT: 
								nowdate.set(Calendar.MINUTE, nowdate.get(Calendar.MINUTE)+aList.get(i).getAlarmtime());
								if(startdate.compareTo(nowdate)>0)
								{
									;
								}
								else
								{
									nowdate.set(Calendar.MINUTE, nowdate.get(Calendar.MINUTE)-aList.get(i).getAlarmtime());
									startdate.set(Calendar.YEAR, nowdate.get(Calendar.YEAR));
									nowdate.set(Calendar.MINUTE, nowdate.get(Calendar.MINUTE)+aList.get(i).getAlarmtime());
									if(startdate.compareTo(nowdate)>0)
									{
										;
									}
									else
									{
										if(startdate.get(Calendar.MONTH)==1&&startdate.get(Calendar.DATE)==29)
										{
											startdate.set(Calendar.YEAR, nowdate.get(Calendar.YEAR)+4);
										}
										else
										{
											startdate.set(Calendar.YEAR, nowdate.get(Calendar.YEAR)+1);
										}
									}
								}
								break;
						}
						AlarmManager alm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
						Intent intent;
						PendingIntent sender;
						String adata[] =new String[5];
						adata[0]=sList.get(j).getTitle();		
						String str3 = sList.get(j).getStartTime().get(Calendar.AM_PM) == 0 ? "오전" : "오후";
						adata[1]=sList.get(j).getStartTime().get(Calendar.YEAR)+"년-"+(sList.get(j).getStartTime().get(Calendar.MONTH)+1)+"월-"+sList.get(j).getStartTime().get(Calendar.DATE)+"일 "+str3+" "+sList.get(j).getStartTime().get(Calendar.HOUR)+"시 "+sList.get(j).getStartTime().get(Calendar.MINUTE)+"분";
						adata[2]=aList.get(i).getAlarmtime()+"";
						adata[3]=sList.get(j).getNo()+"";
						adata[4]=sList.get(j).getType()+"";
						intent = new Intent(BootingService.this,AlarmReceiver.class);
						intent.putExtra("alarmdata", adata);
						sender = PendingIntent.getBroadcast(BootingService.this, sList.get(j).getNo() , intent, PendingIntent.FLAG_UPDATE_CURRENT);
						GregorianCalendar at = (GregorianCalendar) startdate.clone();
						at.set(Calendar.MINUTE, startdate.get(Calendar.MINUTE)-aList.get(i).getAlarmtime());
						alm.set(AlarmManager.RTC_WAKEUP, at.getTimeInMillis(), sender);
					}
				}
			}
		}
	}
}


