package com.idi.arau;

import android.content.Context;

public class ManagerGame {

	private static ManagerGame managerObject;
	private Context context;
	private Word[] words;
	private int[] bmps;
	private int i;

	private ManagerGame(Context context) {
		this.context = context;
		i = 0;
		defineWords();
	}

	public static ManagerGame getInstanceManager(Context context) {
		if (managerObject == null) {
			managerObject = new ManagerGame(context);
		}
		return managerObject;
	}

	public Word getNextWord() {
		Word w = words[i];
		return w;
	}

	public int getNextPicture() {
		int bmp = bmps[i];
		++i;
		return bmp;
	}

	public boolean isLast() {
		if (i == words.length)
			return true;
		return false;
	}

	private void defineWords() {
		DomainController controller = new DomainController(context);
		String[] stringWords = controller.getWordsToPlay();

		initWords(stringWords.length);

		int i = 0;
		for (String stringWord : stringWords) {
			Word word = new Word(stringWord);
			words[i] = word;
			++i;
		}
		bmps = controller.getResourcesToPlay();
	}

	private void initWords(int length) {
		words = new Word[length];
	}

	public void restartIndex() {
		this.i = 0;
	}
}
