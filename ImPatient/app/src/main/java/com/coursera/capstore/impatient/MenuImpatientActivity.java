package com.coursera.capstore.impatient;

import com.coursera.capstore.impatient.bean.UserLogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuImpatientActivity extends Activity {

	private UserLogin uLogin = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.impatient_menu);
		setTextBar();
		initializeActivity();		
	}

	private void initializeActivity() {
		

		ImageView imgViewCheckIn = (ImageView) findViewById(R.id.imageViewCheckIn);

		imgViewCheckIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), CheckInImpatientActivity.class);
				//Toast.makeText(getApplicationContext(), "CheckIn", Toast.LENGTH_SHORT).show();
				intent.putExtra("uLogin", uLogin);
				startActivity(intent);
			}
		});
		
		ImageView imgViewWaitingList = (ImageView) findViewById(R.id.imageViewWaitingList);

		imgViewWaitingList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), WaitingListImpatientActivtity.class);
				//Toast.makeText(getApplicationContext(), "List", Toast.LENGTH_SHORT).show();
				intent.putExtra("uLogin", uLogin);
				startActivity(intent);
			}
		});

		ImageView imgViewProfile = (ImageView) findViewById(R.id.imageViewProfile);

		imgViewProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ProfileImpatientActivity.class);
				//Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_SHORT).show();
				intent.putExtra("uLogin", uLogin);
				startActivity(intent);
			}
		});

		ImageView imageViewDoctor = (ImageView) findViewById(R.id.imageViewDoctor);

		imageViewDoctor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), DoctorImpatientActivity.class);
				//Toast.makeText(getApplicationContext(), "Doctor", Toast.LENGTH_SHORT).show();
				intent.putExtra("uLogin", uLogin);
				startActivity(intent);
			}
		});
		
		ImageView imageViewEmail = (ImageView) findViewById(R.id.imageViewEmail);

		imageViewEmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), EmailImpatientActivity.class);
				//Toast.makeText(getApplicationContext(), "Email", Toast.LENGTH_SHORT).show();
				intent.putExtra("uLogin", uLogin);
				startActivity(intent);
			}
		});
		
		ImageView imgViewTreatment = (ImageView) findViewById(R.id.imageViewTreatment);

		imgViewTreatment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), TreatmentImpatientActivity.class);
				//Toast.makeText(getApplicationContext(), "Treatment", Toast.LENGTH_SHORT).show();
				intent.putExtra("uLogin", uLogin);
				startActivity(intent);
			}
		});
		
		ImageView imgViewDate = (ImageView) findViewById(R.id.imageViewDate);

		imgViewDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), DatePatientActivity.class);
				//Toast.makeText(getApplicationContext(), "Date", Toast.LENGTH_SHORT).show();
				intent.putExtra("uLogin", uLogin);
				startActivity(intent);
			}
		});

		ImageView imgViewSettings = (ImageView) findViewById(R.id.imageViewSettings);

		imgViewSettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SettingImpatientActivity.class);
				//Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
				intent.putExtra("uLogin", uLogin);
				startActivity(intent);
			}
		});
		
		//Read the userLogin passed with parameter
		uLogin = (UserLogin) getIntent().getExtras().getSerializable("uLogin");
		
	}
	
	private void setTextBar(){
		TextView textViewBar = (TextView) findViewById(R.id.textViewBar);
		textViewBar.setText(R.string.menu_layout_title);
	}
}
