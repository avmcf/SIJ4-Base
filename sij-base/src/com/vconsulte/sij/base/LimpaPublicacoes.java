package com.vconsulte.sij.base;

import java.io.File;
import java.io.IOException;

class LimparPublicacoes {  
	
	static int k = 0;
    
    public static void main(String[] args) throws IOException {  
    	String pastaSaida;
    	com.vconsulte.sij.base.Configuracao.carregaConfig();
		pastaSaida = com.vconsulte.sij.base.Parametros.PASTASAIDA;
    	LimparPublicacoes ld = new LimparPublicacoes();  
        ld.remover (new File (pastaSaida));
        k++;
    }  
    private void remover (File f) {  
        if (f.isDirectory()) {  
            File[] files = f.listFiles();  
            for (int i = 0; i < files.length; ++i) {  
                remover (files[i]); 
                k++;
            }  
        }  
        f.delete();  
        k++;
    }  
}
