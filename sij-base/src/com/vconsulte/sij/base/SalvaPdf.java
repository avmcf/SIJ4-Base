package com.vconsulte.sij.base;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.hyphenation.HyphenationConfig;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.vconsulte.sij.base.Base;
//import com.vconsulte.sij.base.Edital;
//import com.vconsulte.sij.base.Parametros;

/**
 * 
 * 
 * 
 * 
 * 
 */

public class SalvaPdf extends Base  {
	
	static Edital Edital = new Edital();
	static Base Base = new Base();
	static int k = 0;
	static String pastaSaida;

	@SuppressWarnings("resource")
	public static void gravaPdf() throws IOException {
		com.vconsulte.sij.base.Configuracao.carregaConfig();
		pastaSaida = com.vconsulte.sij.base.Parametros.PASTASAIDA;
		String nomeArquivo = getFileName();
		String nome = "";
		String pastaPDF = getPastaPDF();
		pastaSaida = pastaSaida + "/" + pastaPDF + "/" + nomeArquivo;
		
		PdfDocument pdfDocumento = new PdfDocument(new PdfWriter(pastaSaida));
		Document documento = new Document(pdfDocumento);
		documento = inicializaDocumento(pdfDocumento);
		formataTitulo(documento);
		formataLocalizacao(documento);
		formataAtores(documento);
		formataIntimados(documento);
		formataIntroducao(documento);
		formataTexto(documento);
		numeraPaginas(pdfDocumento, documento);
		documento.close();
	}
	
	public static void formataIntroducao(Document documento) throws IOException {
    	
    	PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA);
    	ArrayList<String> textoIntroducao = new ArrayList<String>(Edital.getIntroducao());
        String line = "";
        Paragraph p;
        boolean title = true;
        int ix = 0;
        if(textoIntroducao.size()>0) {
	        while (ix < textoIntroducao.size()) {
	        	line = textoIntroducao.get(ix);
	            p = new Paragraph(line);
	            p.setKeepTogether(true);
	            if (title) {								// Titulo
	                p.setFont(bold).setFontSize(12);
	                title = false;
	            }
	            else {
	                p.setFirstLineIndent(8);
	            }
	            if (line.isEmpty()) {
	                p.setMarginBottom(12);
	                title = true;
	            }
	            else {
	                p.setMarginBottom(0);
	            }
	            documento.add(p);
	            ix++;
	        }
        }
    }
    
    public static void formataTexto(Document documento) throws IOException {
    	
    	PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
    	ArrayList<String> textoEdital = new ArrayList<String>(Edital.getTexto());
        String line = "";
        Paragraph p;
        boolean title = true;
        int ix = 0;
        while (ix < textoEdital.size()) {
        	line = textoEdital.get(ix);
            p = new Paragraph(line);
            p.setKeepTogether(true);
            if (title) {								// Titulo
                p.setFont(bold).setFontSize(12);
                title = false;
            }
            else {
                p.setFirstLineIndent(8);
            }
            if (line.isEmpty()) {
                p.setMarginBottom(12);
                title = true;
            }
            else {
                p.setMarginBottom(0);
            }
            documento.add(p);
            ix++;
        }
    }
    
    public static void numeraPaginas(PdfDocument pdfDocumento, Document documento) {
    	int n = pdfDocumento.getNumberOfPages();
        Paragraph footer;
        for (int page = 1; page <= n; page++) {
            footer = new Paragraph(String.format("Página %s/%s", page, n));
            documento.showTextAligned(footer, 297.5f, 20, page, TextAlignment.CENTER, VerticalAlignment.MIDDLE, 0);
        }
    }
    
    public static Document inicializaDocumento(PdfDocument documento) throws IOException {

        Document docPdf = new Document(documento, PageSize.A4, false);
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        docPdf.setTextAlignment(TextAlignment.JUSTIFIED)
            .setHyphenation(new HyphenationConfig("pt", null, 3, 3))
            .setFont(font)
            .setFontSize(10);
        return docPdf;
    }
    
    public static void formataTitulo(Document documento) throws IOException {
    	
    	PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
    	
    	float [] pointColumnWidths = {1F, 470F, 1F};
    	
    	Table linha1 = new Table(pointColumnWidths).setFont(bold);
    	linha1.addCell(getCell("", TextAlignment.LEFT));
		linha1.addCell(getCell(Edital.getTitulo1(), TextAlignment.CENTER));
		linha1.addCell(getCell("", TextAlignment.RIGHT));
		documento.add(linha1);
		
		Table linha2 = new Table(pointColumnWidths).setFont(bold);
    	linha2.addCell(getCell("", TextAlignment.LEFT));
		linha2.addCell(getCell(Edital.getTitulo2(), TextAlignment.CENTER));
		linha2.addCell(getCell("", TextAlignment.RIGHT));
		documento.add(linha2);
		
		Table linha3 = new Table(pointColumnWidths).setFont(bold);
    	linha3.addCell(getCell("", TextAlignment.LEFT));
		linha3.addCell(getCell(Edital.getTitulo3(), TextAlignment.CENTER));
		linha3.addCell(getCell("", TextAlignment.RIGHT));
		documento.add(linha3);
		
		Table linha4 = new Table(pointColumnWidths).setFont(bold);
    	linha4.addCell(getCell("", TextAlignment.LEFT));
		linha4.addCell(getCell(Edital.getTitulo4(), TextAlignment.CENTER));
		linha4.addCell(getCell("", TextAlignment.RIGHT));
		documento.add(linha4);
		
		Table linha5 = new Table(pointColumnWidths).setFont(bold);
    	linha5.addCell(getCell("", TextAlignment.LEFT));
		linha5.addCell(getCell(Edital.getTitulo5(), TextAlignment.CENTER));
		linha5.addCell(getCell("", TextAlignment.RIGHT));
		documento.add(linha5);
    }
    
    public static void formataLocalizacao(Document documento) throws IOException {
    	
    	PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
    	float [] pointColumnWidths = {1F, 660F, 1F};
    	float [] pointColumnWidths2 = {400F, 1F, 1F};

    	Table linha0 = new Table(pointColumnWidths).setFontSize(14);
    	linha0.addCell(getCell("", TextAlignment.LEFT));
		linha0.addCell(getCell("--- * ---", TextAlignment.CENTER));
		linha0.addCell(getCell("", TextAlignment.RIGHT));
		documento.add(linha0);
    	
    	Table linha1 = new Table(pointColumnWidths).setFontSize(14);
    	linha1.addCell(getCell("", TextAlignment.LEFT));
		linha1.addCell(getCell(Edital.getVara(), TextAlignment.CENTER));
		linha1.addCell(getCell("", TextAlignment.RIGHT));
		documento.add(linha1);
		
		Table linha2 = new Table(pointColumnWidths).setFontSize(14);
    	linha2.addCell(getCell("", TextAlignment.LEFT));
		linha2.addCell(getCell(Edital.getGrupo(), TextAlignment.CENTER));
		linha2.addCell(getCell("", TextAlignment.RIGHT));
		documento.add(linha2);
		
		Table linha3 = new Table(pointColumnWidths).setFontSize(14);
    	linha3.addCell(getCell("", TextAlignment.LEFT));
		linha3.addCell(getCell(Edital.getAssunto(), TextAlignment.CENTER));
		linha3.addCell(getCell("", TextAlignment.RIGHT));
		documento.add(linha3);

		Table linha4 = new Table(pointColumnWidths2).setFontSize(14);
    	linha4.addCell(getCell(Edital.getProcessoLinha(), TextAlignment.LEFT));
		linha4.addCell(getCell("", TextAlignment.LEFT));
		linha4.addCell(getCell("", TextAlignment.RIGHT));
		documento.add(linha4);
    }
    
    public static void formataAtores(Document documento) throws IOException {
    	
    	PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
    	Paragraph atores = new Paragraph(Edital.getAtores());
    	atores.setFont(font).setFontSize(12);
    	atores.add(new Tab());
    	atores.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
    	documento.add(atores);
    }
    
    public static void formataIntimados(Document documento) throws IOException {
    	
    	PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
    	Paragraph intimados = new Paragraph(Edital.getIntimados());
    	intimados.setFont(font).setFontSize(12);
    	intimados.add(new Tab());
    	intimados.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
    	documento.add(intimados);
    	intimados.add(new Tab());
    }

    public static Cell getCell(String text, TextAlignment alignment) {
	    Cell cell = new Cell().add(new Paragraph(text));
	    cell.setPadding(0);
	    cell.setTextAlignment(alignment);
	    cell.setBorder(Border.NO_BORDER);
	    return cell;
    }
}