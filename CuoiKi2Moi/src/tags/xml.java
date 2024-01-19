package tags;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class xml {
	public static String createXMLMessage (String YourName, String MyName, int i) 
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	      DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	      Document doc = db.newDocument();
	      
	      // Tạo phần tử root <message>
	      Element root = doc.createElement("message");
	      doc.appendChild(root);
	      
	      // Tạo các phần tử con của <message>
	      Element sender = doc.createElement("sender");
	      sender.setTextContent(MyName);
	      root.appendChild(sender);
	      
	      Element receiver = doc.createElement("receiver");
	      receiver.setTextContent(YourName);
	      root.appendChild(receiver);
	      
	      Element index = doc.createElement("index");
	      index.setTextContent(String.valueOf(i));
	      root.appendChild(index);
	      
	      // Chuyển đổi tài liệu XML sang chuỗi XML
	      TransformerFactory tf = TransformerFactory.newInstance();
	      Transformer transformer = null;
		try {
			transformer = tf.newTransformer();
		} catch (TransformerConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	      StringWriter writer = new StringWriter();
	      try {
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      String xmlString = writer.getBuffer().toString();
	      return xmlString;
	}
	
	public static String parseXML(String xml)
	{
	      
	      // Tạo một tài liệu XML mới từ chuỗi XML
	      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	      DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      InputSource is = new InputSource();
	      is.setCharacterStream(new StringReader(xml));
	      Document doc = null;
		try {
			doc = db.parse(is);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	      // Lấy các phần tử từ tài liệu XML
	      Element root = doc.getDocumentElement();
	      String sender = root.getElementsByTagName("sender").item(0).getTextContent();
	      String receiver = root.getElementsByTagName("receiver").item(0).getTextContent();
	      int index = Integer.parseInt(root.getElementsByTagName("index").item(0).getTextContent());
	      return receiver + sender + tags.POSITION_MARKED_TAG + index  ;
	}
	
	public static void main(String[] args) throws ParserConfigurationException
	{
		System.out.println(xml.createXMLMessage("Phuc", "Tue", 100));
//		System.out.println(xml.parseXML(xml.createXMLMessage("Phuc", "Tue", 100)));
		
	}
}
