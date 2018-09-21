%{
	package sintacticSource;
	import lexicalSource.*;
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
		
		/* Comparadores */
		MAYORIGUAL
		MENORIGUAL
		IGUAL
		DISTINTO
		EOF
%%
/*reglas gramaticales*/	

programa:	list_sentencias;

list_sentencias: sent_declarativa 
				 | sent_ejecutable 
				 | list_sentencias sent_declarativa
				 | list_sentencias sent_ejecutable
			  
sent_declarativas	:	declaracion_variable ','{System.out.println("AAAAAAAAAAAAA");}
					|	sent_declarativas',' declaracion_variable
					;
					
declaracion_variable	:	tipo list_variables ;
						
						  
list_variables		:	list_variables ';' ID 
					|	ID ;
					
tipo	:	 USINTEGER
			|DOUBLE;
			
			
%%

public LexicalAnalizer lexico;

public Parser(String programa) {
    lexico = new LexicalAnalizer(programa);

}

public int yylex() {
	int token = lexico.getToken(); 
	if (token != -1)
		return token;
	else
	 	return -1;
}

public void yyerror(String errormsg){

}

public int Parse(){
	return yyparse();
}

public LexicalAnalizer getAnalizer(){
	return lexico;
}
	