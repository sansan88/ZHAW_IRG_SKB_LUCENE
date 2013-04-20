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

	private Choice queryList;
	private Choice collectionLeft;
	private Choice collectionRight;
	
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

		// TOP
		panelTop = new Panel();
		panelTop.setLayout(new GridLayout(2, 1));

		queryList = new Choice();
		queryList.add("Query 1");
		queryList.add("Query 2");
		panelTop.add(queryList);
		
		btnSearch = new Button("GoGoGo");
		panelTop.add(btnSearch);

		add(panelTop, BorderLayout.NORTH);

		// CENTER
		panelCenter = new Panel();
		panelCenter.setLayout(new GridLayout(3, 2));

		
		collectionLeft = new Choice();
		collectionLeft.add("DE");
		collectionLeft.add("RU");
		panelCenter.add(collectionLeft);
		
		collectionRight = new Choice();
		collectionRight.add("DE");
		collectionRight.add("FR");
		panelCenter.add(collectionRight);
		
		btnExportLeft = new Button("Export to File, Left");
		panelCenter.add(btnExportLeft);
		btnExportRight = new Button("Export to File, Right");
		panelCenter.add(btnExportRight);

		txtAreaLeft = new TextArea("das ist ein Text");
		panelCenter.add(txtAreaLeft);
		txtAreaRight = new TextArea("das ist auch ein  Text");
		panelCenter.add(txtAreaRight);
		
		add(panelCenter, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

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
