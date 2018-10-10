package sintacticSource;

import java.util.ArrayList;

public class ArbolSintactico {
	Nodo a;
	Nodo e;
	Nodo t;
	Nodo f;
	ArrayList<Nodo> nodos;
	
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
		if ((n.getDer() == null) && (n.getIzq() == null)){
			n.imprimirNodo();
		}
		else {
			imprimirSubArbol (n.getIzq());
			imprimirSubArbol (n.getDer());
		}
			
		
	}
}
