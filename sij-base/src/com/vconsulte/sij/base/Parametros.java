package com.vconsulte.sij.base;
/*
 * SplitDOb - Versão batch 
 */

//***************************************************************************************************
// SgjProcDO: Antiga rotina clippingDO atualizada para gravar os editais diretamente no Alfresco 	
// 
// Versão 1.7 	- 23 Março de 2017
//					Correção de falha na finalização geral da rotina
//					Correção na possibilidade de encerramento prematuro da rotina
//					Inclusão de lógica para descartar linhas desnecessárias
//					Quebra de pauta de julgamento
//					Processamento de acórdãos
//
// Versao 1.7.1 	- 29 Março de 2017
//					Vericar exceção ao tipo de quebra de editais (verificaExcecaoQuebra)
//					Limpeza do buffer de introdução quando mudar o assnto
//					Generalização do método validaPadrao e verificaAssunto (atender qualquer região)
//					Nova lógia para validação do "assunto", impedir que uma palavra igual a assunto engane a lógica
//
// Versão 1.8 		- 1º de Abril de 2017
//					Nova logica de controle de leitura de linhas e quebra de editais
//					Testado com os DO do RJ e SP
//
// Versão 1.8.1		- 29 de Abril de 2017
// 					Correção na carga de sessões
//
// Versão 1.8.2 	- 08 de Junho de 2017
//					Inclusão de mais dois padrões de nº de processo
//					Correção para gravação do último edital
//
// versao 1.8.3 	- 01 de Julho de 2017
//					Adcição do tribunal e  nº do edital ao nome do arquivo saida.
//
// versao 1.8.3.7 	- 10 de Julho de 2017
// 					conpilado com versão do Java = 7 para manter compatibilidade com sistemas do PJE.
//
// versao 1.9 		- 17 de Setembro de 2017
//					criação da lista "grupos".
//					nova nomeação do arquivos saida.
//					apagar arquivo intermediario.txt.
//					forçar quebra quando o assunto for "Edital de Notificacao"
//					correção na logica de quebra por nova secao - limpar grupoAnterior e assuntoAnterior
// 					melhorias na logica de separação dos grupos
//					melhorias na logina de quebra por grupo e assunto
//     				inclusão de mais um padrão de nº de processo: "\\w{8}\\s\\w\\W\\s\\w{6}\\W\\d{7}\\W\\d{2}\\W\\d{4}\\W\\d\\W\\d{2}\\W\\d{4}"
//
// versao 1.9.1 	- 06 de Outubro de 2017
//					Pequenas correções
//					Alteração nas leituras do intermedio com unicode UTF-8
//
// versao 1.9.2 	- 30 de Outubro de 2017
//					Gravação do arquivo de saida em UTF-8
//					Correção na quebra por nº de processo
//
// versao 1.9.3 	- 28 de Fevereiro de 2018
//					Inclusão de mais dois padrões de numeração de processos
//					
//	versao 1.9.4 	- 25 de Maio de 2018
//					Evitar carregar a string "PODER JUDICIARIO" na tabela de assuntos
//
//	versao 1.9.5 	- 31 de Maio de 2018
//					Correção de loop infinito na função mapeiaLinhas
//
//	versao 1.9.6 	- 08 de Junho de 2018
//					Correção na funcão "mapeiaLinhas"
//
// ---------------------------------------------------------------------------------------------------------------------------
//	Nova versão da antiga classe clippingDO agora chamada de SplitDO
//
//	versao 2.0 		- .. de Junho de 2018
//					Classe renomeada para SplitDO.java
//					Integração CMIS
//					Gravação dos editais diretamente no servidor Alfresco
//
//	versao 2.1 		- 17 de Dezembro de 2018
//					Inclusão do tratamento do DO do Tribunal Superior do Trabalho - TST
//
//	versao 2.1.1 	- 27 de Dezembro de 2018
//					Correção do método obtemNumeroProcesso
//
//	versao 2.1.2 	- 28 de Janeiro de 2019
//					Correção do método validaPadrao incluindo mais um formato de nº de processo
//
//	versao 2.1.3 	- 06 de Fevereiro de 2019
//					Incluindo mais um formato de nº de processo
//					Correção do metodp carregaAssuntos para excluir a string "PODER JUDICIÀRIO"
//
//	versao 2.2 		- 26 de Fevereiro de 2019
//					Implementação de novo modelo de dados
//					reformulação do metodo obtemNumeroProcesso
//					reformulação do metodo mapeiaLinha para não utilizar o metodo validaPadrao
//
//	versao 2.2.3 	- 27 de Fevereiro de 2019
//					Correção na quebra de editais por nº de processo
//
//	versao 2.2.4 	- 13 de Março de 2019
//					Correção no metodo "mapearLinhas" para tratar qdo o grupo for igual a "Portaria"
//
//	versao 2.2.5 	- 24 de Março de 2019
//					Correção de erros da versão 2.2.4
//
//	versao 2.3.0 	- 16 de Abril de 2019
//					- Nova logica para determinar quebra por assunto
//					- inclusão do método validaAssunto
//					- inclusão do método contarPalavrasAssunto
//
//	versao 2.3.20 	- 27 de Abril de 2020
//					- Novo algorítimo de quebra de editais
//					
//	
//	versao 2.3.21 	- 04 de Maio 2020
//					- Correções na justificação do texto
//					- Diário oficial convertido para a memoria
//					- Novo método carregaIndice
//					
//	versao 2.3.21a	- 10 de Maio 2020
//					- Ajustes no método formataaParagrafo
//					- Atualização na tabela termosChaves
//					- Atualização na tabela falsoFinal
//
//
//	versao 2.3.21b	- 06 de Junho 2020
//					- Parametrização 
//					- Classe de MetodosComuns.java
//					- Correção na quebra das publicações com assunto Pauta
//									
//	versao 2.3.21c	- 17 de Junho 2020			
//					- Correção na quebra das publicações com assunto Pauta
//					- Ajustes no método trataIntimados
//					
//	versao 2.3.21d	- 18 de Junho 2020		
//					- versão provivisoria sem o controle de nº de página
//
//	versao 2.3.21e	- 04 de Julho 2020		
//					- Parametrização para pastaSaida onde serão gravados as publicações PDF
//					- Melhorias no modelo de dados
//					- Correções na quebra por nº de processo
//					- Correções no método trataAtores
// 					- Melhorias no método trataIntimados
//
//	versao 2.3.21f	- 14 de Julho 2020	
//					- Correções na quebra por nº de processo
//
//	versao 2.3.23	- 01 de Outubro 2020	
//					- Correções na quebra quando a publicação for "Pauta"
//
//	versao 2.4		- 01 de Julho 2020	
//					- Unificação do modelo de dados
//					- Ajustes na saida de publicações em PDF
//					- Novo mentodo apresentaMensagem para apresentação de mensagens conforme o tipo de processamento
//
//	versao 2.4.06	- 06 de Novembro 2020
//					- Atualização na tabela de funções
//					- Atualização do método gravaLog();
//					- Adição do método gravaListaDePublicacoes()
//					- Adição de linha de rodapé das publicacoes com informações sobre a publicação
//
//	versao 2.4.07	- 11 de Novembro 2020
//					- Correção na falha que duplicava publicações retornando o sequencial no fim do loop de linhas.
//					- Melhorias no ClippingB e no BatchDO
//					- Classe Transmite com temporizador
//	
//	versao 2.4.08	- 25 de Novembro 2020
//					- Melhorias no registro de logs
//					- Melhorias no loop de processamento
//					- Tratamento de edições sem publicações (vazia)
//					- Batch.jar: Controle de edições para processamento
//					- Transmite.jar: Parametrização 
//
//	versao 2.4.09	- 04 de Dezembro 2020
//					- Processamento recebendo parametros com os TRTs a serem processados
//					- Controle do ciclo de processament através de rotina bash
//					- Retorno ao processamento por rotina (1-splitDO.jar, 2-Transmit.sh, Clippingb.jar)
//					- Atualização da regra de quebra por nº de processo para ajustar as publicações tipo PAUTA
//
//	versao 2.4.10	- 14 de Dezembro 2020
//					- Correções de junções de publicações
//					- Otimização dos loops de processamento
//					- Nova versão do Clippingb.java permitindo parametrização
//
//	versao 2.5.1	- 21 de janeiro 2021
//					- Atribuição da propriedade processo com apenas o nº do processo da publicação
//					- Pesquisa avançada sobre publicações do repositório
//					- Correção no método "mapeiaLinhas"
//
//	versao 2.5.2	- 24 de março 2021
//					- SplitDO: Atualizações nos algrítimos de quebra de publicações
//					- SplitDO: Tratamento da linha que contém o nº do processo 
//
//	versao 2.5.2a	- 25 de março 2021
//					- SplitDO:Correções nas regras de quebra implementadas na versao 2.5.2
//
//	versao 2.5.3a	- 29 de março 2021
//					- SplitDO: Atualização na regra de quebra de processo 
//					- SplitDO: Atualização na regra de tratamento de atores, para prever fim dos atores sem a marca "intimados"
//
//	versao 2.6		- 31 de março 2021
//					- Correções na versão 2.5.3a
//					- Correções na finalização das publicações
//					- Inclusão do resumo com quantidade de publicações processadas
//
//	versao clipping 	2.6		- versao inicial
// 	versao cleaner 		2.6		- versão inicial
//	versao splitDO 		2.6.1	- 10 de abril 2021
//								- correções no método carregarIndice
//								- inclusão do método posicionarIndice
//
//	versao splitDO		2.7		- 16 de abril 2021
//								- Inclusão de méteodo formataBufferEntrada() para formatar o bufferEntrada 
//
//
//	versao splitDO 		2.7.1	- 20 de abril 2021
//								-  correções no loop de leituras zerando os buffers de entrada e formatado
//
//
// 	versao splitDO 		2.7.2	- 25 de abril 2021
//								-  Tratamento no método mapeiaLinha() para tratar duplicidades no indice do diario oficial
//
//
//	versao splitDO 		2.8		-  04 de Maio 2021
//								-  Atualização nos algorítimos de quebra
//
//	versao SplitDO		2.9		- 27 de Junho 2021
//								- Loop de leitira de edições idenpendente de parametros informando quais TRT serão processados
//								- Envio do relatorio para o servidor
//
//
//	versao SplitDO		2.A		- .. de Julho 2021
//								- Gravar tabela de quantidades de publicacoes geradas para verificação pela classe ClipDO
//
//
//
// 	V&C Consultoria Ltda.
// 	Autor: Arlindo Viana.
//***************************************************************************************************
//import java.io.File;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.Session;

public class Parametros {
	
	public static final String USUARIO 					= "sij";
    public static final String PASSWORD 				= "934769386";
    public static final String VERSAOSPLITER 			= "2.A";
    public static final String VERSAOCLIPPING 			= "2.7";
    public static final String VERSAOCLEANER 			= "2.6";
    public static final String FONTSIZE 				= "9";
    public static final String FONT 					= "HELVETICA";
    public static final String FONTCOLOR 				= "BLUE";
    public static final int    LIMITECLIENTES 			= 10;
    
    public static Session SESSAO;
    public static InterfaceServidor SERVIDOR = new InterfaceServidor();
    
    public static String CLIENTE 						= "";			// Código do cliente SIJ
    public static String CONEXAO 						= "LOCAL";		// Tipo de conexao LOCAL ou REMOTO
    public static String SYSOP 							= "LINUX";		// SO onde roda o SplitDO
    public static String URL 							= "";			// URL do servidor Alfresco
    public static String LOGFOLDER 						= "";			// Pasta local para o log do splittDO
    public static String PASTACARREGAMENTO 				= "";			// Pasta Alfresco publicações criadas pelo SplitterDO
    public static String PASTAPUBLICACOES 				= "";			// Pasta Alfresco com publicações indexadas pelo Clipping
    public static String PASTATOKENS 					= "";			// Pasta Alfresco com arquivos tokensutilizados pelo Clipping
    public static String PASTASAIDA 					= "";			// Pasta Alfresco destino para as publicações PDF
    public static String PASTAENTRADA 					= "";			// Pasta Localo entrada para o split
    public static String PASTAEDICOES 					= "";			// Pasta Alfresco onde estão as edições dos diarios oficiais
    public static String TIPODOCUMENTO 					= "";			// Tipo do documento no Alfresco
    public static String TIPOPROCESSAMENTO				= "";			// Tipo de processamento desktop ou batch
    public static String TIPOARQUIVOSAIDA				= "";			// Tipo do arquivo saida PFT ou TXT
    public static String PASTADEEDICOES					= "";			// Pasta no Alfresco onde ficam as edições (PDF) para serem processadas
    public static String NOMEPASTACARREGAMENTO			= "";			// Nome da pasta de carregamento (pasta PDFs)
    public static String PASTARELATORIOS				= "";			// Pasta no Alfresco onde SplitDO grava os relatórios de processamento
    
    public static List <String> TABELAUTORES = new ArrayList<String>();
    public static List <String> JURIDIQUES = new ArrayList<String>();
    public static List <String> FALSOFINAL = new ArrayList<String>();
    public static List <String> STOPWORDS = new ArrayList<String>();
    public static List <String> KEYWORDS = new ArrayList<String>();
    public static List <String> FUNCOES = new ArrayList<String>();
    public static List <String> CONTINUADORES = new ArrayList<String>();
    public static List <String> FORMATODATAS = new ArrayList<String>();
    public static List <String> MESES = new ArrayList<String>();

	public static void carregaTabelas() { 
		
		MESES.add("janeiro"); MESES.add("fevereiro"); MESES.add("marco"); 
		MESES.add("abril"); MESES.add("maio"); MESES.add("junho");
		MESES.add("julho"); MESES.add("agosto"); MESES.add("setembro");
		MESES.add("outubro"); MESES.add("novembro"); MESES.add("dezembro");

		CONTINUADORES.add("ao"); CONTINUADORES.add("como"); CONTINUADORES.add("e");
		CONTINUADORES.add("da"); CONTINUADORES.add("das"); CONTINUADORES.add("de");
		CONTINUADORES.add("desse"); CONTINUADORES.add("desses"); CONTINUADORES.add("deste");
		CONTINUADORES.add("destes"); CONTINUADORES.add("nos"); CONTINUADORES.add("-");
		
		FUNCOES.add("chefe da secretaria"); FUNCOES.add("desembargadora relatora"); FUNCOES.add("desembargadora");
		FUNCOES.add("desembargador relator"); FUNCOES.add("desembargador"); FUNCOES.add("desembargador(a)");
		FUNCOES.add("desembargador(a) vice presidente judicial"); FUNCOES.add("desembargador vice presidente judicial");
		FUNCOES.add("desembargadora vice presidente judicial"); FUNCOES.add("diretor de secretaria");
		FUNCOES.add("diretora de secretaria"); FUNCOES.add("juiza titular"); FUNCOES.add("juiz titular");
		FUNCOES.add("juiz(a) do trabalho titular"); FUNCOES.add("juiza do trabalho titular");
		FUNCOES.add("juiz do trabalho titular"); FUNCOES.add("diretor de secretaria");FUNCOES.add("consignataria");
		FUNCOES.add("secretaria"); FUNCOES.add("tecnico judiciario");FUNCOES.add("magistrada");
		FUNCOES.add("consignante");FUNCOES.add("consignatario");FUNCOES.add("magistrado");FUNCOES.add("juiz");
		FUNCOES.add("juiza");FUNCOES.add("servidor");FUNCOES.add("secretario de audiencia");FUNCOES.add("assessor");
		
		TABELAUTORES.add("advogado"); TABELAUTORES.add("advogados");  TABELAUTORES.add("agravado");
        TABELAUTORES.add("agravados"); TABELAUTORES.add("agravante"); TABELAUTORES.add("agravantes");
        TABELAUTORES.add("autor"); TABELAUTORES.add("autores"); TABELAUTORES.add("autoridade coatora");
        TABELAUTORES.add("consignante"); TABELAUTORES.add("consignantes"); TABELAUTORES.add("consignatario");
        TABELAUTORES.add("consignatarios"); TABELAUTORES.add("custos legais"); TABELAUTORES.add("custos legis");
        TABELAUTORES.add("decimo interessado"); TABELAUTORES.add("exequente"); TABELAUTORES.add("exequentes");
        TABELAUTORES.add("executado"); TABELAUTORES.add("executados"); TABELAUTORES.add("impetrante");
        TABELAUTORES.add("impetrantes"); TABELAUTORES.add("impetrado");  TABELAUTORES.add("impetrados");
        TABELAUTORES.add("juizo recorrente"); TABELAUTORES.add("orgao julgador"); TABELAUTORES.add("perito");
        TABELAUTORES.add("peritos");TABELAUTORES.add("consignante");TABELAUTORES.add("consignatario");
        TABELAUTORES.add("reclamante");TABELAUTORES.add("primeiro");TABELAUTORES.add("segundo");
        TABELAUTORES.add("reclamado");TABELAUTORES.add("terceiro");TABELAUTORES.add("quarto");
        TABELAUTORES.add("reclamados");TABELAUTORES.add("quinto");TABELAUTORES.add("sexto");
        TABELAUTORES.add("recorrido");TABELAUTORES.add("setimo");TABELAUTORES.add("oitavo");
        TABELAUTORES.add("recorridos");TABELAUTORES.add("nono");TABELAUTORES.add("decimo");
        TABELAUTORES.add("recorrente");
        TABELAUTORES.add("recorrentes");
        TABELAUTORES.add("requerente");
        TABELAUTORES.add("requerentes");
        TABELAUTORES.add("requerido");
        TABELAUTORES.add("requeridos");
        TABELAUTORES.add("relator");
        TABELAUTORES.add("relatores");
        TABELAUTORES.add("reu");
        TABELAUTORES.add("res");
        TABELAUTORES.add("revisor");
        TABELAUTORES.add("revisores");
        TABELAUTORES.add("solicitado");
        TABELAUTORES.add("solicitados");
        TABELAUTORES.add("solicitante");
        TABELAUTORES.add("solicitantes");
        TABELAUTORES.add("suscitado");
        TABELAUTORES.add("suscitada");
        TABELAUTORES.add("polo ativo");
        TABELAUTORES.add("polo passivo");
        TABELAUTORES.add("primeiro interessado");
        TABELAUTORES.add("procedencia");
        TABELAUTORES.add("segundo interessado");
        TABELAUTORES.add("terceiro interessado");
        TABELAUTORES.add("testemunha");
        TABELAUTORES.add("quarto interessado");
        TABELAUTORES.add("quinto interessado");
        TABELAUTORES.add("sexto interessado");
        TABELAUTORES.add("setimo interessado");
        TABELAUTORES.add("oitavo interessado");
        TABELAUTORES.add("nono interessado");

        KEYWORDS.add("1.");
		KEYWORDS.add("2.");
		KEYWORDS.add("3.");
		KEYWORDS.add("4.");
		KEYWORDS.add("5.");
		KEYWORDS.add("6.");
		KEYWORDS.add("7.");
		KEYWORDS.add("8.");
		KEYWORDS.add("9.");
		KEYWORDS.add("0.");
    	KEYWORDS.add("artigo");
    	KEYWORDS.add("advogado");
    	KEYWORDS.add("advogados");
    	KEYWORDS.add("cartorio");
    	KEYWORDS.add("cartorios");
    	KEYWORDS.add("clt");
    	KEYWORDS.add("codigo");
    	KEYWORDS.add("codigos");
    	KEYWORDS.add("colegiado");
    	KEYWORDS.add("colegiados");
    	KEYWORDS.add("defesa");
    	KEYWORDS.add("empregado");
    	KEYWORDS.add("empregador");
    	KEYWORDS.add("empregadores");
    	KEYWORDS.add("empregados");
    	KEYWORDS.add("empresa");
    	KEYWORDS.add("empresas");
    	KEYWORDS.add("evidencias");
    	KEYWORDS.add("evidencia");
    	KEYWORDS.add("funcionario");
    	KEYWORDS.add("funcionarios");
    	KEYWORDS.add("inciso");
    	KEYWORDS.add("interessado");
    	KEYWORDS.add("interessados");
		KEYWORDS.add("intimado");
		KEYWORDS.add("intimados");
		KEYWORDS.add("indenizacao");
		KEYWORDS.add("indenizacoes");
		KEYWORDS.add("judiciario");
		KEYWORDS.add("julgamento");
		KEYWORDS.add("julgamentos");
		KEYWORDS.add("julgado");
		KEYWORDS.add("julgados");
		KEYWORDS.add("juiz");
		KEYWORDS.add("juizes");
		KEYWORDS.add("ministerio");
		KEYWORDS.add("ministerios");
		KEYWORDS.add("membro");
		KEYWORDS.add("membros");
		KEYWORDS.add("oral");
		KEYWORDS.add("ordinaria");
		KEYWORDS.add("pagina");
		KEYWORDS.add("paginas");
		KEYWORDS.add("pauta");
		KEYWORDS.add("pautas");
		KEYWORDS.add("pedido");
		KEYWORDS.add("pedidos");
		KEYWORDS.add("penal");
		KEYWORDS.add("penais");
		KEYWORDS.add("pje");
		KEYWORDS.add("presencial");
		KEYWORDS.add("procedencia");
		KEYWORDS.add("processos");
		KEYWORDS.add("processo");
		KEYWORDS.add("prova");
		KEYWORDS.add("provas");
		KEYWORDS.add("publicacao");
		KEYWORDS.add("publicacoes");
		KEYWORDS.add("publico");
		KEYWORDS.add("regional");
		KEYWORDS.add("remoto");
		KEYWORDS.add("resolucao");
		KEYWORDS.add("tecnico");
		KEYWORDS.add("trabalho");
		KEYWORDS.add("trabalhista");
		KEYWORDS.add("tribunal");
		KEYWORDS.add("trt");
		KEYWORDS.add("tst");
		KEYWORDS.add("vara");
		KEYWORDS.add("varas");
		KEYWORDS.add("primeiro");
        KEYWORDS.add("segundo");
        KEYWORDS.add("terceiro");
        KEYWORDS.add("quarto");
        KEYWORDS.add("quinto");
        KEYWORDS.add("sexto");
        KEYWORDS.add("setimo");
        KEYWORDS.add("oitavo");
        KEYWORDS.add("nono");
        KEYWORDS.add("decimo");
        KEYWORDS.add("pernambuco");
		KEYWORDS.add("rio");
		KEYWORDS.add("janeiro");
		KEYWORDS.add("sao paulo");
		KEYWORDS.add("amazonas");
		KEYWORDS.add("para");
		KEYWORDS.add("piaui");
		KEYWORDS.add("maranhao");
		KEYWORDS.add("ceara");
		KEYWORDS.add("grande");
		KEYWORDS.add("norte");
		KEYWORDS.add("sul");
		KEYWORDS.add("parana");
		KEYWORDS.add("supremo");
		KEYWORDS.add("santa catarina");
		KEYWORDS.add("tocantins");
		KEYWORDS.add("mato grosso");
		KEYWORDS.add("mato grosso do sul");
		KEYWORDS.add("goias");
		KEYWORDS.add("espirito");
		KEYWORDS.add("santo");
		KEYWORDS.add("paraiba");
		KEYWORDS.add("alagoas");
		KEYWORDS.add("sergipe");
		KEYWORDS.add("bahia");
		KEYWORDS.add("acre");
		KEYWORDS.add("rondonia");
		KEYWORDS.add("roraima");
		KEYWORDS.add("brasilia");
		KEYWORDS.add("fevereiro");
		KEYWORDS.add("marco");
		KEYWORDS.add("abril");
		KEYWORDS.add("maio");
		KEYWORDS.add("junho");
		KEYWORDS.add("julho");
		KEYWORDS.add("agosto");
		KEYWORDS.add("setembro");
		KEYWORDS.add("outubro");
		KEYWORDS.add("novembro");
		KEYWORDS.add("dezembro");
		KEYWORDS.add("ac");
		KEYWORDS.add("al");
		KEYWORDS.add("am");
		KEYWORDS.add("ap");
		KEYWORDS.add("ba");
		KEYWORDS.add("ce");
		KEYWORDS.add("df");
		KEYWORDS.add("es");
		KEYWORDS.add("go");
		KEYWORDS.add("ma");
		KEYWORDS.add("mg");
		KEYWORDS.add("ms");
		KEYWORDS.add("mt");
		KEYWORDS.add("pa");
		KEYWORDS.add("pb");
		KEYWORDS.add("pe");
		KEYWORDS.add("pi");
		KEYWORDS.add("pr");
		KEYWORDS.add("rj");
		KEYWORDS.add("rn");
		KEYWORDS.add("ro");
		KEYWORDS.add("rr");
		KEYWORDS.add("rs");
		KEYWORDS.add("sc");
		KEYWORDS.add("se");
		KEYWORDS.add("sp");
		KEYWORDS.add("to");
        
        JURIDIQUES.add("acao");
        JURIDIQUES.add("acoes");
        JURIDIQUES.add("acao");
        JURIDIQUES.add("acoes");
        JURIDIQUES.add("acordao");
        JURIDIQUES.add("admissibilidade");
        JURIDIQUES.add("agravo");
        JURIDIQUES.add("agravos");
        JURIDIQUES.add("atraso");
        JURIDIQUES.add("alegacoes");
        JURIDIQUES.add("assinatura");
        JURIDIQUES.add("cartorio");
        JURIDIQUES.add("cartorios");
		JURIDIQUES.add("certidao");
		JURIDIQUES.add("certidoes");
		JURIDIQUES.add("ciencia");
		JURIDIQUES.add("citacao");
		JURIDIQUES.add("coisa");
		JURIDIQUES.add("conclusao");
		JURIDIQUES.add("conexao");
		JURIDIQUES.add("continencia");
		JURIDIQUES.add("consideracoes");
		JURIDIQUES.add("contestacao");
		JURIDIQUES.add("continencia");	
		JURIDIQUES.add("danos");
		JURIDIQUES.add("danos");
		JURIDIQUES.add("gorjetas");
		JURIDIQUES.add("preliminares");
		JURIDIQUES.add("deposito");
		JURIDIQUES.add("garantia ");
		JURIDIQUES.add("despacho");
		JURIDIQUES.add("direito");
		JURIDIQUES.add("edital");
		JURIDIQUES.add("embargos");
		JURIDIQUES.add("embargos"); 
		JURIDIQUES.add("ementa");
		JURIDIQUES.add("ementa:");
		JURIDIQUES.add("empregado");
		JURIDIQUES.add("empregados");
		JURIDIQUES.add("empregador");
		JURIDIQUES.add("empregadores");
		JURIDIQUES.add("empregaticio");
		JURIDIQUES.add("excecoes");
		JURIDIQUES.add("execucao");
		JURIDIQUES.add("execucoes");
		JURIDIQUES.add("extrinsecos");
		JURIDIQUES.add("fgts");
		JURIDIQUES.add("fundamentacao");
		JURIDIQUES.add("hora");
		JURIDIQUES.add("horas");
		JURIDIQUES.add("extra");
		JURIDIQUES.add("extras");
		JURIDIQUES.add("indenizacao");
		JURIDIQUES.add("indevida");
		JURIDIQUES.add("inepcia");
		JURIDIQUES.add("intimacao");
		JURIDIQUES.add("intrinseco");
		JURIDIQUES.add("intrinsecos");
		JURIDIQUES.add("instrumento");
        JURIDIQUES.add("instrumentos");
		JURIDIQUES.add("ipca");
		JURIDIQUES.add("inpc");
		JURIDIQUES.add("juizo");
		JURIDIQUES.add("julgada");
		JURIDIQUES.add("julgadas");
		JURIDIQUES.add("julgador");
		JURIDIQUES.add("julgadores");
		JURIDIQUES.add("julgamento");
		JURIDIQUES.add("julgamentos");
		JURIDIQUES.add("judiciario");
		JURIDIQUES.add("juros");
		JURIDIQUES.add("justica");
		JURIDIQUES.add("litispendencia");
		JURIDIQUES.add("merito");
		JURIDIQUES.add("mora");
		JURIDIQUES.add("morais");
		JURIDIQUES.add("moral");
		JURIDIQUES.add("nota");
		JURIDIQUES.add("nulidade");
		JURIDIQUES.add("obreiro");
		JURIDIQUES.add("orgao julgador");
		JURIDIQUES.add("pagamento");
		JURIDIQUES.add("pagamentos");
		JURIDIQUES.add("pleno");
		JURIDIQUES.add("poder");
		JURIDIQUES.add("poder judiciario");
		JURIDIQUES.add("preliminar");
		JURIDIQUES.add("pressuposto");
		JURIDIQUES.add("pressupostos");
		JURIDIQUES.add("prova");
		JURIDIQUES.add("provas");
		JURIDIQUES.add("provimento");
		JURIDIQUES.add("provimentos");
		JURIDIQUES.add("reconvencao");
		JURIDIQUES.add("recesso");
		JURIDIQUES.add("recisao");
		JURIDIQUES.add("recurso");
		JURIDIQUES.add("recursos");
		JURIDIQUES.add("reclamante");
		JURIDIQUES.add("reclamantes");
		JURIDIQUES.add("reclamada");
		JURIDIQUES.add("reclamadas");
		JURIDIQUES.add("reclamado");
		JURIDIQUES.add("reclamados");
		JURIDIQUES.add("revista");
		JURIDIQUES.add("ordinario");
		JURIDIQUES.add("ordinarios");
		JURIDIQUES.add("ordinario");
        JURIDIQUES.add("regimental");
        JURIDIQUES.add("regimentais");
        JURIDIQUES.add("regimento");
        JURIDIQUES.add("regimentos");
        JURIDIQUES.add("resolucao");
        JURIDIQUES.add("resolucoes");
		JURIDIQUES.add("reu");
        JURIDIQUES.add("salario");
        JURIDIQUES.add("salarios");
		JURIDIQUES.add("secao");
		JURIDIQUES.add("secretaria");
		JURIDIQUES.add("sentenca");
		JURIDIQUES.add("sessao");
		JURIDIQUES.add("sustentacao");
//		JURIDIQUES.add("trabalho");
		JURIDIQUES.add("turma");
		JURIDIQUES.add("trt");
		JURIDIQUES.add("tst");
		JURIDIQUES.add("vinculo");
		JURIDIQUES.add("vara");
		JURIDIQUES.add("varas");
		JURIDIQUES.add("voto");
		JURIDIQUES.add("votos");

		FALSOFINAL.add("ltda.");
		FALSOFINAL.add("a/a.");
		FALSOFINAL.add("v sa.");
		FALSOFINAL.add("v.sa.");
		FALSOFINAL.add("arts.");
		FALSOFINAL.add("clt.");
		FALSOFINAL.add("pag.");
		FALSOFINAL.add("art.");
		FALSOFINAL.add("inc.");
		FALSOFINAL.add("v.");
		
		STOPWORDS.add("a");
		STOPWORDS.add("e");
		STOPWORDS.add("o");
		STOPWORDS.add("as");
		STOPWORDS.add("os");
		STOPWORDS.add("na");
		STOPWORDS.add("no");
		STOPWORDS.add("da");
		STOPWORDS.add("de");
		STOPWORDS.add("do");
		STOPWORDS.add("mas");
		STOPWORDS.add("mais");
		STOPWORDS.add("porem");
		STOPWORDS.add("entretanto");
		STOPWORDS.add("de");
		STOPWORDS.add("ele");
		STOPWORDS.add("num");
		STOPWORDS.add("delas");
		STOPWORDS.add("houveramos");
		STOPWORDS.add("seremos");
		STOPWORDS.add("qualquer");
		STOPWORDS.add("assim");
		STOPWORDS.add("das");
		STOPWORDS.add("nem");
		STOPWORDS.add("esta");
		STOPWORDS.add("haja");
		STOPWORDS.add("serao");
		STOPWORDS.add("cada");
		STOPWORDS.add("afim");
		STOPWORDS.add("tem");
		STOPWORDS.add("suas");
		STOPWORDS.add("estes");
		STOPWORDS.add("hajamos");
		STOPWORDS.add("seria");
		STOPWORDS.add("apos");
		STOPWORDS.add("agora");
		STOPWORDS.add("meu");
		STOPWORDS.add("estas");
		STOPWORDS.add("hajam");
		STOPWORDS.add("seriamos");
		STOPWORDS.add("durante");
		STOPWORDS.add("onde");
		STOPWORDS.add("seu");
		STOPWORDS.add("as");
		STOPWORDS.add("aquele");
		STOPWORDS.add("houvesse");
		STOPWORDS.add("seriam");
		STOPWORDS.add("entanto");
		STOPWORDS.add("outro");
		STOPWORDS.add("sua");
		STOPWORDS.add("minha");
		STOPWORDS.add("aquela");
		STOPWORDS.add("houvessemos");
		STOPWORDS.add("tenho");
		STOPWORDS.add("sempre");
		STOPWORDS.add("outros");
		STOPWORDS.add("ou");
		STOPWORDS.add("tem");
		STOPWORDS.add("aqueles");
		STOPWORDS.add("houvessem");
		STOPWORDS.add("tem");
		STOPWORDS.add("menos");
		STOPWORDS.add("ainda");
		STOPWORDS.add("ser");
		STOPWORDS.add("numa");
		STOPWORDS.add("aquelas");
		STOPWORDS.add("houver");
		STOPWORDS.add("temos");
		STOPWORDS.add("mais");
		STOPWORDS.add("a");
		STOPWORDS.add("quando");
		STOPWORDS.add("pelos");
		STOPWORDS.add("isto");
		STOPWORDS.add("houvermos");
		STOPWORDS.add("tem");
		STOPWORDS.add("caso");
		STOPWORDS.add("o");
		STOPWORDS.add("muito");
		STOPWORDS.add("elas");
		STOPWORDS.add("aquilo");
		STOPWORDS.add("houverem");
		STOPWORDS.add("tinha");
		STOPWORDS.add("segundo");
		STOPWORDS.add("que");
		STOPWORDS.add("ha");
		STOPWORDS.add("havia");
		STOPWORDS.add("estou");
		STOPWORDS.add("houverei");
		STOPWORDS.add("tinhamos");
		STOPWORDS.add("aqueles");
		STOPWORDS.add("vario");
		STOPWORDS.add("nos");
		STOPWORDS.add("seja");
		STOPWORDS.add("esta");
		STOPWORDS.add("houvera");
		STOPWORDS.add("houve");
		STOPWORDS.add("houveram");
		STOPWORDS.add("houverao");
		STOPWORDS.add("tinham");
		STOPWORDS.add("destas");
		STOPWORDS.add("desta");
		STOPWORDS.add("destes");
		STOPWORDS.add("dezessete");
		STOPWORDS.add("varios");
		STOPWORDS.add("ja");
		STOPWORDS.add("qual");
		STOPWORDS.add("estamos");
		STOPWORDS.add("houveremos");
		STOPWORDS.add("houver");
		STOPWORDS.add("houveriamos");
		STOPWORDS.add("tive");
		STOPWORDS.add("todos");
		STOPWORDS.add("vario");
		STOPWORDS.add("varios");
		STOPWORDS.add("varias");
		STOPWORDS.add("esta");
		STOPWORDS.add("sera");
		STOPWORDS.add("estão");
		STOPWORDS.add("houverao");
		STOPWORDS.add("teve");
		STOPWORDS.add("varios");
		STOPWORDS.add("eu");
		STOPWORDS.add("nos");
		STOPWORDS.add("estive");
		STOPWORDS.add("houveria");
		STOPWORDS.add("tivemos");
		STOPWORDS.add("e");
		STOPWORDS.add("tambem");
		STOPWORDS.add("tenho");
		STOPWORDS.add("esteve");
		STOPWORDS.add("houveriamos");
		STOPWORDS.add("tiveram");
		STOPWORDS.add("do");
		STOPWORDS.add("so");
		STOPWORDS.add("lhe");
		STOPWORDS.add("estivemos");
		STOPWORDS.add("houveriam");
		STOPWORDS.add("tivera");
		STOPWORDS.add("da");
		STOPWORDS.add("pelo");
		STOPWORDS.add("deles");
		STOPWORDS.add("estiveram ");
		STOPWORDS.add("sou");
		STOPWORDS.add("tiveramos");
		STOPWORDS.add("uns");
		STOPWORDS.add("pela ");
		STOPWORDS.add("essas");
		STOPWORDS.add("estava");
		STOPWORDS.add("somos");
		STOPWORDS.add("tenha");
		STOPWORDS.add("em");
		STOPWORDS.add("ate");
		STOPWORDS.add("esses");
		STOPWORDS.add("estavamos");
		STOPWORDS.add("sao");
		STOPWORDS.add("tenhamos");
		STOPWORDS.add("um");
		STOPWORDS.add("isso");
		STOPWORDS.add("pelas");
		STOPWORDS.add("estavam");
		STOPWORDS.add("era");
		STOPWORDS.add("tenham");
		STOPWORDS.add("para");
		STOPWORDS.add("ela");
		STOPWORDS.add("este");
		STOPWORDS.add("estivera");
		STOPWORDS.add("eramos");
		STOPWORDS.add("tivesse");
		STOPWORDS.add("e");
		STOPWORDS.add("entre");
		STOPWORDS.add("fosse");
		STOPWORDS.add("estiveramos");
		STOPWORDS.add("eram");
		STOPWORDS.add("tivessemos");
		STOPWORDS.add("antes");
		STOPWORDS.add("era");
		STOPWORDS.add("dele");
		STOPWORDS.add("esteja");
		STOPWORDS.add("fui");
		STOPWORDS.add("tivessem");
		STOPWORDS.add("anti");
		STOPWORDS.add("depois");
		STOPWORDS.add("tu");
		STOPWORDS.add("estejamos");
		STOPWORDS.add("foi");
		STOPWORDS.add("tiver");
		STOPWORDS.add("com");
		STOPWORDS.add("sem ");
		STOPWORDS.add("te"); 
		STOPWORDS.add("estejam"); 
		STOPWORDS.add("fomos");
		STOPWORDS.add("tivermos");
		STOPWORDS.add("não");
		STOPWORDS.add("mesmo"); 
		STOPWORDS.add("vocês"); 
		STOPWORDS.add("estivesse"); 
		STOPWORDS.add("foram"); 
		STOPWORDS.add("tiverem");
		STOPWORDS.add("uma"); 
		STOPWORDS.add("aos"); 
		STOPWORDS.add("vos"); 
		STOPWORDS.add("estivéssemos"); 
		STOPWORDS.add("fora");
		STOPWORDS.add("terei");
		STOPWORDS.add("os"); 
		STOPWORDS.add("ter");
		STOPWORDS.add("lhes");
		STOPWORDS.add("estivessem"); 
		STOPWORDS.add("foramos"); 
		STOPWORDS.add("tera");
		STOPWORDS.add("no");
		STOPWORDS.add("seus"); 
		STOPWORDS.add("meus"); 
		STOPWORDS.add("estiver"); 
		STOPWORDS.add("seja"); 
		STOPWORDS.add("teremos");
		STOPWORDS.add("se"); 
		STOPWORDS.add("quem");
		STOPWORDS.add("minhas"); 
		STOPWORDS.add("estivermos"); 
		STOPWORDS.add("sejamos"); 
		STOPWORDS.add("terao");
		STOPWORDS.add("na");
		STOPWORDS.add("nas"); 
		STOPWORDS.add("teu"); 
		STOPWORDS.add("estiverem"); 
		STOPWORDS.add("sejam"); 
		STOPWORDS.add("teria");
		STOPWORDS.add("por"); 
		STOPWORDS.add("me"); 
		STOPWORDS.add("tua"); 
		STOPWORDS.add("hei"); 
		STOPWORDS.add("fosse"); 
		STOPWORDS.add("teriamos");
		STOPWORDS.add("mais"); 
		STOPWORDS.add("esse"); 
		STOPWORDS.add("teus"); 
		STOPWORDS.add("ha"); 
		STOPWORDS.add("fossemos"); 
		STOPWORDS.add("teriam");
		STOPWORDS.add("as"); 
		STOPWORDS.add("eles"); 
		STOPWORDS.add("tuas"); 
		STOPWORDS.add("havemos"); 
		STOPWORDS.add("fossem"); 
		STOPWORDS.add("porem");
		STOPWORDS.add("dos"); 
		STOPWORDS.add("estao"); 
		STOPWORDS.add("nosso"); 
		STOPWORDS.add("hao"); 
		STOPWORDS.add("for"); 
		STOPWORDS.add("todavia");
		STOPWORDS.add("como");
		STOPWORDS.add("voce");
		STOPWORDS.add("nossa"); 
		STOPWORDS.add("houve"); 
		STOPWORDS.add("formos"); 
		STOPWORDS.add("entretanto");
		STOPWORDS.add("mas"); 
		STOPWORDS.add("tinha"); 
		STOPWORDS.add("nossos"); 
		STOPWORDS.add("houvemos"); 
		STOPWORDS.add("forem"); 
		STOPWORDS.add("contudo");
		STOPWORDS.add("foi"); 
		STOPWORDS.add("foram"); 
		STOPWORDS.add("nossas"); 
		STOPWORDS.add("houveram");
		STOPWORDS.add("serei"); 
		STOPWORDS.add("quer");
		STOPWORDS.add("ao"); 
		STOPWORDS.add("essa"); 
		STOPWORDS.add("dela"); 
		STOPWORDS.add("houvera");
		STOPWORDS.add("sera"); 
		STOPWORDS.add("quais");
		
		// - Formatos de datas
		// dd/mm/aaaa
		FORMATODATAS.add("(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/(19|20)\\d{2}");			// dd/mm/aaaa
		
		//aaaammdd
		FORMATODATAS.add("\"^(?:(?:(?:(?:[13579][26]|[2468][048])00)|(?:[0-9]{2}(?:(?:[13579][26])|(?:[2468][048]|0[48]))))(?:(?:(?:09|04|06|11)(?:0[1-9]|1[0-9]|2[0-9]|30))|(?:(?:01|03|05|07|08|10|12)(?:0[1-9]|1[0-9]|2[0-9]|3[01]))|(?:02(?:0[1-9]|1[0-9]|2[0-9]))))|(?:[0-9]{4}(?:(?:(?:09|04|06|11)(?:0[1-9]|1[0-9]|2[0-9]|30))|(?:(?:01|03|05|07|08|10|12)(?:0[1-9]|1[0-9]|2[0-9]|3[01]))|(?:02(?:[01][0-9]|2[0-8]))))$\"");
		
		//dd/mm/aaaa
		FORMATODATAS.add("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$");

	}

}