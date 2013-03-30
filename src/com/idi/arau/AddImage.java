package com.idi.arau;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class AddImage extends Activity {	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_image_form);

		Button button = (Button) findViewById(R.id.selectImage);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {								
				Intent intent = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, 0);
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
					Bitmap cropedImg = cropImage(bitmap);
					ImageView img = (ImageView) findViewById(R.id.imageView1);
					img.setImageBitmap(cropedImg);
					saveImage(cropedImg);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private Bitmap cropImage(Bitmap bmp) {
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

		return Bitmap.createScaledBitmap(bmp, newWidth, newHeight, true);			
	}

	private void saveImage(Bitmap bmp) {

	}	

}
