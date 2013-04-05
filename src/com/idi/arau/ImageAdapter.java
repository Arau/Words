package com.idi.arau;


import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> names;
	private Integer[] mThumbIds;

	public ImageAdapter(Context c) {		
        mContext = c;
        getImages();
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);            
            imageView.setLayoutParams(new GridView.LayoutParams(120, 120));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(6, 6, 6, 6);
        } else {
            imageView = (ImageView) convertView;
        }                        
    
        if (mThumbIds[position] == -1) { 
        	setImgFromDecodedFile(imageView, names.get(position)); 
        }        	        	        
        else {
        	imageView.setImageResource(mThumbIds[position]);
        }
        return imageView;
    }
    
    private void getImages() {    	
		DomainController manager = DomainController.getDomainControllerInstance(mContext);
		names  = manager.getWordsToPlay();				
    	List<Integer> resources = manager.getResourcesToPlay();
    	int i = 0;
    	mThumbIds = new Integer[resources.size()];
    	for (int value: resources) {
    		mThumbIds[i] = value;    		 
    		++i;
    	}    	
    }   
    
    private void setImgFromDecodedFile(ImageView imageView, String fileName) {
    	String path = mContext.getFilesDir().getPath() + '/' + fileName;
    	Bitmap bmp = BitmapFactory.decodeFile(path);
    	imageView.setImageBitmap(bmp);
    }
}