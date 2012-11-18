package com.idi.arau;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class Word {
	String word;
	Drawable picture;

	public Word(String word) {
		this.word = word;
	}
	
	public void drawTouchedLetters(Canvas canvas, String touchedLetters, int width, int height, int wordLength){		
		int xPos = width/3;  
		if(wordLength > 4 && wordLength <= 7) xPos = width/4;
		else if (wordLength > 7) xPos = width/8;
		int yPos = (5*height)/6;
		Paint paint = new Paint();		
		paint.setTextSize(35);
		paint.setStrokeWidth(3);
		if (width > 300 && height > 300) paint.setTextSize(50); 		
		canvas.drawText(touchedLetters,xPos,yPos,paint);	
	}
	
	public void onDraw(Canvas canvas) {

	}

	public Boolean checkWord(String touchedLetters) {
		for (int i = 0; i < touchedLetters.length(); i++) {					
			if (word.charAt(i) != touchedLetters.charAt(i)) {
				return false;
			}			
		}
		return true;
	}

	public String getString() {
		return this.word;
	}
}
