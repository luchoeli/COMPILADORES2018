package assembler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map.Entry;

import javax.swing.plaf.synth.SynthSpinnerUI;

import java.util.Stack;

import com.sun.javafx.font.Disposer;

import lexicalSource.*;
import sintacticSource.*;

public class Assembler {
	private File archivo;
	private PrintWriter p;
	private Table tablaSimbAux;
	private LexicalAnalizer lexico;
	private Parser parser;
	private String proxSalto;
	private String program;
	private String codigo;
	private String path;
	private boolean huboElse = false;
	private Nodo raiz;
	private ArrayList<Nodo> funciones;
	private InstruccionesASS instrucciones = new InstruccionesASS();
	private int auxNro=0;
	private Stack<String> labelsStack = new Stack<String>();
	private int nroLabel = 0;
	private static final String LABEL ="@Label";
	private String consola;
	
	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public Assembler(Parser parser, String program) {
		this.parser = parser;
		this.tablaSimbAux = new Table();
		this.lexico = parser.getAnalizer();
		this.program = program;	
		this.raiz = parser.getRaiz();
		this.funciones = parser.getFunciones();
		this.raiz.setLexema("RAIZ");
		this.consola = "";
	}
		

	public File getArchivo() {
		return archivo;
	}


	public void setArchivo(File archivo) {
		this.archivo = archivo;
	}


	public void ejecutable() throws IOException{
		archivo = new File(path);
		p = new PrintWriter(new FileWriter(archivo.getParent()+File.separator+archivo.getName().replace("txt","asm")));
		
		p.println(".386");
		p.println(".model flat, stdcall");
		p.println("option casemap :none");
		p.println("include \\masm32\\include\\windows.inc");
		p.println("include \\masm32\\include\\kernel32.inc");
		p.println("include \\masm32\\include\\user32.inc");
		p.println("includelib \\masm32\\lib\\kernel32.lib");
		p.println("includelib \\masm32\\lib\\user32.lib");
		p.println(".data");
		p.println("HelloWorld db \"Hello freak bitches\", 0");
		p.println("overflow db \"Ha ocurrido Overflow\" , 0");
		p.println("divideZero db \"Se ha intentado dividir por cero\" , 0");
		p.println("aux_mem_2bytes dw ?");
		p.println("perdidaInfo db \"Se ha producido perdida de informacion\" , 0");
		p.print(declararVariables());		
		String aux = generarAssembler();
		p.println(declararVariablesAux());
		p.println(".code");
		p.println("start:");
		p.print(aux);
		p.println("invoke HelloWorld, NULL, addr HelloWorld,addr HelloWorld,MB_OK");
		//TODO Codigo de las Funciones!! ver donde se declaran las variables!
		p.println("JMP @LABEL_END");
		p.println("@LABEL_OVERFLOW:");
		p.println("invoke MessageBox, NULL, addr overflow, addr overflow, MB_OK");
		p.println("JMP @LABEL_END");
		p.println("@LABEL_DIVIDEZERO:"); 
		p.println("invoke MessageBox, NULL, addr divideZero, addr divideZero, MB_OK");
		p.println("JMP @LABEL_END");
		p.println("@LABEL_PERDIDAINFO:");
		p.println("invoke MessageBox, NULL, addr perdidaInfo, addr perdidaInfo, MB_OK");
		p.println("JMP @LABEL_END");
		p.println("@LABEL_END:");
		p.println("invoke ExitProcess, 0");
		p.println("end start");
		
		p.close();
		
	}

	private String generarAssembler() {
		String codigo="";
		//raiz.limpiarTree();
		
		while (raiz.getLeftSubTree()!=null){// && raiz.getLeftSubTree()!=raiz){ //&& codigo.equals("")){
			
			Nodo subTree = raiz.getLeftSubTree();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			//subTree.imprimirNodo();
			System.out.println("-------------- SUBTREE "+subTree.getLexema());
			
			Nodo nodoI=null,nodoD = null;
			String left="",rigth="";
			TableRecord trI=null,trD=null;
			if (subTree.getIzq()!=null){
				nodoI = subTree.getIzq();
				if (nodoI.getTableRec()!=null){
					trI=nodoI.getTableRec();
					left = dameValoresOids(trI);
				}
			}
			if (subTree.getDer()!=null){
				nodoD = subTree.getDer();
				if (nodoD.getTableRec()!=null){
					trD = nodoD.getTableRec();
					rigth=dameValoresOids(trD);					
				}
			}
			
			String varAux = null;
			
			
			switch (subTree.getLexema()){
			//------------------------------------------------------------------------ ASIGNACION ---//
			// _a:= 3.,  _a:= _b,  _a:= @aux,
			case ":=":
			{
				//System.out.println("ASIGNACION");
				if (trI.getType().equals("usinteger")){
					codigo+=instrucciones.asignacionUSINT(left, rigth);
				}
				
				
				if (trI.getType().equals("double")){
					codigo+=instrucciones.asignacionDOUB(left,rigth);
				}
				
				//-------------------------- elimino el bubtree -----------------------///
				subTree.reemplazarSubtree(null);
				
				break;
			}
				//------------------------------------------------------------------------ SUMA ---//
			case "+": //usi + usi || doub + doubl
			{
				System.out.println("HEEEEEELOOOOOOOOU ");
				varAux=dameNombreAux();
				if (trI.getType().equals("usinteger")){
					codigo+= instrucciones.sumaUsinteger(left,rigth,varAux);
				}
				if (trI.getType().equals("double")){
					codigo+= instrucciones.sumaDouble(left,rigth,varAux);
				}
				
				
				TableRecord auxTR = new TableRecord(varAux, lexico.ID);
				auxTR.setType(trI.getType());
				Nodo auxN = new Nodo(varAux);
				auxN.setTableRec(auxTR);
				subTree.reemplazarSubtree(auxN);
				tablaSimbAux.put(varAux, auxTR);
				break;
			}
			case "-" : //------------------------------------------------------------------------ RESTA ---//
				
			{
				varAux=dameNombreAux();
				if (trI.getType().equals("usinteger")){
					codigo+= instrucciones.restaUsinteger(left,rigth,varAux);
				}
				if (trI.getType().equals("double")){
					codigo+= instrucciones.restaDouble(left,rigth,varAux);
				}
				TableRecord auxTR = new TableRecord(varAux, lexico.ID);
				auxTR.setType(trI.getType());
				Nodo auxN = new Nodo(varAux);
				auxN.setTableRec(auxTR);
				subTree.reemplazarSubtree(auxN);
				tablaSimbAux.put(varAux, auxTR);
				break;
			}
			
			
			case "*"://------------------------------------------------------------------------ MULTIPLICATION ---//
			{
				varAux=dameNombreAux();
				if (trI.getType().equals("usinteger")){
					codigo+= instrucciones.multiplicaUsinteger(left,rigth,varAux);
				}
				if (trI.getType().equals("double")){
					codigo+= instrucciones.multiplicaDouble(left,rigth,varAux);
				}
				
				TableRecord auxTR = new TableRecord(varAux, lexico.ID);
				auxTR.setType(trI.getType());
				Nodo auxN = new Nodo(varAux);
				auxN.setTableRec(auxTR);
				subTree.reemplazarSubtree(auxN);
				tablaSimbAux.put(varAux, auxTR);
				break;
				
			}
			case "/"://------------------------------------------------------------------------ divide ---//
			{
				varAux=dameNombreAux();
				if (trI.getType().equals("usinteger")){
					codigo+= instrucciones.divideUsinteger(left,rigth,varAux);
				}
				if (trI.getType().equals("double")){
					codigo+= instrucciones.divideDouble(left,rigth,varAux);
				}
				
				TableRecord auxTR = new TableRecord(varAux, lexico.ID);
				auxTR.setType(trI.getType());
				Nodo auxN = new Nodo(varAux);
				auxN.setTableRec(auxTR);
				subTree.reemplazarSubtree(auxN);
				tablaSimbAux.put(varAux, auxTR);
				break;
				
			}
			case "="://-------------------------------------------------------------------------------------------------igualdad----///
			{	
				if (trI.getType().equals("usinteger")){
					codigo+= instrucciones.usintComparador(left,rigth);
				}
				if (trI.getType().equals("double")){
					codigo+=instrucciones.doubleComparador(left,rigth);
				
				}
				proxSalto="JNE";
				subTree.reemplazarSubtree(null);
				break;
			}
			case "<="://-------------------------------------------------------------------------------------------------igualdad----///
			{
				if (trI.getType().equals("usinteger")){
					codigo+= instrucciones.usintComparador(left,rigth);
					proxSalto="JA";
				}
				if (trI.getType().equals("double")){
					codigo+=instrucciones.doubleComparador(left,rigth);
					proxSalto="JG";
				}
				subTree.reemplazarSubtree(null);
				break;
			}
			case ">="://-------------------------------------------------------------------------------------------------igualdad----///
			{	
				if (trI.getType().equals("usinteger")){
					codigo+= instrucciones.usintComparador(left,rigth);
					proxSalto="JB";
				}
				if (trI.getType().equals("double")){
					codigo+=instrucciones.doubleComparador(left,rigth);
					proxSalto="JL";
				}
				subTree.reemplazarSubtree(null);
				break;
			}
			case "<"://-------------------------------------------------------------------------------------------------igualdad----///
			{
				if (trI.getType().equals("usinteger")){
					codigo+= instrucciones.usintComparador(left,rigth);
					proxSalto="JAE";
				}
				if (trI.getType().equals("double")){
					codigo+=instrucciones.doubleComparador(left,rigth);
					proxSalto="JGE";
				}
				subTree.reemplazarSubtree(null);
				break;
			}
			case ">"://-------------------------------------------------------------------------------------------------igualdad----///
			{
				if (trI.getType().equals("usinteger")){
					codigo+= instrucciones.usintComparador(left,rigth);
					proxSalto="JBE";
				}
				if (trI.getType().equals("double")){
					codigo+=instrucciones.doubleComparador(left,rigth);
					proxSalto="JLE";
				}
				
				subTree.reemplazarSubtree(null);
				break;
			}
			
			case "Condicion":
			{	
				String label = dameNextLabel();
				codigo+=proxSalto + " " + label+" \n" ;
				codigo+=";--------------------------------------------------------------------------[THEN]----------\n";
				labelsStack.push(label);
				subTree.reemplazarSubtree(null);
				break;
			}
			
			
			case "THEN" :
			{
				String label = dameNextLabel();
				codigo+="JMP "+label+" \n";
				codigo+=labelsStack.pop()+" \n";
				labelsStack.push(label);
				subTree.reemplazarSubtree(null);
				codigo+=";--------------------------------------------------------------------------[ELSE/FIN]----------\n";
				break;
			}
			
			case "ELSE":
			{
				codigo+=labelsStack.pop()+" \n";
				subTree.reemplazarSubtree(null);
				huboElse = true;
				break;
			}
			
			case "Cuerpo" :
			{
				System.out.println("<cuerpo>");
				subTree.reemplazarSubtree(null);
				if (!huboElse){
					codigo+=labelsStack.pop()+" \n";
				}
				break;
			}
			
			case "IF" :
			{
				System.out.println("<IF>");
				subTree.reemplazarSubtree(null);
				break;
			}
			case "print" :
			{
				System.out.println("<print>");
				consola+=subTree.getIzq().getLexema()+" \n";
				subTree.reemplazarSubtree(null);
				break;
			}
				
			}
			
			
			raiz.limpiarTree();
		}
		
		return codigo;
	}
	
	/**devuelve un string con el codigo assembler necesario para declarar
	 * las variables no auxiliares encontrados en la tabla de simbolos**/
	private String declararVariables() {
		String auxiliares = "";
		
		for (TableRecord tr: parser.getTable().getElements()) {
			tr.print();
			if (tr.getIdToken() == lexico.ID){ 
				if ((tr.getType() != null) && (tr.getType().equals("usinteger"))){
					auxiliares +=tr.getLexema() + "\t" + "dw ?" + "\n";  // 2 bytess - 16 bit 
				}
				if ((tr.getType() != null) && (tr.getType().equals("double"))){  //8 bytes - 64 bits
					auxiliares += tr.getLexema() + "\t" + "dq ?" + "\n";
				}
			}
			
		}
		return auxiliares;
	}
	
	
	private String dameNextLabel(){
		String label = LABEL+nroLabel;
		nroLabel++;
		return label;
		
	}
	private String declararVariablesAux() {
		String auxiliares="";
		auxiliares += "var_aux_dx dw ?" + "\n";
		auxiliares += "@0 dw 0" + "\n";
		for (TableRecord tr : tablaSimbAux.getElements()) {
			tr.print();
			if (tr.getIdToken() == lexico.ID){ 
				if ((tr.getType() != null) && (tr.getType().equals("usinteger"))){
					auxiliares +=tr.getLexema() + "\t" + "dw ?" + "\n";
				}
				if ((tr.getType() != null) && (tr.getType().equals("double"))){
					auxiliares += tr.getLexema() + "\t" + "dd ?" + "\n";
				}
			}
		}
		return auxiliares;
	}
	/** dado un tr devuelve
	 * 		si es CTE --> valor 
	 * 		si es ID  --> lexema
	 * **/
	private String dameValoresOids(TableRecord tr){
		int idtoken = tr.getIdToken();
		
		if (idtoken == lexico.CTE){
			return tr.getValue();
		}
		
		if (idtoken == lexico.ID || tr.getLexema().contains("@")){
			return tr.getLexema();
		}
		return null;
	}
	
	public String dameNombreAux(){
		String auxiliar = "@aux"+String.valueOf(auxNro);
		auxNro++;
		return auxiliar;
	}
	
	public String getConsola(){
		return consola;
	}

}
