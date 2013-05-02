package com.idi.arau;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class UserActivity extends ListActivity {
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		defineActionBar();
		
		List<String> values = getListOfItems();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.row_layout, R.id.score, values);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.user_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){        	
        	case android.R.id.home:
                Intent intent = new Intent(this, Words.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
		}
	}
	
	private void defineActionBar() {
		ActionBar bar = getActionBar();
		bar.setHomeButtonEnabled(true);
		bar.setTitle("Users");
	}
	
	private List<String> getListOfItems() {
		UserController controller = UserController.getInstance(this);
		Map<String, Integer> userScore = controller.getRanking();
		List<String> values = new ArrayList<String>();
		int index = 1;
		for (String key : userScore.keySet()) {
			values.add(index + "  " + key + "\t\t\t\t" + userScore.get(key) + " points");
			index++;
		}
		return values;
	}
} 