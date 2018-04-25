package model;

import java.util.List;

public interface Synset {
	public List<String> getWords ();
	public void addWord (String s);
	public Integer getSynonymousPointer();
	public Integer getAntonymPointer();
	public void setSynonymousPointer(int pointer);
	public void setAntonymousPointer(int pointer);
	public String getType();
	public String toString();
}
