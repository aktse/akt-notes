package cs.ualberta.akt.akt_notes.adapters;

import java.util.ArrayList;

import cs.ualberta.akt.akt_notes.R;
import cs.ualberta.akt.akt_notes.ToDoItem;
import cs.ualberta.akt.akt_notes.R.id;
import cs.ualberta.akt.akt_notes.R.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class EditModeCustomArrayAdapter extends ArrayAdapter<ToDoItem> {
	private ArrayList<ToDoItem> toDoItems = null;
	
	public EditModeCustomArrayAdapter(Context context, ArrayList<ToDoItem> toDoItems) {
		super(context,R.layout.edit_list_item, toDoItems);
		
		this.toDoItems = toDoItems;
	}
	
	//Used to get the item located at the position of the onClick event
	@Override
	public ToDoItem getItem(int position) {
		return toDoItems.get(position);
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
		item.setText(toDoItems.get(position).getToDoItem());
		if (toDoItems.get(position).getCheckedOff() == true) {
			checkBox.setChecked(true);
		} else {
			checkBox.setChecked(false);
		}	
		
		return view;
		
	}

}
