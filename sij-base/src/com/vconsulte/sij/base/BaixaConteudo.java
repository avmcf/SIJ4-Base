package com.vconsulte.sij.base;

	import java.io.BufferedInputStream;
	import java.io.BufferedOutputStream;
	import java.io.FileOutputStream;
	import java.io.IOException;
	import java.io.OutputStream;
	import org.apache.chemistry.opencmis.client.api.Document;
	import org.apache.chemistry.opencmis.client.api.Session;
    import org.apache.chemistry.opencmis.commons.data.ContentStream;

public class BaixaConteudo {

	public static boolean baixaConteudo(Session sessao, String IdDocumento, String tribunal, String pastaDestino) throws IOException{
		String fullPath = "";
		if(tribunal.length() == 2) {
			fullPath = pastaDestino + "/" + tribunal+"edicao.pdf";
		} else {
			fullPath = pastaDestino + "/" + tribunal;
		}
		Document newDocument =  (Document) sessao.getObject(IdDocumento);
		try {
			ContentStream cs = newDocument.getContentStream(null);
			BufferedInputStream in =new BufferedInputStream(cs.getStream());
			FileOutputStream fos = new FileOutputStream(fullPath);
			OutputStream bufferedOutputStream = new BufferedOutputStream(fos);
			byte[] buf = new byte[1024];
			int n=0;
			while ((n=in.read(buf))>0){
				bufferedOutputStream.write(buf,0,n);
			}
			bufferedOutputStream.close();
			fos.close();
			in.close();
			return true;
		}
		catch (IOException e)
		{
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}
}
