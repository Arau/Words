package com.idi.arau;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;


public class UserActivity extends ListActivity {
	
	private ActionMode mActionMode;
	static ArrayAdapter<String> adapter;
	static List<String> values;
	static int position;
	
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		defineActionBar();
		
		values = getListOfItems();
		adapter = new ArrayAdapter<String>(this,
				R.layout.row_layout, R.id.user, values);
		setListAdapter(adapter);
		
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
	
	private List<String> getListOfItems() {
		UserController controller = UserController.getInstance(this);
		Map<String, Integer> userScore = controller.getRanking();
		List<String> values = new ArrayList<String>();
		int index = 1;
		for (String key : userScore.keySet()) {
			values.add(index + "  " + key + " \t\t\t\t" + userScore.get(key) + " points");
			index++;
		}
		return values;
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

		// Called each time the action mode is shown. Always called after onCreateActionMode.
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false; // Return false if nothing is done
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.delete_user:
				alertDialog();
				mode.finish(); // close the CAB
				return true;
			default:
				return false;
			}
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
		}
		
		private void alertDialog() {
			AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
			builder.setTitle("Are you sure to delete?");
			builder.setCancelable(true);

			builder.setPositiveButton(R.string.delete,
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					deleteUser();
					Toast.makeText(UserActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
				}
			});
			
			builder.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});				
			
			AlertDialog alertDialog = builder.create();
			alertDialog.show();
		}
		
		private void deleteUser() {			
			String[] item = values.get(position).split("\\s+");
			String user = item[1];
			UserController controller = UserController.getInstance(UserActivity.this);
			controller.deleteUser(user);
			values.remove(position);
			adapter.notifyDataSetChanged();
		}
	};
} 