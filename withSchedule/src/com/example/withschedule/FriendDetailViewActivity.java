package com.example.withschedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.example.withschedule.db.ExcuteDB;
import com.example.withschedule.dto.CommentInfo;
import com.example.withschedule.dto.EntryInfo;
import com.example.withschedule.friend.Friend;
import com.example.withschedule.schedule.Schedule;
import com.example.withschedule.socket.MemberSetting;
import com.example.withschedule.socket.ScheduleSetting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendDetailViewActivity extends Activity{
	private ImageView btn_infoImageView;
	private ImageView btn_entryImageView;
	private ImageView btn_talkImageView;
	
	private View infoPage,commentPage,entryPage;
	private int scheduleNo;
	private Schedule schedule;
	private GregorianCalendar startdate;
	private CommentListAdapter cmtListAdapter;
	private int type=0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_detail_view);	
		
		btn_infoImageView=(ImageView)findViewById(R.id.btn_info);
		btn_entryImageView=(ImageView)findViewById(R.id.btn_entry);
		btn_talkImageView=(ImageView)findViewById(R.id.btn_talk);
		
		btn_infoImageView.setAlpha(1000);
		btn_entryImageView.setAlpha(300);
		btn_talkImageView.setAlpha(300);
		
				
		infoPage=findViewById(R.id.info_page);
		entryPage=findViewById(R.id.entry_page);
		commentPage=findViewById(R.id.comment_page);
		
		
		infoPage.setVisibility(View.VISIBLE);
		entryPage.setVisibility(View.INVISIBLE);	
		commentPage.setVisibility(View.INVISIBLE);
		
				
		scheduleNo  = getIntent().getIntExtra("schedule_no", -1);
		type  = getIntent().getIntExtra("type", -1);
		if(type==1){
			schedule = AppManager.getInstance().getSm().search(scheduleNo);
		}else{
			schedule = AppManager.getInstance().getSm().getFriendSchedule().search(scheduleNo);
		}		
		if(schedule==null){
			showDialog(AppManager.POPUP_I);
			finish();
		}
		
		ArrayList<Schedule> sList = new ArrayList<Schedule>();
		sList.add(schedule);
		InfoAdapter infoAdapter = new InfoAdapter(FriendDetailViewActivity.this,R.layout.schedule_detail_info_page,sList);
		
		ListView infoList;
		infoList = (ListView)findViewById(R.id.info_list);
		infoList.setAdapter(infoAdapter);
		OnClickListener mClickListener = new OnClickListener() {			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				infoPage.setVisibility(View.INVISIBLE);
				entryPage.setVisibility(View.INVISIBLE);
				commentPage.setVisibility(View.INVISIBLE);
				
				switch(view.getId()){
					case R.id.btn_info:
						if(!AppManager.getInstance().isPossibleInternet()){
							showDialog(AppManager.POPUP_J);
						}
						else{
							//서버에서 최신 일정정보 업데이트!!
						}

						if(type==1){
							schedule = AppManager.getInstance().getSm().search(scheduleNo);
						}else{
							schedule = AppManager.getInstance().getSm().getFriendSchedule().search(scheduleNo);
						}		
						if(schedule==null){
							showDialog(AppManager.POPUP_I);
							finish();
						}
						ArrayList<Schedule> sList = new ArrayList<Schedule>();
						sList.add(schedule);
						InfoAdapter infoAdapter = new InfoAdapter(FriendDetailViewActivity.this,R.layout.schedule_detail_info_page,sList);
						
						ListView infoList;
						infoList = (ListView)findViewById(R.id.info_list);
						infoList.setAdapter(infoAdapter);
						
						btn_infoImageView.setImageResource(R.drawable.friend_info);
						btn_entryImageView.setImageResource(R.drawable.friend_entry2);
						btn_talkImageView.setImageResource(R.drawable.friend_talk2);
						btn_infoImageView.setAlpha(1000);
						btn_entryImageView.setAlpha(300);
						btn_talkImageView.setAlpha(300);
						infoPage.setVisibility(View.VISIBLE);
						break;
					case R.id.btn_entry:
						if(!AppManager.getInstance().isPossibleInternet()){
							showDialog(AppManager.POPUP_J);
							infoPage.setVisibility(View.VISIBLE);
							break;
						}
						
						btn_infoImageView.setImageResource(R.drawable.friend_info2);
						btn_entryImageView.setImageResource(R.drawable.friend_entry);
						btn_talkImageView.setImageResource(R.drawable.friend_talk2);
						btn_infoImageView.setAlpha(300);
						btn_entryImageView.setAlpha(1000);
						btn_talkImageView.setAlpha(300);
						ArrayList<EntryInfo> eList =  new ArrayList<EntryInfo>();						
						eList = ScheduleSetting.entryCheck(scheduleNo);
						
						if(eList==null)
						{
							//Error 확인(dialog)필요??
						}
						else
						{
							EntryListAdapter entryListAdapter = new EntryListAdapter(FriendDetailViewActivity.this,R.layout.entry_row,eList);
							
							ListView entryList;
							entryList = (ListView)findViewById(R.id.entry_list);
							entryList.setAdapter(entryListAdapter);
						}
						entryPage.setVisibility(View.VISIBLE);
						break;
					case R.id.btn_talk:
						if(!AppManager.getInstance().isPossibleInternet())
						{
							showDialog(AppManager.POPUP_J);
							infoPage.setVisibility(View.VISIBLE);
							break;
						}
						btn_infoImageView.setImageResource(R.drawable.friend_info2);
						btn_entryImageView.setImageResource(R.drawable.friend_entry2);
						btn_talkImageView.setImageResource(R.drawable.friend_talk);
						btn_infoImageView.setAlpha(300);
						btn_entryImageView.setAlpha(300);
						btn_talkImageView.setAlpha(1000);
					
						final ArrayList<CommentInfo> cList = ScheduleSetting.commentCheck(scheduleNo);						
						if(cList==null)
						{ 
							//Error 확인(dialog)필요??
						}
						else
						{
							cmtListAdapter = new CommentListAdapter(FriendDetailViewActivity.this,R.layout.comment_row,cList);
							
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
						boolean k=false;
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
									k=true;
									break;
								}
							}
							if(k==false)
							{
								commentAlarmCheckBox.setVisibility(BIND_NOT_FOREGROUND);
							}
						}
						commentEnrollButton.setOnClickListener(new OnClickListener() {			
						@Override
							public void onClick(View arg0) {
								EditText commentText = (EditText)findViewById(R.id.enroll_comment);
								if(commentText.getText().toString().length()==0)
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
									
									String check = ScheduleSetting.enrollcomment(scheduleNo,commentText.getText().toString(),email,gc,gcm);
									
									if(check.equals(AppManager.COMPLETION))
									{
										final ArrayList<CommentInfo> cList = ScheduleSetting.commentCheck(scheduleNo);						
										if(cList==null){ 
											//Error 확인(dialog)필요??
										}
										else{
											cmtListAdapter.setClist(cList);
											cmtListAdapter.notifyDataSetChanged();
											commentText.setText("");											
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
				}
			}
		};
		
		btn_infoImageView.setOnClickListener(mClickListener);
		btn_entryImageView.setOnClickListener(mClickListener);
		btn_talkImageView.setOnClickListener(mClickListener);
		
		
		
	}
	
	protected ArrayList<EntryInfo> parseOfEntryInfo(String entryInfo) {
		// TODO Auto-generated method stub
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
	protected void onDestroy()
	{
		
		super.onDestroy();
//		Toast.makeText(getBaseContext(), "add onDestroy ...", Toast.LENGTH_LONG).show();
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
					public void onClick(View arg0) {
						final LinearLayout linear = (LinearLayout)View.inflate(FriendDetailViewActivity.this, R.layout.comment_remove_check, null);
					      new AlertDialog.Builder(FriendDetailViewActivity.this)
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

		@Override
		public View getView(int position, View cv, ViewGroup parent) 
		{
			if(cv==null)
			{
				cv = this.inflater.inflate(layout, parent,false);
			}

			SharedPreferences pref = getSharedPreferences("withschedule", 0);
			final String myEmail = pref.getString("email", "");
			
			TextView nameView = (TextView)cv.findViewById(R.id.entry_name);
			TextView emailView = (TextView)cv.findViewById(R.id.entry_email);
			final ImageView btnAddFriend = (ImageView)cv.findViewById(R.id.btn_add_friend);
			final int pos = position;
			if(AppManager.getInstance().getFm().search(eList.get(position).getEmail())!=null||myEmail.equals(eList.get(position).getEmail()))
			{
				btnAddFriend.setVisibility(View.INVISIBLE);
			}
			else
			{
				btnAddFriend.setVisibility(View.VISIBLE);
				btnAddFriend.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						final LinearLayout linear = (LinearLayout)View.inflate(FriendDetailViewActivity.this, R.layout.friend_add_check, null);
						new AlertDialog.Builder(FriendDetailViewActivity.this)
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
								String check = MemberSetting.addFriend(FriendDetailViewActivity.this,myEmail,eList.get(pos).getEmail());
								if(check.equals(AppManager.COMPLETION))
								{
									Friend friend = new Friend(eList.get(pos).getName(),eList.get(pos).getEmail(),"x");
									ExcuteDB.insertFriend(FriendDetailViewActivity.this,friend);
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
			
			startdate = (GregorianCalendar) sList.get(0).getStartTime();
			String str1 = startdate.get(Calendar.AM_PM) == 0 ? "오전" : "오후";
			StringBuilder str = new StringBuilder();
			str.append(startdate.get(Calendar.YEAR));
			str.append("년 ");
			str.append(startdate.get(Calendar.MONTH)+1);
			str.append("월 ");
			str.append(startdate.get(Calendar.DAY_OF_MONTH));
			str.append("일 ");
			str.append(str1);
			str.append(" ");
			str.append(startdate.get(Calendar.HOUR));
			str.append("시 ");
			str.append(startdate.get(Calendar.MINUTE));
			str.append("분 ");		
			startTimeView.setText(str.toString());
			
			
			isPublicView.setText(sList.get(0).isPublic()? "공개":"비공개");
			isOpenView.setText(sList.get(0).isOpen()? "개방":"비개방");
						
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
	@Override
	 public boolean onCreateOptionsMenu(Menu menu) {
	  // TODO Auto-generated method stub
	  MenuItem item1 = menu.add(0,5,0,"도움말").setIcon(R.drawable.imformation);
	  
	  //final LinearLayout linear = (LinearLayout)View.inflate(CalendarActivity.this, R.layout.help_calendar, null);
	  item1.setOnMenuItemClickListener(new OnMenuItemClickListener() {   
	   @Override
	   public boolean onMenuItemClick(MenuItem arg0) {
	    // TODO Auto-generated method stub
	    
	    final LinearLayout linear = (LinearLayout)View.inflate(FriendDetailViewActivity.this, R.layout.help_schedule_list_view, null);
	    
	    new AlertDialog.Builder(FriendDetailViewActivity.this)
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
