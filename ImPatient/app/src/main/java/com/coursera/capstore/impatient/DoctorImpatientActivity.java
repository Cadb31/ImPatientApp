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

public class DoctorImpatientActivity extends Activity {

	private	EditText editTextDoctorIdNumber;
	private	EditText editTextDoctorFirstName;
	private	EditText editTextDoctorLastName;	
	private	EditText editTextDoctorEmail;
	private	EditText editTextDoctorPhone;
	
	private Button buttonDoctorContact;
	
	private UserLogin uLogin;
	private User user;
	private ICloudImpatient iCloudImpatient;
	private CharSequence[] items = {"email", "watsapp", "phone"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.impatient_doctor);
		setTextBar();
		initializeActivity();
		
	}

	private void initializeActivity(){

		iCloudImpatient = HttpClientImpatient.createClient(ICloudImpatient.class);

		editTextDoctorIdNumber  = (EditText) findViewById(R.id.editTextDoctorIdNumber);
		editTextDoctorFirstName = (EditText) findViewById(R.id.editTextDoctorFirstName);
		editTextDoctorLastName = (EditText) findViewById(R.id.editTextDoctorLastName);		
		editTextDoctorEmail = (EditText) findViewById(R.id.editTextDoctorEmail);
		editTextDoctorPhone = (EditText) findViewById(R.id.editTextDoctorPhone);

		buttonDoctorContact = (Button) findViewById(R.id.buttonDoctorContact);
		
		buttonDoctorContact.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popUpAppContact();
			}
		});

		uLogin = (UserLogin) getIntent().getExtras().getSerializable("uLogin");
		
		loadDataFromImpatientCloudService(iCloudImpatient, uLogin);
	}
	
	private void loadDataFromImpatientCloudService(ICloudImpatient iCloudImpatient, UserLogin uLogin){
		
		Call<User> callUser = iCloudImpatient.getUserDoctor(uLogin);
		callUser.enqueue(new Callback<User>() {

			@Override
			public void onFailure(Throwable t) {
				Log.e("DoctorImpatientActivity", " Exception: " + t.getMessage());
				t.printStackTrace();
				popUpAppError();
			}

			@Override
			public void onResponse(Response<User> response, Retrofit retrofit) {
				Log.d("DoctorImpatientActivity", "Status Code = " + response.code());
				if(response.isSuccess()){
					user = response.body();
					if(user != null){
						Log.d("DoctorImpatientActivity", "user = " + user.toString());
						loadDoctorEditText(user);
					}else{
						popUpAppError();
					}
				}else{
					popUpAppError();
				}
			}		
		});	
	}
	
	private void loadDoctorEditText(User user){

		try {
			editTextDoctorIdNumber.setText(user.getUserId());
			editTextDoctorFirstName.setText(user.getFirstName());
			editTextDoctorLastName.setText(user.getLastName());	
			editTextDoctorEmail.setText(user.getEmail());
			editTextDoctorPhone.setText(user.getPhone());
			
		} catch (Exception e) {
			Log.e("DoctorImpatientActivity", "Exception: " + e.getMessage());
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

	private void popUpAppContact() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.info_message);		
		builder.setItems(items, new DialogInterface.OnClickListener()  {
			
			@Override
			public void onClick(DialogInterface dialog, int id) {
				Log.i("DoctorImpatientActivity", "item = " + items[id]);
			}
		});

		builder.setCancelable(false).setNegativeButton(R.string.button_close, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();			   
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	private void setTextBar(){
		TextView textViewBar = (TextView) findViewById(R.id.textViewBar);
		textViewBar.setText(R.string.doctor_layout_title);
	}
}
