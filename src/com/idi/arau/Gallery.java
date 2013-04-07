package com.idi.arau;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class Gallery extends Activity {
	
	private ActionMode mActionMode;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.grid);
	    getActionBar().setHomeButtonEnabled(true);
   	}

	@Override
	protected void onResume() {
		super.onResume();
		GridView gridview = (GridView) findViewById(R.id.gridview);	    
	    gridview.setAdapter(new ImageAdapter(this));	    

	    gridview.setOnItemLongClickListener(new OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View view,
                    int position, long arg3) {
                
            	 if (mActionMode != null) {
 	                return false;
 	            }

 	            // Start the CAB using the ActionMode.Callback defined above
 	            mActionMode = Gallery.this.startActionMode(mActionModeCallback);
 	            view.setSelected(true);
 	            return true;
            	
            }
        }); 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.gallery_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
        	case R.id.addToGallery:
        		Intent i = new Intent(this, AddImage.class);
        		startActivity(i);
        		break;
        	case android.R.id.home:
                Intent intent = new Intent(this, Words.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
		}
		return false;
	}
	
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

	    // Called when the action mode is created; startActionMode() was called
	    @Override
	    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	        // Inflate a menu resource providing context menu items
	        MenuInflater inflater = mode.getMenuInflater();
	        inflater.inflate(R.menu.context_action_items_gallery, menu);
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
	            case R.id.menu_delete:
	                mode.finish(); // Action picked, so close the CAB
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
	};
	
}
