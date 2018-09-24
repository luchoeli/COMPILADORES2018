%{
	package sintacticSource;
	import lexicalSource.*;
	import java.util.ArrayList;
	import sintacticSource.Error;
	import java.util.Enumeration;
	import java.util.Vector;
%}

%token
		ID
		CTE
		CADENA
		/* Palabras reservadas */
		IF
		THEN
		ELSE
		END_IF
		BEGIN
		END
		USINTEGER
		DOUBLE
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
programa:	list_sentencias;

list_sentencias: sent_declarativa 
				 | sent_ejecutable 
				 | list_sentencias sent_declarativa
				 | list_sentencias sent_ejecutable 
				 ;
			  
sent_declarativa	:	declaracion_variable ','
					|	declaracion_funcion ','
					;
					
declaracion_variable	:	tipo list_variables {System.out.println("Declaracion variable");
												 setRegla(((Token)$1.obj).getNroLine(), "Declaracion de variables", ((Token)$1.obj).getLexema());
												 //updateTable(((Vector<Token>)$2.obj), ((Token)$1.obj).getLexema());												 
												 }
						;

declaracion_funcion	: tipo ID '(' tipo ID')' '{'
					  list_sentencias // conjunto de sentencias declarativas y ejecutables
					  RETURN '(' expresion ')'
					  '}'
					  {setRegla(((Token)$1.obj).getNroLine(), "Declaracion de funcion", ((Token)$1.obj).getLexema()+" "+((Token)$2.obj).getLexema());}
					  ;

						  
list_variables		:	list_variables ';' ID
					|	ID ; {System.out.println("WEPA");}

tipo	:	 USINTEGER
		|	DOUBLE
			;
			

sent_ejecutable  : sent_seleccion ','
				 | sent_control ','
				 | imprimir ','
				 | asignacion ',' {System.out.println("signacion realizada");}
				 | invocacion ','
				 ;
				 
invocacion	:	ID '(' nombre_parametro ';' lista_permisos')'

nombre_parametro :	ID
				 |  CTE
				 ;

lista_permisos	: READONLY
				| WRITE
				| PASS
				| WRITE ';' PASS
				;

sent_control	: CASE '(' ID ')''{' linea_control '}'  				
										 {System.out.println("Case do");
				  						  setRegla(((Token)$1.obj).getNroLine(), "Sentencia de control", ((Token)$1.obj).getLexema());
										  //updateTable(((Vector<Token>)$2.obj), ((Token)$1.obj).getLexema());												 
										 }
				;

linea_control	: 	CTE ':' DO bloque_de_sentencias','
				|	linea_control CTE ':' DO bloque_de_sentencias','
				;


sent_seleccion :	IF '('expresion_logica')' bloque_de_sentencias ELSE bloque_de_sentencias END_IF 
			   |	IF '('expresion_logica')' bloque_de_sentencias
			    {setRegla(((Token)$1.obj).getNroLine(), "Sentencia de Control", ((Token)$1.obj).getLexema());}
			   ;
/* esto va en las de control.. pero me genera ambiguedad VER
bloque_sin_declaracion : '{'list_sentencias'}'
*/				   ;

/*
condicion : expresion_logica comparador expresion_logica
		  ;
*/


expresion_logica : expresion comparador expresion { setRegla(((Token)$1.obj).getNroLine(), "expresion logica", ((Token)$2.obj).getLexema());}
				 ;

comparador : MAYORIGUAL
		   | MENORIGUAL
		   | IGUAL
		   | DISTINTO
		   | '>'
		   | '<'
		   ;


expresion : expresion '+' termino 
		  | expresion '-' termino
		  | termino
		  ;

termino : termino '*' factor
		| termino '/' factor
		| factor
		;

imprimir	:	PRINT '('CADENA')'
			;
			
asignacion 	:	ID ':=' expresion  {System.out.println("ASIGNACION");setRegla(((Token)$1.obj).getNroLine(), "Asignacion", ((Token)$1.obj).getLexema()+":="+((Token)$3.obj).getLexema());}
			;

/*chequear factor*/
factor : CTE
	   | ID
	   ;

bloque_de_sentencias : '{'list_sentencias'}'
					 ;

			
%%
/**/
public LexicalAnalizer lexico;
public Table table;
public ArrayList<SintacticStructure> structures = new ArrayList<SintacticStructure>();
public ArrayList<Error> errors = new ArrayList<Error>();

public Parser(String programa, Table table) {
    lexico = new LexicalAnalizer(programa, table);
	this.table = table;
}

private int yylex() {
	Token token=lexico.getToken();

	if (token!=null){
	    yylval = new ParserVal(token);
	    return token.getId();
	}

	return 0;
}
public void yyerror(String errormsg){

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

public void updateTable(Vector<Token> tokens, String type){
	Enumeration e = tokens.elements();
	while (e.hasMoreElements()){
		Token token = (Token)e.nextElement();
		TableRecord tr = token.getRecord();
		String lexema = tr.getLexema();
		String newKey = lexema;// + getAlcance(alcance);
		if (!(table.contains(newKey))){
			tr.decrement();
			if (tr.getRef()==0){
				table.remove(lexema);
			}
			TableRecord newTr = new TableRecord(lexema, tr.getIdToken());
			newTr.setAmbito(newKey);
			table.put(newKey, newTr);		
			if (newTr.getType()== null){
				newTr.setType(type);
			}
			token.setRecord(newTr);

		}else{
			this.addError("Error sintactico: Ya existe una variable declarada con el mismo nombre.", token.getNroLine());
		}
		
	}
}
