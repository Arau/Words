package com.idi.arau;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.ContextWrapper;

public class DomainController {

	private static DomainController domainControllerObject;
	private Context context;
	private boolean sync = true;
	private List<String> words = new ArrayList<String>();
	private List<Integer> resources = new ArrayList<Integer>();
	private List<Integer> levels = new ArrayList<Integer>();

	private DomainController(Context context) {
		this.context = context;			
		catchData();
	}
	
	public static DomainController getInstance(Context context) {
		if (domainControllerObject == null) {						
			domainControllerObject = new DomainController(context);			
		}		
		return domainControllerObject;
	}

	private void catchData() {		
		fillStaticWords();
		fillCustomWords();
		shuffleList();		
	}

	private void addWord(String word, Integer resource, Integer level) {
		words.add(word);
		resources.add(resource);
		levels.add(level);		
	}
	
	private void fillStaticWords() {		

		// Level 0
		addWord("apple", R.drawable.apple, 0);
		addWord("basket", R.drawable.basket, 0);
		addWord("boat", R.drawable.boat, 0);
		addWord("bottle", R.drawable.bottle, 0);
		addWord("coins", R.drawable.coins, 0);
		addWord("engine", R.drawable.engine, 0);
		addWord("orange", R.drawable.orange, 0);
		addWord("pineapple", R.drawable.pineapple, 0);
		addWord("tie", R.drawable.tie, 0);
		addWord("wheel", R.drawable.wheel, 0);

		// Level 1
		addWord("dumbbells",
				R.drawable.dumbbells, 1);
		addWord("dustbin", R.drawable.dustbin, 1);
		addWord("helmet", R.drawable.helmet, 1);
		addWord("screwdriver", R.drawable.screwdriver, 1);
		addWord("seagull", R.drawable.seagull, 1);
		addWord("snail", R.drawable.snail, 1);
	}

	private void fillCustomWords() {
		if (existDB()) {
			WordsDataSource data = WordsDataSource.getInstance(context);	
			List<ModelWord> dataFromDB = data.readAllWords();
			for (ModelWord word: dataFromDB) {
				addWord(word.getWord(), -1, word.getLevel());
			}
		}
	}

	private boolean existDB() {
		ContextWrapper wrapper = new ContextWrapper(context);
		File dbFile = wrapper.getDatabasePath("wordsDB.db");
		return dbFile.exists();
	}

	
	private void shuffleList() {
		int n = words.size();
		Random random = new Random();
		random.nextInt();
		for (int i = 0; i < n; i++) {
			int change = i + random.nextInt(n - i);
			swap(i, change);
		}
	}

	private void swap(int i, int change) {
		String auxWord = words.get(i);
		Integer auxRes = resources.get(i);
		Integer auxLev = levels.get(i);

		words.set(i, words.get(change));
		resources.set(i, resources.get(change));
		levels.set(i, levels.get(change));

		words.set(change, auxWord);
		resources.set(change, auxRes);
		levels.set(change, auxLev);
	}
	
	public List<String> getWordsToPlay() {
		this.sync = true;
		return words;
	}

	public List<Integer> getResourcesToPlay() {
		return resources;
	}
	
	public List<Integer> getLevels() {
		return levels;
	}

	public void setWord(ModelWord word) {
		words.add(word.getWord());
		resources.add(word.getResource());
		levels.add(word.getLevel());
		this.sync = false;
	}
	
	public boolean isUpdated() {
		return this.sync;
	}
}