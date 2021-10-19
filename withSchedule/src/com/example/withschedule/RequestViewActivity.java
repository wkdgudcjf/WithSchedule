package com.example.withschedule;

import java.util.ArrayList;
import com.example.withschedule.db.ExcuteDB;
import com.example.withschedule.dto.FriendInfo;
import com.example.withschedule.dto.RequestInfo;
import com.example.withschedule.friend.Friend;
import com.example.withschedule.socket.MemberSetting;
import com.example.withschedule.socket.ScheduleSetting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class RequestViewActivity extends Activity{
	private ImageView btnFriendIn;
	private ImageView btnScheduleIn;	
	private View friendRequestView,scheduleRequestView;		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.request_view);	
				
		btnFriendIn=(ImageView)findViewById(R.id.btn_friend_in);
		btnScheduleIn=(ImageView)findViewById(R.id.btn_schedule_in);
		btnFriendIn.setAlpha(1000);
		btnScheduleIn.setAlpha(300);
				
		
		ArrayList<FriendInfo> flist =  new ArrayList<FriendInfo>();
		SharedPreferences pref = getSharedPreferences(
				"withschedule", 0);
		String email = pref.getString("email", "");
		flist = MemberSetting.friendRequest(email);
		FriendRequestListAdapter cl;
		if(flist==null)
		{ 
			;
		}
		else
		{
			cl = new FriendRequestListAdapter(RequestViewActivity.this,R.layout.entry_row,flist);
			
			ListView commentl;
			commentl = (ListView)findViewById(R.id.friend_request_list);
			commentl.setAdapter(cl);
		}
		
		
		friendRequestView=findViewById(R.id.friend_request_page);
		scheduleRequestView=findViewById(R.id.schedule_request_page);
		
		friendRequestView.setVisibility(View.VISIBLE);
		scheduleRequestView.setVisibility(View.INVISIBLE);	
						
		OnClickListener mClickListener = new OnClickListener() {			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				friendRequestView.setVisibility(View.INVISIBLE);
				//mPage2.setVisibility(View.INVISIBLE);
				//mPage3.setVisibility(View.INVISIBLE);
				scheduleRequestView.setVisibility(View.INVISIBLE);
				
				switch(view.getId()){
					case R.id.btn_friend_in:
						if(!AppManager.getInstance().isPossibleInternet()){
							showDialog(AppManager.POPUP_J);
							friendRequestView.setVisibility(View.VISIBLE);
							break;
						}
						btnFriendIn.setImageResource(R.drawable.request_friend);
						btnScheduleIn.setImageResource(R.drawable.request_schedule2);
						btnFriendIn.setAlpha(1000);
						btnScheduleIn.setAlpha(300);
						
						ArrayList<FriendInfo> flist =  new ArrayList<FriendInfo>();
						SharedPreferences pref = getSharedPreferences(
								"withschedule", 0);
						String email = pref.getString("email", "");
						flist = MemberSetting.friendRequest(email);
						FriendRequestListAdapter cl;
						if(flist==null)
						{ 
							Log.d("kk", "널됫당");
						}
						else
						{
							cl = new FriendRequestListAdapter(RequestViewActivity.this,R.layout.entry_row,flist);
							
							ListView commentl;
							commentl = (ListView)findViewById(R.id.friend_request_list);
							commentl.setAdapter(cl);
						}
						
						friendRequestView.setVisibility(View.VISIBLE);
						break;
					
					case R.id.btn_schedule_in:
						if(!AppManager.getInstance().isPossibleInternet()){
							showDialog(AppManager.POPUP_J);
							friendRequestView.setVisibility(View.VISIBLE);
							break;
						}
						btnFriendIn.setImageResource(R.drawable.request_friend2);
						btnScheduleIn.setImageResource(R.drawable.request_schedule);
						btnFriendIn.setAlpha(300);
						btnScheduleIn.setAlpha(1000);
						
						final ArrayList<RequestInfo> rlist = ScheduleSetting.requestCheck(RequestViewActivity.this);
						
						if(rlist==null)
						{ 
							//Error 확인(dialog)필요?
						}
						else
						{
							RequestListAdapter requestListAdapter = new RequestListAdapter(RequestViewActivity.this,R.layout.request_schedule_enter_row,R.layout.request_schedule_accept_row,rlist);							
							ListView commentl;
							commentl = (ListView)findViewById(R.id.schedule_request_list);
							commentl.setAdapter(requestListAdapter);
						}
						scheduleRequestView.setVisibility(View.VISIBLE);
						break;
					
				}
			}
		};
		
		btnFriendIn.setOnClickListener(mClickListener);
		btnScheduleIn.setOnClickListener(mClickListener);		
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
	
	
	
	class RequestListAdapter extends BaseAdapter
	{
		Context maincon;
		LayoutInflater inflater;
		ArrayList<RequestInfo> ci;
		int layout;
		int layout2;
		public RequestListAdapter(Context context,int layout,int layout2,ArrayList<RequestInfo> ci)
		{
			this.maincon=context;
			this.layout=layout;
			this.layout2=layout2;
			this.ci=ci;
			this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			return ci.size();
		}
		public void setClist(ArrayList<RequestInfo> clist)
		{
			this.ci=clist;
		}
		@Override
		public RequestInfo getItem(int positon) {
			return ci.get(positon);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View cv, ViewGroup parent) 
		{			
			SharedPreferences pref = getSharedPreferences("withschedule", 0);
			final String myEmail = pref.getString("email", "");
			
			final RequestInfo ri = ci.get(position);
			
			switch(ri.getType()){
			case 1:				
				if(ri.getReceiverEmail().equals(myEmail)){
					if(cv==null)
					{				
						cv = this.inflater.inflate(layout2, parent,false);
					}
					
					final TextView where = (TextView)cv.findViewById(R.id.where);
					final TextView who = (TextView)cv.findViewById(R.id.who);
					final ImageView btn = (ImageView)cv.findViewById(R.id.btn_choice);
					final ImageView btn2 = (ImageView)cv.findViewById(R.id.btn_choice2);
					
					who.setText(ri.getSenderName()+"("+ri.getSenderEmail()+")"+"님이 ");
					where.setText("["+ri.getTitle()+"]방에 참가를 원함.");
					btn.setBackgroundResource(R.drawable.btn_accept_schedule);
					btn.setOnTouchListener(new OnTouchListener()
					{
						public boolean onTouch(View view, MotionEvent e)
						{
							if (e.getAction() == MotionEvent.ACTION_DOWN)
							{
								btn.setAlpha(80);
							} 
							else if (e.getAction() == MotionEvent.ACTION_UP)
							{
								if ((int) e.getX() < 0
											|| (int) e.getX() > btn
													.getWidth()
											|| (int) e.getY() < 0
											|| (int) e.getY() > btn
													.getHeight()) 
								{
									btn.setAlpha(1000);
								}
								else
								{
									if(!AppManager.getInstance().isPossibleInternet()){
										showDialog(AppManager.POPUP_J);
										return true;
									}
									btn.setAlpha(1000);	
									
									new AlertDialog.Builder(RequestViewActivity.this)
									.setTitle("참가승인")
									.setMessage("정말로 참가를 승인하시겠습니까?")
									.setIcon(R.drawable.btn_accept_schedule)
									.setPositiveButton("확인", new DialogInterface.OnClickListener()
									{
										public void onClick(DialogInterface arg0, int arg1)
										{
											String result = ScheduleSetting.acceptEnterRequest(ri.getNo(),ri.getSenderEmail());
											
											if(result.equals(AppManager.COMPLETION))
											{
												who.setText(ri.getSenderName()+"("+ri.getSenderEmail()+")"+"님에게 ");
												where.setText("["+ri.getTitle()+"]방  참가승인 완료!");
												btn.setVisibility(View.INVISIBLE);
												btn2.setVisibility(View.INVISIBLE);
											}
											else if(result.equals(AppManager.NO_SEARCH)){
												showDialog(AppManager.POPUP_G);
											}
											else if(result.equals(AppManager.HTTP_ERROR)){
												showDialog(AppManager.POPUP_R);
											}
											else{
												showDialog(AppManager.POPUP_F);
											}
										}
									})
									.setNegativeButton("취소", null)
									.show();
								}
							}
							else
							{
								btn.setAlpha(1000);
							}
							return true;
						}
					});
					btn2.setOnTouchListener(new OnTouchListener()
					{
						public boolean onTouch(View view, MotionEvent e)
						{
							if (e.getAction() == MotionEvent.ACTION_DOWN)
							{
								btn.setAlpha(80);
							} 
							else if (e.getAction() == MotionEvent.ACTION_UP)
							{
								if ((int) e.getX() < 0
											|| (int) e.getX() > btn
													.getWidth()
											|| (int) e.getY() < 0
											|| (int) e.getY() > btn
													.getHeight()) 
								{
									btn.setAlpha(1000);
								}
								else
								{
									if(!AppManager.getInstance().isPossibleInternet()){
										showDialog(AppManager.POPUP_J);
										return true;
									}
									btn.setAlpha(1000);	
									
									new AlertDialog.Builder(RequestViewActivity.this)
									.setTitle("참가요청거부")
									.setMessage("정말로 참가요청을 거부하시겠습니까?")
									.setIcon(R.drawable.btn_accept_schedule)
									.setPositiveButton("확인", new DialogInterface.OnClickListener()
									{
										public void onClick(DialogInterface arg0, int arg1)
										{
											String result = ScheduleSetting.rejectEnterRequest(ri.getNo(),ri.getSenderEmail());
											//String result=AppManager.COMPLETION;
											if(result.equals(AppManager.COMPLETION))
											{
												who.setText(ri.getSenderName()+"("+ri.getSenderEmail()+")"+"님에게 ");
												where.setText("["+ri.getTitle()+"]방  참가요청 거부!");
												btn.setVisibility(View.INVISIBLE);
												btn2.setVisibility(View.INVISIBLE);
											}
											else if(result.equals(AppManager.NO_SEARCH)){
												showDialog(AppManager.POPUP_G);
											}
											else if(result.equals(AppManager.HTTP_ERROR)){
												showDialog(AppManager.POPUP_R);
											}
											else{
												showDialog(AppManager.POPUP_F);
											}
										}
									})
									.setNegativeButton("취소", null)
									.show();
								}
							}
							else
							{
								btn.setAlpha(1000);
							}
							return true;
						}
					});
				}else{
					if(cv==null)
					{				
						cv = this.inflater.inflate(layout, parent,false);
					}
					
					final TextView where = (TextView)cv.findViewById(R.id.where);
					final TextView who = (TextView)cv.findViewById(R.id.who);
					final ImageView btn = (ImageView)cv.findViewById(R.id.btn_choice);
					
					who.setText(ri.getReceiverName()+"("+ri.getReceiverEmail()+")"+"님에게 ");
					where.setText("["+ri.getTitle()+"]방  참가신청 중...");
					btn.setBackgroundResource(R.drawable.btn_exit);
					btn.setOnTouchListener(new OnTouchListener()
					{
						public boolean onTouch(View view, MotionEvent e)
						{
							if (e.getAction() == MotionEvent.ACTION_DOWN)
							{
								btn.setAlpha(80);
							} 
							else if (e.getAction() == MotionEvent.ACTION_UP)
							{
								if ((int) e.getX() < 0
											|| (int) e.getX() > btn
													.getWidth()
											|| (int) e.getY() < 0
											|| (int) e.getY() > btn
													.getHeight()) 
								{
									btn.setAlpha(1000);
								}
								else
								{
									if(!AppManager.getInstance().isPossibleInternet()){
										showDialog(AppManager.POPUP_J);
										return true;
									}
									btn.setAlpha(1000);	
									
									new AlertDialog.Builder(RequestViewActivity.this)
									.setTitle("참가취소")
									.setMessage("정말로 참가요청을 취소하시겠습니까?")
									.setIcon(R.drawable.btn_exit)
									.setPositiveButton("확인", new DialogInterface.OnClickListener()
									{
										public void onClick(DialogInterface arg0, int arg1)
										{
											String result = ScheduleSetting.cancleEnterRequest(ri.getNo(),ri.getSenderEmail(),ri.getReceiverEmail());
											
											if(result.equals(AppManager.COMPLETION))
											{
												who.setText(ri.getReceiverName()+"("+ri.getReceiverEmail()+")"+"님에게 ");
												where.setText("["+ri.getTitle()+"]방  참가신청 취소됩");
												btn.setVisibility(View.INVISIBLE);
											}
											else if(result.equals(AppManager.NO_SEARCH)){
												showDialog(AppManager.POPUP_G);
											}
											else if(result.equals(AppManager.HTTP_ERROR)){
												showDialog(AppManager.POPUP_R);
											}
											else{
												showDialog(AppManager.POPUP_F);
											}
										}
									})
									.setNegativeButton("취소", null)
									.show();
								}
							}
							else
							{
								btn.setAlpha(1000);
							}
							return true;
						}
					});
				}
				
				break;
			//초대
			case 2: 
				break;
			}
			
		return cv;
		}
	}
	
	class FriendRequestListAdapter extends BaseAdapter
	{
		Context maincon;
		LayoutInflater inflater;
		ArrayList<FriendInfo> ci;
		int layout;
		int layout2;
		public FriendRequestListAdapter(Context context,int layout,ArrayList<FriendInfo> ci)
		{
			this.maincon=context;
			this.layout=layout;
			this.ci=ci;
			this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			return ci.size();
		}
		public void setClist(ArrayList<FriendInfo> clist)
		{
			this.ci=clist;
		}
		@Override
		public FriendInfo getItem(int positon) {
			return ci.get(positon);
		}

		@Override
		public long getItemId(int position) {
			return position;
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
			
			TextView name = (TextView)cv.findViewById(R.id.entry_name);
			TextView email = (TextView)cv.findViewById(R.id.entry_email);
			final ImageView btnAddFriend = (ImageView)cv.findViewById(R.id.btn_add_friend);
				
			if(AppManager.getInstance().getFm().search(ci.get(position).getEmail())!=null||myEmail.equals(ci.get(position).getEmail()))
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
						final LinearLayout linear = (LinearLayout)View.inflate(RequestViewActivity.this, R.layout.friend_add_check, null);
						new AlertDialog.Builder(RequestViewActivity.this)
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
								String check = MemberSetting.addFriend(RequestViewActivity.this,myEmail,ci.get(pos).getEmail());
								if(check.equals(AppManager.COMPLETION))
								{
									Friend friend = new Friend(ci.get(pos).getName(),ci.get(pos).getEmail(),"x");
									ExcuteDB.insertFriend(RequestViewActivity.this,friend);
									AppManager.getInstance().getFm().enroll(friend);
									btnAddFriend.setVisibility(View.INVISIBLE);
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
			name.setText(ci.get(position).getName()+"님이 친구가 되고싶어합니다.");
			email.setText(ci.get(position).getEmail());
			
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
	    
	    final LinearLayout linear = (LinearLayout)View.inflate(RequestViewActivity.this, R.layout.help_request_view, null);
	    
	    new AlertDialog.Builder(RequestViewActivity.this)
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
