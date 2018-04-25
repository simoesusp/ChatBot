import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLButtonElement;

import database.DataBase;
import manipulador.ControleES;
import model.Synset;



public class Dict {

	public void findVerbs() {
		File dir = new File("c:/Chatbot");
		File file = new File("c://Chatbot/verbs.txt");

		try {
			dir.mkdir();
		    file.createNewFile();
		} catch (IOException ioe) {
			System.out.println("Error while Creating File in Java" + ioe);
		}
		
		ControleES controleES = new ControleES();
		controleES.init();
		System.out.println(DataBase.tamanhoBase());
		
		List<Synset> list = null;
		
		list = DataBase.getSynsets();

		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage site = null;
		HtmlPage page2 = null;

		try {
			site = webClient.getPage("https://www.conjugacao.com.br/");
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HtmlForm form = (HtmlForm) site.getElementById("searchForm");

		HtmlSubmitInput button = form.getInputByValue("conjugar");
		HtmlTextInput textField = form.getInputByName("q");

		for(int i = 0; i < DataBase.tamanhoBase(); i++) {
			if(list.get(i).getType() == "VERBO") {
				String str = list.get(i).toString();
				if(!str.contains("-")) {
					String verb = str.substring(str.indexOf(" ") + 1, str.indexOf(" ", str.indexOf(" ") + 1));
					System.out.println(verb);
					
				    textField.setValueAttribute(verb);
					try {
						page2 = button.click();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(page2.asText().length() > 1000) {
						String content = page2.asText().replaceAll("\n", "").replaceAll("\r", " ");
						String verb1, verb2, verb3, verb4, verb5, verb6;
						verb1 = content.substring(content.indexOf("Presente") + 11, content.indexOf("tu", content.indexOf("Presente") + 1)); // 1 PRESENTE DO INDICATIVO
						verb2 = content.substring(content.indexOf("ele", content.indexOf(verb1)) + 3, content.indexOf("nós", content.indexOf(verb1))); // 3 PRESENTE DO INDICATIVO
						verb3 = content.substring(content.indexOf("Pretérito Perfeito") + 21, content.indexOf("tu", content.indexOf("Pretérito Perfeito") + 1)); // 1 Pretérito Perfeito
						verb4 = content.substring(content.indexOf("ele", content.indexOf(verb3)) + 3, content.indexOf("nós", content.indexOf("Pretérito Perfeito"))); // 3 Pretérito Perfeito
						verb5 = content.substring(content.indexOf("Futuro do Presente") + 21, content.indexOf("tu", content.indexOf("Futuro do Presente") + 3)); // 1 Futuro do Presente
						verb6 = content.substring(content.indexOf("ele", content.indexOf(verb5)) + 3, content.indexOf("nós", content.indexOf("Futuro do Presente"))); // 3 Futuro do Presente
					
						String line = "c1" + verb1 + "c2" + verb2 + "c3" +verb3 + "c4" + verb4 + "c5" + verb5 + "c6" + verb6;

						try {
							FileWriter fw;
							BufferedWriter bw;
							fw = new FileWriter(file, true);
							bw = new BufferedWriter(fw);
							bw.write(line);
							bw.newLine();
							bw.flush();
							bw.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		}
		
		webClient.close();
	}
	
	public static void main(String[] args) {
		Dict dict = new Dict();
		//dict.findVerbs();
	}
}
