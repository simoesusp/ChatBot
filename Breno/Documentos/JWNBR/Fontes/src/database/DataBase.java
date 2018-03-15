package database;

import java.util.ArrayList;
import java.util.List;

import model.Synset;

public class DataBase {
	private static List<Synset> synsets = new ArrayList<Synset>();
	
	public static void addSynset(Synset s){
		synsets.add(s);
	}
	public static List<Synset> getSynsets(String s){
		List<Synset> ss = new ArrayList<Synset>();
		for(int i=0; i<synsets.size(); i++){
			if(synsets.get(i).getWords().contains(s))
				ss.add(synsets.get(i));
		}
		
		if(ss.size() == 0){
			return null;
		}
		return ss;
	}
	
	public static boolean exists(String s){
		for(int i=0; i<synsets.size(); i++){
			if(synsets.get(i).getWords().contains(s))
				return true;
		}
		return false;
	}
	
	public static Synset getSynsetsByReference(int reference){
		for(int i=0; i<synsets.size(); i++){
			if(synsets.get(i).getSynonymousPointer() == reference){
				return synsets.get(i);
			}
		}
		return null;
	}
	public static int tamanhoBase(){
		return synsets.size();
	}
	public static List<Synset> getSynsets(){
		return synsets;
	}
}
