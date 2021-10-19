package com.example.withschedule;

import java.util.regex.Pattern;

import com.example.withschedule.socket.MemberSetting;
import com.google.android.gcm.GCMRegistrar;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoginActivity extends Activity {	
	private TextView emailView;
	private TextView pwView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_login_view);
		
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
		
		emailView = (TextView)findViewById(R.id.login_email);
		pwView = (TextView)findViewById(R.id.login_password);

		LinearLayout emailLinearLayout = (LinearLayout)findViewById(R.id.linear_login_email);
		emailLinearLayout.setOnClickListener(new OnClickListener() {			
			EditText emailEditText;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub	
				final LinearLayout linear = (LinearLayout)View.inflate(LoginActivity.this, R.layout.member_join_email, null);
				
				emailEditText = (EditText)linear.findViewById(R.id.join_email);
				emailEditText.setFilters(new InputFilter[]{inputEmailFilter});
				emailEditText.setText(emailView.getText());	
				
				new AlertDialog.Builder(LoginActivity.this)
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
				
		LinearLayout pwLinearLayout = (LinearLayout)findViewById(R.id.linear_login_password);
		pwLinearLayout.setOnClickListener(new OnClickListener() {			
			EditText passwordEditText;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub	
				final LinearLayout linear = (LinearLayout)View.inflate(LoginActivity.this, R.layout.member_join_password, null);
				
				passwordEditText = (EditText)linear.findViewById(R.id.join_pw);
				passwordEditText.setText(pwView.getText());
				passwordEditText.setFilters(new InputFilter[]{inputPasswordFilter,new InputFilter.LengthFilter(12)});
				
				new AlertDialog.Builder(LoginActivity.this)
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
		
		
		Button btnLogin = (Button)findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub				
				String emailtext = emailView.getText().toString();
				
				if(!emailtext.contains("@")||!emailtext.contains(".")
						||emailtext.lastIndexOf("@")!=emailtext.indexOf("@")
						||emailtext.lastIndexOf(".")!=emailtext.indexOf(".")
						||emailtext.lastIndexOf("@")>=emailtext.indexOf(".")){		
					showDialog(AppManager.POPUP_B);
				}				
				else if(pwView.length()<5||pwView.length()>12){
					showDialog(AppManager.POPUP_D);
				}
				else{
					String result=null;
					if(!AppManager.getInstance().isPossibleInternet()) {
						showDialog(AppManager.POPUP_J);	
					}
					else{
						ProgressBarThread thread = new ProgressBarThread(LoginActivity.this);
						thread.start();
						
						result = MemberSetting.login(LoginActivity.this,emailView.getText().toString(),pwView.getText().toString());
						
						thread.stopProgressBar();
					}
					if(result.equals(AppManager.COMPLETION)){
						GCMRegistrar.checkDevice(LoginActivity.this);
						GCMRegistrar.checkManifest(LoginActivity.this);
						final String regId = GCMRegistrar.getRegistrationId(LoginActivity.this);
						if("".equals(regId))   //구글 가이드에는 regId.equals("")로 되어 있는데 Exception을 피하기 위해 수정
						{
							GCMRegistrar.register(LoginActivity.this, "461412796848");
						}
						else
						{
							MemberSetting.gcmregister(LoginActivity.this, regId);
						}
						moveToMainPage();
					}
					else if(result.equals(AppManager.NO_SEARCH)){
						showDialog(AppManager.POPUP_G);
					}
					else if(result.equals(AppManager.HTTP_ERROR)){
						showDialog(AppManager.POPUP_R);
					}
					else{
						showDialog(AppManager.POPUP_H);
					}
				}				
			}
		});		
		
	}
	public void moveToMainPage(){
		Intent myIntent = new Intent(LoginActivity.this,SplashActivity.class);
		startActivity(myIntent);
	    finish();
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
