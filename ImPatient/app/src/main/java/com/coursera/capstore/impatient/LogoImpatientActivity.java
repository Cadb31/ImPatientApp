package com.coursera.capstore.impatient;

import com.coursera.capstore.impatient.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

public class LogoImpatientActivity extends Activity {
	
	private ThreadDelay delayThread = new ThreadDelay();
	
	private int DELAY = 2000;
	private Handler myHandler;
	private Runnable myRunnable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.impatient_logo);
		
		delayThread.start();
		myHandler = new Handler();
		myRunnable = new Runnable()	{
			@Override
			public void run() {
				Intent myIntent = new Intent(LogoImpatientActivity.this, LoginImpatientActivity.class);
				startActivity(myIntent);
				overridePendingTransition(R.anim.impatient_logo_fade_in, R.anim.impatient_logo_fade_out);
				finish();
			}
		};
	}
	
	
    public class ThreadDelay extends Thread {
		private boolean stopMe;

    	public void run() {
		   SystemClock.sleep(DELAY);
		   if(!stopMe)
		   {
			   myHandler.post(myRunnable);   
		   }
	   }
    	public void stopMe(boolean action){
    		this.stopMe = action;
    	}
    }
    
	@Override
	public void onBackPressed() {
		delayThread.stopMe(true);
		this.finish();
	}
	
	 
	
}