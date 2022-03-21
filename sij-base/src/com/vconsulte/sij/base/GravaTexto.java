package com.vconsulte.sij.base;

	import java.io.BufferedWriter;
	import java.io.FileWriter;
	import java.io.IOException;
import java.util.ArrayList;

public class GravaTexto {
	
	static int k = 0;
	static Edital edital = new Edital();
	static StringBuilder bloco = new StringBuilder();
	static Edital Publicacao = new Edital();
	
	public static void main(String arquivoSaida, int sequencialSecao, int sequencialGrupo, int sequencialAssunto, int sequencialProcesso, String versaoSplitter) throws IOException {
		k++;
		bloco.setLength(0);
		formataArquivo(sequencialSecao, sequencialGrupo, sequencialAssunto, sequencialProcesso, versaoSplitter);
		gravaArquivo(arquivoSaida);
	}
	
	public static void gravaArquivo(String pastaSaida) throws IOException {
		String bufferSaida = "";
		FileWriter arqSaida = new FileWriter(pastaSaida);
		BufferedWriter bw = new BufferedWriter(arqSaida);
		bufferSaida = bloco.toString();
		bw.write(bufferSaida);
		bw.close();
		k++;
	}
	
	private static String centralizaLinha(String linha) {
		StringBuilder preenchimento = new StringBuilder();
		int espacos = 100 - linha.length();
		for(int x = 0; x<(espacos/2); x++) {
			preenchimento = preenchimento.append(" ");
		}
		return preenchimento + linha;
	}
	
	public static void formataArquivo(int sequencialSecao, int sequencialGrupo, int sequencialAssunto, int sequencialProcesso, String versaoSplitter){
		ArrayList<String> texto;
		
		bloco.append(centralizaLinha(Publicacao.getTitulo1()+"\n"));
		bloco.append(centralizaLinha(Publicacao.getTitulo2()+"\n"));
		bloco.append(centralizaLinha(Publicacao.getTitulo3()+"\n"));
		bloco.append(centralizaLinha(Publicacao.getTitulo4()+"\n"));
		bloco.append(centralizaLinha(Publicacao.getTitulo5()+"\n"));
		bloco.append(centralizaLinha("--- * ---"+"\n"));
		bloco.append(Publicacao.getVara()+"\n");
		bloco.append(Publicacao.getGrupo()+"\n");
		bloco.append(Publicacao.getAssunto()+"\n");
		bloco.append(Publicacao.getProcessoLinha()+"\n");
		bloco.append(Publicacao.getAtores()+"\n");
		bloco.append(Publicacao.getIntimados()+"\n");
		
		texto = Publicacao.getTexto();
		for(String linhas : texto){
			bloco.append(linhas + "\n");
			k++;
		}
		k++;
	}
}