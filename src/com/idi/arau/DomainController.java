package com.idi.arau;

import java.util.List;
import java.util.Random;

import android.content.Context;

public class DomainController {

	private static DomainController domainControllerObject;
	private Context context;
	private String[] words;
	private int[] resources;
	private int[] levels;	

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
		initArrays(readedData.size());
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
			words[i] 	 = word.getWord();					
			resources[i] = word.getResource();				
			levels[i] 	 = word.getLevel();					
			++i;
		}
	}

	private void initArrays(int size) {
		words = new String[size];
		resources = new int[size];
		levels = new int[size];
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
	
	
	public String[] getWordsToPlay() {			
		return words;
	}

	public int[] getResourcesToPlay() {	
		return resources;
	}
	
	public int[] getLevels() {
		return levels;
	}	
}
