package com.idi.arau;

import java.util.ArrayList;

public class ManagerGame {

	private ViewGame viewGame;
	private static ManagerGame managerObject;
	private Word[] words;
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
		++i;		
		return w;
	}

	public boolean isLast() {
		if (i == words.length) return true;
		return false;
	}
	
	private void defineWords(){
		Word w1 = new Word("a");
		Word w2 = new Word("b");		
		
		Word w5 = new Word("c");
		words = new Word[]{w1,w2,w5};
		
	}
}
