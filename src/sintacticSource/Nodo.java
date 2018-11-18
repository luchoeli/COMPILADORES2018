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
		//TODO Cuerpo?, IF?
		if (((this.getLexema().equals("Condicion") || 
				this.getLexema().equals("ELSE") || 
				this.getLexema().equals("THEN") || 
				this.getLexema().equals("IF") ||
				this.getLexema().equals("Cuerpo"))	&& imAleave()) ||
				this.getLexema().equals("print")){
			return true;
		}
		
		
		/*
		if (getDer() == null && getIzq()!=null&&getIzq().imAleave()){
			System.out.println(this.getLexema()+" 2");
			return true;
		}
		 */
		return false;
	}
	/** true si soy una hoja**/
	private boolean imAleave(){
		if (getDer()==null && getIzq()==null){
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
		if (getLexema().equals("S")){
			return false;
		}
		if (soyUnario()){
			//System.out.println("UNARIO");
			return true;
		}
		if (imAleave()){
			//System.out.println("HOJA");
			return false;
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
		System.out.println("getLeftSub:"+this.getLexema());
		if (imLeftSubTree()){
			return this;
		}
		
		if (getIzq()!=null ){ //&& !getIzq().imAleave()
			if(getIzq().imLeftSubTree())
				return this.getIzq();
			return getIzq().getLeftSubTree();
		}
		
		if(getDer()!=null){
			if(getDer().imLeftSubTree())
				return this.getDer();
			return getDer().getLeftSubTree();	
		}
		
		if (this.getPadre()!=null && this.getPadre().getDer()!=null){
			return this.getPadre().getDer().getLeftSubTree();
		}
		System.out.println("aca es null");
		return null;
		
	}
	
	
	public void reemplazarSubtree(Nodo reemplazo){
		Nodo padre = this.getPadre();
		System.out.println("PADRE: "+padre.getLexema());
		if (padre.getIzq()!=null && padre.getIzq().equals(this))
			padre.setIzq(reemplazo);
		if (padre.getDer()!=null && padre.getDer().equals(this))
			padre.setDer(reemplazo);
	}
	
	public void limpiarTree(){
		if (this.getIzq()!=null)
			this.getIzq().limpiarTree();
		
		if (this.getDer()!=null)
			this.getDer().limpiarTree();
		
		if (getLexema().equals("S") && (imAleave())){
			reemplazarSubtree(null);
		}
	}
	
	
}
