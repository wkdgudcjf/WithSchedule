package com.example.withschedule;

import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.withschedule.socket.*;
import com.google.android.gcm.*;

public class JoinActivity extends Activity {
	
	private CheckBox provisionCheckBox;
	private TextView emailView;
	private TextView nameView;
	private TextView pwView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_join_view);
	
		AppManager.getInstance().setActivity(this);
		AppManager.getInstance().setResources(getResources());
		
		//email 입력박스
		final InputFilter inputEmailFilter = new InputFilter(){
			public CharSequence filter(CharSequence source, int start, int end, android.text.Spanned dest, int dstart, int dend){
				Pattern ps = Pattern.compile("^[a-zA-Z0-9@.]+$");
				if(!ps.matcher(source).matches()){
					return"";
				}
				return null;
			}
		};
		
		//비밀번호 입력박스
		final InputFilter inputPasswordFilter = new InputFilter(){
			public CharSequence filter(CharSequence source, int start, int end, android.text.Spanned dest, int dstart, int dend){
				Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
				if(!ps.matcher(source).matches()){
					return"";
				}
				return null;
			}
		};
		

		provisionCheckBox = (CheckBox)findViewById(R.id.use_ok);
		emailView = (TextView)findViewById(R.id.join_email);
		nameView = (TextView)findViewById(R.id.join_name);
		pwView = (TextView)findViewById(R.id.join_password);
	
		
		LinearLayout emailLinearLayout = (LinearLayout)findViewById(R.id.linear_join_email);
		emailLinearLayout.setOnClickListener(new OnClickListener() {			
			EditText emailEditText;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub	
				final LinearLayout linear = (LinearLayout)View.inflate(JoinActivity.this, R.layout.member_join_email, null);
				
				emailEditText = (EditText)linear.findViewById(R.id.join_email);
				emailEditText.setFilters(new InputFilter[]{inputEmailFilter});
				emailEditText.setText(emailView.getText());	
				
				new AlertDialog.Builder(JoinActivity.this)
				.setTitle("이메일")
				.setView(linear)
				.setPositiveButton("확인", new DialogInterface.OnClickListener()
				{
				public void onClick(DialogInterface arg0, int arg1)
				{
					emailView.setText(emailEditText.getText());
					InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);   
					imm.hideSoftInputFromWindow(emailEditText.getWindowToken(),0); 
				}
				})
				.setNegativeButton("취소", null)
				.show();
			}
		});
		
		LinearLayout nameLinearLayout = (LinearLayout)findViewById(R.id.linear_join_name);
		nameLinearLayout.setOnClickListener(new OnClickListener() {			
			EditText nameEditText;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub	
				final LinearLayout linear = (LinearLayout)View.inflate(JoinActivity.this, R.layout.member_join_name, null);
				
				nameEditText = (EditText)linear.findViewById(R.id.join_name);
				nameEditText.setText(nameView.getText());	
				
				new AlertDialog.Builder(JoinActivity.this)
				.setTitle("이름")
				.setView(linear)
				.setPositiveButton("확인", new DialogInterface.OnClickListener()
				{
				public void onClick(DialogInterface arg0, int arg1)
				{
					nameView.setText(nameEditText.getText());
					InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);   
					imm.hideSoftInputFromWindow(nameEditText.getWindowToken(),0); 
				}
				})
				.setNegativeButton("취소", null)
				.show();
			}
		});
		
		LinearLayout pwLinearLayout = (LinearLayout)findViewById(R.id.linear_join_password);
		pwLinearLayout.setOnClickListener(new OnClickListener() {			
			EditText passwordEditText;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub	
				final LinearLayout linear = (LinearLayout)View.inflate(JoinActivity.this, R.layout.member_join_password, null);
				
				passwordEditText = (EditText)linear.findViewById(R.id.join_pw);
				passwordEditText.setText(pwView.getText());	
				passwordEditText.setFilters(new InputFilter[]{inputPasswordFilter,new InputFilter.LengthFilter(12)});
				new AlertDialog.Builder(JoinActivity.this)
				.setTitle("비밀번호")
				.setView(linear)
				.setPositiveButton("확인", new DialogInterface.OnClickListener()
				{
				public void onClick(DialogInterface arg0, int arg1)
				{
					pwView.setText(passwordEditText.getText());
					InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);   
					imm.hideSoftInputFromWindow(passwordEditText.getWindowToken(),0); 
				}
				})
				.setNegativeButton("취소", null)
				.show();
			}
		});
		
		Button btnJoin = (Button)findViewById(R.id.btn_join);
		btnJoin.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				String emailtext = emailView.getText().toString();
				
				if(!provisionCheckBox.isChecked()){
					showDialog(AppManager.POPUP_A);
				}				
				else if(!emailtext.contains("@")||!emailtext.contains(".")
						||emailtext.lastIndexOf("@")!=emailtext.indexOf("@")
						||emailtext.lastIndexOf(".")!=emailtext.indexOf(".")
						||emailtext.lastIndexOf("@")>=emailtext.indexOf("."))
				{		
					showDialog(AppManager.POPUP_B);
				}				
				else if(pwView.length()<5||pwView.length()>12)
				{
					showDialog(AppManager.POPUP_D);
				}
				else if(nameView.length()==0)
				{
					showDialog(AppManager.POPUP_C);
				}
				else{
					if(!AppManager.getInstance().isPossibleInternet())
					{
						showDialog(AppManager.POPUP_J);	
					}
					else
					{					
						ProgressBarThread thread = new ProgressBarThread(JoinActivity.this);
						thread.start();
						
						String result = MemberSetting.join(JoinActivity.this,emailView.getText().toString(),nameView.getText().toString(),pwView.getText().toString());
						
						thread.stopProgressBar();
						if(result.equals(AppManager.COMPLETION))
						{			

							MemberSetting.initSetting(JoinActivity.this, emailView.getText().toString(),nameView.getText().toString(),pwView.getText().toString());
							MemberSetting.friendSetting(JoinActivity.this);
							GCMRegistrar.checkDevice(JoinActivity.this);
							GCMRegistrar.checkManifest(JoinActivity.this);
							final String regId = GCMRegistrar.getRegistrationId(JoinActivity.this);
							if("".equals(regId))   //구글 가이드에는 regId.equals("")로 되어 있는데 Exception을 피하기 위해 수정
							{
								GCMRegistrar.register(JoinActivity.this, "461412796848");
							}
							else
							{
								MemberSetting.gcmregister(JoinActivity.this, regId);
							}
							
							moveToMainPage();
						}
						else if(result.equals(AppManager.DOUBLE_EMAIL))
						{
							showDialog(AppManager.POPUP_E);
						}
						else if(result.equals(AppManager.HTTP_ERROR))
						{
							showDialog(AppManager.POPUP_R);
						}
						else
						{
							showDialog(AppManager.POPUP_F);
						}
					}					
				}
			}
		});
		
		

		TextView btnMoveLoginPage = (TextView)findViewById(R.id.already_join);
		btnMoveLoginPage.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				moveToLoginPage();
			}
		});
	}
		
	public void moveToMainPage(){
		Intent myIntent = new Intent(JoinActivity.this,CalendarActivity.class);
		startActivity(myIntent);
		finish();
	}
	
	public void moveToLoginPage(){
		Intent myIntent = new Intent(JoinActivity.this,LoginActivity.class);
		startActivity(myIntent);
		finish();
	}
	
	protected void onDestroy()
	{
		JoinActivity.this.finish();
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
		return builder.create();
	}
	
}
