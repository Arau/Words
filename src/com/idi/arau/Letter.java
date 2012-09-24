package com.idi.arau;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Letter {
	private char character;
	private Bitmap letterPicture;
	private int x, y;
	private int xSpeed, ySpeed;	
	
	
	private ViewGame view;
	private int MAX_SPEED = 8;
	public static final int VELOCITY = 10;

	public Letter(){}
	
	public Letter(char character, ViewGame view, Bitmap bmp) {		
		this.character = character;		
		this.view = view;
		letterPicture = bmp;
		Random rnd = new Random();
		x = rnd.nextInt(view.getWidth()-letterPicture.getWidth());
		y = rnd.nextInt(view.getHeight()-letterPicture.getHeight());
		xSpeed = rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED;
        ySpeed = rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED;		
	}	
	
	public void update() {
		if (x >= view.getWidth() - letterPicture.getWidth() - xSpeed || x + xSpeed <= 0) {
			xSpeed = -xSpeed;
		}			
		x = x + xSpeed;
		
		if (y >= view.getHeight() - letterPicture.getHeight()- ySpeed || y + ySpeed <= 0) {
			ySpeed = -ySpeed;
		}			
		y = y + ySpeed;
			
	}
	
	public void onDraw(Canvas canvas){		
		update();
		canvas.drawBitmap(letterPicture,x,y,null);
	}
	
	public Boolean isTouch(float x2, float y2){
		return x2 > x && x2 < x + letterPicture.getWidth() && y2 > y && y2 < y + letterPicture.getHeight();
	}
	
	public Bitmap getBmp(){
		return letterPicture;
	}

	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public void setX(int xPosition){
		if (xPosition >= view.getWidth() - letterPicture.getWidth() - xSpeed){
			Random rnd = new Random();
			xPosition -= rnd.nextInt(view.getWidth());
		}		
		this.x = xPosition;
	}
	
	public void setY(int yPosition){
		if (yPosition >= view.getHeight() - letterPicture.getHeight() - xSpeed){
			Random rnd = new Random();
			yPosition -= rnd.nextInt(view.getHeight());
		}
		this.y = yPosition;
	}
	
	public Character getChar(){
		return character;
	}
	
	public Integer getWidth() {
		return view.getWidth();
	}
	
	public Integer getHeight() {
		return view.getHeight();
	}
}
