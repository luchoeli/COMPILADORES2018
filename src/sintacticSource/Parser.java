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
	import java.util.HashSet;
	import java.util.Vector;
//#line 27 "Parser.java"




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
    0,    1,    1,    1,    1,    2,    2,    4,    4,    8,
    5,    7,    7,    6,    6,    6,    6,    6,    6,    6,
    3,    3,    3,    3,   14,   14,   14,   15,   15,   16,
   16,   16,   16,   11,   11,   17,   17,   17,   18,   18,
   18,   18,   10,   10,   10,   10,   20,   20,   20,   20,
   19,   22,   22,   22,   21,   21,   21,   21,   23,   23,
   23,   23,   23,   23,    9,    9,    9,   24,   24,   24,
   12,   12,   13,   13,   25,   25,   25,   25,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    2,    2,    2,    2,    3,    6,
    8,    3,    1,    1,    1,    1,    1,    1,    1,    1,
    2,    2,    2,    2,    6,    5,    5,    1,    1,    1,
    1,    1,    3,    5,    5,    3,    3,    3,    5,    6,
    4,    4,    2,    4,    2,    5,    5,    5,    5,    5,
    3,    2,    1,    2,    3,    3,    3,    3,    1,    1,
    1,    1,    1,    1,    3,    3,    1,    3,    3,    1,
    4,    4,    3,    3,    1,    2,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,   14,   15,   16,   20,   18,   17,   19,    0,
    0,    0,    0,    2,    3,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   75,    0,    0,
    0,    0,    0,    0,   70,    0,    0,    4,    5,    6,
    7,    0,    0,    0,   21,   22,   23,   24,   45,    0,
    0,   74,    0,   59,   60,   61,   62,   63,   64,    0,
   28,   29,    0,    0,    0,   76,    0,    0,    0,    0,
   78,    0,    0,    0,    0,    0,    0,    0,    0,    9,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   57,    0,    0,   68,   69,    0,    0,
   72,   71,    0,   12,    0,    0,   53,    0,   44,    0,
    0,   30,    0,   32,    0,   50,   47,   48,   49,    0,
    0,   35,    0,   34,    0,    0,   54,   51,   52,   46,
    0,    0,   26,    0,    0,    0,    0,    0,   10,    0,
   25,   33,    0,    0,    0,   38,   36,    0,   37,    0,
   41,    0,   42,    0,   11,   39,    0,   40,
};
final static short yydgoto[] = {                         12,
   13,   14,   15,   16,   17,   18,   43,   19,   31,   20,
   21,   22,   23,   32,   64,  115,  122,  123,   84,   24,
   33,  108,   60,   34,   35,
};
final static short yysindex[] = {                       -80,
 -212,  -29,    0,    0,    0,    0,    0,    0,    0,   41,
   49,    0,  -80,    0,    0,   47,   51, -198,  -17,   61,
   63,   64,   67, -195,  -23,  -45,   -2,    0,    7,   12,
  -25,   71,   77,   28,    0, -184, -193,    0,    0,    0,
    0,   79,  -34,  -80,    0,    0,    0,    0,    0,   -3,
 -142,    0,   45,    0,    0,    0,    0,    0,    0,   12,
    0,    0, -173,   65,    4,    0,   12,   12,   12,   10,
    0,   -3,   12,   12,   81,   82,   84,   88, -169,    0,
 -127, -124,  -80, -133,   -3,   45,   73, -199,  -84, -122,
   45,   28,   28,    0,   45, -117,    0,    0,  -95,  -95,
    0,    0, -116,    0,  110, -104,    0, -102,    0, -111,
 -199,    0,   95,    0,  117,    0,    0,    0,    0,  -15,
  -98,    0,  -83,    0,  120,   12,    0,    0,    0,    0,
  121, -115,    0,   -3,  -96,  -89,  107,  -90,    0,   19,
    0,    0,  124,   -3,  126,    0,    0, -105,    0,   53,
    0,  130,    0,   -3,    0,    0,  132,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  182,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -41,    0,    0,    0,
    0,    0,    0,  -36,    0,    0,    0,    0,    0,    0,
    0,    6,  139,    0,    0,    0,    0,    0,    0,    0,
  140,    0,  141,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    5,    0,    0,    0,    0,
   15,  -31,  -11,    0,   17,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   33,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  142,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  150,   31,   27,    0,    0,  118,    0,    0,   16,    0,
    0,    0,    0,    0,  133,   87,   99,   83,  -18,    0,
  171,    0,  172,   25,   23,
};
final static int YYTABLESIZE=274;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         77,
   77,   77,   77,   77,   67,   77,   67,   67,   67,   65,
   29,   65,   65,   65,   59,   30,   58,   68,   77,   69,
   77,   30,  128,   67,   81,   67,   83,  121,   65,   66,
   65,   66,   66,   66,   59,  147,   58,   63,   83,   39,
   53,  138,  135,   38,   89,   58,   90,   25,   66,   13,
   66,   30,   66,   96,   30,   56,   30,   55,   42,  150,
   49,   68,   77,   69,   13,   78,  110,   50,   51,   73,
  117,   75,   76,   31,   74,   86,   31,  112,  113,  114,
   36,   77,   91,   61,   62,   95,   67,   68,   37,   69,
   40,   65,   92,   93,   41,   97,   98,    3,    4,    5,
    6,    7,    8,    9,   45,   44,   46,   47,   39,  107,
   48,   66,   38,  106,   71,  143,  145,   72,   79,   83,
   85,   99,  100,   88,  101,  152,   83,   58,  102,  104,
  109,  111,    1,  118,  129,  157,    2,   56,  119,   55,
  125,  140,    3,    4,    5,    6,    7,    8,    9,  126,
   10,  127,  130,  132,    1,  105,   11,  133,    2,  120,
  139,  141,  120,  142,  148,  149,  146,  151,  137,  153,
  154,  116,   10,  156,  137,  158,    1,  155,   11,  144,
    2,    1,    8,   43,   73,   27,    3,    4,    5,    6,
    7,    8,    9,   82,   10,   87,  103,  131,  124,   65,
   11,    0,   70,  136,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   77,    0,    0,    0,    0,   67,
    0,   80,    0,    0,   65,    0,   26,   27,   28,    0,
   67,    0,   52,   27,   28,    0,   54,   55,   56,   57,
   77,   77,   77,   77,   66,   67,   67,   67,   67,    0,
   65,   65,   65,   65,   61,   62,   54,   55,   56,   57,
  134,   13,   26,   27,   28,   94,   27,   28,   27,   28,
   66,   66,   66,   66,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   41,   47,   43,   44,   45,   41,
   40,   43,   44,   45,   60,   45,   62,   43,   60,   45,
   62,   45,  125,   60,   59,   62,  123,  123,   60,   41,
   62,   43,   44,   45,   60,  125,   62,   40,  123,   13,
   25,  125,   58,   13,   41,   41,   65,  260,   60,   44,
   62,   45,   30,   72,   45,   41,   45,   41,  257,   41,
  256,   43,  256,   45,   59,  259,   85,  263,  264,   42,
   89,  256,  257,   41,   47,   60,   44,  277,  278,  279,
   40,  123,   67,  257,  258,   70,  123,   43,   40,   45,
   44,  123,   68,   69,   44,   73,   74,  267,  268,  269,
  270,  271,  272,  273,   44,  123,   44,   44,   82,   83,
   44,  123,   82,   83,   44,  134,  135,   41,   40,  123,
  263,   41,   41,   59,   41,  144,  123,  123,   41,  257,
  264,   59,  257,  256,  108,  154,  261,  123,  256,  123,
  257,  126,  267,  268,  269,  270,  271,  272,  273,   40,
  275,  256,  264,   59,  257,  280,  281,   41,  261,  258,
   41,   41,  258,  279,   58,  256,  256,   44,  258,   44,
  276,  256,  275,   44,  258,   44,  257,  125,  281,  276,
  261,    0,   44,   44,   44,   44,  267,  268,  269,  270,
  271,  272,  273,   44,  275,   63,   79,  111,  100,   29,
  281,   -1,   31,  121,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  256,   -1,   -1,   -1,   -1,  256,
   -1,  256,   -1,   -1,  256,   -1,  256,  257,  258,   -1,
  256,   -1,  256,  257,  258,   -1,  282,  283,  284,  285,
  282,  283,  284,  285,  256,  282,  283,  284,  285,   -1,
  282,  283,  284,  285,  257,  258,  282,  283,  284,  285,
  276,  256,  256,  257,  258,  256,  257,  258,  257,  258,
  282,  283,  284,  285,
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
"encabezado : tipo ID '(' tipo ID ')'",
"declaracion_funcion : encabezado '{' list_sentencias RETURN '(' expresion ')' '}'",
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

//#line 457 "especificacion.y"
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


//#line 612 "Parser.java"
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
//#line 50 "especificacion.y"
{
								System.out.println("TERMINO GRAMATICA");
								this.raiz = (Nodo)val_peek(0).obj;
								raiz.imprimirNodo();							
							}
break;
case 3:
//#line 58 "especificacion.y"
{
				 						Nodo nuevo = new Nodo("S",(Nodo)val_peek(0).obj, null);
		 								if (raiz == null){
					 						raiz = nuevo;
					 						nuevo.setPadre(null);
		 								}
		 								yyval.obj = nuevo;
				 				   }
break;
case 5:
//#line 68 "especificacion.y"
{	
				 									Nodo nuevo = new Nodo("S", (Nodo)val_peek(0).obj, null);
				 									
					 								if (raiz == null){
					 									raiz = nuevo;
					 									nuevo.setPadre(null);
					 								}else{						 										
				 											((Nodo)val_peek(1).obj).setProximaSentencia(nuevo);
				 											nuevo.setPadre((Nodo)val_peek(1).obj);
					 									 }
					 								yyval.obj =nuevo;
					 								}
break;
case 7:
//#line 83 "especificacion.y"
{funciones.add((Nodo)val_peek(1).obj);}
break;
case 8:
//#line 86 "especificacion.y"
{/*System.out.println("Declaracion variable");*/
												 setRegla(((Token)val_peek(1).obj).getNroLine(), "Declaracion de variables", ((Token)val_peek(1).obj).getLexema());
												 updateTable(((Vector<Token>)val_peek(0).obj), ((Token)val_peek(1).obj).getLexema(), "Identificador de variable",ambito.get(ambito.size()-1));
												 System.out.println("lal");
												/*setAmbito(((Vector<Token>)$2.obj), ambito.getLast());												 */
												 }
break;
case 9:
//#line 92 "especificacion.y"
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
//#line 102 "especificacion.y"
{	
									  		/*System.out.println("Encabezado ambito: "+ambit+" - "+ambito.get(ambito.size()-1));*/
									  		
									  		String ambit = ambito.get(ambito.size()-1);
											Vector<Token> vec = new Vector<Token>(); 
									  		vec.add((Token)val_peek(4).obj);
									  		updateTable(vec, ((Token)val_peek(5).obj).getLexema(), FUNCION,ambit);	
									  		ambit = ((Token)val_peek(4).obj).getLexema();
											apilarAmbito(ambit);
									  		vec.removeAllElements();
									  		vec.add((Token)val_peek(1).obj);
									  		updateTable(vec, ((Token)val_peek(2).obj).getLexema(), PARAMETRO,ambit);
									  		Nodo nuevo = new Nodo(((Token)val_peek(4).obj).getLexema(),null,null);
									  		yyval.obj = nuevo;
									  		
										}
break;
case 11:
//#line 123 "especificacion.y"
{	
								 		System.out.println("wepa "+((Nodo)val_peek(5).obj).getLexema());
								  		Nodo padre = ((Nodo)val_peek(5).obj).getFuncionPadre();
								  		System.out.println("La primera del padre es "+padre.getLexema()+" -> "+(padre.getIzq().getLexema()+(padre.getIzq()).getDer().getLexema()));
								  		System.out.println();
								  		Nodo nuevo = ((Nodo)val_peek(7).obj);
								  		nuevo.setIzq(padre);					  		
								  		/*lo siguiente es para evitar que la raiz apunte a la primera sentencia de la funcion*/
								  		if (raiz == padre){
								  			System.out.println("ENTRO");
								  			raiz = null;
								  		}
					  		
					  					yyval.obj = nuevo;
					  					
					  					desapilarAmbito();
								 	 }
break;
case 12:
//#line 175 "especificacion.y"
{
											Vector<Token> tokens = (Vector<Token>)val_peek(2).obj;
											Token token = (Token)val_peek(0).obj;
											tokens.add(token);
											yyval.obj = tokens;
											
											}
break;
case 13:
//#line 182 "especificacion.y"
{
							Vector<Token> tokens = new Vector<Token>();
							Token token = (Token)val_peek(0).obj;
							tokens.add(token);
							yyval.obj = tokens;
							}
break;
case 16:
//#line 192 "especificacion.y"
{this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 17:
//#line 193 "especificacion.y"
{this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 18:
//#line 194 "especificacion.y"
{this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 19:
//#line 195 "especificacion.y"
{this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 20:
//#line 196 "especificacion.y"
{this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 21:
//#line 200 "especificacion.y"
{yyval.obj = (Nodo)val_peek(1).obj;}
break;
case 24:
//#line 203 "especificacion.y"
{yyval.obj = (Nodo)val_peek(1).obj;}
break;
case 25:
//#line 207 "especificacion.y"
{
																setRegla(((Token)val_peek(5).obj).getNroLine(), "Invocacion", ((Token)val_peek(5).obj).getLexema());
															 }
break;
case 26:
//#line 210 "especificacion.y"
{
																	addError("Error sintactico: falta '(' al inicio de la invocacion ", ((Token)val_peek(4).obj).getNroLine());
															 	}
break;
case 27:
//#line 214 "especificacion.y"
{
																addError("Error sintactico: falta ')' al final de la invocacion ", ((Token)val_peek(4).obj).getNroLine());
															}
break;
case 34:
//#line 230 "especificacion.y"
{System.out.println("Case do");
				  						  setRegla(((Token)val_peek(4).obj).getNroLine(), "Sentencia de control", ((Token)val_peek(4).obj).getLexema());												 
										 }
break;
case 35:
//#line 234 "especificacion.y"
{
				  						  addError("Error sintactico: condicion erronea ", ((Token)val_peek(3).obj).getNroLine());												 
										 }
break;
case 37:
//#line 240 "especificacion.y"
{addError("Error sintactico: falta '{' para iniciar el bloque de sentencias de control ", ((Token)val_peek(2).obj).getNroLine());}
break;
case 38:
//#line 241 "especificacion.y"
{addError("Error sintactico: falta '}' para terminar el bloque de sentencias de control ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 41:
//#line 247 "especificacion.y"
{addError("Error sintactico: falta ':' antes del 'do'", ((Token)val_peek(3).obj).getNroLine());}
break;
case 42:
//#line 248 "especificacion.y"
{addError("Error sintactico: falta 'do' despues del ':'", ((Token)val_peek(3).obj).getNroLine());}
break;
case 43:
//#line 251 "especificacion.y"
{
									/*Nodo nuevo = new Nodo("IF",);*/
									yyval.obj = (Nodo)val_peek(1).obj;
								}
break;
case 44:
//#line 256 "especificacion.y"
{
			   													setRegla(((Token)val_peek(2).obj).getNroLine(), "Sentencia de Control", "else");
			   													/*Nodo = new Nodo("IF",(Nodo)$3.obj,(Nodo)$5.obj);*/
			   													Nodo ifNodo = (Nodo)val_peek(3).obj;
			   													Nodo elseNodo = new Nodo("ELSE",(Nodo)val_peek(1).obj,null); 
			   													
			   													ifNodo.getDer().setDer(elseNodo);
			   													
			   			  								   }
break;
case 45:
//#line 265 "especificacion.y"
{ addError("Error sintactico: Falta palabra reservada 'end_if' luego del bloque ",((Token)val_peek(0).obj).getNroLine());}
break;
case 46:
//#line 266 "especificacion.y"
{ addError("Error sintactico: 'else' incorrecto luego del 'end_if' ",((Token)val_peek(2).obj).getNroLine());}
break;
case 47:
//#line 269 "especificacion.y"
{
			   				  	    	setRegla(((Token)val_peek(4).obj).getNroLine(), "Sentencia de Control", "if");
			   				  	    	Nodo thenNodo = new Nodo("THEN",(Nodo)val_peek(0).obj,null);
			   				  	    	Nodo cuerpoNodo = new Nodo("Cuerpo",thenNodo,null);
			   				  	    	Nodo nuevo = new Nodo("IF",(Nodo)val_peek(2).obj,cuerpoNodo);
			   				  	    	yyval.obj = nuevo;
			   			   		   }
break;
case 48:
//#line 277 "especificacion.y"
{
											addError("Falta parentesis de cierre ')'",((Token)val_peek(3).obj).getNroLine());
 									     }
break;
case 49:
//#line 281 "especificacion.y"
{
										  	addError("Falta parentesis de apertura '('",((Token)val_peek(3).obj).getNroLine());
 									     }
break;
case 50:
//#line 285 "especificacion.y"
{
				   		addError("Error sintactico en el bloque ",((Token)val_peek(3).obj).getNroLine());
 			      }
break;
case 51:
//#line 291 "especificacion.y"
{yyval.obj = (Nodo)val_peek(1).obj;}
break;
case 52:
//#line 294 "especificacion.y"
{
																					((Nodo)val_peek(1).obj).setDer((Nodo)val_peek(0).obj);	
																				}
break;
case 53:
//#line 297 "especificacion.y"
{
								 						Nodo nuevo = new Nodo("S",(Nodo)val_peek(0).obj, null);
								 						yyval.obj = nuevo;
								 				 	}
break;
case 54:
//#line 302 "especificacion.y"
{ 
															addError("Error sintáctico: no se permiten sentencias declarativas dentro de un bloque de control ",((Token)val_peek(1).obj).getNroLine());
														}
break;
case 55:
//#line 309 "especificacion.y"
{ 
														setRegla(((Token)val_peek(2).obj).getNroLine(), "expresion logica", ((Nodo)val_peek(1).obj).getLexema());
														Nodo comparador = new Nodo(((Nodo)val_peek(1).obj).getLexema(),((Token)val_peek(2).obj).getNodo(),((Token)val_peek(0).obj).getNodo());	
														Nodo nuevo = new Nodo("Condicion",comparador,null);
														yyval.obj = nuevo;
												  }
break;
case 56:
//#line 316 "especificacion.y"
{
													addError("Errorsintactico: Comparador invalido. ", ((Token)val_peek(2).obj).getNroLine());
												}
break;
case 57:
//#line 319 "especificacion.y"
{
													addError("Error sintactico: Expresion derecha invalida ", ((Token)val_peek(2).obj).getNroLine());
												}
break;
case 58:
//#line 322 "especificacion.y"
{
													addError("Error sintactico: Expresion izquierda invalida ", ((Token)val_peek(2).obj).getNroLine());
												}
break;
case 59:
//#line 327 "especificacion.y"
{
						 Nodo nuevo = new Nodo(">=");
						 yyval.obj = nuevo;
						}
break;
case 60:
//#line 331 "especificacion.y"
{	
						Nodo nuevo = new Nodo("<=");
						yyval.obj = nuevo;
		   				}
break;
case 61:
//#line 335 "especificacion.y"
{	
		   				Nodo nuevo = new Nodo("==");
		   				yyval.obj = nuevo;	
		   		   }
break;
case 62:
//#line 339 "especificacion.y"
{	
		   				Nodo nuevo = new Nodo("!=");
		   				yyval.obj = nuevo;
		   			  }
break;
case 63:
//#line 343 "especificacion.y"
{	
		   			Nodo nuevo = new Nodo(">");
		   			yyval.obj = nuevo;
		   		}
break;
case 64:
//#line 347 "especificacion.y"
{	
		   			Nodo nuevo = new Nodo("<");
		   			yyval.obj = nuevo;
		   		}
break;
case 65:
//#line 354 "especificacion.y"
{
									if (datosCompatibles(((Token)val_peek(2).obj).getRecord().getType(),((Token)val_peek(0).obj).getRecord().getType())){
		   								Nodo nuevo = new Nodo ("+",((Token)val_peek(2).obj).getNodo(),((Token)val_peek(0).obj).getNodo());
		   								yyval.obj = new Token(0, ((Token)val_peek(2).obj).getLexema() + "+" +((Token)val_peek(0).obj).getLexema(), ((Token)val_peek(2).obj).getNroLine(), "", null,nuevo);
		   							}else{
	   									addError("Error semántico: Los tipos de datos de la operacion + no coinciden. ", ((Token)val_peek(2).obj).getNroLine());
	   								}
								 }
break;
case 66:
//#line 362 "especificacion.y"
{
		  							if (datosCompatibles(((Token)val_peek(2).obj).getRecord().getType(),((Token)val_peek(0).obj).getRecord().getType())){
										Nodo nuevo = new Nodo ("-",(Nodo)val_peek(2).obj,(Nodo)val_peek(0).obj);
		   								yyval.obj = new Token(0, ((Token)val_peek(2).obj).getLexema() + "-" +((Token)val_peek(0).obj).getLexema(), ((Token)val_peek(2).obj).getNroLine(), "", null,nuevo);
		   							}else{
	   									addError("Error semántico: Los tipos de datos de la operacion - no coinciden. ", ((Token)val_peek(2).obj).getNroLine());
	   								}
								 }
break;
case 67:
//#line 370 "especificacion.y"
{
		  				yyval.obj = (Token)val_peek(0).obj;
		  			}
break;
case 68:
//#line 375 "especificacion.y"
{	
							
								if (datosCompatibles(((Token)val_peek(2).obj).getRecord().getType(),((Token)val_peek(0).obj).getRecord().getType())){
									Nodo nuevo = new Nodo ("*",((Token)val_peek(2).obj).getNodo(),((Token)val_peek(0).obj).getNodo());
		   							yyval.obj = new Token(0, ((Token)val_peek(2).obj).getLexema() + "*" +((Token)val_peek(0).obj).getLexema(), ((Token)val_peek(2).obj).getNroLine(), "", null,nuevo);
	   							}else{
	   								addError("Error semántico: Los tipos de datos de la operacion * no coinciden. ", ((Token)val_peek(2).obj).getNroLine());
	   							}
							}
break;
case 69:
//#line 384 "especificacion.y"
{
								if (datosCompatibles(((Token)val_peek(2).obj).getRecord().getType(),((Token)val_peek(0).obj).getRecord().getType())){
									Nodo nuevo = new Nodo ("/",((Token)val_peek(2).obj).getNodo(),((Token)val_peek(0).obj).getNodo());
		   							yyval.obj = new Token(0, ((Token)val_peek(2).obj).getLexema() + "/" +((Token)val_peek(0).obj).getLexema(), ((Token)val_peek(2).obj).getNroLine(), "", null,nuevo);
		   						}else{
	   								addError("Error semántico: Los tipo de datos de la operacion / no coinciden. ", ((Token)val_peek(2).obj).getNroLine());
	   							}
							}
break;
case 70:
//#line 392 "especificacion.y"
{
						yyval.obj = (Token)val_peek(0).obj;
					}
break;
case 71:
//#line 398 "especificacion.y"
{setRegla(((Token)val_peek(3).obj).getNroLine(), "Impresion",((Token)val_peek(3).obj).getLexema()+"("+((Token)val_peek(1).obj).getLexema()+")" ) ;}
break;
case 72:
//#line 399 "especificacion.y"
{addError("Error sintactico: el contenido de impresion debe ser una cadena. ", ((Token)val_peek(3).obj).getNroLine());}
break;
case 73:
//#line 402 "especificacion.y"
{	
											System.out.println("ASIGNA");
											if (isDeclarated((Token)val_peek(2).obj)){
											System.out.println("THEN");	
												setRegla(((Token)val_peek(2).obj).getNroLine(), "Asignacion", ((Token)val_peek(2).obj).getLexema()+":="+((Token)val_peek(0).obj).getLexema());
												Nodo nodoId = new Nodo(table.get(((Token)val_peek(2).obj).getLexema()));
												Nodo nuevo = new Nodo(":=", nodoId, ((Token)val_peek(0).obj).getNodo());
												yyval.obj = nuevo;
												registrarEscritura(((Token)val_peek(2).obj).getLexema());																							
											}
											else{
												System.out.println(((Token)val_peek(2).obj).getLexema()+ "  no esta declarada");
											}
										}
break;
case 74:
//#line 416 "especificacion.y"
{
							System.out.println("ERROR"); 
							addError("Asignacion erronea ", ((Token)val_peek(2).obj).getNroLine());
							
							/*FIXME arreglar esto, tendria que devolver un nodo para que no me tire error.*/
						 }
break;
case 75:
//#line 424 "especificacion.y"
{	
					Nodo nuevo = new Nodo(table.get(((Token)val_peek(0).obj).getLexema()));
					((Token)val_peek(0).obj).setNodo(nuevo);
	   			}
break;
case 76:
//#line 428 "especificacion.y"
{
	   				System.out.println("Un negative "+((Token)val_peek(0).obj).getRecord().getType());
	   				if (((Token)val_peek(0).obj).getRecord().getType() == "usinteger"){
	   					this.addError("Error sintactico: usinteger no puede ser negativio ",((Token)val_peek(0).obj).getNroLine());
	   					/*$$.obj = error;*/
	   				}else{
	   					updateTableNegative(   ((Token)val_peek(0).obj).getLexema()   );
	   					Nodo nuevo = new Nodo(table.get("-"+((Token)val_peek(0).obj).getLexema()));
						yyval.obj = new Token(0, "-"+((Token)val_peek(0).obj).getLexema(), ((Token)val_peek(1).obj).getNroLine(), "", null,nuevo);
	   				}

	   			 }
break;
case 77:
//#line 441 "especificacion.y"
{ 
	   			isDeclarated((Token)val_peek(0).obj);
	   			Nodo nuevo = new Nodo(table.get(((Token)val_peek(0).obj).getLexema()));
	   			System.out.println(table.get(((Token)val_peek(0).obj).getLexema()).getLexema());
	   			((Token)val_peek(0).obj).setNodo(nuevo);
	   		 }
break;
case 78:
//#line 448 "especificacion.y"
{
						yyval.obj = (Token)val_peek(1).obj;
					 }
break;
//#line 1226 "Parser.java"
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
