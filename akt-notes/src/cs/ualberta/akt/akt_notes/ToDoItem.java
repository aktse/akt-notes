package cs.ualberta.akt.akt_notes;

import java.io.Serializable;

public class ToDoItem implements Serializable {
	
	public String toDoItem;
	public Boolean checkedOff;
	public Boolean archived;
	
	public ToDoItem(String toDoItem) {
		super();
		this.toDoItem = toDoItem;
		this.checkedOff = false;
		this.archived = false;
	}

	public String getToDoItem() {
		return toDoItem;
	}
	
	public void setToDoItem(String toDoItem) {
		this.toDoItem = toDoItem;
	}
	
	public Boolean getCheckedOff() {
		return checkedOff;
	}

	public void toggleCheckedOff(){
		this.checkedOff= !this.checkedOff;
	}
	
	public Boolean getArchived(){
		return archived;
	}
	
	public void toggleArchived(){
		this.archived = !this.archived;
	}
	
	public String toString() {
		return toDoItem;
	}
}
