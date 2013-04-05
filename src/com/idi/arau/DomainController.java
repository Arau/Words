package com.idi.arau;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;

public class DomainController {

	private static DomainController domainControllerObject;
	private Context context;
	private List<String> words;
	private List<Integer> resources;
	private List<Integer> levels;	

	private DomainController(Context context) {
		this.context = context;			
		catchData();
	}
	
	public static DomainController getDomainControllerInstance(Context context) {
		if (domainControllerObject == null) {						
			domainControllerObject = new DomainController(context);			
		}		
		return domainControllerObject;
	}
		

	private void catchData() {		
		List<ModelWord> readedData = readData();		
		shuffleList(readedData);
		initArrays();
		fillData(readedData);
	}

	private List<ModelWord> readData() {		
		WordsDataSource data = WordsDataSource.getInstance(context);		
		List<ModelWord> readedData = data.readAllWords();				
		return readedData;
	}

	private void fillData(List<ModelWord> readedData) {		
		int i = 0;
		for (ModelWord word : readedData) {		
			words.add(i, word.getWord());
			resources.add(i, word.getResource());				
			levels.add(i, word.getLevel());					
			++i;
		}
	}

	public static void shuffleList(List<ModelWord> a) {
		int n = a.size();
		Random random = new Random();
		random.nextInt();
		for (int i = 0; i < n; i++) {
			int change = i + random.nextInt(n - i);
			swap(a, i, change);
		}
	}

	private static void swap(List<ModelWord> a, int i, int change) {
		ModelWord aux = a.get(i);
		a.set(i, a.get(change));
		a.set(change, aux);
	}

	private void initArrays() {
		words = new ArrayList<String>();
		resources = new ArrayList<Integer>();
		levels = new ArrayList<Integer>(); 
	}
	
	
	public List<String> getWordsToPlay() {			
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
	}	
}
