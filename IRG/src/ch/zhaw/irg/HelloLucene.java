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
import org.apache.lucene.util.Version;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class HelloLucene {
	private org.apache.lucene.document.Document d = null;
	// private String returnString = null;
	private String filename = null;
	private String filenameStopWords = null;
	private String[] collection = { "collection/irg_collection_DE.xml",
			"collection/irg_collection_FI.xml",
			"collection/irg_collection_FR.xml",
			"collection/irg_collection_IT.xml",
			"collection/irg_collection_RU.xml",
			"collection/irg_collection_EN.xml" };
	private String[] stopWords = { "stopwords/stopwords_GermanStopwords.txt",
			"stopwords/stopwords_FinnishStopwords.txt",
			"stopwords/stopwords_FrenchStopwords.txt",
			"stopwords/stopwords_ItalianStopwords.txt",
			"stopwords/stopwords_RussianStopwords.txt", 
			"stopwords/stopwords_EnglishStopwords.txt" };

	private Collection<String> outputString = null;

	// public static void main(String[] args, FileHandler collection)
	public HelloLucene(String[] args, String language, int queryId,
			boolean boolStopWords, Boolean isLeft) throws IOException,
			ParseException {

		// local Attribute
		StandardAnalyzer analyzer = null;

		// 00. Get XML File from Language Key
		switch (language) {
		case "DE":
			this.filename = collection[0];
			this.filenameStopWords = stopWords[0];
			break;
		case "FI":
			this.filename = collection[1];
			this.filenameStopWords = stopWords[1];
			break;
		case "FR":
			this.filename = collection[2];
			this.filenameStopWords = stopWords[2];
			break;
		case "IT":
			this.filename = collection[3];
			this.filenameStopWords = stopWords[3];
			break;
		case "RU":
			this.filename = collection[4];
			this.filenameStopWords = stopWords[4];
			break;
		case "EN":
			this.filename = collection[5];
			this.filenameStopWords = stopWords[5];
			break;
		default:
			this.filename = collection[5];
			this.filenameStopWords = stopWords[5];
			break;
		}

		// Create absolut path:
		File absolut = new File(filename);
		filename = absolut.getAbsolutePath();

		System.out
		.println("-----------------------------------------------------------------");
		
		if (boolStopWords == true) {
			// Create absolut path Stop Words:
			System.out.println("Use stopwords in Language " + language);
			File absolutStopWords = new File(filenameStopWords);
			filenameStopWords = absolutStopWords.getAbsolutePath();

			// 000. StopWords
			// StandardAnalyzer(Version matchVersion)
			// Builds an analyzer with the default stop words (STOP_WORDS_SET).
			//
			// StandardAnalyzer(Version matchVersion, CharArraySet stopWords)
			// Builds an analyzer with the given stop words.
			//
			// StandardAnalyzer(Version matchVersion, Reader stopwords)
			// Builds an analyzer with the stop words from the given reader.
			Collection<String> strList = new ArrayList<String>();
			BufferedReader br = null;
			try {
				String sCurrentLine;
				br = new BufferedReader(new FileReader(filenameStopWords));
				while ((sCurrentLine = br.readLine()) != null) {
					strList.add(sCurrentLine);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)
						br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

			CharArraySet stopwords = new CharArraySet(Version.LUCENE_40,
					strList, true);

			analyzer = new StandardAnalyzer(Version.LUCENE_40, stopwords);
		} else {
			analyzer = new StandardAnalyzer(Version.LUCENE_40);
		}

		// 1. create the index
		Directory index = new RAMDirectory();

		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40,
				analyzer);

		// READ Collection
		try {
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
			NodeList nList = doc.getElementsByTagName("DOC");

			IndexWriter w = new IndexWriter(index, config);
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					// add to collection
					addDoc(w, eElement.getElementsByTagName("text").item(0)
							.getTextContent(),
							eElement.getElementsByTagName("recordId").item(0)
									.getTextContent());
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
		int hitsPerPage = 1000;
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(
				hitsPerPage, true);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		// 4. display results
		// System.out.println("Found " + hits.length + " hits.");
		if (hits.length == 0) {
			System.out.println("No documents found for query: " + queryId);
		}

		outputString = new ArrayList<String>();

		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			d = searcher.doc(docId);

			// Return String als Konsolen output
			System.out.println(queryId + "\t" + "Q0" + "\t" + d.get("recordId")
					+ "\t" + i + "\t" + hits[i].score + "\t"
					+ "SCALCSAN&MAMUTNAD");

			// output File
			outputString.add(queryId + " " + "Q0" + " " + d.get("recordId")
					+ " " + i + " " + hits[i].score + " "
					+ "SCALCSAN&MAMUTNAD\n");
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

	public Collection<String> getOutputString() {
		return outputString;
	}

	public void setOutputString(Collection<String> outputString) {
		this.outputString = outputString;
	}
}
