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
		nodo.setPadre(pos);
		//System.out.println("al nodo "+nodo.getLexema()+" es el hijo der de '"+ pos.getLexema() +"' ");
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
		//FIXME LOS UNARIOS VAN A LA IZQ;
		/*
		if ((getIzq()==null && getDer()!=null && getDer().imAleave())){
			System.out.println("1");
			return true;
		}
		*/
		if (getDer() == null && getIzq()!=null&&getIzq().imAleave()){
			System.out.println("2");
			return true;
		}
		return false;
	}
	/** true si soy una hoja**/
	private boolean imAleave(){
		if (getDer()==null && getIzq()==null){
			
			//System.out.println(getLexema()+": soy hoja");
			return true;
		}
		return false;
	}
	/** true si soy un subarbol izquierdo (nodo con hijos sin nietos),
	 *  es decir:
	 *  	- que mis dos hijos son hojas
	 *  	o
	 *  	- uno es unario y el otro es null**/
	private boolean imLeftSubTree(){
		//FIXME no puede ser ni S ni then, ni else, ni cuerpo, ni cond 
		if (getLexema().equals("S")){
			return false;
		}
		if (imAleave()){
			//System.out.println("HOJA");
			return false;
		}
		if (soyUnario()){
			//System.out.println("UNARIO");
			return true;
		}
		if(getIzq()!=null &&  getDer()!=null){
			if (getDer().imAleave() && getIzq().imAleave()){
				return true;
			}
		}
		return false;
	}
	/** devuelvo mi proximo leftSubTree **/
	public Nodo getLeftSubTree(){
		

		if (imLeftSubTree()){
			//System.out.println("<><SUBTREE><><><><> ");
			//this.imprimirNodo();
			//System.out.println("<><><><><><><><><><><");
			return this;
		}else{
			if (getIzq()!=null && !getIzq().imAleave()){
				return getIzq().getLeftSubTree();
			}
			
			if(getDer()!=null && !getDer().imAleave()){
				return getDer().getLeftSubTree();	
			}
			return null;
		}
		
		
		//return null;
	}
	
	public void reemplazarSubtree(Nodo reemplazo){
		Nodo padre = this.getPadre();
		//System.out.println(padre);
		if (padre.getIzq()!=null && padre.getIzq().equals(this))
			padre.setIzq(reemplazo);
		if (padre.getDer()!=null && padre.getDer().equals(this))
			padre.setDer(reemplazo);
	}
	
}
