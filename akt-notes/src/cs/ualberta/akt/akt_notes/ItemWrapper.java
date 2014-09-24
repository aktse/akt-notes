package cs.ualberta.akt.akt_notes;

import java.io.Serializable;
import java.util.ArrayList;

//Object used to wrap an ArrayList for transferring between activities
//It seemed easier to pass an object than an ArrayList

public class ItemWrapper implements Serializable{

	private static final long serialVersionUID = 1649888347434622256L;
	
	private ArrayList<ToDoItem> toDoItems;
	
	//Initialized when passing ArrayList to new activity
	public ItemWrapper(ArrayList<ToDoItem> arrayItems) {
		this.toDoItems = arrayItems;
	}
	
	//Retrieve ArrayList once in new activity
	public ArrayList<ToDoItem> getArray(){
		return this.toDoItems;
	}
	
}
