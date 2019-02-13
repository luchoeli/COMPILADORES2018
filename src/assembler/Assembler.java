package assembler;
// ADRIAN
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
	private String path;
	private boolean huboElse = false;
	private Nodo raiz;
	private ArrayList<Nodo> funciones;
	private InstruccionesASS instrucciones = new InstruccionesASS();
	private int auxNro=0;
	private Stack<String> labelsStack = new Stack<String>();
	private int nroLabel = 0;
	private static final String LABEL ="@Label_";
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
		p.println("; \\masm32\\bin\\ml /c /Zd /coff ");
		p.println("; \\masm32\\bin\\Link /SUBSYSTEM:CONSOLE ");
		p.println(".386");
		p.println(".model flat, stdcall");
		p.println("option casemap :none");
		p.println();
		p.println(";------------includes------------");
		p.println("include \\masm32\\include\\windows.inc");
		p.println("include \\masm32\\macros\\macros.asm");
		p.println("include \\masm32\\include\\masm32.inc");
		p.println("include \\masm32\\include\\kernel32.inc");
		p.println("include \\masm32\\include\\user32.inc");
		p.println("include \\masm32\\include\\gdi32.inc");
		p.println();
		p.println(";------------librerias------------");
	    p.println("includelib \\masm32\\lib\\masm32.lib");
	    p.println("includelib \\masm32\\lib\\gdi32.lib");
		p.println("includelib \\masm32\\lib\\kernel32.lib");
		p.println("includelib \\masm32\\lib\\user32.lib");
		p.println();
		p.println("FUNCPROTO       TYPEDEF PROTO"); 
		p.println("FUNCPTR         TYPEDEF PTR FUNCPROTO");
		p.println(";__________________________VARIABLES____________________________");
		p.println(".data ");
		p.println("HelloWorld db \"Hello freak bitches\", 0");
		p.println("overflow db \"Error en ejecucion: Ha ocurrido Overflow\" , 0");
		p.println("divideZero db \"Error en ejecucion:Se ha intentado dividir por cero\" , 0");
		p.println("resultadoNegativo db \"Error en ejecucion: Usinteger negativo\" , 0");
		p.println("aux_mem_2bytes dw ?");
		p.print(declararVariables());		
		p.println(";______________________VARIABLES AUXILIARES____________________");
		String aux = generarAssembler();
		p.println(declararVariablesAux());
		p.println(";_____________________________CODE_____________________________");
		p.println(".code");
		p.println("start:");
		p.print(aux);
		p.println("JMP @LABEL_END"); // FINNNN
		p.println("@LABEL_OVERFLOW:");
		p.println("invoke MessageBox, NULL, addr overflow, addr overflow, MB_OK");
		p.println("JMP @LABEL_END");
		p.println("@LABEL_DIVIDEZERO:"); 
		p.println("invoke MessageBox, NULL, addr divideZero, addr divideZero, MB_OK");
		p.println("JMP @LABEL_END");
		p.println("@LABEL_RESULTADO:");
		p.println("invoke MessageBox, NULL, addr resultadoNegativo, addr resultadoNegativo, MB_OK");
		p.println("@LABEL_END:");
		p.println("invoke ExitProcess, 0");
		p.println("end start");
		
		p.close();
		
	}
	
	private String generarAssembler(){
		String codigo="";
		System.out.println("===COD PRINCIPAL===");
		codigo+="main proc \n";
		codigo+=generarAssemblerNodo(this.raiz);
		codigo+="main endp \n";
		codigo+="JMP @LABEL_END \n \n";

		for (Nodo funcion : funciones) {
			System.out.println("===COD FUNC "+funcion.getLexema()+"===");
			codigo+=funcion.getLexema()+"F proc \n";
			codigo+=generarAssemblerNodo(funcion);
			codigo+="ret \n"+funcion.getLexema()+"F endp \n";
		}
		return codigo;
	}
	private String generarAssemblerNodo(Nodo raiz) {
		String codigo="";
		//raiz.limpiarTree();
		
		while (raiz.getLeftSubTree()!=null){// && raiz.getLeftSubTree()!=raiz){ //&& codigo.equals("")){
			
			Nodo subTree = raiz.getLeftSubTree();
			//subTree.imprimirNodo();
			System.out.println("-------------- SUBTREE "+subTree.getLexema());
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Nodo nodoI=null,nodoD = null;
			String left="",rigth="";
			TableRecord trI=null,trD=null;
			if (subTree.getIzq()!=null){
				nodoI = subTree.getIzq();
				if (nodoI.getTableRec()!=null){
					trI=nodoI.getTableRec();
					left = dameValoresOids(trI);
					
					//if(trI.getType().equals("double"))
						left = get_id_VarDouble(left);
				}
			}
			if (subTree.getDer()!=null){
				nodoD = subTree.getDer();
				if (nodoD.getTableRec()!=null){
					trD = nodoD.getTableRec();
					rigth=dameValoresOids(trD);	
					rigth=get_id_VarDouble(rigth);
				}
			}

			System.out.println("L valores oid: "+left);
			System.out.println("L idvardoub: "+left);
			System.out.println("R valores oid: "+rigth);
			System.out.println("R idvardoub: "+rigth);
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
			case "!="://-------------------------------------------------------------------------------------------------igualdad----///
			{
				if (trI.getType().equals("usinteger")){
					codigo+= instrucciones.usintComparador(left,rigth);
				
				}
				if (trI.getType().equals("double")){
					codigo+=instrucciones.doubleComparador(left,rigth);
				}
				proxSalto="JE";
				
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
					proxSalto="JA";
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
					proxSalto="JB";
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
					proxSalto="JAE";
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
					proxSalto="JBE";
				}
				
				subTree.reemplazarSubtree(null);
				break;
			}
			
			
			case "Condicion":
			{	
				String label = dameNextLabel();
				codigo+="\t "+proxSalto + " " + label+" \n" ;
				codigo+=";==================[THEN]==================\n";
				labelsStack.push(label);
				subTree.reemplazarSubtree(null);
				break;
			}
			
			
			case "THEN" :
			{
				String label = dameNextLabel();
				codigo+="\t JMP "+label+" \n";
				codigo+=";==================[ELSE/FIN]==================\n";
				codigo+=labelsStack.pop()+":\n";
				labelsStack.push(label);
				subTree.reemplazarSubtree(null);
				break;
			}
			
			case "ELSE":
			{
				codigo+=labelsStack.pop()+":\n";
				subTree.reemplazarSubtree(null);
				huboElse = true;
				break;
			}
			
			case "Cuerpo" :
			{
				System.out.println("<cuerpo>");
				subTree.reemplazarSubtree(null);
					codigo+=";==================[FIN IF]==================\n";
				if (!huboElse){
					codigo+=labelsStack.pop()+":\n";
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
				//consola+=subTree.getIzq().getLexema()+" \n";
				codigo+= "\t print chr$(\""+subTree.getIzq().getLexema()+"\", 13,10) \n";

				subTree.reemplazarSubtree(null);
				break;
			}
			case "Call":
			{
				System.out.println("<Call : "+subTree.getIzq().getLexema());
				codigo+="\t CALL "+subTree.getIzq().getLexema()+"F \n";
				subTree.reemplazarSubtree(subTree.getIzq());
				break;
			}
			case "CASE":
			{	
				if (nodoI == null){
					subTree.reemplazarSubtree(null);
				}else{
						while (nodoI.getIzq()!=null){
							nodoI = nodoI.getIzq();
						}
						left = dameValoresOids(nodoI.getTableRec());
						left = get_id_VarDouble(left);
						
						if (trD.getType().equals("usinteger")){
							codigo+=instrucciones.usintComparador(left, rigth);
						}
						if (trD.getType().equals("double")){
							//left = get_id_VarDouble(left);
							codigo+=instrucciones.doubleComparador(left, rigth);
						}
						String label = dameNextLabel();
						codigo+="\t JNE "+label+"\n";
						labelsStack.push(label);
						codigo+=generarAssemblerNodo(nodoI.getDer());
						codigo+=labelsStack.pop()+": \n";
						
						nodoI.reemplazarSubtree(null);
					}
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
					if (tr.getLexema().contains("@cte"))
						auxiliares +=tr.getLexema() + "\t" + "dw " + tr.getLexema().substring(4)+"\n";
					else
						auxiliares +=tr.getLexema() + "\t" + "dw ?" + "\n";
				}
				if ((tr.getType() != null) && (tr.getType().equals("double"))){
					if (tr.getLexema().contains("@cte"))
						auxiliares += tr.getLexema() + "\t" + "dq " + tr.getLexema().replace("$n", "-").replace("$p",".").substring(4)+"\n";
					else
						auxiliares +=tr.getLexema() + "\t" + "dq ?" + "\n";
				}
			}
		}
		return auxiliares;		
	}
	
	private String get_id_VarDouble(String id_var) {
		String aux="";
		{
			if ((id_var.contains("_") && !(id_var.contains("_ui")) || id_var.contains("@")))
				return id_var;
			else{
				TableRecord auxTR=null;
				if (id_var.contains(".")){
					aux = "@cte" + id_var.replace(".", "$p").replace("+", "").replace("-", "$n");
					auxTR = new TableRecord(aux, lexico.CTE);
					auxTR.setType("double");
					auxTR.setIdToken(lexico.ID);
				}
				else if (id_var.contains("_ui")){
					aux = "@cte" + id_var.replace("_ui", "");
					auxTR = new TableRecord(aux, lexico.CTE);
					auxTR.setType("usinteger");
					auxTR.setIdToken(lexico.ID);
				}
				tablaSimbAux.put(aux, auxTR);
			}
		}
		return aux;
	}
	/** dado un tr devuelve
	 * 		si es CTE --> valor 
	 * 		si es ID  --> lexema
	 * **/
	private String dameValoresOids(TableRecord tr){
		int idtoken = tr.getIdToken();
		
		if (idtoken == lexico.CTE){
			if (tr.getType().equals("double"))
				return tr.getValue();
			else
				return tr.getLexema();  // si es usint 3_ui, 
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
