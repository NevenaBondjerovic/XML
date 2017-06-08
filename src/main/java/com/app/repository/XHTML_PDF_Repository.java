package com.app.repository;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.TransformerFactoryImpl;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.stereotype.Repository;

import com.app.config.Config;
import com.app.model.Act;
import com.app.model.Amendment;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;

@Repository
public class XHTML_PDF_Repository {

	private DatabaseClient client;
	
	public boolean generateXml(Object obj, String filePath){
		client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.database,
				Config.user, Config.password, Config.authType);

		if(obj instanceof Amendment && obj!=null){
			Amendment am=(Amendment)obj;
			try {
				// Definiše se JAXB kontekst (putanja do paketa sa JAXB bean-ovima)
				JAXBContext context = JAXBContext.newInstance(Amendment.class);

				// Marshaller je objekat zadužen za konverziju iz objektnog u XML model
				Marshaller marshaller = context.createMarshaller();
				
				// Podešavanje marshaller-a
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				
				// Umesto System.out-a, može se koristiti FileOutputStream
				marshaller.marshal(am, new File(filePath));

				client.release();
				return true;
				
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}else if(obj instanceof Act && obj!=null){
			Act act=(Act)obj;
			try {
				// Definiše se JAXB kontekst (putanja do paketa sa JAXB bean-ovima)
				JAXBContext context = JAXBContext.newInstance(Act.class);

				// Marshaller je objekat zadužen za konverziju iz objektnog u XML model
				Marshaller marshaller = context.createMarshaller();
				
				// Podešavanje marshaller-a
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				
				// Umesto System.out-a, može se koristiti FileOutputStream
				marshaller.marshal(act, new File(filePath));

				client.release();
				return true;
				
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
		client.release();
		return false;
	}
	
	public boolean generatePDF(String xslPath, String xmlPath){
//		String xmlPath="src/main/resources/data/data.xml";
//		Document d=buildDocument(xmlPath);
//		transform(d);

		FopFactory fopFactory;
		TransformerFactory transformerFactory;
		
		fopFactory = FopFactory.newInstance();
		transformerFactory = new TransformerFactoryImpl();

		try{
			File xsltFile = new File(xslPath);
			// Create transformation source
			StreamSource transformSource = new StreamSource(xsltFile);
			
			// Initialize the transformation subject
			StreamSource source = new StreamSource(new File(xmlPath));

			// Initialize user agent needed for the transformation
			FOUserAgent userAgent = fopFactory.newFOUserAgent();
			
			// Create the output stream to store the results
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();

			// Initialize the XSL-FO transformer object
			Transformer xslFoTransformer = transformerFactory.newTransformer(transformSource);
			
			// Construct FOP instance with desired output format
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, userAgent, outStream);

			// Resulting SAX events 
			Result res = new SAXResult(fop.getDefaultHandler());

			// Start XSLT transformation and FOP processing
			xslFoTransformer.transform(source, res);

			// Generate PDF file
			File pdfFile = new File("src/main/webapp/downloads/data.pdf");
			OutputStream out = new BufferedOutputStream(new FileOutputStream(pdfFile));
			out.write(outStream.toByteArray());

			out.close();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean generateXHTML(String xslPath, String xmlPath){
        try
        {
            TransformerFactory tFactory = TransformerFactory.newInstance();

            Source xslDoc = new StreamSource(xslPath);
            Source xmlDoc = new StreamSource(xmlPath);

            String outputFileName = "src/main/webapp/downloads/data.html";
            OutputStream htmlFile = new FileOutputStream(outputFileName);

            Transformer transformer = tFactory.newTransformer(xslDoc);
            transformer.transform(xmlDoc, new StreamResult(htmlFile));
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
	}
//	
//	private Document buildDocument(String xmlPath){
//		DocumentBuilderFactory documentFactory;
//		Document document=null;
//		
//		/* Inicijalizacija DOM fabrike */
//		documentFactory = DocumentBuilderFactory.newInstance();
//		documentFactory.setNamespaceAware(true);
//		documentFactory.setIgnoringComments(true);
//		documentFactory.setIgnoringElementContentWhitespace(true);
//		
//		
//		try {
//				DocumentBuilder builder = documentFactory.newDocumentBuilder();
//				document = builder.parse(new File(xmlPath)); 
//
//				System.out.println(document);
//	
//		}catch(IOException e){
//			e.printStackTrace();
//		}catch(ParserConfigurationException e){
//			e.printStackTrace();
//		} catch (org.xml.sax.SAXException e) {
//			e.printStackTrace();
//		}
//		
//		return document;
//	}
//	
//	private void transform(Node node){
//		TransformerFactory transformerFactory;
//		transformerFactory = TransformerFactory.newInstance();
//		try {
//			// Kreiranje instance objekta zaduzenog za serijalizaciju DOM modela
//			Transformer transformer = transformerFactory.newTransformer();
//
//			// Indentacija serijalizovanog izlaza
//			transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
//			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//
//			// Nad "source" objektom (DOM stablo) vrši se transformacija
//			DOMSource source = new DOMSource(node);
//
//			// Rezultujući stream (argument metode) 
//			StreamResult result = new StreamResult(System.out);
//
//			// Poziv metode koja vrši opisanu transformaciju
//			transformer.transform(source, result);
//
//		} catch (TransformerConfigurationException e) {
//			e.printStackTrace();
//		} catch (TransformerFactoryConfigurationError e) {
//			e.printStackTrace();
//		} catch (TransformerException e) {
//			e.printStackTrace();
//		}
//	}
	
}
