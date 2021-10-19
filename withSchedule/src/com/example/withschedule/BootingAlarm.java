package com.example.withschedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootingAlarm extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
		{
			Intent serviceIntent = new Intent();
			serviceIntent.setAction("BootingService");
			context.startService(serviceIntent);
		}
	}

}
