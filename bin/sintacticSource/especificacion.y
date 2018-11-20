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

list_sentencias:   sent_declarativa {$$.obj = new Nodo("null",null,null);}
				 | sent_ejecutable {
				 						Nodo nuevo = new Nodo("S",(Nodo)$1.obj, null);
				 						((Nodo)$1.obj).setPadre(nuevo);
		 								if (raiz == null){
					 						raiz = nuevo;
					 						nuevo.setPadre(null);
		 								}
		 								$$.obj = nuevo;
				 				   }	
				//| error {	this.addError("Error en la sentencia: ", ((Token)$1.obj).getNroLine());}
				 //| list_sentencias error {	this.addError("Error en la sentencia: ", ((Token)$2.obj).getNroLine());}				 		
				 | list_sentencias sent_declarativa{$$.obj =  (Nodo)$1.obj;}
				 | list_sentencias sent_ejecutable{	
				 									Nodo nuevo = new Nodo("S", (Nodo)$2.obj, null);
				 									((Nodo)$2.obj).setPadre(nuevo);
				 									System.out.println(((Nodo)$2.obj).getLexema());
				 									
					 								if (raiz == null){
					 									System.out.println("si");
					 									raiz = nuevo;
					 									nuevo.setPadre(null);
					 									((Nodo)$2.obj).setPadre(nuevo);
					 									
					 								}else{	
					 									if (((Nodo)$1.obj).getLexema().equals("null")){		
					 										System.out.println("listapadre null");			 										
					 										((Nodo)$1.obj).setLexema("S");
				 											((Nodo)$1.obj).setIzq((Nodo)$2.obj);
				 											((Nodo)$1.obj).setDer(null);
				 											((Nodo)$2.obj).setPadre((Nodo)$1.obj); 
				 											
				 										}else{
				 											System.out.println("listapadre != null");
				 											((Nodo)$1.obj).setProximaSentencia(nuevo);
					 										//nuevo.setPadre((Nodo)$1.obj);	 YA LO HAGO EN LA SENT ANTERIOR
				 										}
					 								}
					 								$$.obj =nuevo;
					 								}
				 ;
			  
sent_declarativa	:	declaracion_variable ',' 
					|	declaracion_funcion ',' {
													if (!((Nodo)$1.obj).equals(null)){
														funciones.add((Nodo)$1.obj);
													}
												}
					|  declaracion_variable  
					;
					
declaracion_variable	:	tipo list_variables {
												 setRegla(((Token)$1.obj).getNroLine(), "Declaracion de variables", ((Token)$1.obj).getLexema());
												 updateTable(((Vector<Token>)$2.obj), ((Token)$1.obj).getLexema(), "Identificador de variable",ambito.get(ambito.size()-1));
												}
						| tipo list_variables error {
														Vector<Token> tokens = (Vector<Token>)$2.obj;
														if (tokens.size()>1){
															this.addError("Error sintactico en declaraci�n m�ltiple.", ((Token)$1.obj).getNroLine());
														}else{
															this.addError("Error sintactico en declaraci�n.", ((Token)$1.obj).getNroLine());
														}
													}
						;

encabezado : tipo ID '(' tipo ID ')'	{	
										  	String ambit = ambito.get(ambito.size()-1);
									  		//System.out.println("Encabezado ambito: "+ambit+" - "+ambito.get(ambito.size()-1));
									  		if (ambit.equals("main")){
									  			//System.out.println("declarar ");
												Vector<Token> vec = new Vector<Token>(); 
										  		vec.add((Token)$2.obj);
										  		updateTable(vec, ((Token)$1.obj).getLexema(), FUNCION,ambit);	
										  		ambit = ((Token)$2.obj).getLexema();
												apilarAmbito(ambit);
										  		vec.removeAllElements();
										  		vec.add((Token)$5.obj);
										  		updateTable(vec, ((Token)$4.obj).getLexema(), PARAMETRO,ambit);
										  		Nodo nuevo = new Nodo(((Token)$2.obj).getLexema(),null,null);
										  		nuevo.setTableRec(table.get(((Token)$2.obj).getLexema()));
										  		$$.obj = nuevo;
										  	}else{
										  		System.out.println("en "+ambit+"Error semantico: no se puede declarar "+((Token)$2.obj).getLexema());
										  		this.addError("Error semantico: no se puede declarar funcion dentro de otra ", ((Token)$2.obj).getNroLine());
										  		//FIXME
										  		//Error error = new Error("Error semantico: no se puede declarar funcion dentro de otra ",((Token)$5.obj).getNroLine());
										  		$$.obj = new Nodo("null",null,null);
										  	}
									  		
										}

		   ;

declaracion_funcion	: encabezado '{' list_sentencias
									 RETURN '(' expresion ')'
								 '}' {	
								 //TODO fichar los tipos de retorno coincidan con los del encabezado
								 		//System.out.println("wepa "+((Nodo)$3.obj).getLexema());
								  		Nodo padre = ((Nodo)$3.obj).getFuncionPadre();
								  		//System.out.println("La primera del padre es "+padre.getLexema()+" -> "+(padre.getIzq().getLexema()+(padre.getIzq()).getDer().getLexema()));
								  		Nodo nuevo = ((Nodo)$1.obj);
								  		padre.setPadre(nuevo);
								  		nuevo.setIzq(padre);		
								  		//agrego retorno 
								  		Nodo nodoIzqRetorno = new Nodo("_RET"+((Nodo)$1.obj).getLexema(), null, null);
								  		Nodo nodoDerRetorno = ((Token)$6.obj).getNodo();
								  		Nodo nodoRetorno = new Nodo(":=",nodoIzqRetorno, nodoDerRetorno);
								  		nodoIzqRetorno.setPadre(nodoRetorno);
								  		nodoIzqRetorno.setTableRec(((Nodo)$1.obj).getTableRec());
								  		nodoDerRetorno.setPadre(nodoRetorno);		
								  		padre.setProximaSentencia(nodoRetorno);  		
								  		/*lo siguiente es para evitar que la raiz apunte a la primera sentencia de la funcion*/
								  		if (raiz == padre){
								  			System.out.println("ENTRO");
								  			raiz = null;
								  		}
					  		
					  					$$.obj = nuevo;
					  					
					  					desapilarAmbito();
								 	 }
					| encabezado '{'
					  list_sentencias // conjunto de sentencias declarativas y ejecutables
					  '}' 	{
					  			System.out.println("rmpo");
					  			this.addError("Error sintactico: falta return en la declaracion de la funcion ", ((Token)$4.obj).getNroLine());
					  		}
			
					;
								 
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
			

sent_ejecutable  : sent_seleccion ',' {$$.obj = ((Token)$1.obj).getNodo();}
				 | sent_control ','
				 | imprimir ','	{$$.obj = ((Token)$1.obj).getNodo();}
				 | asignacion ',' {$$.obj = ((Token)$1.obj).getNodo();} 
				 
				 ;
				 
invocacion	:	ID '(' ID ';' lista_permisos')' { 	if (isFunDeclarated(((Token)$1.obj).getLexema())){
														//TODO chequear ambito de $2.obj !
														//System.out.println("Declatada la funcion");
														System.out.println("amito actual "+ambito.get(ambito.size()-1));
														if (checkAmbito(((Token)$1.obj).getLexema())){
															if (checkPermisos( ((Token)$1.obj).getLexema() , ((String)$5.obj))){
																setRegla(((Token)$1.obj).getNroLine(), "Invocacion", ((Token)$1.obj).getLexema());
																Nodo nodoFun = new Nodo(((Token)$1.obj).getLexema(),null,null);
																TableRecord tr = table.get(((Token)$1.obj).getLexema());
																nodoFun.setTableRec(tr);
																Nodo nodoCall = new Nodo("Call",nodoFun,null);
																
																$$.obj = new Token(0, ((Token)$1.obj).getLexema()+"()", ((Token)$1.obj).getNroLine(), tr.getType(), null,nodoCall);
															}else{
																addError("Error semantico: la funcion "+((Token)$1.obj).getLexema()+" no permite el pasaje de parametros por "+(String)$5.obj, ((Token)$1.obj).getNroLine());
																System.out.println("No cumple chqueo de permisos");
															}
														}else{
															//no esta en el ambito correspondiente
														}
													}else{
														System.out.println("FUNC NO DECLARADA");
														addError("Error semantico: la funcion '"+((Token)$1.obj).getLexema()+"' no esta declarada", ((Token)$1.obj).getNroLine());
													}
												 }
/*															 
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
*/
lista_permisos	: READONLY  {$$.obj = "READONLY";}
				| WRITE	{$$.obj = "WRITE";}
				| PASS	{$$.obj = "PASS";}
				| WRITE ';' PASS	{$$.obj = "WRITE;PASS";}
				;

sent_control	: CASE '(' ID ')' bloque_control  				
										 {System.out.println("Case do");
										 //TODO chequear ambito de $3.obj, 
							
				  						  setRegla(((Token)$1.obj).getNroLine(), "Sentencia de control", ((Token)$1.obj).getLexema());
				  						  TableRecord trID = table.get(((Token)$3.obj).getLexema());
				  						  Nodo nodoID = new Nodo(trID.getLexema(),null,null);
				  						  nodoID.setTableRec(trID);
				  						  Nodo nodo = new Nodo("CASE",(Nodo)$5.obj,nodoID);
				  						  nodoID.setPadre(nodo);
				  						  ((Nodo)$5.obj).setPadre(nodo);
				  						  $$.obj = nodo;												 
										 }
				| CASE '(' error ')' bloque_control  				
										 {
										  Nodo nodo = new Nodo("error",null,null);
				  						  $$.obj = nodo;									
										  System.out.println("metiste cualquier en el case");
				  						  addError("Error semantico: la codndicion del case debe ser una variable", ((Token)$2.obj).getNroLine());												 
										 }
				;

bloque_control 	: '{' linea_control '}' {$$.obj = ((Nodo)$2.obj);}
				|  linea_control '}' error {addError("Error sintactico: falta '{' para iniciar el bloque de sentencias de control ", ((Token)$1.obj).getNroLine());}
				| '{' linea_control error {addError("Error sintactico: falta '}' para terminar el bloque de sentencias de control ", ((Token)$3.obj).getNroLine());}
				;
				
linea_control	: 	CTE ':' DO bloque_sin_declaracion','   	{	
																Nodo padre = ((Nodo)$4.obj).getFuncionPadre();
																/*lo siguiente es para evitar que la raiz apunte a la primera sentencia de la funcion*/
														  		if (raiz == padre){
														  			System.out.println("ENTRO");
														  			raiz = null;
														  		}
																Nodo nodoLctrl = new Nodo(((Token)$1.obj).getLexema(),null,((Nodo)$4.obj));
																nodoLctrl.setTableRec( table.get(((Token)$1.obj).getLexema() ));
																padre.setPadre(nodoLctrl);
																$$.obj = nodoLctrl;				
															}
				| 	CTE ':' DO bloque_sin_declaracion',' linea_control	{	
																		Nodo padre = ((Nodo)$4.obj).getFuncionPadre();
																		/*lo siguiente es para evitar que la raiz apunte a la primera sentencia de la funcion*/
																  		if (raiz == padre){
																  			System.out.println("ENTRO");
																  			raiz = null;
																  		}
																			Nodo nodoLctrl = new Nodo(((Token)$1.obj).getLexema(),((Nodo)$6.obj),((Nodo)$4.obj));
																			nodoLctrl.setTableRec( table.get(((Token)$1.obj).getLexema() ));
																			((Nodo)$6.obj).setPadre(nodoLctrl);
																			((Nodo)$4.obj).setPadre(nodoLctrl);
																			$$.obj = nodoLctrl;
																		}
			
			
			
				//| 	CTE ':' DO bloque_de_sentencias error {addError("Error sintactico: los bloques de las sentencias de control deben estar separados con ',' ", ((Token)$5.obj).getNroLine());}
				| 	CTE DO bloque_sin_declaracion ',' {addError("Error sintactico: falta ':' antes del 'do'", ((Token)$1.obj).getNroLine());}
				| 	CTE ':' bloque_sin_declaracion ','{addError("Error sintactico: falta 'do' despues del ':'", ((Token)$1.obj).getNroLine());}
				;
				
sent_seleccion : sent_if END_IF {
									//$$.obj = ((Token)$1.obj).getNodo();
								}
	
			   | sent_if ELSE bloque_sin_declaracion END_IF{
			   													Nodo padre = ((Nodo)$3.obj).getFuncionPadre();
																/*lo siguiente es para evitar que la raiz apunte a la primera sentencia de la funcion*/
														  		if (raiz == padre){
														  			System.out.println("ENTRO");
														  			raiz = null;
														  		}
			   													setRegla(((Token)$2.obj).getNroLine(), "Sentencia de Control", "else");
			   													Nodo ifNodo = ((Token)$1.obj).getNodo();
			   													Nodo cuerpo = ifNodo.getDer();
			   													Nodo elseNodo = new Nodo("ELSE",(Nodo)$3.obj,null); 
			   													
			   													((Nodo)$3.obj).setPadre(elseNodo);
			   													elseNodo.setPadre(cuerpo);
			   													cuerpo.setDer(elseNodo);
			   													
			   													
			   			  								   }
			   | sent_if error { addError("Error sintactico: Falta palabra reservada 'end_if' luego del bloque ",((Token)$2.obj).getNroLine());}
			   | sent_if END_IF ELSE bloque_sin_declaracion END_IF { addError("Error sintactico: 'else' incorrecto luego del 'end_if' ",((Token)$3.obj).getNroLine());}
			   ;			  									
sent_if :	IF '('expresion_logica')'  	
			bloque_sin_declaracion {	
										Nodo padre = ((Nodo)$5.obj).getFuncionPadre();
										/*lo siguiente es para evitar que la raiz apunte a la primera sentencia de la funcion*/
								  		if (raiz == padre){
								  			System.out.println("ENTRO");
								  			raiz = null;
								  		}
			   				  	    	setRegla(((Token)$1.obj).getNroLine(), "Sentencia de Control", "if");
			   				  	    	Nodo thenNodo = new Nodo("THEN",(Nodo)$5.obj,null);
			   				  	    	Nodo cuerpoNodo = new Nodo("Cuerpo",thenNodo,null);
			   				  	    	Nodo nuevo = new Nodo("IF",(Nodo)$3.obj,cuerpoNodo);
			   				  	    	((Nodo)$5.obj).setPadre(thenNodo);
			   				  	    	((Nodo)$3.obj).setPadre(nuevo);
			   				  	    	thenNodo.setPadre(cuerpoNodo);
			   				  	    	cuerpoNodo.setPadre(nuevo);
										((Token)$1.obj).setNodo(nuevo);
			   				  	    	//$$.obj = nuevo;
			   			   		   }
		|	IF '(' expresion_logica 
			bloque_sin_declaracion error {
											addError("Falta parentesis de cierre ')'",((Token)$2.obj).getNroLine());
											((Token)$1.obj).setNodo(new Nodo("null",null,null));
											$$.obj = ((Token)$1.obj);
 									     }
 		|	IF expresion_logica ')' 
			bloque_sin_declaracion error {
										  	addError("Falta parentesis de apertura '('",((Token)$2.obj).getNroLine());
										  	((Token)$1.obj).setNodo(new Nodo("null",null,null));
											$$.obj = ((Token)$1.obj);
 									     }
		|	IF '('expresion_logica ')' 
			error {
				   		addError("Error sintactico en el bloque ",((Token)$2.obj).getNroLine());
				   		((Token)$1.obj).setNodo(new Nodo("null",null,null));
						$$.obj = ((Token)$1.obj);
 			      }
 		 		
	    ;

bloque_sin_declaracion : '{'list_sentencias_no_declarables'}' {$$.obj = ((Nodo)$2.obj).getFuncionPadre();}
					   ;			  
list_sentencias_no_declarables :    sent_declarativa {
														$$.obj = new Nodo("null",null,null);
														addError("Error semantico: no se permiten sentencias declarativas dentro de un bloque de control ",((Token)$1.obj).getNroLine());
													 }
								| sent_ejecutable{
								 						Nodo nuevo = new Nodo("S",(Nodo)$1.obj, null);
								 						((Nodo)$1.obj).setPadre(nuevo);
						 								if (raiz == null){
									 						raiz = nuevo;
									 						nuevo.setPadre(null);
						 								}
						 								$$.obj = nuevo;
								 				   }
								|	list_sentencias_no_declarables sent_ejecutable {	
												 									Nodo nuevo = new Nodo("S", (Nodo)$2.obj, null);
												 									((Nodo)$2.obj).setPadre(nuevo);
													 								if (raiz == null){
													 									raiz = nuevo;
					 																	nuevo.setPadre(null);
					 																	((Nodo)$2.obj).setPadre(nuevo);
													 									
													 								}else{	
													 									if (((Nodo)$1.obj).getLexema().equals("null")){					 										
													 										((Nodo)$1.obj).setLexema("S");
												 											((Nodo)$1.obj).setIzq((Nodo)$2.obj);
												 											((Nodo)$1.obj).setDer(null); 
												 											((Nodo)$2.obj).setPadre((Nodo)$1.obj);
												 										}else{
												 											((Nodo)$1.obj).setProximaSentencia(nuevo);
													 										//nuevo.setPadre((Nodo)$1.obj);	
												 										}
													 								}
													 								$$.obj =nuevo;
													 								}
				 				| list_sentencias_no_declarables sent_declarativa	{
				 																			$$.obj =  (Nodo)$1.obj;
																							addError("Error semantico: no se permiten sentencias declarativas dentro de un bloque de control ",((Token)$2.obj).getNroLine());
				 																	}
				 				
							
								;
								


expresion_logica : expresion comparador expresion { 
														setRegla(((Token)$1.obj).getNroLine(), "expresion logica", ((Nodo)$2.obj).getLexema());
														Nodo comp1 =((Token)$1.obj).getNodo();
														Nodo comp2 =((Token)$3.obj).getNodo();
														Nodo comparador = new Nodo(((Nodo)$2.obj).getLexema(),((Token)$1.obj).getNodo(),((Token)$3.obj).getNodo());	
														comp1.setPadre(comparador);
														comp2.setPadre(comparador);
														Nodo nuevo = new Nodo("Condicion",comparador,null);
														comparador.setPadre(nuevo);
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
		   				Nodo nuevo = new Nodo("=");
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
		   								
									Nodo nuevo = new Nodo ("+",((Token)$1.obj).getNodo(),((Token)$3.obj).getNodo());
									if (datosCompatibles(((Token)$1.obj).getRecord().getType(),((Token)$3.obj).getRecord().getType())){
										String type = ((Token)$3.obj).getRecord().getType();
										((Token)$1.obj).getNodo().setPadre(nuevo);
										((Token)$3.obj).getNodo().setPadre(nuevo);
		   								$$.obj = new Token(0, ((Token)$1.obj).getLexema() + "+" +((Token)$3.obj).getLexema(), ((Token)$1.obj).getNroLine(), type, null,nuevo);
		   							}else{
	   									addError("Error sem�ntico: Los tipos de datos de la operacion + no coinciden. ", ((Token)$1.obj).getNroLine());
	   									$$.obj = new Token(0, ((Token)$1.obj).getLexema() + "+" +((Token)$3.obj).getLexema(), ((Token)$1.obj).getNroLine(), "", null,nuevo);
	   								}
								 } 
		  | expresion '-' termino{
									Nodo nuevo = new Nodo ("-",((Token)$1.obj).getNodo(),((Token)$3.obj).getNodo());
		  							if (datosCompatibles(((Token)$1.obj).getRecord().getType(),((Token)$3.obj).getRecord().getType())){
		  								String type = ((Token)$3.obj).getRecord().getType();
										((Token)$1.obj).getNodo().setPadre(nuevo);
										((Token)$3.obj).getNodo().setPadre(nuevo);
		   								$$.obj = new Token(0, ((Token)$1.obj).getLexema() + "-" +((Token)$3.obj).getLexema(), ((Token)$1.obj).getNroLine(), type, null,nuevo);
		   							}else{
	   									addError("Error sem�ntico: Los tipos de datos de la operacion - no coinciden. ", ((Token)$1.obj).getNroLine());
	   								}
								 }
		  | termino	{
		  				$$.obj = (Token)$1.obj;
		  			}
		  ;

termino : termino '*' factor{	
							
								if (datosCompatibles(((Token)$1.obj).getRecord().getType(),((Token)$3.obj).getRecord().getType())){
									String type = ((Token)$3.obj).getRecord().getType();
									Nodo nuevo = new Nodo ("*",((Token)$1.obj).getNodo(),((Token)$3.obj).getNodo());
									Token token = new Token(0, ((Token)$1.obj).getLexema() + "*" +((Token)$3.obj).getLexema(), ((Token)$1.obj).getNroLine(), type, null,nuevo);
									token.setRecord(((Token)$1.obj).getRecord());
		   							$$.obj = token; 
	   							}else{
	   								//$$.obj  = new Error("ss", 2);
	   								addError("Error sem�ntico: Los tipos de datos de la operacion * no coinciden. ", ((Token)$1.obj).getNroLine());
	   							}
							}
		| termino '/' factor{
								if (datosCompatibles(((Token)$1.obj).getRecord().getType(),((Token)$3.obj).getRecord().getType())){
									String type = ((Token)$3.obj).getRecord().getType();
									Nodo nuevo = new Nodo ("/",((Token)$1.obj).getNodo(),((Token)$3.obj).getNodo());
									Token token = new Token(0, ((Token)$1.obj).getLexema() + "*" +((Token)$3.obj).getLexema(), ((Token)$1.obj).getNroLine(), type, null,nuevo);
									token.setRecord(((Token)$1.obj).getRecord());
		   							$$.obj = token;
		   						}else{
	   								addError("Error sem�ntico: Los tipo de datos de la operacion / no coinciden. ", ((Token)$1.obj).getNroLine());
	   							}
							}
		| factor	{
						$$.obj = (Token)$1.obj;
					}
		| invocacion  '+'{
							$$.obj = (Token)$1.obj;
						}
		
		;

imprimir	:	PRINT '('CADENA')'
				//TODO chequear ambito de la cadena
				{	
					//System.out.println(((Token)$1.obj).getRecord().getIdToken());
					//	if (((Token)$1.obj).getRecord().getIdToken()==lexico.CADENA){
						setRegla(((Token)$1.obj).getNroLine(), "Impresion",((Token)$1.obj).getLexema()+"("+((Token)$3.obj).getLexema()+")" ) ;
						Nodo nodoCadena = new Nodo(((Token)$3.obj).getLexema());
						Nodo nuevo = new Nodo(((Token)$1.obj).getLexema(), nodoCadena, null);
						((Token)$1.obj).setNodo(nuevo);
					//}
				}
			| 	PRINT '(' error ')' {	
										System.out.println("je");
										Nodo nuevo = new Nodo("error", null , null);
										((Token)$1.obj).setNodo(nuevo);
										addError("Error sintactico: el contenido de impresion debe ser una cadena. ", ((Token)$1.obj).getNroLine());
									}
			;
			
asignacion 	:	ID ASIGNACION expresion {	

											((Token)$1.obj).setNodo(new Nodo(null, null, ((Token)$3.obj).getNodo())); //nodo basura
											((Token)$1.obj).setRecord(table.get(((Token)$1.obj).getLexema()));								
											if (isDeclarated((Token)$1.obj)){
												if (datosCompatibles( ((Token)$1.obj).getRecord().getType(),((Token)$3.obj).getType())){
													if (checkAmbito(((Token)$1.obj).getLexema())){
														setRegla(((Token)$1.obj).getNroLine(), "Asignacion", ((Token)$1.obj).getLexema()+":="+((Token)$3.obj).getLexema());
														Nodo nodoId = new Nodo(table.get(((Token)$1.obj).getLexema()));
														Nodo nuevo = new Nodo(":=", nodoId, ((Token)$3.obj).getNodo());
														nodoId.setPadre(nuevo);
														System.out.println("Datos Compatibles");
														((Token)$3.obj).getNodo().setPadre(nuevo);
														//$$.obj = nuevo;
														((Token)$1.obj).setNodo(nuevo);
														registrarEscritura(((Token)$1.obj).getLexema());
													}else{
														addError("Error Semantico: identificador '"+((Token)$1.obj).getLexema()+"' no pertenece al ambito '"+ambito.get(ambito.size()-1)+"'.",((Token)$1.obj).getNroLine());
													}
												}else{
												 	
													addError("Error sem�ntico: Los tipos de la asignacion no coinciden. ", ((Token)$2.obj).getNroLine());
												}
											}
											else{
														System.out.println(((Token)$1.obj).getLexema()+ "  no esta declarada");
														//$$.obj = new Nodo(null, null, ((Token)$3.obj).getNodo());
														addError("Error Sintactico: identificador '"+((Token)$1.obj).getLexema()+"' no esta declarado.",((Token)$1.obj).getNroLine());
											}
										}
		/*	|	ID ASIGNACION error {
							System.out.println("ERROR"); 
							addError("Asignacion erronea ", ((Token)$1.obj).getNroLine());
							//((Token)$1.obj).setNodo("error",null,null);
							//$$.obj = new 
							//FIXME arreglar esto, tendria que devolver un nodo para que no me tire error.
						 }
		*/
			;

factor : CTE 	{	
					TableRecord tr = table.get(((Token)$1.obj).getLexema());
					Nodo nuevo = new Nodo(table.get(((Token)$1.obj).getLexema()));
					((Token)$1.obj).setNodo(nuevo);
	   			}
	   | '-' factor {
	   				System.out.println("Un negative "+((Token)$2.obj).getRecord().getType());
	   				if (((Token)$2.obj).getRecord().getType() == "usinteger"){
	   					this.addError("Error sintactico: usinteger no puede ser negativio ",((Token)val_peek(0).obj).getNroLine());
	   					//$$.obj = error;
	   				}else{
	   					TableRecord tr = updateTableNegative(   ((Token)$2.obj).getLexema()   );
	   					Nodo nuevo = new Nodo(table.get("-"+((Token)$2.obj).getLexema()));
						$$.obj = new Token(lexico.CTE, "-"+((Token)$2.obj).getLexema(), ((Token)$1.obj).getNroLine(), "double", tr ,nuevo);
	   				}

	   			 }
	   
	   | ID  { 
	   
	   			if (isDeclarated((Token)$1.obj)){
		   			//TODO �chequeo ambito? �chequeo de declaracion?
		   			System.out.println(((Token)$1.obj).getLexema());
		   			TableRecord tr = table.get(((Token)$1.obj).getLexema());
		   			Nodo nuevo = new Nodo(tr.getLexema(),null,null);
		   			nuevo.setTableRec(tr);
		   			((Token)$1.obj).setNodo(nuevo);
		   			((Token)$1.obj).setRecord(tr);
	   			}else{
	   				Nodo nuevo = new Nodo("error",null,null);
		   			((Token)$1.obj).setNodo(nuevo);
	   			}
	   			 
	   		 }
	   ;
/*
bloque_de_sentencias : '{'list_sentencias'}' {
												
												$$.obj = ((Nodo)$2.obj).getFuncionPadre();
											 }
					   ;
*/
%%
/*******************************************************************************************************/
private static final String FUNCION = "ID funcion";
private static final String PARAMETRO = "ID parametro";
private static final String IDENTIFICADOR = "IDENTIFICADOR";
ArrayList<String> ambito = new ArrayList<String>();
LexicalAnalizer lexico;
private Table table;
Nodo raiz = null;
Nodo actual = null;
ArrayList<Nodo> funciones = new ArrayList<Nodo>();
HashSet<String> alcanceActual = new HashSet<String>();
public ArrayList<SintacticStructure> structures = new ArrayList<SintacticStructure>();
public ArrayList<Error> errors = new ArrayList<Error>();
public ArbolSintactico arbol = new ArbolSintactico();

public Table getTable(){
	return this.table;
}
	
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
	/*setea el tipo del _id en la tabla de simbolos, el uso*/
	Enumeration e = tokens.elements();
	while (e.hasMoreElements()){
		Token token = (Token)e.nextElement();
		TableRecord tr = token.getRecord();
		String lexema = tr.getLexema();
		//System.out.println("-- "+(table.get(lexema).getUso()));
		
		if (table.containsLexema(lexema) && tr.getIdToken() == ID){
			if  ((table.get(lexema).getType()!=null)){
				addError("Error sintactico: la variable ya fue declarada ",token.getNroLine());
			}	
			else{
				if (uso == FUNCION){
					if (ambito != "main"){
						System.out.println("declaracion en declaracion '"+lexema+"' declarada en '"+ambito+"'");
						addError("Error semantico: no se puede declarar funcion dentro de una funcion. ",token.getNroLine());
						(table.get(lexema,uso)).setType(type);
						(table.get(lexema)).setUso(uso);
						(table.get(lexema)).setAmbito(ambito);
					}else{
						(table.get(lexema,uso)).setType(type);
						(table.get(lexema)).setUso(uso);
						(table.get(lexema)).setAmbito(ambito);
					}
				}else{
					(table.get(lexema,uso)).setType(type);
					(table.get(lexema)).setUso(uso);
					(table.get(lexema)).setAmbito(ambito);
				}
			}
		}else{
				System.out.println("No esta asignado en la tabla");
		}
				
		
			/*		
			 if (table.containsLexema(lexema) && tr.getIdToken() == ID){
			 
				if (ambito != "main" && uso==FUNCION) {
					System.out.println("declaracion en declaracion '"+lexema+"' declarada en '"+ambito+"'");
					addError("Error semantico: no se puede declarar funcion dentro de una funcion. ",token.getNroLine());
				}else{
					if  ((table.get(lexema).getType()!=null)){
						addError("Error sintactico: la variable ya fue declarada ",token.getNroLine());
					}
					else{
							// SETEO TYPE -> DOUBLE O USINTEGER
							//TableRecord ntr = token.getRecord();
							(table.get(lexema,uso)).setType(type);
							(table.get(lexema)).setUso(uso);
							(table.get(lexema)).setAmbito(ambito);
					}
				}
			}
			else{
				System.out.println("No esta asignado en la tabla");				
			}
			*/
	}
}

public void setAmbito(Vector<Token> tokens, String ambito){
	

}

public TableRecord updateTableNegative(String key){//key: 2.0
	{	TableRecord tr = (TableRecord)table.get(key); //tomo el el tr de la tabla de simbolos
		String newLexema = "-"+ key;
		//System.out.println("NEWKEY: "+newLexema);
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
	if (table.get(id.getLexema()).getType() == null )
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
	System.out.println(type+" ** "+type2);
	if (type.equals(type2)){
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
		//System.out.println("escritado en "+funcionTR.getLexema());
		funcionTR.setWritten(true);		
	}
}


public void registrarPasaje(String id){
	TableRecord tr = table.get(id);
	if (tr.getUso() == PARAMETRO){
		String ambito = tr.getAmbito();
		TableRecord funcionTR = table.get(ambito);
		//System.out.println("pasado en "+ambito);
		funcionTR.setPassed(true);
	}
}

public boolean checkPermisos(String func, String permiso){
	TableRecord tr = table.get(func);
	boolean written = tr.isWritten();
	boolean passed = tr.isPassed();
	//System.out.println(tr.getLexema()+" writen="+written+" passed="+passed);
	//System.out.println("Lexema de LLLa func "+tr.getLexema()+" --- permiso "+permiso+"-");
	switch (permiso){
		case ("READONLY") :
			if (written || passed){
			 	return false ;
			}
			break;
		case ("WRITE") : 
			
			if (passed){
				return false;
			}
			break;
		case ("written") : 
			if (passed){
				return false;
			}
			break;
		case ("WRITE;PASS") : 
			return true;
		default : System.out.println("Mandaron cualquira");
				  return false;
	}	
	//System.out.println("permisos aceptados");
	return true;
}

private boolean isFunDeclarated(String funcion){
	//System.out.println("=="+funcion);
	if (table.contains(funcion)){
		//System.out.println("contiene la fun: "+funcion);
		TableRecord funTR = table.get(funcion);
		if (funTR.getUso()!=null && funTR.getUso().equals(FUNCION)){
			//System.out.println("es tipo funcion");
			return true;
		}
		System.out.println("no es tipo funcion: "+funTR.getUso()+" != "+FUNCION);
		return false;
	}
	System.out.println("la tabla no contiene a: "+funcion);
	return false;
	
}

private boolean checkAmbito(String lexema){
	if (table.contains(lexema)){
		//System.out.println("contiene el lex: "+lexema);
		TableRecord tr = table.get(lexema);
		int pos = ambito.size()-1;
		if (tr.getAmbito()!=null){
			while (pos>=0 && !tr.getAmbito().equals(ambito.get(pos))){
				pos-=1; 
			}
			if (pos<0){
				System.out.println("no esta en ambito!");
				return false;			
			}
			//System.out.println("esta en ambito!");
			return true;
		}
		System.out.println("no esta en ambito, ambito de "+lexema+" es '"+tr.getAmbito()+"' != de "+ambito.get(ambito.size()-1));
		return false;
	}
	System.out.println("la tabla no contiene a: "+lexema);
	return false;
	
}

