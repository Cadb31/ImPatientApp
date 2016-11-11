package com.coursera.capstore.impatient;

import com.coursera.capstore.impatient.api.ICloudImpatient;
import com.coursera.capstore.impatient.bean.UserLogin;
import com.coursera.capstore.impatient.bean.Waiting;
import com.coursera.capstore.impatient.bean.WaitingList;
import com.coursera.capstore.impatient.client.HttpClientImpatient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class WaitingListImpatientActivtity extends Activity {

	private EditText editTextWaitingListDate;
	private ListView listViewWaitingList;
	
	private UserLogin uLogin;
	private Waiting waiting;
	private WaitingList wList;
	
	private ICloudImpatient iCloudImpatient;
	private CharSequence[] items = {"Wait", "Process", "Delay", "Finish"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.impatient_waitinglist);
		setTextBar();
		initializeActivity();
		
	}

	private void initializeActivity(){
		
		iCloudImpatient = HttpClientImpatient.createClient(ICloudImpatient.class);
		
		editTextWaitingListDate = (EditText) findViewById(R.id.editTextWaitingListDate);
		listViewWaitingList = (ListView) findViewById(R.id.listViewWaitingList);

		listViewWaitingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(uLogin.getUserRole() == 1){
					popUpAppChangeState(position);
				}			
			}
		});

		uLogin = (UserLogin) getIntent().getExtras().getSerializable("uLogin");
		
		loadDataFromImpatientCloudService(iCloudImpatient, uLogin);

	}

	private void loadDataFromImpatientCloudService(ICloudImpatient iCloudImpatient, UserLogin uLogin){
		
		Call<WaitingList> callWList = iCloudImpatient.getWaitingList(uLogin);
		callWList.enqueue(new Callback<WaitingList>() {

			@Override
			public void onFailure(Throwable t) {
				Log.e("WaitingListImpatientActivtity", " Exception: " + t.getMessage());
				t.printStackTrace();
				popUpAppError();
			}

			@Override
			public void onResponse(Response<WaitingList> response, Retrofit retrofit) {
				Log.d("WaitingListImpatientActivtity", "Status Code = " + response.code());
				if(response.isSuccess()){
					wList = response.body();
					if(wList != null){
						Log.d("WaitingListImpatientActivtity", "wList = " + wList.toString());
						loadWaitingListText(wList);
					}else{
						popUpAppError();
					}
				}else{
					popUpAppError();
				}
			}		
		});	
	}
	
	private void updateDataInImpatientCloudService(ICloudImpatient iCloudImpatient, Waiting wData){
		
		Call<WaitingList> callWaiting = iCloudImpatient.updateWaitingList(wData);
		callWaiting.enqueue(new Callback<WaitingList>() {

			@Override
			public void onFailure(Throwable t) {
				Log.e("ProfileImpatientActivity", " Exception: " + t.getMessage());
				t.printStackTrace();
				popUpAppError();
			}

			@Override
			public void onResponse(Response<WaitingList> response, Retrofit retrofit) {
				Log.d("ProfileImpatientActivity", "Status Code = " + response.code());
				if(response.isSuccess()){
					wList = response.body();
					if(wList != null){
						Log.d("WaitingListImpatientActivtity", "wList = " + wList.toString());
						loadWaitingListText(wList);
					}else{
						popUpAppError();
					}
				}else{
					popUpAppError();
				}
			}		
		});	
	}
	
	
	private void loadWaitingListText(WaitingList wList){

		try {
			if(wList.getWaitingList() != null){
				editTextWaitingListDate.setText(wList.getDate());
				ItemAdapterWaiting adapter = new ItemAdapterWaiting(getBaseContext(), wList.getWaitingList());
				listViewWaitingList.setAdapter(adapter);
				for (Waiting w: wList.getWaitingList()) {
					Log.i("WaitingListImpatientActivtity", "w = " + w.toString());
				}
			}
		} catch (Exception e) {
			Log.e("WaitingListImpatientActivtity", "Exception: " + e.getMessage());
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

	private void popUpAppChangeState(int position) {
		
		waiting = wList.getWaitingList().get(position);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.info_message);		
		builder.setItems(items, new DialogInterface.OnClickListener()  {
			
			@Override
			public void onClick(DialogInterface dialog, int id) {
				Log.i("WaitingListImpatientActivtity", "item = " + items[id]);
				waiting.setState(items[id].toString());
				updateDataInImpatientCloudService(iCloudImpatient, waiting);				
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
		textViewBar.setText(R.string.waiting_list_layout_title);
	}
}
