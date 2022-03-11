package it.kdm.docer.timbrodigitale.bl;

import it.kdm.docer.timbrodigitale.sdk.BusinessLogicException;
import it.kdm.docer.timbrodigitale.sdk.IBusinessLogic;
import it.kdm.docer.timbrodigitale.sdk.IProvider;
import it.kdm.docer.timbrodigitale.sdk.ImageFormat;
import it.kdm.docer.timbrodigitale.sdk.ProviderException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;

public class BusinessLogic implements IBusinessLogic{

	private IProvider provider;

	public BusinessLogic(IProvider provider) {
		this.provider = provider;
	}
	
//	public byte[] applicaTimbro(byte[] timbro, byte[] pdf, int pagina, int x,
//			int y) throws BusinessLogicException {
//		
//		try {						
//			String defaulSystemAllowUnsafe = System
//			.getProperty("sun.security.ssl.allowUnsafeRenegotiation");
//			String configAllowUnsafe = this.provider.getConfiguration()
//					.get("only_for_test_allowUnsafeRenegotiation").toString();
//			if (configAllowUnsafe != null
//					&& configAllowUnsafe.equalsIgnoreCase("true")) {
//				if (defaulSystemAllowUnsafe == null
//						|| !defaulSystemAllowUnsafe.equalsIgnoreCase("true")) {
//					System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
//				}
//			}
//			InputStream documentoPDF = new ByteArrayInputStream(pdf);
//			PDDocument pddocPDF = PDDocument.load(documentoPDF);
//			if(pddocPDF!=null && pddocPDF.getNumberOfPages()>0){
//				if(pagina<=pddocPDF.getNumberOfPages()){
//					PDPage page = (PDPage)pddocPDF.getDocumentCatalog().getAllPages().get(pagina-1);
//					InputStream in = new ByteArrayInputStream(timbro);
//					BufferedImage image = ImageIO.read(in);
//					PDJpeg img = new PDJpeg(pddocPDF, image);
//					PDPageContentStream contentStream = new PDPageContentStream(pddocPDF, page, true, true);
//					contentStream.drawXObject(img, x, y, img.getWidth(),img.getHeight());
//					contentStream.close();
//					ByteArrayOutputStream os = new ByteArrayOutputStream();
//					pddocPDF.save(os);
//					pddocPDF.close();
//					return os.toByteArray();
//
//				}else
//					throw new BusinessLogicException("Il documento contiene un numero di pagine inferiore a quelle richieste");
//			}else
//				throw new BusinessLogicException("Impossibile leggere il documento pdf");
//		} catch (COSVisitorException e) {
//			throw new BusinessLogicException(e);
//		} catch (IOException e) {
//			throw new BusinessLogicException(e);
//		}
//		catch (ProviderException e) {
//			throw new BusinessLogicException(e);
//		}
//	}

	public File applicaTimbro(InputStream timbro, InputStream pdfStream, int pagina, int x,
			int y) throws BusinessLogicException {
		
		PDDocument pddocPDF = null;
		try {					
			
			String tempDir = this.provider.getConfiguration()
					.get("file_buffer_directory").toString();
			
			File f = new File(tempDir,UUID.randomUUID().toString());
			
			String defaulSystemAllowUnsafe = System
			.getProperty("sun.security.ssl.allowUnsafeRenegotiation");
			String configAllowUnsafe = this.provider.getConfiguration()
					.get("only_for_test_allowUnsafeRenegotiation").toString();
			if (configAllowUnsafe != null
					&& configAllowUnsafe.equalsIgnoreCase("true")) {
				if (defaulSystemAllowUnsafe == null
						|| !defaulSystemAllowUnsafe.equalsIgnoreCase("true")) {
					System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
				}
			}
			//InputStream documentoPDF = new ByteArrayInputStream(pdf);
			InputStream documentoPDF = pdfStream;
			//PDDocument 			
			pddocPDF = PDDocument.load(documentoPDF);

			if(pddocPDF!=null && pddocPDF.getNumberOfPages()>0){
				if(pagina<=pddocPDF.getNumberOfPages()){
					PDPage page;
					if (pagina==-1) {
						PDRectangle size = ((PDPage)pddocPDF.getDocumentCatalog().getAllPages().get(0)).getMediaBox();
						page = new PDPage();
						page.setMediaBox(size);
						pddocPDF.addPage(page);
					} else {
						page = (PDPage) pddocPDF.getDocumentCatalog().getAllPages().get(pagina - 1);
					}

					InputStream in = timbro;
					BufferedImage image = ImageIO.read(in);
					
					if(image==null){
						throw new BusinessLogicException("Errore lettura immagine del timbro");
					}
					
//					if(png){
//						InputStream is = new ByteArrayInputStream(timbro);
//						PDPixelMap img = new PDPixelMap(new PDStream(pddocPDF, is));						
//						PDPageContentStream contentStream = new PDPageContentStream(pddocPDF, page, true, false);
//						contentStream.drawXObject(img, x, y, img.getWidth(),img.getHeight());
//						contentStream.close();
//					} else {
					
					PDJpeg img = new PDJpeg(pddocPDF, image, 1); // senza compressione
					PDPageContentStream contentStream = new PDPageContentStream(pddocPDF, page, true, false);
					contentStream.drawXObject(img, x, y, img.getWidth(),img.getHeight());
					contentStream.close();
					
					
					
					//ByteArrayOutputStream os = new ByteArrayOutputStream();
					FileOutputStream fos = new FileOutputStream(f.getAbsolutePath());
					pddocPDF.save(fos);
//					pddocPDF.close();
					return f;

				}else
					throw new BusinessLogicException("Il documento contiene un numero di pagine inferiore a quelle richieste");
			}else
				throw new BusinessLogicException("Impossibile leggere il documento pdf");
		} catch (COSVisitorException e) {
			throw new BusinessLogicException(e);
		} catch (IOException e) {
			throw new BusinessLogicException(e);
		}
		catch (ProviderException e) {
			throw new BusinessLogicException(e);
		}
		finally{
			if(pddocPDF!=null){
				try {
					pddocPDF.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public byte[] getTimbro(byte[] data, ImageFormat imgFormat, int imgMaxH,
			int imgMaxW, int imgDPI, Map<String,String> params) throws BusinessLogicException {
		try {
			String defaulSystemAllowUnsafe = System
			.getProperty("sun.security.ssl.allowUnsafeRenegotiation");
			String configAllowUnsafe = this.provider.getConfiguration()
					.get("only_for_test_allowUnsafeRenegotiation").toString();
			if (configAllowUnsafe != null
					&& configAllowUnsafe.equalsIgnoreCase("true")) {
				if (defaulSystemAllowUnsafe == null
						|| !defaulSystemAllowUnsafe.equalsIgnoreCase("true")) {
					System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
				}
			}
			
			return this.provider.getTimbro(data, imgFormat, imgMaxH, imgMaxW, imgDPI, params);
		} catch (ProviderException e) {
			throw new BusinessLogicException(e);
		}
	}

}
