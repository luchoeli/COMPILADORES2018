package assembler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map.Entry;

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
	private Nodo raiz;
	private ArrayList<Nodo> funciones;
	private InstruccionesASS instrucciones = new InstruccionesASS();
	private int auxNro=0;
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
		while (raiz.getLeftSubTree()!=null){// && raiz.getLeftSubTree()!=raiz){ //&& codigo.equals("")){
			/*
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			
			Nodo subTree = raiz.getLeftSubTree();
			subTree.imprimirNodo();
			System.out.println("SUBTREE "+subTree.getLexema() + "->" +subTree.getDer().getLexema()+"<-");
			Nodo nodoI=null,nodoD = null;
			nodoI = subTree.getIzq();
			nodoD = subTree.getDer();
			String left = dameValoresOids(nodoI.getTableRec());
			String rigth=dameValoresOids(nodoD.getTableRec());
			String varAux = null;
			TableRecord trI=null,trD = null;
			if (nodoI!=null){
				trI = subTree.getIzq().getTableRec();
				left = dameValoresOids(trI);
			}
			if (nodoD!=null){
				trD = subTree.getDer().getTableRec();
				rigth = dameValoresOids(trD);
			}
			
			
			switch (subTree.getLexema()){
			//------------------------------------------------------------------------ ASIGNACION ---//
			// _a:= 3.,  _a:= _b,  _a:= @aux,
			case ":=":
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
				//agrego aux a la tabla de simbolos FIXME ver en que tabla lo hago
				//System.out.println("CCCCC: \n"+codigo);
				TableRecord auxTR = new TableRecord(varAux, lexico.ID);
				auxTR.setType(trI.getType());
				Nodo auxN = new Nodo(varAux);
				auxN.setTableRec(auxTR);
				subTree.reemplazarSubtree(auxN);
				tablaSimbAux.put(varAux, auxTR);
				break;
			}
			case "-": //usi + usi || doub + doubl
			{
				varAux=dameNombreAux();
				if (trI.getType().equals("usinteger")){
					codigo+= instrucciones.restaUsinteger(left,rigth,varAux);
				}
				if (trI.getType().equals("double")){
					codigo+= instrucciones.restaDouble(left,rigth,varAux);
				}
				//agrego aux a la tabla de simbolos FIXME ver en que tabla lo hago
				//System.out.println("CCCCC: \n"+codigo);
				TableRecord auxTR = new TableRecord(varAux, lexico.ID);
				auxTR.setType(trI.getType());
				Nodo auxN = new Nodo(varAux);
				auxN.setTableRec(auxTR);
				subTree.reemplazarSubtree(auxN);
				tablaSimbAux.put(varAux, auxTR);
				break;
			}
			}
			
			
			
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
					auxiliares +=tr.getLexema() + "\t" + "dw ?" + "\n";
				}
				if ((tr.getType() != null) && (tr.getType().equals("double"))){
					auxiliares += tr.getLexema() + "\t" + "dd ?" + "\n";
				}
			}
			
		}
		return auxiliares;
	}
	
	private String declararVariablesAux() {
		String auxiliares="";
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

}
