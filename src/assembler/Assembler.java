package assembler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map.Entry;


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
	
	
	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public Assembler(Parser parser, String program) {
		this.parser = parser;
		this.tablaSimbAux = parser.getTable();
		this.lexico = parser.getAnalizer();
		this.program = program;	
		//this.path = path;
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
//		
//		String aux = generarAssembler();
//		p.println(declararVariablesAux());
		p.println(".code");
		p.println("start:");
		p.println("invoke HelloWorld, NULL, addr HelloWorld,addr HelloWorld,MB_OK");
//		p.println("JMP @LABEL_END");
//		p.println("@LABEL_OVERFLOW:");
//		p.println("invoke MessageBox, NULL, addr overflow, addr overflow, MB_OK");
//		p.println("JMP @LABEL_END");
//		p.println("@LABEL_DIVIDEZERO:"); 
//		p.println("invoke MessageBox, NULL, addr divideZero, addr divideZero, MB_OK");
//		p.println("JMP @LABEL_END");
//		p.println("@LABEL_PERDIDAINFO:");
//		p.println("invoke MessageBox, NULL, addr perdidaInfo, addr perdidaInfo, MB_OK");
//		p.println("JMP @LABEL_END");
//		p.println("@LABEL_END:");
//		p.println("invoke ExitProcess, 0");
		p.println("end start");
//		
		p.close();
		
	}


	private char[] declararVariablesAux() {
		// TODO Auto-generated method stub
		return null;
	}


	private void generarAssembler() {
		
		while (raiz.getLeftSubTree()!=null){
			Nodo subTree = raiz.getLeftSubTree();
			TableRecord trI,trD;
			trI = subTree.getIzq().getTableRec();
			trD = subTree.getDer().getTableRec();
			switch (subTree.getLexema()){
				
			case "+": //usi + usi || doub + doubl
				
				if (trI.getType().equals(trD.getType())){
					if (trI.equals("usinteger")){
						
					}
					if (trI.equals("double")){
						
					}
				}
				else{
					System.out.println("los tipos del subArbol no coinciden");
				}
			}
		}
		
	}
	private String declararVariables() {
		String aux = "";
		
		for (TableRecord tr: tablaSimbAux.getElements()) {
			tr.print();
			if (tr.getIdToken() == lexico.ID){ 
				if ((tr.getType() != null) && (tr.getType().equals("usinteger"))){
					aux +=tr.getLexema() + "\t" + "dw ?" + "\n";
				}
				if ((tr.getType() != null) && (tr.getType().equals("double"))){
					aux += tr.getLexema() + "\t" + "dd ?" + "\n";
				}
			}
			
		}
		return aux;
	}

}
