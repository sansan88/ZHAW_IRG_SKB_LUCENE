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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

public class MyFrame extends Frame implements WindowListener, ActionListener {
	private Color bg;

	// Gui Elemente
	private Panel panelTop;
	private Panel panelCenter;

	private Button btnSearch = null;
	private Button btnSearchAll = null;
	// private Button btnExportLeft = null;
	// private Button btnExportRight = null;

	private TextArea txtAreaLeft;
	private TextArea txtAreaRight;

	private Checkbox chbxStopLeft;
	private Checkbox chbxStopRight;

	// private Checkbox chbxPortStemLeft;
	// private Checkbox chbxPortStemRight;

	private Choice collectionLeft;
	private Choice collectionRight;

	private StringTokenizer st = null;
	private String[] queryDoc = null;
	private String filename = null;
	private String[] queryArray = { "query/irg_queries_DE.xml",
			"query/irg_queries_FI.xml", "query/irg_queries_FR.xml",
			"query/irg_queries_IT.xml", "query/irg_queries_RU.xml",
			"query/irg_queries_EN.xml" };

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
		panelTop.setSize(50, 200);
		panelTop.setLayout(new GridLayout(1, 2)); // Reihen / Spalten

		btnSearch = new Button("GoGoGo Left/Right");
		panelTop.add(btnSearch);

		btnSearchAll = new Button("Search All");
		panelTop.add(btnSearchAll);

		add(panelTop, BorderLayout.NORTH);

		// CENTER
		panelCenter = new Panel();
		panelCenter.setLayout(new GridLayout(3, 2));
		panelCenter.setSize(600, 1000);

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

		txtAreaLeft = new TextArea("Magic happens here");
		panelCenter.add(txtAreaLeft);
		txtAreaRight = new TextArea("or it could happen here");
		panelCenter.add(txtAreaRight);

		add(panelCenter, BorderLayout.CENTER);

		// Beim Listener registrieren
		btnSearch.addActionListener(this);
		btnSearchAll.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		// MAGIC HAPPENS HERE!!
		if (e.getSource() == btnSearch) {
			System.out
					.println("MAGIC HAPPENS HERE!! GOGOGO Lucene\n Start Time: ");
			java.util.Date date = new java.util.Date();
			System.out.println(new Timestamp(date.getTime()));

			// ------------------------------------
			// Process Left Side
			// ------------------------------------
			// Get XML File from Language Key
			filename = getQueryFileFromName(collectionLeft.getSelectedItem());

			try {
				NodeList nList = getNodeListFromFilename(filename);// Parse XML
																	// File
				outputFile = new ArrayList<String>();

				for (int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;

						setLuceneLeft(new HelloLucene(getQueryDoc(eElement),
								collectionLeft.getSelectedItem(),
								Integer.valueOf(eElement
										.getElementsByTagName("recordId")
										.item(0).getTextContent()),
								chbxStopLeft.getState(), true));

						for (Iterator iterator = getLuceneLeft()
								.getOutputString().iterator(); iterator
								.hasNext();) {
							outputFile.add(iterator.next().toString());
						}
					}// ifend
				}// for

				writeOutPutFile(chbxStopLeft.getState(), collectionLeft,
						outputFile);
				txtAreaLeft.setText(outputFile.toString());
				// ------------------------------------
				// Process Right Side
				// ------------------------------------
				java.util.Date date1 = new java.util.Date();
				System.out.println(new Timestamp(date1.getTime()));

				// Get XML File from Language Key
				filename = getQueryFileFromName(collectionRight
						.getSelectedItem());

				NodeList nListRight = getNodeListFromFilename(filename);// Parse
																		// XML
																		// File
				outputFile = new ArrayList<String>();

				for (int temp = 0; temp < nListRight.getLength(); temp++) {
					Node nNode = nListRight.item(temp);

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;

						setLuceneRight(new HelloLucene(getQueryDoc(eElement),
								collectionRight.getSelectedItem(),
								Integer.valueOf(eElement
										.getElementsByTagName("recordId")
										.item(0).getTextContent()),
								chbxStopRight.getState(), false));

						for (Iterator iterator = getLuceneRight()
								.getOutputString().iterator(); iterator
								.hasNext();) {
							outputFile.add(iterator.next().toString());
						}
					}// ifend
				}// for

				writeOutPutFile(chbxStopRight.getState(), collectionRight,
						outputFile);
				txtAreaRight.setText(outputFile.toString());

			} catch (Exception exception) {
				// TODO: handle exception
			}
		} // ENDIF MAGIC HAPPENS HERE!!

		if (e.getSource() == "btnSearchAll") {
			HashMap<Integer, Integer> rangliste = new HashMap<>();
			//Read Files DE
			BufferedReader br = null;
			try {
				String sCurrentLine;
				br = new BufferedReader(new FileReader("C:\\Users\\sandro\\Documents\\GitHub\\ZHAW_IRG_SKB_LUCENE\\IRG\\results\\DE.txt"));
				while ((sCurrentLine = br.readLine()) != null) {
					StringTokenizer st = new StringTokenizer(sCurrentLine);
					Integer queryId = new Integer(Integer.getInteger(st.nextToken()));
					Integer durchgang = new Integer(Integer.getInteger(st.nextToken()));
					Integer document = new Integer(Integer.getInteger(st.nextToken()));
					
					String keyString = new String(String.valueOf(queryId)+String.valueOf(document));
					Integer keyInteger = new Integer(Integer.valueOf(keyString));
					
					Integer rang = new Integer(Integer.valueOf(st.nextToken()));
					rang ++;
					
					rangliste.put(keyInteger, rang);
				}
	 
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				try {
					if (br != null)br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} 
		}
	}

	// METHODEN
	public String[] getQueryDoc(Element el) {
		st = new StringTokenizer(el // create tokenizer from query
				.getElementsByTagName("text").item(0).getTextContent());

		queryDoc = new String[st.countTokens()];
		for (int i = 0; st.hasMoreTokens(); i++) {
			queryDoc[i] = st.nextToken();
		}
		return queryDoc;
	}

	public String getQueryFileFromName(String language) {
		String filename = null;

		switch (language) {
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
		absolut = new File(filename);
		filename = absolut.getAbsolutePath();
		return filename;
	}

	public void writeOutPutFile(boolean checkbox, Choice choice,
			Collection<String> outFile) {
		// Write Output FIle and txtAreaRight
		String filename = null;
		if (checkbox == true) {
			filename = new String("results/"
					+ choice.getSelectedItem().toString() + "_Stop" + ".txt");
		} else {
			filename = new String("results/"
					+ choice.getSelectedItem().toString() + ".txt");
		}
		File temp = null;
		temp = new File(filename);
		String absolutPath = null;
		absolutPath = new String(temp.getAbsolutePath());
		File outputFileRight = null;
		outputFileRight = new File(absolutPath);

		try {
			// if file doesnt exists, then create it
			if (!outputFileRight.exists()) {
				outputFileRight.createNewFile();
			}
			System.out.println("Write File: " + outputFileRight);
			FileWriter fw = new FileWriter(outputFileRight.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for (Iterator iterator = outFile.iterator(); iterator.hasNext();) {
				bw.write(iterator.next().toString());
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public NodeList getNodeListFromFilename(String filename) {
		NodeList nList = null;
		try {
			File file = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();
			nList = doc.getElementsByTagName("DOC");

		} catch (Exception e) {
			// TODO: handle exception
		}
		return nList;
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
