package sintacticSource;

import lexicalSource.TableRecord;

public class Nodo {
	
	private String lexema;
	private TableRecord tableRec;
	private Nodo izq;
	private Nodo der;
		
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
	
	public Nodo getIzq () {
		return this.izq;
	}
	
	public Nodo getDer () {
		return this.der;
	}
	
	public void imprimirNodo () {
		System.out.println(this.lexema);
	}
	
}
