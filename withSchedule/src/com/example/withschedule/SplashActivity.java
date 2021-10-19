package com.example.withschedule;

import com.example.withschedule.alarm.*;
import com.example.withschedule.db.ExcuteDB;
import com.example.withschedule.friend.*;
import com.example.withschedule.schedule.*;
import com.example.withschedule.socket.*;

import android.annotation.SuppressLint;
import android.app.*;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
	ProgressDialog dialog;
	
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);
		
		final ScheduleManagement sm = AppManager.getInstance().getSm();
		final AlarmManegement am = AppManager.getInstance().getAm();
		final FriendManagement fm =  AppManager.getInstance().getFm();
		
		Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (checkLogin())
				{
					sm.allRemove();
					am.allRemove();
					fm.allRemove();
					am.getAlarmList().setList(ExcuteDB.selectAlarm(SplashActivity.this));
					sm.getScheduleList().setList(ExcuteDB.selectSchedule(SplashActivity.this));
					AppManager.getInstance().setActivity(SplashActivity.this);
					
					ProgressBarThread progressBar = new ProgressBarThread(SplashActivity.this);
					
					progressBar.start();						
					String result = updateFriendList();
					
					if(result.equals(AppManager.NO_INTERNET)){
						//인터넷 안되면 그냥 넘어감
					}else if(result.equals(AppManager.HTTP_ERROR)){
						//HttpError가 나면 경고창
				    	showDialog(AppManager.POPUP_R);
				    }					
					progressBar.stopProgressBar();
					
					Intent myIntent = new Intent(SplashActivity.this,CalendarActivity.class);
					startActivity(myIntent);
				} 
				else
				{
					Intent myIntent = new Intent(SplashActivity.this,JoinActivity.class);
					startActivity(myIntent);
				}
				finish();
			};
		};
		handler.sendEmptyMessageDelayed(0, 600);
	}

	private String updateFriendList(){
		String result = null;
		if(!AppManager.getInstance().isPossibleInternet()) {
			return AppManager.NO_INTERNET;
		}
		result = MemberSetting.friendSetting(SplashActivity.this);
		return result;
	}
	
	private boolean checkLogin() {
		SharedPreferences pref = getSharedPreferences("withschedule", 0);
		String check = pref.getString("login", "false");				
		
		return check.equals("true")?true:false;	
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = null;
		String popupText = getString(id);
		builder = new AlertDialog.Builder(this);
		builder.setMessage(popupText);
		builder.setNegativeButton("확인", null);
		return builder.create();
	}
}
