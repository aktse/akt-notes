package cs.ualberta.akt.akt_notes;

import java.util.ArrayList;

import cs.ualberta.akt.akt_notes.adapters.EditModeCustomArrayAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class EditMode extends Activity {

	private ArrayList<ToDoItem> toDoItems;
		
	private ListView itemList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_edit_mode);
        itemList = (ListView) findViewById(R.id.editList);
        
    	//Gets the ArrayList passed from MainActivity
    	DataWrapper dw = (DataWrapper)getIntent().getSerializableExtra("items");
    	toDoItems = dw.getArray();

    	//Assigns CustomArrayAdapter to ListView
    	final EditModeCustomArrayAdapter toDoItemsViewAdapter = new EditModeCustomArrayAdapter(this, toDoItems);
    	itemList.setAdapter(toDoItemsViewAdapter);
    	
    	//Assigns ItemClickListener to ListView to toggle check box on row click
    	itemList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ToDoItem toDoItem = toDoItemsViewAdapter.getItem(position);
				toDoItem.toggleCheckedOff();
				toDoItemsViewAdapter.notifyDataSetChanged();
			}
    	
    	
    	});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_mode, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		/*
		if (id == R.id.action_settings) {
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}
}
