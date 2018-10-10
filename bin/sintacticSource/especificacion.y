%{
	package sintacticSource;
	import lexicalSource.*;
	import java.util.ArrayList;
	import sintacticSource.Error;
	import semanticSource.SemanticAction;
	import semanticSource.CheckRangeAction;
	import java.util.Enumeration;
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
/*reglas gramaticales*/	

/*-----------------------------------------------------------------------------------------------------------------
Programa:

*Programa constituido por un conjunto de sentencias, que pueden ser declarativas o ejecutables.
Las sentencias declarativas pueden aparecer en cualquier lugar del cÃ³digo fuente, exceptuando los bloques de las sentencias de control.

*El programa no tendrÃ¡ ningÃºn delimitador.

*Cada sentencia debe terminar con " , ".

*Sentencias declarativas:
	<tipo> <lista_de_variables>, //o la estructura que corresponda al tema asignado (11 a 13)

	<tipo> ID ( <tipo> ID) {
		<cuerpo_de_la_funcion> // conjunto de sentencias declarativas y ejecutables
		return ( <retorno> )
	}
	Donde <retorno> es expresion

*Sentencias ejecutables:
	*ClÃ¡usula de selecciÃ³n (if). Cada rama de la selecciÃ³n serÃ¡ un bloque de sentencias. La condiciÃ³n serÃ¡ una comparaciÃ³n entre expresiones aritmÃ©ticas, variables o constantes, y debe escribirse entre ( ). La estructura de la selecciÃ³n serÃ¡, entonces:
	if (<condicion>) <bloque_de_sentencias> else <bloque_de_sentencias> end_if
	El bloque para el else puede estar ausente.

	* Un bloque de sentencias puede estar constituido por una sola sentencia, o un conjunto de sentencias delimitadas por â€˜{â€˜ y â€˜}â€™.

	* Sentencia de control segÃºn tipo especial asignado al grupo. (Temas 7 al 10 del Trabajo prÃ¡ctico 1)
	Debe permitirse anidamiento de sentencias de control. Por ejemplo, puede haber una iteraciÃ³n dentro de una rama de una selecciÃ³n.
	* Sentencia de salida de mensajes por pantalla. El formato serÃ¡ print(cadena). Las cadenas de caracteres sÃ³lo podrÃ¡n ser usadas en esta sentencia, y tendrÃ¡n el formato asignado al grupo en el Trabajo PrÃ¡ctico 1.
	* Los operandos de las expresiones aritmÃ©ticas pueden ser variables, constantes, u otras expresiones aritmÃ©ticas.
	No se permiten anidamientos de expresiones con parÃ©ntesis.
	
	*Incorporar la invocaciÃ³n de funciones, con la siguiente sintaxis:
		ID(<nombre_parametro>;<lista_permisos>),

	Donde <lista_permisos> puede ser: readonly / write / pass / write ; pass
	La invocaciÃ³n puede aparecer en cualquier lugar donde pueda aparecer una expresiÃ³n aritmÃ©tica.

*Sentencias de control
	case do (tema 10 en TP1)
		case ( variable ){
			valor1: do <bloque> .
			valor2: do <bloque> .
			...
			valorN: do <bloque> .
		} .
	Nota: Los valores valor1, valor2, etc., sÃ³lo podrÃ¡n ser constantes del mismo tipo que la variable.
	La restricciÃ³n de tipo serÃ¡ chequeada en la etapa 3 del trabajo prÃ¡ctico.
------------------------------------------------------------------------------------------------------------*/
programa:	list_sentencias {System.out.println("TERMINO GRAMATICA");}
		;

list_sentencias:   sent_declarativa 
				 | sent_ejecutable 
			
				 | list_sentencias sent_declarativa
				 | list_sentencias sent_ejecutable
				 ;
			  
sent_declarativa	:	declaracion_variable ','
					|	declaracion_funcion ','
					;
					
declaracion_variable	:	tipo list_variables {//System.out.println("Declaracion variable");
												 setRegla(((Token)$1.obj).getNroLine(), "Declaracion de variables", ((Token)$1.obj).getLexema());
												 updateTable(((Vector<Token>)$2.obj), ((Token)$1.obj).getLexema());												 
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

declaracion_funcion	: tipo ID '(' tipo ID')' '{'
					  list_sentencias // conjunto de sentencias declarativas y ejecutables
					  RETURN '(' expresion ')'
					  '}' {
					  		setRegla(((Token)$1.obj).getNroLine(), "Declaracion de funcion ", ((Token)$1.obj).getLexema()+" "+((Token)$2.obj).getLexema());
					  		Vector<Token> vec = new Vector<Token>(); 
					  		vec.add((Token)$2.obj);
					  		updateTable(vec, ((Token)$1.obj).getLexema());
					  	  } 
					
					| tipo ID '(' tipo ID')' '{'
					  list_sentencias // conjunto de sentencias declarativas y ejecutables
					  '}' {this.addError("Error sintactico: falta return en la declaracion de la funcion ", ((Token)$1.obj).getNroLine());}
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
			

sent_ejecutable  : sent_seleccion ','
				 | sent_control ','
				 | imprimir ','
				 | asignacion ',' //{System.out.println("Asignacion realizada");}
				 | invocacion ','
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
				
sent_seleccion : sent_if END_IF
	
			   | sent_if ELSE bloque_sin_declaracion END_IF{setRegla(((Token)$2.obj).getNroLine(), "Sentencia de Control", "else");
			   			  									}
			   | sent_if error { addError("Error sintactico: Falta palabra reservada 'end_if' luego del bloque ",((Token)$2.obj).getNroLine());}
			   | sent_if END_IF ELSE bloque_sin_declaracion END_IF { addError("Error sintactico: 'else' incorrecto luego del 'end_if' ",((Token)$3.obj).getNroLine());}
			   ;			  									
sent_if :	IF '('expresion_logica')'  
			bloque_sin_declaracion {
			   				  	    	setRegla(((Token)$1.obj).getNroLine(), "Sentencia de Control", "if");
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

bloque_sin_declaracion : '{'list_sentencias_no_declarables'}'
					   ;

list_sentencias_no_declarables : list_sentencias_no_declarables sent_ejecutable
								| sent_ejecutable
								//| sent_ejecutable error {addError("Error sintactico: falta la coma",((Token)$2.obj).getNroLine());}
								| sent_declarativa error{ 
															addError("Error sint�ctico: no se permiten sentencias declarativas dentro de un bloque de control ",((Token)$1.obj).getNroLine());
														}
								;
								


expresion_logica : expresion comparador expresion { setRegla(((Token)$1.obj).getNroLine(), "expresion logica", ((Token)$2.obj).getLexema());}
				 		
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

comparador : MAYORIGUAL
		   | MENORIGUAL
		   | IGUAL
		   | DISTINTO
		   | '>'
		   | '<'
		   ;


expresion : expresion '+' termino{
									$$.obj = new Token(0, ((Token)$1.obj).getLexema() + "+" +((Token)$3.obj).getLexema(), ((Token)$1.obj).getNroLine(), "", null);
									Nodo nuevo = new Nodo("+",arbol.getE(),arbol.getT());
	   								arbol.setE(nuevo);
								 } 
		  | expresion '-' termino{
									$$.obj = new Token(0, ((Token)$1.obj).getLexema() + "-" +((Token)$3.obj).getLexema(), ((Token)$1.obj).getNroLine(), "", null);
									Nodo nuevo = new Nodo("-",arbol.getE(),arbol.getT());
	   								arbol.setE(nuevo);
								 }
		  | termino	{
		  				arbol.setE(arbol.getT());
		  			}
		  ;

termino : termino '*' factor{
								$$.obj = new Token(0, ((Token)$1.obj).getLexema() + "*" +((Token)$3.obj).getLexema(), ((Token)$1.obj).getNroLine(), "", null);
								Nodo nuevo = new Nodo("*",arbol.getT(),arbol.getF());
	   							arbol.setT(nuevo);
							}
		| termino '/' factor{
								$$.obj = new Token(0, ((Token)$1.obj).getLexema() + "/" +((Token)$3.obj).getLexema(), ((Token)$1.obj).getNroLine(), "", null);
								Nodo nuevo = new Nodo("/",arbol.getT(),arbol.getF());
	   							arbol.setT(nuevo);
							}
		| factor	{
						arbol.setT(arbol.getF());
					}
		;

imprimir	:	PRINT '('CADENA')'
				{setRegla(((Token)$1.obj).getNroLine(), "Impresion",((Token)$1.obj).getLexema()+"("+((Token)$3.obj).getLexema()+")" ) ;}
			| 	PRINT '(' error ')' {addError("Error sintactico: el contenido de impresion debe ser una cadena. ", ((Token)$1.obj).getNroLine());}
			;
			
asignacion 	:	ID ASIGNACION expresion {
											if (isDeclarated((Token)$1.obj)){	
												setRegla(((Token)$1.obj).getNroLine(), "Asignacion", ((Token)$1.obj).getLexema()+":="+((Token)$3.obj).getLexema());
												Nodo nuevo = new Nodo(":=", new Nodo(table.get(((Token)$1.obj).getLexema())), arbol.getE());
												//arbol.setA(nuevo);
												arbol.add(nuevo);
																								
											}
										}
			|	ID ASIGNACION error { 
							addError("Asignacion erronea ", ((Token)$1.obj).getNroLine());
						 }
			;

factor : CTE 	{	Nodo nuevo = new Nodo(table.get(((Token)$1.obj).getLexema()));
	   				arbol.setF(nuevo);
	   			}
	   | '-' factor {
	   				System.out.println("Un negative "+((Token)$2.obj).getRecord().getType());
	   				if (((Token)$2.obj).getRecord().getType() == "usinteger"){
	   					this.addError("Error sintactico: usinteger no puede ser negativio ",((Token)val_peek(0).obj).getNroLine());
	   					//$$.obj = error;
	   				}else{
	   					updateTableNegative(   ((Token)$2.obj).getLexema()   );
	   					$$.obj = new Token(0, "-"+((Token)$2.obj).getLexema(), ((Token)$1.obj).getNroLine(), "", null);
	   					Nodo nuevo = new Nodo(table.get("-"+((Token)$2.obj).getLexema()));
	   					arbol.setF(nuevo);
	   				}

	   			 }
	   
	   | ID  { 
	   			isDeclarated((Token)$1.obj);
	   			Nodo nuevo = new Nodo(table.get(((Token)$1.obj).getLexema()));
	   			arbol.setF(nuevo);
	   		 }
	   | invocacion','
	   	{
	   		//TODO 	poner la invocacion en una variable auxuliar y agregarla a la tabla de simbolos 
	   	}
	   		 
	   ;

//bloque_de_sentencias : '{'list_sentencias'}'
//					   ;
%%
/**/
LexicalAnalizer lexico;
Table table;
public ArrayList<SintacticStructure> structures = new ArrayList<SintacticStructure>();
public ArrayList<Error> errors = new ArrayList<Error>();
public ArbolSintactico arbol = new ArbolSintactico();

public Parser(String programa, Table table) {
    lexico = new LexicalAnalizer(programa, table);
	this.table = table;
}

public ArbolSintactico getArbol(){
	return arbol;
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

public void updateTable(Vector<Token> tokens, String type){  //type double o usinteger, tokens son identificadores
	/*setea el tipo del _id en la tabla de simbolos*/
	Enumeration e = tokens.elements();
	while (e.hasMoreElements()){
		Token token = (Token)e.nextElement();
		TableRecord tr = token.getRecord();
		String lexema = tr.getLexema();
		
		if (table.containsLexema(lexema)){
			//System.out.println("esta en la tabla");
			
			if  (table.get(lexema).getType()!="IDENTIFICADOR"){
				addError("Error sintactico: la variable ya fue declarada ",token.getNroLine());
			}
			else{
				(table.get(lexema)).setType(type);
			}
		}
		else{
			System.out.println("No esta asignado en la tabla");				
			}
	}
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
	if (table.get(id.getLexema()).getType() == "IDENTIFICADOR" )
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
