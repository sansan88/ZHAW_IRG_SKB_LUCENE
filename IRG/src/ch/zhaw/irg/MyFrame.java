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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MyFrame extends Frame implements WindowListener, ActionListener {
	private Color bg;

	private FileHandler qFile; // query File für Dropdown
	private FileHandler cFileLeft; // Left Collection File
	private FileHandler cFileRight; // Right Collection File
	private FileHandler qFileLeft; // Left Query File
	private FileHandler qFileRight; // Right Query File

	private static HelloLucene lucene;

	// Gui Elemente
	private Panel panelTop;
	private Panel panelCenter;

	private Button btnSearch = null;
	private Button btnExportLeft = null;
	private Button btnExportRight = null;

	private TextArea txtAreaLeft;
	private TextArea txtAreaRight;

	private Choice queryList;
	private Choice collectionLeft;
	private Choice collectionRight;

	public static void main(String args[]) {
		MyFrame myFrame = new MyFrame();
		myFrame.setSize(600, 600);
		myFrame.setVisible(true);
		myFrame.setLayout(new BorderLayout());
		lucene = new HelloLucene();
	}

	public MyFrame() {
		this.addWindowListener(this);

		bg = new Color(255, 255, 200);
		setBackground(bg);

		setTitle("IRG Search by scalcsan & mamutnad");

		// GUI Komponenten erzeugen

		// TOP Panel mit Query Bereich
		panelTop = new Panel();
		panelTop.setLayout(new GridLayout(2, 1));

		// Dropdown Liste aufbauen für Query select
		queryList = new Choice();
		qFile = new FileHandler(true, false, "EN");
		qFile.setIds();
		queryList.add("All");
		// for (int i = 0; i < qFile.docIds.length; i++) {
		// System.out.println("add item to choicebox. ID: " + qFile.docIds[i]);
		// queryList.add(qFile.docIds[i]);
		// }
		panelTop.add(queryList);

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

		if (e.getSource() == btnSearch) {
			System.out.println("Search, Call Lucene");

			// Process Left Side
			System.out.println("File Selected Left: "
					+ collectionLeft.getSelectedItem());
			cFileLeft = new FileHandler(false, true, // Collection
					collectionLeft.getSelectedItem());
			qFileLeft = new FileHandler(true, false, // Query
					collectionLeft.getSelectedItem());

			// Process Right Side
			System.out.println("File selected Right: "
					+ collectionRight.getSelectedItem());
			cFileRight = new FileHandler(false, true, // Collection
					collectionRight.getSelectedItem());
			qFileRight = new FileHandler(true, false, // Query
					collectionRight.getSelectedItem());

			FileHandler[] args = new FileHandler[2];
			args[0] = qFileRight;
			args[1] = cFileRight;

			HelloLucene luceneRight = new HelloLucene();

			// txtAreaRight.setText(luceneRight.getResult);
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
