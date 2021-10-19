package com.example.withschedule;

import java.util.*;

import com.example.withschedule.alarm.Alarm;
import com.example.withschedule.alarm.SystemAlarmManager;
import com.example.withschedule.db.ExcuteDB;
import com.example.withschedule.dto.CntInfo;

import com.example.withschedule.schedule.*;
import com.example.withschedule.socket.*;
import com.example.withschedule.util.WithParser;
import com.ibm.icu.util.ChineseCalendar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.*;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;

public class CalendarActivity extends Activity 
{
	public static final String LOG_TAG = CalendarActivity.class.getSimpleName();
	private ArrayList<TextView> dayListView;
	private ArrayList<TextView> sNumListView;
	private TextView todayView;
	private TextView dayInfoView;
	private int iYear;
	private int iMonth;
	private int iDay;
	private int cYear;
	private int cMonth;
	private int cDay;
	private int nowYear;
	private int nowMonth;
	private int nowDay;
	private ImageView btnShowScheduleImageView;
	private ImageView btnAddScheduleImageView;
	private int currentCheckDay = 0;
		
	@Override
	protected void onResume() 
	{
		super.onResume();
		if(AppManager.getInstance().isPossibleInternet())
		{
			SharedPreferences pref = getSharedPreferences("withschedule", 0);
			String check = pref.getString("login", "false");
			if(check.equals("true")){
				ScheduleList result = ScheduleSetting.updateMyEntrySchedule(CalendarActivity.this);
				if(result==null){
					//Error 확인(dialog)필요?
				}
				setCalendar(iYear, iMonth);
				scheduleTableSetting(AppManager.getInstance().getSm().getScheduleOfDay(iYear,iMonth,iDay)); 
			}						
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		SharedPreferences pref = getSharedPreferences("withschedule", 0);
		String logincheck = pref.getString("login", "false");
		if(logincheck.equals("false"))
		{
			Intent myIntent = new Intent(
					CalendarActivity.this,
					JoinActivity.class);
			CalendarActivity.this
					.startActivityForResult(
							myIntent, 3);
		}
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.calendar_view);
		AppManager.getInstance().setActivity(this);
	
		ChineseCalendar cc = new ChineseCalendar();
		Calendar nowcalendar = Calendar.getInstance();
		
		cc.setTimeInMillis(nowcalendar.getTimeInMillis());
				
		iYear = nowcalendar.get(Calendar.YEAR);
		iMonth = nowcalendar.get(Calendar.MONTH);
		iDay = nowcalendar.get(Calendar.DATE);
		
		nowYear = nowcalendar.get(Calendar.YEAR);
		nowMonth = nowcalendar.get(Calendar.MONTH);
		nowDay = nowcalendar.get(Calendar.DATE);
		
		cYear = cc.get(ChineseCalendar.YEAR);
		cMonth = cc.get(ChineseCalendar.MONTH) + 1;
		cDay = cc.get(ChineseCalendar.DAY_OF_MONTH);
		

		todayView = (TextView) findViewById(R.id.today);
		dayInfoView = (TextView) findViewById(R.id.day_info);
		dayListView = new ArrayList<TextView>();
		sNumListView = new ArrayList<TextView>();
		
		TableLayout monthTablelayout = (TableLayout) findViewById(R.id.month_table);
		for (int i = 0; i < 6; i++)
		{
			TableRow weekTableRow = new TableRow(this);
			for (int j = 0; j < 7; j++)
			{
				TextView dayText = new TextView(this);
				TextView sNumText = new TextView(this);
				LinearLayout dateLayout = new LinearLayout(this);
				dateLayout.setGravity(Gravity.CENTER_HORIZONTAL);
				dateLayout.setPadding(0, 2, 0, 2);
				dateLayout.setOrientation(1);
				dayText.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

				dayText.setGravity(Gravity.CENTER_HORIZONTAL);
				sNumText.setGravity(Gravity.CENTER_HORIZONTAL);
				dateLayout.addView(dayText);
				dateLayout.addView(sNumText);
				weekTableRow.addView(dateLayout);
				dayListView.add(dayText);
				sNumListView.add(sNumText);
			}
			monthTablelayout.addView(weekTableRow);
		}

		monthTablelayout.setStretchAllColumns(true);

		monthTablelayout = (TableLayout) findViewById(R.id.week_table);
		monthTablelayout.setStretchAllColumns(true);
		
		scheduleTableSetting(AppManager.getInstance().getSm().getScheduleOfDay(nowYear,nowMonth,nowDay));
		
		btnShowScheduleImageView = (ImageView) findViewById(R.id.show_timeImage);
		btnShowScheduleImageView.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View view,MotionEvent e)
			{
				if (e.getAction() == MotionEvent.ACTION_DOWN)
				{
					btnShowScheduleImageView.setAlpha(80);
				} else if (e.getAction() == MotionEvent.ACTION_UP)
						{
							if ((int) e.getX() < 0
									|| (int) e.getX() > btnShowScheduleImageView
											.getWidth()
									|| (int) e.getY() < 0
									|| (int) e.getY() > btnShowScheduleImageView
											.getHeight()) 
							{
								btnShowScheduleImageView.setAlpha(1000);
							} 
							else 
							{
								btnShowScheduleImageView.setAlpha(1000);
								Intent myIntent = new Intent(
										CalendarActivity.this,
										TimeScheduleActivity.class);
								ArrayList<Integer> date = new ArrayList<Integer>();
								date.add(iYear);
								date.add(iMonth+1);
								date.add(iDay);
								myIntent.putIntegerArrayListExtra(
										"date", date);
								startActivity(myIntent);
							}
						}
						return true;
					}
				});

		btnAddScheduleImageView = (ImageView) findViewById(R.id.add_schedule);
		btnAddScheduleImageView
				.setOnTouchListener(new OnTouchListener() 
				{
					@Override
					public boolean onTouch(View view,
							MotionEvent e) {
						// TODO Auto-generated method stub
						if (e.getAction() == MotionEvent.ACTION_DOWN)
						{
							btnAddScheduleImageView.setAlpha(80);
						} 
						else if (e.getAction() == MotionEvent.ACTION_UP)
						{
							if ((int) e.getX() < 0
									|| (int) e.getX() > btnAddScheduleImageView
											.getWidth()
									|| (int) e.getY() < 0
									|| (int) e.getY() > btnAddScheduleImageView
											.getHeight()) 
							{
								btnAddScheduleImageView
										.setAlpha(1000);
							}
							else
							{
								btnAddScheduleImageView.setAlpha(1000);
								if(!AppManager.getInstance().isPossibleInternet()){
									showDialog(AppManager.POPUP_J);
									return true;
								}
								Intent myIntent = new Intent(
										CalendarActivity.this,
										AddScheduleActivity.class);
								ArrayList<Integer> date = new ArrayList<Integer>();
								date.add(iYear);
								date.add(iMonth+1);
								date.add(iDay);
								myIntent.putIntegerArrayListExtra(
										"date", date);
								CalendarActivity.this
										.startActivityForResult(
												myIntent, 1);
							}
						}
						return true;
					}
				});
		
		setCalendar(iYear, iMonth);

		ImageView preBtn = (ImageView) findViewById(R.id.pre);
		preBtn.setOnClickListener(new Button.OnClickListener()
		{

			public void onClick(View v)
			{
				iMonth--;
				if (iMonth <= 0) {
					iYear--;
					iMonth = 11;
				}
				setCalendar(iYear, iMonth);

			}
		});

		ImageView nextBtn = (ImageView) findViewById(R.id.next);
		nextBtn.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				iMonth++;
				if (iMonth >= 12) {
					iMonth = 0;
					iYear++;
				}
				setCalendar(iYear, iMonth);
			}
		});
	}

	private void setCalendar(int year, int month)
	{
		ChineseCalendar chineseCal = new ChineseCalendar();
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		todayView.setText(year + "년 " + (month + 1) + "월");
		todayView.setTextSize(18);
		todayView.setBackgroundResource(R.drawable.boarder_clickable);
		
		todayView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
				final LinearLayout linear = (LinearLayout)View.inflate(CalendarActivity.this, R.layout.schedule_date_select, null);
				final DatePicker dp;
			    dp = (DatePicker)linear.findViewById(R.id.maindatePicker);
				
				dp.init(iYear, iMonth, iDay, new DatePicker.OnDateChangedListener() {
					
					public void onDateChanged(DatePicker arg0, int arg1, int arg2, int arg3) {
						iYear=arg1;
						iMonth=arg2;
						iDay=arg3;
					}
				});
								
				new AlertDialog.Builder(CalendarActivity.this)
				.setTitle("날짜 설정")
				.setIcon(R.drawable.time_choice)
				.setView(linear)
				.setPositiveButton("확인", new DialogInterface.OnClickListener()
				{
				public void onClick(DialogInterface arg0, int arg1)
				{
					setCalendar(iYear, iMonth);
					scheduleTableSetting(AppManager.getInstance().getSm().getScheduleOfDay(iYear,iMonth,iDay));
				}
				})
				.setNegativeButton("취소", null)
				.show();
			}
		});
		int whatDay = calendar.get(Calendar.DAY_OF_WEEK);
		int lastday = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
				+ whatDay - 1;
		int j = 1;

		for (int i = 0; i < dayListView.size(); i++)
		{
			((LinearLayout)dayListView.get(i).getParent()).setOnTouchListener(null);
			dayListView.get(i).setTextSize(18);
			dayListView.get(i).setText(" ");
			((LinearLayout)dayListView.get(i).getParent()).setBackgroundColor(Color.WHITE);
			sNumListView.get(i).setTextSize(12);
			sNumListView.get(i).setText(" ");
		}

		for (int i = whatDay - 1; i < lastday; i++)
		{
			dayListView.get(i).setTextColor(Color.BLACK);
			sNumListView.get(i).setTextColor(Color.BLACK);
			chineseCal.setTimeInMillis(calendar.getTimeInMillis());

			int cyear = chineseCal.get(ChineseCalendar.YEAR);
			int cmonth = chineseCal.get(ChineseCalendar.MONTH) + 1;
			int cday = chineseCal.get(ChineseCalendar.DAY_OF_MONTH);
			
			if(year==nowYear&&month==nowMonth&&j==nowDay)
			{
				currentCheckDay = i;
				((LinearLayout)dayListView.get(i).getParent()).setBackgroundColor(Color.rgb(0xc6,0xff,0xff));
			}
			if (calendar.get(Calendar.DAY_OF_WEEK) == 1)
			{
				dayListView.get(i).setTextColor(Color.RED);
			} 
			else if (calendar.get(Calendar.DAY_OF_WEEK) == 7)
			{
				dayListView.get(i).setTextColor(Color.BLUE);
			}

			switch (month + 1)
			{
				case 1:
					if (j == 1)
					{
						dayListView.get(i).setTextColor(Color.RED);
					}
					break;
				case 2:
					break;
				case 3:
					if (j == 1)
					{
						dayListView.get(i).setTextColor(Color.RED);
					}
					break;
				case 4:
					break;
				case 5:
					if (j == 5 || j == 28)
					{
						dayListView.get(i).setTextColor(Color.RED);
					}
					break;
				case 6:
					if (j == 6)
					{
						dayListView.get(i).setTextColor(Color.RED);
					}
					break;
				case 7:
					break;
				case 8:
					if (j == 15)
					{
						dayListView.get(i).setTextColor(Color.RED);
					}
					break;
				case 9:
					break;
				case 10:
					if (j == 3)
					{
						dayListView.get(i).setTextColor(Color.RED);
					}
					break;
				case 11:
					break;
				case 12:
					if (j == 25)
					{
						dayListView.get(i).setTextColor(Color.RED);
					}
					break;
			}

			switch (cmonth)
			{
			case 1:
				if (cday == 1 || cday == 2) 
				{
					dayListView.get(i).setTextColor(Color.RED);
				}
				break;

			case 8:
				if (cday == 14 || cday == 15 || cday == 16) 
				{
					dayListView.get(i).setTextColor(Color.RED);
				}
				break;
			case 12:
				if (cday == chineseCal.getMaximum(ChineseCalendar.DAY_OF_MONTH))
				{
					dayListView.get(i).setTextColor(Color.RED);
				}
				break;
			}

			final int ii = i;
			final int y = year;
			final int m = month + 1;
			final int d = j;
			final int cy = cyear;
			final int cm = cmonth;
			final int cd = cday;


			View.OnTouchListener listOn = new OnTouchListener()
			{
				public boolean onTouch(View v, MotionEvent e)
				{
					int distance = 0;
					int downX = 0; 
					if (e.getAction() == MotionEvent.ACTION_DOWN)
					{
						downX = (int)e.getX();
					}
					else if (e.getAction() == MotionEvent.ACTION_UP)
					{
						int currentX = (int)e.getX();
						distance = currentX-downX;
						if(distance<-100){
							iMonth++;
							if (iMonth >= 12) {
								iMonth = 0;
								iYear++;
							}
							setCalendar(iYear, iMonth);
						}else if(distance>100){
							iMonth--;
							if (iMonth <= 0) {
								iYear--;
								iMonth = 11;
							}
							setCalendar(iYear, iMonth);
						}
						else{								
							if ((int) e.getX() < 0
									|| (int) e.getX() > ((LinearLayout) dayListView
											.get(ii).getParent())
											.getWidth()
									|| (int) e.getY() < 0
									|| (int) e.getY() > ((LinearLayout) dayListView
											.get(ii).getParent())
											.getHeight())
							{
								
							}
							else
							{
								((LinearLayout)dayListView.get(currentCheckDay).getParent()).setBackgroundColor(Color.WHITE);
								((LinearLayout)dayListView.get(ii).getParent()).setBackgroundColor(Color.rgb(0xc6,0xff,0xff));
								currentCheckDay=ii;
								
								nowYear = y;
								nowMonth = m-1;
								nowDay = d;
								
								cYear = cy;
								cMonth = cm;
								cDay = cd;
								iDay=d;
								scheduleTableSetting(AppManager.getInstance()
										.getSm().getScheduleOfDay(y, m-1, d));
							}
						}
					}
					return true;
				}
			};
							
			((LinearLayout) dayListView.get(i).getParent()).setOnTouchListener(listOn);
			if(AppManager.getInstance().getSm().getScheduleOfDay(year, month, j).size()==0)
		    {
				sNumListView.get(i).setText(" ");
		    }
		    else
		    {
		    	sNumListView.get(i).setText("(" + AppManager.getInstance()
		      .getSm().getScheduleOfDay(year, month, j).size() + ")");
		    }
			
			dayListView.get(i).setTextSize(18);
			dayListView.get(i).setText(j++ + "");
			sNumListView.get(i).setTextSize(12);
			
			calendar.set(year, month, j);
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode)
		{
			
			case 1:
				if (resultCode == -1)
				{
					setCalendar(iYear, iMonth);
					scheduleTableSetting(AppManager.getInstance().getSm().getScheduleOfDay(iYear,iMonth,iDay));
				}
				break;
				
			case 2:
				if (resultCode == -1)
				{
					setCalendar(iYear, iMonth);
					scheduleTableSetting(AppManager.getInstance().getSm().getScheduleOfDay(iYear,iMonth,iDay));
				}
				break;
				
			case 3:
				android.os.Process.killProcess(android.os.Process.myPid());
				break;
		}
	}
	
	public void scheduleTableSetting(ArrayList<Schedule> sl) 
	{
		LinearLayout scheduleTable = (LinearLayout) findViewById(R.id.shcedule_table);
		scheduleTable.removeAllViews();
		dayInfoView.setText(iYear + "/" + (iMonth+1) +"/" + iDay +"(음력 " + cMonth + "/" + cDay +")");
		ArrayList<CntInfo> cntList = ScheduleSetting.checkScheduleCNT(sl);
		
		HashMap<String,Boolean> ownerCheckMap=null;
		if(AppManager.getInstance().isPossibleInternet()){
			String result = ScheduleSetting.ownerCheck(CalendarActivity.this,sl);
			ownerCheckMap = WithParser.ownerCheckParse(result);
		}		
		
		boolean isOwner = false;
		int i=0;
		for (Schedule schedule : sl)
		{
			String owner = "";
			if(ownerCheckMap!=null){
				isOwner = ownerCheckMap.get(String.valueOf(schedule.getNo()));		
				if(isOwner) owner="(방장)";
			}
			
			RelativeLayout mlayout = (RelativeLayout) LayoutInflater.from(CalendarActivity.this).inflate(R.layout.schedule_row, null, false);
			LinearLayout btnCollection = (LinearLayout)mlayout.findViewById(R.id.btn_collection);
			btnCollection.removeViewInLayout((ImageView)btnCollection.findViewById(R.id.btn_join));
			TextView titleView = (TextView)mlayout.findViewById(R.id.schedule_title);
			if (schedule.getTitle().length() > 10){
				titleView.setText(owner+schedule.getTitle().substring(0, 10) + "...");
			} 
			else{
				titleView.setText(owner+schedule.getTitle());
			}

			TextView timeView = (TextView)mlayout.findViewById(R.id.schedule_time);
			timeView.setText("※ "
							+ schedule.getStartTime().get(
									Calendar.HOUR_OF_DAY) 
							+ ":"
							+ schedule.getStartTime().get(
									Calendar.MINUTE) + "~" +" 댓글("+cntList.get(i).getCcnt()+") 인원("+cntList.get(i++).getEcnt()+")");

			final ImageView btnDetail = (ImageView)mlayout.findViewById(R.id.btn_detail);
			final ImageView btnAlarm = (ImageView)mlayout.findViewById(R.id.btn_alram);
			final ImageView btnDelete = (ImageView)mlayout.findViewById(R.id.btn_exit);
						
			final int sno = schedule.getNo();
			final boolean isOwnerCheck = isOwner;
			btnDetail.setOnTouchListener(new OnTouchListener()
			{
				public boolean onTouch(View view, MotionEvent e)
				{
					if (e.getAction() == MotionEvent.ACTION_DOWN)
					{
						btnDetail.setAlpha(80);
					} 
					else if (e.getAction() == MotionEvent.ACTION_UP)
					{
						if ((int) e.getX() < 0
									|| (int) e.getX() > btnDetail
											.getWidth()
									|| (int) e.getY() < 0
									|| (int) e.getY() > btnDetail
											.getHeight()) 
						{
							btnDetail.setAlpha(1000);
						}
						else
						{
							btnDetail.setAlpha(1000);
							Intent myIntent = null;
							if(isOwnerCheck){
								myIntent = new Intent(
										CalendarActivity.this,
										DetailViewActivity.class);
							}
							else{				
								myIntent = new Intent(
										CalendarActivity.this,
										FriendDetailViewActivity.class);
								myIntent.putExtra("type", 1);
							}
							myIntent.putExtra("schedule_no", sno);
							CalendarActivity.this.startActivityForResult(myIntent, 2);
						}
					}
					else
					{
						btnDetail.setAlpha(1000);
					}
					return true;
				}
			});			
									
			btnDelete.setOnTouchListener(new OnTouchListener()
			{
				public boolean onTouch(View view, MotionEvent e)
				{
					if (e.getAction() == MotionEvent.ACTION_DOWN)
					{
						btnDelete.setAlpha(80);
					} 
					else if (e.getAction() == MotionEvent.ACTION_UP)
					{
						if ((int) e.getX() < 0
									|| (int) e.getX() > btnDelete
											.getWidth()
									|| (int) e.getY() < 0
									|| (int) e.getY() > btnDelete
											.getHeight()) 
						{
							btnDelete.setAlpha(1000);
						}
						else
						{
							if(!AppManager.getInstance().isPossibleInternet()){
								showDialog(AppManager.POPUP_J);
								return true;
							}
							btnDelete.setAlpha(1000);	
							
							new AlertDialog.Builder(CalendarActivity.this)
							.setTitle("일정취소")
							.setMessage("정말로 일정을 취소하시겠습니까?")
							.setIcon(R.drawable.btn_exit)
							.setPositiveButton("확인", new DialogInterface.OnClickListener()
							{
							public void onClick(DialogInterface arg0, int arg1)
							{
								String result = ScheduleSetting.delete(CalendarActivity.this,sno);
								if(result.equals(AppManager.COMPLETION))
								{
									Alarm alm = AppManager.getInstance().getAm().search(sno);
									if(alm==null){
										;
									}
									else{
										SystemAlarmManager.cancelAlarm(CalendarActivity.this, sno);
										ExcuteDB.deleteAlarm(CalendarActivity.this, sno);
										AppManager.getInstance().getAm().cancel(sno);
									}
									AppManager.getInstance().getSm().cancel(sno);
									ExcuteDB.deleteSchedule(CalendarActivity.this, sno);
								
									setCalendar(iYear, iMonth);
									scheduleTableSetting(AppManager.getInstance().getSm().getScheduleOfDay(iYear,iMonth,iDay));
								}
								else if(result.equals(AppManager.NO_SEARCH)){
									showDialog(AppManager.POPUP_G);
								}
								else if(result.equals(AppManager.HTTP_ERROR)){
									showDialog(AppManager.POPUP_R);
								}								
							}
						})
						.setNegativeButton("취소", null)
						.show();
						}
					}
					else
					{
						btnDelete.setAlpha(1000);
					}
					return true;
				}
			});
			
			btnAlarm.setOnTouchListener(new OnTouchListener()
			{
				public boolean onTouch(View view, MotionEvent e)
				{
					if (e.getAction() == MotionEvent.ACTION_DOWN)
					{
						btnAlarm.setAlpha(80);
					} 
					else if (e.getAction() == MotionEvent.ACTION_UP)
					{
						if ((int) e.getX() < 0
									|| (int) e.getX() > btnAlarm
											.getWidth()
									|| (int) e.getY() < 0
									|| (int) e.getY() > btnAlarm
											.getHeight()) 
						{
							btnAlarm.setAlpha(1000);
						}
						else
						{
							btnAlarm.setAlpha(1000);	
							final LinearLayout linear = (LinearLayout)View.inflate(CalendarActivity.this, R.layout.schedule_set_alram, null);
							final CheckBox alarmCheckView = (CheckBox) linear.findViewById(R.id.set_alarmcheck);
							final RadioGroup alarmGroupView = (RadioGroup)linear.findViewById(R.id.set_alarmgroup);
							Alarm alm = AppManager.getInstance().getAm().search(sno);
							if(alm==null)
							{
								alarmCheckView.setChecked(false);
								alarmGroupView.check(R.id.set_a_5);
							}
							else
							{
								alarmCheckView.setChecked(true);
								switch(alm.getAlarmtime())
								{
									case 5:
										alarmGroupView.check(R.id.set_a_5);
										break;
									case 15:
										alarmGroupView.check(R.id.set_a_15);
										break;
									case 30:
										alarmGroupView.check(R.id.set_a_30);
										break;
								}
							}
							
							new AlertDialog.Builder(CalendarActivity.this)
							.setTitle("알람설정")
							.setIcon(R.drawable.btn_alram)
							.setView(linear)
							.setPositiveButton("확인", new DialogInterface.OnClickListener()
							{
							public void onClick(DialogInterface arg0, int arg1)
							{
								Alarm alm = AppManager.getInstance().getAm().search(sno);
								int atm;
								int id = alarmGroupView.getCheckedRadioButtonId();
								if(id==R.id.set_a_5)
								{
									atm=5;
								}
								else if(id==R.id.set_a_15)
								{
									atm=15;
								}
								else
								{
									atm=30;
								}
				
								GregorianCalendar testGc = (GregorianCalendar) AppManager.getInstance().getSm().search(sno).getStartTime().clone();
								testGc.set(Calendar.MINUTE, AppManager.getInstance().getSm().search(sno).getStartTime().get(Calendar.MINUTE)-atm);
								
								if(alm==null&&alarmCheckView.isChecked())
								{
									if(Calendar.getInstance().compareTo(testGc)>=0&&AppManager.getInstance().getSm().search(sno).getType()==AppManager.GENERAL)
									{
										Toast.makeText(CalendarActivity.this, "알림시간이 지났습니다..", Toast.LENGTH_LONG).show();
									}
									else
									{
										Schedule schedule = AppManager.getInstance().getSm().search(sno);
										SystemAlarmManager.typeenrollAlarm(CalendarActivity.this, schedule.getTitle(), schedule.getStartTime(), schedule.getType(), sno, atm);
										ExcuteDB.insertAlarm(CalendarActivity.this, sno, true, atm);
										AppManager.getInstance().getAm().enroll(new Alarm(sno,true,atm));
									}
								}
								else if(alm!=null&&alarmCheckView.isChecked())
								{
									if(Calendar.getInstance().compareTo(testGc)>=0&&AppManager.getInstance().getSm().search(sno).getType()==AppManager.GENERAL)
									{
										Toast.makeText(CalendarActivity.this, "알림시간이 지났습니다..", Toast.LENGTH_LONG).show();
									}
									else
									{
										Schedule schedule = AppManager.getInstance().getSm().search(sno);
										SystemAlarmManager.cancelAlarm(CalendarActivity.this, sno);
										SystemAlarmManager.typeenrollAlarm(CalendarActivity.this, schedule.getTitle(), schedule.getStartTime(), schedule.getType(), sno, atm);
										ExcuteDB.updateAlarm(CalendarActivity.this, sno, true, atm);
										alm.setAlarmtime(atm);
										alm.setSetalarm(alarmCheckView.isChecked());
									}
								}
								else if(alm!=null&&!(alarmCheckView.isChecked()))
								{
									SystemAlarmManager.cancelAlarm(CalendarActivity.this, sno);
									ExcuteDB.deleteAlarm(CalendarActivity.this, sno);
									AppManager.getInstance().getAm().cancel(sno);
								}
							}
							})
							.setNegativeButton("취소", null)
							.show();
						}
					}
					else
					{
						btnAlarm.setAlpha(1000);
					}
					return true;
				}
			});
			
			scheduleTable.addView(mlayout);
		}			
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuItem item1 = menu.add(0,1,0,"친구약속 보기").setIcon(R.drawable.menu_schedule_list);
		MenuItem item2 = menu.add(0,2,0,"친구목록 보기").setIcon(R.drawable.friendlist);
		MenuItem item3 = menu.add(0,4,0,"설정").setIcon(R.drawable.settingmode);
		MenuItem item4 = menu.add(0,3,0,"초대/신청").setIcon(R.drawable.request);
		MenuItem item5 = menu.add(0,5,0,"도움말").setIcon(R.drawable.imformation);
		
		item1.setOnMenuItemClickListener(new OnMenuItemClickListener() {			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				// TODO Auto-generated method stub
				if(!AppManager.getInstance().isPossibleInternet()){
					showDialog(AppManager.POPUP_J);
					return false;
				}
				Intent myIntent = new Intent(CalendarActivity.this,ScheduleListActivity.class);
				ArrayList<Integer> date = new ArrayList<Integer>();
				date.add(nowYear);
				date.add(nowMonth+1);
				date.add(nowDay);
				myIntent.putIntegerArrayListExtra(
						"date", date);
				CalendarActivity.this.startActivity(myIntent);
				return false;
			}
		});
		
		item2.setOnMenuItemClickListener(new OnMenuItemClickListener() {			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				if(!AppManager.getInstance().isPossibleInternet())
				{
					showDialog(AppManager.POPUP_J);
					return false;
				}
				Intent myIntent = new Intent(CalendarActivity.this,FriendListActivity.class);
				CalendarActivity.this.startActivity(myIntent);
				return true;
			}
		});
		
		item3.setOnMenuItemClickListener(new OnMenuItemClickListener() {			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(CalendarActivity.this,SettingActivity.class);
				CalendarActivity.this.startActivity(myIntent);
				return false;
			}
		});
		
		item4.setOnMenuItemClickListener(new OnMenuItemClickListener() {			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(CalendarActivity.this,RequestViewActivity.class);
				CalendarActivity.this.startActivity(myIntent);
				return false;
			}
		});
		
		//final LinearLayout linear = (LinearLayout)View.inflate(CalendarActivity.this, R.layout.help_calendar, null);
		item5.setOnMenuItemClickListener(new OnMenuItemClickListener() {			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				// TODO Auto-generated method stub
				
				final LinearLayout linear = (LinearLayout)View.inflate(CalendarActivity.this, R.layout.help_calendar_view, null);
				
				new AlertDialog.Builder(CalendarActivity.this)
				.setTitle("도움말")
				.setView(linear)
				.setIcon(R.drawable.imformation)
				.show();
				return false;
			}
		});
		return super.onCreateOptionsMenu(menu);		
	}
	
	
	protected void onDestroy()
	{
		AppManager.getInstance().getAm().clear(this);
		super.onDestroy();
	}	

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = null;
		String popupText = getString(id);
		builder = new AlertDialog.Builder(this);
		builder.setMessage(popupText);
		builder.setNegativeButton("확인", null);
		//if(builder == null){
		//	return super.onCreateDialog(id);
		//}
		return builder.create();
	}	
}
