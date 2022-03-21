package com.vconsulte.sij.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.chemistry.opencmis.client.api.Session;

/**
 * This class holds common properties and methods for the example classes.
 */

public class Edital extends Base{
	
	// propriedades do documento
    private static Date edicao;
	private static String sequencial;
	private static String seqEdicao;
	private static String tribunal;
    private static String tipoDocumento;
    private static String descricao;
    private static String processoNumero;
    private static String processoLinha;
    private static String strEdicao;
    private static String cliente;
    private static String folder;
    private static String label;
    private static String observacoes;
    private static int numeroOrdem;
    
    // parametros para o texto
	private static String titulo1;
	private static String titulo2;
	private static String titulo3;
	private static String titulo4;
	private static String titulo5;
    private static String vara;
	private static String grupo;
	private static String assunto;
	private static String atores = "";
	private static String intimados = "";
	private static ArrayList<String> texto = new ArrayList<String>();
	private static ArrayList<String> introducao = new ArrayList<String>();
	
	public static String getStrEdicao() {
		return strEdicao;
	}

	public void setStrEdicao(String setStrEdicao) {
		Edital.strEdicao = setStrEdicao;
	}

	public Date getEdicao() {
		return Edital.edicao;
	}

	public void setEdicao(Date edicao) {
		Edital.edicao = edicao;
	}
	
	public static String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String setTipoDocumento) {
		Edital.tipoDocumento = setTipoDocumento;
	}
	
	// ------------------------------------

	public ArrayList<String> getIntroducao() {					// corpo da publicacao
		return introducao;
	}
	
	public void setIntroducao(ArrayList<String> introducao) {	// corpo da publicacao
		Edital.introducao = introducao;
	}
	
	// ------------------------------------

	public ArrayList<String> getTexto() {							// corpo da publicacao
		return texto;
	}
	
	public void setTexto(ArrayList<String> texto) {					// corpo da publicacao
		Edital.texto = texto;
	}
	
	// ------------------------------------
	
	public String getSeqEdicao() { 
		return seqEdicao; 
	}
	
	public void setSeqEdicao(String seqEdicao) {
		Edital.seqEdicao = seqEdicao;
	}
	
	// ------------------------------------
	
	public String getSequencial() { 
		return sequencial; 
	}
	
	public void setSequencial(String sequencial) {
		Edital.sequencial = sequencial;
	}
	
	// ------------------------------------
	
	public String getTribunal() { 
		return tribunal; 
	}
	
	public void setTribunal(String tribunal) {
		Edital.tribunal = tribunal;
	}
	
	// ------------------------------------
	
	public String getDescricao() { 
		return descricao; 
	}
	
	public void setDescricao(String descricao) {
		Edital.descricao = descricao;
	}
	
	public String getProcessoLinha() { 
		return processoLinha; 
	}
	
	public void setProcessoLinha(String processoLinha) {
		Edital.processoLinha = processoLinha;
	}
	
	public String getProcessoNumero() { 
		return processoNumero; 
	}
	
	public void setProcessoNumero(String processoNumero) {
		Edital.processoNumero = processoNumero;
	}
	
	public String getVara() {
		return vara;
	}

	public void setVara(String setVara) {
		Edital.vara = setVara;
	}
	
	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String setGrupo) {
		Edital.grupo = setGrupo;
	}
	
	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String setAssunto) {
		Edital.assunto = setAssunto;
	}
	
	public String getTitulo1() {
		return titulo1;
	}

	public void setTitulo1(String setTitulo1) {
		Edital.titulo1 = setTitulo1;
	}
	
	public String getTitulo2() {
		return titulo2;
	}

	public void setTitulo2(String setTitulo2) {
		Edital.titulo2 = setTitulo2;
	}
	
	public String getTitulo3() {
		return titulo3;
	}

	public void setTitulo3(String setTitulo3) {
		Edital.titulo3 = setTitulo3;
	}
	
	public String getTitulo4() {
		return titulo4;
	}

	public void setTitulo4(String setTitulo4) {
		Edital.titulo4 = setTitulo4;
	}
	
	public String getTitulo5() {
		return titulo5;
	}

	public void setTitulo5(String setTitulo5) {
		Edital.titulo5 = setTitulo5;
	}
	
	public String getAtores() {
		return atores;
	}

	public void setAtores(String setAtores) {
		Edital.atores = setAtores;
	}
	
	public String getIntimados() {
		return intimados;
	}

	public void setIntimados(String setIntimados) {
		Edital.intimados = setIntimados;
	}
	
	public String getCliente() {
		return cliente;
	}

	public void setCliente(String setCliente) {
		Edital.cliente = setCliente;
	}
	
	public String getFolder() {
		return folder;
	}

	public void setFolder(String setFolder) {
		Edital.folder = setFolder;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String setLabel) {
		Edital.label = setLabel;
	}
	
	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String setObservacoes) {
		Edital.observacoes = setObservacoes;
	}
	
	public int getNumeroOrdem() {
		return numeroOrdem;
	}

	public int setNumeroOrdem(int numeroOrdem) {
		return Edital.numeroOrdem = numeroOrdem;
	}
	
}
