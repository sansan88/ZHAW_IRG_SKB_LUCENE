package ch.zhaw.irg;

import java.awt.BorderLayout;
import java.awt.Button;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

	private Choice collectionLeft;
	private Choice collectionRight;

	// XML und Andere Attribute
	private HelloLucene luceneLeft = null;
	private StringTokenizer st = null;
	private String[] queryDoc = null;
	private String filename = null;
	private File file; // parsed XML File
	private String[] queryArray = { "../query/irg_queries_DE.xml",
			"../query/irg_queries_FI.xml", "../query/irg_queries_FR.xml",
			"../query/irg_queries_IT.xml", "../query/irg_queries_RU.xml",
			"../query/irg_queries_EN.xml" };

	public static void main(String args[]) {
		MyFrame myFrame = new MyFrame();
		myFrame.setSize(600, 600);
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
		panelCenter.setLayout(new GridLayout(3, 2));

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
				File file = new File("../results/resultLeft.txt");
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

		}

		// MAGIC HAPPENS HERE!!
		if (e.getSource() == btnSearch) {
			System.out.println("MAGIC HAPPENS HERE!! GOGOGO Lucene");

			// Process Left Side
			System.out.println("File Selected Left: "
					+ collectionLeft.getSelectedItem());

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

			// Parse XML File
			try {

				File file = new File(filename);
				System.out.println("Read File: \n" + file);

				DocumentBuilderFactory dbFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				
				Document doc = dBuilder.parse(file);
				System.out.println("test");
				
				doc.getDocumentElement().normalize();

				System.out.println("Root element :"
						+ doc.getDocumentElement().getNodeName());

				NodeList nList = doc.getElementsByTagName("DOC");

				for (int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);

					System.out.println("\nCurrent Element :"
							+ nNode.getNodeName());
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;

						// Create Lucene Instance with String
						System.out
								.println("Create Lucene Instance with String");
						System.out.println(eElement
								.getElementsByTagName("text").item(0)
								.getTextContent());

						// create tokenizer
						st = new StringTokenizer(eElement
								.getElementsByTagName("text").item(0)
								.getTextContent());

						System.out.println("create query string array.");
						queryDoc = new String[st.countTokens()];
						for (int i = 0; st.hasMoreTokens(); i++) {
							queryDoc[i] = st.nextToken();
						}

						System.out.println("call lucene with query doc");
						luceneLeft = new HelloLucene(queryDoc,
								collectionLeft.getSelectedItem());

						System.out.println("get Results from document id: "
								+ luceneLeft.getDocumentId());
						System.out.println(luceneLeft.getResult());
						System.out.println(luceneLeft.getDocument().get(
								"recordId")
								+ luceneLeft.getDocument().get("text"));

					}// ifend
				}// for

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
}
