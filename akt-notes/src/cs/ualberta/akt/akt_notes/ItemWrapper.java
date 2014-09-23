package cs.ualberta.akt.akt_notes;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemWrapper implements Serializable{

	private static final long serialVersionUID = 1649888347434622256L;
	
	private ArrayList<ToDoItem> toDoItems;
	
	public ItemWrapper(ArrayList<ToDoItem> arrayItems) {
		this.toDoItems = arrayItems;
	}
	
	public ArrayList<ToDoItem> getArray(){
		return this.toDoItems;
	}
	
}
