package cs.ualberta.akt.akt_notes.adapters;

import java.util.ArrayList;

import cs.ualberta.akt.akt_notes.R;
import cs.ualberta.akt.akt_notes.ToDoItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.CheckBox;

//This class is used to display the ArrayList objects in the fragments called by MainActivity
//Nearly identical to SelectingCustomArrayAdapter but uses a different layout xml so the user can see a visual difference

public class CustomArrayAdapter extends ArrayAdapter<ToDoItem> {
	
	private ArrayList<ToDoItem> itemsOfInterest = null;
	
	//Initializes the adapter with the ArrayList we wish to display
	public CustomArrayAdapter(Context context, ArrayList<ToDoItem> itemsOfInterest) {
		super(context,R.layout.list_item, itemsOfInterest);
		
		this.itemsOfInterest = itemsOfInterest;
	}
	
	//Used to get the item located at the position of the onClick event
	@Override
	public ToDoItem getItem(int position) {
		return itemsOfInterest.get(position);
	}
	
	//Used to create ListView of items with a check box 
	//The check boxes are unclickable so onClick actions can be properly handled
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.list_item, null);		
		}
		
		TextView item = (TextView) view.findViewById(R.id.list_item);
		CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
		
		//Checks to see how to display check boxes
		item.setText(itemsOfInterest.get(position).getToDoItem());
		if (itemsOfInterest.get(position).getCheckedOff() == true) {
			checkBox.setChecked(true);
		} else {
			checkBox.setChecked(false);
		}	
		
		return view;
		
	}
	
}
