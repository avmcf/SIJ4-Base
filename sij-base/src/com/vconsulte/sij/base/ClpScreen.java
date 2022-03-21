package com.vconsulte.sij.base;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

public class ClpScreen extends JFrame implements ActionListener {
	
	// variáveis
	private static final long serialVersionUID = 1L;
	private TextArea areaTexto = new TextArea();
	private JScrollPane scrollTexto = new JScrollPane(areaTexto);
	private JPanel textoPanel = new JPanel();
	private JLabel lbl = new JLabel();
	
	public int k = 0;				// para teste
	
	// construtor
	public ClpScreen() {
		super("dpk");
		this.setSize(600, 500);
		this.setTitle("Indexação do Diario Oficial");
		this.setFont(new Font("Times", 0, 11));
		this.setResizable(false);
		int policy = scrollTexto.getVerticalScrollBarPolicy(); scrollTexto.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
		
		scrollTexto.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		scrollTexto.setBounds(20, 195, 435, 70);
		scrollTexto.add(areaTexto);
		
		k = 0;
		
	}
	
	public void montaJanela() {
		
		// montando area de texto no textoPanel
		textoPanel.setSize(600, 500);
		textoPanel.add(areaTexto, JPanel.TOP_ALIGNMENT);
		getContentPane().add(textoPanel, BorderLayout.NORTH);

		// adicionais
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void incluiLinha(String linha) {
		areaTexto.append(linha + "\n");
	} 
	
	public void incluiLabel(String linha) {
		lbl.setText(linha);
	} 
	
	public static void main (String args[]) {
		MsgWindow msg = new MsgWindow();
		msg.montaJanela();
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}