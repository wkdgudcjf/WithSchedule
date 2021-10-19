package com.example.withschedule;


import java.util.regex.Pattern;

import com.example.withschedule.socket.MemberSetting;


import android.os.Bundle;
import android.app.*;
import android.content.*;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class SettingActivity extends Activity {
	
	private TextView emailView;
	private TextView nameView;
	private TextView pwView;
	private TextView memberDeleteView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_setting_view);
		                                      
		emailView = (TextView) findViewById(R.id.setting_email);
		nameView = (TextView) findViewById(R.id.setting_name);
		pwView = (TextView) findViewById(R.id.setting_password);
		memberDeleteView = (TextView) findViewById(R.id.remove_member);
		
		SharedPreferences pref = getSharedPreferences(
				"withschedule", 0);
		final String email = pref.getString("email", "");
		final String nickname = pref.getString("name", "");
		
		emailView.setText(email);
		nameView.setText(nickname);
		//비밀번호
		final InputFilter filterAlpha2 = new InputFilter(){
			public CharSequence filter(CharSequence source, int start, int end, android.text.Spanned dest, int dstart, int dend){
				Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
				if(!ps.matcher(source).matches()){
					return"";
				}
				return null;
			}
		};
		//이메일
		final InputFilter filterAlpha = new InputFilter(){
			public CharSequence filter(CharSequence source, int start, int end, android.text.Spanned dest, int dstart, int dend){
				Pattern ps = Pattern.compile("^[a-zA-Z0-9@.]+$");
				if(!ps.matcher(source).matches()){
					return"";
				}
				return null;
			}
		};

		pwView.setOnClickListener(new OnClickListener() {			
			EditText dp;
			EditText dp2;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub	
				final LinearLayout linear = (LinearLayout)View.inflate(SettingActivity.this, R.layout.member_setting_password, null);
				
			    dp = (EditText)linear.findViewById(R.id.edit_pw);
			    dp2 = (EditText)linear.findViewById(R.id.edit_newpw);				
				dp.setHint("이전 비밀번호를 입력하세요");
				dp2.setHint("새로운 비밀번호를 입력하세요(6~12숫자)");
				dp.setFilters(new InputFilter[]{filterAlpha2,new InputFilter.LengthFilter(12)});
				dp2.setFilters(new InputFilter[]{filterAlpha2,new InputFilter.LengthFilter(12)});
				new AlertDialog.Builder(SettingActivity.this)
				.setTitle("암호변경")
				.setView(linear)
				.setPositiveButton("확인", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{
						SharedPreferences pref = getSharedPreferences(
								"withschedule", 0);
						String pw = pref.getString("password", "");
						if(!AppManager.getInstance().isPossibleInternet()){
							showDialog(AppManager.POPUP_J);
						}
						else
						{
							if(dp.getText().toString().equals(pw))
							{
								if(!(dp2.getText().toString().length()<5||dp2.getText().toString().length()>12))
								{
									String check = MemberSetting.memberModify(SettingActivity.this,emailView.getText().toString(),emailView.getText().toString(),nameView.getText().toString(),dp2.getText().toString());
									if(check.equals(AppManager.COMPLETION))
									{
										SharedPreferences.Editor edit = pref.edit();
										edit.putString("password", dp2.getText().toString());
										edit.commit();//저장 시작
										InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);   
										imm.hideSoftInputFromWindow(dp2.getWindowToken(),0); 
									}
									else if(check.equals(AppManager.NO_SEARCH)){
										showDialog(AppManager.POPUP_G);
									}
									else if(check.equals(AppManager.HTTP_ERROR)){
										showDialog(AppManager.POPUP_R);
									}
									else if(check.equals(AppManager.DOUBLE_EMAIL)){
										showDialog(AppManager.POPUP_E);
									}
									else
									{
										showDialog(AppManager.POPUP_F);
									}
								}
								else
								{
									showDialog(AppManager.POPUP_D);
								}
							}
							else
							{
								showDialog(AppManager.POPUP_L);
							}
						}
					}
				})
				.setNegativeButton("취소", null)
				.show();
			}
		});
		
		emailView.setOnClickListener(new OnClickListener() {			
			
			EditText dp;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub	
				final LinearLayout linear = (LinearLayout)View.inflate(SettingActivity.this, R.layout.member_setting_emaill, null);
				
			    dp = (EditText)linear.findViewById(R.id.edit_email);
								
				dp.setText(emailView.getText());
				dp.setFilters(new InputFilter[]{filterAlpha});
				
				new AlertDialog.Builder(SettingActivity.this)
				.setTitle("이메일변경")
				.setView(linear)
				.setPositiveButton("확인", new DialogInterface.OnClickListener()
				{
				public void onClick(DialogInterface arg0, int arg1)
				{
					if(!AppManager.getInstance().isPossibleInternet()){
						showDialog(AppManager.POPUP_J);
					}
					else
					{
						if(!dp.getText().toString().contains("@")||!dp.getText().toString().contains(".")
						||dp.getText().toString().lastIndexOf("@")!=dp.getText().toString().indexOf("@")
						||dp.getText().toString().lastIndexOf(".")!=dp.getText().toString().indexOf(".")
						||dp.getText().toString().lastIndexOf("@")>=dp.getText().toString().indexOf("."))
						{
							showDialog(AppManager.POPUP_B);
						}
						else
						{
							SharedPreferences pref = getSharedPreferences(
									"withschedule", 0);
							String pw = pref.getString("password", "");
							String check = MemberSetting.memberModify(SettingActivity.this,emailView.getText().toString(), dp.getText().toString(), nameView.getText().toString(), pw);
							if(check.equals(AppManager.COMPLETION))
							{
								SharedPreferences.Editor edit = pref.edit();
								edit.putString("email", dp.getText().toString());
							    edit.commit();//저장 시작
							    emailView.setText(dp.getText());
								InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);   
								imm.hideSoftInputFromWindow(dp.getWindowToken(),0); 
							}
							else if(check.equals(AppManager.NO_SEARCH)){
								showDialog(AppManager.POPUP_G);
							}
							else if(check.equals(AppManager.HTTP_ERROR)){
								showDialog(AppManager.POPUP_R);
							}
							else if(check.equals(AppManager.DOUBLE_EMAIL)){
								showDialog(AppManager.POPUP_E);
							}
							else
							{
								showDialog(AppManager.POPUP_F);
							}
						}
					}						
				}
				})
				.setNegativeButton("취소", null)
				.show();
			}
		});
		
		nameView.setOnClickListener(new OnClickListener() {			
			EditText dp;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub	
				final LinearLayout linear = (LinearLayout)View.inflate(SettingActivity.this, R.layout.member_setting_name, null);
				
			    dp = (EditText)linear.findViewById(R.id.edit_name);
								
				dp.setText(nameView.getText());
				
				new AlertDialog.Builder(SettingActivity.this)
				.setTitle("닉네임변경")
				.setView(linear)
				.setPositiveButton("확인", new DialogInterface.OnClickListener()
				{
				public void onClick(DialogInterface arg0, int arg1)
				{
					if(!AppManager.getInstance().isPossibleInternet()){
						showDialog(AppManager.POPUP_J);
					}
					else
					{
						if(dp.getText().toString().length()==0)
						{
							showDialog(AppManager.POPUP_C);
						}
						else
						{
							SharedPreferences pref = getSharedPreferences(
									"withschedule", 0);
							String pw = pref.getString("password", "");
							String check = MemberSetting.memberModify(SettingActivity.this,emailView.getText().toString(),emailView.getText().toString() , dp.getText().toString(), pw);
							if(check.equals(AppManager.COMPLETION)){
								SharedPreferences.Editor edit = pref.edit();
								edit.putString("name", dp.getText().toString());
							    edit.commit();//저장 시작
							    nameView.setText(dp.getText());
							    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);   
								imm.hideSoftInputFromWindow(dp.getWindowToken(),0); 
							}
						    else if(check.equals(AppManager.NO_SEARCH)){
								showDialog(AppManager.POPUP_G);
							}
							else if(check.equals(AppManager.HTTP_ERROR)){
								showDialog(AppManager.POPUP_R);
							}
							else if(check.equals(AppManager.DOUBLE_EMAIL)){
								showDialog(AppManager.POPUP_E);
							}
							else
							{
								showDialog(AppManager.POPUP_F);
							}
						}
					}
				}
				})
				.setNegativeButton("취소", null)
				.show();
			}
		});
		

		memberDeleteView.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub	
				final LinearLayout linear = (LinearLayout)View.inflate(SettingActivity.this, R.layout.member_delete_check, null);
				new AlertDialog.Builder(SettingActivity.this)
				.setTitle("회원탈퇴")
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
						String check = MemberSetting.remove(SettingActivity.this,emailView.getText().toString());
						
						if(check.equals(AppManager.COMPLETION))
						{
							Toast.makeText(getBaseContext(), "저희 어플리케이션을 이용해주셔서 감사힙니다.", Toast.LENGTH_LONG).show();
							Intent intent = new Intent();
							intent.setAction(Intent.ACTION_MAIN);
							intent.addCategory(Intent.CATEGORY_HOME);
							startActivity(intent);
							android.os.Process.killProcess(android.os.Process.myPid());
						}
						else if(check.equals(AppManager.NO_SEARCH))
						{
							Toast.makeText(getBaseContext(), "삭제도중 확인되지 않은 일정이 발견되었습니다. 010-6372-2065로 문의 바랍니다.", Toast.LENGTH_LONG).show();
						}
						else{
							Toast.makeText(getBaseContext(), "삭제도중 에러가 발생하였습니다. 010-6372-2065로 문의 바랍니다.", Toast.LENGTH_LONG).show();
						}
					}
					
				}})
				.setNegativeButton("취소", null)
				.show();
			}
		});
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
