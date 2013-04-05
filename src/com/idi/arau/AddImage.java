package com.idi.arau;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

public class AddImage extends Activity {	
	private Button button;
	private String name;
	private Bitmap scaledImg;
	private int level = -1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_image_form);

		button = (Button) findViewById(R.id.selectImage);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {								
				Intent intent = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, 0);
			}
		});						
				
		
		button = (Button) findViewById(R.id.saveFields);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText nameField = (EditText) findViewById(R.id.nameImageToAdd);
				name = nameField.getText().toString();				
																						
				if (name == "" || level == -1 || scaledImg == null) {					
					Toast.makeText(AddImage.this, "Empty fields", Toast.LENGTH_LONG).show();
				}
				else {												
					saveFields();
					Toast.makeText(AddImage.this, "Data saved", Toast.LENGTH_LONG).show();
					goGallery();
				}
			}
		});
	} 

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK){
			Uri targetUri = data.getData();							
			try {			
				InputStream imageStream = getContentResolver().openInputStream(targetUri);
				Bitmap bitmap = BitmapFactory.decodeStream(imageStream);

				if (bitmap == null) {
					Toast.makeText(AddImage.this, "Can't import image", Toast.LENGTH_LONG).show();
				}
				else {
					scaleImage(bitmap);
					ImageView img = (ImageView) findViewById(R.id.imageView1);
					img.setImageBitmap(scaledImg);
					
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void scaleImage(Bitmap bmp) {
		int width     = bmp.getWidth();
		int height    = bmp.getHeight();
		int newWidth  = width;
		int newHeight = height;
		int max_size  = 270;

		if (width > height && width > max_size) {
			newWidth  = max_size;			
			newHeight = (newWidth*height)/width;			
		}
		else if (height > width && height > max_size) {
			newHeight = max_size;
			newWidth  = (newHeight*width)/height;
		}
		else if (height == width && height > max_size) {
			newWidth  = max_size;
			newHeight = max_size;
		}

		scaledImg = Bitmap.createScaledBitmap(bmp, newWidth, newHeight, true);			
	}

	private void saveImage() {
		FileOutputStream outputStream;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {			
			scaledImg.compress(CompressFormat.PNG, 0, stream);
			outputStream = openFileOutput(this.name, Context.MODE_PRIVATE);
			outputStream.write(stream.toByteArray());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}	

	private void saveFields() {
		saveImage();
		
		// Save to DB
		ModelWord word = new ModelWord(this.name, -1, level);				
		List<ModelWord> words = new ArrayList<ModelWord>();		
		words.add(word);
		
		WordsDataSource data = WordsDataSource.getInstance(this);
		data.addWords(words);
		
		// Save to Mem
		DomainController controller = DomainController.getDomainControllerInstance(this);
		controller.setWord(word);
	}	
	
	
	
	protected void goGallery() {
		Intent i = new Intent(this, Gallery.class);
		startActivity(i);		
	}
	
	// Callback
	public void onRadioButtonClicked(View view) {	    
	    boolean checked = ((RadioButton) view).isChecked();
	    	    
	    switch(view.getId()) {
	        case R.id.level1:
	            if (checked)
	                level = 0;
	            break;
	        case R.id.level2:
	            if (checked)
	                level = 1;
	            break;
	    }
	}
}
