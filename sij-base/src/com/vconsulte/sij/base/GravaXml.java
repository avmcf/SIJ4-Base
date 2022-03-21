package com.vconsulte.sij.base;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.chemistry.opencmis.client.api.Folder;
//import java.text.SimpleDateFormat;
//import java.util.Date;

//import com.vconsulte.sij.base.Parametros;

public class GravaXml {
	static String bloco = "";
	static int k = 0;
	static Edital edital = new Edital();
//	static String path = "/Users/avmcf/sij/saida_editais/";
	
	public static void main(String pasta, boolean arquivo, String strEdicao) throws IOException {
		String arquivoSaida;
		//com.vconsulte.sij.base.Configuracao.carregaConfig();
		//pastaSaida = com.vconsulte.sij.base.Parametros.PASTASAIDA + "/";
		//pastaSaida = "/Users/avmcf/vms/shared/teste/" + edital.getFileName() + ".metadata.properties.xml";
		//String arquivoSaida = pasta + edital.getFileName() + ".metadata.properties.xml";
		if(arquivo) {
			arquivoSaida = pasta + edital.getFileName() + ".metadata.properties.xml";
			formataArquivo();
		} else {
			arquivoSaida = pasta + ".metadata.properties.xml";
			formataPasta(strEdicao);
		}
		gravaArquivo(arquivoSaida);
	}
	
	public static void gravaArquivo(String pasta) throws IOException {
		File file = new File(pasta);
	
				
	//	long begin = System.currentTimeMillis();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));

		writer.write(bloco);
	//	long end = System.currentTimeMillis();
	//	writer.write("Tempo de gravação: " + (end - begin) + "ms.");
		//Criando o conteúdo do arquivo
		writer.flush();
		//Fechando conexão e escrita do arquivo.
		writer.close();
		//System.out.println("Arquivo gravado em: " + path);
		k++;
	}
	
	public static void formataPasta(String strEdicao){

		bloco = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n";
		bloco = bloco + "<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">" + "\n";
		bloco = bloco + "<properties>" + "\n";

		bloco = bloco + "<entry key=\"type\">cm:folder</entry>" + "\n";
		bloco = bloco + "<entry key=\"aspects\">cm:versionable,cm:generalclassifiable,sij:edicoes</entry>" + "\n";
		bloco = bloco + "<entry key=\"cm:title\">" + "Edição do Diário Oficial" + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"cm:description\">" + edital.getGrupo() + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"cm:author\">sij" + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"cm:publisher\">" + "VeC Consultoria Ltda." + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"cm:identifier\">" + edital.getFolderName() + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"cm:rights\">" + "VeC Consultoria Ltda." + "</entry>" + "\n";
				
		bloco = bloco + "<entry key=\"sij:edcEdicao\">" +  edital.getStrEdicao() + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"sij:edcStrEdicao\">" +  strEdicao + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"sij:edcTribunal\">" +  edital.getTribunal() + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"sij:edcIndexados\">" + "novo" + "</entry>" + "\n";
		bloco = bloco + "</properties>";
	}
	
	public static void formataArquivo(){

		bloco = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n";
		bloco = bloco + "<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">" + "\n";
		bloco = bloco + "<properties>" + "\n";
		
		//bloco = bloco + "<entry key=\"type\">sij:documento</entry>" + "\n";
		//bloco = bloco + "<entry key=\"type\">cm:content</entry>" + "\n";			deu merda no query
		
		bloco = bloco + "<entry key=\"type\">sij:publicacao</entry>" + "\n";
		
		//bloco = bloco + "<entry key=\"aspects\">cm:versionable,cm:generalclassifiable,sij:publicacao</entry>" + "\n";
		
		bloco = bloco + "<entry key=\"aspects\">cm:versionable,cm:generalclassifiable</entry>" + "\n";
		bloco = bloco + "<entry key=\"cm:title\">" + "Publicação do Diário Oficial" + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"cm:description\">" + edital.getDescricao() + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"cm:author\">sij" + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"cm:publisher\">" + ">VeC Consultoria Ltda." + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"cm:type\">" + "Adobe PDF Document" + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"cm:identifier\">" + edital.getFileName() + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"cm:rights\">VeC Consultoria Ltda.</entry>" + "\n";
		
		bloco = bloco + "<entry key=\"sij:pubEdicao\">" +  edital.getStrEdicao() + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"sij:pubStrEdicao\">" +  edital.getStrEdicao() + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"sij:pubTribunal\">" +  edital.getTribunal() + "</entry>" + "\n";
		//bloco = bloco + "<entry key=\"sij:pubProcesso\">" + edital.getProcessoLinha() + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"sij:pubProcesso\">" + edital.getProcessoNumero() + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"sij:pubVara\">" + edital.getVara() +"</entry>" + "\n";
		bloco = bloco + "<entry key=\"sij:pubGrupo\">" + edital.getGrupo()  + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"sij:pubAssunto\">" + edital.getAssunto() + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"sij:pubAdvogados\">" + edital.getAtores() + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"sij:pubIntimados\">" + edital.getIntimados() + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"sij:pubFolder\">" + edital.getFolder() + "</entry>" + "\n";
		bloco = bloco + "<entry key=\"sij:pubNumeroOrdem\">" + edital.getNumeroOrdem()+ "</entry>" + "\n";
		
		//bloco = bloco + "<entry key=\"sij:jurNumProcesso\">" + edital.getProcesso() + "</entry>" + "\n";
		//bloco = bloco + "<entry key=\"sij:jurTribunal\">" + edital.getTribunal() + "</entry>" + "\n";
		//bloco = bloco + "<entry key=\"sij:jurVara\">" + edital.getVara() + "</entry>" + "\n";
		bloco = bloco + "</properties>";
	}
}