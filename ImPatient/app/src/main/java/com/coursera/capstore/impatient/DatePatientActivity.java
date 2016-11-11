package com.coursera.capstore.impatient;

import com.coursera.capstore.impatient.api.ICloudImpatient;
import com.coursera.capstore.impatient.bean.UserDate;
import com.coursera.capstore.impatient.bean.UserLogin;
import com.coursera.capstore.impatient.client.HttpClientImpatient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DatePatientActivity extends Activity{

	private CheckBox checkBoxRemember;
	
	private EditText editTextDateDate;
	private EditText editTextDateHour;
	private EditText editTextDateDoctor;
	
	private UserLogin uLogin;
	private UserDate uDate;
	private ICloudImpatient iCloudImpatient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.impatient_date);
		setTextBar();
		initializeActivity();
	}
	
	private void initializeActivity(){
		
		checkBoxRemember = (CheckBox) findViewById(R.id.checkBoxRemember);
		
		iCloudImpatient = HttpClientImpatient.createClient(ICloudImpatient.class);

		editTextDateDate = (EditText) findViewById(R.id.editTextDateDate);
		editTextDateHour = (EditText) findViewById(R.id.editTextDateHour);
		editTextDateDoctor = (EditText) findViewById(R.id.editTextDateDoctor);
		
		uLogin = (UserLogin) getIntent().getExtras().getSerializable("uLogin");
		
		loadDataFromImpatientCloudService(iCloudImpatient, uLogin);

	}

	private void loadDataFromImpatientCloudService(ICloudImpatient iCloudImpatient, UserLogin uLogin){
		
		Call<UserDate> callUserDate = iCloudImpatient.getUserDate(uLogin);
		callUserDate.enqueue(new Callback<UserDate>() {

			@Override
			public void onFailure(Throwable t) {
				Log.e("DatePatientActivity", " Exception: " + t.getMessage());
				t.printStackTrace();
				popUpAppError();
			}

			@Override
			public void onResponse(Response<UserDate> response, Retrofit retrofit) {
				Log.d("DatePatientActivity", "Status Code = " + response.code());
				if(response.isSuccess()){
					uDate = response.body();
					if(uDate != null){
						Log.d("DatePatientActivity", "uDate = " + uDate.toString());
						loadDateEditText(uDate);
					}else{
						popUpAppError();
					}
				}else{
					popUpAppError();
				}
			}		
		});	
	}
	
	private void loadDateEditText(UserDate uDate){

		try {

			checkBoxRemember.setChecked(uDate.isRemember());
			
			editTextDateDate.setText(uDate.getDate());
			editTextDateHour.setText(uDate.getHour());
			editTextDateDoctor.setText(uDate.getDoctorName());
			
		} catch (Exception e) {
			Log.e("DatePatientActivity", "Exception: " + e.getMessage());
		}
		
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

	private void setTextBar(){
		TextView textViewBar = (TextView) findViewById(R.id.textViewBar);
		textViewBar.setText(R.string.date_layout_title);
	}
}
