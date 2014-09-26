package cs.ualberta.akt.akt_notes;

import java.util.ArrayList;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

//Activity used to handle the addition of new "To Do" items

public class NewItem extends Activity {
	
	private ArrayList<ToDoItem> toDoItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_item);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle("New Item");
		
	}

    protected void onStart(){
    	super.onStart();
    	
    	//Gets the ArrayList passed from MainActivity
    	ItemWrapper iw = (ItemWrapper)getIntent().getSerializableExtra("items");
    	toDoItems = iw.getArray();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_item, menu);
		return true;
	}
	
	//Called when the "Done" button is clicked
	public void addItem(View view){
		Intent addIntent = new Intent(this, MainActivity.class);
		
		//Acquires string in EditText field that the user typed in
		EditText newEditText = (EditText) findViewById(R.id.newEditText);
		String newItem = newEditText.getText().toString();
		
		//Creates an object whose name is the string in the text field
		ToDoItem toDoItem = new ToDoItem(newItem);
		toDoItems.add(toDoItem);
		
		//Passes user back to MainActivity
		addIntent.setType("other"); //Identifier used to identify useful intents to the intent used to launch app
		addIntent.putExtra("uniqueID", "newItem"); //Identifier used to identify the different intents
		addIntent.putExtra("items", new ItemWrapper(toDoItems)); //ArrayList wrapped in an object
		startActivity(addIntent);
		finish();
	}
}
