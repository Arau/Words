package com.idi.arau;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class Word {
	String word;
	Drawable picture;

	public Word(String word) {
		this.word = word;
	}
	
	public void drawTouchedLetters(Canvas canvas, String touchedLetters, int width, int height){
		int xPos = width/3;
		int yPos = (2*height)/3;
		Paint paint = new Paint();	
		paint.setTextSize(50);
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
