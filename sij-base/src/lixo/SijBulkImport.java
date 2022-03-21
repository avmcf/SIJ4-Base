package lixo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.vconsulte.sij.base.Configuracao;
import com.vconsulte.sij.base.Parametros;

public class SijBulkImport {
	static String url;
	static String pastaPublicacoes;
	static String pastaCarregamento;
	
	static int k = 0;
	
	public static void main(String[] args) throws Exception {
		com.vconsulte.sij.base.Parametros.carregaTabelas();
    	com.vconsulte.sij.base.Configuracao.carregaConfig();
    	url = com.vconsulte.sij.base.Parametros.URL;
    	pastaPublicacoes = com.vconsulte.sij.base.Parametros.PASTASAIDA;
    	pastaCarregamento = com.vconsulte.sij.base.Parametros.PASTACARREGAMENTO;
		enviaArquivos(pastaPublicacoes, pastaCarregamento, url);
	}

	public static void enviaArquivos(String origem, String destino, String urlServidor) throws IOException {
		int exitCode;
		System.out.println("origem: " + origem);
		System.out.println("destino: " + destino);
		String url = urlServidor.replaceAll("http://", "");
		String commando = "curl -v -u admin:aaa -L POST --url \"" + url + "/alfresco/s/bulkfsimport/initiate\" --data \"targetPath=" + destino + "&sourceDirectory=" + origem + "&existingFileMode=REPLACE\"";
		
			ProcessBuilder processBuilder = new ProcessBuilder(commando.split(" "));
			processBuilder.directory(new File("/home/"));
			//Process processo = processBuilder.start();
			Process processo = Runtime.getRuntime().exec(commando);
		
		InputStream inputStream = processo.getInputStream();
		exitCode = processo.exitValue();
		k++;
		processo.destroy();
	}
}
