package it.kdm.sign.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.xml.utils.XMLReaderManager;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class XMLUtils {
	
	public static boolean isParsable( FileInputStream fis) {
		XMLReader reader = null;
		InputSource is = new InputSource(fis);
		try {
			reader = XMLReaderManager.getInstance().getXMLReader();
			reader.parse(is);
		} catch (IOException | SAXException e) {
			return false;
		}
		return true;
	}
		
	public static void main(String[] args) throws SAXException, IOException {
		File file = new File("C:\\Users\\annibale\\Desktop\\KDM\\test\\inputDir\\sample2.xml");
		FileInputStream fis = new FileInputStream(file);
		boolean isParsable = XMLUtils.isParsable(fis);
		System.out.println( isParsable );
	}
}

