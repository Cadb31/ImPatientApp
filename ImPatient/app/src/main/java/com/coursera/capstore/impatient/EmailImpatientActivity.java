package com.coursera.capstore.impatient;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class EmailImpatientActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.impatient_email);
		setTextBar();
	}

	private void setTextBar(){
		TextView textViewBar = (TextView) findViewById(R.id.textViewBar);
		textViewBar.setText(R.string.email_layout_title);
	}
}
