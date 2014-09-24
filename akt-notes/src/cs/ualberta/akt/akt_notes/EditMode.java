package cs.ualberta.akt.akt_notes;

import java.util.ArrayList;

import cs.ualberta.akt.akt_notes.adapters.SelectingCustomArrayAdapter;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class EditMode extends FragmentActivity {

	private ArrayList<ToDoItem> itemsOfInterest;
	private ArrayList<ToDoItem> transfer;
	private ArrayList<ToDoItem> otherItems;
	private ArrayList<ToDoItem> selected = new ArrayList<ToDoItem>();
	private String viewFrom;
		
	private ListView itemList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_mode);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle("Edit Items");
		
        itemList = (ListView) findViewById(R.id.editList);
        
    	//Gets the ArrayList of items populating the current view passed from MainActivity
    	ItemWrapper iw = (ItemWrapper)getIntent().getSerializableExtra("interest");
    	transfer = iw.getArray();
    	itemsOfInterest = transfer;
    	
    	//Gets the ArrayList of items not populating the current view
    	ItemWrapper iw_others = (ItemWrapper)getIntent().getSerializableExtra("others");
    	otherItems = iw_others.getArray();
    	
    	//Gets the view that called EditMode
    	viewFrom = getIntent().getStringExtra("View");

    	//Assigns EditModeCustomArrayAdapter to ListView + displays ArrayList of items
    	final SelectingCustomArrayAdapter toDoItemsViewAdapter = new SelectingCustomArrayAdapter(this, itemsOfInterest);
    	itemList.setAdapter(toDoItemsViewAdapter);
    	
    	//Assigns ItemClickListener to ListView to toggle check box on row click
    	itemList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ToDoItem toDoItem = toDoItemsViewAdapter.getItem(position);
				toDoItem.toggleSelected();
				if (!selected.contains(toDoItem)){
					selected.add(toDoItem);
				} else {
					selected.remove(toDoItem);
				}
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
		
		//When an option is selected, create a dialogFragment to confirm the action
		//Code derived from Android Developer website
		//http://developer.android.com/guide/topics/ui/dialogs.html
		if (id == R.id.action_delete) {
			if (selected.isEmpty()){
				Toast.makeText(this, "No items have been selected", Toast.LENGTH_SHORT).show();
			} else {
				ConfirmationFragment confirmationFragment = ConfirmationFragment.newInstance(selected.size() + " item(s) to be deleted","action_delete");
				confirmationFragment.show(getSupportFragmentManager(),"delete");
			} return true;
		} else if (id == R.id.action_archive){
			if (selected.isEmpty()){
				Toast.makeText(this, "No items have been selected", Toast.LENGTH_SHORT).show();
			} else {
				if (viewFrom.equals("toDoList")) {
					ConfirmationFragment confirmationFragment = ConfirmationFragment.newInstance(selected.size() + " item(s) to be archived", "action_archive");
					confirmationFragment.show(getSupportFragmentManager(), "archive");
				} else {
					ConfirmationFragment confirmationFragment = ConfirmationFragment.newInstance(selected.size() + " item(s) to be unarchived", "action_archive");
					confirmationFragment.show(getSupportFragmentManager(), "archive");
				}
			}
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//When "confirm" is clicked on dialogBox, performs an action based on which button is clicked
	public void doPositiveClick(String dialogID){
		if (dialogID.equals("action_delete")){
			for (int i = 0; i < selected.size(); i++){
				transfer.remove(selected.get(i));
			}
			
		} else if (dialogID.equals("action_archive")){
			for (int i = 0; i < selected.size(); i++){
				ToDoItem toDoItem = selected.get(i);
				toDoItem.setSelected();
				otherItems.add(toDoItem);
				transfer.remove(selected.get(i));

			}
		}
		
		Intent archiveIntent = new Intent(this,MainActivity.class);
		archiveIntent.setType("other");
		archiveIntent.putExtra("uniqueID", "editItem");
		archiveIntent.putExtra("View", viewFrom);
		archiveIntent.putExtra("interest", new ItemWrapper(transfer));
		archiveIntent.putExtra("others", new ItemWrapper(otherItems));
		startActivity(archiveIntent);
		finish();
	}
	
	public void doNegativeClick(){
		
	}

	//DialogFragment
	//Code derived from Android Developer website
	//http://developer.android.com/guide/topics/ui/dialogs.html
	public static class ConfirmationFragment extends DialogFragment {
	    
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState){
			
			final String displayMessage = getArguments().getString("message");
			final String displayID = getArguments().getString("ID");
			
			setCancelable(false);
			
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage(displayMessage)
	               .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   ((EditMode)getActivity()).doPositiveClick(displayID);
	                   }
	               })
	               .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   ((EditMode)getActivity()).doNegativeClick();
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
		}
		
		public static ConfirmationFragment newInstance(String displayMessage, String displayID){
			ConfirmationFragment confirmationFragment = new ConfirmationFragment();
			
			Bundle args = new Bundle();
			args.putString("message", displayMessage);			
			args.putString("ID", displayID);
			confirmationFragment.setArguments(args);
			
			return confirmationFragment;
		}	
		
	}
}
