package com.coursera.capstore.impatient;

import java.util.List;

import com.coursera.capstore.impatient.bean.Waiting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemAdapterWaiting extends BaseAdapter {

	private Context context;
	private List<Waiting> itemList;

	public ItemAdapterWaiting() {
		// TODO Auto-generated constructor stub
	}

	public ItemAdapterWaiting(Context context, List<Waiting> items) {
		this.context = context;
		this.itemList = items;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return itemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;

		if (convertView == null) {
			// Create a new view into the list.
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.impatient_waiting_item, parent, false);
		}

		// Set data into the view.
		TextView textViewIdList = (TextView) rowView.findViewById(R.id.textViewIdList);
		TextView textViewHour = (TextView) rowView.findViewById(R.id.textViewHour);
		TextView textViewFullName = (TextView) rowView.findViewById(R.id.textViewFullName);
		TextView textViewState = (TextView) rowView.findViewById(R.id.textViewState);
		
		Waiting wItem = itemList.get(position);
		if(!wItem.equals("Finish")){
			textViewIdList.setText(Integer.toString(wItem.getIdList()));
			textViewHour.setText(wItem.getHour());
			textViewFullName.setText(wItem.getFullName());
			textViewState.setText(wItem.getState());
		}
		return rowView;
		
	}

}
