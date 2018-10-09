package sintacticSource;

import lexicalSource.TableRecord;

public class Nodo {
	
	String lexema;
	TableRecord tableRec;
	Nodo izq;
	Nodo der;
		
	public Nodo(TableRecord tr){
		tableRec = tr;
		this.lexema = tr.getLexema();
	}
	
	public Nodo(String lexema, Nodo nodoIzq, Nodo nodoDer){
		this.lexema = lexema;
		this.izq=nodoIzq;
		this.der=nodoDer;
	}
	
	
}
