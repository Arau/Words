package com.idi.arau;

import com.idi.arau.R;
import com.idi.arau.R.id;
import com.idi.arau.R.layout;
import com.idi.arau.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class Gallery extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.grid);

   	}

	@Override
	protected void onResume() {
		super.onResume();
		GridView gridview = (GridView) findViewById(R.id.gridview);	    
	    gridview.setAdapter(new ImageAdapter(this));	    

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(Gallery.this, "" + position, Toast.LENGTH_SHORT).show();
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
		}
		return false;
	}
}
