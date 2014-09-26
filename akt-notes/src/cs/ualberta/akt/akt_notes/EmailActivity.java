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

//Activity used to send an email
//User can select any number of items > 0 and then email them
//Implements a DialogFragment to confirm user selection
//Contains both the active "To Do" items as well as the archived ones separated with a <TextView>
//selected is used to keep track of which items the user has selected

public class EmailActivity extends FragmentActivity {
	
	private ArrayList<ToDoItem> toDoItems;
	private ArrayList<ToDoItem> archivedItems;
	private ArrayList<ToDoItem> selected = new ArrayList<ToDoItem>();
	
	private ListView itemList;
	private ListView archivedList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle("Email Items");
		
		//Acquires the ListViews to display the items
		itemList = (ListView) findViewById(R.id.email_item);
		archivedList = (ListView) findViewById(R.id.email_archive);
		
		//ArrayList of active "To Do" items
		ItemWrapper iw = (ItemWrapper)getIntent().getSerializableExtra("items");
		toDoItems = iw.getArray();		
		final SelectingCustomArrayAdapter toDoItemsViewAdapter = new SelectingCustomArrayAdapter(this, toDoItems);
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
		
		//ArrayList of archived "To Do" items
		ItemWrapper iw_archive = (ItemWrapper)getIntent().getSerializableExtra("archived");
		archivedItems = iw_archive.getArray();
		final SelectingCustomArrayAdapter archivedItemsViewAdapter = new SelectingCustomArrayAdapter(this, archivedItems);
		archivedList.setAdapter(archivedItemsViewAdapter);
    	
    	//Assigns ItemClickListener to ListView to toggle check box on row click
    	archivedList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ToDoItem archivedItem = archivedItemsViewAdapter.getItem(position);
				archivedItem.toggleSelected();
				if (!selected.contains(archivedItem)){
					selected.add(archivedItem);
				} else {
					selected.remove(archivedItem);
				}
				archivedItemsViewAdapter.notifyDataSetChanged();
			}
    	});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.email, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		
		//Only button, allows user to confirm his selection
		if (id == R.id.action_done_email) {
			if (selected.isEmpty()){
				Toast.makeText(this, "No items have been selected", Toast.LENGTH_SHORT).show();
			} 
			//Creates a DialogFragment to confirm selection before initializing Intent
			else {
				ConfirmationFragment confirmationFragment = ConfirmationFragment.newInstance(selected.size() + " item(s) to be emailed");
				confirmationFragment.show(getSupportFragmentManager(),"email");
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	//Called when user confirms selection for emailing
	public void doPositiveClick(){
		
		//Constructs body message
		String emailContents = "Here are your items: \n\n";
		int j = 1;
		for (int i = 0; i < selected.size(); i++){
			emailContents = emailContents + j + ". " + selected.get(i).getToDoItem() + "\n";
			j++;
		}
		
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("message/rfc822");
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your To Do Items");
		emailIntent.putExtra(Intent.EXTRA_TEXT, emailContents);
		try {
		    startActivity(Intent.createChooser(emailIntent, "Preparing to send email"));
		    finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doNegativeClick(){
		//User clicked cancel
	}
	
	//Displays a DialogFragment to confirm the selection with user
	//Android Developer web site was referenced in developing this section of code
	//http://developer.android.com/guide/topics/ui/dialogs.html
	public static class ConfirmationFragment extends DialogFragment {
	    
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState){
			
			final String displayMessage = getArguments().getString("message");
			
			setCancelable(false);
			
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage(displayMessage)
	               .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   ((EmailActivity)getActivity()).doPositiveClick();
	                   }
	               })
	               .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   ((EmailActivity)getActivity()).doNegativeClick();
	                   }
	               });
	        return builder.create();
		}
		
		//Called to create new instance to allow for passing of dynamically generated message
		public static ConfirmationFragment newInstance(String displayMessage){
			ConfirmationFragment confirmationFragment = new ConfirmationFragment();
			
			Bundle args = new Bundle();
			args.putString("message", displayMessage);			
			confirmationFragment.setArguments(args);
			
			return confirmationFragment;
		}	
		
	}
}
