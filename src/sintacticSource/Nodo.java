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
			retorno = retorno.getPadre();
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
	
	/** true si tengo un hijo null y el otro es una hoja**/
	private boolean soyUnario(){
		if ((getIzq()==null && getDer().imAleave()) || (getDer() == null && getIzq().imAleave()) ){
			return true;
		}
		return false;
	}
	/** true si soy una hoja**/
	private boolean imAleave(){
		if (getDer()==null && getIzq()==null){
			return true;
		}
		return false;
	}
	/** true si soy un subarbol sintactico,
	 *  es decir:
	 *  	- que mis dos hijos son hojas
	 *  	o
	 *  	- uno es unario y el otro es null**/
	private boolean imLeftSubTree(){
		if (getDer().imAleave() && getIzq().imAleave()){
			return true;
		}
		if (soyUnario()){
			return true;
		}
		return false;
	}
	/** devuelvo mi proximo leftSubTree **/
	public Nodo getLeftSubTree(){
		if (this.imLeftSubTree()){
			return this;
		}
		
		if (getIzq()!=null && !getIzq().imLeftSubTree()){
			getIzq().getLeftSubTree();
		}
		if(getDer()!=null && !getDer().imLeftSubTree()){
			getDer().getLeftSubTree();	
		}
		
		return this;
	}
}
