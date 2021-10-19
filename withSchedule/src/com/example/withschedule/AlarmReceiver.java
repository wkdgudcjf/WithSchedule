package com.example.withschedule;


import com.example.withschedule.alarm.SystemAlarmManager;
import com.example.withschedule.db.ExcuteDB;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public class AlarmReceiver extends BroadcastReceiver{
	NotificationManager nManager;
	public void onReceive(Context context, Intent intent)
	{
		nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	
		String[] str = intent.getStringArrayExtra("alarmdata");
		Notification notify = new Notification(R.drawable.btn_alram, str[0]+"이" +str[2]+"분전입니다.",
				System.currentTimeMillis());
	
		notify.flags |= Notification.FLAG_AUTO_CANCEL;
		notify.vibrate = new long[] {400,1500};
		notify.sound = Uri.parse("android.resource://com.example.withschedule/"+R.raw.ohyear);
			
		notify.number++;
	
		Intent intent2 = new Intent(context, SplashActivity.class);
		intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pender = PendingIntent.getActivity(context, 0, intent2, 0);
			
		notify.setLatestEventInfo(context,str[1], str[0]+"이 " +str[2]+"분전 입니다.", pender);
		nManager.notify(Integer.parseInt(str[3]), notify);
		ExcuteDB.deleteAlarm(context, Integer.parseInt(str[3]));
			
		SystemAlarmManager.reenrollAlarm(context, str);
	}
}
