package com.example.withschedule;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.withschedule.socket.*;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
	private static final String PROJECT_ID = "461412796848";

	// 구글 api 페이지 주소 [https://code.google.com/apis/console/#project:긴 번호]
	// #project: 이후의 숫자가 위의 PROJECT_ID 값에 해당한다
	// public 기본 생성자를 무조건 만들어야 한다.

	public GCMIntentService(){
		this(PROJECT_ID);
	}

	public GCMIntentService(String project_id) {
		super(project_id);
	}

	/** 푸시로 받은 메시지 */
	@Override
	protected void onMessage(Context context, Intent intent) {
		String title = intent.getStringExtra("title");
		String message = intent.getStringExtra("msg");
		setNotification(context, title, message);
	}
	/** 단말에서 GCM 서비스 등록 했을 때 등록 id를 받는다 */
	@Override
	protected void onRegistered(Context context, String regId) {
		MemberSetting.gcmregister(context, regId);
	}

	/** 단말에서 GCM 서비스 등록 해지를 하면 해지된 등록 id를 받는다 */
	@Override
	protected void onUnregistered(Context context, String regId) {
		Log.d("kk", regId + "가 해지됫다.");
	}

	@Override
	protected void onError(Context arg0, String arg1) {
		Log.d("kk", arg1 + "가 에러남");
	}

	private void setNotification(Context context, String title, String message) {
		NotificationManager notificationManager = null;
		Notification notification = null;
		try {
			notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			notification = new Notification(R.drawable.ic_launcher,
					title, System.currentTimeMillis());
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.vibrate = new long[] {400,1500};
			notification.sound = Uri.parse("android.resource://com.example.withschedule/"+R.raw.ohyear);
			notification.number++;
		
			Intent intent = new Intent(context, SplashActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
			notification.setLatestEventInfo(context, message, title, pi);
			notificationManager.notify(0, notification);
		} catch (Exception e) {
			Log.d("kk", "[setNotification] Exception : " + e.getMessage());
		}
	}
}
