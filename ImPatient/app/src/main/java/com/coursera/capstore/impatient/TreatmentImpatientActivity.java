package com.coursera.capstore.impatient;

import com.coursera.capstore.impatient.api.ICloudImpatient;
import com.coursera.capstore.impatient.bean.Treatment;
import com.coursera.capstore.impatient.bean.TreatmentList;
import com.coursera.capstore.impatient.bean.UserLogin;
import com.coursera.capstore.impatient.client.HttpClientImpatient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TreatmentImpatientActivity extends Activity {

	private ListView listViewTreatmentList;
	
	private UserLogin uLogin;
	private TreatmentList tList;
	private ICloudImpatient iCloudImpatient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.impatient_treatment);
		setTextBar();
		initializeActivity();
		
	}
	
	private void initializeActivity(){
		
		iCloudImpatient = HttpClientImpatient.createClient(ICloudImpatient.class);

		listViewTreatmentList = (ListView) findViewById(R.id.listViewTreatmentList);		
		
		listViewTreatmentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								
			}
		});

		uLogin = (UserLogin) getIntent().getExtras().getSerializable("uLogin");
		
		loadDataFromImpatientCloudService(iCloudImpatient, uLogin);

	}

	private void loadDataFromImpatientCloudService(ICloudImpatient iCloudImpatient, UserLogin uLogin){
		
		Call<TreatmentList> callTList = iCloudImpatient.getTreatmentList(uLogin);
		callTList.enqueue(new Callback<TreatmentList>() {

			@Override
			public void onFailure(Throwable t) {
				Log.e("TreatmentImpatientActivity", " Exception: " + t.getMessage());
				t.printStackTrace();
				popUpAppError();
			}

			@Override
			public void onResponse(Response<TreatmentList> response, Retrofit retrofit) {
				Log.d("TreatmentImpatientActivity", "Status Code = " + response.code());
				if(response.isSuccess()){
					tList = response.body();
					if(tList != null){
						Log.d("TreatmentImpatientActivity", "tList = " + tList.toString());
						loadTreatmentListText(tList);
					}else{
						popUpAppError();
					}
				}else{
					popUpAppError();
				}
			}		
		});	
	}
	
	private void loadTreatmentListText(TreatmentList tList){

		try {
		
			ItemAdapterTreatment adapter = new ItemAdapterTreatment(getBaseContext(), tList.getTreatemntList());
			listViewTreatmentList.setAdapter(adapter);
			
			for (Treatment t: tList.getTreatemntList()) {
				Log.i("TreatmentImpatientActivity", "t = " + t.toString());
			}
		} catch (Exception e) {
			Log.e("TreatmentImpatientActivity", "Exception: " + e.getMessage());
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
		textViewBar.setText(R.string.treatment_layout_title);
	}
}
