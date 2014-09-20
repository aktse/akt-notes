package cs.ualberta.akt.akt_notes;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
	
	public void addItem(View view){
		Intent addIntent = new Intent(this, MainActivity.class);
		
		EditText newEditText = (EditText) findViewById(R.id.newEditText);
		String newItem = newEditText.getText().toString();
		
		ToDoItem toDoItem = new ToDoItem(newItem);
		toDoItems.add(toDoItem);
		
		addIntent.setType("other");
		addIntent.putExtra("uniqueID", "newItem");
		addIntent.putExtra("items", new DataWrapper(toDoItems));
		startActivity(addIntent);
	}
}
