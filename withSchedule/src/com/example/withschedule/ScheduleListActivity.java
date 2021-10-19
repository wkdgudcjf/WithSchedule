package com.example.withschedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.example.withschedule.db.ExcuteDB;
import com.example.withschedule.friend.Friend;
import com.example.withschedule.schedule.Schedule;
import com.example.withschedule.schedule.ScheduleList;
import com.example.withschedule.socket.ScheduleSetting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScheduleListActivity extends Activity {
	private GregorianCalendar calendar;
	private TextView todayView;
	private HashMap<String,ArrayList<Schedule>> friendsScheduleMap;
	private ArrayList<String> friendEmailList;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_list_view);
		ArrayList<Integer> arr = getIntent().getIntegerArrayListExtra("date");
		calendar = new GregorianCalendar(arr.get(0),arr.get(1)-1,arr.get(2));
		todayView = (TextView) findViewById(R.id.today);
		friendEmailList = new ArrayList<String>();
		for(Friend fd:ExcuteDB.selectFriend(ScheduleListActivity.this))
		{
			friendEmailList.add(fd.getEmail());
		}
		friendsScheduleMap = ScheduleSetting.setScheduleList2(calendar,friendEmailList);
		if(friendsScheduleMap==null){
			//Error 확인(dialog)필요??
			friendsScheduleMap =new HashMap<String,ArrayList<Schedule>>();
		}
		LinearLayout table = (LinearLayout) findViewById(R.id.schedule_table);
		table.removeAllViews();
		setScheduleTable(friendsScheduleMap);
		

		ImageView prev = (ImageView)findViewById(R.id.pre);
		prev.setOnClickListener(new PrevClickListener());
		
		ImageView next = (ImageView)findViewById(R.id.next);
		next.setOnClickListener(new NextClickListener());
		
		EditText search = (EditText) findViewById(R.id.edit_search);
		
		search.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				LinearLayout table = (LinearLayout) findViewById(R.id.schedule_table);
				table.removeAllViews();
				String searchKeyword = s.toString();
				setScheduleTable(searchList(searchKeyword));
			}
	    });
		
	}
	private HashMap<String,ArrayList<Schedule>> searchList(String searchKeyword){
		HashMap<String,ArrayList<Schedule>> searchList = new HashMap<String,ArrayList<Schedule>>();
		
		Iterator<String> key = friendsScheduleMap.keySet().iterator();
		while(key.hasNext())
		{
			String value = (String)key.next();
			ArrayList<Schedule> als = friendsScheduleMap.get(value);
			ArrayList<Schedule> newals = new ArrayList<Schedule>();
			for(int i=0;i<als.size();i++)
			{
				if(check(als.get(i).getTitle(),searchKeyword))
				{
					newals.add(als.get(i));
				}
			}
			if(newals.size()!=0)
			{
				searchList.put(value, newals);
			}
		}
		return searchList;
	}

	private boolean check(String content,String searchKeyword)
	{
		boolean isAdd = false;

		if (searchKeyword != null && "".equals(searchKeyword.trim()) == false) {
			String iniName = com.example.withschedule.util.HangulUtils.getHangulInitialSound(content,
					searchKeyword);
			if (iniName.indexOf(searchKeyword) >= 0) {
				isAdd = true;
			}
		} 
		else 
		{
			isAdd = true;
		}
		return isAdd;
	}
	
	protected void setMoveDay(int i) {
		// TODO Auto-generated method stub
		calendar.setTimeInMillis(calendar.getTimeInMillis()+(i*1000*60*60*24));
	}
	
	protected void setScheduleTable(HashMap<String,ArrayList<Schedule>> friendsMap) {
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
		todayView.setOnClickListener(new DatePickerListener());
		
		LinearLayout table = (LinearLayout) findViewById(R.id.schedule_table);
		for (String email : friendEmailList) {
			final String friendEmail = email;
			ArrayList<Schedule> als = friendsMap.get(email);
			if(als==null)
			{
				continue;
			}
			TextView tv = new TextView(ScheduleListActivity.this);
			String name = AppManager.getInstance().getFm().search(email).getNickname();
			tv.setText(name+"("+email+")님의 스케줄");
			tv.setTextColor(Color.BLACK);
			table.addView(tv);
			final ScheduleList flist = new ScheduleList(als);
			for (Schedule schedule : als) {
				RelativeLayout mlayout = (RelativeLayout) LayoutInflater.from(ScheduleListActivity.this).inflate(R.layout.schedule_row, null, false);
				LinearLayout btnCollection = (LinearLayout)mlayout.findViewById(R.id.btn_collection);
				btnCollection.removeViewInLayout((ImageView)btnCollection.findViewById(R.id.btn_exit));
				btnCollection.removeViewInLayout((ImageView)btnCollection.findViewById(R.id.btn_alram));
				TextView title = (TextView)mlayout.findViewById(R.id.schedule_title);
				if (schedule.getTitle().length() > 10)
				{
					title.setText(schedule.getTitle().substring(0, 10) + "...");
				} 
				else
				{
					 title.setText(schedule.getTitle());
				}

				TextView time = (TextView)mlayout.findViewById(R.id.schedule_time);
				time.setText("※ "
								+ schedule.getStartTime().get(
										Calendar.HOUR_OF_DAY)
								+ ":"
								+ schedule.getStartTime().get(
										Calendar.MINUTE) + "~");
				
				final ImageView btn_detail = (ImageView)mlayout.findViewById(R.id.btn_detail);
				final int sno = schedule.getNo();
				
				
				btn_detail.setOnTouchListener(new OnTouchListener()
				{
					public boolean onTouch(View view, MotionEvent e)
					{
						AppManager.getInstance().getSm().setFriendSchedule(flist);
						if (e.getAction() == MotionEvent.ACTION_DOWN)
						{
							btn_detail.setAlpha(80);
						}
						else if (e.getAction() == MotionEvent.ACTION_UP)
						{
							if ((int) e.getX() < 0
										|| (int) e.getX() > btn_detail
												.getWidth()
										|| (int) e.getY() < 0
										|| (int) e.getY() > btn_detail
												.getHeight())
							{
								btn_detail.setAlpha(1000);
							}
							else
							{
								btn_detail.setAlpha(1000);				
								Intent myIntent = new Intent(
										ScheduleListActivity.this,
										FriendDetailViewActivity.class);
								
								myIntent.putExtra("schedule_no", sno);
								startActivityForResult(myIntent, 2);
							}
						}
						else
						{
							btn_detail.setAlpha(1000);
						}
						return true;
					}
				});
				

				List<Schedule> slist = AppManager.getInstance().getSm().getScheduleList().getList();
				boolean check = false;
				
				if(!schedule.isPublic()){
					btnCollection.removeViewInLayout((ImageView)btnCollection.findViewById(R.id.btn_join));
					btnCollection.removeViewInLayout((ImageView)btnCollection.findViewById(R.id.btn_detail));
				}
				
				else{
					for(Schedule s : slist){
						if(s.getNo()==schedule.getNo()){
							mlayout.setBackgroundResource(R.drawable.boarder_bottom_gray);
							check=true;
						}
					}
					
					if(check){
						btnCollection.removeViewInLayout((ImageView)btnCollection.findViewById(R.id.btn_join));
					}
					else if(schedule.isOpen()==true)
					{
						final ImageView btn_join = (ImageView)mlayout.findViewById(R.id.btn_join);
						//join 눌럿을때
						btn_join.setOnTouchListener(new OnTouchListener()
						{
							public boolean onTouch(View view, MotionEvent e)
							{
								if (e.getAction() == MotionEvent.ACTION_DOWN)
								{
									btn_join.setAlpha(80);
								} 
								else if (e.getAction() == MotionEvent.ACTION_UP)
								{
									if ((int) e.getX() < 0
												|| (int) e.getX() > btn_join
														.getWidth()
												|| (int) e.getY() < 0
												|| (int) e.getY() > btn_join
														.getHeight()) 
									{
										btn_join.setAlpha(1000);
									}
									else
									{
										btn_join.setAlpha(1000);				
										String myEmail=getSharedPreferences("withschedule", 0).getString("email", "null");
										enterRequestAction(sno,myEmail,friendEmail);
									}
								}
								else
								{
									btn_join.setAlpha(1000);
								}
								return true;
							}
						});
					}			
					else
					{
						btnCollection.removeViewInLayout((ImageView)btnCollection.findViewById(R.id.btn_join));
					}
				}
				
				table.addView(mlayout);
			}
		}
	}
	
	private void enterRequestAction(final int sno,final String senderEmail,final String receiverEmail){
		new AlertDialog.Builder(ScheduleListActivity.this)
		.setTitle("일정참가")
		.setMessage("정말로 일정에 참가하시겠습니까?")
		.setPositiveButton("확인", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1)
			{
				String result = ScheduleSetting.enterRequest(sno,senderEmail,receiverEmail);
			    			    
				if(result.equals(AppManager.COMPLETION)){
					
				}			      
				else if(result.equals(AppManager.NO_SEARCH)){
					showDialog(AppManager.POPUP_G);
				}
				else if(result.equals(AppManager.HTTP_ERROR)){
					showDialog(AppManager.POPUP_R);
				}
				else{
					showDialog(AppManager.POPUP_Q);
				}	
			}
		})
		.setNegativeButton("취소", null)
		.show();
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
	
	private class DatePickerListener implements OnClickListener{
		public void onClick(View paramAnonymousView)
		{
			final LinearLayout linear = (LinearLayout)View.inflate(ScheduleListActivity.this, R.layout.schedule_date_select, null);
			final DatePicker dp;
		    dp = (DatePicker)linear.findViewById(R.id.maindatePicker);
			
			dp.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
				
				public void onDateChanged(DatePicker arg0, int arg1, int arg2, int arg3) {
					calendar.set(Calendar.YEAR,arg1);
					calendar.set(Calendar.MONTH,arg2);
					calendar.set(Calendar.DAY_OF_MONTH,arg3);
				}
			});
							
			new AlertDialog.Builder(ScheduleListActivity.this)
			.setTitle("날짜 설정")
			.setIcon(R.drawable.time_choice)
			.setView(linear)
			.setPositiveButton("확인", new DialogInterface.OnClickListener()
			{
			public void onClick(DialogInterface arg0, int arg1)
			{
				LinearLayout table = (LinearLayout) findViewById(R.id.schedule_table);
				table.removeAllViews();
				friendsScheduleMap = ScheduleSetting.setScheduleList2(calendar,friendEmailList);
				if(friendsScheduleMap==null){
					//Error 확인(dialog)필요??
					friendsScheduleMap =new HashMap<String,ArrayList<Schedule>>();
				}
				setScheduleTable(friendsScheduleMap);
			}
			})
			.setNegativeButton("취소", null)
			.show();
		}		
	}
	private class PrevClickListener implements View.OnClickListener{
		public void onClick(View v) {
			  setMoveDay(-1);
			  LinearLayout table = (LinearLayout) findViewById(R.id.schedule_table);
			  table.removeAllViews();
			  friendsScheduleMap = ScheduleSetting.setScheduleList2(calendar,friendEmailList);
				if(friendsScheduleMap==null){
					//Error 확인(dialog)필요??
					friendsScheduleMap =new HashMap<String,ArrayList<Schedule>>();
				}
			  setScheduleTable(friendsScheduleMap);
		  }
	}
	
	private class NextClickListener implements View.OnClickListener{
		public void onClick(View v) {
			  setMoveDay(+1);
			  LinearLayout table = (LinearLayout) findViewById(R.id.schedule_table);
			  table.removeAllViews();
			  friendsScheduleMap = ScheduleSetting.setScheduleList2(calendar,friendEmailList);
				if(friendsScheduleMap==null){
					//Error 확인(dialog)필요??
					friendsScheduleMap =new HashMap<String,ArrayList<Schedule>>();
				}
			  setScheduleTable(friendsScheduleMap);
		  }
	}
	@Override
	 public boolean onCreateOptionsMenu(Menu menu) {
	  // TODO Auto-generated method stub
	  MenuItem item1 = menu.add(0,5,0,"도움말").setIcon(R.drawable.imformation);
	  
	  //final LinearLayout linear = (LinearLayout)View.inflate(CalendarActivity.this, R.layout.help_calendar, null);
	  item1.setOnMenuItemClickListener(new OnMenuItemClickListener() {   
	   @Override
	   public boolean onMenuItemClick(MenuItem arg0) {
	    // TODO Auto-generated method stub
	    
	    final LinearLayout linear = (LinearLayout)View.inflate(ScheduleListActivity.this, R.layout.help_schedule_list_view, null);
	    
	    new AlertDialog.Builder(ScheduleListActivity.this)
	    .setTitle("도움말")
	    .setView(linear)
	    .setIcon(R.drawable.imformation)
	    .show();
	    return false;
	   }
	  });
	  return super.onCreateOptionsMenu(menu);  
	 }
}
