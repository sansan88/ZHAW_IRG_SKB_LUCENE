package ch.zhaw.irg;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FileHandler {

	private static final Boolean TRUE = null;
	public Boolean isQuery;
	public Boolean isCollection;
	public String language;

	public String[] ids;

	private File file; // parsed XML File
	private String[] collection = { "\\collection\\irg_collection.xml" };
	private String[] query = { "\\query\\irg_queries_DE.xml",
			"\\query\\irg_queries_FI.xml", "\\query\\irg_queries_FR.xml",
			"\\query\\irg_queries_IT.xml", "\\query\\irg_queries_RU.xml",
			"\\query\\irg_queries_EN.xml" };

	public FileHandler(Boolean isQuery, Boolean isCollection, String language) {
		this.isQuery = isQuery;
		this.isCollection = isCollection;
		this.language = language;

		setIds();
	}

	public void parseXML(String filename) {
		// READ FILE
		System.out
				.println("--------------------------------------------------------------------------\n"
						+ "parseXML File\n"
						+ "--------------------------------------------------------------------------");
		try {
			this.file = new File(
					"C:\\Users\\sandro\\Dropbox\\ZHAW\\63_IRG\\SKB\\irg_queries.xml");

			System.out.println("Read File: \n" + this.file);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(file);

			doc.getDocumentElement().normalize();

			// ROOT --> TREC
			System.out.println("Root element :"
					+ doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("DOC");

			int i = 0;

			this.ids = new String[nList.getLength()];

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;

					System.out.println("recordId : "
							+ eElement.getElementsByTagName("recordId").item(0)
									.getTextContent()
							+ "\n"
							+ eElement.getElementsByTagName("text").item(0)
									.getTextContent());

					this.ids[i] = eElement.getElementsByTagName("recordId")
							.item(0).getTextContent();

					i++;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void setIds() {
		if (this.isQuery == TRUE) {
			if (this.language == "DE") {
				parseXML(query[0]);
			}
			if (this.language == "FI") {
				parseXML(query[1]);
			}
			if (this.language == "FR") {
				parseXML(query[2]);
			}
			if (this.language == "IT") {
				parseXML(query[3]);
			}
			if (this.language == "RU") {
				parseXML(query[4]);
			}
			if (this.language == "EN") {
				parseXML(query[5]);
			}

		}// EndOfQuery

		if (this.isCollection == TRUE) {
			if (this.language == "DE") {
				parseXML(collection[0]);
			}
			if (this.language == "FI") {
				parseXML(collection[1]);
			}
			if (this.language == "FR") {
				parseXML(collection[2]);
			}
			if (this.language == "IT") {
				parseXML(collection[3]);
			}
			if (this.language == "RU") {
				parseXML(collection[4]);
			}
			if (this.language == "EN") {
				parseXML("C:\\Users\\sandro\\Dropbox\\ZHAW\\63_IRG\\SKB\\irg_collection.xml"); // collection[5]
			}
		} // endofcollection

	}

}
