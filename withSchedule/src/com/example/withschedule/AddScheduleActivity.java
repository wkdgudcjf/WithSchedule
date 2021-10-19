package com.example.withschedule;

import java.util.*;

import com.example.withschedule.socket.ScheduleSetting;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.*;

public class AddScheduleActivity extends Activity implements OnCheckedChangeListener
{
	private TextView dateView;
	private Spinner repeatSpinner;
	private CheckBox alarmCheckBox;
	private TextView titleView;
	private TextView memoView;
	private GregorianCalendar startdate;
	private RadioGroup alarmRadioGroup;
	private RadioGroup publicRadioGroup;
	private RadioGroup openRadioGroup;
	private ArrayList<Integer> initdate;
	private Button confirmButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_add_view);
		final Intent localIntent = getIntent();
		initdate = localIntent.getIntegerArrayListExtra("date");
		
		dateView = (TextView)findViewById(R.id.start_time);
		titleView = (TextView)findViewById(R.id.edit_title);
		memoView = (TextView)findViewById(R.id.edit_memo);
		repeatSpinner = (Spinner)findViewById(R.id.repeat);
		publicRadioGroup = (RadioGroup)findViewById(R.id.publicgroup);
		openRadioGroup = (RadioGroup)findViewById(R.id.opengroup);
		alarmRadioGroup = (RadioGroup)findViewById(R.id.alarmgroup);
		startdate = new GregorianCalendar(initdate.get(0),initdate.get(1)-1,initdate.get(2));
		dateView.setText(startdate.get(Calendar.YEAR)+"년-"+(startdate.get(Calendar.MONTH)+1)+"월-"+startdate.get(Calendar.DATE)+"일" + " 오전 0시 0분");
					
		alarmRadioGroup.setVisibility(BIND_NOT_FOREGROUND);
		alarmCheckBox = (CheckBox)findViewById(R.id.alarmcheck);
		alarmCheckBox.setOnCheckedChangeListener(this);
				
		LinearLayout titleLinearLayout = (LinearLayout)findViewById(R.id.title);
		LinearLayout memoLinearLayout = (LinearLayout)findViewById(R.id.memo);
		LinearLayout startLinearLayout = (LinearLayout)findViewById(R.id.start);
		
		titleLinearLayout.setOnClickListener(new OnClickListener() 
		{			
			EditText titleEditText;
			@Override
			public void onClick(View arg0)
			{	
				final LinearLayout linear = (LinearLayout)View.inflate(AddScheduleActivity.this, R.layout.edit_text, null);
				
				titleEditText = (EditText)linear.findViewById(R.id.edit_text);
				titleEditText.setText(titleView.getText());	
				
				new AlertDialog.Builder(AddScheduleActivity.this)
				.setTitle("제목")
				.setView(linear)
				.setPositiveButton("확인", new DialogInterface.OnClickListener()
				{
				public void onClick(DialogInterface arg0, int arg1)
				{
					titleView.setText(titleEditText.getText());
					InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);   
					imm.hideSoftInputFromWindow(titleEditText.getWindowToken(),0); 
				}
				})
				.setNegativeButton("취소", null)
				.show();
			}
		});
		
		memoLinearLayout.setOnClickListener(new OnClickListener() {			
			EditText memoEditText;
			@Override
			public void onClick(View arg0) {	
				final LinearLayout linear = (LinearLayout)View.inflate(AddScheduleActivity.this, R.layout.edit_text, null);
				
				memoEditText = (EditText)linear.findViewById(R.id.edit_text);
			    memoEditText.setText(memoView.getText());	
				
				new AlertDialog.Builder(AddScheduleActivity.this)
				.setTitle("메모")
				.setView(linear)
				.setPositiveButton("확인", new DialogInterface.OnClickListener()
				{
				public void onClick(DialogInterface arg0, int arg1)
				{
					memoView.setText(memoEditText.getText());
					InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);   
					imm.hideSoftInputFromWindow(memoEditText.getWindowToken(),0); 
				}
				})
				.setNegativeButton("취소", null)
				.show();
			}
		});
		startLinearLayout.setOnClickListener(new View.OnClickListener()
		{
			private int getCurrentHour( TimePicker timePicker )
			{
				Integer hour = Integer.parseInt( ((EditText)((LinearLayout) ( ( LinearLayout ) timePicker.getChildAt(0) ).getChildAt(0))
					.getChildAt(1)).getText().toString() );
				// 오후일 경우
				if( ((Button) (( LinearLayout ) timePicker.getChildAt(0)).getChildAt(2) ).getText().toString().equals("오후") ) {
					// 오후이면서 12시라면 12시를 넘겨주고
					if( hour == 12 ) {
						return hour;
					}
					// 오후이면서 12시가 아니면 5시일경우 17시가 된다
					else return 12 + hour;
					}
						
					// 오전일경우 
					else {
					// 오전이면서 12시면 자정이기때문에 0을 넘겨주고
					if( hour == 12 ) {
						return 0;
					}
					// 오전이면서 12시가 아니면 새벽이나 아침이기 때문에 그냥 hour
					else {
						return hour;
					}
				}
			}
			private int getCurrentMinute( TimePicker timePicker ) {
				Integer minute = Integer.parseInt( ((EditText)((LinearLayout) ( ( LinearLayout ) timePicker.getChildAt(0) ).getChildAt(1))
						.getChildAt(1)).getText().toString() );
				return minute;
			}

			public void onClick(View paramAnonymousView) {
				final LinearLayout linear = (LinearLayout)View.inflate(AddScheduleActivity.this, R.layout.schedule_time_date_select, null);
				final TimePicker timePicker;
				final DatePicker datePicker;
				datePicker = (DatePicker)linear.findViewById(R.id.datePicker);
			    timePicker = (TimePicker)linear.findViewById(R.id.timePicker);
				
			    timePicker.setCurrentHour(startdate.get(Calendar.HOUR_OF_DAY));
			    timePicker.setCurrentMinute(startdate.get(Calendar.MINUTE));
						
			    datePicker.init(startdate.get(Calendar.YEAR), startdate.get(Calendar.MONTH), startdate.get(Calendar.DATE), new DatePicker.OnDateChangedListener() {
							
					public void onDateChanged(DatePicker arg0, int arg1, int arg2, int arg3)
					{
						startdate.set(arg1, arg2, arg3);
					}
				});
						
				new AlertDialog.Builder(AddScheduleActivity.this)
				.setTitle("시간설정")
				.setIcon(R.drawable.time_choice)
				.setView(linear)
				.setPositiveButton("확인", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{
						startdate.set(Calendar.HOUR_OF_DAY, getCurrentHour(timePicker));
						startdate.set(Calendar.MINUTE, getCurrentMinute(timePicker));
						String str = startdate.get(Calendar.AM_PM) == 0 ? "오전" : "오후";
						dateView.setText(startdate.get(Calendar.YEAR)+"년-"+(startdate.get(Calendar.MONTH)+1)+"월-"+startdate.get(Calendar.DATE)+"일 "+str+" "+startdate.get(Calendar.HOUR)+"시 "+startdate.get(Calendar.MINUTE)+"분");
					}
					})
					.setNegativeButton("취소", null)
					.show();
					}
				});
		
		publicRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch(checkedId){
				case R.id.publicradio:
					openRadioGroup.setVisibility(View.VISIBLE);
					break;
				case R.id.privateradio:
					openRadioGroup.check(R.id.close);
					openRadioGroup.setVisibility(View.INVISIBLE);
					break;
				}
			}
		});
		
		confirmButton = (Button)findViewById(R.id.button_ok);
		confirmButton.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View paramAnonymousView)
			{
				if (titleView.getText().toString().equals(""))
		        {
					showDialog(AppManager.POPUP_N);
		            return;
		        }
				if(alarmCheckBox.isChecked())
				{
					int id = alarmRadioGroup.getCheckedRadioButtonId();
					int aTime;
					if(id==R.id.a_5)
					{
						aTime=5;
					}
					else if(id==R.id.a_15)
					{
						aTime=15;
					}
					else
					{
						aTime=30;
					}
					
					GregorianCalendar testGc = (GregorianCalendar) startdate.clone();
					testGc.set(Calendar.MINUTE, startdate.get(Calendar.MINUTE)-aTime);
					
					if(Calendar.getInstance().compareTo(testGc)>=0)
					{
						showDialog(AppManager.POPUP_O);
						return;
					}
					
				}
				String check = createSchedule();
				if(check.equals(AppManager.COMPLETION))
				{
					localIntent.putExtra("check", "ok");
					AddScheduleActivity.this.setResult(-1, localIntent);
					AddScheduleActivity.this.finish();
				}
				else if(check.equals(AppManager.NO_SEARCH)){
					showDialog(AppManager.POPUP_J);
				}
				else if(check.equals(AppManager.HTTP_ERROR)){
					showDialog(AppManager.POPUP_R);
				}
				else{
					showDialog(AppManager.POPUP_F);
				}
			}
		});
	}
	private String createSchedule()
	{
		String title = titleView.getText().toString();
		String memo = memoView.getText()==null?"":memoView.getText().toString();
		boolean isPublic;
		RadioButton publicradioView = (RadioButton) findViewById(publicRadioGroup.getCheckedRadioButtonId());
		if(publicradioView.getText().equals("공개"))
		{
			isPublic=true;
		}
		else
		{
			isPublic=false;
		}
		boolean isOpen;
		RadioButton openradioView = (RadioButton) findViewById(openRadioGroup.getCheckedRadioButtonId());
		if(openradioView.getText().equals("가능"))
		{
			isOpen=true;
		}
		else
		{
			isOpen=false;
		}
		
		int type;
		String str = repeatSpinner.getSelectedItem().toString();
		if(str.equals("한번"))
		{
			type=AppManager.GENERAL;
		}
		else if(str.equals("매주"))
		{
			type=AppManager.WEEK_REPEAT;
		}
		else if(str.equals("월말"))
		{
			type=AppManager.LASTDAY_REPEAT;
		}
		else if(str.equals("매달"))
		{
			type=AppManager.MONTH_REPEAT;
		}
		else
		{
			type=AppManager.YEAR_REPEAT;
		}
		
		if(type==AppManager.LASTDAY_REPEAT)
		{
			startdate.set(Calendar.DATE, startdate.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		
		int atime = 0;
		if(alarmCheckBox.isChecked())
		{
			int id = alarmRadioGroup.getCheckedRadioButtonId();
			if(id==R.id.a_5)
			{
				atime=5;
			}
			else if(id==R.id.a_15)
			{
				atime=15;
			}
			else
			{
				atime=30;
			}
		}
		return ScheduleSetting.enroll(AddScheduleActivity.this,alarmCheckBox.isChecked(), alarmRadioGroup.getCheckedRadioButtonId(), atime, title, memo, type, isOpen, isPublic, startdate);
	}
	
	@Override
	public void onCheckedChanged(CompoundButton bv, boolean check)
	{
		if(check)
		{
			alarmRadioGroup.setVisibility(MODE_APPEND);
		}
		else
		{
			alarmRadioGroup.setVisibility(BIND_NOT_FOREGROUND);
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
	

}
