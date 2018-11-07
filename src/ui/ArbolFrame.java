package ui;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.border.EmptyBorder;

import javafx.scene.input.MouseDragEvent;
import sintacticSource.Nodo;
import javax.swing.JScrollPane;

import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Window.Type;

public class ArbolFrame extends JFrame {

	private JPanel contentPane;
	private Nodo raiz = null;
	private ArrayList<Nodo> funciones = null;
	private JTabbedPane tabbedPane;

	/**
	 * Create the frame.
	 */
	public ArbolFrame() {
		setTitle("Arbol Sintactico");
		setBounds(100, 100, 1123, 654);
		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(50, 50));
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
			
			ArbolExpresionGrafico jpanel = new ArbolExpresionGrafico(nodo);
			
			//jpanel.setPreferredSize(jpanel.get);
			jpanel.setPreferredSize(new Dimension(2000,1000));
			tabbedPane.addTab(nodo.getLexema(),null,jpanel,null);
		}
	}
}
