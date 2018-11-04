package sintacticSource;

import javax.swing.JPanel;

import lexicalSource.TableRecord;
import ui.ArbolExpresionGrafico;

public class Nodo {
	
	private String lexema;
	private TableRecord tableRec;
	private Nodo izq;
	private Nodo der;
	private Nodo padre;
		
	public Nodo(TableRecord tr){
		tableRec = tr;
		this.lexema = tr.getLexema();
		this.izq = null;
		this.der = null;
	}
	
	public Nodo(String lexema, Nodo nodoIzq, Nodo nodoDer){
		this.lexema = lexema;
		this.izq=nodoIzq;
		this.der=nodoDer;
	}
	
	public Nodo(String lexema) {
		tableRec = null;
		this.lexema = lexema;
		this.izq = null;
		this.der = null;
	}

	public String getLexema() {
		return lexema;
	}

	public void setLexema(String lexema) {
		this.lexema = lexema;
	}

	public TableRecord getTableRec() {
		return tableRec;
	}

	public void setTableRec(TableRecord tableRec) {
		this.tableRec = tableRec;
	}

	public Nodo getIzq () {
		return this.izq; 
	}
	
	public Nodo getDer () {
		return this.der;
	}
	
	public void setIzq(Nodo izq) {
		this.izq = izq;
	}

	public void setDer(Nodo der) {
		this.der = der;
	}
	
	public void imprimirNodo () {
		if(izq != null)
			izq.imprimirNodo();
		System.out.println(this.lexema);
		if(der != null)
			der.imprimirNodo();
		
	}
	
	public void setProximaSentencia(Nodo nodo){
		Nodo pos = this;
		while (pos.getDer() != null){
			pos = pos.getDer();
		}
		pos.setDer(nodo);
	}
	
	public Nodo getFuncionPadre(){
		Nodo retorno = this;
		while (retorno.getPadre() != null){
			System.out.println("nodo "+retorno.getLexema());
			retorno = retorno.getPadre();
			System.out.println("padre "+retorno.getLexema());
		}
		return retorno;
	}
	public Nodo getPadre() {
		return padre;
	}

	public void setPadre(Nodo padre) {
		this.padre = padre;
	}
	
	public ArbolExpresionGrafico getdibujo() {
        return new ui.ArbolExpresionGrafico(this);
    }
}
