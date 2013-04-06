package com.idi.arau;

public class ModelWord {
	private long id;
	private String word;
	private int resource;
	private int level;
	
	public ModelWord(){}
	
	public ModelWord(String word, int res, int level) {
		this.word = word;
		this.resource = res;
		this.level = level;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	public int getResource() {
		return this.resource;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
}
