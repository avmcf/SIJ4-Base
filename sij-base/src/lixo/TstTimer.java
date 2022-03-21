package lixo;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import com.vconsulte.sij.base.Comuns;

public class TstTimer {
	static int k = 0;
	public static void main(String[] args) {
		
		Timer cronometro = new Timer();
		TimerTask tarefa = new TimerTask() {
		    @Override
		    public void run() {
		    	apresentaMenssagem("Timer: Fim do loop de espera - \t\t","informativa", null);
		    }
		    
		};
		apresentaMenssagem("Timer: Início do loop de espera - \t\t","informativa", null);
		//int milissegundos = 600000;
		 int milissegundos = 1300000;
		cronometro.schedule(tarefa, milissegundos);
		
	}
	
	private static String obtemHrAtual() {

		String hr = "";
		String mn = "";
		String sg = "";
		Calendar data = Calendar.getInstance();
		hr = Integer.toString(data.get(Calendar.HOUR_OF_DAY));
		mn = Integer.toString(data.get(Calendar.MINUTE));
		sg = Integer.toString(data.get(Calendar.SECOND));

		return completaEsquerda(hr,'0',2)+":"+completaEsquerda(mn,'0',2)+":"+completaEsquerda(sg, '0', 2);
	}
	
	private static String completaEsquerda(String value, char c, int size) {
		String result = value;
		while (result.length() < size) {
			result = c + result;
		}
		return result;
	}
	
	private static void apresentaMenssagem(String mensagem, String tipoMensagem, String erro) {
		Comuns.apresentaMenssagem(mensagem, "BATCH", tipoMensagem, erro);	
	}
	
	

}
