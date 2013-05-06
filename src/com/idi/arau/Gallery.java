package com.idi.arau;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class Gallery extends Activity {
	
	public ImageAdapter adapter;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.grid);
	    getActionBar().setHomeButtonEnabled(true);		    	    
   	}

	@Override
	protected void onResume() {
		super.onResume();
		GridView gridview = (GridView) findViewById(R.id.gridview);
		adapter = new ImageAdapter(this);
	    gridview.setAdapter(adapter);
	    
	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(Gallery.this, "Try with long click to delete", Toast.LENGTH_SHORT).show();
	        }
	    });
	    
	    gridview.setOnDragListener(new OnDragListener() {
			
			@Override
			public boolean onDrag(View v, DragEvent event) {
				boolean ret = true;
				float initEventX = 0, initEventY = 0;

				switch (event.getAction()) {
					case DragEvent.ACTION_DRAG_STARTED:
						 // Determines if this View can accept the dragged data
		                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
		                    v.invalidate(); // Mark view as dirty to force redraw it
		                    ret = true;
		                } 
		                else {
		                    // Returns false. During the current drag and drop operation, this View will
		                    // not receive events again until ACTION_DRAG_ENDED is sent.
		                    ret = false;
	                    }
		                
						initEventX = event.getX();
						initEventY = event.getY();		                
		                break;
		                
					case DragEvent.ACTION_DRAG_ENTERED:
		                v.invalidate();
		                ret = true;
		                break;
		                
					case DragEvent.ACTION_DRAG_LOCATION:
						ret = true;
		                break;
		                
					case DragEvent.ACTION_DRAG_EXITED:        
	                    v.invalidate();
	                    ret = true;
	                    break;
	                    
					case DragEvent.ACTION_DROP:
			
						//Get drag info 
						ClipData.Item item = event.getClipData().getItemAt(0);
						CharSequence dragData = item.getText();
						int position = Integer.parseInt(dragData.toString());
						
						float distanceX = getDistance(initEventX, event.getX());
						float distanceY = getDistance(initEventY, event.getY());
																		
						if (distanceX > 200 || distanceY > 370) {
							alertDialog(position);
						}
						v.invalidate();						
						ret = true;
	                break;
	                
					case DragEvent.ACTION_DRAG_ENDED:
			
						// Invalidates the view to force a redraw
						v.invalidate();
			
						// Does a getResult(), and displays what happened.
						if (event.getResult()) {
							Log.v("DelteDrag", "The drop was handled.");
			
						} else {
							Log.v("DelteDrag", "The drop didn't work.");
						}	
						// returns true; the value is ignored.
						ret = true;

						break;
			
					default:
						Log.e("DragDrop Gallery","Unknown action type received by OnDragListener.");
						break;
				}
				return ret;
			}

			private void deleteImage(int position) {
				//Delete from grid view and implicitly from words list.				
				adapter.deleteItem(position);																
			}
			
			private void alertDialog(final int position) {
				AlertDialog.Builder builder = new AlertDialog.Builder(Gallery.this);
				builder.setTitle("Are you sure to delete?");
				builder.setCancelable(true);

				builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int id) {
						deleteImage(position);
						Toast.makeText(Gallery.this, "Deleted", Toast.LENGTH_SHORT).show();
					}
				});
				
				builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});				
				
				AlertDialog alertDialog = builder.create();
				alertDialog.show();
			}
			
			private float getDistance(float a, float b) {
				float distance = a - b;
				if (distance < 0) distance *= -1;
				return distance;
			}
		});
	    
	    gridview.invalidate();
	    
	    
	    gridview.setOnItemLongClickListener(new OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	            	
            	String tag = "" + position;            	
            	ClipData dragData = ClipData.newPlainText(tag, tag);
                View.DragShadowBuilder myShadow = new MyDragShadowBuilder(view, Gallery.this);
                view.startDrag(dragData, myShadow, null, 0);
                            	
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
} 