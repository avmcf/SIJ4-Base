package com.vconsulte.sij.base;

/*
 * Seleciona edição do Diário Oficial 
 */

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

public class SelecionaDO {
	
	public static File selecionaEdicao() throws IOException{	
		File diarioEscolhido = null;
		JFileChooser arquivo = new JFileChooser();
		arquivo.showDialog(null, "Selecione um Diário Oficial");
        File diario = arquivo.getSelectedFile();
        if (diario != null){ 
        	diarioEscolhido = diario;
        }
        return diarioEscolhido;
	}
}
