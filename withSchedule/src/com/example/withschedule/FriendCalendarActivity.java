package com.example.withschedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.withschedule.dto.CntInfo;
import com.example.withschedule.schedule.*;
import com.example.withschedule.socket.ScheduleSetting;
import com.ibm.icu.util.ChineseCalendar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class FriendCalendarActivity extends Activity
{
	private ArrayList<TextView> dayListView;
	private ArrayList<TextView> sNumListView;
	private TextView todayView;
	private TextView dayInfoView;
	private TextView whoView;
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
	private String friendEmail;
	private String friendname;
	private int currentCheckDay = 0;	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_calendar_view);
		
		this.friendEmail  = getIntent().getStringExtra("email");
		this.friendname  = getIntent().getStringExtra("name");
		//친구의 계정을 받아와서 서버에 친구의 리스트를 받아오는 부분 추가해야됨.
		
		ChineseCalendar cc = new ChineseCalendar();
		Calendar calendar = Calendar.getInstance();
		
		cc.setTimeInMillis(calendar.getTimeInMillis());
		whoView	= (TextView) findViewById(R.id.who);
		whoView.setText(friendname+"님의 스케줄");
		iYear = calendar.get(Calendar.YEAR);
		iMonth = calendar.get(Calendar.MONTH);
		iDay = calendar.get(Calendar.DATE);
		nowYear = calendar.get(Calendar.YEAR);
		nowMonth = calendar.get(Calendar.MONTH);
		nowDay = calendar.get(Calendar.DATE);

		todayView = (TextView) findViewById(R.id.today);
		dayInfoView = (TextView) findViewById(R.id.day_info);
		
		ScheduleList result = ScheduleSetting.friendSchedule(friendEmail);
		if(result==null){
			//Error 표시(dialog) 필요??
			AppManager.getInstance().getSm().setFriendSchedule(new ScheduleList());
		}else{
			AppManager.getInstance().getSm().setFriendSchedule(result);
		}		

		scheduleTableSetting(AppManager.getInstance().getSm().getFriendScheduleOfDay(nowYear,nowMonth,nowDay));
		
		cYear = cc.get(ChineseCalendar.YEAR);
		cMonth = cc.get(ChineseCalendar.MONTH) + 1;
		cDay = cc.get(ChineseCalendar.DAY_OF_MONTH);
		
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
				LinearLayout datelayout = new LinearLayout(this);
				datelayout.setGravity(Gravity.CENTER_HORIZONTAL);
				datelayout.setPadding(0, 2, 0, 2);
				datelayout.setOrientation(1);
				dayText.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

				dayText.setGravity(Gravity.CENTER_HORIZONTAL);
				sNumText.setGravity(Gravity.CENTER_HORIZONTAL);
				datelayout.addView(dayText);
				datelayout.addView(sNumText);
				weekTableRow.addView(datelayout);
				dayListView.add(dayText);
				sNumListView.add(sNumText);
			}
			monthTablelayout.addView(weekTableRow);
		}

		monthTablelayout.setStretchAllColumns(true);

		monthTablelayout = (TableLayout) findViewById(R.id.week_table);
		monthTablelayout.setStretchAllColumns(true);
		
		scheduleTableSetting(AppManager.getInstance().getSm().getFriendScheduleOfDay(nowYear,nowMonth,nowDay));
		
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
										FriendCalendarActivity.this,
										TimeScheduleActivity.class);
								ArrayList<Integer> date = new ArrayList<Integer>();
								
								date.add(iYear);
								date.add(iMonth+1);
								date.add(iDay);
								myIntent.putIntegerArrayListExtra(
										"friend_date", date);
								startActivity(myIntent);
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
				final LinearLayout linear = (LinearLayout)View.inflate(FriendCalendarActivity.this, R.layout.schedule_date_select, null);
				final DatePicker dp;
			    dp = (DatePicker)linear.findViewById(R.id.maindatePicker);
				
				dp.init(iYear, iMonth, iDay, new DatePicker.OnDateChangedListener() {
					
					public void onDateChanged(DatePicker arg0, int arg1, int arg2, int arg3) {
						iYear=arg1;
						iMonth=arg2;
						iDay=arg3;
					}
				});
								
				new AlertDialog.Builder(FriendCalendarActivity.this)
				.setTitle("날짜 설정")
				.setIcon(R.drawable.time_choice)
				.setView(linear)
				.setPositiveButton("확인", new DialogInterface.OnClickListener()
				{
				public void onClick(DialogInterface arg0, int arg1)
				{
					setCalendar(iYear, iMonth);
					scheduleTableSetting(AppManager.getInstance().getSm().getFriendSchedule().getScheduleOfDay(iYear,iMonth,iDay));
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
										.getSm().getFriendSchedule().getScheduleOfDay(y, m-1, d));
							}
						}
					}
					return true;
				}
			};
							
			((LinearLayout) dayListView.get(i).getParent()).setOnTouchListener(listOn);
			if(AppManager.getInstance().getSm().getFriendSchedule().getScheduleOfDay(year, month, j).size()==0)
		    {
				sNumListView.get(i).setText(" ");
		    }
		    else
		    {
		    	sNumListView.get(i).setText("(" + AppManager.getInstance()
		      .getSm().getFriendSchedule().getScheduleOfDay(year, month, j).size() + ")");
		    }
			
			dayListView.get(i).setTextSize(18);
			dayListView.get(i).setText(j++ + "");
			sNumListView.get(i).setTextSize(12);
			
			calendar.set(year, month, j);
		}

	}

	public void scheduleTableSetting(ArrayList<Schedule> sl) 
	{
		LinearLayout scheduleTable = (LinearLayout) findViewById(R.id.shcedule_table);
		scheduleTable.removeAllViews();
		dayInfoView.setText(iYear + "/" + (iMonth+1) +"/" + iDay +"(음력 " + cMonth + "/" + cDay +")");
		ArrayList<CntInfo> cntList = ScheduleSetting.checkScheduleCNT(sl);
		int i=0;
		for (Schedule schedule : sl)
		{
			final int sno = schedule.getNo();
			RelativeLayout mlayout = (RelativeLayout) LayoutInflater.from(FriendCalendarActivity.this).inflate(R.layout.schedule_row, null, false);
			LinearLayout btnCollection = (LinearLayout)mlayout.findViewById(R.id.btn_collection);
			btnCollection.removeViewInLayout((ImageView)btnCollection.findViewById(R.id.btn_exit));
			btnCollection.removeViewInLayout((ImageView)btnCollection.findViewById(R.id.btn_alram));
			TextView titleView = (TextView)mlayout.findViewById(R.id.schedule_title);
			if (schedule.getTitle().length() > 10)
			{
				titleView.setText(schedule.getTitle().substring(0, 10) + "...");
			} 
			else
			{
				titleView.setText(schedule.getTitle());
			}

			TextView timeView = (TextView)mlayout.findViewById(R.id.schedule_time);
			timeView.setText("※ "
							+ schedule.getStartTime().get(
									Calendar.HOUR_OF_DAY)
							+ ":"
							+ schedule.getStartTime().get(
									Calendar.MINUTE) + "~" +"댓글 ("+cntList.get(i).getCcnt()+") 참여인원("+cntList.get(i++).getEcnt()+")");

			final ImageView btnDetail = (ImageView)mlayout.findViewById(R.id.btn_detail);
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
									|| (int) e.getX() > btnDetail.getWidth()
									|| (int) e.getY() < 0
									|| (int) e.getY() > btnDetail.getHeight()) 
						{
							btnDetail.setAlpha(1000);
						}
						
						else
						{
							btnDetail.setAlpha(1000);				
							Intent myIntent = new Intent(
									FriendCalendarActivity.this,
									FriendDetailViewActivity.class);
							myIntent.putExtra("schedule_no", sno);
							startActivityForResult(myIntent, 2);
						}
					}
					else
					{
						btnDetail.setAlpha(1000);
					}
					return true;
				}
			});
			
			//detail 눌럿을떄
			List<Schedule> sList = AppManager.getInstance().getSm().getScheduleList().getList();
			boolean check = false;
			
			if(!schedule.isPublic()){
				btnCollection.removeViewInLayout((ImageView)btnCollection.findViewById(R.id.btn_join));
				btnCollection.removeViewInLayout((ImageView)btnCollection.findViewById(R.id.btn_detail));
			}
			else{
				for(Schedule s : sList)
				{
					if(s.getNo()==schedule.getNo())
					{
						mlayout.setBackgroundResource(R.drawable.boarder_bottom_gray);
						check=true;
					}
				}
				
				if(check)
				{
					btnCollection.removeViewInLayout((ImageView)btnCollection.findViewById(R.id.btn_join));
				}
				else if(schedule.isOpen()==true)
				{
					final ImageView btnJoin = (ImageView)mlayout.findViewById(R.id.btn_join);
					//join 눌럿을때
					btnJoin.setOnTouchListener(new OnTouchListener()
					{
						public boolean onTouch(View view, MotionEvent e)
						{
							if (e.getAction() == MotionEvent.ACTION_DOWN)
							{
								btnJoin.setAlpha(80);
							} 
							else if (e.getAction() == MotionEvent.ACTION_UP)
							{
								if ((int) e.getX() < 0
											|| (int) e.getX() > btnJoin
													.getWidth()
											|| (int) e.getY() < 0
											|| (int) e.getY() > btnJoin
													.getHeight()) 
								{
									btnJoin.setAlpha(1000);
								}
								else
								{
									btnJoin.setAlpha(1000);		
									String myEmail=getSharedPreferences("withschedule", 0).getString("email", "null");
									enterRequestAction(sno,myEmail,friendEmail);
								}
							}
							else
							{
								btnJoin.setAlpha(1000);
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
											
			scheduleTable.addView(mlayout);
			}
		}
		
	private void enterRequestAction(final int sno,final String senderEmail,final String receiverEmail){
		new AlertDialog.Builder(FriendCalendarActivity.this)
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
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode)
		{
		case 1:
			if (resultCode == -1)
			{
				setCalendar(iYear, iMonth);
				scheduleTableSetting(AppManager.getInstance().getSm().getFriendScheduleOfDay(iYear,iMonth,iDay));
			}
		case 2:
			if (resultCode == -1) 
			{
				setCalendar(iYear, iMonth);
				scheduleTableSetting(AppManager.getInstance().getSm().getFriendScheduleOfDay(iYear,iMonth,iDay));
			}
		}
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
	@Override
	 public boolean onCreateOptionsMenu(Menu menu) {
	  // TODO Auto-generated method stub
	  MenuItem item1 = menu.add(0,5,0,"도움말").setIcon(R.drawable.imformation);
	  
	  //final LinearLayout linear = (LinearLayout)View.inflate(CalendarActivity.this, R.layout.help_calendar, null);
	  item1.setOnMenuItemClickListener(new OnMenuItemClickListener() {   
	   @Override
	   public boolean onMenuItemClick(MenuItem arg0) {
	    // TODO Auto-generated method stub
	    
	    final LinearLayout linear = (LinearLayout)View.inflate(FriendCalendarActivity.this, R.layout.help_friend_calendar_view, null);
	    
	    new AlertDialog.Builder(FriendCalendarActivity.this)
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
