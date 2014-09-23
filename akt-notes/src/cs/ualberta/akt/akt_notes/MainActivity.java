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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import android.widget.TextView;
import android.widget.Toast;

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
        
        //Swipe views using tabs
        //Code derived from Android Developer website
        //http://developer.android.com/training/implementing-navigation/lateral.html
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
        		ItemWrapper iw = (ItemWrapper)intent.getSerializableExtra("items");
            	toDoItems = iw.getArray();
        	} else if (uniqueID.equals("editItem")){
        		String viewFrom = intent.getStringExtra("View");
        		if (viewFrom.equals("toDoList")){
        			ItemWrapper iw = (ItemWrapper)intent.getSerializableExtra("interest");
        			toDoItems = iw.getArray();
        		
        			ItemWrapper iw_archive = (ItemWrapper)intent.getSerializableExtra("others");
        			archivedItems = iw_archive.getArray();
        			
        		} else if (viewFrom.equals("archive")) {
        			ItemWrapper iw = (ItemWrapper)intent.getSerializableExtra("interest");
        			archivedItems = iw.getArray();
        			
        			ItemWrapper iw_archive = (ItemWrapper)intent.getSerializableExtra("others");
        			toDoItems = iw_archive.getArray();
        		}
        	}
    	}	
    }

    protected void onStart(){
    	super.onStart();
    	    	
    }
    
    //General fragment structure derived from Android Developer website
    //http://developer.android.com/training/implementing-navigation/lateral.html

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
        return true;
    }
    
    public void onPageSelected(int pageNum){
    	invalidateOptionsMenu();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
		public boolean onOptionsItemSelected(MenuItem item){ 
	        int id = item.getItemId();
	        
	        //Prepares the activity to transition to the add new item activity
	        //Passes the ArrayList of items in a data wrapper 
	        if (id == R.id.action_new) {
	        	Intent newIntent = new Intent(getActivity(), NewItem.class);
	        	newIntent.putExtra("items", new ItemWrapper(toDoItems));
	        	startActivity(newIntent);
	            return true;
	        }
	        
	        else if (id == R.id.action_email){
	        	Toast.makeText(getActivity(), "email", Toast.LENGTH_LONG).show();
	        }
	        
	        //Prepares the activity to transition to the edit mode activity
	        else if (id == R.id.action_edit){
	        	Intent editIntent = new Intent(getActivity(),EditMode.class);
	        	editIntent.putExtra("interest", new ItemWrapper(toDoItems));
	        	editIntent.putExtra("others", new ItemWrapper(archivedItems));
	        	editIntent.putExtra("View", "toDoList");
	        	startActivity(editIntent);
	        }
	        
	        else if (id == R.id.action_summary){
	        	int complete = getComplete(toDoItems);
	        	int incomplete = toDoItems.size() - complete;
	        	int archived_complete = getComplete(archivedItems);
	        	int archived_incomplete = archivedItems.size() - archived_complete;
	        	
	        	SummarizeFragment summarizeFragment = SummarizeFragment.newInstance(incomplete, complete,
	        			archived_incomplete, archived_complete, toDoItems.size(), archivedItems.size());
	        	summarizeFragment.show(getFragmentManager(),"archive");
	        }
	        
			return super.onOptionsItemSelected(item);
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
		
		public int getComplete(ArrayList<ToDoItem> items){
			int size = 0;
			for (int i = 0; i < items.size(); i++){
				if (items.get(i).getCheckedOff()){
					size++;
				}
			}
			return size;
		}
	}
	
	//Same as above
	//http://developer.android.com/training/implementing-navigation/lateral.html
	
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
		public boolean onOptionsItemSelected(MenuItem item){ 
	        int id = item.getItemId();
	        
	        //Prepares the activity to transition to the add new item activity
	        //Passes the ArrayList of items in a data wrapper 
	        if (id == R.id.action_email) {
	        	//Intent newIntent = new Intent(getActivity(), NewItem.class);
	        	//newIntent.putExtra("items", new DataWrapper(toDoItems));
	        	//startActivity(newIntent);
	        	Toast.makeText(getActivity(),"action_email",Toast.LENGTH_LONG).show();
	            return true;
	        } 
	        //Prepares the activity to transition to the edit mode activity
	        else if (id == R.id.action_archive_edit){
	        	Intent editIntent = new Intent(getActivity(),EditMode.class);
	        	editIntent.putExtra("interest", new ItemWrapper(archivedItems));
	        	editIntent.putExtra("others", new ItemWrapper(toDoItems));
	        	editIntent.putExtra("View", "archive");
	        	startActivity(editIntent);
	        	
	        } else if (id == R.id.action_summary){
	        	int complete = getComplete(toDoItems);
	        	int incomplete = toDoItems.size() - complete;
	        	int archived_complete = getComplete(archivedItems);
	        	int archived_incomplete = archivedItems.size() - archived_complete;
	        	
	        	SummarizeFragment summarizeFragment = SummarizeFragment.newInstance(incomplete, complete, 
	        			archived_incomplete, archived_complete, toDoItems.size(), archivedItems.size());
	        	summarizeFragment.show(getFragmentManager(),"archive");
	        }
			return super.onOptionsItemSelected(item);
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
	
		public int getComplete(ArrayList<ToDoItem> items){
			int size = 0;
			for (int i = 0; i < items.size(); i++){
				if (items.get(i).getCheckedOff()){
					size++;
				}
			}
			return size;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static class SummarizeFragment extends DialogFragment{
		
		private TextView total_tv;
		private TextView complete_tv;
		private TextView incomplete_tv;
		private TextView archived_ttv;
		private TextView archived_ctv;
		private TextView archived_itv;
		
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState){
			
			int total = getArguments().getInt("total");
			int incomplete = getArguments().getInt("incomplete");
			int complete = getArguments().getInt("complete");
			int archived_total = getArguments().getInt("archived_total");
			int archived_incomplete = getArguments().getInt("archived_incomplete");
			int archived_complete = getArguments().getInt("archived_complete");
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			View inflater = getActivity().getLayoutInflater().inflate(R.layout.summary_dialog, null);
			
			setCancelable(false);
			
			builder.setTitle("Summary of Items");
			builder.setView(inflater).setNeutralButton("Okay", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
			
			total_tv = (TextView)inflater.findViewById(R.id.total);
			total_tv.setText(total + " Total To Do items.");
			
			complete_tv = (TextView)inflater.findViewById(R.id.complete);
			complete_tv.setText(complete + " Complete items.");
			
			incomplete_tv = (TextView)inflater.findViewById(R.id.incomplete);
			incomplete_tv.setText(incomplete + " Incomplete items.");
			
			archived_ttv = (TextView) inflater.findViewById(R.id.archived_total);
			archived_ttv.setText(archived_total + " Total archived items.");
			
			archived_ctv = (TextView)inflater.findViewById(R.id.archived_complete);
			archived_ctv.setText(archived_complete + " Incomplete, archived items.");
			
			archived_itv = (TextView)inflater.findViewById(R.id.archived_incomplete);
			archived_itv.setText(archived_incomplete + " Complete, archived items.");

			return builder.create();
		}
		
		
		public static SummarizeFragment newInstance(int incomplete, int complete, int archived_incomplete, 
				int archived_complete, int total, int archived_total){
			SummarizeFragment summarizeFragment = new SummarizeFragment();
			
			Bundle args = new Bundle();
			args.putInt("total", total);
			args.putInt("incomplete", incomplete);
			args.putInt("complete", complete);
			args.putInt("archived_total", archived_total);
			args.putInt("archived_incomplete", archived_incomplete);
			args.putInt("archived_complete", archived_complete);
			summarizeFragment.setArguments(args);
			
			return summarizeFragment;
		}
	}
	
	
	
	
	
	
	
}
