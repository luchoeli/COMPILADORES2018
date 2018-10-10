package sintacticSource;

import java.util.ArrayList;

public class ArbolSintactico {
	private Nodo a;
	private Nodo e;
	private Nodo t;
	private Nodo f;
	private ArrayList<Nodo> nodos;
	public int nivel; 
	
	public ArbolSintactico(){
		setA(null);
		setE(null);
		setT(null);
		setF(null);
		nodos = new ArrayList<Nodo>();
	}
	
	public void add(Nodo n){
		nodos.add(n);
	}

	
	public void imprimirArbol () {
		
		for (int i=0; i<nodos.size(); i++) {
			System.out.println("---------- NUEVO SUBARBOL -----------");
				imprimirSubArbol (nodos.get(i));
		}
		
	}
	
	private void imprimirSubArbol (Nodo n) {
		
		if (n!=null){
			imprimirSubArbol (n.getIzq());
			n.imprimirNodo();
			imprimirSubArbol (n.getDer());
			
		}
	}

	public Nodo getA() {
		return a;
	}

	public void setA(Nodo a) {
		this.a = a;
	}

	public Nodo getE() {
		return e;
	}

	public void setE(Nodo e) {
		this.e = e;
	}

	public Nodo getT() {
		return t;
	}

	public void setT(Nodo t) {
		this.t = t;
	}

	public Nodo getF() {
		return f;
	}

	public void setF(Nodo f) {
		this.f = f;
	}
}
