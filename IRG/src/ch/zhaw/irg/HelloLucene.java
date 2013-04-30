package ch.zhaw.irg;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.PagedBytes.Reader;
import org.apache.lucene.util.Version;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import com.sun.corba.se.impl.transport.ReaderThreadImpl;
import com.sun.org.apache.bcel.internal.generic.NEWARRAY;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class HelloLucene {
	private org.apache.lucene.document.Document d = null;
	private String returnString = null;
	private String filename = null;
	private String[] collection = { "collection/irg_collection_DE.xml",
			"collection/irg_collection_FI.xml",
			"collection/irg_collection_FR.xml",
			"collection/irg_collection_IT.xml",
			"collection/irg_collection_RU.xml",
			"collection/irg_collection_EN.xml" };

	// public static void main(String[] args, FileHandler collection)
	public HelloLucene(String[] args, String language, int queryId)
			throws IOException, ParseException {
		
		// 000. StopWords
		Collection<String> strList = new ArrayList<String>();
		strList.add("Foobar"); 
		
		CharArraySet stopwords = new CharArraySet(Version.LUCENE_40, strList , true);
	
		
		
		
		
		

		// 00. Get XML File from Language Key
		switch (language) {
		case "DE":
			this.filename = collection[0];
			break;
		case "FI":
			this.filename = collection[1];
			break;
		case "FR":
			this.filename = collection[2];
			break;
		case "IT":
			this.filename = collection[3];
			break;
		case "RU":
			this.filename = collection[4];
			break;
		case "EN":
			this.filename = collection[5];
			break;
		default:
			this.filename = collection[5];
			break;
		}

		// Create absolut path:
		// System.out.println("Realtive Path: " + filename);
		File absolut = new File(filename);
		filename = absolut.getAbsolutePath();
		// System.out.println("Absolut path: " + filename);
		

		// 0. Specify the analyzer for tokenizing text.
		// The same analyzer should be used for indexing and searching
 		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40, stopwords);
 		
// 		  StandardAnalyzer(Version matchVersion) 
//        Builds an analyzer with the default stop words (STOP_WORDS_SET). 
//        
//        StandardAnalyzer(Version matchVersion, CharArraySet stopWords) 
//        Builds an analyzer with the given stop words. 
//
//        StandardAnalyzer(Version matchVersion, Reader stopwords) 
//        Builds an analyzer with the stop words from the given reader. 

 		
 		
 		

		// 1. create the index
		Directory index = new RAMDirectory();

		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40,
				analyzer);

		// READ Collection
		try {

			// http://www.lucenetutorial.com/lucene-in-5-minutes.html
			File fXmlFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			// ROOT --> TREC
			// System.out.println("Root element :"
			// + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("DOC");

			// System.out.println(" Create Index Writer ");
			IndexWriter w = new IndexWriter(index, config);
			// System.out.println("Read File: " + filename);
			// System.out.println("Create Collection in Language: " + language);
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				// System.out.println("\nCurrent Element :" +
				// nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					// add to collection
					addDoc(w, eElement.getElementsByTagName("text").item(0)
							.getTextContent(),
							eElement.getElementsByTagName("recordId").item(0)
									.getTextContent());
					// System.out.println("RecordId: " +
					// eElement.getElementsByTagName("recordId").item(0)
					// .getTextContent());
				}
			}
			// close doc
			w.close();

		} catch (Exception e) {
			// TODO: handle exception
		}

		// 2. query
		// query
		String querystr = null;
		if (args.length > 0) {
			querystr = args[0];
		}

		// the "title" arg specifies the default field to use
		// when no field is explicitly specified in the query.
		Query q = new QueryParser(Version.LUCENE_40, "text", analyzer)
				.parse(querystr);

		// 3. search
		int hitsPerPage = 10;
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(
				hitsPerPage, true);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		// 4. display results
		// System.out.println("Found " + hits.length + " hits.");

		System.out
				.println("-----------------------------------------------------------------");

		if (hits.length == 0) {
			System.out.println("No documents found for query: " + queryId);
		}

		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			d = searcher.doc(docId);

			// Return String als Konsolen output
			System.out.println(queryId + "\t" + "Q0" + "\t" + d.get("recordId")
					+ "\t" + i + "\t" + hits[i].score + "\t"
					+ "SCALCSAN&MAMUTNAD");
		}
		// reader can only be closed when there
		// is no need to access the documents any more.
		reader.close();
	}

	private static void addDoc(IndexWriter w, String text, String recordId)
			throws IOException {
		Document doc = new Document();
		doc.add(new TextField("text", text, Field.Store.YES));

		// use a string field for isbn because we don't want it tokenized
		doc.add(new StringField("recordId", recordId, Field.Store.YES));
		w.addDocument(doc);
	}

	public String getReturnString() {
		return this.returnString;
	}
}
