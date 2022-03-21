package lixo;

public class Importacao {
	
	private static  Process processo;

	public static void main(String[] args) {

		try {
			System.out.println("*** Primeiro Catch ***");
			processo = new ProcessBuilder("./roda.sh").start();
		} catch (Exception excessao) {		
			System.out.println(excessao);
		}
		
		try {
			if(processo.isAlive()) {
				System.out.println("Bulkimport em execução !");
				Thread.sleep(150000);
				processo.destroy();
			}
		}
		catch (InterruptedException excessao) {
			System.out.println(excessao);
		}
	}
}