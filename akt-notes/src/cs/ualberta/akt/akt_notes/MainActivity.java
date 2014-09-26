package cs.ualberta.akt.akt_notes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

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

//MainActivity contains the bulk of the logic
//Contains 2 separate ArrayLists, 1 for active items and 1 for archived items
//Implementation of a swipe view with tabs
//Each ArrayList has its own ListFragment with its own action bar options
//Implements a DialogFragment to summarize number of archived/unarchived items and whether they are checked off or not
//onCreate implements loading from file -> may generate error on first load because file doesn't exist but it doesn't happen every time
//onPause implements saving to file because it will be called no matter what if the app is closed

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
        //Android Developer web site was referenced in developing this section of code
        //http://developer.android.com/training/implementing-navigation/lateral.html
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsPagerAdapter);
        
        actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        for (String title : titles) {
        	actionBar.addTab(actionBar.newTab().setText(title).setTabListener(this));
        }
        
        //Handles changing of pages on manual click
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
    	
    	//If this activity was called for the first time (i.e launch), will attempt to load data from internal storage
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
        	//Loads active objects into ArrayList
    		try {
    			FileInputStream fin = new FileInputStream(myItems);
    			ObjectInputStream oin = new ObjectInputStream(fin);
    			toDoItems = (ArrayList<ToDoItem>) oin.readObject();
    			fin.close();
    			oin.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		
    		//Loads archived objects into ArrayList
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
        			
        			viewPager.setCurrentItem(1); //Used to return back to Archive screen
        		}
        	}
    	}	
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
    	
    	//Saves active items ArrayList to internal storage
    	try {
			FileOutputStream fout = new FileOutputStream(myItems);
			ObjectOutputStream oout = new ObjectOutputStream(fout);
			oout.writeObject(toDoItems);
			fout.close();
			oout.close();
		} catch (Exception e){
			e.printStackTrace();
		}
    	
    	//Saves archived items ArrayList to internal storage
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
	
	
	//Class that handles creation of fragments and assigning them to the tab being selected
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
	
    //Fragment used to display toDoItems ArrayList
	//Implements its own options in ActionBar and handles onClick events
    //Android Developer web site was referenced in developing this section of code
    //http://developer.android.com/training/implementing-navigation/lateral.html
	public static class ToDoListFragment extends ListFragment{
		
		public CustomArrayAdapter toDoItemsViewAdapter = null;
		
		//Handles displaying items with a check box
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
	        
	        //Prepares the activity to transition to activity specified by button pressed
	        //Passes the ArrayList of items in a data wrapper 
	        if (id == R.id.action_new) {
	        	Intent newIntent = new Intent(getActivity(), NewItem.class);
	        	newIntent.putExtra("items", new ItemWrapper(toDoItems));
	        	startActivity(newIntent);
	            return true;
	        } else if (id == R.id.action_email){
	        	Intent emailIntent = new Intent(getActivity(), EmailActivity.class);
	        	emailIntent.putExtra("items", new ItemWrapper(toDoItems));
	        	emailIntent.putExtra("archived", new ItemWrapper(archivedItems));
	        	startActivity(emailIntent);
	        } else if (id == R.id.action_edit){
	        	Intent editIntent = new Intent(getActivity(),EditMode.class);
	        	editIntent.putExtra("interest", new ItemWrapper(toDoItems));
	        	editIntent.putExtra("others", new ItemWrapper(archivedItems));
	        	editIntent.putExtra("View", "toDoList");
	        	startActivity(editIntent);
	        }
	        
	        //Creates a DialogFragment to display a summary of the number of items the user has
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
		
		//Used to calculate the number of checked off items
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
	
	//Nearly identical to above fragment, contains 1 less menu option
	//Android Developer web site was referenced in developing this section of code
	//http://developer.android.com/training/implementing-navigation/lateral.html
	
	public static class ArchiveFragment extends ListFragment{
		
		public CustomArrayAdapter archivedItemsViewAdapter = null;
		
		//Handles displaying items with a check box
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
	        
	        //Prepares the activity to transition to activity specified by button pressed
	        //Passes the ArrayList of items in a data wrapper 
	        if (id == R.id.action_email) {
	        	Intent emailIntent = new Intent(getActivity(), EmailActivity.class);
	        	emailIntent.putExtra("items", new ItemWrapper(toDoItems));
	        	emailIntent.putExtra("archived", new ItemWrapper(archivedItems));
	        	startActivity(emailIntent);
	            return true;
	        } else if (id == R.id.action_archive_edit){
	        	Intent editIntent = new Intent(getActivity(),EditMode.class);
	        	editIntent.putExtra("interest", new ItemWrapper(archivedItems));
	        	editIntent.putExtra("others", new ItemWrapper(toDoItems));
	        	editIntent.putExtra("View", "archive");
	        	startActivity(editIntent);
	        } 
	        //Creates a DialogFragment to display a summary of the number of items the user has
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
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancedState) {
			
	    	//Assigns CustomArrayAdapter to ListView
			this.archivedItemsViewAdapter = new CustomArrayAdapter(getActivity(), archivedItems);
			setListAdapter(archivedItemsViewAdapter);
			archivedItemsViewAdapter.notifyDataSetChanged();
				
			return inflater.inflate(R.layout.fragment_main, container, false);
		}
	
		//Used to calculate the number of checked off items
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
	
	//DialogFragment for creating a pop-up to summarize number of archived/unarchived items
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
			
			//Sets number of items in the TextViews of layout
			total_tv = (TextView)inflater.findViewById(R.id.total);
			total_tv.setText(total + " Total To Do Items.");
			
			complete_tv = (TextView)inflater.findViewById(R.id.complete);
			complete_tv.setText(complete + " Complete");
			
			incomplete_tv = (TextView)inflater.findViewById(R.id.incomplete);
			incomplete_tv.setText(incomplete + " Incomplete");
			
			archived_ttv = (TextView) inflater.findViewById(R.id.archived_total);
			archived_ttv.setText(archived_total + " Total Archived Items.");
			
			archived_ctv = (TextView)inflater.findViewById(R.id.archived_complete);
			archived_ctv.setText(archived_complete + " Complete");
			
			archived_itv = (TextView)inflater.findViewById(R.id.archived_incomplete);
			archived_itv.setText(archived_incomplete + " Incomplete");

			return builder.create();
		}
		
		//Used to pass the number of items to fragment
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
