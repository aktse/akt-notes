package cs.ualberta.akt.akt_notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class NewItem extends Activity {

	public final static String EXTRA_MESSAGE = "cs.ualberta.akt.akt_notes.newItem";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		return super.onOptionsItemSelected(item);
	}
	
	public void addItem(View view){
		Intent addIntent = new Intent(this, MainActivity.class);
		EditText newEditText = (EditText) findViewById(R.id.newEditText);
		String newItem = newEditText.getText().toString();
		addIntent.putExtra(EXTRA_MESSAGE, newItem);
		startActivity(addIntent);
	}
}
