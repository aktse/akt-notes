package cs.ualberta.akt.akt_notes;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class NewItem extends Activity {

	public final static String EXTRA_MESSAGE = "cs.ualberta.akt.akt_notes.newItem";	
		
	private ArrayList<ToDoItem> toDoItems;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_item);
	}

    protected void onStart(){
    	super.onStart();
    	
    	//Gets the ArrayList passed from MainActivity
    	DataWrapper dw = (DataWrapper)getIntent().getSerializableExtra("items");
    	toDoItems = dw.getArray();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_item, menu);
		return true;
	}

	/*
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		return super.onOptionsItemSelected(item);
	}*/
	
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
		addIntent.putExtra("items", new DataWrapper(toDoItems)); //ArrayList wrapped in an object
		startActivity(addIntent);
	}
}
