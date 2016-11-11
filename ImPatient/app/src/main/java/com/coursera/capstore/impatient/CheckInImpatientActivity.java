package com.coursera.capstore.impatient;

import com.coursera.capstore.impatient.api.ICloudImpatient;
import com.coursera.capstore.impatient.bean.CheckIn;
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

public class CheckInImpatientActivity extends Activity {

	private TextView textViewCheckInInformation;

	private EditText editTextCheckInId;
	private EditText editTextCheckInFullName;
	private EditText editTextCheckInDate;
	private EditText editTextCheckInHour;

	private Button buttonCheckInEdit;
	private Button buttonCheckInCancel;
	private Button buttonCheckInCheck;

	private UserLogin uLogin;
	private CheckIn uCheckIn;
	private ICloudImpatient iCloudImpatient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.impatient_checkin);
		setTextBar();
		initializeActivity();		
	}

	private void initializeActivity(){
		
		textViewCheckInInformation = (TextView) findViewById(R.id.textViewCheckInHeader);
		iCloudImpatient = HttpClientImpatient.createClient(ICloudImpatient.class);

		editTextCheckInId = (EditText) findViewById(R.id.editTextCheckInId);
		editTextCheckInFullName = (EditText) findViewById(R.id.editTextCheckInFullName);
		editTextCheckInDate = (EditText) findViewById(R.id.editTextCheckInDate);
		editTextCheckInHour = (EditText) findViewById(R.id.editTextCheckInHour);		
		
		buttonCheckInEdit = (Button) findViewById(R.id.buttonCheckInEdit);
		buttonCheckInCancel = (Button) findViewById(R.id.buttonCheckInCancel);		
		buttonCheckInCheck = (Button) findViewById(R.id.buttonCheckInCheck);
		
		buttonCheckInEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadEnabledStatus();
			}
			
		});

		buttonCheckInCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				popUpCancel(v);
			}
			
		});

		buttonCheckInCheck.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CheckIn ucheckIn = readCheckInEditText(); 
				updateDataInImpatientCloudService(iCloudImpatient, ucheckIn);			
			}
		});
		
		uLogin = (UserLogin) getIntent().getExtras().getSerializable("uLogin");
		
		loadDataFromImpatientCloudService(iCloudImpatient, uLogin);

	}

	private void loadDataFromImpatientCloudService(ICloudImpatient iCloudImpatient, UserLogin uLogin){
		
		Call<CheckIn> callCheckIn = iCloudImpatient.getUserCheckIn(uLogin);
		callCheckIn.enqueue(new Callback<CheckIn>() {

			@Override
			public void onFailure(Throwable t) {
				Log.e("CheckInImpatientActivity", " Exception: " + t.getMessage());
				t.printStackTrace();
				popUpAppError();
			}

			@Override
			public void onResponse(Response<CheckIn> response, Retrofit retrofit) {
				Log.d("LoginImpatientActivity", "Status Code = " + response.code());
				if(response.isSuccess()){
					uCheckIn = response.body();
					if(uCheckIn != null){
						Log.d("CheckInImpatientActivity", "uCheckIn = " + uCheckIn.toString());
						loadCheckInEditText(uCheckIn);
					}else{
						popUpAppError();
					}
				}else{
					popUpAppError();
				}
			}		
		});	
	}
	
	private void updateDataInImpatientCloudService(ICloudImpatient iCloudImpatient, CheckIn uData){
		
		Call<CheckIn> callCheckIn = iCloudImpatient.updateCheckIn(uData);
		callCheckIn.enqueue(new Callback<CheckIn>() {

			@Override
			public void onFailure(Throwable t) {
				Log.e("ProfileImpatientActivity", " Exception: " + t.getMessage());
				t.printStackTrace();
				popUpAppError();
			}

			@Override
			public void onResponse(Response<CheckIn> response, Retrofit retrofit) {
				Log.d("CheckInImpatientActivity", "Status Code = " + response.code());
				if(response.isSuccess()){
					uCheckIn = response.body();
					if(uCheckIn != null){
						Log.d("CheckInImpatientActivity", "uCheckIn = " + uCheckIn.toString());
						loadCheckInEditText(uCheckIn);
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
	
	private void loadCheckInEditText(CheckIn uCheckIn){

		try {
			editTextCheckInId.setText(Integer.toString(uCheckIn.getCheckInId()));
			editTextCheckInFullName.setText(uCheckIn.getFullName());
			editTextCheckInDate.setText(uCheckIn.getDate());
			editTextCheckInHour.setText(uCheckIn.getHour());
		} catch (Exception e) {
			Log.e("CheckInImpatientActivity", "Exception: " + e.getMessage());
		}
		
	}
	
	private CheckIn readCheckInEditText(){
		CheckIn checkIn = new CheckIn();
		
		try {
			checkIn.setCheckInId(uCheckIn.getCheckInId());
			checkIn.setFullName(editTextCheckInFullName.getText().toString());
			checkIn.setDate(editTextCheckInDate.getText().toString());
			checkIn.setHour(editTextCheckInHour.getText().toString());
		} catch (Exception e) {
			Log.e("CheckInImpatientActivity", "Exception: " + e.getMessage());
		}
		return checkIn;
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
		
		textViewCheckInInformation.setText(R.string.button_edit);
		
		editTextCheckInDate.setEnabled(true);
		editTextCheckInHour.setEnabled(true);

		buttonCheckInCancel.setEnabled(true);
		buttonCheckInCheck.setEnabled(true);
		buttonCheckInEdit.setEnabled(false);

	}
	
	private void loadEnabledStatusUpdate(){
		
		textViewCheckInInformation.setText(R.string.text_view_header);
		
		editTextCheckInDate.setEnabled(false);
		editTextCheckInHour.setEnabled(false);

		buttonCheckInCancel.setEnabled(false);
		buttonCheckInCheck.setEnabled(false);
		buttonCheckInEdit.setEnabled(true);
		
	}
	
	private void setTextBar(){
		TextView textViewBar = (TextView) findViewById(R.id.textViewBar);
		textViewBar.setText(R.string.checkin_layout_title);
	}

}
