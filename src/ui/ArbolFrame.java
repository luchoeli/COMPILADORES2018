package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Panel;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.border.EmptyBorder;

import com.sun.xml.internal.ws.api.server.Container;

import sintacticSource.Nodo;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class ArbolFrame extends JFrame {

	private JPanel contentPane;
	private Nodo raiz = null;
	private ArrayList<Nodo> funciones = null;
	private JTabbedPane tabbedPane;

	/**
	 * Create the frame.
	 */
	public ArbolFrame() {
		setBounds(100, 100, 734, 521);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		scrollPane.setViewportView(tabbedPane);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 716, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
		);
		contentPane.setLayout(gl_contentPane);
		
		JPanel panel = new JPanel();
		//tabbedPane.addTab("New tab", null, panel, null);
	}


	public void setRaiz(Nodo raiz) {
		this.raiz = raiz;
		dibujarRaiz(raiz);
	}
	
	private void dibujarRaiz(Nodo raiz2) {
		tabbedPane.addTab("Programa Principal",null,raiz.getdibujo(),null);
		
	}


	public void dibujamelo(JPanel panel){
		contentPane.add(panel);
	}


	public ArrayList<Nodo> getFunciones() {
		return funciones;
	}


	public void setFunciones(ArrayList<Nodo> funciones) {
		this.funciones = funciones;
		fillTabPanel(funciones);
	}
	
	public void fillTabPanel(ArrayList<Nodo> funciones){
		
		for (Nodo nodo : funciones) {
			
			ArbolExpresionGrafico jpanel = nodo.getdibujo();
			JScrollPane sp = new JScrollPane(jpanel);
			sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			tabbedPane.addTab(nodo.getLexema(),null,sp,null);
			
			
		}
	}
}
