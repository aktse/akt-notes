package cs.ualberta.akt.akt_notes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;



public class MainActivity extends Activity {
	
	private ArrayList<ToDoItem> toDoItems;
		
	private ListView itemList;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        itemList = (ListView) findViewById(R.id.itemsList);
    	
    	Intent intent = this.getIntent();
    	//If this activity was called for the first time (i.e launch) attempts to load data from internal storage
    	if (intent.getType() == null){
    		toDoItems = new ArrayList<ToDoItem>();	
    		
        	File file = new File(this.getFilesDir(),"file.txt");
        	//Checks to see if directory exists
        	if (!file.exists()){
        		file.mkdirs();
        	}
        	
        	//Creates a file in the directory if file doesn't already exist
        	File myFile = new File(file, "myfile.txt");
        	
        	//Loads the object ArrayList
    		try {
    			FileInputStream fin = new FileInputStream(myFile);
    			ObjectInputStream oin = new ObjectInputStream(fin);
    			toDoItems = (ArrayList<ToDoItem>) oin.readObject();
    			fin.close();
    			oin.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		
    	} 
    	//Activity was called by an intent
    	else {
    		//Checks to see which intent called this activity 
        	String uniqueID = intent.getStringExtra("uniqueID");
        	
        	//Grabs the ArrayList from the data wrapper used to pass the ArrayList 
        	if (uniqueID.equals("newItem")){
        		DataWrapper dw = (DataWrapper)intent.getSerializableExtra("items");
            	toDoItems = dw.getArray();
        	}	
 		
    	}
    	
    	//Assigns CustomArrayAdapter to ListView
    	final CustomArrayAdapter toDoItemsViewAdapter = new CustomArrayAdapter(this, toDoItems);
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

    protected void onStart(){
    	super.onStart();
    	    	
    }

    protected void onPause(){
    	//Is called whenever the activity becomes invisible.
    	//Trivial while app is running but enables saving of data when the app is closed
    	super.onPause();
    	
    	//Checks to make sure directory exists
    	File file = new File(this.getFilesDir(),"file.txt");
    	if (!file.exists()){
    		file.mkdirs();
    	}
    	
    	//Creates file in the directory if file doesn't already exist
    	File myFile = new File(file, "myfile.txt");
    	
    	//Saves the array to internal storage
    	try {
			FileOutputStream fout = new FileOutputStream(myFile);
			ObjectOutputStream oout = new ObjectOutputStream(fout);
			oout.writeObject(toDoItems);
			fout.close();
			oout.close();
		} catch (Exception e){
			e.printStackTrace();
		}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        //Prepares the activity to transition to the add new item activity
        //Passes the ArrayList of items in a data wrapper 
        if (id == R.id.action_new) {
        	Intent newIntent = new Intent(this, NewItem.class);
        	newIntent.putExtra("items", new DataWrapper(toDoItems));
        	startActivity(newIntent);
            return true;
        } 
        //Prepares the activity to transition to the edit mode activity
        else if (id == R.id.action_edit){
        	Toast.makeText(this,"edit has been selected", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    
}
