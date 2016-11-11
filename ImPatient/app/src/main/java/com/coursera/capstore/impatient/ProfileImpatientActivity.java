package com.coursera.capstore.impatient;

import com.coursera.capstore.impatient.api.ICloudImpatient;
import com.coursera.capstore.impatient.bean.User;
import com.coursera.capstore.impatient.bean.UserLogin;
import com.coursera.capstore.impatient.client.HttpClientImpatient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ProfileImpatientActivity extends Activity {

	private TextView textViewProfileInformation;
	
	private EditText editTextProfileIdNumber;
	private EditText editTextProfileFirstName;
	private EditText editTextProfileLastName;	
	private EditText editTextProfileDateOfBirth;
	private EditText editTextProfileEmail;
	private EditText editTextProfilePhone;

	private Button buttonProfileEdit;
	private Button buttonProfileCancel;
	private Button buttonProfileSave;
	
	private UserLogin uLogin;
	private User user;
	private ICloudImpatient iCloudImpatient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.impatient_profile);
		setTextBar();
		initializeActivity();
	}
	
	private void initializeActivity(){

		textViewProfileInformation = (TextView) findViewById(R.id.textViewProfileHeader);
		iCloudImpatient = HttpClientImpatient.createClient(ICloudImpatient.class);

		editTextProfileIdNumber  = (EditText) findViewById(R.id.editTextProfileIdNumber);
		editTextProfileFirstName = (EditText) findViewById(R.id.editTextProfileFirstName);
		editTextProfileLastName = (EditText) findViewById(R.id.editTextProfileLastName);
		editTextProfileDateOfBirth = (EditText) findViewById(R.id.editTextProfileDateOfBirth);
		editTextProfileEmail = (EditText) findViewById(R.id.editTextProfileEmail);
		editTextProfilePhone = (EditText) findViewById(R.id.editTextProfilePhone);

		buttonProfileEdit = (Button) findViewById(R.id.buttonProfileEdit);
		
		buttonProfileEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				loadEnabledStatus();
				
			}
		});
		
		buttonProfileCancel = (Button) findViewById(R.id.buttonProfileCancel);
		
		buttonProfileCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popUpCancel(v);			
			}
		});
		
		
		buttonProfileSave = (Button) findViewById(R.id.buttonProfileSave);
		
		buttonProfileSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				User uData = readProfileEditText(); 
				updateDataInImpatientCloudService(iCloudImpatient, uData);			
			}
		});
		
		uLogin = (UserLogin) getIntent().getExtras().getSerializable("uLogin");
		
		loadDataFromImpatientCloudService(iCloudImpatient, uLogin);
	}
	
	private void loadDataFromImpatientCloudService(ICloudImpatient iCloudImpatient, UserLogin uLogin){
		
		Call<User> callUser = iCloudImpatient.getUser(uLogin);
		callUser.enqueue(new Callback<User>() {

			@Override
			public void onFailure(Throwable t) {
				Log.e("ProfileImpatientActivity", " Exception: " + t.getMessage());
				t.printStackTrace();
				popUpAppError();
			}

			@Override
			public void onResponse(Response<User> response, Retrofit retrofit) {
				Log.d("ProfileImpatientActivity", "Status Code = " + response.code());
				if(response.isSuccess()){
					user = response.body();
					if(user != null){
						Log.d("ProfileImpatientActivity", "user = " + user.toString());
						loadProfileEditText(user);
					}else{
						popUpAppError();
					}
				}else{
					popUpAppError();
				}
			}		
		});	
	}
	
	private void updateDataInImpatientCloudService(ICloudImpatient iCloudImpatient, User uData){
		
		Call<User> callUser = iCloudImpatient.updateUser(uData);
		callUser.enqueue(new Callback<User>() {

			@Override
			public void onFailure(Throwable t) {
				Log.e("ProfileImpatientActivity", " Exception: " + t.getMessage());
				t.printStackTrace();
				popUpAppError();
			}

			@Override
			public void onResponse(Response<User> response, Retrofit retrofit) {
				Log.d("ProfileImpatientActivity", "Status Code = " + response.code());
				if(response.isSuccess()){
					user = response.body();
					if(user != null){
						Log.d("ProfileImpatientActivity", "user = " + user.toString());
						loadProfileEditText(user);
						popUpAppUpate();
						loadEnabledStatusUpdate();
					}else{
						popUpAppError();
					}
				}else{
					popUpAppError();
				}
			}		
		});	
	}
	
	
	private void loadProfileEditText(User user){

		try {
			editTextProfileIdNumber.setText(user.getUserId());
			editTextProfileFirstName.setText(user.getFirstName());
			editTextProfileLastName.setText(user.getLastName());	
			editTextProfileDateOfBirth.setText(user.getDateOfBirth());
			editTextProfileEmail.setText(user.getEmail());
			editTextProfilePhone.setText(user.getPhone());
			
		} catch (Exception e) {
			Log.e("ProfileImpatientActivity", "Exception: " + e.getMessage());
		}
		
	}
	
	private User readProfileEditText(){
		User user = new User();
		try {

			user.setUserId(this.user.getUserId());
			user.setFirstName(editTextProfileFirstName.getText().toString()); 
			user.setLastName(editTextProfileLastName.getText().toString());
			user.setDateOfBirth(editTextProfileDateOfBirth.getText().toString());
			user.setEmail(editTextProfileEmail.getText().toString());
			user.setPhone(editTextProfilePhone.getText().toString());
		
		} catch (Exception e) {
			Log.e("ProfileImpatientActivity", "Exception: " + e.getMessage());
		}
		return user;
	}
	
	
	private void popUpAppError() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.app_error_message).setTitle(R.string.alert_message)
				.setCancelable(false).setNegativeButton(R.string.button_close, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						setResult(RESULT_CANCELED, null);
				        finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void popUpCancel(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.question_cancel).setTitle(R.string.alert_message)
				.setCancelable(false).setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				}).setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						setResult(RESULT_CANCELED, null);
				        finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void popUpAppUpate() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.app_update_message).setTitle(R.string.info_message)
				.setCancelable(false).setNegativeButton(R.string.button_close, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void loadEnabledStatus(){
		
		textViewProfileInformation.setText(R.string.button_edit);
		
		editTextProfileFirstName.setEnabled(true);				
		editTextProfileLastName.setEnabled(true);				
		editTextProfileDateOfBirth.setEnabled(true);				
		editTextProfileEmail.setEnabled(true);
		editTextProfilePhone.setEnabled(true);

		buttonProfileCancel.setEnabled(true);
		buttonProfileSave.setEnabled(true);
		buttonProfileEdit.setEnabled(false);
		
	}
	
	private void loadEnabledStatusUpdate(){
		
		textViewProfileInformation.setText(R.string.text_view_header);
		
		editTextProfileFirstName.setEnabled(false);				
		editTextProfileLastName.setEnabled(false);				
		editTextProfileDateOfBirth.setEnabled(false);				
		editTextProfileEmail.setEnabled(false);
		editTextProfilePhone.setEnabled(false);

		buttonProfileCancel.setEnabled(false);
		buttonProfileSave.setEnabled(false);
		buttonProfileEdit.setEnabled(true);
		
	}
	
	private void setTextBar(){
		TextView textViewBar = (TextView) findViewById(R.id.textViewBar);
		textViewBar.setText(R.string.profile_layout_title);
	}

}
