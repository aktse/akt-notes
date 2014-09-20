package cs.ualberta.akt.akt_notes.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import cs.ualberta.akt.akt_notes.ToDoItem;

public class ItemManager {

	private static final String FILENAME = "file.txt";
	
	public ItemManager(){
		
	}
		
	public ArrayList<ToDoItem> loadItems(){
		ArrayList<ToDoItem> tdl = new ArrayList<ToDoItem>();
	
		try {
			FileInputStream fis = new FileInputStream(FILENAME);
			ObjectInputStream ois = new ObjectInputStream(fis);
			tdl = (ArrayList<ToDoItem>) ois.readObject();
			fis.close();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tdl;
	}
	
	public void saveItems(ArrayList<ToDoItem> tdl) {
		
		try {
			FileOutputStream fos = new FileOutputStream(FILENAME);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(tdl);
			fos.close();
			oos.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
}

