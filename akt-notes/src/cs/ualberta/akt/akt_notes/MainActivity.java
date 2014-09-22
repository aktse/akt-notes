package cs.ualberta.akt.akt_notes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import cs.ualberta.akt.akt_notes.adapters.ArchivedArrayAdapter;
import cs.ualberta.akt.akt_notes.adapters.CustomArrayAdapter;

import android.app.ActionBar.Tab;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;



public class MainActivity extends FragmentActivity implements ActionBar.TabListener{
	
	private static ArrayList<ToDoItem> toDoItems;
	private static ArrayList<ToDoItem> archivedItems;
	
	private ViewPager viewPager;
	private TabsPagerAdapter tabsPagerAdapter;
	private String[] titles = {"To Do List", "Archive"};
	
	private ActionBar actionBar;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsPagerAdapter);
        
        actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        for (String title : titles) {
        	actionBar.addTab(actionBar.newTab().setText(title).setTabListener(this));
        }
        
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
				viewPager.getAdapter().notifyDataSetChanged();
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub		
			}
		});
        
    	Intent intent = this.getIntent();
    	//If this activity was called for the first time (i.e launch) attempts to load data from internal storage
    	if (intent.getType() == null){
    		toDoItems = new ArrayList<ToDoItem>();
    		archivedItems = new ArrayList<ToDoItem>();
    		
        	File file = new File(this.getFilesDir(),"file.txt");
        	//Checks to see if directory exists
        	if (!file.exists()){
        		file.mkdirs();
        	}
        	
        	//Creates a file in the directory if file doesn't already exist
        	File myItems = new File(file, "myItems.txt");
        	File myFile = new File(file, "archivedItems.txt");
        	//Loads the object ArrayList
    		try {
    			FileInputStream fin = new FileInputStream(myItems);
    			ObjectInputStream oin = new ObjectInputStream(fin);
    			toDoItems = (ArrayList<ToDoItem>) oin.readObject();
    			fin.close();
    			oin.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		
    		try {
    			FileInputStream fin = new FileInputStream(myFile);
    			ObjectInputStream oin = new ObjectInputStream(fin);
    			archivedItems = (ArrayList<ToDoItem>) oin.readObject();
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
    	File myItems = new File(file, "myItems.txt");
    	File myFile = new File(file, "archivedItems.txt");
    	
    	//Saves the array to internal storage
    	try {
			FileOutputStream fout = new FileOutputStream(myItems);
			ObjectOutputStream oout = new ObjectOutputStream(fout);
			oout.writeObject(toDoItems);
			fout.close();
			oout.close();
		} catch (Exception e){
			e.printStackTrace();
		}
    	
    	try {
    		FileOutputStream fout = new FileOutputStream(myFile);
    		ObjectOutputStream oout = new ObjectOutputStream(fout);
    		oout.writeObject(archivedItems);
    		fout.close();
    		oout.close();
    	} catch (Exception e){
    		e.printStackTrace();
    	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.to_do_list, menu);
        return true;
    }
    
    public void onPageSelected(int pageNum){
    	invalidateOptionsMenu();
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
        	Intent editIntent = new Intent(this,EditMode.class);
        	editIntent.putExtra("items", new DataWrapper(toDoItems));
        	startActivity(editIntent);
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
			viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	public class TabsPagerAdapter extends FragmentPagerAdapter{

		public TabsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position){
			
			if (position == 0){
				//ToDoList
				return new ToDoListFragment();
			} else if (position == 1){
				//Archive
				return new ArchiveFragment();
			}
			
			return null;
		}
		
		@Override
		public int getItemPosition(Object object){
			return POSITION_NONE;
		}

		@Override
		public int getCount() {
			return 2;
		}
		
		
	}
	
	public static class ToDoListFragment extends ListFragment{
		
		public CustomArrayAdapter toDoItemsViewAdapter = null;
		
		@Override
		public void onListItemClick(ListView l, View v, int position, long i){
			
			ToDoItem toDoItem = toDoItemsViewAdapter.getItem(position);
			toDoItem.toggleCheckedOff();
			toDoItemsViewAdapter.notifyDataSetChanged();
			
		}
		
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
			inflater.inflate(R.menu.to_do_list, menu);
			super.onCreateOptionsMenu(menu, inflater);	
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			
	    	//Assigns CustomArrayAdapter to ListView
	    	this.toDoItemsViewAdapter = new CustomArrayAdapter(getActivity(), toDoItems);
	    	setListAdapter(toDoItemsViewAdapter);
	    		
			return inflater.inflate(R.layout.fragment_main, container, false);
		}
		
	}
	
	public static class ArchiveFragment extends ListFragment{
		
		public ArchivedArrayAdapter archivedItemsViewAdapter = null;
		
		@Override
		public void onListItemClick(ListView l, View v, int position, long i){
			
			ToDoItem archivedItem = archivedItemsViewAdapter.getItem(position);
			archivedItem.toggleCheckedOff();
			archivedItemsViewAdapter.notifyDataSetChanged();
			
		}
		
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
			inflater.inflate(R.menu.archive_list, menu);
			super.onCreateOptionsMenu(menu, inflater);
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancedState) {
			
			this.archivedItemsViewAdapter = new ArchivedArrayAdapter(getActivity(), archivedItems);
			setListAdapter(archivedItemsViewAdapter);
			archivedItemsViewAdapter.notifyDataSetChanged();
				
			return inflater.inflate(R.layout.fragment_main, container, false);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
