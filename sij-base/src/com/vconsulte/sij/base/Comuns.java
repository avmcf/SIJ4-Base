package com.vconsulte.sij.base;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;
import org.apache.chemistry.opencmis.client.api.Session;

public class Comuns {
	
	static boolean janelaAtiva = false;
	static int k = 0;

	public static String obtemHrAtual() {

		String hr = "";
		String mn = "";
		String sg = "";
		Calendar data = Calendar.getInstance();
		hr = Integer.toString(data.get(Calendar.HOUR_OF_DAY));
		mn = Integer.toString(data.get(Calendar.MINUTE));
		sg = Integer.toString(data.get(Calendar.SECOND));

		return completaEsquerda(hr,'0',2)+":"+completaEsquerda(mn,'0',2)+":"+completaEsquerda(sg, '0', 2);
	}
	
	public static void apresentaMenssagem(String mensagem, String tipoProcessamento, String tipoMensagem, String erro) {
		if(tipoMensagem.equals("informativa")) {
			if(tipoProcessamento.equals("DESKTOP")){
				MsgWindow msgWindow = new MsgWindow();
		//		if(!janelaAtiva) {
		//			msgWindow.montaJanela();
		//			janelaAtiva = true;
		//		}
				msgWindow.incluiLinha(obtemHrAtual() + " - " + mensagem);
			} 
			if(tipoProcessamento.equals("BATCH")) {
				System.out.println(obtemHrAtual() + " " + mensagem);
			}
		}
		
		if(tipoMensagem.equals("erro") && tipoProcessamento.equals("DESKTOP")){
			JOptionPane.showMessageDialog(null, mensagem + erro);
		} else if(tipoMensagem.equals("erro") && tipoProcessamento.equals("BATCH")) {
			System.out.println(obtemHrAtual() + " " + mensagem);
		}

		if(tipoMensagem.equals("final") && tipoProcessamento.equals("DESKTOP")) {
			JOptionPane.showMessageDialog(null, mensagem);
		} else if(tipoMensagem.equals("erro") && tipoProcessamento.equals("BATCH")) {
			System.out.println(obtemHrAtual() + " " + mensagem);
		}
	}

	public static void gravaLog(String pastaLog, String nomeArquivo, String rotina, List log) throws IOException {
		
		String nomeLog = "";
		int i = 0;

		nomeLog = pastaLog + "/" + rotina + "-" + nomeArquivo + ".log";
		
		//FileWriter subjAtualizado = new FileWriter(nomeLog);
		PrintWriter outReg = new PrintWriter(nomeLog);		
		while(i <= log.size()-1){
			outReg.printf("%s\n",log.get(i));
			i++;		 
		}
		outReg.close();
		log.clear();
	}
	
	public static void gravaArquivoTexto(String pasta, String nomeArquivo, List texto) throws IOException {
		
		String nomeLog = pasta + "/" + nomeArquivo;
		int i = 0;

		//FileWriter subjAtualizado = new FileWriter(nomeLog);
		PrintWriter outReg = new PrintWriter(nomeLog);		
		while(i <= texto.size()-1){
			outReg.printf("%s\n",texto.get(i));
			i++;		 
		}
		outReg.close();
		texto.clear();
	}
	
	public static Session conectaServidor(String usuario, String password, String url) {
		InterfaceServidor conexao = new InterfaceServidor();
		Session sessao;
		conexao.setUser(usuario);
		conexao.setPassword(password);
		conexao.setUrl(url);
		sessao = InterfaceServidor.serverConnect();
		return sessao;
	}
	
	public static void finalizaProcesso(String tipoProcessamento) {
		apresentaMenssagem("Fim do Processamento.", tipoProcessamento, "informativa", null);
        System.exit(0);
	}
	
	public static String completaEsquerda(String value, char c, int size) {
		String result = value;
		while (result.length() < size) {
			result = c + result;
		}
		return result;
	}

	public static String formataPalavra(String palavra) {    			// retira acentos e transforma para minusculas
		 
		palavra = palavra.replaceAll("[aáàãâäåAÁÀÃÂÄÅ]","a");
        palavra = palavra.replaceAll("[eéèêëEÉÈÊË]","e");
        palavra = palavra.replaceAll("[iíìîïIÍÌÎÏ]","i");
        palavra = palavra.replaceAll("[oóòôöOÓÒÔÖÕ]","o");
        palavra = palavra.replaceAll("[uúùûüUÚÙÛÜ]","u");
        palavra = palavra.replaceAll("[çÇ]","c");
        palavra = palavra.replaceAll("[B]","b");
        palavra = palavra.replaceAll("[C]","c");
        palavra = palavra.replaceAll("[D]","d");
        palavra = palavra.replaceAll("[F]","f");
        palavra = palavra.replaceAll("[G]","g");
        palavra = palavra.replaceAll("[H]","h");
        palavra = palavra.replaceAll("[J]","j");
        palavra = palavra.replaceAll("[L]","l");
        palavra = palavra.replaceAll("[M]","m");
        palavra = palavra.replaceAll("[N]","n");
        palavra = palavra.replaceAll("[P]","p");
        palavra = palavra.replaceAll("[R]","r");
        palavra = palavra.replaceAll("[S]","s");
        palavra = palavra.replaceAll("[T]","t");
        palavra = palavra.replaceAll("[Q]","q");
        palavra = palavra.replaceAll("[V]","v");
        palavra = palavra.replaceAll("[X]","x");
        palavra = palavra.replaceAll("[Z]","z");
        palavra = palavra.replaceAll("[W]","w");
        palavra = palavra.replaceAll("[Y]","y");
        palavra = palavra.replaceAll("[K]","k");
        
        String palavraFormatada = "";

        for(int i=0; i <= palavra.length()-1; i++) {
        	if ((palavra.charAt(i) == ' ' && (i > 0 && palavra.charAt(i-1) == ' '))){
        		continue;
        	} else {
        		palavraFormatada = palavraFormatada + palavra.charAt(i);
        	}        	
        }      
    return palavraFormatada;  
    }
}