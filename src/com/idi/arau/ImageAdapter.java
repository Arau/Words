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
	private List<Integer> mThumbIds;

	public ImageAdapter(Context c) {		
        mContext = c;
        getImages();
    }

    public int getCount() {
        return mThumbIds.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
    
    public String getName(int position) {
    	return names.get(position);
    }
    
    public Integer getThumb(int position) {
    	return mThumbIds.get(position);
    }
    
    public void deleteItem(int position) {
    	names.remove(position);
    	mThumbIds.remove(position);
    	this.notifyDataSetChanged();
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
    
        if (mThumbIds.get(position) == -1) { 
        	setImgFromDecodedFile(imageView, names.get(position)); 
        }        	        	        
        else {
        	imageView.setImageResource(mThumbIds.get(position));
        }
        return imageView;
    }
    
    private void getImages() {    	
		DomainController manager = DomainController.getInstance(mContext);
		names  = manager.getWordsToPlay();				
    	mThumbIds = manager.getResourcesToPlay();
    }   
    
    private void setImgFromDecodedFile(ImageView imageView, String fileName) {
    	String path = mContext.getFilesDir().getPath() + '/' + fileName;
    	Bitmap bmp = BitmapFactory.decodeFile(path);
    	imageView.setImageBitmap(bmp);
    }
}