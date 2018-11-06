%{
	package sintacticSource;
	import lexicalSource.*;
	import java.util.ArrayList;
	import sintacticSource.Error;
	import semanticSource.SemanticAction;
	import semanticSource.CheckRangeAction;
	import java.util.Enumeration;
	import java.util.HashSet;
	import java.util.Vector;
%}

%token
		ID
		CTE
		CADENA
		ASIGNACION
		/* Palabras reservadas */
		IF
		THEN
		ELSE
		END_IF
		BEGIN
		END
		USINTEGER
		DOUBLE
		LONG 
		INTEGER
		LINTEGER
		USLINTEGER
		SINGLE
		WHILE
		CASE 
		DO
		READONLY 
		WRITE
		PASS
		RETURN //*
		PRINT
		
		/* Comparadores */
		MAYORIGUAL
		MENORIGUAL
		IGUAL
		DISTINTO
		EOF
%%
/*-------------------------------------- reglas gramaticales ------------------------------------*/	

programa:	list_sentencias {
								System.out.println("TERMINO GRAMATICA");
								this.raiz = (Nodo)$1.obj;
								raiz.imprimirNodo();							
							}
		;

list_sentencias:   sent_declarativa 
				 | sent_ejecutable {
				 						Nodo nuevo = new Nodo("S",(Nodo)$1.obj, null);
		 								if (raiz == null){
					 						raiz = nuevo;
					 						nuevo.setPadre(null);
		 								}
		 								$$.obj = nuevo;
				 				   }	
				 ;		
				 |  list_sentencias sent_declarativa
				 |  list_sentencias sent_ejecutable{	
				 									Nodo nuevo = new Nodo("S", (Nodo)$2.obj, null);
				 									
					 								if (raiz == null){
					 									raiz = nuevo;
					 									nuevo.setPadre(null);
					 								}else{						 										
				 											((Nodo)$1.obj).setProximaSentencia(nuevo);
				 											nuevo.setPadre((Nodo)$1.obj);
					 									 }
					 								$$.obj =nuevo;
					 								}
				 ;
			  
sent_declarativa	:	declaracion_variable ',' 
					|	declaracion_funcion ',' {funciones.add((Nodo)$1.obj);}
					;
					
declaracion_variable	:	tipo list_variables {//System.out.println("Declaracion variable");
												 setRegla(((Token)$1.obj).getNroLine(), "Declaracion de variables", ((Token)$1.obj).getLexema());
												 updateTable(((Vector<Token>)$2.obj), ((Token)$1.obj).getLexema(), "Identificador de variable",ambito.get(ambito.size()-1));
												 System.out.println("lal");
												//setAmbito(((Vector<Token>)$2.obj), ambito.getLast());												 
												 }
						| tipo list_variables error {
														Vector<Token> tokens = (Vector<Token>)$2.obj;
														if (tokens.size()>1){
															this.addError("Error sintactico en declaración múltiple.", ((Token)$1.obj).getNroLine());
														}else{
															this.addError("Error sintactico en declaración.", ((Token)$1.obj).getNroLine());
														}
													}
						;

encabezado : tipo ID '(' tipo ID ')'	{	
									  		//System.out.println("Encabezado ambito: "+ambit+" - "+ambito.get(ambito.size()-1));
									  		
									  		String ambit = ambito.get(ambito.size()-1);
											Vector<Token> vec = new Vector<Token>(); 
									  		vec.add((Token)$2.obj);
									  		updateTable(vec, ((Token)$1.obj).getLexema(), FUNCION,ambit);	
									  		ambit = ((Token)$2.obj).getLexema();
											apilarAmbito(ambit);
									  		vec.removeAllElements();
									  		vec.add((Token)$5.obj);
									  		updateTable(vec, ((Token)$4.obj).getLexema(), PARAMETRO,ambit);
									  		Nodo nuevo = new Nodo(((Token)$2.obj).getLexema(),null,null);
									  		$$.obj = nuevo;
									  		
										}

		   ;

declaracion_funcion	: encabezado '{' list_sentencias
									 RETURN '(' expresion ')'
								 '}' {	
								 		System.out.println("wepa "+((Nodo)$3.obj).getLexema());
								  		Nodo padre = ((Nodo)$3.obj).getFuncionPadre();
								  		System.out.println("La primera del padre es "+padre.getLexema()+" -> "+(padre.getIzq().getLexema()+(padre.getIzq()).getDer().getLexema()));
								  		System.out.println();
								  		Nodo nuevo = ((Nodo)$1.obj);
								  		nuevo.setIzq(padre);					  		
								  		/*lo siguiente es para evitar que la raiz apunte a la primera sentencia de la funcion*/
								  		if (raiz == padre){
								  			System.out.println("ENTRO");
								  			raiz = null;
								  		}
					  		
					  					$$.obj = nuevo;
					  					
					  					desapilarAmbito();
								 	 }
								 
/*								   
declaracion_funcion	: tipo ID '(' tipo ID')' '{'
					  list_sentencias 
					  RETURN '(' expresion ')'
					  '}' {
					  		//setRegla(((Token)$1.obj).getNroLine(), "Declaracion de funcion ", ((Token)$1.obj).getLexema()+" "+((Token)$2.obj).getLexema());
					  		Vector<Token> vec = new Vector<Token>(); 
					  		vec.add((Token)$2.obj);
					  		updateTable(vec, ((Token)$1.obj).getLexema(), "Identificador de funcion");
					  		vec.add((Token)$5.obj);
					  		vec.removeAllElements();
					  		vec.add((Token)$5.obj);
					  		updateTable(vec, ((Token)$4.obj).getLexema(), "ID funcion");
					  		//
					  		//updateTable(new Vector<Token>((Token)$5.obj)), ((Token)$4.obj).getLexema(), "Identificador del parametro de la funcion");
					  		System.out.println("La primera de la func es "+((Nodo)$8.obj).getLexema()+" -> "+((Nodo)$8.obj).getIzq().getLexema()+(((Nodo)$8.obj).getIzq()).getDer().getLexema());
					  		Nodo padre = ((Nodo)$8.obj).getFuncionPadre();
					  		System.out.println("La primera del padre es "+padre.getLexema()+" -> "+(padre.getIzq().getLexema()+(padre.getIzq()).getDer().getLexema()));
					  		Nodo nuevo = new Nodo(((Token)$2.obj).getLexema(),padre,null);					  		
					  		///lo siguiente es para evitar que la raiz apunte a la primera sentencia de la funcion
					  		if (raiz == padre){
					  			System.out.println("ENTRO");
					  			raiz = null;
					  		}
					  		
					  		$$.obj = nuevo;
					  	  } 
					
					| tipo ID '(' tipo ID')' '{'
					  list_sentencias // conjunto de sentencias declarativas y ejecutables
					  '}' {this.addError("Error sintactico: falta return en la declaracion de la funcion ", ((Token)$1.obj).getNroLine());}
					;

*/					  
list_variables		:	list_variables ';' ID  {
											Vector<Token> tokens = (Vector<Token>)$1.obj;
											Token token = (Token)$3.obj;
											tokens.add(token);
											$$.obj = tokens;
											
											}
					|	ID  {
							Vector<Token> tokens = new Vector<Token>();
							Token token = (Token)$1.obj;
							tokens.add(token);
							$$.obj = tokens;
							}
					;

tipo	:	USINTEGER
		|	DOUBLE
		|	LONG {this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)$1.obj).getNroLine());}
		|	USLINTEGER {this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)$1.obj).getNroLine());}
		|	LINTEGER {this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)$1.obj).getNroLine());}
		|	SINGLE {this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)$1.obj).getNroLine());}
		|	INTEGER {this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)$1.obj).getNroLine());}
		;
			

sent_ejecutable  : sent_seleccion ',' {$$.obj = (Nodo)$1.obj;}
				 | sent_control ','
				 | imprimir ','
				 | asignacion ',' {$$.obj = (Nodo)$1.obj;} 
				// | invocacion ','
				 ;
				 
invocacion	:	ID '(' nombre_parametro ';' lista_permisos')'{
																setRegla(((Token)$1.obj).getNroLine(), "Invocacion", ((Token)$1.obj).getLexema());
															 }
			|	ID nombre_parametro ';' lista_permisos')'  {
																	addError("Error sintactico: falta '(' al inicio de la invocacion ", ((Token)$1.obj).getNroLine());
															 	}
															 	
			|	ID '('nombre_parametro ';' lista_permisos  {
																addError("Error sintactico: falta ')' al final de la invocacion ", ((Token)$1.obj).getNroLine());
															}
			;

nombre_parametro :	ID
				 |  CTE
				 ;

lista_permisos	: READONLY
				| WRITE
				| PASS
				| WRITE ';' PASS
				;

sent_control	: CASE '(' ID ')' bloque_control  				
										 {System.out.println("Case do");
				  						  setRegla(((Token)$1.obj).getNroLine(), "Sentencia de control", ((Token)$1.obj).getLexema());												 
										 }
				| CASE '(' error ')' bloque_control  				
										 {
				  						  addError("Error sintactico: condicion erronea ", ((Token)$2.obj).getNroLine());												 
										 }
				;

bloque_control 	: '{' linea_control '}'
				|  linea_control '}' error {addError("Error sintactico: falta '{' para iniciar el bloque de sentencias de control ", ((Token)$1.obj).getNroLine());}
				| '{' linea_control error {addError("Error sintactico: falta '}' para terminar el bloque de sentencias de control ", ((Token)$3.obj).getNroLine());}
				;
				
linea_control	: 	CTE ':' DO bloque_sin_declaracion','
				|	linea_control CTE ':' DO bloque_sin_declaracion','
				//| 	CTE ':' DO bloque_sin_declaracion error {addError("Error sintactico: los bloques de las sentencias de control deben estar separados con ',' ", ((Token)$5.obj).getNroLine());}
				| 	CTE DO bloque_sin_declaracion ',' {addError("Error sintactico: falta ':' antes del 'do'", ((Token)$1.obj).getNroLine());}
				| 	CTE ':' bloque_sin_declaracion ','{addError("Error sintactico: falta 'do' despues del ':'", ((Token)$1.obj).getNroLine());}
				;
				
sent_seleccion : sent_if END_IF {
									//Nodo nuevo = new Nodo("IF",);
									$$.obj = (Nodo)$1.obj;
								}
	
			   | sent_if ELSE bloque_sin_declaracion END_IF{
			   													setRegla(((Token)$2.obj).getNroLine(), "Sentencia de Control", "else");
			   													//Nodo = new Nodo("IF",(Nodo)$3.obj,(Nodo)$5.obj);
			   													Nodo ifNodo = (Nodo)$1.obj;
			   													Nodo elseNodo = new Nodo("ELSE",(Nodo)$3.obj,null); 
			   													
			   													ifNodo.getDer().setDer(elseNodo);
			   													
			   			  								   }
			   | sent_if error { addError("Error sintactico: Falta palabra reservada 'end_if' luego del bloque ",((Token)$2.obj).getNroLine());}
			   | sent_if END_IF ELSE bloque_sin_declaracion END_IF { addError("Error sintactico: 'else' incorrecto luego del 'end_if' ",((Token)$3.obj).getNroLine());}
			   ;			  									
sent_if :	IF '('expresion_logica')'  
			bloque_sin_declaracion {
			   				  	    	setRegla(((Token)$1.obj).getNroLine(), "Sentencia de Control", "if");
			   				  	    	Nodo thenNodo = new Nodo("THEN",(Nodo)$5.obj,null);
			   				  	    	Nodo cuerpoNodo = new Nodo("Cuerpo",thenNodo,null);
			   				  	    	Nodo nuevo = new Nodo("IF",(Nodo)$3.obj,cuerpoNodo);
			   				  	    	$$.obj = nuevo;
			   			   		   }
		|	IF '(' expresion_logica 
			bloque_sin_declaracion error {
											addError("Falta parentesis de cierre ')'",((Token)$2.obj).getNroLine());
 									     }
 		|	IF expresion_logica ')' 
			bloque_sin_declaracion error {
										  	addError("Falta parentesis de apertura '('",((Token)$2.obj).getNroLine());
 									     }
		|	IF '('expresion_logica ')' 
			error {
				   		addError("Error sintactico en el bloque ",((Token)$2.obj).getNroLine());
 			      }
 		 		
	    ;

bloque_sin_declaracion : '{'list_sentencias_no_declarables'}' {$$.obj = (Nodo)$2.obj;}
					   ;

list_sentencias_no_declarables : list_sentencias_no_declarables sent_ejecutable {
																					((Nodo)$1.obj).setDer((Nodo)$2.obj);	
																				}
								| sent_ejecutable	{
								 						Nodo nuevo = new Nodo("S",(Nodo)$1.obj, null);
								 						$$.obj = nuevo;
								 				 	}
								//| sent_ejecutable error {addError("Error sintactico: falta la coma",((Token)$2.obj).getNroLine());}
								| sent_declarativa error{ 
															addError("Error sintáctico: no se permiten sentencias declarativas dentro de un bloque de control ",((Token)$1.obj).getNroLine());
														}
								;
								


expresion_logica : expresion comparador expresion { 
														setRegla(((Token)$1.obj).getNroLine(), "expresion logica", ((Nodo)$2.obj).getLexema());
														Nodo comparador = new Nodo(((Nodo)$2.obj).getLexema(),((Token)$1.obj).getNodo(),((Token)$3.obj).getNodo());	
														Nodo nuevo = new Nodo("Condicion",comparador,null);
														$$.obj = nuevo;
												  }
				 		
				|expresion error expresion  	{
													addError("Errorsintactico: Comparador invalido. ", ((Token)$1.obj).getNroLine());
												}
				| expresion comparador error	{
													addError("Error sintactico: Expresion derecha invalida ", ((Token)$1.obj).getNroLine());
												}	
				| error comparador expresion	{
													addError("Error sintactico: Expresion izquierda invalida ", ((Token)$1.obj).getNroLine());
												}
				 ;

comparador : MAYORIGUAL {
						 Nodo nuevo = new Nodo(">=");
						 $$.obj = nuevo;
						}
		   | MENORIGUAL {	
						Nodo nuevo = new Nodo("<=");
						$$.obj = nuevo;
		   				}
		   | IGUAL {	
		   				Nodo nuevo = new Nodo("==");
		   				$$.obj = nuevo;	
		   		   }
		   | DISTINTO {	
		   				Nodo nuevo = new Nodo("!=");
		   				$$.obj = nuevo;
		   			  }
		   | '>' {	
		   			Nodo nuevo = new Nodo(">");
		   			$$.obj = nuevo;
		   		}
		   | '<' {	
		   			Nodo nuevo = new Nodo("<");
		   			$$.obj = nuevo;
		   		}
		   ;


expresion : expresion '+' termino{
									if (datosCompatibles(((Token)$1.obj).getRecord().getType(),((Token)$3.obj).getRecord().getType())){
		   								Nodo nuevo = new Nodo ("+",((Token)$1.obj).getNodo(),((Token)$3.obj).getNodo());
		   								$$.obj = new Token(0, ((Token)$1.obj).getLexema() + "+" +((Token)$3.obj).getLexema(), ((Token)$1.obj).getNroLine(), "", null,nuevo);
		   							}else{
	   									addError("Error semántico: Los tipos de datos de la operacion + no coinciden. ", ((Token)$1.obj).getNroLine());
	   								}
								 } 
		  | expresion '-' termino{
		  							if (datosCompatibles(((Token)$1.obj).getRecord().getType(),((Token)$3.obj).getRecord().getType())){
										Nodo nuevo = new Nodo ("-",(Nodo)$1.obj,(Nodo)$3.obj);
		   								$$.obj = new Token(0, ((Token)$1.obj).getLexema() + "-" +((Token)$3.obj).getLexema(), ((Token)$1.obj).getNroLine(), "", null,nuevo);
		   							}else{
	   									addError("Error semántico: Los tipos de datos de la operacion - no coinciden. ", ((Token)$1.obj).getNroLine());
	   								}
								 }
		  | termino	{
		  				$$.obj = (Token)$1.obj;
		  			}
		  ;

termino : termino '*' factor{	
							
								if (datosCompatibles(((Token)$1.obj).getRecord().getType(),((Token)$3.obj).getRecord().getType())){
									Nodo nuevo = new Nodo ("*",((Token)$1.obj).getNodo(),((Token)$3.obj).getNodo());
		   							$$.obj = new Token(0, ((Token)$1.obj).getLexema() + "*" +((Token)$3.obj).getLexema(), ((Token)$1.obj).getNroLine(), "", null,nuevo);
	   							}else{
	   								addError("Error semántico: Los tipos de datos de la operacion * no coinciden. ", ((Token)$1.obj).getNroLine());
	   							}
							}
		| termino '/' factor{
								if (datosCompatibles(((Token)$1.obj).getRecord().getType(),((Token)$3.obj).getRecord().getType())){
									Nodo nuevo = new Nodo ("/",((Token)$1.obj).getNodo(),((Token)$3.obj).getNodo());
		   							$$.obj = new Token(0, ((Token)$1.obj).getLexema() + "/" +((Token)$3.obj).getLexema(), ((Token)$1.obj).getNroLine(), "", null,nuevo);
		   						}else{
	   								addError("Error semántico: Los tipo de datos de la operacion / no coinciden. ", ((Token)$1.obj).getNroLine());
	   							}
							}
		| factor	{
						$$.obj = (Token)$1.obj;
					}
					
		
		;

imprimir	:	PRINT '('CADENA')'
				{setRegla(((Token)$1.obj).getNroLine(), "Impresion",((Token)$1.obj).getLexema()+"("+((Token)$3.obj).getLexema()+")" ) ;}
			| 	PRINT '(' error ')' {addError("Error sintactico: el contenido de impresion debe ser una cadena. ", ((Token)$1.obj).getNroLine());}
			;
			
asignacion 	:	ID ASIGNACION expresion {	
											if (isDeclarated((Token)$1.obj)){
											System.out.println("THEN");	
												setRegla(((Token)$1.obj).getNroLine(), "Asignacion", ((Token)$1.obj).getLexema()+":="+((Token)$3.obj).getLexema());
												Nodo nodoId = new Nodo(table.get(((Token)$1.obj).getLexema()));
												Nodo nuevo = new Nodo(":=", nodoId, ((Token)$3.obj).getNodo());
												$$.obj = nuevo;
												registrarEscritura(((Token)$1.obj).getLexema());																							
											}
											else{
												System.out.println(((Token)$1.obj).getLexema()+ "  no esta declarada");
											}
										}
			|	ID ASIGNACION error {
							System.out.println("ERROR"); 
							addError("Asignacion erronea ", ((Token)$1.obj).getNroLine());
							
							//FIXME arreglar esto, tendria que devolver un nodo para que no me tire error.
						 }
			;

factor : CTE 	{	
					Nodo nuevo = new Nodo(table.get(((Token)$1.obj).getLexema()));
					((Token)$1.obj).setNodo(nuevo);
	   			}
	   | '-' factor {
	   				System.out.println("Un negative "+((Token)$2.obj).getRecord().getType());
	   				if (((Token)$2.obj).getRecord().getType() == "usinteger"){
	   					this.addError("Error sintactico: usinteger no puede ser negativio ",((Token)val_peek(0).obj).getNroLine());
	   					//$$.obj = error;
	   				}else{
	   					updateTableNegative(   ((Token)$2.obj).getLexema()   );
	   					Nodo nuevo = new Nodo(table.get("-"+((Token)$2.obj).getLexema()));
						$$.obj = new Token(0, "-"+((Token)$2.obj).getLexema(), ((Token)$1.obj).getNroLine(), "", null,nuevo);
	   				}

	   			 }
	   
	   | ID  { 
	   			isDeclarated((Token)$1.obj);
	   			Nodo nuevo = new Nodo(table.get(((Token)$1.obj).getLexema()));
	   			System.out.println(table.get(((Token)$1.obj).getLexema()).getLexema());
	   			((Token)$1.obj).setNodo(nuevo);
	   		 }
	   
		| invocacion ','{
							$$.obj = (Token)$1.obj;
					 	}
	 	
	   ;

//bloque_de_sentencias : '{'list_sentencias'}'
//					   ;
%%
/*******************************************************************************************************/
private static final String FUNCION = "ID funcion";
private static final String PARAMETRO = "ID parametro";
private static final String IDENTIFICADOR = "IDENTIFICADOR";
ArrayList<String> ambito = new ArrayList<String>();
LexicalAnalizer lexico;
Table table;
Nodo raiz = null;
Nodo actual = null;
ArrayList<Nodo> funciones = new ArrayList<Nodo>();
HashSet<String> alcanceActual = new HashSet<String>();
public ArrayList<SintacticStructure> structures = new ArrayList<SintacticStructure>();
public ArrayList<Error> errors = new ArrayList<Error>();
public ArbolSintactico arbol = new ArbolSintactico();

private void apilarAmbito(String ambito){
	this.ambito.add(ambito);
}

private void desapilarAmbito(){
	int pos = this.ambito.size()-1;
	if (pos >= 0){
		this.ambito.remove(pos);
	}
}

public Parser(String programa, Table table) {
    lexico = new LexicalAnalizer(programa, table);
	this.table = table;
	this.ambito.add("main");
}

public ArbolSintactico getArbol(){
	return arbol;
}
public ArrayList<Nodo> getFunciones(){
	return funciones;
}

public Nodo getRaiz(){
	return raiz;
}
private int yylex() {
	Token token=lexico.getToken();

	if (token!=null){
	    yylval = new ParserVal(token);
	    return token.getId();
	}

	return 0;
}


public int Parse(){
	return yyparse();
}

public LexicalAnalizer getAnalizer(){
	return lexico;
}

public void setRegla(int line, String type, String desc){
	SintacticStructure structure  = new SintacticStructure(line,type,desc);
	this.structures.add(structure);

}

public void yyerror(String errormsg){
	//addError(errormsg);
	//this.addError("Error Sintactico: "+ ((Token)yylval.obj).getLexema()+" ", this.lexico.getLine());
}

public void addError(String e, int line){
	this.errors.add(new Error(e,line));	
}

public ArrayList<Error> getErrors(){
	return errors;
}

public ArrayList<SintacticStructure> getReglas(){
	return structures;
}
public LexicalAnalizer getLexical(){
	return lexico;
}

public void updateTable(Vector<Token> tokens, String type, String uso, String ambito){  //type double o usinteger, tokens son identificadores
	/*setea el tipo del _id en la tabla de simbolos*/
	Enumeration e = tokens.elements();
	while (e.hasMoreElements()){
		Token token = (Token)e.nextElement();
		TableRecord tr = token.getRecord();
		String lexema = tr.getLexema();
		//System.out.println("-- "+(table.get(lexema).getUso()));
		
			 if (table.containsLexema(lexema)){
			 
				//System.out.println("esta en la tabla");
	
				if  ((table.get(lexema).getType()!=IDENTIFICADOR)){
					addError("Error sintactico: la variable ya fue declarada ",token.getNroLine());
				}
				else{
						//TableRecord ntr = token.getRecord();
						(table.get(lexema,uso)).setType(type);
						(table.get(lexema)).setUso(uso);
						(table.get(lexema)).setAmbito(ambito);
				}
			}
			else{
				System.out.println("No esta asignado en la tabla");				
			}
	}
}

public void setAmbito(Vector<Token> tokens, String ambito){
	

}

public TableRecord updateTableNegative(String key ){//key: 2.0
	{	TableRecord tr = (TableRecord)table.get(key); //tomo el el tr de la tabla de simbolos
		String newLexema = "-"+ key;
		System.out.println("NEWKEY: "+newLexema);
		//FIXME supongo que e ya se que es double 
		SemanticAction as = new CheckRangeAction(lexico,LexicalAnalizer.minNega, LexicalAnalizer.maxDou);
		if (!as.execute(newLexema, ' ')) { //Si no cumple rango retorno null
			return null;
		}
		//si cumplo rango de doble
		
		if (tr.getRef() == 1){ //Si tengo una unica referencia en la TS del 2.0
			if (table.contains(newLexema)){// existe el -2.0
				table.remove(key); // saco el que pense que era positivo 2.0
				TableRecord newr = (TableRecord)table.get(newLexema); 
				newr.increment(); // lo incremento al -2.0
				return newr;
			}
			else{//si !existe el -2.0
				TableRecord newRecord = new TableRecord(newLexema , CTE);
				String value = tr.getValue();
				newRecord.setValue("-"+value);
				table.put(newRecord.getLexema(), newRecord);// agrego el -2.0
				newRecord.setType(tr.getType());
				table.remove(key); //saco el 2.0
				return newRecord;
				}

		}
		else { //(existe mas de una ref al 2.0)
			tr.decrement(); //le resto una ref al positivo
			if (table.contains(newLexema)){
				TableRecord newr = (TableRecord)table.get(newLexema);
				newr.increment();
				return newr;
			}
			else{
				TableRecord newRecord = new TableRecord(newLexema,CTE);
				String value = tr.getValue();
				newRecord.setValue("-"+value);
				table.put(newRecord.getLexema(), newRecord);
				newRecord.setType(tr.getType());
				return newRecord;
			}
		}
	}	
}

public boolean isDeclarated(Token id){
	if (table.get(id.getLexema()).getType() == IDENTIFICADOR )
        {
            this.addError("Error sintactico: Variable "+id.getLexema()+" no declarada.", id.getNroLine());
            this.table.remove(id.getRecord().getLexema());
            return false;
        }
        else{
        	table.get(id.getLexema()).increment();
        	return true;
        }
}

private boolean datosCompatibles(String type, String type2) {
	if (type == type2){
	return true;
	}
	return false;
}

/*
public void registrarUso(String id, String accion){
	TableRecord tr = table.get(id);
	if (tr.getUso() == PARAMETRO){
		String ambito = tr.getAmbito(); 
		switch(accion){
			case PASS: {table.get(ambito)};
			case WRITE:{}; 
			
		}
	}
}
*/
public void registrarEscritura(String id){
	TableRecord tr = table.get(id);
	if (tr.getUso() == PARAMETRO){
		String ambito = tr.getAmbito();
		TableRecord funcionTR = table.get(ambito);
		System.out.println("escritado en "+ambito);
		funcionTR.setWritten(true);
	}
}


public void registrarPasaje(String id){
	TableRecord tr = table.get(id);
	if (tr.getUso() == PARAMETRO){
		String ambito = tr.getAmbito();
		TableRecord funcionTR = table.get(ambito);
		System.out.println("pasado en "+ambito);
		funcionTR.setPassed(true);
	}
}


