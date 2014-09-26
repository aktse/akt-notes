package cs.ualberta.akt.akt_notes;

import java.io.Serializable;

//Object that is being used to represent the "To Do" items the user wishes to interact with

public class ToDoItem implements Serializable {

	private static final long serialVersionUID = 3234955922865625246L;
	
	private String toDoItem;
	private Boolean checkedOff;
	private Boolean archived;
	private Boolean selected;
	
	//Initializes with string from user input
	public ToDoItem(String toDoItem) {
		super();
		this.toDoItem = toDoItem;
		this.checkedOff = false;
		this.archived = false;
		this.selected = false;
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
	
	public Boolean getSelected(){
		return selected;
	}
	
	public void toggleSelected(){
		this.selected = !this.selected;
	}
	
	public void setSelected(){
		this.selected = false;
	}
	
}
