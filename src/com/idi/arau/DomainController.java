package com.idi.arau;

import java.util.List;

import android.content.Context;

public class DomainController {

	Context context;
	String[] words;
	int[] resources;

	public DomainController(Context context) {
		this.context = context;
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
		List<ModelWord> readedData = data.readAllWords();
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
