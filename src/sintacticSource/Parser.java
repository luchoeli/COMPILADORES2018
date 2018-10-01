//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "especificacion.y"
	package sintacticSource;
	import lexicalSource.*;
	import java.util.ArrayList;
	import sintacticSource.Error;
	import semanticSource.SemanticAction;
	import semanticSource.CheckRangeAction;
	import java.util.Enumeration;
	import java.util.Vector;
//#line 26 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short CTE=258;
public final static short CADENA=259;
public final static short ASIGNACION=260;
public final static short IF=261;
public final static short THEN=262;
public final static short ELSE=263;
public final static short END_IF=264;
public final static short BEGIN=265;
public final static short END=266;
public final static short USINTEGER=267;
public final static short DOUBLE=268;
public final static short LONG=269;
public final static short INTEGER=270;
public final static short LINTEGER=271;
public final static short USLINTEGER=272;
public final static short SINGLE=273;
public final static short WHILE=274;
public final static short CASE=275;
public final static short DO=276;
public final static short READONLY=277;
public final static short WRITE=278;
public final static short PASS=279;
public final static short RETURN=280;
public final static short PRINT=281;
public final static short MAYORIGUAL=282;
public final static short MENORIGUAL=283;
public final static short IGUAL=284;
public final static short DISTINTO=285;
public final static short EOF=286;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    1,    1,    2,    2,    4,    4,    5,
    5,    7,    7,    6,    6,    6,    6,    6,    6,    6,
    3,    3,    3,    3,    3,   13,   13,   13,   14,   14,
   15,   15,   15,   15,   10,   10,   16,   16,   16,   17,
   17,   17,   17,    9,    9,    9,    9,   19,   19,   19,
   19,   18,   21,   21,   21,   20,   20,   20,   20,   22,
   22,   22,   22,   22,   22,    8,    8,    8,   23,   23,
   23,   11,   11,   12,   12,   24,   24,   24,   24,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    2,    2,    2,    2,    3,   13,
    9,    3,    1,    1,    1,    1,    1,    1,    1,    1,
    2,    2,    2,    2,    2,    6,    5,    5,    1,    1,
    1,    1,    1,    3,    5,    5,    3,    3,    3,    5,
    6,    4,    4,    2,    4,    2,    5,    5,    5,    5,
    5,    3,    2,    1,    2,    3,    3,    3,    3,    1,
    1,    1,    1,    1,    1,    3,    3,    1,    3,    3,
    1,    4,    4,    3,    3,    1,    2,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,   14,   15,   16,   20,   18,   17,   19,    0,
    0,    0,    0,    2,    3,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   29,   30,    0,    0,    0,    0,
    0,   76,    0,    0,    0,    0,    0,    0,   71,    0,
    0,    4,    5,    6,    7,    0,    0,   21,   22,   23,
   24,   25,   46,    0,    0,   75,    0,    0,    0,   60,
   61,   62,   63,   64,   65,    0,    0,   77,    0,    0,
    0,    0,   79,    0,    0,    0,    0,    0,    0,    0,
    0,    9,    0,    0,    0,    0,    0,   31,    0,   33,
    0,    0,    0,    0,    0,    0,    0,   58,    0,    0,
   69,   70,    0,    0,   73,   72,    0,   12,    0,   54,
    0,   45,    0,    0,    0,   27,   51,   48,   49,   50,
    0,    0,   36,    0,   35,    0,   55,   52,   53,   47,
   26,   34,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   39,   37,    0,   38,    0,   42,    0,   43,    0,
    0,   40,    0,    0,   11,   41,    0,    0,    0,   10,
};
final static short yydgoto[] = {                         12,
   13,   14,   15,   16,   17,   18,   47,   35,   19,   20,
   21,   22,   36,   29,   91,  123,  124,   85,   24,   37,
  111,   66,   38,   39,
};
final static short yysindex[] = {                       -68,
  -24,    5,    0,    0,    0,    0,    0,    0,    0,   59,
   61,    0,  -68,    0,    0,    2,   41, -177,   58,   66,
   67,   69,   70, -194,    0,    0,    8, -217,   57,  -45,
  -29,    0,   11,   21,  -25,   73,   77,   36,    0, -159,
 -191,    0,    0,    0,    0,   79,  -37,    0,    0,    0,
    0,    0,    0,   -3, -142,    0,   45,   63, -183,    0,
    0,    0,    0,    0,    0,   21,    1,    0,   21,   21,
   21,   19,    0,   -3,   21,   21,   84,   87,   90,   91,
 -196,    0, -122,  -68, -128,   -3, -183,    0,   78,    0,
   97,   45,  -69, -116,   45,   36,   36,    0,   45, -115,
    0,    0,  -95,  -95,    0,    0, -113,    0, -114,    0,
 -100,    0, -119,  105, -132,    0,    0,    0,    0,    0,
  -20, -110,    0,  -66,    0,  108,    0,    0,    0,    0,
    0,    0,   -3,  -96,  -70,   93, -104,   30,  110,   -3,
  112,    0,    0, -118,    0,  -68,    0,  116,    0,   -3,
 -102,    0,  118,  124,    0,    0,   21,   48,   47,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  176,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -41,    0,    0,    0,    0,    0,    0,  -36,    0,    0,
    0,    0,    0,    0,    0,   -1,  133,    0,    0,    0,
    0,    0,    0,    0,    2,    0,  139,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   40,    0,
    0,    6,    0,    0,    7,  -31,  -11,    0,   16,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  140,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   49,   31,   23,    0,    0,  113,    0,   34,    0,    0,
    0,    0,   39,  168,  111,  102,   75,   -7,    0,  175,
    0,  174,   38,   29,
};
final static int YYTABLESIZE=279;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         78,
   78,   78,   78,   78,   68,   78,   68,   68,   68,   66,
   28,   66,   66,   66,   65,   28,   64,   70,   78,   71,
   78,   83,  155,   68,  128,   68,   84,  122,   66,   67,
   66,   67,   67,   67,   65,   43,   64,  134,   23,   25,
   26,   93,   13,   42,   33,   44,   59,   57,   67,   34,
   67,   23,   34,   84,  143,   34,   56,   13,  137,   94,
   57,   53,   68,   34,   79,   34,  100,   80,   54,   55,
    3,    4,    5,    6,    7,    8,    9,   75,  113,   46,
   32,   78,   76,   32,   45,  118,   68,   70,  159,   71,
   70,   66,   71,   88,   89,   90,   77,   78,   40,   92,
   41,   48,   95,  101,  102,   99,  110,   96,   97,   49,
   50,   67,   51,   52,  109,   59,   73,   74,   81,   84,
   86,   87,   23,   84,  103,  139,  141,  104,   59,   57,
  105,  106,  148,  129,  108,  112,  115,  116,   56,  119,
  120,  127,  153,  126,  130,  131,  132,  121,  138,   23,
  144,  145,  146,  147,    1,  149,    1,  150,    2,  152,
    2,  156,  121,  157,    3,    4,    5,    6,    7,    8,
    9,  160,   10,   43,   10,    1,    8,  154,   11,  140,
   11,   42,   74,   28,   23,  142,  117,  136,    1,   23,
  158,  136,    2,  107,  151,   58,  135,  114,    3,    4,
    5,    6,    7,    8,    9,  125,   10,   67,   72,    0,
    0,    0,   11,    0,   78,    0,    0,    0,   82,   68,
    0,    0,    0,    0,   66,    0,    0,   25,   26,    0,
   69,    0,   25,   26,    0,   27,   60,   61,   62,   63,
   78,   78,   78,   78,   67,   68,   68,   68,   68,    0,
   66,   66,   66,   66,   13,  133,   60,   61,   62,   63,
   30,   31,   32,   56,   31,   32,   30,   31,   32,    0,
   67,   67,   67,   67,   98,   31,   32,   31,   32,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   41,   47,   43,   44,   45,   41,
   40,   43,   44,   45,   60,   40,   62,   43,   60,   45,
   62,   59,  125,   60,  125,   62,  123,  123,   60,   41,
   62,   43,   44,   45,   60,   13,   62,   58,    0,  257,
  258,   41,   44,   13,   40,   44,   41,   41,   60,   45,
   62,   13,   45,  123,  125,   45,   41,   59,  125,   67,
   27,  256,   34,   45,  256,   45,   74,  259,  263,  264,
  267,  268,  269,  270,  271,  272,  273,   42,   86,  257,
   41,  123,   47,   44,   44,   93,  123,   43,   41,   45,
   43,  123,   45,  277,  278,  279,  256,  257,   40,   66,
   40,   44,   69,   75,   76,   72,   84,   70,   71,   44,
   44,  123,   44,   44,   84,   59,   44,   41,   40,  123,
  263,   59,   84,  123,   41,  133,  134,   41,  123,  123,
   41,   41,  140,  111,  257,  264,   59,   41,  123,  256,
  256,  256,  150,  257,  264,   41,  279,  258,   41,  111,
   58,  256,  123,   44,  257,   44,  257,  276,  261,   44,
  261,   44,  258,   40,  267,  268,  269,  270,  271,  272,
  273,  125,  275,  151,  275,    0,   44,  280,  281,  276,
  281,  151,   44,   44,  146,  256,  256,  258,  257,  151,
  157,  258,  261,   81,  146,   28,  122,   87,  267,  268,
  269,  270,  271,  272,  273,  104,  275,   33,   35,   -1,
   -1,   -1,  281,   -1,  256,   -1,   -1,   -1,  256,  256,
   -1,   -1,   -1,   -1,  256,   -1,   -1,  257,  258,   -1,
  256,   -1,  257,  258,   -1,  260,  282,  283,  284,  285,
  282,  283,  284,  285,  256,  282,  283,  284,  285,   -1,
  282,  283,  284,  285,  256,  276,  282,  283,  284,  285,
  256,  257,  258,  256,  257,  258,  256,  257,  258,   -1,
  282,  283,  284,  285,  256,  257,  258,  257,  258,
};
}
final static short YYFINAL=12;
final static short YYMAXTOKEN=286;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
"'<'",null,"'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","CTE","CADENA","ASIGNACION","IF","THEN",
"ELSE","END_IF","BEGIN","END","USINTEGER","DOUBLE","LONG","INTEGER","LINTEGER",
"USLINTEGER","SINGLE","WHILE","CASE","DO","READONLY","WRITE","PASS","RETURN",
"PRINT","MAYORIGUAL","MENORIGUAL","IGUAL","DISTINTO","EOF",
};
final static String yyrule[] = {
"$accept : programa",
"programa : list_sentencias",
"list_sentencias : sent_declarativa",
"list_sentencias : sent_ejecutable",
"list_sentencias : list_sentencias sent_declarativa",
"list_sentencias : list_sentencias sent_ejecutable",
"sent_declarativa : declaracion_variable ','",
"sent_declarativa : declaracion_funcion ','",
"declaracion_variable : tipo list_variables",
"declaracion_variable : tipo list_variables error",
"declaracion_funcion : tipo ID '(' tipo ID ')' '{' list_sentencias RETURN '(' expresion ')' '}'",
"declaracion_funcion : tipo ID '(' tipo ID ')' '{' list_sentencias '}'",
"list_variables : list_variables ';' ID",
"list_variables : ID",
"tipo : USINTEGER",
"tipo : DOUBLE",
"tipo : LONG",
"tipo : USLINTEGER",
"tipo : LINTEGER",
"tipo : SINGLE",
"tipo : INTEGER",
"sent_ejecutable : sent_seleccion ','",
"sent_ejecutable : sent_control ','",
"sent_ejecutable : imprimir ','",
"sent_ejecutable : asignacion ','",
"sent_ejecutable : invocacion ','",
"invocacion : ID '(' nombre_parametro ';' lista_permisos ')'",
"invocacion : ID nombre_parametro ';' lista_permisos ')'",
"invocacion : ID '(' nombre_parametro ';' lista_permisos",
"nombre_parametro : ID",
"nombre_parametro : CTE",
"lista_permisos : READONLY",
"lista_permisos : WRITE",
"lista_permisos : PASS",
"lista_permisos : WRITE ';' PASS",
"sent_control : CASE '(' ID ')' bloque_control",
"sent_control : CASE '(' error ')' bloque_control",
"bloque_control : '{' linea_control '}'",
"bloque_control : linea_control '}' error",
"bloque_control : '{' linea_control error",
"linea_control : CTE ':' DO bloque_sin_declaracion ','",
"linea_control : linea_control CTE ':' DO bloque_sin_declaracion ','",
"linea_control : CTE DO bloque_sin_declaracion ','",
"linea_control : CTE ':' bloque_sin_declaracion ','",
"sent_seleccion : sent_if END_IF",
"sent_seleccion : sent_if ELSE bloque_sin_declaracion END_IF",
"sent_seleccion : sent_if error",
"sent_seleccion : sent_if END_IF ELSE bloque_sin_declaracion END_IF",
"sent_if : IF '(' expresion_logica ')' bloque_sin_declaracion",
"sent_if : IF '(' expresion_logica bloque_sin_declaracion error",
"sent_if : IF expresion_logica ')' bloque_sin_declaracion error",
"sent_if : IF '(' expresion_logica ')' error",
"bloque_sin_declaracion : '{' list_sentencias_no_declarables '}'",
"list_sentencias_no_declarables : list_sentencias_no_declarables sent_ejecutable",
"list_sentencias_no_declarables : sent_ejecutable",
"list_sentencias_no_declarables : sent_declarativa error",
"expresion_logica : expresion comparador expresion",
"expresion_logica : expresion error expresion",
"expresion_logica : expresion comparador error",
"expresion_logica : error comparador expresion",
"comparador : MAYORIGUAL",
"comparador : MENORIGUAL",
"comparador : IGUAL",
"comparador : DISTINTO",
"comparador : '>'",
"comparador : '<'",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"imprimir : PRINT '(' CADENA ')'",
"imprimir : PRINT '(' error ')'",
"asignacion : ID ASIGNACION expresion",
"asignacion : ID ASIGNACION error",
"factor : CTE",
"factor : '-' factor",
"factor : ID",
"factor : invocacion ','",
};

//#line 335 "especificacion.y"
/**/
LexicalAnalizer lexico;
Table table;
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
//#line 528 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 98 "especificacion.y"
{System.out.println("TERMINO GRAMATICA");}
break;
case 8:
//#line 112 "especificacion.y"
{/*System.out.println("Declaracion variable");*/
												 setRegla(((Token)val_peek(1).obj).getNroLine(), "Declaracion de variables", ((Token)val_peek(1).obj).getLexema());
												 updateTable(((Vector<Token>)val_peek(0).obj), ((Token)val_peek(1).obj).getLexema());												 
												 }
break;
case 9:
//#line 116 "especificacion.y"
{
														Vector<Token> tokens = (Vector<Token>)val_peek(1).obj;
														if (tokens.size()>1){
															this.addError("Error sintactico en declaración múltiple.", ((Token)val_peek(2).obj).getNroLine());
														}else{
															this.addError("Error sintactico en declaración.", ((Token)val_peek(2).obj).getNroLine());
														}
													}
break;
case 10:
//#line 129 "especificacion.y"
{
					  		setRegla(((Token)val_peek(12).obj).getNroLine(), "Declaracion de funcion ", ((Token)val_peek(12).obj).getLexema()+" "+((Token)val_peek(11).obj).getLexema());
					  		Vector<Token> vec = new Vector<Token>(); 
					  		vec.add((Token)val_peek(11).obj);
					  		updateTable(vec, ((Token)val_peek(12).obj).getLexema());
					  	  }
break;
case 11:
//#line 138 "especificacion.y"
{this.addError("Error sintactico: falta return en la declaracion de la funcion ", ((Token)val_peek(8).obj).getNroLine());}
break;
case 12:
//#line 142 "especificacion.y"
{
											Vector<Token> tokens = (Vector<Token>)val_peek(2).obj;
											Token token = (Token)val_peek(0).obj;
											tokens.add(token);
											yyval.obj = tokens;
											
											}
break;
case 13:
//#line 149 "especificacion.y"
{
							Vector<Token> tokens = new Vector<Token>();
							Token token = (Token)val_peek(0).obj;
							tokens.add(token);
							yyval.obj = tokens;
							}
break;
case 16:
//#line 159 "especificacion.y"
{this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 17:
//#line 160 "especificacion.y"
{this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 18:
//#line 161 "especificacion.y"
{this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 19:
//#line 162 "especificacion.y"
{this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 20:
//#line 163 "especificacion.y"
{this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 26:
//#line 174 "especificacion.y"
{
																setRegla(((Token)val_peek(5).obj).getNroLine(), "Invocacion", ((Token)val_peek(5).obj).getLexema());
															 }
break;
case 27:
//#line 177 "especificacion.y"
{
																	addError("Error sintactico: falta '(' al inicio de la invocacion ", ((Token)val_peek(4).obj).getNroLine());
															 	}
break;
case 28:
//#line 181 "especificacion.y"
{
																addError("Error sintactico: falta ')' al final de la invocacion ", ((Token)val_peek(4).obj).getNroLine());
															}
break;
case 35:
//#line 197 "especificacion.y"
{System.out.println("Case do");
				  						  setRegla(((Token)val_peek(4).obj).getNroLine(), "Sentencia de control", ((Token)val_peek(4).obj).getLexema());												 
										 }
break;
case 36:
//#line 201 "especificacion.y"
{
				  						  addError("Error sintactico: condicion erronea ", ((Token)val_peek(3).obj).getNroLine());												 
										 }
break;
case 38:
//#line 207 "especificacion.y"
{addError("Error sintactico: falta '{' para iniciar el bloque de sentencias de control ", ((Token)val_peek(2).obj).getNroLine());}
break;
case 39:
//#line 208 "especificacion.y"
{addError("Error sintactico: falta '}' para terminar el bloque de sentencias de control ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 42:
//#line 214 "especificacion.y"
{addError("Error sintactico: falta ':' antes del 'do'", ((Token)val_peek(3).obj).getNroLine());}
break;
case 43:
//#line 215 "especificacion.y"
{addError("Error sintactico: falta 'do' despues del ':'", ((Token)val_peek(3).obj).getNroLine());}
break;
case 45:
//#line 220 "especificacion.y"
{setRegla(((Token)val_peek(2).obj).getNroLine(), "Sentencia de Control", "else");
			   			  									}
break;
case 46:
//#line 222 "especificacion.y"
{ addError("Error sintactico: Falta palabra reservada 'end_if' luego del bloque ",((Token)val_peek(0).obj).getNroLine());}
break;
case 47:
//#line 223 "especificacion.y"
{ addError("Error sintactico: 'else' incorrecto luego del 'end_if' ",((Token)val_peek(2).obj).getNroLine());}
break;
case 48:
//#line 226 "especificacion.y"
{
			   				  	    	setRegla(((Token)val_peek(4).obj).getNroLine(), "Sentencia de Control", "if");
			   			   		   }
break;
case 49:
//#line 230 "especificacion.y"
{
											addError("Falta parentesis de cierre ')'",((Token)val_peek(3).obj).getNroLine());
 									     }
break;
case 50:
//#line 234 "especificacion.y"
{
										  	addError("Falta parentesis de apertura '('",((Token)val_peek(3).obj).getNroLine());
 									     }
break;
case 51:
//#line 238 "especificacion.y"
{
				   		addError("Error sintactico en el bloque ",((Token)val_peek(3).obj).getNroLine());
 			      }
break;
case 55:
//#line 250 "especificacion.y"
{ 
															addError("Error sintáctico: no se permiten sentencias declarativas dentro de un bloque de control ",((Token)val_peek(1).obj).getNroLine());
														}
break;
case 56:
//#line 257 "especificacion.y"
{ setRegla(((Token)val_peek(2).obj).getNroLine(), "expresion logica", ((Token)val_peek(1).obj).getLexema());}
break;
case 57:
//#line 259 "especificacion.y"
{
													addError("Errorsintactico: Comparador invalido. ", ((Token)val_peek(2).obj).getNroLine());
												}
break;
case 58:
//#line 262 "especificacion.y"
{
													addError("Error sintactico: Expresion derecha invalida ", ((Token)val_peek(2).obj).getNroLine());
												}
break;
case 59:
//#line 265 "especificacion.y"
{
													addError("Error sintactico: Expresion izquierda invalida ", ((Token)val_peek(2).obj).getNroLine());
												}
break;
case 66:
//#line 279 "especificacion.y"
{
									yyval.obj = new Token(0, ((Token)val_peek(2).obj).getLexema() + "+" +((Token)val_peek(0).obj).getLexema(), ((Token)val_peek(2).obj).getNroLine(), "", null);
								 }
break;
case 67:
//#line 282 "especificacion.y"
{
									yyval.obj = new Token(0, ((Token)val_peek(2).obj).getLexema() + "-" +((Token)val_peek(0).obj).getLexema(), ((Token)val_peek(2).obj).getNroLine(), "", null);
								 }
break;
case 69:
//#line 288 "especificacion.y"
{
								yyval.obj = new Token(0, ((Token)val_peek(2).obj).getLexema() + "*" +((Token)val_peek(0).obj).getLexema(), ((Token)val_peek(2).obj).getNroLine(), "", null);
							}
break;
case 70:
//#line 291 "especificacion.y"
{
								yyval.obj = new Token(0, ((Token)val_peek(2).obj).getLexema() + "/" +((Token)val_peek(0).obj).getLexema(), ((Token)val_peek(2).obj).getNroLine(), "", null);
							}
break;
case 72:
//#line 298 "especificacion.y"
{setRegla(((Token)val_peek(3).obj).getNroLine(), "Impresion",((Token)val_peek(3).obj).getLexema()+"("+((Token)val_peek(1).obj).getLexema()+")" ) ;}
break;
case 73:
//#line 299 "especificacion.y"
{addError("Error sintactico: el contenido de impresion debe ser una cadena. ", ((Token)val_peek(3).obj).getNroLine());}
break;
case 74:
//#line 302 "especificacion.y"
{
											if (isDeclarated((Token)val_peek(2).obj)){	
												setRegla(((Token)val_peek(2).obj).getNroLine(), "Asignacion", ((Token)val_peek(2).obj).getLexema()+":="+((Token)val_peek(0).obj).getLexema());
											}
										}
break;
case 75:
//#line 307 "especificacion.y"
{ 
							addError("Asignacion erronea ", ((Token)val_peek(2).obj).getNroLine());
						 }
break;
case 77:
//#line 313 "especificacion.y"
{
	   				System.out.println("Un negative "+((Token)val_peek(0).obj).getRecord().getType());
	   				if (((Token)val_peek(0).obj).getRecord().getType() =="usinteger"){
	   					this.addError("Error sintactico: usinteger no puede ser negativio ",((Token)val_peek(0).obj).getNroLine());
	   					/*$$.obj = error;*/
	   				}else{
	   					updateTableNegative(   ((Token)val_peek(0).obj).getLexema()   );
	   					yyval.obj = new Token(0, "-"+((Token)val_peek(0).obj).getLexema(), ((Token)val_peek(1).obj).getNroLine(), "", null);
	   				}
	   				
	   			 }
break;
case 78:
//#line 325 "especificacion.y"
{ 
	   			isDeclarated((Token)val_peek(0).obj);
	   		 }
break;
//#line 928 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
