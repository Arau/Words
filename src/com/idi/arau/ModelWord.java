package com.idi.arau;

public class ModelWord {
	private long id;
	private String word;
	private int resource;
	
	public ModelWord(){}
	
	public ModelWord(String word, int res) {
		this.word = word;
		this.resource = res;
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
		return resource;
	}
	
	public void setResource(int resource) {
		this.resource = resource;
	}	
}
