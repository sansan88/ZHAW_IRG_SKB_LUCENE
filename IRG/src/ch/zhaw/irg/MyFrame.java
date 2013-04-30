package ch.zhaw.irg;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import ch.zhaw.irg.HelloLucene;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

public class MyFrame extends Frame implements WindowListener, ActionListener {
	private Color bg;

	// Gui Elemente
	private Panel panelTop;
	private Panel panelCenter;

	private Button btnSearch = null;
	private Button btnExportLeft = null;
	private Button btnExportRight = null;

	private TextArea txtAreaLeft;
	private TextArea txtAreaRight;

	private Checkbox chbxStopLeft;
	private Checkbox chbxStopRight;

	private Checkbox chbxPortStemLeft;
	private Checkbox chbxPortStemRight;

	private Choice collectionLeft;
	private Choice collectionRight;

	private StringTokenizer st = null;
	private String[] queryDoc = null;
	private String filename = null;
	private String[] queryArray = { "query/irg_queries_DE.xml",
			"query/irg_queries_FI.xml", "query/irg_queries_FR.xml",
			"query/irg_queries_IT.xml", "query/irg_queries_RU.xml",
			"query/irg_queries_EN.xml" };

	// private int stemmer; //options for checkboxes stemmer
	// private int stopword; //options for checkboxes stopwords
	private int options = 0;
	private File absolut = null;

	private HelloLucene luceneLeft = null;
	private HelloLucene luceneRight = null;

	private Collection<String> outputFile = null;

	public static void main(String args[]) {
		MyFrame myFrame = new MyFrame();
		myFrame.setSize(800, 800);
		myFrame.setVisible(true);
		myFrame.setLayout(new BorderLayout());
	}

	public MyFrame() {
		this.addWindowListener(this);

		bg = new Color(255, 255, 200);
		setBackground(bg);

		setTitle("IRG Search by scalcsan & mamutnad");

		// GUI Komponenten erzeugen

		// TOP Panel mit Query Bereich
		panelTop = new Panel();
		panelTop.setLayout(new GridLayout(1, 1));

		btnSearch = new Button("GoGoGo");
		panelTop.add(btnSearch);

		add(panelTop, BorderLayout.NORTH);

		// CENTER
		panelCenter = new Panel();
		panelCenter.setLayout(new GridLayout(5, 2));

		// Dropdown Left
		collectionLeft = new Choice();
		collectionLeft.add("DE");
		collectionLeft.add("RU");
		collectionLeft.add("FI");
		collectionLeft.add("FR");
		collectionLeft.add("EN");
		collectionLeft.add("IT");
		panelCenter.add(collectionLeft);

		// Dropdown Right
		collectionRight = new Choice();
		collectionRight.add("DE");
		collectionRight.add("RU");
		collectionRight.add("FI");
		collectionRight.add("FR");
		collectionRight.add("EN");
		collectionRight.add("IT");
		panelCenter.add(collectionRight);

		// checkbox Stopwords
		chbxStopLeft = new Checkbox("Enable Stopwords", false);
		panelCenter.add(chbxStopLeft);

		chbxStopRight = new Checkbox("Enable Stopwords", false);
		panelCenter.add(chbxStopRight);

		// checkbox porterstem
		chbxPortStemLeft = new Checkbox("Use Porter Stemmer", false);
		panelCenter.add(chbxPortStemLeft);

		chbxPortStemRight = new Checkbox("Use Porter Stemmer", false);
		panelCenter.add(chbxPortStemRight);

		// export buttons
		btnExportLeft = new Button("Export to File, Left");
		panelCenter.add(btnExportLeft);

		btnExportRight = new Button("Export to File, Right");
		panelCenter.add(btnExportRight);

		txtAreaLeft = new TextArea("Magic happens here");
		panelCenter.add(txtAreaLeft);
		txtAreaRight = new TextArea("or it could happen here");
		panelCenter.add(txtAreaRight);

		add(panelCenter, BorderLayout.CENTER);

		// Beim Listener registrieren
		btnExportLeft.addActionListener(this);
		btnExportRight.addActionListener(this);
		btnSearch.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == btnExportLeft) {
			System.out.println("Export Left");
			System.out.println(txtAreaLeft.getText());
			// !!!!!TODO!!!!!!!
			// Hier einen FileWriter schreiben, der den Inhalt des TextArea in
			// ein Result.trec File Format schreibt
			try {
				File temp = new File("results/resultLeft.txt");
				String absolutPath = new String(temp.getAbsolutePath());
				File file = new File(absolutPath);

				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(txtAreaLeft.getText());
				bw.close();
				System.out.println("Done");

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		if (e.getSource() == btnExportRight) {
			System.out.println("Export Right");
			System.out.println(txtAreaRight.getText());
			// !!!!!TODO!!!!!!!
			// Hier einen FileWriter schreiben, der den Inhalt des TextArea in
			// ein Result.trec File Format schreibt
			try {
				File temp = new File("results/resultRight.txt");
				String absolutPath = new String(temp.getAbsolutePath());
				File file = new File(absolutPath);

				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(txtAreaRight.getText());
				bw.close();
				System.out.println("Done");

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		// MAGIC HAPPENS HERE!!
		if (e.getSource() == btnSearch) {
			System.out
					.println("MAGIC HAPPENS HERE!! GOGOGO Lucene\n Start Time: ");
			java.util.Date date = new java.util.Date();
			System.out.println(new Timestamp(date.getTime()));

			// ------------------------------------
			// Process Left Side
			// ------------------------------------
			// Get Options for left Side
			// Stem Stop
			// 0 0 1
			// 1 0 2
			// 0 1 3
			// 1 1 4
			if (chbxStopLeft.getState() == false
					&& chbxPortStemLeft.getState() == false) {
				options = 1;
			} else if (chbxStopLeft.getState() == true
					&& chbxPortStemLeft.getState() == false) {
				options = 2;
			} else if (chbxStopLeft.getState() == false
					&& chbxPortStemLeft.getState() == true) {
				options = 3;
			} else if (chbxStopLeft.getState() == true
					&& chbxPortStemLeft.getState() == true) {
				options = 4;
			}

			// Get XML File from Language Key
			switch (collectionLeft.getSelectedItem()) {
			case "DE":
				filename = queryArray[0];
				break;
			case "FI":
				filename = queryArray[1];
				break;
			case "FR":
				filename = queryArray[2];
				break;
			case "IT":
				filename = queryArray[3];
				break;
			case "RU":
				filename = queryArray[4];
				break;
			case "EN":
				filename = queryArray[5];
				break;
			default:
				filename = queryArray[5];
				break;
			}

			// Create absolut path:
			// System.out.println("Realtive Path: " + filename);
			absolut = new File(filename);
			filename = absolut.getAbsolutePath();
			// System.out.println("Absolut path: " + filename);

			// Parse XML File
			try {

				File file = new File(filename);

				DocumentBuilderFactory dbFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

				Document doc = dBuilder.parse(file);

				doc.getDocumentElement().normalize();

				// System.out.println("Root element :"
				// + doc.getDocumentElement().getNodeName());

				NodeList nList = doc.getElementsByTagName("DOC");

				outputFile = new ArrayList<String>();

				for (int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);

					// System.out.println("\nCurrent Element :"
					// + nNode.getNodeName());
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;

						// create tokenizer
						st = new StringTokenizer(eElement
								.getElementsByTagName("text").item(0)
								.getTextContent());

						// System.out.println("Create queryDoc Array[].");
						queryDoc = new String[st.countTokens()];
						for (int i = 0; st.hasMoreTokens(); i++) {

							// Stop Words --> wird in collection gemacht.

							// Porter Stemmer
							// PorterStemmer stemmer = new PorterStemmer();
							// return stemmer.stem(term);

							queryDoc[i] = st.nextToken();
						}
						// erstelle Lucene mit Query[], ausgewählter Sprache und
						// der ID von der Query
						setLuceneLeft(new HelloLucene(queryDoc,
								collectionLeft.getSelectedItem(),
								Integer.valueOf(eElement
										.getElementsByTagName("recordId")
										.item(0).getTextContent()), options,
								true));

						for (Iterator iterator = getLuceneLeft()
								.getOutputString().iterator(); iterator
								.hasNext();) {
							outputFile.add(iterator.next().toString());
						}
					}// ifend
				}// for

				// Write Output FIle and txtAreaLEft
				File temp = new File("results/resultLeft.txt");
				String absolutPath = new String(temp.getAbsolutePath());
				File outputFileLeft = new File(absolutPath);

				// if file doesnt exists, then create it
				outputFileLeft.delete();
				if (!outputFileLeft.exists()) {
					outputFileLeft.createNewFile();
				}
				FileWriter fw = new FileWriter(outputFileLeft.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				System.out.println("Write File: " + outputFileLeft);
				for (Iterator iterator = outputFile.iterator(); iterator
						.hasNext();) {
					bw.write(iterator.next().toString());
				}
				bw.close();

			} catch (Exception exception) {
				// TODO: handle exception
			}
			// ------------------------------------
			// Process Right Side
			// ------------------------------------
			java.util.Date date1 = new java.util.Date();
			System.out.println(new Timestamp(date1.getTime()));
			if (chbxStopRight.getState() == false
					&& chbxPortStemRight.getState() == false) {
				options = 1;
			} else if (chbxStopRight.getState() == true
					&& chbxPortStemRight.getState() == false) {
				options = 2;
			} else if (chbxStopRight.getState() == false
					&& chbxPortStemRight.getState() == true) {
				options = 3;
			} else if (chbxStopRight.getState() == true
					&& chbxPortStemRight.getState() == true) {
				options = 4;
			}

			// Get XML File from Language Key
			switch (collectionRight.getSelectedItem()) {
			case "DE":
				filename = queryArray[0];
				break;
			case "FI":
				filename = queryArray[1];
				break;
			case "FR":
				filename = queryArray[2];
				break;
			case "IT":
				filename = queryArray[3];
				break;
			case "RU":
				filename = queryArray[4];
				break;
			case "EN":
				filename = queryArray[5];
				break;
			default:
				filename = queryArray[5];
				break;
			}

			// Create absolut path:
			// System.out.println("Realtive Path: " + filename);
			absolut = new File(filename);
			filename = absolut.getAbsolutePath();
			// System.out.println("Absolut path: " + filename);

			// Parse XML File
			try {

				File file = new File(filename);

				DocumentBuilderFactory dbFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

				Document doc = dBuilder.parse(file);

				doc.getDocumentElement().normalize();

				// System.out.println("Root element :"
				// + doc.getDocumentElement().getNodeName());

				NodeList nList = doc.getElementsByTagName("DOC");
				outputFile = new ArrayList<String>();
				for (int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);

					// System.out.println("\nCurrent Element :"
					// + nNode.getNodeName());
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;

						// create tokenizer
						st = new StringTokenizer(eElement
								.getElementsByTagName("text").item(0)
								.getTextContent());

						// System.out.println("Create queryDoc Array[].");
						queryDoc = new String[st.countTokens()];
						for (int i = 0; st.hasMoreTokens(); i++) {

							// Stop Words --> wird in collection gemacht.

							// Porter Stemmer
							// PorterStemmer stemmer = new PorterStemmer();
							// return stemmer.stem(term);

							queryDoc[i] = st.nextToken();
						}
						// erstelle Lucene mit Query[], ausgewählter Sprache und
						// der ID von der Query
						setLuceneRight(new HelloLucene(queryDoc,
								collectionRight.getSelectedItem(),
								Integer.valueOf(eElement
										.getElementsByTagName("recordId")
										.item(0).getTextContent()), options,
								false));

						for (Iterator iterator = getLuceneRight()
								.getOutputString().iterator(); iterator
								.hasNext();) {
							outputFile.add(iterator.next().toString());
						}
					}// ifend
				}// for

				// Write Output FIle and txtAreaRight
				File tempR = new File("results/resultRight.txt");
				String absolutPath = new String(tempR.getAbsolutePath());
				File outputFileRight = new File(absolutPath);

				// if file doesnt exists, then create it
				outputFileRight.delete();
				if (!outputFileRight.exists()) {
					outputFileRight.createNewFile();
				}

				FileWriter fw = new FileWriter(
						outputFileRight.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				System.out.println("Write File: " + outputFileRight);
				for (Iterator iterator = outputFile.iterator(); iterator
						.hasNext();) {
					bw.write(iterator.next().toString());
				}
				bw.close();

			} catch (Exception exception) {
				// TODO: handle exception
			}
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	public HelloLucene getLuceneLeft() {
		return luceneLeft;
	}

	public void setLuceneLeft(HelloLucene luceneLeft) {
		this.luceneLeft = luceneLeft;
	}

	public HelloLucene getLuceneRight() {
		return luceneRight;
	}

	public void setLuceneRight(HelloLucene luceneRight) {
		this.luceneRight = luceneRight;
	}
}
