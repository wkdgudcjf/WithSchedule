package com.example.withschedule;

import java.util.*;

import com.example.withschedule.friend.Friend;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Intent;

import android.net.Uri;
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

import android.widget.CheckBox;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class FriendListActivity extends Activity
{
	ArrayList<Friend> FriendList = AppManager.getInstance().getFm().getFriendList().getList();
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends_list_view);
		
		ImageView btnCompare = (ImageView)findViewById(R.id.btn_comp);
		btnCompare.setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				LinearLayout FriendsInfoTable = (LinearLayout) findViewById(R.id.friend_table);
				ArrayList<String> emailList = new ArrayList<String>();
				for(int i=0;i<FriendsInfoTable.getChildCount();i++)
				{
					LinearLayout friendsInfoRow = (LinearLayout)FriendsInfoTable.getChildAt(i);
					CheckBox compareCheckBox = (CheckBox)friendsInfoRow.findViewById(R.id.comp_check_box);
					if(compareCheckBox.isChecked())
					{
						emailList.add(FriendList.get(i).getEmail());
					}
				}
				
				if(emailList.size()==0) return;
				
				Intent myIntent = new Intent(
						FriendListActivity.this,
						TimeScheduleActivity.class);
				myIntent.putStringArrayListExtra(
						"friendsEmail", emailList);
				
				startActivity(myIntent);
				
			}
		});
		
		EditText searchEditText = (EditText) findViewById(R.id.edit_search);
		
		searchEditText.addTextChangedListener(new TextWatcher()
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
			public void onTextChanged(CharSequence keyword, int start, int before,
					int count) {
				LinearLayout table = (LinearLayout) findViewById(R.id.friend_table);
				table.removeAllViews();
				String searchKeyword = keyword.toString();
				tableSetting(searchFriendList(searchKeyword));
			}
	    });		
		tableSetting(AppManager.getInstance().getFm().getFriendList().getList());
	}
	
	private List<Friend> searchFriendList(String searchKeyword){

		List<Friend> searchList = new ArrayList<Friend>();

		for(int i=0;i<FriendList.size();i++)
		{
			if(searchCheck(FriendList.get(i).getNickname(),searchKeyword))
			{
				searchList.add(FriendList.get(i));
			}
		}
		return searchList;
	}

	private boolean searchCheck(String name,String searchKeyword)
	{
		boolean isAdd = false;

		if (searchKeyword != null && "".equals(searchKeyword.trim()) == false) {

			String iniName = com.example.withschedule.util.HangulUtils.getHangulInitialSound(name,
					searchKeyword);

			if (iniName.indexOf(searchKeyword) >= 0) {
				isAdd = true;
			}
		} else {
			isAdd = true;
		}
		return isAdd;
	}
	private void tableSetting(List<Friend> list)
	{
		Collections.sort(list,new FriendComparator());		
		LinearLayout table = (LinearLayout) findViewById(R.id.friend_table);
		for (int i = 0; i < list.size()	; i++)
		{
			LinearLayout ll = (LinearLayout) LayoutInflater.from(FriendListActivity.this).inflate(R.layout.friendlist_row, null, false);
		
			final String email = list.get(i).getEmail();
			final String name = list.get(i).getNickname();
			
			final TextView nicknametext = (TextView)ll.findViewById(R.id.nickname);
			nicknametext.setText(name+"("+email+")");
			nicknametext.setBackgroundResource(R.drawable.boarder_clickable);
			nicknametext.setBackgroundResource(0);
			nicknametext
			.setOnTouchListener(new OnTouchListener() 
			{
				@Override
				public boolean onTouch(View view,
						MotionEvent e) {
					// TODO Auto-generated method stub
					if (e.getAction() == MotionEvent.ACTION_DOWN)
					{
						nicknametext.setBackgroundResource(R.drawable.boarder_clickable);
					} 
					else if (e.getAction() == MotionEvent.ACTION_UP)
					{
						if ((int) e.getX() < 0
								|| (int) e.getX() > nicknametext
										.getWidth()
								|| (int) e.getY() < 0
								|| (int) e.getY() > nicknametext
										.getHeight()) 
						{
							nicknametext.setBackgroundResource(0);
						}
						else
						{
							nicknametext.setBackgroundResource(0);
							Intent myIntent = new Intent(
									FriendListActivity.this,
									FriendCalendarActivity.class);
							myIntent.putExtra("email", email);
							myIntent.putExtra("name", name);
						
							FriendListActivity.this.startActivityForResult(myIntent, 2);
						}
					}
					else
					{
						nicknametext.setBackgroundResource(0);
					}
					return true;
				}
			});
			final String phoneNo = list.get(i).getPhoneNo();

			TextView phoneNumView = (TextView)ll.findViewById(R.id.phone_num);
			if(!(list.get(i).getPhoneNo().equals("x")))
			{
				phoneNumView.setText(list.get(i).getPhoneNo());
				phoneNumView.setOnClickListener(new View.OnClickListener() {
					public void onClick(View paramAnonymousView) 
					{
						//전화번호로 이동하는 인텐트 생성
						Intent callIntent = new Intent("android.intent.action.DIAL", Uri.parse("tel:" + phoneNo));
						FriendListActivity.this.startActivity(callIntent);
					}
				});
			}
			else
			{
				ll.removeView(phoneNumView);
			}			
			table.addView(ll);
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
	    
	    final LinearLayout linear = (LinearLayout)View.inflate(FriendListActivity.this, R.layout.help_friends_list_view, null);
	    
	    new AlertDialog.Builder(FriendListActivity.this)
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

