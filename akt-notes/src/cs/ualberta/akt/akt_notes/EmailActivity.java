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

public class EmailActivity extends FragmentActivity {
	
	private ArrayList<ToDoItem> toDoItems;
	private ArrayList<ToDoItem> archivedItems;
	private ArrayList<ToDoItem> transfer;
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
		
		itemList = (ListView) findViewById(R.id.email_item);
		archivedList = (ListView) findViewById(R.id.email_archive);
		
		ItemWrapper iw = (ItemWrapper)getIntent().getSerializableExtra("items");
		transfer = iw.getArray();
		toDoItems = transfer;
		
		ItemWrapper iw_archive = (ItemWrapper)getIntent().getSerializableExtra("archived");
		archivedItems = iw_archive.getArray();
		
		final SelectingCustomArrayAdapter toDoItemsViewAdapter = new SelectingCustomArrayAdapter(this, toDoItems);
		itemList.setAdapter(toDoItemsViewAdapter);
		
		final SelectingCustomArrayAdapter archivedItemsViewAdapter = new SelectingCustomArrayAdapter(this, archivedItems);
		archivedList.setAdapter(archivedItemsViewAdapter);
		
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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_done_email) {
			if (selected.isEmpty()){
				Toast.makeText(this, "No items have been selected", Toast.LENGTH_SHORT).show();
			} else {
				ConfirmationFragment confirmationFragment = ConfirmationFragment.newInstance(selected.size() + " item(s) to be emailed");
				confirmationFragment.show(getSupportFragmentManager(),"email");
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void doPositiveClick(){
		String emailContents = "Here are your items: \n\n";
		int j = 1;
		for (int i = 0; i < selected.size(); i++){
			emailContents = emailContents + j + ". " + selected.get(i).getToDoItem() + "\n";
			j++;
		}
		
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("message/rfc822");
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your To Do Items");
		emailIntent.putExtra(Intent.EXTRA_TEXT   , emailContents);
		try {
		    startActivity(Intent.createChooser(emailIntent, "Sending mail"));
		    finish();
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
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
	        // Create the AlertDialog object and return it
	        return builder.create();
		}
		
		public static ConfirmationFragment newInstance(String displayMessage){
			ConfirmationFragment confirmationFragment = new ConfirmationFragment();
			
			Bundle args = new Bundle();
			args.putString("message", displayMessage);			
			confirmationFragment.setArguments(args);
			
			return confirmationFragment;
		}	
		
	}
}
