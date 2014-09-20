package cs.ualberta.akt.akt_notes;

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

import cs.ualberta.akt.akt_notes.data.ItemManager;



public class MainActivity extends Activity {

	private ItemManager itemManager;
	
	private ArrayList<ToDoItem> toDoItems;
	
	private ArrayAdapter<ToDoItem> toDoItemsViewAdapter;
	
	private ListView itemList;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        itemList = (ListView) findViewById(R.id.itemsList);
    }

    protected void onStart(){
    	super.onStart();
    	
    	itemManager = new ItemManager();
    	toDoItems = itemManager.loadItems();
    	ToDoItem helloWorld = new ToDoItem("Hello World");
    	toDoItems.add(helloWorld);
    	itemManager.saveItems(toDoItems);
    	
    	toDoItems.clear();
    	toDoItems = itemManager.loadItems();
    	toDoItemsViewAdapter = new ArrayAdapter<ToDoItem>(this, R.layout.list_item, toDoItems);
    	itemList.setAdapter(toDoItemsViewAdapter);
    	
    	toDoItemsViewAdapter.notifyDataSetChanged();
    	
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
        itemManager.saveItems(toDoItems);
        if (id == R.id.action_new) {
        	//Toast.makeText(this, "new has been selected", Toast.LENGTH_SHORT).show();
        	Intent newIntent = new Intent(this, NewItem.class);
        	startActivity(newIntent);
            return true;
        } else if (id == R.id.action_edit){
        	Toast.makeText(this,"edit has been selected", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
