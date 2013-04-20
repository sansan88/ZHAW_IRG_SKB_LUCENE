package ch.zhaw.irg;

import java.awt.BorderLayout;
import java.awt.Button;
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
	private Panel panelLeft;
	private Panel panelRight;

	private Button btnSearch = null;
	private Button btnExportLeft = null;
	private Button btnExportRight = null;

	private TextArea txtAreaLeft;
	private TextArea txtAreaRight;

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

		btnSearch = new Button("GoGoGo");
		panelTop.add(btnSearch);

		add(panelTop, BorderLayout.NORTH);

		// CENTER
		panelCenter = new Panel();
		panelCenter.setLayout(new GridLayout(1, 2));
		// panelCenter.add(panelLeft);
		// panelCenter.add(panelRight);

		add(panelCenter, BorderLayout.CENTER);

		// // LEFT
		// panelLeft = new Panel();
		// panelLeft.setLayout(new GridLayout(2, 1));
		// btnExportLeft = new Button("Export to File, Left");
		// panelLeft.add(btnExportLeft);
		// panelLeft.add(txtAreaLeft);
		// // add(panelLeft);
		//
		// // RIGHT
		// panelRight = new Panel();
		// panelRight.setLayout(new GridLayout(2, 1));
		// btnExportRight = new Button("Export to File, Right");
		// panelRight.add(btnExportRight);
		// panelRight.add(txtAreaRight);
		// // add(panelRight);

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
