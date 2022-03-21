package com.vconsulte.sij.base;

//****************************************************************************************************
//	Base: Rotinas básicas para o SIJ 	
//
//
//	versao 3 		- 04 de Março de 2020
//					Versão compativel com o Splitter_3.0
//
// 	V&C Consultoria Ltda.
//	Autor: Arlindo Viana.
//***************************************************************************************************

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
//import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
//import java.util.GregorianCalendar;
import java.text.ParseException;
//import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectId;
//import org.apache.chemistry.opencmis.client.api.ObjectId;
//import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
//import org.apache.chemistry.opencmis.client.util.FileUtils;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;

import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.apache.chemistry.opencmis.commons.data.BulkUpdateObjectIdAndChangeToken;
//import org.apache.chemistry.opencmis.commons.exceptions.CmisUnauthorizedException;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.alfresco.service.cmr.dictionary.InvalidAspectException;
import org.alfresco.service.cmr.repository.InvalidNodeRefException;
import org.alfresco.service.cmr.repository.NodeRef;

//import com.vconsulte.sij.base.*;

import org.alfresco.service.namespace.QName;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.dictionary.InvalidAspectException;

public class InterfaceServidor extends Base {
	static int k = 0;
	static int limiteClientes = 0;
	static Edital edital = new Edital();
	static Comuns comuns = new Comuns();
	
	public static Session serverConnect() {
		
		try {
			Session session;
			// default factory implementation
			SessionFactory factory = SessionFactoryImpl.newInstance();
			Map<String, String> parameter = new HashMap<String, String>();

			// Credenciais
			parameter.put(SessionParameter.USER, getUser());
			parameter.put(SessionParameter.PASSWORD, getPassword());

			// parâmetros da conexao
			parameter.put(SessionParameter.ATOMPUB_URL, getUrl() + "/alfresco/api/-default-/public/cmis/versions/1.1/atom");
			parameter.put(SessionParameter.CACHE_SIZE_OBJECTS,"10000");
			parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
			parameter.put(SessionParameter.REPOSITORY_ID, "-default-");

			// Inicializar sessão
			session = factory.createSession(parameter);
			return session;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void fechaConexao(Session sessao) {
		sessao.clear(); sessao=null;
	}
	
	public static Folder listaPastaEdicoes(Session session, String baseFolder, String folderName) {
		Folder documentLibrary = (Folder) session.getObjectByPath(baseFolder);
		Folder pastaEdicoes = null;
		for (CmisObject child : documentLibrary.getChildren()) {
			if (folderName.equals(child.getName())) {
				pastaEdicoes = (Folder) child;
			}
		}
		return pastaEdicoes;
	}
	
	public static Folder verificaPastaEdicao(Session session, String baseFolder, String folderName, String descricao, String tribunal, Date edicao )
			throws UnsupportedEncodingException {
		
		// verifica se a pasta destino ja existe, a cria se não existir
		Folder documentLibrary = (Folder) session.getObjectByPath(baseFolder);
		Folder edtFolder = null;
		for (CmisObject child : documentLibrary.getChildren()) {
			if (folderName.equals(child.getName())) {
				edtFolder = (Folder) child;
			}
		}

		// create the edtFolder if needed
		if (edtFolder == null) {
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(PropertyIds.NAME, folderName);
			properties.put(PropertyIds.DESCRIPTION, descricao);
			properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
 			properties.put(PropertyIds.SECONDARY_OBJECT_TYPE_IDS, Arrays.asList("P:sij:edicoes"));			
			properties.put("sij:edcTribunal", tribunal);																									
			properties.put("sij:edcEdicao", edicao);
			properties.put("sij:edcIndexados", "novo");
			edtFolder = documentLibrary.createFolder(properties);
		}		
		return edtFolder;
	}

	public static Folder verificaPastaPublicacao(Session session, 
			String pastaOrigem, String pastaNome, String descricao, String tribunal, Date edicao, String cliente, String strEdicao )
			throws UnsupportedEncodingException {
		
		// verifica se a pasta destino ja existe, a cria se não existir
		Folder documentLibrary = (Folder) session.getObjectByPath(pastaOrigem);
		Folder pastaPublicacoes = null;
		for (CmisObject child : documentLibrary.getChildren()) {
			if (pastaNome.equals(child.getName())) {
				pastaPublicacoes = (Folder) child;
			}
		}

		// Cria a pasta publicações do cliente se necessário
		if (pastaPublicacoes == null) {
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(PropertyIds.NAME, pastaNome);
			properties.put(PropertyIds.DESCRIPTION, descricao + " - Edição: " + strEdicao);
			properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
 			properties.put(PropertyIds.SECONDARY_OBJECT_TYPE_IDS, Arrays.asList("P:sij:publicacoes"));			
			properties.put("sij:pstTribunal", tribunal);																									
			properties.put("sij:pstEdicao", edicao);
			properties.put("sij:pstCliente", cliente);
			pastaPublicacoes = documentLibrary.createFolder(properties);
		}		
		return pastaPublicacoes;
	}
	
	public static Folder verificaPastaRelatorios(Session session, String pastaOrigem, String pastaNome, String descricao) throws UnsupportedEncodingException {
		
		// verifica se a pasta destino ja existe, a cria se não existir
		Folder documentLibrary = (Folder) session.getObjectByPath(pastaOrigem);
		Folder pastaRelatorios = null;
		for (CmisObject child : documentLibrary.getChildren()) {
			if (pastaNome.equals(child.getName())) {
				pastaRelatorios = (Folder) child;
			}
		}

		// Cria a pasta publicações do cliente se necessário
		if (pastaRelatorios == null) {
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(PropertyIds.NAME, pastaNome);
			properties.put(PropertyIds.DESCRIPTION, descricao);
			properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");			

			pastaRelatorios = documentLibrary.createFolder(properties);
		}		
		return pastaRelatorios;
	}
	
	public static void atualizaPastaEdicao(Session session, String pastaId, String status, String msgClip ) throws ParseException {
		
		// Atualiza flag indicativa que a pasta de edição teve a indexação ativada 

		CmisObject obj = session.getObject(session.createObjectId(pastaId));
		Map<String, Object> parametro = new HashMap<String, Object>();
		parametro.put("sij:edcStatus", status);
		parametro.put("sij:edcObservacao", msgClip);
		obj.updateProperties(parametro, true);
	}
	
	public static void atualizaPublicacaoClipada(Session session, String idPublicacao, String descricao, String cliente, String tokens) throws ParseException {
		
		// Atualiza flag indicativa que a pasta de edição teve a indexação ativada 

		CmisObject publicacao = session.getObject(session.createObjectId(idPublicacao));
		Map<String, Object> parametro = new HashMap<String, Object>();
		parametro.put("cm:description", descricao);
		parametro.put("sij:pubCliente", cliente);
		publicacao.updateProperties(parametro, true);
	}

/*		EM TESTE
	public static void adcionaAspectPublicacao(Session session, String idPublicacao, String cliente, String tokens) {

		NodeRef nodeRef;
		NodeService nodeService;
		nodeRef.isNodeRef(idPublicacao);
		
		try {		
			QName SIJ_ASPECT_recorte = QName.createQName("sij:sijmodel", "sij:recorte");
			QName PROP_SIJ_recCliente = QName.createQName("sij:sijmodel", "sij:recCliente");
			QName PROP_SIJ_recTokens = QName.createQName("sij:sijmodel", "sij:recTokens");
			Map<QName,Serializable> aspectValues = new HashMap<QName,Serializable>();
			aspectValues.put(PROP_SIJ_recCliente, cliente);
			aspectValues.put(PROP_SIJ_recTokens, tokens);
			nodeService.addAspect(nodeRef, SIJ_ASPECT_recorte, aspectValues);	
		} catch (InvalidNodeRefException  ex) {
			System.out.println("NodeRef inválido: " + ex.toString());
		}
	}
*/
	
	public static boolean incluiPublicacao(Session session, Folder edtFolder) throws UnsupportedEncodingException {

		Collection<String> texto = new ArrayList<String>();
		// prepare properties
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyIds.NAME, getFileName());

		properties.put(PropertyIds.OBJECT_TYPE_ID, "D:sij:publicacao");
		//properties.put(PropertyIds.SECONDARY_OBJECT_TYPE_IDS, Arrays.asList("cm:versionable,cm:generalclassifiable"));
		
		properties.put(PropertyIds.DESCRIPTION, edital.getDescricao());
		properties.put(PropertyIds.VERSION_LABEL, edital.getLabel());

		//properties.put("sij:docTipo","Publicação");
		//properties.put("sij:docSituacao","Novo");

		properties.put("sij:pubEdicao",edital.getEdicao());
		properties.put("sij:pubStrEdicao",Edital.getStrEdicao());
		properties.put("sij:pubTribunal",edital.getTribunal());				
		properties.put("sij:pubProcesso",edital.getProcessoLinha());
		properties.put("sij:pubVara",edital.getVara());												
		properties.put("sij:pubGrupo",edital.getGrupo());					
		properties.put("sij:pubAssunto",edital.getAssunto());				
		properties.put("sij:pubAdvogados",edital.getAtores());				
		properties.put("sij:pubIntimados",edital.getIntimados());			
		properties.put("sij:pubFolder",edital.getFolder());

		// preparação do conteúdo do edital
		String content = "";
		String mimetype = "text/plain; charset=UTF-8";
		texto = edital.getTexto();
		for(String linhas : texto){
			content = content + linhas + "\n";
		}
		byte[] contentBytes = content.getBytes("UTF-8");
		ByteArrayInputStream stream = new ByteArrayInputStream(contentBytes);
		ContentStream contentStream = session.getObjectFactory().createContentStream(getFileName(), contentBytes.length, mimetype, stream);

		// criar documento no servidor alfresco
		Document documento = edtFolder.createDocument(properties, contentStream, VersioningState.MAJOR);
		
		if(documento == null) {
			return false;
		}
		return true;
	}
	
	public static boolean enviaRelatorio(Session session, Folder pastaRelatorio, String nomeArquivo, List <String> texto) throws UnsupportedEncodingException {

		//Collection<String> texto = new ArrayList<String>();
		// prepare properties
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyIds.NAME, getFileName());

		properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
		properties.put(PropertyIds.NAME, nomeArquivo); // folder name

		// preparação do conteúdo do edital
		String content = "";
		String mimetype = "text/plain; charset=UTF-8";
		for(String linhas : texto){
			content = content + linhas + "\n";
		}
		byte[] contentBytes = content.getBytes("UTF-8");
		ByteArrayInputStream stream = new ByteArrayInputStream(contentBytes);
		ContentStream contentStream = session.getObjectFactory().createContentStream(getFileName(), contentBytes.length, mimetype, stream);

		// criar documento no servidor alfresco
		Document documento = pastaRelatorio.createDocument(properties, contentStream, VersioningState.MAJOR);
		
		if(documento == null) {
			return false;
		}
		return true;
	}
	
	public String getIdPastaEdicoes(Session session, String path) {
		String folderId = session.getObjectByPath(path).getId();
		return folderId;
	}
	
	public int verificaQtdPublicacoes(Session session, String idPasta) {
		String dummy = "";
		int qtdPublicacoes = 0;
		String queryString = "SELECT P.sij:pubNumeroOrdem FROM cmis:document AS D JOIN sij:publicacao AS P ON D.cmis:objectId = P.cmis:objectId WHERE in_folder(D,'" + idPasta + "') ORDER BY P.sij:pubNumeroOrdem DESC";

		ItemIterable<QueryResult> results = session.query(queryString, false);
		for (QueryResult hit : results) {
			for (PropertyData<?> property : hit.getProperties()) {
				Object value = property.getFirstValue();
				dummy = value.toString();
				qtdPublicacoes = Integer.parseInt(dummy);
				if(qtdPublicacoes > 0) {
					return qtdPublicacoes;
				} else {
					break;
				}
			}
		}
		return qtdPublicacoes;
	}

	public List <String> localizaPublicacoes(Session session, String idPasta, String tribunal, String token) {
		List <String> idDocs = new ArrayList<String>();
		String queryString;

		queryString = "SELECT D.cmis:objectId FROM cmis:document AS D JOIN sij:publicacao AS P ON D.cmis:objectId = P.cmis:objectId WHERE P.sij:pubTribunal = '" + tribunal + "' AND in_folder(D,'" + idPasta + "') AND CONTAINS(D,'\\'" + token + "\\'')";
		//queryString = "SELECT D.cmis:objectId FROM cmis:document AS D JOIN sij:publicacao AS P ON D.cmis:objectId = P.cmis:objectId WHERE P.sij:pubTribunal = '" + tribunal + "' AND in_folder(D,'" + idPasta + "') AND CONTAINS(D,'" + token + "')";
		//queryString = "SELECT D.cmis:objectId FROM cmis:document AS D JOIN sij:publicacao AS P ON D.cmis:objectId = P.cmis:objectId WHERE P.sij:pubTribunal = '" + tribunal + "' AND in_folder(D,'" + idPasta + "') AND CONTAINS(D,'TEXT:" + token + "')";
		//erro queryString = "SELECT D.cmis:objectId FROM cmis:document AS D JOIN sij:publicacao AS P ON D.cmis:objectId = P.cmis:objectId WHERE contains(D,'\\'" + token + "\\'') AND P.sij:pubTribunal = '" + tribunal + "'";
		//erro queryString = "SELECT d.cmis:objectId FROM sij:documento AS d JOIN sij:publicacao AS w ON d.cmis:objectId = w.cmis:objectId WHERE contains(d,'\\'" + token + "\\'') AND in_folder(d,'" + idPasta + "') AND w.sij:pubTribunal = '" + tribunal + "'";
		//erro queryString = "SELECT d.cmis:objectId FROM sij:documento AS d JOIN sij:publicacao AS w ON d.cmis:objectId = w.cmis:objectId WHERE contains(d,'\\'" + token + "\\'') AND in_folder(d,'" + idPasta + "') AND w.sij:pubTribunal = '" + tribunal + "'";
		
		ItemIterable<QueryResult> results = session.query(queryString, false);
		for (QueryResult hit : results) {
			for (PropertyData<?> property : hit.getProperties()) {
				String queryName = property.getQueryName();
				Object value = property.getFirstValue();
				idDocs.add(value.toString());
				k++;
			}
		}
		return idDocs;
	}
	
	public List <String> localizaPublicacoesPorPasta(Session session, String idPasta) {
		List <String> idDocs = new ArrayList<String>();
		String queryString;
		
		//queryString = "SELECT D.cmis:objectId FROM cmis:document AS D JOIN sij:publicacao AS P ON D.cmis:objectId = P.cmis:objectId WHERE in_folder(D,'" + idPasta + "')";
		queryString = "SELECT cmis:objectId FROM cmis:document WHERE in_folder('" + idPasta + "')";
		ItemIterable<QueryResult> results = session.query(queryString, false);
		for (QueryResult hit : results) {
			for (PropertyData<?> property : hit.getProperties()) {
				String queryName = property.getQueryName();
				Object value = property.getFirstValue();
				idDocs.add(value.toString());
			}
		}
		return idDocs;
	}

	public List <String> listarEdicoesPorTribunal(Session session, String tribunal) {
		// Lista pastas com indexação das publicações ainda não atualizadas
		List <String> edicoes = new ArrayList<String>();
		String queryString;
		String dummy;
		String trib;
		String indexados;
				
		if(!tribunal.equals("todos")){
			queryString = "SELECT E.cmis:objectId FROM sij:edicoes AS E WHERE E.sij:edcTribunal = '" + tribunal + "' AND E.sij:edcIndexados = 'novo'";
		} else {
			queryString = "SELECT cmis:objectId FROM sij:edicoes";
		}
	
		ItemIterable<QueryResult> results = session.query(queryString, false);		
		for (QueryResult hit : results) {
			for (PropertyData<?> property : hit.getProperties()) {
				Object value = property.getFirstValue();
				edicoes.add(value.toString());
			}
		}
		return edicoes;
	}
	
	public List <String> listarEdicoesPorEdicao(Session session, String strEdicao) {
		// Lista pastas de edições menor ou igual a uma data fornecida
		List <String> edicoes = new ArrayList<String>();
	
		String queryString = "SELECT E.cmis:objectId FROM sij:edicoes AS E WHERE E.sij:edcStrEdicao = '" + strEdicao + "'";
		//String queryString = "SELECT E.cmis:objectId FROM sij:edicoes AS E WHERE E.sij:edcEdicao <= '" + strEdicao + "' ORDER BY E.sij:edcEdicao";
		ItemIterable<QueryResult> results = session.query(queryString, false);		
		for (QueryResult hit : results) {
			for (PropertyData<?> property : hit.getProperties()) {
				Object value = property.getFirstValue();
				edicoes.add(value.toString());
			}
		}
		return edicoes;
	}
	
	public List <String> listaEdicoesDoLixo(Session session, String idPastaLixo) {
		// Lista edições que estão na pasta Lixo
		List <String> edicoes = new ArrayList<String>();
		String queryString ="SELECT cmis:objectId FROM cmis:folder WHERE in_folder('" + idPastaLixo + "')";
		ItemIterable<QueryResult> results = session.query(queryString, false);		
		for (QueryResult hit : results) {
			for (PropertyData<?> property : hit.getProperties()) {
				Object value = property.getFirstValue();
				edicoes.add(value.toString());
			}
		}
		return edicoes;
	}
	
	public String [][] listarClientes(Session session, int limiteClientes) {
		int ix = 0;
    	String [][] tabClientes = new String[limiteClientes][2];
		String dummy = null;	
		String queryString = "SELECT C.sij:cliCodigo, C.sij:cliPastaPublicacoes FROM cmis:document AS D JOIN sij:clienteSIJ AS C on D.cmis:objectId = C.cmis:objectId";   
		ItemIterable<QueryResult> results = session.query(queryString, false);
    	for (QueryResult qResult : results) {
    		dummy = qResult.getPropertyValueByQueryName("C.sij:cliCodigo").toString();
    		tabClientes [ix][0] = dummy;
    		dummy = qResult.getPropertyValueByQueryName("C.sij:cliPastaPublicacoes").toString();
    		tabClientes [ix][1] = dummy;
    		ix++;
    	}
		return tabClientes;
	}

//===========================================================================================
	
	public List <String> obtemTabelasTokens(Session session, String cliente, String tribunal) {
		
		// Lista pastas com indexação das publicações ainda não atualizads
		List <String> tabelas = new ArrayList<String>();
		String queryString = "SELECT cmis:objectId FROM sij:tabTokens WHERE sij:tknCliente='" + cliente + "' AND sij:tknTribunal='" + tribunal + "'";
		//String queryString = "SELECT D.cmis:objectId FROM sij:conteudo AS D JOIN sij:tabTokens AS T ON D.cmis:objectId = T.cmis:objectId  WHERE T.sij:tknCliente='0001'";
		ItemIterable<QueryResult> results = session.query(queryString, false);
		for (QueryResult hit : results) {
			for (PropertyData<?> property : hit.getProperties()) {
				Object value = property.getFirstValue();
				tabelas.add(value.toString());
			}
		}
		return tabelas;
	}
	
	public String obtemIdEdicao(Session sessao, String tribunal) throws Exception {
		int ix = 0;
    	String idEdicao = "*";
		String dummy = null;
		String queryString = "";
		try {
		// Lista edições para processamento
		List <String> edicoes = new ArrayList<String>();

		if(tribunal.equals("all")) {
			queryString = "SELECT cmis:objectId FROM sij:diarioOficial WHERE sij:dofProcessado = false";
		} else {
			queryString = "SELECT cmis:objectId FROM sij:diarioOficial WHERE sij:dofProcessado = false AND sij:dofTribunal = '" + tribunal + "'";
		}

		ItemIterable<QueryResult> results = sessao.query(queryString, false);
		for (QueryResult qResult : results) {
    		dummy = qResult.getPropertyValueByQueryName("cmis:objectId").toString();
    		idEdicao = dummy;
    		ix++;
    	}
		return idEdicao;
		} catch (Exception erro) {
			return null; 
		}
	}
	
	public String [][] obtemNovasEdicoes(Session sessao) throws Exception {
		int ix = 0;
    	String [][] tabEdicoes = new String[30][2];
		String dummy = null;
		try {
		// Lista edições para processamento
		List <String> edicoes = new ArrayList<String>();
		String queryString = "SELECT cmis:objectId, sij:dofTribunal FROM sij:diarioOficial WHERE sij:dofProcessado = false";
		ItemIterable<QueryResult> results = sessao.query(queryString, false);
		for (QueryResult qResult : results) {
    		dummy = qResult.getPropertyValueByQueryName("cmis:objectId").toString();
    		tabEdicoes [ix][0] = dummy;
    		dummy = qResult.getPropertyValueByQueryName("sij:dofTribunal").toString();
    		tabEdicoes [ix][1] = Comuns.completaEsquerda(dummy, '0', 2);
    		ix++;
    	}
		return tabEdicoes;
		} catch (Exception erro) {
			System.out.println("Erro na localização das edicoes no Servidor - " + erro.toString());
		}
		return null; 
	}

	public List <String> obtemEdicoes(Session session, String cliente) {
		
		// Lista pastas com indexação das publicações ainda não atualizads
		List <String> edicoes = new ArrayList<String>();
		String queryString = "SELECT cmis:objectId FROM sij:diarioOficial WHERE sij:tknCliente='" + cliente + "'";
		//String queryString = "SELECT D.cmis:objectId FROM sij:conteudo AS D JOIN sij:tabTokens AS T ON D.cmis:objectId = T.cmis:objectId  WHERE T.sij:tknCliente='0001'";
		ItemIterable<QueryResult> results = session.query(queryString, false);
		for (QueryResult hit : results) {
			for (PropertyData<?> property : hit.getProperties()) {
				Object value = property.getFirstValue();
				edicoes.add(value.toString());
			}
		}
		return edicoes;
	}
	
	public List <String> obtemTabelasTokensM(Session session, String cliente) {
		
		// Lista pastas com indexação das publicações ainda não atualizads
		List <String> tabelas = new ArrayList<String>();
		String queryString = "SELECT cmis:objectId FROM sij:tabTokens WHERE sij:tknCliente='" + cliente + "'";
		//String queryString = "SELECT D.cmis:objectId FROM sij:conteudo AS D JOIN sij:tabTokens AS T ON D.cmis:objectId = T.cmis:objectId  WHERE T.sij:tknCliente='0001'";
		ItemIterable<QueryResult> results = session.query(queryString, false);
		for (QueryResult hit : results) {
			for (PropertyData<?> property : hit.getProperties()) {
				Object value = property.getFirstValue();
				tabelas.add(value.toString());
			}
		}
		return tabelas;
	}
	
	/*
	public List <String> listarEdicoesNaoIndexadas(Session session, String path) {
		
		// Lista pastas com indexação das publicações ainda não atualizads		
		List <String> edicoes = new ArrayList<String>();
		ItemIterable<QueryResult> results = session.query("SELECT cmis:objectId FROM sij:edicoes WHERE sij:edcStatus = 'processado'", false);		
		for (QueryResult hit : results) {
			for (PropertyData<?> property : hit.getProperties()) {
				Object value = property.getFirstValue();
				edicoes.add(value.toString());
			}
		}
		return edicoes;
	}
	
	public List <String> listarPublicacoesNaoIndexadas(Session session, String queryString) {
		
		// Listas as publicações de uma pasta com indexação ainda não ativada
		
		List <String> idDocs = new ArrayList<String>();
		ItemIterable<QueryResult> results = session.query(queryString, false);

		for (QueryResult hit : results) {
			for (PropertyData<?> property : hit.getProperties()) {
				String queryName = property.getQueryName();
				Object value = property.getFirstValue();
				idDocs.add((String) value);
			}
		}
		return idDocs;
	}
	 */
	public static void ativaIndexPublicacaoEmBloco(Session session, List<String> publicacoes) throws ParseException {
		
		// Atualiza em bloco as novas publicações		*** EM DESENVOLVIMENTO ***
		
		List<CmisObject> objectList = new ArrayList<CmisObject>();
		String id = "";

		for (int x=0; x<=publicacoes.size()-1; x++) {
			objectList.add(session.getObject(publicacoes.get(x)));
		}

		Map<String, Object> parametro = new HashMap<String, Object>();
		parametro.put("cm:isIndexed", true);
		parametro.put("cm:isContentIndexed", true);
		List<BulkUpdateObjectIdAndChangeToken> updatedIds = session.bulkUpdateProperties(objectList, parametro, null, null);
	}
	
	public static void ativaIndexPublicacao(Session session, String docId) throws ParseException {
		
		// Atualiza unitariamente as novas publicações 

		CmisObject obj = session.getObject(session.createObjectId(docId));
		Map<String, Object> parametro = new HashMap<String, Object>();
		parametro.put("cm:isIndexed", true);
		parametro.put("cm:isContentIndexed", true);
		obj.updateProperties(parametro, true);
	}
	
	public Map<String, String> listarEdicoes(Session session, String path) {
		Map<String, String> edicoes = new HashMap<String, String>();		
		String folderId = session.getObjectByPath(path).getId();
		Folder folder = (Folder) session.getObject(folderId);
		Iterator<CmisObject> it = folder.getChildren().iterator();
		
		while(it.hasNext()) {
			CmisObject object = it.next();
			edicoes.put(object.getPropertyValue("cmis:objectId").toString(), 
					object.getPropertyValue("cmis:name").toString());
		}
		return edicoes;
	}
/*	
	public Map<String, String> listarEdicoesNaoProcessadas(Session session, String path) {
		Map<String, String> edicoes = new HashMap<String, String>();		
		String folderId = session.getObjectByPath(path).getId();
		Folder folder = (Folder) session.getObject(folderId);
		Iterator<CmisObject> it = folder.getChildren().iterator();
		
		while(it.hasNext()) {
			CmisObject object = it.next();
			edicoes.put(object.getPropertyValue("cmis:objectId").toString(), 
					object.getPropertyValue("sij:dofProcessado").toString());
		}
		return edicoes;
	}
*/
	public String getCargaId(Session session, String path) {	
		String folderId = session.getObjectByPath(path).getId();		
		return folderId;
	}
	
	public Map<String, String> encontrarEdicoes(Session session, String path) {

		Map<String, String> edicoes = new HashMap<String, String>();		
		String folderId = session.getObjectByPath(path).getId();
		Folder folder = (Folder) session.getObject(folderId);		
		Iterator<CmisObject> it = folder.getChildren().iterator();
		
		while(it.hasNext()) {
			  CmisObject object = it.next();
			  edicoes.put(object.getPropertyValue("cmis:objectId").toString(), 
					  object.getPropertyValue("cmis:description").toString());
		}
		return edicoes;
	}
	
	public List <String> carregaTokens(Session session, String tribunal) throws NullPointerException, IOException {
		List <String> tokens = new ArrayList<String>();
		String idDoc = "";
		String linha = "";
		String fileTokens = tribunal+"tokens.txt";

		ItemIterable<QueryResult> results = session.query("SELECT cmis:objectId FROM cmis:document where cmis:name='" + fileTokens + "'", false);
		
		for (QueryResult hit : results) {
			for (PropertyData<?> property : hit.getProperties()) {
				Object value = property.getFirstValue();
				idDoc = value.toString();
			}
		}
		if(idDoc != "") {
			Document obj = (Document) session.getObject(session.createObjectId(idDoc));	
			ContentStream contentStream = obj.getContentStream();
			InputStream stream = contentStream.getStream();		
			BufferedReader lerArq = new BufferedReader(new InputStreamReader((stream), "UTF-8"));

			while(linha != null){			
				linha = lerArq.readLine();			
				if(linha != null) {
					if(!linha.substring(0, 3).contains("++")) {	
						tokens.add(linha);							
					}				
				}
			}		
		}
		return tokens;
	}
	
	public List <String> carregaTokensBatch(Session session, String idTokens) throws NullPointerException, IOException {

		List <String> tokens = new ArrayList<String>();
		String token = "";
		String linha = "";

		Document obj = (Document) session.getObject(session.createObjectId(idTokens));	
		ContentStream contentStream = obj.getContentStream();
		InputStream stream = contentStream.getStream();		
		BufferedReader lerArq = new BufferedReader(new InputStreamReader((stream), "UTF-8"));

		while(linha != null){			
			linha = lerArq.readLine();		
			if(linha != null) {
				if(!linha.substring(0, 3).contains("++")) {
					token = linha.replaceAll("[/&%#]", " ");
					token = token.replaceAll("[:(){}]", " ");
					System.out.println(token);
					tokens.add(token);							
				}				
			}
		}		
		return tokens;
	}
	
	public List <String> carregaAssuntosRemoto(Session session) throws NullPointerException, IOException {

		List <String> tabAssuntos = new ArrayList<String>();
		String idDoc = "";
		String linha = "";
		String arquivo = "subjects.txt";

		ItemIterable<QueryResult> results = session.query("SELECT cmis:objectId FROM cmis:document where cmis:name='" + arquivo + "'", false);
		
		for (QueryResult hit : results) {
			for (PropertyData<?> property : hit.getProperties()) {
				Object value = property.getFirstValue();
				idDoc = value.toString();
			}
		}
		if(idDoc != "") {
			Document obj = (Document) session.getObject(session.createObjectId(idDoc));	
			ContentStream contentStream = obj.getContentStream();
			InputStream stream = contentStream.getStream();		
			BufferedReader lerArq = new BufferedReader(new InputStreamReader((stream), "UTF-8"));

			while(linha != null){			
				linha = lerArq.readLine();			
				if(linha != null) {
					if(!linha.substring(0, 3).contains("++")) {	
						tabAssuntos.add(linha);							
					}				
				}
			}		
		}
		return tabAssuntos;
	}
	
	public String obtemTribunal(Session session, String pastaId) {
		
		String tribunal = null;	
		String queryString = "SELECT sij:edcTribunal FROM sij:edicoes WHERE cmis:objectId = '" + pastaId + "'";   
		ItemIterable<QueryResult> results = session.query(queryString, false);
    	for (QueryResult qResult : results) {
    		tribunal = qResult.getPropertyValueByQueryName("sij:edcTribunal").toString();
    	}
		return tribunal;
	}

	public String obtemTokenTribunal(Session session, String idToken) {
		
		String tribunal = null;	
		String queryString = "SELECT sij:tknTribunal FROM sij:tabTokens WHERE cmis:objectId = '" + idToken + "'";   
		ItemIterable<QueryResult> results = session.query(queryString, false);
    	for (QueryResult qResult : results) {
    		tribunal = qResult.getPropertyValueByQueryName("sij:tknTribunal").toString();
    	}
		return tribunal;
	}
	
	public String obtemNomePastaDestino(Session session, String idEdital) {
		
		String nomePasta = null;	
		String queryString = "SELECT P.sij:pubFolder FROM cmis:document AS D JOIN sij:publicacao AS P ON D.cmis:objectId = P.cmis:objectId WHERE D.cmis:objectId = '" + idEdital + "'";   
		ItemIterable<QueryResult> results = session.query(queryString, false);
    	for (QueryResult qResult : results) {
    		nomePasta = qResult.getPropertyValueByQueryName("P.sij:pubFolder").toString();
    	}
		return nomePasta;
	}
	
	public String obtemNomeEdicaoPDF(Session session, String idArquivo) {
		
		String nomeEdicaoPDF = null;	
		String queryString = "SELECT cmis:name FROM cmis:document  WHERE cmis:objectId = '" + idArquivo + "'";   
		ItemIterable<QueryResult> results = session.query(queryString, false);
    	for (QueryResult qResult : results) {
    		nomeEdicaoPDF = qResult.getPropertyValueByQueryName("cmis:name").toString();
    	}
		return nomeEdicaoPDF;
	}

	public static void deleteDocuments(Session session) throws UnsupportedEncodingException {

		// locate the document library
		String path = "/Sites/advocacia/documentLibrary";
		Folder documentLibrary = (Folder) session.getObjectByPath(path);

		// locate the marketing folder
		Folder marketingFolder = null;
		for (CmisObject child : documentLibrary.getChildren()) {
			if ("carregamento".equals(child.getName())) {
				marketingFolder = (Folder) child;
			}
		}

		// create the marketing folder if needed
		if (marketingFolder != null) {
			for (CmisObject child : marketingFolder.getChildren()) {
				if(child != null) {
					session.delete(child);
					System.out.println("excluido: " +child);
				}				
			}
		}
	}
	
	public static String getFolderId(Session session, String nomePasta) {
		String id = "";
		List <String> resultados = new ArrayList<String>();		
		String queryString ="SELECT cmis:objectId,cmis:path,cmis:description,cmis:name FROM cmis:folder WHERE cmis:name = '" + nomePasta + "'";
		ItemIterable<QueryResult> results = session.query(queryString, false);
    	for (QueryResult qResult : results) {
    		resultados.add(qResult.getPropertyValueByQueryName("cmis:objectId").toString());
    		id = qResult.getPropertyValueByQueryName("cmis:objectId").toString();				// qebra galho
    		//resultados.add(qResult.getPropertyValueByQueryName("cmis:name").toString());
    		//resultados.add(qResult.getPropertyValueByQueryName("cmis:path").toString());
    		//resultados.add(qResult.getPropertyValueByQueryName("cmis:description").toString());
    	}
    	//return resultados;
    	return id;
	}
	
	public static String getFileId(Session session, String idFile) {
		List <String> resultados = new ArrayList<String>();		
//		String queryString = "SELECT cmis:objectId,cmis:name FROM cmis:document WHERE cmis:objectId = '" + idFile + "'";    
//		ItemIterable<QueryResult> results = session.query(queryString, false);
		ItemIterable<QueryResult> results = session.query(idFile, false);
		for (QueryResult qResult : results) {
    		resultados.add(qResult.getPropertyValueByQueryName("cmis:objectId").toString());
    		idFile = resultados.get(0);
    	}
    	return idFile;
	}
/*	
	public static String getFileId(Session session, Folder idPastaDestino, String nomePublicacao) {
		String idFile = null;
		String queryString = "";
		List <String> resultados = new ArrayList<String>();		
	//	queryString = "SELECT cmis:objectId,cmis:name,cmis:path,cmis:description FROM cmis:folder where cmis:objectId = '" + idDocumento + "'";   
	//	queryString = "SELECT cmis:objectId,cmis:name,cmis:description FROM cmis:document WHERE cmis:objectId ='" + idDocumento + "'";    	
		queryString = "SELECT cmis:objectId FROM cmis:document WHERE in_folder('" + idPastaDestino + "') AND cmis:name='" + nomePublicacao + "' AND cmis:lastModifiedBy='sij'";
		ItemIterable<QueryResult> results = session.query(queryString, false);
    	for (QueryResult qResult : results) {
    		resultados.add(qResult.getPropertyValueByQueryName("cmis:objectId").toString());
    		idFile = resultados.get(0);
    	}
    	return idFile;
	}
*/	
	public static String getNomeDocumento(Session session, String idDocumento) {
		String nomeDoc = null;
		List <String> resultados = new ArrayList<String>();		
		String queryString = "SELECT cmis:name FROM cmis:document WHERE cmis:objectId ='" + idDocumento + "'";    	
		ItemIterable<QueryResult> results = session.query(queryString, false);
    	for (QueryResult qResult : results) {
    		resultados.add(qResult.getPropertyValueByQueryName("cmis:name").toString());
    		nomeDoc = resultados.get(0);
    	}
    	return nomeDoc;
	}
	
	public static String obtemTribunalEdicao(Session session, String idPasta) {
		String dummy = "";	
		String queryString = "SELECT P.sij:pubTribunal FROM sij:documento AS D JOIN sij:publicacao AS P ON D.cmis:objectId = P.cmis:objectId WHERE in_folder(D,'" + idPasta + "')" ;    
		ItemIterable<QueryResult> results = session.query(queryString, false);

    	for (QueryResult qResult : results) {
    		dummy = qResult.getPropertyValueByQueryName("p.sij:pubTribunal").toString();
    		break;
    	}
    	return dummy;
	}
	
	public static String obtemTribunalDaEdicao(Session session, String idEdicao) {
		String dummy = "";	
		String queryString = "SELECT sij:edcTribunal FROM sij:edicoes WHERE cmis:objectId = '" + idEdicao + "'" ;    
		ItemIterable<QueryResult> results = session.query(queryString, false);

    	for (QueryResult qResult : results) {
    		dummy = qResult.getPropertyValueByQueryName("sij:edcTribunal").toString();
    		break;
    	}
    	return dummy;
	}
	
	public static String obtemTribunalToken(Session session, String idTabTokens) {
		String dummy = "";	
		String queryString = "SELECT T.sij:tknTribunal FROM sij:tabTokens AS T WHERE T.cmis:objectId = '" + idTabTokens + "'" ;    
		ItemIterable<QueryResult> results = session.query(queryString, false);

    	for (QueryResult qResult : results) {
    		dummy = qResult.getPropertyValueByQueryName("T.sij:tknTribunal").toString();
    		break;
    	}
    	return dummy;
	}
	
	public static String obtemEdicao(Session session, String IdPublicacao) {
		String dummy = "";
		Date edicao;
		String queryString = "SELECT P.sij:pubStrEdicao FROM cmis:document AS D JOIN sij:publicacao AS P ON D.cmis:objectId = P.cmis:objectId WHERE D.cmis:objectId ='" + IdPublicacao + "'";
		ItemIterable<QueryResult> results = session.query(queryString, false);
    	for (QueryResult qResult : results) {
    		dummy = qResult.getPropertyValueByQueryName("P.sij:pubStrEdicao").toString();
    		break;
    	}
    	return dummy;
	}
	
	public static String obtemEdicaoPasta(Session session, String IdEdicao) {
		String edicao = null;
		String queryString = "SELECT E.sij:edcStrEdicao FROM sij:edicoes AS E WHERE E.cmis:objectId ='" + IdEdicao + "'";
		ItemIterable<QueryResult> results = session.query(queryString, false);
    	for (QueryResult qResult : results) {
    		edicao = qResult.getPropertyValueByQueryName("E.sij:edcStrEdicao");
    		break;
    	}  	
    	return edicao;
	}

	public static String getIdEdicoes(Session session, String folderName) {

		String id = "";	
		String dummy = "";
		String queryString = "SELECT cmis:objectId FROM cmis:folder where cmis:name = '" + folderName + "'";
		ItemIterable<QueryResult> results = session.query(queryString, false);

		for (QueryResult qResult : results) {
    		id = qResult.getPropertyValueByQueryName("cmis:objectId").toString();
    	}
    	return id;
	}
	
	public static List <String> getFolderInfo(Session session, String folderId) {

		List <String> resultados = new ArrayList<String>();	
		String queryString = "SELECT cmis:objectId,cmis:path,cmis:description,cmis:name FROM cmis:folder WHERE cmis:objectId = '" + folderId + "'";   	
		ItemIterable<QueryResult> results = session.query(queryString, false);
    	for (QueryResult qResult : results) {
    		resultados.add(qResult.getPropertyValueByQueryName("cmis:objectId").toString());
    		resultados.add(qResult.getPropertyValueByQueryName("cmis:path").toString());
    		if(qResult.getPropertyValueByQueryName("cmis:description").toString().isBlank()) {
    			resultados.add("descrição vazia");
    		} else {
    			resultados.add(qResult.getPropertyValueByQueryName("cmis:description").toString());
    		}
    		resultados.add(qResult.getPropertyValueByQueryName("cmis:name").toString());
    	}
    	return resultados;
	}
	
	public static List <String> listaEdicoesNaoProcessadas(Session session) {

		List <String> edicoes = new ArrayList<String>();	
		String queryString = "SELECT cmis:objectId FROM sij:diarioOficial WHERE sij:dofProcessado = false";   	
		ItemIterable<QueryResult> results = session.query(queryString, false);
    	for (QueryResult qResult : results) {
    		edicoes.add(qResult.getPropertyValueByQueryName("cmis:objectId").toString());
    	}
    	return edicoes;
	}
	
	public static List <String> listaEdicoesNaoClipadas(Session session) {

		List <String> edicoes = new ArrayList<String>();	
		String queryString = "SELECT cmis:objectId FROM sij:edicoes WHERE sij:edcIndexados = 'novo'";   	
		ItemIterable<QueryResult> results = session.query(queryString, false);
    	for (QueryResult qResult : results) {
    		edicoes.add(qResult.getPropertyValueByQueryName("cmis:objectId").toString());
    	}
    	return edicoes;
	}
	
	public boolean verificaEdicaoNaoClipada(Session session, String folderId, String cliente) {
		String queryString = "";
		String jaIndexado = "";
		// v1 - String queryString = "SELECT E.sij:edcIndexados FROM sij:edicoes AS E WHERE E.cmis:objectId = '" + folderId + "'  AND E.sij:edcIndexados = 'novo'";   	
		// v2 - String queryString = "SELECT E.sij:edcIndexados FROM sij:edicoes AS E WHERE E.cmis:objectId = '" + folderId + "' AND CONTAINS('sij:edcIndexados:novo')";   	

		queryString = "SELECT E.sij:edcIndexados FROM sij:edicoes AS E WHERE E.cmis:objectId = '" + folderId + "'";
		ItemIterable<QueryResult> results = session.query(queryString, false);
    	for (QueryResult qResult : results) {
    		jaIndexado = qResult.getPropertyValueByQueryName("E.sij:edcIndexados").toString();
    	}
    	if(!jaIndexado.contains(cliente)) {
    		return true;
    	}
    	return false;
	}
	
	public static List <String> obtemInformacosPublicacao(Session sessao, String idPublicacao) {

		List <String> propriedades = new ArrayList<String>();	
		String queryString = "SELECT D.cmis:objectId, D.cmis:description, D.cmis:name, "
				+ "P.sij:pubEdicao, P.sij:pubStrEdicao, P.sij:pubTribunal, "
				+ "P.sij:pubVara, P.sij:pubProcesso, P.sij:pubGrupo, "
				+ "P.sij:pubAssunto, P.sij:pubAdvogados, P.sij:pubIntimados, P.sij:pubFolder "
				+ " FROM cmis:document AS D JOIN sij:publicacao AS P ON D.cmis:objectId = P.cmis:objectId WHERE D.cmis:objectId = '" + idPublicacao + "'";   	
		ItemIterable<QueryResult> results = sessao.query(queryString, false);
    	for (QueryResult qResult : results) {
    		propriedades.add(qResult.getPropertyValueByQueryName("D.cmis:objectId").toString());
    		propriedades.add(qResult.getPropertyValueByQueryName("D.cmis:description").toString());
    		propriedades.add(qResult.getPropertyValueByQueryName("D.cmis:name").toString());
    		propriedades.add(qResult.getPropertyValueByQueryName("P.sij:pubEdicao").toString());
    		propriedades.add(qResult.getPropertyValueByQueryName("P.sij:pubStrEdicao").toString());
    		propriedades.add(qResult.getPropertyValueByQueryName("P.sij:pubTribunal").toString());
    		propriedades.add(qResult.getPropertyValueByQueryName("P.sij:pubVara").toString());
    		propriedades.add(qResult.getPropertyValueByQueryName("P.sij:pubProcesso").toString());
    		propriedades.add(qResult.getPropertyValueByQueryName("P.sij:pubGrupo").toString());
    		propriedades.add(qResult.getPropertyValueByQueryName("P.sij:pubAssunto").toString());
    		propriedades.add(qResult.getPropertyValueByQueryName("P.sij:pubAdvogados").toString());
    		propriedades.add(qResult.getPropertyValueByQueryName("P.sij:pubIntimados").toString());
    		propriedades.add(qResult.getPropertyValueByQueryName("P.sij:pubFolder").toString());
    	}
    	return propriedades;
	}

	public static void indexaPublicacao(Session session, String docId, String propConteudo) throws ParseException {

		String token = "";
		String descri = "";
		CmisObject obj = session.getObject(session.createObjectId(docId));
		token = obj.getPropertyValue("sij:edtToken");

		if (token== null) token = "";

		descri = obj.getDescription() + "\n" + "Token: " + propConteudo;
		Map<String, Object> parametro = new HashMap<String, Object>();
		parametro.put("cm:description",descri);
		parametro.put("sij:pubToken", token + "\n" + propConteudo);
		obj.updateProperties(parametro, true);
	}
	
	public static void atualizaEdicaoClipada(Session session, String cliente, String idPastaEdicao) throws ParseException {

		String edcIndexados = "";
		CmisObject obj = session.getObject(session.createObjectId(idPastaEdicao));
		edcIndexados = obj.getPropertyValue("sij:edcIndexados");
		if(edcIndexados.equals("novo")) {
			edcIndexados = cliente.trim();
		} else {
			edcIndexados = edcIndexados + "\n" + cliente.trim();
		}		
		Map<String, Object> parametro = new HashMap<String, Object>();
		parametro.put("sij:edcIndexados",edcIndexados);
		obj.updateProperties(parametro, true);
	}
	
	public static void atualizaEdicaoProcessada(Session session, String idEdicao) throws ParseException {

		String edcIndexados = "";
		CmisObject obj = session.getObject(session.createObjectId(idEdicao));	
		Map<String, Object> parametro = new HashMap<String, Object>();
		parametro.put("sij:dofProcessado",true);
		obj.updateProperties(parametro, true);	
	}
	
	public static void reverteEdicaoProcessada(Session session, String idEdicao) throws ParseException {

		CmisObject obj = session.getObject(session.createObjectId(idEdicao));	
		Map<String, Object> parametro = new HashMap<String, Object>();
		parametro.put("sij:edcIndexados","novo");
		obj.updateProperties(parametro, true);	
		k++;
	}
	
	public static void moveEditalIndexado(Session session, String docId, String sourcePath, Folder indexFolder) {

		Folder destinoFolder = indexFolder;	
		Folder origemFolder = (Folder) session.getObjectByPath(sourcePath);
		Document documento = (Document) session.getObject(docId);
		documento.move(origemFolder, destinoFolder);
	}
	
	public static boolean copiaPublicacao(Session session, String docId, String sourcePath, String destPath, String nomePublicacao, String cliente) {

		try {
			Folder pastaDestino = (Folder) session.getObjectByPath(destPath);
			Document documento = (Document) session.getObject(docId);
			documento.copy(pastaDestino);
			return true;
		} catch (CmisContentAlreadyExistsException ccaee) {
			return false;
		}
	}

	public static void excluiPublicacao(Session sessao, String docId) {
		Document documento = (Document) sessao.getObject(docId);
		documento.delete();
	}
	
	public static void excluiPasta(Session sessao, String folderId) {
		Folder pasta = (Folder) sessao.getObject(folderId);
		pasta.delete();
	}

}
