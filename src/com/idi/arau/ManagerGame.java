package com.idi.arau;

import android.util.Log;

public class ManagerGame {

	private ViewGame viewGame;
	private static ManagerGame managerObject;
	private Word[] words;
	private int[] bmps;
	private int i;	

	private ManagerGame(ViewGame view) {
		this.viewGame = view;
		i = 0;			
		defineWords();
		
	}

	public static ManagerGame getInstanceManager(ViewGame view) {
		if (managerObject == null) {
			managerObject = new ManagerGame(view);
		}
		return managerObject;
	}	

	public Word getNextWord() {		
		Word w = words[i];		
		return w;
	}
	
	public int getNextPicture(){
		int bmp = bmps[i];
		++i;	
		return bmp;
	}

	public boolean isLast() {
		if (i == words.length) return true;
		return false;
	}
	
	private void defineWords(){
		Word w1 = new Word("t");		
		Word w2 = new Word("b");				
		Word w5 = new Word("c");
		words = new Word[]{w1,w2,w5};
		
		int b1 = R.drawable.apple;
		int b2 = R.drawable.orange;
		int b3 = R.drawable.pineapple;
		bmps = new int[]{b1,b2,b3};
		
		
		
	}
}
