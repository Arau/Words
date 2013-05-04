package com.idi.arau;


import java.util.List;

import android.content.Context;

public class ManagerGame {

	private static ManagerGame managerObject;
	private DomainController controller;
	private Context context;
	private Word[] words_level0;
	private Word[] words_level1;
	private int[] bmps_level0;
	private int[] bmps_level1;
	private int[] levels;
	private int i;
	private int index0;
	private int index1;
	private int levelToPlay;

	private ManagerGame(Context context) {
		this.context = context;
		i = 0;		
		controller = DomainController.getInstance(context);
		defineWords();
	}
	
	public static ManagerGame getInstanceManager(Context context) {
		if (managerObject == null) {
			managerObject = new ManagerGame(context);
		}
		return managerObject;
	}

	public Word getNextWord() {
		Word w = words_level0[i];
		if (levelToPlay == 1)			
			w = words_level1[i];
		return w;
	}

	public int getNextPicture() {		
		int bmp = bmps_level0[i];
		if (levelToPlay == 1) {
			bmp = bmps_level1[i];
		}
		++i;
		return bmp;
	}

	public boolean isLast() {
		if (levelToPlay == 0) {
			if (i == index0)
				return true;
		} else {
			if (i == index1)
				return true;
		}
		return false;
	}
	
	private void defineWords() {		
		index0 = 0;
		index1 = 0;
		List<String> stringWords = controller.getWordsToPlay();
		List<Integer> bmps = controller.getResourcesToPlay();
		List<Integer> levels = controller.getLevels();

		initWords(stringWords.size());
		for (String stringWord : stringWords) {
			Word word = new Word(stringWord);
			
			if (levels.get(index0 + index1) == 0) {
				words_level0[index0] = word;
				bmps_level0[index0] = bmps.get(index0 + index1);
				++index0;
			}
			else {
				words_level1[index1] = word;
				bmps_level1[index1] = bmps.get(index0 + index1);
				++index1;
			}			
		}		
	}

	private void initWords(int length) {
		words_level0 = new Word[length];
		words_level1 = new Word[length];
		bmps_level0 = new  int[length];
		bmps_level1 = new  int[length];
	}

	public void refresh() {
		if (controller.isUpdated())
			defineWords();		
	}
	
	public void restartIndex() {
		this.i = 0;
	}

	public int getIndex() {
		return i;
	}

	public void setIndex(int index) {
		this.i = index;
	}

	public void setLevel(int level) {
		this.levelToPlay = level;
	}
	
	public int getWordsLength(int level) {
		return (level == 0) ? index0 : index1;
	}
}