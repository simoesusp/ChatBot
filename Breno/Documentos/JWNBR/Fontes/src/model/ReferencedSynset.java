package model;

import java.util.ArrayList;
import java.util.List;

public class ReferencedSynset implements Synset{
	
	private String type;
	private Integer synonymousReference = null;
	private Integer antonymReference = null;
	private List<String> words = new ArrayList<String>();
	
	public List<String> getWords() {
		return words;
	}
	
	public String toString(){
		StringBuilder str = new StringBuilder();
		str.append(type);
		str.append(" ");
		for(int i=0; i<words.size(); i++){
			str.append(words.get(i));
			str.append(" ");
		}
		str.append(synonymousReference);
		str.append(" ");
		str.append(antonymReference);
		str.append(" ");
		return new String(str);
	} 
	public void setType(String type){
		this.type = type;
	}
	@Override
	public String getType() {
		return this.type;
	}
	@Override
	public void addWord(String s) {
		words.add(s);
	}

	@Override
	public Integer getSynonymousPointer() {
		return synonymousReference;
	}

	@Override
	public Integer getAntonymPointer() {
		return antonymReference;
	}

	@Override
	public void setSynonymousPointer(int pointer) {
		this.synonymousReference = pointer;
	}

	@Override
	public void setAntonymousPointer(int pointer) {
		this.antonymReference = pointer;
	}
}
