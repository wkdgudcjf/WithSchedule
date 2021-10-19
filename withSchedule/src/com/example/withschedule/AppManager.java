package com.example.withschedule;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.net.*;

import com.example.withschedule.alarm.*;
import com.example.withschedule.friend.*;
import com.example.withschedule.schedule.*;

public class AppManager {
	public static boolean DEBUG_MODE=false;
	
	public static final int POPUP_A = R.string.check_checkbox;
	public static final int POPUP_B = R.string.check_email;
	public static final int POPUP_C = R.string.check_name;
	public static final int POPUP_D = R.string.check_password;
	public static final int POPUP_E = R.string.check_double;
	public static final int POPUP_F = R.string.system_error;
	public static final int POPUP_G = R.string.not_search;
	public static final int POPUP_H = R.string.missmatch_password;
	public static final int POPUP_I = R.string.schedule_not_found;
	public static final int POPUP_J = R.string.no_internet;
	public static final int POPUP_K = R.string.edit_email;
	public static final int POPUP_L = R.string.edit_pw;
	public static final int POPUP_M = R.string.no_owner;
	public static final int POPUP_O = R.string.late_alarm;
	public static final int POPUP_N = R.string.check_title;
	public static final int POPUP_P = R.string.check_time;
	public static final int POPUP_Q = R.string.duplicate;
	public static final int POPUP_R = R.string.http_error;

	public static final String SYSTEM_ERROR = "system_error";
	public static final String DOUBLE_EMAIL = "double_email";
	public static final String COMPLETION = "completion";
	public static final String NO_SEARCH = "no_search";
	public static final String MISSMATCH_PASSWORD = "missmatch_password";
	public static final String DUPLICATE = "duplicate";
	public static final String HTTP_ERROR = "http_error";
	public static final String NO_INTERNET = "no_internet";

	public static final int GENERAL = 1;
	public static final int WEEK_REPEAT = 2;
	public static final int LASTDAY_REPEAT = 3;
	public static final int MONTH_REPEAT = 4;
	public static final int YEAR_REPEAT = 5;

	private static AppManager manager = new AppManager();
	private ScheduleManagement sm;
	private AlarmManegement am;
	private FriendManagement fm;
	
	private Activity mActivity;
	private Resources mResources;
	

	public FriendManagement getFm() {
		return fm;
	}

	public void setFm(FriendManagement fm) {
		this.fm = fm;
	}

	private int mDisplayWidth;
	private int mDisplayHeight;

	public ScheduleManagement getSm() {
		return sm;
	}

	public void setSm(ScheduleManagement sm) {
		this.sm = sm;
	}

	public AlarmManegement getAm() {
		return am;
	}

	public void setAm(AlarmManegement am) {
		this.am = am;
	}

	private AppManager() {
		sm = new ScheduleManagement();
		am = new AlarmManegement();
		fm = new FriendManagement();
	}

	public static AppManager getInstance() {
		return manager;
	}

	public int getDisplayWidth() {
		return mDisplayWidth;
	}

	public int getDisplayHeight() {
		return mDisplayHeight;
	}

	void setResources(Resources _resources) {
		mResources = _resources;
	}

	public Resources getResources() {
		return mResources;
	}

	public Bitmap getBitmap(int r) {
		return BitmapFactory.decodeResource(mResources, r);
	}

	public Activity getActivity() {
		return mActivity;
	}

	public void setActivity(Activity mActivity) {
		this.mActivity = mActivity;
	}

	public boolean isPossibleInternet() {
		// 네트워크 연결 상태 확인하는 로직
		ConnectivityManager cm = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		// boolean isWifiAvail = ni.isAvailable();
		boolean isWifiConn = ni.isConnected();
		ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		// boolean isMobileAvail = ni.isAvailable();
		boolean isMobileConn = ni.isConnected();

		/*
		 * String status = "WiFi\nAvail = " + isWifiAvail + "\nConn = " +
		 * isWifiConn + "\nMobile\nAvail = " + isMobileAvail + "\nConn = " +
		 * isMobileConn + "\n";
		 */
		
		if (!isWifiConn && !isMobileConn) {
			return false;
		}
		return true;
	}
}
