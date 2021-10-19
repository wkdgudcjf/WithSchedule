package com.example.withschedule;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.example.withschedule.alarm.*;
import com.example.withschedule.db.ExcuteDB;
import com.example.withschedule.dto.CommentInfo;
import com.example.withschedule.dto.EntryInfo;
import com.example.withschedule.friend.Friend;

import com.example.withschedule.schedule.Schedule;
import com.example.withschedule.socket.*;

import android.app.*;
import android.content.*;

import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.*;
import android.view.MenuItem.*;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class DetailViewActivity extends Activity{
	private ImageView btn_infoImageView;
	private ImageView btn_entryImageView;
	private ImageView btn_talkImageView;
	private ImageView btn_ModifyImageView;
	
	private View infoPage,commentPage,entryPage,modifyPage;
	private int scheduleNo;
	
	private Schedule schedule;
	private GregorianCalendar startdate;
	private CommentListAdapter cmtListAdapter;
	int currentPage = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_detail_view);	
		
		btn_infoImageView=(ImageView)findViewById(R.id.btn_info);
		btn_entryImageView=(ImageView)findViewById(R.id.btn_entry);
		btn_talkImageView=(ImageView)findViewById(R.id.btn_talk);
		btn_ModifyImageView=(ImageView)findViewById(R.id.btn_schedule_setting);		
		btn_infoImageView.setAlpha(1000);
		btn_entryImageView.setAlpha(300);
		btn_talkImageView.setAlpha(300);
		btn_ModifyImageView.setAlpha(300);
				
		infoPage=findViewById(R.id.info_page);
		entryPage=findViewById(R.id.entry_page);
		commentPage=findViewById(R.id.comment_page);
		modifyPage=findViewById(R.id.modify_page);
		
		infoPage.setVisibility(View.VISIBLE);
		entryPage.setVisibility(View.INVISIBLE);	
		commentPage.setVisibility(View.INVISIBLE);
		modifyPage.setVisibility(View.INVISIBLE);	
				
		scheduleNo  = getIntent().getIntExtra("schedule_no", -1);
		schedule = AppManager.getInstance().getSm().search(scheduleNo);
		startdate = schedule.getStartTime();
		if(schedule==null){
			showDialog(AppManager.POPUP_I);
			finish();
		}

		ArrayList<Schedule> slist = new ArrayList<Schedule>();
		slist.add(schedule);
		InfoAdapter detailAdapter = new InfoAdapter(DetailViewActivity.this,R.layout.schedule_detail_info_page,slist);
		
		ListView infolist;
		infolist = (ListView)findViewById(R.id.info_list);
		infolist.setAdapter(detailAdapter);
		OnClickListener mClickListener = new OnClickListener() 
		{			
			@Override
			public void onClick(View view) {
				infoPage.setVisibility(View.INVISIBLE);
				entryPage.setVisibility(View.INVISIBLE);
				commentPage.setVisibility(View.INVISIBLE);
				modifyPage.setVisibility(View.INVISIBLE);
				
				switch(view.getId()){
					case R.id.btn_info:
						currentPage=1;
						if(!AppManager.getInstance().isPossibleInternet()){
							showDialog(AppManager.POPUP_J);
						}
						else{
							//서버에서 최신 일정정보 업데이트!!
						}

						schedule = AppManager.getInstance().getSm().search(scheduleNo);
						if(schedule==null){
							showDialog(AppManager.POPUP_I);
							finish();
						}
						ArrayList<Schedule> sList = new ArrayList<Schedule>();
						sList.add(schedule);
						InfoAdapter detailAdapter = new InfoAdapter(DetailViewActivity.this,R.layout.schedule_detail_info_page,sList);
						
						ListView infoList;
						infoList = (ListView)findViewById(R.id.info_list);
						infoList.setAdapter(detailAdapter);
						
						btn_infoImageView.setImageResource(R.drawable.info);
						btn_entryImageView.setImageResource(R.drawable.entry2);
						btn_talkImageView.setImageResource(R.drawable.talk2);
						btn_ModifyImageView.setImageResource(R.drawable.schedule_setting2);
						btn_infoImageView.setAlpha(1000);
						btn_entryImageView.setAlpha(300);
						btn_talkImageView.setAlpha(300);
						btn_ModifyImageView.setAlpha(300);
						infoPage.setVisibility(View.VISIBLE);
						break;
					case R.id.btn_entry:
						currentPage=2;
						if(!AppManager.getInstance().isPossibleInternet()){
							showDialog(AppManager.POPUP_J);
							infoPage.setVisibility(View.VISIBLE);
							break;
						}
						
						btn_infoImageView.setImageResource(R.drawable.info2);
						btn_entryImageView.setImageResource(R.drawable.entry);
						btn_talkImageView.setImageResource(R.drawable.talk2);
						btn_ModifyImageView.setImageResource(R.drawable.schedule_setting2);
						btn_infoImageView.setAlpha(300);
						btn_entryImageView.setAlpha(1000);
						btn_talkImageView.setAlpha(300);
						btn_ModifyImageView.setAlpha(300);
						ArrayList<EntryInfo> eList =  new ArrayList<EntryInfo>();
						
						eList = ScheduleSetting.entryCheck(scheduleNo);
						
						if(eList==null)
						{
							//Error 확인(dialog)필요??
						}
						else
						{
							EntryListAdapter etyListAdapter = new EntryListAdapter(DetailViewActivity.this,R.layout.entry_row,eList);
							
							ListView entryList;
							entryList = (ListView)findViewById(R.id.entry_list);
							entryList.setAdapter(etyListAdapter);
						}
						entryPage.setVisibility(View.VISIBLE);
						break;
					case R.id.btn_talk:
						currentPage=3;
						if(!AppManager.getInstance().isPossibleInternet())
						{
							showDialog(AppManager.POPUP_J);
							infoPage.setVisibility(View.VISIBLE);
							break;
						}
						btn_infoImageView.setImageResource(R.drawable.info2);
						btn_entryImageView.setImageResource(R.drawable.entry2);
						btn_talkImageView.setImageResource(R.drawable.talk);
						btn_ModifyImageView.setImageResource(R.drawable.schedule_setting2);
						btn_infoImageView.setAlpha(300);
						btn_entryImageView.setAlpha(300);
						btn_talkImageView.setAlpha(1000);
						btn_ModifyImageView.setAlpha(300);					
						
						final ArrayList<CommentInfo> cList = ScheduleSetting.commentCheck(scheduleNo);						
						if(cList==null)
						{ 
							//Error 확인(dialog)필요??
						}
						else
						{
							cmtListAdapter = new CommentListAdapter(DetailViewActivity.this,R.layout.comment_row,cList);
							
							ListView commentList;
							commentList = (ListView)findViewById(R.id.comment_list);
							commentList.setAdapter(cmtListAdapter);
						}
						Button commentEnrollButton = (Button)findViewById(R.id.enroll_comment_btn);
						SharedPreferences pref = getSharedPreferences(
								"withschedule", 0);
						final String email = pref.getString("email", "");
						ArrayList<EntryInfo> eList2 =  new ArrayList<EntryInfo>();
						eList2 = ScheduleSetting.entryCheck(scheduleNo);
						final CheckBox commentAlarmCheckBox = (CheckBox)findViewById(R.id.comment_alarm);
						boolean checkMe=false;
						if(eList2.size()==1)
						{
							commentAlarmCheckBox.setVisibility(BIND_NOT_FOREGROUND);
						}
						else
						{
							for(int i=0;i<eList2.size();i++)
							{
								if(eList2.get(i).getEmail().equals(email))
								{
									checkMe=true;
									break;
								}
							}
							if(checkMe==false)
							{
								commentAlarmCheckBox.setVisibility(BIND_NOT_FOREGROUND);
							}
						}
						
						commentEnrollButton.setOnClickListener(new OnClickListener() {			
						@Override
							public void onClick(View arg0) {
								EditText commentEditText = (EditText)findViewById(R.id.enroll_comment);
								if(commentEditText.getText().toString().length()==0)
								{
									;
								}
								else
								{
									boolean gcm=false;
									if(commentAlarmCheckBox.isChecked()){
										gcm=true;
									}	
									GregorianCalendar gc = new GregorianCalendar();
									String check = ScheduleSetting.enrollcomment(scheduleNo,commentEditText.getText().toString(),email,gc,gcm);
									
									if(check.equals(AppManager.COMPLETION))
									{
										final ArrayList<CommentInfo> cList = ScheduleSetting.commentCheck(scheduleNo);						
										if(cList==null)
										{ 
											//Error 확인(dialog)필요??
										}
										else{
											cmtListAdapter.setClist(cList);
											cmtListAdapter.notifyDataSetChanged();
											commentEditText.setText("");
										}
									}
									else if(check.equals(AppManager.NO_SEARCH)){
										showDialog(AppManager.POPUP_G);
									}
									else if(check.equals(AppManager.HTTP_ERROR)){
										showDialog(AppManager.POPUP_R);
									}
									else{
										showDialog(AppManager.POPUP_F);
									}
								}
							}
						});
						commentPage.setVisibility(View.VISIBLE);
						break;
					case R.id.btn_schedule_setting:
						if(!AppManager.getInstance().isPossibleInternet())
						{
							showDialog(AppManager.POPUP_J);
							break;
						}		
						
						currentPage=4;

						schedule = AppManager.getInstance().getSm().search(scheduleNo);
						if(schedule==null)
						{
							showDialog(AppManager.POPUP_I);
							finish();
						}
						Schedule mschedule = new Schedule(schedule.getNo(),schedule.getTitle(),schedule.getMemo(),(GregorianCalendar)schedule.getStartTime().clone(),schedule.getType(),schedule.isOpen(),schedule.isPublic());
						ArrayList<Schedule> sList2 = new ArrayList<Schedule>();
						sList2.add(mschedule);
						ModifyAdapter modifyAdapter = new ModifyAdapter(DetailViewActivity.this,R.layout.schedule_detail_edit_page,sList2);
						
						ListView modifyList;
						modifyList = (ListView)findViewById(R.id.modify_list);
						modifyList.setAdapter(modifyAdapter);
						btn_infoImageView.setImageResource(R.drawable.info2);
						btn_entryImageView.setImageResource(R.drawable.entry2);
						btn_talkImageView.setImageResource(R.drawable.talk2);
						btn_ModifyImageView.setImageResource(R.drawable.schedule_setting);
						btn_infoImageView.setAlpha(300);
						btn_entryImageView.setAlpha(300);
						btn_talkImageView.setAlpha(300);
						btn_ModifyImageView.setAlpha(1000);
						modifyPage.setVisibility(View.VISIBLE);
						break;
				}
			}
		};
		
		btn_infoImageView.setOnClickListener(mClickListener);
		btn_entryImageView.setOnClickListener(mClickListener);
		btn_talkImageView.setOnClickListener(mClickListener);
		btn_ModifyImageView.setOnClickListener(mClickListener);
		
		
		
	}
	
	protected ArrayList<EntryInfo> parseOfEntryInfo(String entryInfo) 
	{
		JSONTokener jtk = new JSONTokener(entryInfo);
		JSONArray ja = null;
		try 
		{
			ja = (JSONArray) jtk.nextValue();
			
			ArrayList<EntryInfo> eList = new ArrayList<EntryInfo>();
			for (int i = 0; i < ja.length(); i++)
			{
				JSONObject job = ja.getJSONObject(i);
				String email = job.getString("email");
				String name = job.getString("name");
				boolean isOwner = job.getBoolean("isowner");			
				eList.add(new EntryInfo(name, email, isOwner));
			}
			
			return eList;
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}	
		return null;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = null;
		String popupText = getString(id);
		builder = new AlertDialog.Builder(this);
		builder.setMessage(popupText);
		builder.setNegativeButton("확인", null);
		return builder.create();
	}
	
	class CommentListAdapter extends BaseAdapter
	{
		Context maincon;
		LayoutInflater inflater;
		ArrayList<CommentInfo> cList;
		int layout;
		public CommentListAdapter(Context context,int layout,ArrayList<CommentInfo> cList)
		{
			this.maincon=context;
			this.layout=layout;
			this.cList=cList;
			this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			return cList.size();
		}
		public void setClist(ArrayList<CommentInfo> clist)
		{
			this.cList=clist;
		}
		@Override
		public CommentInfo getItem(int positon) {
			return cList.get(positon);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View cv, ViewGroup parent) 
		{
			if(cv==null)
			{
				cv = this.inflater.inflate(layout, parent,false);
			}

			SharedPreferences pref = getSharedPreferences("withschedule", 0);
			final String myEmail = pref.getString("email", "");
			final int pos = position;
			TextView commentView = (TextView)cv.findViewById(R.id.comment);
			TextView c_EmailView = (TextView)cv.findViewById(R.id.comment_email);
			TextView c_NameView = (TextView)cv.findViewById(R.id.comment_name);
			TextView c_DateView = (TextView)cv.findViewById(R.id.comment_date);
			ImageView btnRemove = (ImageView)cv.findViewById(R.id.btn_remove_comment);
			
			if(!(myEmail.equals(cList.get(position).getWriterEmail())))
			{
				btnRemove.setVisibility(View.INVISIBLE);
			}
			else
			{
				btnRemove.setVisibility(View.VISIBLE);
				btnRemove.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0)
					{
						final LinearLayout linear = (LinearLayout)View.inflate(DetailViewActivity.this, R.layout.comment_remove_check, null);
					      new AlertDialog.Builder(DetailViewActivity.this)
					      .setTitle("댓글삭제")
					      .setView(linear)
						.setPositiveButton("확인", new DialogInterface.OnClickListener()
						{
						public void onClick(DialogInterface arg0, int arg1)
						{
							if(!AppManager.getInstance().isPossibleInternet())
							{
								showDialog(AppManager.POPUP_J);
							}
							else
							{
								String check = ScheduleSetting.cancelcomment(scheduleNo,cList.get(pos).getCno());
								
								if(check.equals(AppManager.COMPLETION))
								{
									final ArrayList<CommentInfo> cList = ScheduleSetting.commentCheck(scheduleNo);						
									if(cList==null)
									{ 
										//Error 확인(dialog)필요??
									}
									else{
										cmtListAdapter.setClist(cList);
										cmtListAdapter.notifyDataSetChanged();
									}
								}
							}
							
						}})
						.setNegativeButton("취소", null)
						.show();
					}
				});
			}
			StringBuilder str1 = new StringBuilder();
			str1.append(cList.get(position).getDate().get(Calendar.YEAR));
			str1.append("년 ");
			str1.append(cList.get(position).getDate().get(Calendar.MONTH)+1);
			str1.append("월 ");
			str1.append(cList.get(position).getDate().get(Calendar.DAY_OF_MONTH));
			str1.append("일 ");
			String str3 = cList.get(position).getDate().get(Calendar.AM_PM) == 0 ? "오전" : "오후";
			str1.append(str3);
			str1.append(" ");
			str1.append(cList.get(position).getDate().get(Calendar.HOUR));
			str1.append("시 ");
			str1.append(cList.get(position).getDate().get(Calendar.MINUTE));
			str1.append("분 ");		
			
			commentView.setText(cList.get(position).getComment());
			c_NameView.setText(cList.get(position).getWriterName());
			c_EmailView.setText("("+cList.get(position).getWriterEmail()+")");
			c_DateView.setText(str1.toString());
			
							
		return cv;
		}
	}
	class EntryListAdapter extends BaseAdapter
	{
		Context maincon;
		LayoutInflater inflater;
		ArrayList<EntryInfo> eList;
		int layout;
		public EntryListAdapter(Context context,int layout,ArrayList<EntryInfo> eList)
		{
			this.maincon=context;
			this.layout=layout;
			this.eList=eList;
			this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			return eList.size();
		}

		@Override
		public EntryInfo getItem(int positon) {
			return eList.get(positon);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		public void setElist(ArrayList<EntryInfo> eList)
		{
			this.eList=eList;
		}
		@Override
		public View getView(int position, View cv, ViewGroup parent) 
		{
			final int pos = position;
			if(cv==null)
			{
				cv = this.inflater.inflate(layout, parent,false);
			}

			SharedPreferences pref = getSharedPreferences("withschedule", 0);
			final String myEmail = pref.getString("email", "");
			
			TextView nameView = (TextView)cv.findViewById(R.id.entry_name);
			TextView emailView = (TextView)cv.findViewById(R.id.entry_email);
			final ImageView btnAddFriend = (ImageView)cv.findViewById(R.id.btn_add_friend);
				
			if(AppManager.getInstance().getFm().search(eList.get(position).getEmail())!=null||myEmail.equals(eList.get(position).getEmail()))
			{
				btnAddFriend.setVisibility(View.INVISIBLE);
			}
			else
			{
				btnAddFriend.setVisibility(View.VISIBLE);
				btnAddFriend.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View arg0) 
					{
						final LinearLayout linear = (LinearLayout)View.inflate(DetailViewActivity.this, R.layout.friend_add_check, null);
						new AlertDialog.Builder(DetailViewActivity.this)
						.setTitle("친구추가")
						.setView(linear)
						.setPositiveButton("확인", new DialogInterface.OnClickListener()
						{
						public void onClick(DialogInterface arg0, int arg1)
						{
							if(!AppManager.getInstance().isPossibleInternet())
							{
								showDialog(AppManager.POPUP_J);
							}
							else
							{
								String check = MemberSetting.addFriend(DetailViewActivity.this,myEmail,eList.get(pos).getEmail());
								if(check.equals(AppManager.COMPLETION))
								{
									Friend friend = new Friend(eList.get(pos).getName(),eList.get(pos).getEmail(),"x");
									ExcuteDB.insertFriend(DetailViewActivity.this,friend);
									AppManager.getInstance().getFm().enroll(friend);
									btnAddFriend.setVisibility(View.INVISIBLE);
									Toast.makeText(getBaseContext(), eList.get(pos).getName()+"님과 친구가 되었습니다.", Toast.LENGTH_LONG).show();
								}
								else if(check.equals(AppManager.DUPLICATE))
								{
									showDialog(AppManager.POPUP_Q);
								}
								else
								{
									showDialog(AppManager.POPUP_G);
								}
							}
							
						}})
						.setNegativeButton("취소", null)
						.show();
					}
				});
			}
			nameView.setText(eList.get(position).getName());
			emailView.setText(eList.get(position).getEmail());
			
			return cv;		
		}
	}
	class InfoAdapter extends BaseAdapter
	{
		Context maincon;
		LayoutInflater inflater;
		ArrayList<Schedule> sList;
		int layout;
		public InfoAdapter(Context context,int layout,ArrayList<Schedule> sList)
		{
			this.maincon=context;
			this.layout=layout;
			this.sList=sList;
			this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			return sList.size();
		}

		@Override
		public Object getItem(int positon) {
			return sList.get(positon);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View cv, ViewGroup parent) {
			if(cv==null)
			{
				cv = this.inflater.inflate(layout, parent,false);
			}
			TextView titleView,startTimeView,isPublicView,isOpenView,repeatView,memoView;
			titleView = (TextView)cv.findViewById(R.id.info_title);
			startTimeView = (TextView)cv.findViewById(R.id.info_start_time);
			isPublicView = (TextView)cv.findViewById(R.id.info_is_public);
			isOpenView = (TextView)cv.findViewById(R.id.info_is_open);
			repeatView = (TextView)cv.findViewById(R.id.info_repeat);
			memoView = (TextView)cv.findViewById(R.id.info_memo);
			
			titleView.setText(sList.get(0).getTitle());
			
			GregorianCalendar gc = (GregorianCalendar) sList.get(0).getStartTime();
			String str1 = gc.get(Calendar.AM_PM) == 0 ? "오전" : "오후";
			StringBuilder str = new StringBuilder();
			str.append(gc.get(Calendar.YEAR));
			str.append("년 ");
			str.append(gc.get(Calendar.MONTH)+1);
			str.append("월 ");
			str.append(gc.get(Calendar.DAY_OF_MONTH));
			str.append("일 ");
			str.append(str1);
			str.append(" ");
			str.append(gc.get(Calendar.HOUR));
			str.append("시 ");
			str.append(gc.get(Calendar.MINUTE));
			str.append("분 ");		
			startTimeView.setText(str.toString());
			
			
			isPublicView.setText(sList.get(0).isPublic()? "공개":"비공개");
			isOpenView.setText(sList.get(0).isOpen()? "가능":"불가능");
						
			switch(sList.get(0).getType()){
			case AppManager.GENERAL: 
				repeatView.setText("한번");
				break;
			case AppManager.WEEK_REPEAT: 
				repeatView.setText("매주");
				break;
			case AppManager.LASTDAY_REPEAT: 
				repeatView.setText("월말");
				break;
			case AppManager.MONTH_REPEAT: 
				repeatView.setText("매월");
				break;
			case AppManager.YEAR_REPEAT: 
				repeatView.setText("매년");
				break;
			}
			if(sList.get(0).getMemo().equals("null"))
			{
				memoView.setText("메모없음");
			}
			else
			{
				memoView.setText(sList.get(0).getMemo());
			}
			
			return cv;
		}
	}
	

	class ModifyAdapter extends BaseAdapter
	{
		Context maincon;
		LayoutInflater inflater;
		ArrayList<Schedule> sList;
		int layout;
		public ModifyAdapter(Context context,int layout,ArrayList<Schedule> sList)
		{
			this.maincon=context;
			this.layout=layout;
			this.sList=sList;
			this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			return sList.size();
		}

		@Override
		public Object getItem(int positon) {
			return sList.get(positon);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View cv, ViewGroup parent) {
			if(cv==null)
			{
				cv = this.inflater.inflate(layout, parent,false);
			}

			final TextView m_Edit_TitleView;
			final TextView m_startTimeView;
			final TextView m_Edit_memoView;
			final RadioGroup m_publicGroup;
			final RadioGroup m_openGroup;
			final Spinner m_repeatSpinner;
			Button comfirmButton;
			
			m_Edit_TitleView = (TextView)cv.findViewById(R.id.m_edit_title);
			m_startTimeView = (TextView)cv.findViewById(R.id.m_start_time);
			m_publicGroup = (RadioGroup)cv.findViewById(R.id.m_publicgroup);
			m_openGroup = (RadioGroup)cv.findViewById(R.id.m_opengroup);
			m_repeatSpinner = (Spinner)cv.findViewById(R.id.m_repeat);
			m_Edit_memoView = (TextView)cv.findViewById(R.id.m_edit_memo);
			comfirmButton = (Button)cv.findViewById(R.id.m_button_ok);
			
			m_Edit_TitleView.setText(sList.get(position).getTitle());

			StringBuilder str1 = new StringBuilder();
			str1.append(sList.get(position).getStartTime().get(Calendar.AM_PM) == 0 ? "오전" : "오후");
			str1.append(" ");
			str1.append(sList.get(position).getStartTime().get(Calendar.HOUR));
			str1.append("시 ");
			str1.append(sList.get(position).getStartTime().get(Calendar.MINUTE));
			str1.append("분 ");		
			m_startTimeView.setText(str1.toString());

			LinearLayout titleLinearLayout = (LinearLayout)cv.findViewById(R.id.m_title);
			LinearLayout memoLinearLayout = (LinearLayout)cv.findViewById(R.id.m_memo);
			
			titleLinearLayout.setOnClickListener(new OnClickListener() {			
				EditText titleEditText;
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub	
					final LinearLayout linear = (LinearLayout)View.inflate(DetailViewActivity.this, R.layout.edit_text, null);
					
					titleEditText = (EditText)linear.findViewById(R.id.edit_text);
					titleEditText.setText(m_Edit_TitleView.getText());	
					
					new AlertDialog.Builder(DetailViewActivity.this)
					.setTitle("제목")
					.setView(linear)
					.setPositiveButton("확인", new DialogInterface.OnClickListener()
					{
					public void onClick(DialogInterface arg0, int arg1)
					{
						sList.get(0).setTitle(titleEditText.getText().toString());
						InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);   
						imm.hideSoftInputFromWindow(titleEditText.getWindowToken(),0); 
					}
					})
					.setNegativeButton("취소", null)
					.show();
				}
			});
			
			if(sList.get(0).getMemo().equals("null"))
			{
				m_Edit_memoView.setText("");
			}
			else
			{
				m_Edit_memoView.setText(sList.get(0).getMemo());
			}
			
			memoLinearLayout.setOnClickListener(new OnClickListener() {			
				EditText memoEditText;
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub	
					final LinearLayout linear = (LinearLayout)View.inflate(DetailViewActivity.this, R.layout.edit_text, null);
					
					memoEditText = (EditText)linear.findViewById(R.id.edit_text);
					memoEditText.setText(m_Edit_memoView.getText());	
					
					new AlertDialog.Builder(DetailViewActivity.this)
					.setTitle("메모")
					.setView(linear)
					.setPositiveButton("확인", new DialogInterface.OnClickListener()
					{
					public void onClick(DialogInterface arg0, int arg1)
					{
						sList.get(0).setMemo(memoEditText.getText().toString());
						InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);   
						imm.hideSoftInputFromWindow(memoEditText.getWindowToken(),0); 
					}
					})
					.setNegativeButton("취소", null)
					.show();
				}
			});

			LinearLayout startLinearLayout = (LinearLayout)cv.findViewById(R.id.m_start);
			startLinearLayout.setOnClickListener(new View.OnClickListener() {
				private int getCurrentHour( TimePicker timePicker ) {
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
					final LinearLayout linear = (LinearLayout)View.inflate(DetailViewActivity.this, R.layout.schedule_time_select, null);
					final TimePicker timePicker;
					timePicker = (TimePicker)linear.findViewById(R.id.modifytimePicker);
					
					timePicker.setCurrentHour(sList.get(0).getStartTime().get(Calendar.HOUR_OF_DAY));
					timePicker.setCurrentMinute(sList.get(0).getStartTime().get(Calendar.MINUTE));
					
					new AlertDialog.Builder(DetailViewActivity.this)
					.setTitle("시간설정")
					.setIcon(R.drawable.time_choice)
					.setView(linear)
					.setPositiveButton("확인", new DialogInterface.OnClickListener()
					{
					public void onClick(DialogInterface arg0, int arg1)
					{
						sList.get(0).getStartTime().set(Calendar.HOUR_OF_DAY, getCurrentHour(timePicker));
						sList.get(0).getStartTime().set(Calendar.MINUTE, getCurrentMinute(timePicker));
						String str = sList.get(0).getStartTime().get(Calendar.AM_PM) == 0 ? "오전" : "오후";
						m_startTimeView.setText(str+" "+sList.get(0).getStartTime().get(Calendar.HOUR)+"시 "+sList.get(0).getStartTime().get(Calendar.MINUTE)+"분");
					}
					})
					.setNegativeButton("취소", null)
					.show();
				}
			});

			if((sList.get(0).isPublic()? "공개":"비공개").equals("공개"))
			{
				m_publicGroup.check(R.id.m_publicradio);
			}
			else
			{
				m_publicGroup.check(R.id.m_privateradio);
			}
			if((sList.get(0).isOpen()? "가능":"불가능").equals("가능"))
			{
				m_openGroup.check(R.id.m_open);
			}
			else
			{
				m_openGroup.check(R.id.m_close);
			}
			
			switch(sList.get(0).getType()){
			case AppManager.GENERAL: 
				m_repeatSpinner.setSelection(AppManager.GENERAL-1);
				break;
			case AppManager.WEEK_REPEAT: 
				m_repeatSpinner.setSelection(AppManager.WEEK_REPEAT-1);
				break;
			case AppManager.LASTDAY_REPEAT: 
				m_repeatSpinner.setSelection(AppManager.LASTDAY_REPEAT-1);
				break;
			case AppManager.MONTH_REPEAT: 
				m_repeatSpinner.setSelection(AppManager.MONTH_REPEAT-1);
				break;
			case AppManager.YEAR_REPEAT: 
				m_repeatSpinner.setSelection(AppManager.YEAR_REPEAT-1);
				break;
			}
			
			m_publicGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub
					switch(checkedId){
					case R.id.m_publicradio:
						m_openGroup.setVisibility(View.VISIBLE);
						break;
					case R.id.m_privateradio:
						m_openGroup.check(R.id.m_close);
						m_openGroup.setVisibility(View.INVISIBLE);
						break;
					}
				}
			});
			
			
			comfirmButton.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View paramAnonymousView)
				{
					if (m_Edit_TitleView.getText().toString().equals(""))
			        {
					   showDialog(AppManager.POPUP_N);
			           return;
			        }
					if(startdate.compareTo(sList.get(0).getStartTime())>0&&sList.get(0).getType()==1)
					{
						SystemAlarmManager.cancelAlarm(DetailViewActivity.this, scheduleNo);
						AppManager.getInstance().getAm().cancel(scheduleNo);
						ExcuteDB.deleteAlarm(DetailViewActivity.this, scheduleNo);						
					}
					new AlertDialog.Builder(DetailViewActivity.this)
					.setTitle("일정수정")
					.setMessage("정말로 일정을 수정하시겠습니까?")
					.setIcon(R.drawable.btn_exit)
					.setPositiveButton("확인", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface arg0, int arg1)
						{
							String result = modifySchedule();
								
							if(result.equals(AppManager.COMPLETION))
							{
								ExcuteDB.updateSchedule(DetailViewActivity.this, scheduleNo, sList.get(0).getTitle(), sList.get(0).getStartTime(), sList.get(0).getType(), sList.get(0).isOpen(), sList.get(0).isPublic(), sList.get(0).getMemo());
								AppManager.getInstance().getSm().modify(scheduleNo, sList.get(0).getTitle(), sList.get(0).getMemo(), sList.get(0).getStartTime(), sList.get(0).getType(), sList.get(0).isOpen(), sList.get(0).isPublic());
								
								if(!(AppManager.getInstance().getAm().search(scheduleNo)==null))
								{
									SystemAlarmManager.cancelAlarm(DetailViewActivity.this, scheduleNo);
									SystemAlarmManager.typeenrollAlarm(DetailViewActivity.this, sList.get(0).getTitle(), sList.get(0).getStartTime(), sList.get(0).getType(), scheduleNo, AppManager.getInstance().getAm().search(scheduleNo).getAlarmtime());
								}
								
								DetailViewActivity.this.setResult(-1, getIntent());
								DetailViewActivity.this.finish();
							}
							else if(result.equals(AppManager.HTTP_ERROR)){
								showDialog(AppManager.POPUP_R);
							}				      
							else
							{
								showDialog(AppManager.POPUP_F);
							}					
						}
					})
					.setNegativeButton("취소", null)
					.show();
				}
				private String modifySchedule()
				{					
					RadioButton bPublic = (RadioButton) findViewById(m_publicGroup.getCheckedRadioButtonId());
					if(bPublic.getText().equals("공개"))
					{
						sList.get(0).setPublic(true);
					}
					else
					{
						sList.get(0).setPublic(false);
					}
					
					RadioButton bOpen = (RadioButton) findViewById(m_openGroup.getCheckedRadioButtonId());
					if(bOpen.getText().equals("가능"))
					{
						sList.get(0).setOpen(true);
					}
					else
					{
						sList.get(0).setOpen(false);
					}
					
					
					String str = m_repeatSpinner.getSelectedItem().toString();
					if(str.equals("한번"))
					{
						sList.get(0).setType(AppManager.GENERAL);
					}
					else if(str.equals("매주"))
					{
						sList.get(0).setType(AppManager.WEEK_REPEAT);
					}
					else if(str.equals("월말"))
					{
						sList.get(0).getStartTime().set(Calendar.DATE, sList.get(0).getStartTime().getActualMaximum(Calendar.DAY_OF_MONTH));
						sList.get(0).setType(AppManager.LASTDAY_REPEAT);
					}
					else if(str.equals("매달"))
					{
						sList.get(0).setType(AppManager.MONTH_REPEAT);
					}
					else
					{
						sList.get(0).setType(AppManager.YEAR_REPEAT);
					}
					
					return ScheduleSetting.modify(scheduleNo, sList.get(0).getTitle(), sList.get(0).getMemo(), sList.get(0).getType(), sList.get(0).isOpen(), sList.get(0).isPublic(), sList.get(0).getStartTime());
				}
			});
			return cv;
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
				
				final LinearLayout linear = (LinearLayout)View.inflate(DetailViewActivity.this, R.layout.help_detail_view, null);
				
				new AlertDialog.Builder(DetailViewActivity.this)
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
