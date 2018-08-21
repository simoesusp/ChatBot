import java.awt.BorderLayout;
import java.awt.EventQueue;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChatScreen extends JFrame {

	private JPanel contentPane;
	
	String[][] gen = new String [10][4]; // 10 indivíduos, cada um contendo 4 genes (palavras)
	int[][] posGen = new int [10][4];
	int[][] newPosGen = new int [10][4];
	String[][] newGen = new String [10][4];
	int[] fitGen = new int [10];
	int perfect = -1, generations = 1, fitTotal;
	
	/*
	 *  - ESTUDAR PRINCIPAIS ERROS GRAMATICAIS NA IDADES DAS PESSOAS
	 *  - SUBSTANTIVOS INANIMADOS
	 *  - FAZER TEMAS
	 *  - VER SUBSTANTIVOS ANTERIORMENTE FALADOS
	 *  - ESCOLHER VERBO PRIMEIRO
	 *  - ADICIONAR BOT ESTÁ DIGITANDO...
	 *  - ADICIONAR EMOJIS
	 */
	
	String getVerbsDatabase() {
		String everything = "";
		
		try(BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    everything = sb.toString();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    return everything;
		
	}

	String[] noun1 = {"menino", "pai", "homem", "tio", "avô", "irmão", "primo", "dia", "sol", "cachorro", "gato",
	"livro", "instrumento", "violão", "computador", "videogame", "amigo", "teclado", "dedo", "pé", "professor", "giz",
	"trovão", "papel", "lápis", "namorado", "almoço", "mercado", "nome", "sobrenome", "copo", "prato", "teste", "barulho", "mouse",
	"aviso", "cinema", "senhor", "moço", "tempo", "momento", "acontecimento", "chuveiro", "cérebro", "olho", "lixo"}; // 46 SUBSTANTIVOS COM ARTIGOS MASCULINOS (O, UM)
	
	String[] noun2 = { "pessoa", "menina", "mãe", "criança", "mulher", "tia", "avó", "irmã", "prima", "vida", "lua", "família",
	"música", "guitarra", "amiga", "conversa", "mão", "cabeça", "professora", "lousa", "maçã", "laranja", 
	"chuva", "noite", "caneta", "namorada", "comida", "janta", "sobremesa", "prova", "notícia", "tv", "televisão", "estante", "ideia", "pergunta", "resposta", "senhora",
	"moça", "casa", "coisa", "boca", "mania", "atividade", "lapiseira", "banana"}; // 46 SUBSTANTIVOS COM ARTIGOS FEMININOS (A, UMA)
	
	String[] verb1 = { "almoçei", "molhei", "olhei", "fiz", "falei", "ouvi", "amei", "escrevi", "dei", "corrigi", "bebi", "li", "recebi", "toquei", "perdi", "estudei", "atendi",
	"enviei", "avisei", "paguei", "peguei", "beijei", "escutei", "alegrei", "telefonei", "mordi", "abandonei", "movi", "apoiei", "provoquei", "aconselhei", "alertei", "machuquei",
	"enxerguei", "arranhei", "cortei", "procurei", "protegi", "defendi", "vi", "abri", "esperei", "ensinei", "plantei", "compartilhei", "limpei", "visitei", "pintei", "animei",
	"pertubou"}; // VTD NO PASSADO PRIMEIRA PESSOA
	
	String[] verb2 = { "almoço", "molho", "olho", "faço", "falo", "ouço", "amo", "escrevo", "dou", "corrijo", "bebo", "leio", "recebo", "toco", "perco", "estudo", "atendo",
	"envio", "aviso", "pago", "pego", "beijo", "escuto", "alegro", "telefono", "mordo", "abandono", "movo", "apoio", "provoco", "aconselho", "alerto", "machuco",
	"enxergo", "arranho", "corto", "procuro", "protejo", "defendo", "vejo", "abro", "espero", "ensino", "planto", "compartilho", "limpo", "visito", "pinto", "animo",
	"pertubo"}; // VTD NO PRESENTE PRIMEIRA PESSOA
	
	String[] verb3 = { "almoçarei", "molharei", "olharei", "farei", "falarei", "ouvirei", "amarei", "escreverei", "darei", "corrigirei", "beberei", "lerei", "receberei", "tocarei", "perderei", "estudarei", "atenderei",
	"enviarei", "avisarei", "pagarei", "pegarei", "beijarei", "escutarei", "alegrarei", "telefonarei", "morderei", "abandonarei", "moverei", "apoiarei", "provocarei", "aconselharei", "alertarei", "machucarei",
	"enxergarei", "arranharei", "cortarei", "procurarei", "protegerei", "defenderei", "verei", "abrirei", "esperarei", "ensinarei", "plantarei", "compartilharei", "limparei", "visitarei", "pintarei", "animarei",
	"pertubarei"}; // VTD NO FUTURO PRIMEIRA PESSOA
	
	String[] verb4 = { "almoçou", "molhou", "olhou", "fez", "falou", "ouviu", "amou", "escreveu", "deu", "corrigiu", "bebeu", "leu", "recebeu", "tocou", "perdeu", "estudou", "atendeu",
	"enviou", "avisou", "pagou", "pegou", "beijou", "escutou", "alegrou", "telefonou", "mordeu", "abandonou", "moveu", "apoiou", "provocou", "aconselhou", "alertou", "machucou",
	"enxergou", "arranhou", "cortou", "procurou", "protegeu", "defendeu", "viu", "abriu", "esperou", "ensinou", "plantou", "compartilhou", "limpou", "visitou", "pintou", "animou",
	"pertubou"}; // VTD NO PASSADO TERCEIRA PESSOA
	
	String[] verb5 = { "almoça", "molha", "olha", "faz", "fala", "ouve", "ama", "escreve", "dá", "corrige", "bebe", "lê", "recebe", "toca", "perde", "estuda", "atende",
	"envia", "avisa", "paga", "pega", "beija", "escuta", "alegra", "telefona", "morde", "abandona", "move", "apoia", "provoca", "aconselha", "alerta", "machuca",
	"enxerga", "arranha", "corta", "procura", "protege", "defende", "vê", "abre", "espera", "ensina", "planta", "compartilha", "limpa", "visita", "pinta", "anima",
	"pertuba"}; // VTD NO PRESENTE TERCEIRA PESSOA
	
	String[] verb6 = { "almoçará", "molhará", "olhará", "fará", "falará", "ouvirá", "amará", "escreverá", "dará", "corrigirá", "beberá", "lerá", "receberá", "tocará", "perderá", "estudará", "atenderá",
	"enviará", "avisará", "pagará", "pegará", "beijará", "escutará", "alegrará", "telefonará", "morderá", "abandonará", "moverá", "apoiará", "provocará", "aconselhará", "alertará", "machucará",
	"enxergará", "arranhará", "cortará", "procurará", "protegerá", "defenderá", "verá", "abrirá", "esperará", "ensinará", "plantará", "compartilhará", "limpará", "visitará", "pintará", "animará",
	"pertubará"}; // VTD NO FUTURO TERCEIRA PESSOA
	
	String[] pronoun1 = {"eu"}; // PRONOME DE PRIMEIRA PESSOA
	String[] pronoun2 = {"ele", "ela", "você" }; // PRONOMES DE TERCEIRA PESSOA
	
	String[] article1 = {"o", "um"}; // ARTIGOS PARA GÊNERO MASCULINO
	String[] article2 = {"a", "uma"}; // ARTIGOS PARA GÊNERO FEMININO
	
	public void initGenerations(){
		// Cria a geração atual
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 4; j++){
				int rand = (int)(Math.random() * 12);
				
				if(rand == 0) {
					int tst = (int)(Math.random() * 46);
					gen[i][j] = noun1[tst];
					posGen[i][j] = 0;
					if(gen[i][j] == "null") {
						System.out.println(tst);
					}
				} else if (rand == 1) {
					gen[i][j] = noun2[(int)(Math.random() * 46)];
					posGen[i][j] = 1;
				} else if (rand == 2) {
					gen[i][j] = verb1[(int)(Math.random() * 50)]; // VTD NO PASSADO PRIMEIRA PESSOA
					posGen[i][j] = 2;
				} else if (rand == 3) {
					gen[i][j] = verb2[(int)(Math.random() * 50)]; // VTD NO PRESENTE PRIMEIRA PESSOA
					posGen[i][j] = 3;
				} else if (rand == 4) {
					gen[i][j] = verb3[(int)(Math.random() * 50)]; // VTD NO FUTURO PRIMEIRA PESSOA
					posGen[i][j] = 4;
				} else if (rand == 5) {
					gen[i][j] = verb4[(int)(Math.random() * 50)]; // VTD NO PASSADO TERCEIRA PESSOA
					posGen[i][j] = 5;
				} else if (rand == 6) {
					gen[i][j] = verb5[(int)(Math.random() * 50)]; // VTD NO PRESENTE TERCEIRA PESSOA
					posGen[i][j] = 6;
				} else if (rand == 7) {
					gen[i][j] = verb6[(int)(Math.random() * 50)]; // VTD NO FUTURO TERCEIRA PESSOA
					posGen[i][j] = 7;
				} else if (rand == 8) {
					gen[i][j] = pronoun1[0];
					posGen[i][j] = 8;
				} else if (rand == 9) {
					gen[i][j] = pronoun2[(int)(Math.random() * 3)];
					posGen[i][j] = 9;
				} else if (rand == 10) {
					gen[i][j] = article1[(int)(Math.random() * 2)];
					posGen[i][j] = 10;
				} else if (rand == 11) {
					gen[i][j] = article2[(int)(Math.random() * 2)];
					posGen[i][j] = 11;
				} 
				
				
			}
		}
		
		//System.out.println(gen[0][0] + " "+ gen[0][1] + " " + gen[0][2] + " " + gen[0][3]);
		
	}
	
	public boolean searchNoun(String str) {
		String lastLine = "";
		try {
			//FileInputStream inputStream = new FileInputStream("c://Chatbot/log.txt");
			File file = new File("c://Chatbot/log.txt");
			List<String> lines = FileUtils.readLines(file, "UTF-8");
			lastLine = lines.get(lines.size() - 1);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(lastLine.contains(str)) {
			return true;
		}

		return false;
	}
	
	public boolean evaluate(){
		fitTotal = 0;
		for(int i = 0; i < 10; i++){
			int fitness = 0;
			
			if(posGen[i][0] == 8 || posGen[i][0] == 9) { // SE COMEÇA COM PRONOME
				fitness += 2;
				if(posGen[i][0] == 8) {
					if(posGen[i][1] >= 5 && posGen[i][1] <= 7) { // SE ESTÁ EM PRIMEIRA PESSOA COM VERBO EM TERCEIRA
						fitness -= 2;
					}
				} else {
					if(posGen[i][1] >= 2 && posGen[i][1] <= 4) { // SE ESTÁ EM TERCEIRA PESSOA COM VERBO EM PRIMEIRA
						fitness -= 2;
					}
				}
			}
			
			if(posGen[i][2] == 10 || posGen[i][2] == 11) { // SE TEM ARTIGOS NA POSIÇÃO ESPECÍFICA
				fitness += 3;
				if(posGen[i][2] == 10) {
					if(posGen[i][3] == 1) { // SE O ARTIGO É MASCULINO MAS SUBSTANTIVO É FEMININO
						fitness -= 3;
					}
				} else {
					if(posGen[i][3] == 0) { // SE O ARTIGO É FEMININO MAS SUBSTANTIVO É MASCULINO
						fitness -= 3;
					}
				}
			}
			
			if(posGen[i][3] == 0 || posGen[i][3] == 1) { // SE TERMINA COM SUBSTANTIVO
				fitness += 4;
				//if(searchNoun(gen[i][3])){ // PRIORIZA QUANDO APARECE JÁ NO LOG
				//	fitness += 5; 
				//}
			}
			
			if(posGen[i][1] >= 2 && posGen[i][1] <= 7) { // SE TEM VTD NO MEIO
				fitness += 2;
			} 
			

			if (fitness >= 10){ // SE ENCONTROU ALGUM ORGANISMO COM FITNESS ADEQUADO
				perfect = i;
				return true;
			}
			
			fitGen[i] = fitness;
			fitTotal += fitness;
			
		}
		return false;
	}
	
	public int selectOne(){ // Método da roleta
		  int runningTotal = 0;
		  int randomSelectPoint;

		  randomSelectPoint = 1 + (int)(Math.random() * fitTotal);

		  for(int i = 0; i < 10; i++){
		    runningTotal += fitGen[i];
		    if(runningTotal >= randomSelectPoint){
		    	return i;
		    }
		  }
		  return 0;
	}
	
	public void nextGen(){
		int p1;
		int p2;
		int crossover;
		int mutate;

		for(int i = 0; i < 10; i++){
			p1 = selectOne();
		    p2 = selectOne();

		    crossover  =  0 + (int)(Math.random() * 4); // Ponto aleatório no vetor do organismo para a troca de genes entre o pai e a mãe

		    for(int j = 0; j < 4; j++){
		    	mutate = (int)(Math.random() * (int)(101)); // TAXA DE MUTAÇÃO EM 1%
		    	if(mutate == 0){ // MUTAÇÃO
					int rand = (int)(Math.random() * 12);
					
					if(rand == 0) {
						int tst = (int)(Math.random() * 46);
						gen[i][j] = noun1[tst];
						posGen[i][j] = 0;
						if(gen[i][j] == "null") {
							System.out.println(tst);
						}
					} else if (rand == 1) {
						gen[i][j] = noun2[(int)(Math.random() * 46)];
						posGen[i][j] = 1;
					} else if (rand == 2) {
						gen[i][j] = verb1[(int)(Math.random() * 50)];
						posGen[i][j] = 2;
					} else if (rand == 3) {
						gen[i][j] = verb2[(int)(Math.random() * 50)];
						posGen[i][j] = 3;
					} else if (rand == 4) {
						gen[i][j] = verb3[(int)(Math.random() * 50)];
						posGen[i][j] = 4;
					} else if (rand == 5) {
						gen[i][j] = verb4[(int)(Math.random() * 50)];
						posGen[i][j] = 5;
					} else if (rand == 6) {
						gen[i][j] = verb5[(int)(Math.random() * 50)];
						posGen[i][j] = 6;
					} else if (rand == 7) {
						gen[i][j] = verb6[(int)(Math.random() * 50)];
						posGen[i][j] = 7;
					} else if (rand == 8) {
						gen[i][j] = pronoun1[0];
						posGen[i][j] = 8;
					} else if (rand == 9) {
						gen[i][j] = pronoun2[(int)(Math.random() * 3)];
						posGen[i][j] = 9;
					} else if (rand == 10) {
						gen[i][j] = article1[(int)(Math.random() * 2)];
						posGen[i][j] = 10;
					} else if (rand == 11) {
						gen[i][j] = article2[(int)(Math.random() * 2)];
						posGen[i][j] = 11;
					} 
					
		    	} else { // PROCESSO NORMAL DE TROCA DE GENES
		    		if (j < crossover){ // PAI
		    			newGen[i][j] = gen[p1][j];
		    			newPosGen[i][j] = posGen[p1][j];
		    		} else { // MÃE
		    			newGen[i][j] = gen[p2][j];
		    			newPosGen[i][j] = posGen[p2][j];
		    		}
		    	}
		    }
		}
		
		for(int i = 0; i < 10; i++){
		   for(int j = 0; j < 4; j++){
		      gen[i][j] = newGen[i][j];
		      posGen[i][j] = newPosGen[i][j];
		   }
		}
		
		//System.out.println(gen[0][0] + " "+ gen[0][1] + " " + gen[0][2] + " " + gen[0][3]);
		
	}
	
	public ChatScreen() {

		// create File object
		File dir = new File("c:/Chatbot");
		File file = new File("c://Chatbot/log.txt");

		
		try {
			dir.mkdir();
		    file.createNewFile();

		} catch (IOException ioe) {
			System.out.println("Error while Creating File in Java" + ioe);
		}
		
		
		setTitle("Chat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 712, 621);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setResizable(false);
		
		JTextArea textArea = new JTextArea();
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 23, 488, 351);
		contentPane.add(scrollPane);
		JTextArea textAreaChat = new JTextArea();
		textAreaChat.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
		scrollPane.setViewportView(textAreaChat);
		textAreaChat.setFocusable(false);
		textAreaChat.setEditable(false);
	
		textAreaChat.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		initGenerations();
		
		while(!evaluate()){
			nextGen();
			generations++;
		}
		
		System.out.println(generations);
		textAreaChat.append("BOT digitou: " + gen[perfect][0] + " "+ gen[perfect][1] + " " + gen[perfect][2] + " " + gen[perfect][3] + "\n\n");
		
		textArea.addKeyListener(new KeyAdapter() {
			int spaceCount;
	
			public void keyPressed(KeyEvent e) {
			    if(e.getKeyCode() == KeyEvent.VK_ENTER){
			    	e.consume();
			    	
			    	spaceCount = 0;
			    	String str = textArea.getText();
			    	
			    	for (char c : str.toCharArray()) {
			    	    if (c == ' ') {
			    	         spaceCount++;
			    	    }
			    	}
			    	
			    	if(!textArea.getText().equals("") && str.length() != spaceCount) { // Se há texto para ser digitado, ele envia a frase gerada pelo usuário para o ChatBot
				        textAreaChat.append("Você digitou: " + textArea.getText() + "\n\n");
				        try {
				    		FileWriter fw;
				    		BufferedWriter bw;
					        fw = new FileWriter(file, true);
					        bw = new BufferedWriter(fw);
					        bw.write(textArea.getText());
					        bw.newLine();
					        bw.flush();
					        bw.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				        textArea.setText("");
				        
						initGenerations();
						
						while(!evaluate()){
							nextGen();
							generations++;
						}
						
						System.out.println(generations);
						textAreaChat.append("BOT digitou: " + gen[perfect][0] + " "+ gen[perfect][1] + " " + gen[perfect][2] + " " + gen[perfect][3] + "\n\n");
						generations = 0;
						perfect = -1;
			    	}

			    }
			}
		});
		
		textArea.setBorder(new LineBorder(new Color(0, 0, 0)));
		textArea.setBounds(22, 439, 488, 120);
		contentPane.add(textArea);
		
		JButton btnNewButton = new JButton("Enviar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
		        System.out.println("TESTANDO");
			}
		});
		btnNewButton.setBounds(543, 439, 130, 120);
		contentPane.add(btnNewButton);
		

		setLocationRelativeTo(null);
	}
}
