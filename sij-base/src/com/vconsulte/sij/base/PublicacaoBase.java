package com.vconsulte.sij.base;

/*
 * 
 * NÃO ESTAR EM USO
 * 
 */

import java.util.Collection;
import java.util.Date;
//import java.util.GregorianCalendar;

//import org.apache.chemistry.opencmis.client.api.Session;

/**
 * This class holds common properties and methods for the example classes.
 */

public class PublicacaoBase extends Base{
    private static Date edicao;
    private static String tribunal;
    private static String secao;
    private static String grupo;
    private static String assunto;
    private static String strEdicao;
	private static String sequencial;
	private static String seqEdicao;
	private static String strTribunal;

    private static String tipoDocumento;
    static Collection<String> strTexto;
	
	public Date getEdicao() {
		return PublicacaoBase.edicao;
	}

	public void setEdicao(Date dataEdicao) {
		PublicacaoBase.edicao = dataEdicao;
	}
	
	public String getTribunal() {
		return tribunal;
	}

	public void setTribunal(String setTribunal) {
		PublicacaoBase.tribunal = setTribunal;
	}
	
	public String getStrEdicao() {
		return strEdicao;
	}

	public void setStrEdicao(String setStrEdicao) {
		PublicacaoBase.strEdicao = setStrEdicao;
	}
	
	public static String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String setTipoDocumento) {
		PublicacaoBase.tipoDocumento = setTipoDocumento;
	}

	public Collection<String> getStrTexto() {							// corpo da pubicacao
		return strTexto;
	}
	
	public void setStrTexto(Collection<String> strTexto) {				// corpo da pubicacao
		PublicacaoBase.strTexto = strTexto;
	}
	
	// ------------------------------------
	
	public String getSeqEdicao() { 
		return seqEdicao; 
	}
	
	public void setSeqEdicao(String seqEdicao) {
		PublicacaoBase.seqEdicao = seqEdicao;
	}
	
	// ------------------------------------
	
	public String getSequencial() { 
		return sequencial; 
	}
	
	public void setSequencial(String sequencial) {
		PublicacaoBase.sequencial = sequencial;
	}
	
	// ------------------------------------
	
	public String getStrTribunal() { 
		return strTribunal; 
	}
	
	public void setStrTribunal(String strTribunal) {
		PublicacaoBase.strTribunal = strTribunal;
	}
	
}
