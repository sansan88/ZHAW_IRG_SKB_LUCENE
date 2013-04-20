package ch.zhaw.irg;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MyFrame extends Frame implements WindowListener, ActionListener{
	private Color bg;
	
	//Gui Elemente
	private Panel panelTop;
	private Panel panelLeft;
	private Panel panelRight; 
	
	private Button btnSearch = null;
	private Button btnExportLeft = null;
	private Button btnExportRight = null; 
	
	
	public static void main(String args[]){
		MyFrame myFrame = new MyFrame();
		myFrame.setSize(600, 600);
		myFrame.setVisible(true);
		myFrame.setLayout(new BorderLayout());
	}
	
	public MyFrame(){
		this.addWindowListener(this);
		
		bg = new Color(255,255,200);
		setBackground(bg);
		
		setTitle("IRG Search by scalcsan & mamutnad");
		
		//GUI Komponenten erzeugen
		
		//TOP
		panelTop = new Panel();
		panelTop.setLayout(new GridLayout(2,1));
		
		btnSearch = new Button("GoGoGo");
		panelTop.add(btnSearch);
		
		add(panelTop, BorderLayout.NORTH);
		
		//LEFT
		panelLeft = new Panel();
		panelLeft.setLayout(new GridLayout(2,1));
		btnExportLeft = new Button("Export to File, Left");
		panelLeft.add(btnExportLeft);
		
		add(panelLeft, BorderLayout.WEST);
		
		//RIGHT
		panelRight = new Panel();
		panelRight.setLayout(new GridLayout(12,2,5,5));
		btnExportRight = new Button("Export to File, Right");
		panelRight.add(btnExportRight);
		
		add(panelRight, BorderLayout.EAST);
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
