package com.idi.arau;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;


public class UserActivity extends ListActivity {
	
	private ActionMode mActionMode;
	static ArrayAdapter<String> adapter;
	static Map<String, Integer> values;
	static int position;
	
	
	SimpleCursorAdapter mAdapter;

	
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		defineActionBar();

		final String[] matrix  = { "_id", "position", "name", "value" };
		MatrixCursor  cursor = new MatrixCursor(matrix);
		
		final String[] columns = { "position", "name", "value" };
		final int[]    layouts = { R.id.position, R.id.user, R.id.score};
		
		getListOfItems();
		int index = 0;
		for (String user : values.keySet()) {
			cursor.addRow(new Object[] { index++, index, user, values.get(user) + " points"});
		}
		
		

		
		SimpleCursorAdapter data = new SimpleCursorAdapter(this, R.layout.row_layout, cursor, columns, layouts);

	    setListAdapter( data );
		
		

//		
//		
//		values = getListOfItems();
//		adapter = new ArrayAdapter<String>(this,
//				R.layout.row_layout, R.id.user, values);
//		setListAdapter(adapter);
		
		this.getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int index, long id) {
				
				position = index;
				if (mActionMode != null) {
					  return false;
				}
				// Start the CAB using the ActionMode.Callback defined above
				mActionMode = UserActivity.this.startActionMode(mActionModeCallback);				
				view.setSelected(true);
				return true;
			}
		});
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
	
	private void getListOfItems() {
		UserController controller = UserController.getInstance(this);
		values = controller.getRanking();
	}
	
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		// Called when the action mode is created; startActionMode() was called
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// Inflate a menu resource providing context menu items
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.context_useractivity_menu, menu);
			return true;
		}

		// Called each time the action mode is shown. Always called after onCreateActionMode, but
		// may be called multiple times if the mode is invalidated.
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false; // Return false if nothing is done
		}

		// Called when the user selects a contextual menu item
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.delete_user:
				
				deleteUser();
				mode.finish(); // close the CAB
				return true;
			default:
				return false;
			}
		}

		// Called when the user exits the action mode
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
		}
		
		private void deleteUser() {			
//			String[] item = values.get(position).split("\\s+");
//			String user = item[1];
//			UserController controller = UserController.getInstance(UserActivity.this);
//			controller.deleteUser(user);
//			values.remove(position);
//			adapter.notifyDataSetChanged();
		}
	};
} 