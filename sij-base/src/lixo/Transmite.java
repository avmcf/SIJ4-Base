package lixo;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.vconsulte.sij.base.Configuracao;
import com.vconsulte.sij.base.Parametros;

import lixo.Transmite;

import com.vconsulte.sij.base.Comuns;

public class Transmite {
	final static String tipoProcessamento = "BATCH";
	private static  Process processo;
	static Comuns comuns = new Comuns();
	static Transmite transmite = new Transmite();
	static Configuracao configuracao = new Configuracao();
	static String pastaLog;
	static String pastaSaida;
	static String pastaOrigem;
	static String rotina;
	static List <String> sijLog = new ArrayList<String>();
	private static final Logger log = Logger.getLogger(Transmite.class.getName());
	
	public static void main(String[] args) throws Exception {
		transmite();
	}
	
	public static void transmite() throws IOException {
		configuracao.carregaConfig();
		pastaOrigem = com.vconsulte.sij.base.Parametros.PASTAORIGEM;
		pastaLog = com.vconsulte.sij.base.Parametros.LOGFOLDER;		
        final Transmite shell = new Transmite();	
        registraLog("(Transmite) Início da transmissão para o servidor Alfresco.");
        apresentaMenssagem("Início da transmissão para o servidor Alfresco.","informativa", null);
        shell.executeCommando(pastaOrigem + "/transmit.sh");
        registraLog("(Transmite) Fim da transmissão para o servidor Alfresco.");
        apresentaMenssagem("Fim da transmissão para o servidor Alfresco.","informativa", null);
        shell.executeCommando(pastaOrigem + "/clear.sh");
        comuns.gravaLog(pastaLog, Comuns.obtemHrAtual().replace(":", ""), "Transmite",sijLog);
	}
	
	private void executeCommando(final String commando) throws IOException {

        final ArrayList<String> commands = new ArrayList<String>();
        commands.add("/bin/bash");
        commands.add("-c");
        commands.add(commando);

        BufferedReader br = null;

        try {
        	apresentaMenssagem("(Transmite) Transmitindo.","informativa", null);
            final ProcessBuilder p = new ProcessBuilder(commands);
            final Process process = p.start();
            final InputStream is = process.getInputStream();
            final InputStreamReader isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            String line;
            while((line = br.readLine()) != null) {
            	apresentaMenssagem("(Transmite) Retorno do comando = [" + line + "]","informativa", null);
            }
        } catch (IOException ioe) {
        	registraLog("(Transmite) Erro ao executar comando shell " + ioe.getMessage());
        	apresentaMenssagem("(Transmite) Erro ao executar comando shell " + ioe.getMessage(),"informativa", null);
            ((Logger) log).severe("(Transmite) Erro ao executar comando shell" + ioe.getMessage());
            throw ioe;
        } finally {
            secureClose(br);
        }
    }
	
	private static void secureClose(final Closeable resource) {
        try {
            if (resource != null) {
                resource.close();
            }
        } catch (IOException ex) {
            ((Logger) log).severe("Erro = " + ex.getMessage());
        }
    }

	private static void apresentaMenssagem(String mensagem, String tipoMensagem, String erro) {
		Comuns.apresentaMenssagem(mensagem, tipoProcessamento, tipoMensagem, erro);	
	}
	private static void registraLog(String registroLog) {
		sijLog.add(Comuns.obtemHrAtual() + " - " + registroLog);
	}
	
	
}
