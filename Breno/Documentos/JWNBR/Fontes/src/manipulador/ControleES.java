package manipulador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Adjetivo;
import model.Adverbio;
import model.Substantivo;
import model.Synset;
import model.Verbo;
import database.DataBase;

public class ControleES {

	/*
	 * MŽtodo que inicializa a wordnet default
	 * */
	public void init(){
		try {
			init("base_jwnbr.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void init(String path) throws FileNotFoundException, UnsupportedEncodingException{
		init(new File(path));
	}

	public void init(File f) throws FileNotFoundException, UnsupportedEncodingException{
		Scanner s = null;
		Scanner l = null;
		String aux;
		Synset synset = null;
		int referenciaSinonimo;
		int referenciaAntonimo;
		s = new Scanner(new BufferedReader(new InputStreamReader(new FileInputStream(f.getAbsolutePath()), "UTF-8")));
		s.useDelimiter("\n");
		while(s.hasNext()){
			l = new Scanner(s.next());
			referenciaSinonimo = Integer.parseInt(l.next());
			referenciaAntonimo = 0;
			while(l.hasNext()){
				aux = l.next();
				if(aux.charAt(0) == 'V')
					synset = new Verbo();
				else if(aux.charAt(2) == 'v')
					synset = new Adverbio();
				else if(aux.charAt(0) == 'S')
					synset = new Substantivo();
				else synset = new Adjetivo();

				synset.setSynonymousPointer(referenciaSinonimo);

				while(l.hasNext()){
					aux = l.next();
					if(aux.charAt(0) == '.'){
						referenciaAntonimo = (Integer.parseInt(aux.substring(1)));
						synset.setAntonymousPointer(referenciaAntonimo);
					}else{
						synset.addWord(aux);
					}
				}
			}
			DataBase.addSynset(synset);
		}
	}

	public void salvar(String path) throws IOException{
			BufferedWriter b = new BufferedWriter(new FileWriter(path));
			List<String> conteudo = new ArrayList<String>();
			if(DataBase.getSynsets() != null &&  DataBase.getSynsets().size() > 0){

				for (int i = 0; i < DataBase.getSynsets().size(); i++) {
					Synset s = DataBase.getSynsets().get(i);
					StringBuilder str = new StringBuilder();
					str.append(s.getSynonymousPointer());
					str.append(" ");
					str.append(s.getType());
					str.append(" ");
					for(int j=0; j<s.getWords().size(); j++){
						str.append(s.getWords().get(j));
						str.append(" ");
					}
					if(s.getAntonymPointer() != null){
						str.append("."+ s.getAntonymPointer());
					}
					conteudo.add(str.toString());
				}
				for(int i=0; i<conteudo.size(); i++){
					b.write(conteudo.get(i));
					b.newLine();
				}
			}
			b.close();
	}


}
