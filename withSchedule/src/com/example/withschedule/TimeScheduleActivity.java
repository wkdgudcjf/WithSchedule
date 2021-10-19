package com.example.withschedule;

import java.util.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.example.withschedule.schedule.*;
import com.example.withschedule.socket.ScheduleSetting;
import com.ibm.icu.util.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.*;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;

public class TimeScheduleActivity extends Activity {
	private GregorianCalendar calendar;
	private ChineseCalendar chineseCalendar;
	private ArrayList<ScheduleList> list;
	private TextView todayView;
	private ArrayList<String> friendsEmailList;
	private int type=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_schedule);
		list=new ArrayList<ScheduleList>();
		calendar = new GregorianCalendar();
		friendsEmailList = getIntent().getStringArrayListExtra("friendsEmail");
		ArrayList<Integer> arr = getIntent().getIntegerArrayListExtra("friend_date");
		
		if(friendsEmailList!=null) type=0;
		else if(arr==null){
			arr = getIntent().getIntegerArrayListExtra("date");
			type=2;
		}
		else type=1;
		
		list.add(AppManager.getInstance().getSm().getScheduleList());
		calendar = new GregorianCalendar();
		chineseCalendar = new ChineseCalendar();
				
		switch(type){
		case 0:
			settingFriendsScheduleList();
			break;
		case 1:			
			calendar = new GregorianCalendar(arr.get(0),arr.get(1)-1,arr.get(2));
			list.add(AppManager.getInstance().getSm().getFriendSchedule());
			break;
		case 2:
			calendar = new GregorianCalendar(arr.get(0),arr.get(1)-1,arr.get(2));
			break;
		}		
		
		chineseCalendar.setTimeInMillis(calendar.getTimeInMillis());		
		todayView = (TextView) findViewById(R.id.select_date);
		todayView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
				final LinearLayout linear = (LinearLayout)View.inflate(TimeScheduleActivity.this, R.layout.schedule_date_select, null);
				final DatePicker dp;
			    dp = (DatePicker)linear.findViewById(R.id.maindatePicker);				
				dp.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
					public void onDateChanged(DatePicker arg0, int arg1, int arg2, int arg3) {
						calendar.set(Calendar.YEAR,arg1);
						calendar.set(Calendar.MONTH,arg2);
						calendar.set(Calendar.DAY_OF_MONTH,arg3);
					}
				});
								
				new AlertDialog.Builder(TimeScheduleActivity.this)
				.setTitle("날짜 설정")
				.setIcon(R.drawable.time_choice)
				.setView(linear)
				.setPositiveButton("확인", new DialogInterface.OnClickListener()
				{
				public void onClick(DialogInterface arg0, int arg1)
				{
					if(type==0)	settingFriendsScheduleList();			
					setDatePicker();
					setScheduleTimeTable();
				}
				})
				.setNegativeButton("취소", null)
				.show();
			}
		});
		
		View.OnClickListener prevClick = new OnClickListener() {
			public void onClick(View v) {
				setMoveDay(-1);
				if(type==0){
					settingFriendsScheduleList();
				}
				setDatePicker();
				setScheduleTimeTable();
			}
		};

		View.OnClickListener nextClick = new OnClickListener() {
			public void onClick(View v) {
				setMoveDay(1);
				if(type==0){
					settingFriendsScheduleList();
				}
				setDatePicker();
				setScheduleTimeTable();
			}
		};

		ImageView prev = (ImageView) findViewById(R.id.pre);
		prev.setOnClickListener(prevClick);

		ImageView next = (ImageView) findViewById(R.id.next);
		next.setOnClickListener(nextClick);
		
		setDatePicker();
		setScheduleTimeTable();
	}

	private void setDatePicker() {
		// TODO Auto-generated method stub
		String str=null;
		switch(calendar.get(Calendar.DAY_OF_WEEK))
		{
			case Calendar.MONDAY:
				str="월";
				break;
			case Calendar.TUESDAY:
				str="화";
				break;
			case Calendar.WEDNESDAY:
				str="수";
				break;
			case Calendar.THURSDAY:
				str="목";
				break;
			case Calendar.FRIDAY:
				str="금";
				break;
			case Calendar.SATURDAY:
				str="토";
				break;
			case Calendar.SUNDAY:
				str="일";
				break;
		}
		todayView.setTextSize(16);
		todayView.setBackgroundResource(R.drawable.boarder_clickable);
		todayView.setText(calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.DAY_OF_MONTH)+"("+str+")");
	}

	private void setMoveDay(int i) {
		// TODO Auto-generated method stub
		calendar.setTimeInMillis(calendar.getTimeInMillis()
				+ (i * 86400000));
		chineseCalendar.setTimeInMillis(calendar.getTimeInMillis());
	}

	private void setScheduleTimeTable() {
		// TODO Auto-generated method stub
		
		LinearLayout timeTable = (LinearLayout) findViewById(R.id.shcedule_table);
		timeTable.removeAllViewsInLayout();
		
		int color_id = 0;
		int cnt=0;
		for (ScheduleList fflist : this.list) {
			switch(cnt%4){
			case 0:
				color_id = R.drawable.start_time_red;
				break;
			case 1:
				color_id = R.drawable.start_time_blue;
				break;
			case 2:
				color_id = R.drawable.start_time_green;
				break;
			case 3:
				color_id = R.drawable.start_time_yello;
				break;
			}
			cnt++;
			
			LinearLayout timeTableRow = new LinearLayout(this);
			timeTableRow.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
			timeTableRow.setOrientation(LinearLayout.VERTICAL);

			for (int i = 0; i < 24; i++) {
				TextView tv = new TextView(this);
				int height = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 40,
						getApplicationContext().getResources()
								.getDisplayMetrics());
				
				int width = ((View) findViewById(R.id.shcedule_table))
						.getWidth();
				tv.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, height));
				tv.setWidth(width / this.list.size());
				tv.setBackgroundResource(R.drawable.line);
				tv.setPadding(0, 0, 0, 0);
				timeTableRow.addView(tv);
			}
			
			List<Schedule> scheduleList=null;			
			if(type!=0||cnt==1){
				scheduleList = fflist.getScheduleOfDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			}else{
				scheduleList=fflist.getList();
			}
			
			for (Schedule schedule : scheduleList) {
				int startHour = schedule.getStartTime().get(Calendar.HOUR_OF_DAY);
				TextView text = (TextView) timeTableRow.getChildAt(startHour);
				text.setTextColor(Color.BLACK);
				if (text.getText().equals("")) {
					text.setText("♣" + schedule.getTitle());
				} else {
					text.setText(text.getText() + "  ♣" + schedule.getTitle());
				}
				timeTableRow.getChildAt(startHour).setBackgroundResource(color_id);
			}
			timeTable.addView(timeTableRow);
		}
	}
	private void settingFriendsScheduleList(){
		// TODO Auto-generated method stub
		ArrayList<ScheduleList> friendsSchedulelist = ScheduleSetting.setFriendsScheduleList(calendar, friendsEmailList);
		if(friendsSchedulelist==null){
			//Error 확인(dialog)필요??
			friendsSchedulelist =new ArrayList<ScheduleList>();
		}
		this.list.clear();
		this.list.add(AppManager.getInstance().getSm().getScheduleList());
		this.list.addAll(friendsSchedulelist);
	}

}
