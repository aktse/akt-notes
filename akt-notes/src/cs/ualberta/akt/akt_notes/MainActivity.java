package cs.ualberta.akt.akt_notes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Build;



public class MainActivity extends Activity {

	//private ItemManager itemManager;
	
	private ArrayList<ToDoItem> toDoItems;
	
	private ArrayAdapter<ToDoItem> toDoItemsViewAdapter;
	
	private ListView itemList;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        itemList = (ListView) findViewById(R.id.itemsList);
    	
    	Intent intent = this.getIntent();
    	if (intent.getType() == null){
    		toDoItems = new ArrayList<ToDoItem>();	
    		
        	File file = new File(this.getFilesDir(),"file.txt");
        	if (!file.exists()){
        		file.mkdirs();
        	}
        	
        	File myFile = new File(file, "myfile.txt");
        	
    		try {
    			FileInputStream fin = new FileInputStream(myFile);
    			ObjectInputStream oin = new ObjectInputStream(fin);
    			toDoItems = (ArrayList<ToDoItem>) oin.readObject();
    			fin.close();
    			oin.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		
    	} else {
        	String uniqueID = intent.getStringExtra("uniqueID");
        	
        	if (uniqueID.equals("newItem")){
        		DataWrapper dw = (DataWrapper)intent.getSerializableExtra("items");
            	toDoItems = dw.getArray();
        	}	
 		
    	}
    	toDoItemsViewAdapter = new ArrayAdapter<ToDoItem>(this, R.layout.list_item, toDoItems);
    	itemList.setAdapter(toDoItemsViewAdapter);
    }

    protected void onStart(){
    	super.onStart();
    	
    	    	
    }
    /*
    protected void onResume(){
    	super.onResume();
    	
    	toDoItems = itemManager.loadItems();
    	ToDoItem byeWorld = new ToDoItem("Bye World");
    	toDoItems.add(byeWorld);
    	toDoItemsViewAdapter.notifyDataSetChanged();
    	
    }
    */
    
    protected void onPause(){
    	super.onPause();
    	//save stuff here
    	
    	File file = new File(this.getFilesDir(),"file.txt");
    	if (!file.exists()){
    		file.mkdirs();
    	}
    	
    	File myFile = new File(file, "myfile.txt");
    	
    	try {
			FileOutputStream fout = new FileOutputStream(myFile);
			ObjectOutputStream oout = new ObjectOutputStream(fout);
			oout.writeObject(toDoItems);
			fout.close();
			oout.close();
		} catch (Exception e){
			e.printStackTrace();
		}
    	//itemManager.saveItems(toDoItems);
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
        if (id == R.id.action_new) {
        	//Toast.makeText(this, "new has been selected", Toast.LENGTH_SHORT).show();
        	Intent newIntent = new Intent(this, NewItem.class);
        	newIntent.putExtra("items", new DataWrapper(toDoItems));
        	startActivity(newIntent);
            return true;
        } else if (id == R.id.action_edit){
        	Toast.makeText(this,"edit has been selected", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
