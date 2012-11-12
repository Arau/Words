package com.idi.arau;

import java.util.List;

import android.content.Context;

public class DomainController {

	private Context context;
	private String[] words;
	private int[] resources;
	private int level;

	public DomainController(Context context, int level) {
		this.context = context;
		this.level = level;
		catchData();
	}

	private void catchData() {
		List<ModelWord> readedData = readData();
		initArrays(readedData.size());
		fillData(readedData);
	}

	private List<ModelWord> readData() {
		WordsDataSource data = new WordsDataSource(context);
		data.open();
		List<ModelWord> readedData = data.readAllWords(this.level);
		data.close();
		return readedData;
	}

	private void fillData(List<ModelWord> readedData) {
		int i = 0;
		for (ModelWord word : readedData) {
			words[i] = word.getWord();			
			resources[i] = word.getResource();			
			++i;
		}
	}

	private void initArrays(int size) {
		words = new String[size];
		resources = new int[size];
	}

	public String[] getWordsToPlay() {
		return words;
	}

	public int[] getResourcesToPlay() {
		return resources;
	}

}
