package com.coursera.capstore.impatient;

import java.util.List;

import com.coursera.capstore.impatient.bean.Treatment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemAdapterTreatment extends BaseAdapter {

	private Context context;
	private List<Treatment> itemList;
	
	public ItemAdapterTreatment() {
		// TODO Auto-generated constructor stub
	}

	public ItemAdapterTreatment(Context context, List<Treatment> itemList) {
		super();
		this.context = context;
		this.itemList = itemList;
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
			rowView = inflater.inflate(R.layout.impatient_treatment_item, parent, false);
		}

		// Set data into the view.
		TextView textViewOrder = (TextView) rowView.findViewById(R.id.textViewOrder);
		TextView textViewDrugName = (TextView) rowView.findViewById(R.id.textViewDrugName);
		TextView textViewDescription = (TextView) rowView.findViewById(R.id.textViewDescription);
		
		Treatment tItem = itemList.get(position);
		textViewOrder.setText(Integer.toString(tItem.getOrder()));
		textViewDrugName.setText(tItem.getDrugName());
		textViewDescription.setText(tItem.getDescription());

		return rowView;
	}

}
