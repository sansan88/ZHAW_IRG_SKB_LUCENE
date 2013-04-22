package ch.zhaw.irg;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FileHandler {

	public Boolean isQuery;
	public Boolean isCollection;
	public String language;

	public String[] docIds;

	private File file; // parsed XML File
	public String filename;
	private String[] collection = { "../collection/irg_collection_DE.xml",
			"../collection/irg_collection_FI.xml",
			"../collection/irg_collection_FR.xml",
			"../collection/irg_collection_IT.xml",
			"../collection/irg_collection_RU.xml",
			"../collection/irg_collection_EN.xml" };
	private String[] query = { "../query/irg_queries_DE.xml",
			"../query/irg_queries_FI.xml", "../query/irg_queries_FR.xml",
			"../query/irg_queries_IT.xml", "../query/irg_queries_RU.xml",
			"../query/irg_queries_EN.xml" };

	// Constructor
	public FileHandler(Boolean isQuery, Boolean isCollection, String language) {
		this.isQuery = isQuery;
		this.isCollection = isCollection;
		this.language = language;

		// set filename
		if (this.isQuery == true && this.isCollection == false) {
			if (this.language == "DE") {
				this.filename = query[0];
			}
			if (this.language == "FI") {
				this.filename = query[1];
			}
			if (this.language == "FR") {
				this.filename = query[2];
			}
			if (this.language == "IT") {
				this.filename = query[3];
			}
			if (this.language == "RU") {
				this.filename = query[4];
			}
			if (this.language == "EN") {
				this.filename = query[5];
			}

		}// EndOfQuery

		if (this.isCollection == true && this.isQuery == false ) {
			if (this.language == "DE") {
				this.filename = collection[0];
			}
			if (this.language == "FI") {
				this.filename = collection[1];
			}
			if (this.language == "FR") {
				this.filename = collection[2];
			}
			if (this.language == "IT") {
				this.filename = collection[3];
			}
			if (this.language == "RU") {
				this.filename = collection[4];
			}
			if (this.language == "EN") {
				this.filename = collection[5];
			}
		} // endofcollection

	}

	public String[] setIds() {
		String[] ids = null;
		
		
		// READ FILE
		System.out
				.println("--------------------------------------------------------------------------\n"
						+ "parseXML File\n"
						+ "--------------------------------------------------------------------------");
		try {
			this.file = new File(this.filename);

			if (this.isQuery == true && this.isCollection == false) {
				System.out.println("Processing Query");
			}
			if (this.isCollection == true && this.isQuery == false) {
				System.out.println("Processing Collection");
			}
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

			ids = new String[nList.getLength()];

			// Build ID Array
			System.out.println("Build ID Array");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;

					// System.out.println("recordId : "
					// + eElement.getElementsByTagName("recordId").item(0)
					// .getTextContent()
					// + "\n"
					// + eElement.getElementsByTagName("text").item(0)
					// .getTextContent());

					ids[i] = eElement.getElementsByTagName("recordId").item(0)
							.getTextContent();

					i++;
				} // endif
			} // endfor

		} catch (Exception e) {
			// TODO: handle exception
		}
		return ids;

	}

}
