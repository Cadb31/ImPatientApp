package com.coursera.capstore.impatient;

import com.coursera.capstore.impatient.api.ICloudImpatient;
import com.coursera.capstore.impatient.bean.UserLogin;
import com.coursera.capstore.impatient.client.HttpClientImpatient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginImpatientActivity extends Activity {

	private EditText editTextUser;
	private EditText editTextPassword;
	private UserLogin userLogin;
	private ICloudImpatient iCloudImpatient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.impatient_login);
		setTextBar();
		initializeActivity();
		
	}
	
	private void initializeActivity() {

		editTextUser = (EditText) findViewById(R.id.editTextLoginUser);
		editTextPassword = (EditText) findViewById(R.id.editTextLoginPassword);

		iCloudImpatient = HttpClientImpatient.createClient(ICloudImpatient.class);
		
		Button btnLogin = (Button) findViewById(R.id.buttonLogin);
		
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Log.d("LoginImpatientActivity", "user: " + editTextUser.getText());
				Log.d("LoginImpatientActivity", "password: " + editTextPassword.getText());
			
				if((editTextUser.getText().toString().trim().length() == 0) || (editTextPassword.getText().toString().trim().length() == 0)){
					popUpNotUser();
				}else{
					UserLogin uLogin = new UserLogin();
					uLogin.setUsername(editTextUser.getText().toString());
					uLogin.setPassword(editTextPassword.getText().toString());
					Call<UserLogin> callUser = iCloudImpatient.getUserLogin(uLogin);
					callUser.enqueue(new Callback<UserLogin>() {
	
						@Override
						public void onFailure(Throwable t) {
							Log.e("LoginImpatientActivity", " Exception: " + t.getMessage());
							t.printStackTrace();
							popUpAppError();
						}
	
						@Override
						public void onResponse(Response<UserLogin> response, Retrofit retrofit) {
							Log.d("LoginImpatientActivity", "Status Code = " + response.code());
							if(response.isSuccess()){
								userLogin = response.body();
								if(userLogin != null){
									Log.d("LoginImpatientActivity", "userLogin = " + userLogin.toString());
									if(editTextUser.getText().toString().equals(userLogin.getUsername()) && editTextPassword.getText().toString().equals(userLogin.getPassword())){
										Intent intent = new Intent(getApplicationContext(), MenuImpatientActivity.class);
										intent.putExtra("uLogin", userLogin);
										startActivity(intent);
									} else {
										popUpNotUser();
									}
								}else{
									popUpNotUser();
								}
							}else{
								popUpAppError();
							}
						}
					});	
				}
			}
		});		
	}	

	private void popUpNotUser() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.login_not_valid).setTitle(R.string.alert_message)
				.setCancelable(false).setNegativeButton(R.string.button_close, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void popUpAppError() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.app_error_message).setTitle(R.string.alert_message)
				.setCancelable(false).setNegativeButton(R.string.button_close, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void setTextBar(){
		TextView textViewBar = (TextView) findViewById(R.id.textViewBar);
		textViewBar.setText(R.string.login_layout_title);
	}

}
