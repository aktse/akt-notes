package cs.ualberta.akt.akt_notes.adapters;

import java.util.ArrayList;

import cs.ualberta.akt.akt_notes.R;
import cs.ualberta.akt.akt_notes.ToDoItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

//This class is used to display the ArrayList objects in EditMode and EmailActivity.
//Necessary to create check boxes in a ListView
//Nearly identical to CustomArrayAdapter but uses a different layout xml so user can see a visual difference

public class SelectingCustomArrayAdapter extends ArrayAdapter<ToDoItem> {
	
	private ArrayList<ToDoItem> itemsOfInterest;
	
	//Initializes the adapter with the ArrayList we wish to display in a specific ListView
	public SelectingCustomArrayAdapter(Context context, ArrayList<ToDoItem> itemsOfInterest) {
		super(context,R.layout.edit_list_item, itemsOfInterest);
		
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
			view = inflater.inflate(R.layout.edit_list_item, null);		
		}
		
		TextView item = (TextView) view.findViewById(R.id.edit_list_item);
		CheckBox checkBox = (CheckBox) view.findViewById(R.id.editCheckBox);
		
		//Checks to see how to display check boxes
		item.setText(itemsOfInterest.get(position).getToDoItem());
		if (itemsOfInterest.get(position).getSelected() == true) {
			checkBox.setChecked(true);
		} else {
			checkBox.setChecked(false);
		}	
		
		return view;
		
	}

}
