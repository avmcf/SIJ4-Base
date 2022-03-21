package com.vconsulte.sij.base;

	import java.io.BufferedReader;
	import java.io.File;
	import java.io.FileInputStream;
	import java.io.IOException;
	import java.io.InputStreamReader;
	import java.net.URISyntaxException;

//import org.apache.chemistry.opencmis.client.api.Session;

//import com.vconsulte.sij.base.*;

public class Configuracao {

	static String caminho = "";
	static File config;
	static int k = 0;
	
	
	//public static void carregaConfig(File config) throws IOException {
	public static void carregaConfig() throws IOException {

		//gravaLog(obtemHrAtual() + "Carga configuração");
		String linha = "";
		String linhaTratada = "";
		int x = 0;
		pastaCorrente();
		config = new File("/"+caminho+"split.cnf");
        FileInputStream arquivoIn = new FileInputStream(config);
		BufferedReader registro = new BufferedReader(new InputStreamReader((arquivoIn), "UTF-8"));
        
        while(linha != null){
	    	linha = registro.readLine();
	    	
	    	if(linha == null) {
	    		break;
	    	} else {
	    		linhaTratada = com.vconsulte.sij.base.Comuns.formataPalavra(linha);
	    	}
	    	switch(x) {
				case 0:
					com.vconsulte.sij.base.Parametros.CLIENTE = linha;					// 1
					break;
				case 1:
					com.vconsulte.sij.base.Parametros.CONEXAO = linha;					// 2
					break;
				case 2:
					com.vconsulte.sij.base.Parametros.SYSOP = linha;					// 3
					break;
				case 3:
					com.vconsulte.sij.base.Parametros.URL = linha;						// 4
					break;
				case 4:
					com.vconsulte.sij.base.Parametros.LOGFOLDER = linha;				// 5: Pasta local para gravar Logs
					break;
				case 5:
					com.vconsulte.sij.base.Parametros.PASTACARREGAMENTO = linha;		// 6: Pasta alfresco carregamento de publicacoes e entrada para o clip
					break;
				case 6:
					com.vconsulte.sij.base.Parametros.PASTAPUBLICACOES = linha;			// 7: Pasta publicacoes alfresco -> Saída do clip
					break;		
				case 7:
					com.vconsulte.sij.base.Parametros.PASTATOKENS = linha;				// 8: Pasta Alfresco com tabelas tokens
					break;
				case 8:
					com.vconsulte.sij.base.Parametros.PASTASAIDA = linha;				// 9: Pasta local onde split gravar publicações
					break;
				case 9:
					com.vconsulte.sij.base.Parametros.PASTAENTRADA = linha;				// SEM USO - 10: Pasta local origem onde split lê edicoes.pdf
					break;
				case 10:
					com.vconsulte.sij.base.Parametros.PASTAEDICOES = linha;				// 11: Pasta local onde split processa edicoes.pdf
					break;
				case 11:
					com.vconsulte.sij.base.Parametros.TIPODOCUMENTO = linha;			// 12: Tipo documento alfresco (Publicacao)
					break;
				case 12:
					com.vconsulte.sij.base.Parametros.TIPOPROCESSAMENTO = linha;		// 13: Tipo processamento (BATCH ou DESKTOP)
					break;
				case 13:
					com.vconsulte.sij.base.Parametros.TIPOARQUIVOSAIDA = linha;			// 14: Tipo arq saida (texto ou PDF)
					break;
				case 14:
					com.vconsulte.sij.base.Parametros.PASTADEEDICOES = linha;			// SEM USO - 15: Pasta alfresco onde split lê edicoes.pdf (modo não SERVIDOR)
					break;
				case 15:
					com.vconsulte.sij.base.Parametros.PASTARELATORIOS = linha;			// 16: Pasta alfresco para os relatorios de processamento
					break;
	    	}
	    	x++;
        }
        registro.close();
	}
	
	public static void pastaCorrente() {
		try {
			caminho = Configuracao.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			caminho = caminho.substring(1, caminho.lastIndexOf('/') + 1);
			k++;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	
}