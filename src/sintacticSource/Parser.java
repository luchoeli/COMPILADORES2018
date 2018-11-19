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
    0,    1,    1,    1,    1,    2,    2,    2,    4,    4,
    8,    5,    5,    7,    7,    6,    6,    6,    6,    6,
    6,    6,    3,    3,    3,    3,   14,   15,   15,   15,
   15,   11,   11,   16,   16,   16,   17,   17,   17,   17,
   10,   10,   10,   10,   19,   19,   19,   19,   18,   21,
   21,   21,   21,   20,   20,   20,   20,   22,   22,   22,
   22,   22,   22,    9,    9,    9,   23,   23,   23,   23,
   12,   12,   13,   24,   24,   24,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    2,    2,    2,    1,    2,    3,
    6,    8,    4,    3,    1,    1,    1,    1,    1,    1,
    1,    1,    2,    2,    2,    2,    6,    1,    1,    1,
    3,    5,    5,    3,    3,    3,    5,    6,    4,    4,
    2,    4,    2,    5,    5,    5,    5,    5,    3,    1,
    1,    2,    2,    3,    3,    3,    3,    1,    1,    1,
    1,    1,    1,    3,    3,    1,    3,    3,    1,    2,
    4,    4,    3,    1,    2,    1,
};
final static short yydefred[] = {                         0,
    0,    0,   16,   17,   18,   22,   20,   19,   21,    0,
    0,    0,    0,    2,    3,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   74,    0,    0,
    0,    0,    0,    0,   69,    0,    0,    4,    5,    6,
    7,    0,    0,    0,   23,   24,   25,   26,   43,    0,
    0,    0,   58,   59,   60,   61,   62,   63,    0,    0,
    0,   76,   75,    0,    0,    0,    0,   70,    0,    0,
    0,    0,    0,    0,    0,    0,   10,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   56,
    0,    0,   67,   68,    0,    0,   72,   71,    0,   14,
    0,   13,   50,   51,    0,   42,    0,    0,   48,   45,
   46,   47,    0,    0,   33,    0,   32,    0,    0,   49,
   53,   52,   44,   28,    0,   30,    0,    0,    0,    0,
    0,   11,    0,    0,   27,    0,    0,    0,   36,   34,
   35,    0,   31,   39,    0,   40,   12,    0,   38,
};
final static short yydgoto[] = {                         12,
   13,   14,   15,   16,   17,   18,   43,   19,   31,   20,
   21,   22,   23,   32,  127,  115,  116,   81,   24,   33,
  105,   59,   34,   35,
};
final static short yysindex[] = {                      -123,
 -211,    3,    0,    0,    0,    0,    0,    0,    0,   23,
   29,    0, -123,    0,    0,   17,   41, -184,  -33,   49,
   55,   59,   60, -198,  -23,  -43,   76,    0,  -32,  -21,
  -18,   74,   77,   25,    0, -169, -188,    0,    0,    0,
    0,   79,  -27, -123,    0,    0,    0,    0,    0,   -2,
 -141,   31,    0,    0,    0,    0,    0,    0,  -23, -132,
   -3,    0,    0,  -23,  -23,  -23,  -25,    0,   -2,  -21,
  -21,   86,   88,   96,   98, -158,    0, -117,  -95, -123,
 -122,   -2,   31,   82,  -86, -105,   31,   25,   25,    0,
   31, -103,    0,    0,  -89,  -89,    0,    0, -102,    0,
  114,    0,    0,    0,  -78,    0, -108, -182,    0,    0,
    0,    0,  -58, -101,    0,   34,    0,  122,  -23,    0,
    0,    0,    0,    0,  105,    0,  124,   -2,  -94,  -85,
  -88,    0,   14, -112,    0,  140,   -2,  143,    0,    0,
    0,   63,    0,    0,  152,    0,    0, -101,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  198,    0,    0,   35,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -39,    0,    0,    0,
    0,    0,    0,  -34,    0,    0,    0,    0,    0,    0,
    0,    1,   18,    0,    0,    0,    0,    0,    0,    0,
   41,  155,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    5,    0,    0,    0,   12,  -29,   38,    0,
   13,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  159,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -75,    0,
};
final static short yygindex[] = {                         0,
  157,   26,   28,    0,    0,  126,    0,    0,   11,    0,
    0,    0,    0,    0,    0,  108,  -62,   -5,    0,  176,
    0,  175,   36,   21,
};
final static int YYTABLESIZE=323;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        129,
   15,   76,   76,   76,   76,   76,   66,   76,   66,   66,
   66,   64,   30,   64,   64,   64,   58,    9,   57,   30,
   76,   30,   76,   30,   65,   66,   66,   66,   80,  102,
   64,   78,   64,  114,    8,   52,   80,   85,   38,  140,
   39,   58,   29,   57,   15,   57,  120,   30,   25,   37,
   63,  130,   55,   54,  142,   86,   65,   49,   66,   15,
   40,    9,   36,   92,   50,   51,   70,   74,   37,   83,
   75,   71,   42,   65,   87,   66,  107,   91,   65,  110,
   65,   65,   65,   76,   41,  149,   72,   73,   66,   44,
   93,   94,   45,   64,  124,  125,  126,   65,   46,   65,
   88,   89,   47,   48,   38,  103,   39,  104,    3,    4,
    5,    6,    7,    8,    9,   60,   68,   69,   76,   80,
   80,   82,  136,  138,   84,   15,   95,   57,   96,  133,
  121,  145,  122,    1,   55,   54,   97,    2,   98,  100,
  108,  106,    9,    3,    4,    5,    6,    7,    8,    9,
  111,   10,  112,  119,  118,  123,  113,   11,  131,    8,
   65,    1,  132,  134,  135,    2,  143,  141,  113,  109,
  139,    3,    4,    5,    6,    7,    8,    9,    1,   10,
   37,  137,    2,  144,  101,   11,  146,  147,    3,    4,
    5,    6,    7,    8,    9,  148,   10,    1,   73,   29,
   79,   99,   11,  117,   61,   67,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   76,  128,    0,    0,
    0,   66,    0,   26,   27,   28,   64,    0,   77,    0,
   90,   27,   28,   27,   28,   62,   28,   64,   53,   54,
   55,   56,   76,   76,   76,   76,    0,   66,   66,   66,
   66,    0,   64,   64,   64,   64,   15,   15,   26,   27,
   28,   15,    0,   53,   54,   55,   56,   15,   15,   15,
   15,   15,   15,   15,    9,   15,    0,    0,    9,    0,
   15,   15,    0,    0,    9,    9,    9,    9,    9,    9,
    9,    8,    9,   65,    0,    8,    0,    9,    9,    0,
    0,    8,    8,    8,    8,    8,    8,    8,    0,    8,
    0,    0,    0,    0,    8,    8,    0,    0,    0,   65,
   65,   65,   65,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         58,
    0,   41,   42,   43,   44,   45,   41,   47,   43,   44,
   45,   41,   45,   43,   44,   45,   60,    0,   62,   45,
   60,   45,   62,   45,   43,   60,   45,   62,  123,  125,
   60,   59,   62,  123,    0,   25,  123,   41,   13,  125,
   13,   60,   40,   62,   44,   41,  125,   45,  260,  125,
   30,  114,   41,   41,   41,   61,   43,  256,   45,   59,
   44,   44,   40,   69,  263,  264,   42,  256,   40,   59,
  259,   47,  257,   43,   64,   45,   82,   67,   41,   85,
   43,   44,   45,  123,   44,  148,  256,  257,  123,  123,
   70,   71,   44,  123,  277,  278,  279,   60,   44,   62,
   65,   66,   44,   44,   79,   80,   79,   80,  267,  268,
  269,  270,  271,  272,  273,   40,   43,   41,   40,  123,
  123,  263,  128,  129,  257,  125,   41,  123,   41,  119,
  105,  137,  105,  257,  123,  123,   41,  261,   41,  257,
   59,  264,  125,  267,  268,  269,  270,  271,  272,  273,
  256,  275,  256,   40,  257,  264,  258,  281,  125,  125,
  123,  257,   41,   59,   41,  261,  279,  256,  258,  256,
  256,  267,  268,  269,  270,  271,  272,  273,  257,  275,
  256,  276,  261,   44,  280,  281,   44,  125,  267,  268,
  269,  270,  271,  272,  273,   44,  275,    0,   44,   41,
   44,   76,  281,   96,   29,   31,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  256,  276,   -1,   -1,
   -1,  256,   -1,  256,  257,  258,  256,   -1,  256,   -1,
  256,  257,  258,  257,  258,  257,  258,  256,  282,  283,
  284,  285,  282,  283,  284,  285,   -1,  282,  283,  284,
  285,   -1,  282,  283,  284,  285,  256,  257,  256,  257,
  258,  261,   -1,  282,  283,  284,  285,  267,  268,  269,
  270,  271,  272,  273,  257,  275,   -1,   -1,  261,   -1,
  280,  281,   -1,   -1,  267,  268,  269,  270,  271,  272,
  273,  257,  275,  256,   -1,  261,   -1,  280,  281,   -1,
   -1,  267,  268,  269,  270,  271,  272,  273,   -1,  275,
   -1,   -1,   -1,   -1,  280,  281,   -1,   -1,   -1,  282,
  283,  284,  285,
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
"sent_declarativa : declaracion_variable",
"declaracion_variable : tipo list_variables",
"declaracion_variable : tipo list_variables error",
"encabezado : tipo ID '(' tipo ID ')'",
"declaracion_funcion : encabezado '{' list_sentencias RETURN '(' expresion ')' '}'",
"declaracion_funcion : encabezado '{' list_sentencias '}'",
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
"invocacion : ID '(' ID ';' lista_permisos ')'",
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
"linea_control : CTE ':' DO bloque_sin_declaracion ',' linea_control",
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
"list_sentencias_no_declarables : sent_declarativa",
"list_sentencias_no_declarables : sent_ejecutable",
"list_sentencias_no_declarables : list_sentencias_no_declarables sent_ejecutable",
"list_sentencias_no_declarables : list_sentencias_no_declarables sent_declarativa",
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
"termino : invocacion '+'",
"imprimir : PRINT '(' CADENA ')'",
"imprimir : PRINT '(' error ')'",
"asignacion : ID ASIGNACION expresion",
"factor : CTE",
"factor : '-' factor",
"factor : ID",
};

//#line 642 "especificacion.y"
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

//#line 728 "Parser.java"
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
case 2:
//#line 57 "especificacion.y"
{yyval.obj = new Nodo("null",null,null);}
break;
case 3:
//#line 58 "especificacion.y"
{
				 						Nodo nuevo = new Nodo("S",(Nodo)val_peek(0).obj, null);
				 						((Nodo)val_peek(0).obj).setPadre(nuevo);
		 								if (raiz == null){
					 						raiz = nuevo;
					 						nuevo.setPadre(null);
		 								}
		 								yyval.obj = nuevo;
				 				   }
break;
case 4:
//#line 69 "especificacion.y"
{yyval.obj =  (Nodo)val_peek(1).obj;}
break;
case 5:
//#line 70 "especificacion.y"
{	
				 									Nodo nuevo = new Nodo("S", (Nodo)val_peek(0).obj, null);
				 									((Nodo)val_peek(0).obj).setPadre(nuevo);
				 									System.out.println(((Nodo)val_peek(0).obj).getLexema());
				 									
					 								if (raiz == null){
					 									System.out.println("si");
					 									raiz = nuevo;
					 									nuevo.setPadre(null);
					 									((Nodo)val_peek(0).obj).setPadre(nuevo);
					 									
					 								}else{	
					 									if (((Nodo)val_peek(1).obj).getLexema().equals("null")){		
					 										System.out.println("listapadre null");			 										
					 										((Nodo)val_peek(1).obj).setLexema("S");
				 											((Nodo)val_peek(1).obj).setIzq((Nodo)val_peek(0).obj);
				 											((Nodo)val_peek(1).obj).setDer(null);
				 											((Nodo)val_peek(0).obj).setPadre((Nodo)val_peek(1).obj); 
				 											
				 										}else{
				 											System.out.println("listapadre != null");
				 											((Nodo)val_peek(1).obj).setProximaSentencia(nuevo);
					 										/*nuevo.setPadre((Nodo)$1.obj);	 YA LO HAGO EN LA SENT ANTERIOR*/
				 										}
					 								}
					 								yyval.obj =nuevo;
					 								}
break;
case 7:
//#line 100 "especificacion.y"
{
													if (!((Nodo)val_peek(1).obj).equals(null)){
														funciones.add((Nodo)val_peek(1).obj);
													}
												}
break;
case 9:
//#line 108 "especificacion.y"
{
												 setRegla(((Token)val_peek(1).obj).getNroLine(), "Declaracion de variables", ((Token)val_peek(1).obj).getLexema());
												 updateTable(((Vector<Token>)val_peek(0).obj), ((Token)val_peek(1).obj).getLexema(), "Identificador de variable",ambito.get(ambito.size()-1));
												}
break;
case 10:
//#line 112 "especificacion.y"
{
														Vector<Token> tokens = (Vector<Token>)val_peek(1).obj;
														if (tokens.size()>1){
															this.addError("Error sintactico en declaración múltiple.", ((Token)val_peek(2).obj).getNroLine());
														}else{
															this.addError("Error sintactico en declaración.", ((Token)val_peek(2).obj).getNroLine());
														}
													}
break;
case 11:
//#line 122 "especificacion.y"
{	
										  	String ambit = ambito.get(ambito.size()-1);
									  		/*System.out.println("Encabezado ambito: "+ambit+" - "+ambito.get(ambito.size()-1));*/
									  		if (ambit.equals("main")){
									  			/*System.out.println("declarar ");*/
												Vector<Token> vec = new Vector<Token>(); 
										  		vec.add((Token)val_peek(4).obj);
										  		updateTable(vec, ((Token)val_peek(5).obj).getLexema(), FUNCION,ambit);	
										  		ambit = ((Token)val_peek(4).obj).getLexema();
												apilarAmbito(ambit);
										  		vec.removeAllElements();
										  		vec.add((Token)val_peek(1).obj);
										  		updateTable(vec, ((Token)val_peek(2).obj).getLexema(), PARAMETRO,ambit);
										  		Nodo nuevo = new Nodo(((Token)val_peek(4).obj).getLexema(),null,null);
										  		nuevo.setTableRec(table.get(((Token)val_peek(4).obj).getLexema()));
										  		yyval.obj = nuevo;
										  	}else{
										  		System.out.println("en "+ambit+"Error semantico: no se puede declarar "+((Token)val_peek(4).obj).getLexema());
										  		this.addError("Error semantico: no se puede declarar funcion dentro de otra ", ((Token)val_peek(4).obj).getNroLine());
										  		/*FIXME*/
										  		/*Error error = new Error("Error semantico: no se puede declarar funcion dentro de otra ",((Token)$5.obj).getNroLine());*/
										  		yyval.obj = new Nodo("null",null,null);
										  	}
									  		
										}
break;
case 12:
//#line 152 "especificacion.y"
{	
								 		/*System.out.println("wepa "+((Nodo)$3.obj).getLexema());*/
								  		Nodo padre = ((Nodo)val_peek(5).obj).getFuncionPadre();
								  		/*System.out.println("La primera del padre es "+padre.getLexema()+" -> "+(padre.getIzq().getLexema()+(padre.getIzq()).getDer().getLexema()));*/
								  		Nodo nuevo = ((Nodo)val_peek(7).obj);
								  		padre.setPadre(nuevo);
								  		nuevo.setIzq(padre);		
								  		/*agrego retorno */
								  		Nodo nodoIzqRetorno = new Nodo("_RET"+((Nodo)val_peek(7).obj).getLexema(), null, null);
								  		Nodo nodoDerRetorno = ((Token)val_peek(2).obj).getNodo();
								  		Nodo nodoRetorno = new Nodo(":=",nodoIzqRetorno, nodoDerRetorno);
								  		nodoIzqRetorno.setPadre(nodoRetorno);
								  		nodoIzqRetorno.setTableRec(((Nodo)val_peek(7).obj).getTableRec());
								  		nodoDerRetorno.setPadre(nodoRetorno);		
								  		padre.setProximaSentencia(nodoRetorno);  		
								  		/*lo siguiente es para evitar que la raiz apunte a la primera sentencia de la funcion*/
								  		if (raiz == padre){
								  			System.out.println("ENTRO");
								  			raiz = null;
								  		}
					  		
					  					yyval.obj = nuevo;
					  					
					  					desapilarAmbito();
								 	 }
break;
case 13:
//#line 179 "especificacion.y"
{
					  			System.out.println("rmpo");
					  			this.addError("Error sintactico: falta return en la declaracion de la funcion ", ((Token)val_peek(0).obj).getNroLine());
					  		}
break;
case 14:
//#line 186 "especificacion.y"
{
											Vector<Token> tokens = (Vector<Token>)val_peek(2).obj;
											Token token = (Token)val_peek(0).obj;
											tokens.add(token);
											yyval.obj = tokens;
											
											}
break;
case 15:
//#line 193 "especificacion.y"
{
							Vector<Token> tokens = new Vector<Token>();
							Token token = (Token)val_peek(0).obj;
							tokens.add(token);
							yyval.obj = tokens;
							}
break;
case 18:
//#line 203 "especificacion.y"
{this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 19:
//#line 204 "especificacion.y"
{this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 20:
//#line 205 "especificacion.y"
{this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 21:
//#line 206 "especificacion.y"
{this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 22:
//#line 207 "especificacion.y"
{this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 23:
//#line 211 "especificacion.y"
{yyval.obj = ((Token)val_peek(1).obj).getNodo();}
break;
case 25:
//#line 213 "especificacion.y"
{yyval.obj = ((Token)val_peek(1).obj).getNodo();}
break;
case 26:
//#line 214 "especificacion.y"
{yyval.obj = ((Token)val_peek(1).obj).getNodo();}
break;
case 27:
//#line 218 "especificacion.y"
{ 	if (isFunDeclarated(((Token)val_peek(5).obj).getLexema())){
														/*TODO chequear ambito de $2.obj !*/
														/*System.out.println("Declatada la funcion");*/
														System.out.println("amito actual "+ambito.get(ambito.size()-1));
														if (checkAmbito(((Token)val_peek(5).obj).getLexema())){
															if (checkPermisos( ((Token)val_peek(5).obj).getLexema() , ((String)val_peek(1).obj))){
																setRegla(((Token)val_peek(5).obj).getNroLine(), "Invocacion", ((Token)val_peek(5).obj).getLexema());
																Nodo nodoFun = new Nodo(((Token)val_peek(5).obj).getLexema(),null,null);
																TableRecord tr = table.get(((Token)val_peek(5).obj).getLexema());
																nodoFun.setTableRec(tr);
																Nodo nodoCall = new Nodo("Call",nodoFun,null);
																
																yyval.obj = new Token(0, ((Token)val_peek(5).obj).getLexema()+"()", ((Token)val_peek(5).obj).getNroLine(), tr.getType(), null,nodoCall);
															}else{
																addError("Error semantico: la funcion "+((Token)val_peek(5).obj).getLexema()+" no permite el pasaje de parametros por "+(String)val_peek(1).obj, ((Token)val_peek(5).obj).getNroLine());
																System.out.println("No cumple chqueo de permisos");
															}
														}else{
															/*no esta en el ambito correspondiente*/
														}
													}else{
														System.out.println("FUNC NO DECLARADA");
														addError("Error semantico: la funcion '"+((Token)val_peek(5).obj).getLexema()+"' no esta declarada", ((Token)val_peek(5).obj).getNroLine());
													}
												 }
break;
case 28:
//#line 258 "especificacion.y"
{yyval.obj = "READONLY";}
break;
case 29:
//#line 259 "especificacion.y"
{yyval.obj = "WRITE";}
break;
case 30:
//#line 260 "especificacion.y"
{yyval.obj = "PASS";}
break;
case 31:
//#line 261 "especificacion.y"
{yyval.obj = "WRITE;PASS";}
break;
case 32:
//#line 265 "especificacion.y"
{System.out.println("Case do");
										 /*TODO chequear ambito de $3.obj, */
							
				  						  setRegla(((Token)val_peek(4).obj).getNroLine(), "Sentencia de control", ((Token)val_peek(4).obj).getLexema());
				  						  TableRecord trID = table.get(((Token)val_peek(2).obj).getLexema());
				  						  Nodo nodoID = new Nodo(trID.getLexema(),null,null);
				  						  nodoID.setTableRec(trID);
				  						  Nodo nodo = new Nodo("CASE",(Nodo)val_peek(0).obj,nodoID);
				  						  nodoID.setPadre(nodo);
				  						  ((Nodo)val_peek(0).obj).setPadre(nodo);
				  						  yyval.obj = nodo;												 
										 }
break;
case 33:
//#line 278 "especificacion.y"
{
				  						  addError("Error sintactico: condicion erronea ", ((Token)val_peek(3).obj).getNroLine());												 
										 }
break;
case 34:
//#line 283 "especificacion.y"
{yyval.obj = ((Nodo)val_peek(1).obj);}
break;
case 35:
//#line 284 "especificacion.y"
{addError("Error sintactico: falta '{' para iniciar el bloque de sentencias de control ", ((Token)val_peek(2).obj).getNroLine());}
break;
case 36:
//#line 285 "especificacion.y"
{addError("Error sintactico: falta '}' para terminar el bloque de sentencias de control ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 37:
//#line 288 "especificacion.y"
{	
																Nodo padre = ((Nodo)val_peek(1).obj).getFuncionPadre();
																/*lo siguiente es para evitar que la raiz apunte a la primera sentencia de la funcion*/
														  		if (raiz == padre){
														  			System.out.println("ENTRO");
														  			raiz = null;
														  		}
																Nodo nodoLctrl = new Nodo(((Token)val_peek(4).obj).getLexema(),null,((Nodo)val_peek(1).obj));
																nodoLctrl.setTableRec( table.get(((Token)val_peek(4).obj).getLexema() ));
																padre.setPadre(nodoLctrl);
																yyval.obj = nodoLctrl;				
															}
break;
case 38:
//#line 300 "especificacion.y"
{	
																		Nodo padre = ((Nodo)val_peek(2).obj).getFuncionPadre();
																		/*lo siguiente es para evitar que la raiz apunte a la primera sentencia de la funcion*/
																  		if (raiz == padre){
																  			System.out.println("ENTRO");
																  			raiz = null;
																  		}
																			Nodo nodoLctrl = new Nodo(((Token)val_peek(5).obj).getLexema(),((Nodo)val_peek(0).obj),((Nodo)val_peek(2).obj));
																			nodoLctrl.setTableRec( table.get(((Token)val_peek(5).obj).getLexema() ));
																			((Nodo)val_peek(0).obj).setPadre(nodoLctrl);
																			((Nodo)val_peek(2).obj).setPadre(nodoLctrl);
																			yyval.obj = nodoLctrl;
																		}
break;
case 39:
//#line 317 "especificacion.y"
{addError("Error sintactico: falta ':' antes del 'do'", ((Token)val_peek(3).obj).getNroLine());}
break;
case 40:
//#line 318 "especificacion.y"
{addError("Error sintactico: falta 'do' despues del ':'", ((Token)val_peek(3).obj).getNroLine());}
break;
case 41:
//#line 321 "especificacion.y"
{
									/*$$.obj = ((Token)$1.obj).getNodo();*/
								}
break;
case 42:
//#line 325 "especificacion.y"
{
			   													Nodo padre = ((Nodo)val_peek(1).obj).getFuncionPadre();
																/*lo siguiente es para evitar que la raiz apunte a la primera sentencia de la funcion*/
														  		if (raiz == padre){
														  			System.out.println("ENTRO");
														  			raiz = null;
														  		}
			   													setRegla(((Token)val_peek(2).obj).getNroLine(), "Sentencia de Control", "else");
			   													Nodo ifNodo = ((Token)val_peek(3).obj).getNodo();
			   													Nodo cuerpo = ifNodo.getDer();
			   													Nodo elseNodo = new Nodo("ELSE",(Nodo)val_peek(1).obj,null); 
			   													
			   													((Nodo)val_peek(1).obj).setPadre(elseNodo);
			   													elseNodo.setPadre(cuerpo);
			   													cuerpo.setDer(elseNodo);
			   													
			   													
			   			  								   }
break;
case 43:
//#line 343 "especificacion.y"
{ addError("Error sintactico: Falta palabra reservada 'end_if' luego del bloque ",((Token)val_peek(0).obj).getNroLine());}
break;
case 44:
//#line 344 "especificacion.y"
{ addError("Error sintactico: 'else' incorrecto luego del 'end_if' ",((Token)val_peek(2).obj).getNroLine());}
break;
case 45:
//#line 347 "especificacion.y"
{	
										Nodo padre = ((Nodo)val_peek(0).obj).getFuncionPadre();
										/*lo siguiente es para evitar que la raiz apunte a la primera sentencia de la funcion*/
								  		if (raiz == padre){
								  			System.out.println("ENTRO");
								  			raiz = null;
								  		}
			   				  	    	setRegla(((Token)val_peek(4).obj).getNroLine(), "Sentencia de Control", "if");
			   				  	    	Nodo thenNodo = new Nodo("THEN",(Nodo)val_peek(0).obj,null);
			   				  	    	Nodo cuerpoNodo = new Nodo("Cuerpo",thenNodo,null);
			   				  	    	Nodo nuevo = new Nodo("IF",(Nodo)val_peek(2).obj,cuerpoNodo);
			   				  	    	((Nodo)val_peek(0).obj).setPadre(thenNodo);
			   				  	    	((Nodo)val_peek(2).obj).setPadre(nuevo);
			   				  	    	thenNodo.setPadre(cuerpoNodo);
			   				  	    	cuerpoNodo.setPadre(nuevo);
										((Token)val_peek(4).obj).setNodo(nuevo);
			   				  	    	/*$$.obj = nuevo;*/
			   			   		   }
break;
case 46:
//#line 366 "especificacion.y"
{
											addError("Falta parentesis de cierre ')'",((Token)val_peek(3).obj).getNroLine());
											((Token)val_peek(4).obj).setNodo(new Nodo("null",null,null));
											yyval.obj = ((Token)val_peek(4).obj);
 									     }
break;
case 47:
//#line 372 "especificacion.y"
{
										  	addError("Falta parentesis de apertura '('",((Token)val_peek(3).obj).getNroLine());
										  	((Token)val_peek(4).obj).setNodo(new Nodo("null",null,null));
											yyval.obj = ((Token)val_peek(4).obj);
 									     }
break;
case 48:
//#line 378 "especificacion.y"
{
				   		addError("Error sintactico en el bloque ",((Token)val_peek(3).obj).getNroLine());
				   		((Token)val_peek(4).obj).setNodo(new Nodo("null",null,null));
						yyval.obj = ((Token)val_peek(4).obj);
 			      }
break;
case 49:
//#line 386 "especificacion.y"
{yyval.obj = ((Nodo)val_peek(1).obj).getFuncionPadre();}
break;
case 50:
//#line 388 "especificacion.y"
{
														yyval.obj = new Nodo("null",null,null);
														addError("Error semantico: no se permiten sentencias declarativas dentro de un bloque de control ",((Token)val_peek(0).obj).getNroLine());
													 }
break;
case 51:
//#line 392 "especificacion.y"
{
								 						Nodo nuevo = new Nodo("S",(Nodo)val_peek(0).obj, null);
								 						((Nodo)val_peek(0).obj).setPadre(nuevo);
						 								if (raiz == null){
									 						raiz = nuevo;
									 						nuevo.setPadre(null);
						 								}
						 								yyval.obj = nuevo;
								 				   }
break;
case 52:
//#line 401 "especificacion.y"
{	
												 									Nodo nuevo = new Nodo("S", (Nodo)val_peek(0).obj, null);
												 									((Nodo)val_peek(0).obj).setPadre(nuevo);
													 								if (raiz == null){
													 									raiz = nuevo;
					 																	nuevo.setPadre(null);
					 																	((Nodo)val_peek(0).obj).setPadre(nuevo);
													 									
													 								}else{	
													 									if (((Nodo)val_peek(1).obj).getLexema().equals("null")){					 										
													 										((Nodo)val_peek(1).obj).setLexema("S");
												 											((Nodo)val_peek(1).obj).setIzq((Nodo)val_peek(0).obj);
												 											((Nodo)val_peek(1).obj).setDer(null); 
												 											((Nodo)val_peek(0).obj).setPadre((Nodo)val_peek(1).obj);
												 										}else{
												 											((Nodo)val_peek(1).obj).setProximaSentencia(nuevo);
													 										/*nuevo.setPadre((Nodo)$1.obj);	*/
												 										}
													 								}
													 								yyval.obj =nuevo;
													 								}
break;
case 53:
//#line 422 "especificacion.y"
{
				 																			yyval.obj =  (Nodo)val_peek(1).obj;
																							addError("Error semantico: no se permiten sentencias declarativas dentro de un bloque de control ",((Token)val_peek(0).obj).getNroLine());
				 																	}
break;
case 54:
//#line 432 "especificacion.y"
{ 
														setRegla(((Token)val_peek(2).obj).getNroLine(), "expresion logica", ((Nodo)val_peek(1).obj).getLexema());
														Nodo comp1 =((Token)val_peek(2).obj).getNodo();
														Nodo comp2 =((Token)val_peek(0).obj).getNodo();
														Nodo comparador = new Nodo(((Nodo)val_peek(1).obj).getLexema(),((Token)val_peek(2).obj).getNodo(),((Token)val_peek(0).obj).getNodo());	
														comp1.setPadre(comparador);
														comp2.setPadre(comparador);
														Nodo nuevo = new Nodo("Condicion",comparador,null);
														comparador.setPadre(nuevo);
														yyval.obj = nuevo;
												  }
break;
case 55:
//#line 444 "especificacion.y"
{
													addError("Errorsintactico: Comparador invalido. ", ((Token)val_peek(2).obj).getNroLine());
												}
break;
case 56:
//#line 447 "especificacion.y"
{
													addError("Error sintactico: Expresion derecha invalida ", ((Token)val_peek(2).obj).getNroLine());
												}
break;
case 57:
//#line 450 "especificacion.y"
{
													addError("Error sintactico: Expresion izquierda invalida ", ((Token)val_peek(2).obj).getNroLine());
												}
break;
case 58:
//#line 455 "especificacion.y"
{
						 Nodo nuevo = new Nodo(">=");
						 yyval.obj = nuevo;
						}
break;
case 59:
//#line 459 "especificacion.y"
{	
						Nodo nuevo = new Nodo("<=");
						yyval.obj = nuevo;
		   				}
break;
case 60:
//#line 463 "especificacion.y"
{	
		   				Nodo nuevo = new Nodo("=");
		   				yyval.obj = nuevo;	
		   		   }
break;
case 61:
//#line 467 "especificacion.y"
{	
		   				Nodo nuevo = new Nodo("!=");
		   				yyval.obj = nuevo;
		   			  }
break;
case 62:
//#line 471 "especificacion.y"
{	
		   			Nodo nuevo = new Nodo(">");
		   			yyval.obj = nuevo;
		   		}
break;
case 63:
//#line 475 "especificacion.y"
{	
		   			Nodo nuevo = new Nodo("<");
		   			yyval.obj = nuevo;
		   		}
break;
case 64:
//#line 482 "especificacion.y"
{	
		   								
									Nodo nuevo = new Nodo ("+",((Token)val_peek(2).obj).getNodo(),((Token)val_peek(0).obj).getNodo());
									if (datosCompatibles(((Token)val_peek(2).obj).getRecord().getType(),((Token)val_peek(0).obj).getRecord().getType())){
										String type = ((Token)val_peek(0).obj).getRecord().getType();
										((Token)val_peek(2).obj).getNodo().setPadre(nuevo);
										((Token)val_peek(0).obj).getNodo().setPadre(nuevo);
		   								yyval.obj = new Token(0, ((Token)val_peek(2).obj).getLexema() + "+" +((Token)val_peek(0).obj).getLexema(), ((Token)val_peek(2).obj).getNroLine(), type, null,nuevo);
		   							}else{
	   									addError("Error semántico: Los tipos de datos de la operacion + no coinciden. ", ((Token)val_peek(2).obj).getNroLine());
	   									yyval.obj = new Token(0, ((Token)val_peek(2).obj).getLexema() + "+" +((Token)val_peek(0).obj).getLexema(), ((Token)val_peek(2).obj).getNroLine(), "", null,nuevo);
	   								}
								 }
break;
case 65:
//#line 495 "especificacion.y"
{
									Nodo nuevo = new Nodo ("-",((Token)val_peek(2).obj).getNodo(),((Token)val_peek(0).obj).getNodo());
		  							if (datosCompatibles(((Token)val_peek(2).obj).getRecord().getType(),((Token)val_peek(0).obj).getRecord().getType())){
		  								String type = ((Token)val_peek(0).obj).getRecord().getType();
										((Token)val_peek(2).obj).getNodo().setPadre(nuevo);
										((Token)val_peek(0).obj).getNodo().setPadre(nuevo);
		   								yyval.obj = new Token(0, ((Token)val_peek(2).obj).getLexema() + "-" +((Token)val_peek(0).obj).getLexema(), ((Token)val_peek(2).obj).getNroLine(), type, null,nuevo);
		   							}else{
	   									addError("Error semántico: Los tipos de datos de la operacion - no coinciden. ", ((Token)val_peek(2).obj).getNroLine());
	   								}
								 }
break;
case 66:
//#line 506 "especificacion.y"
{
		  				yyval.obj = (Token)val_peek(0).obj;
		  			}
break;
case 67:
//#line 511 "especificacion.y"
{	
							
								if (datosCompatibles(((Token)val_peek(2).obj).getRecord().getType(),((Token)val_peek(0).obj).getRecord().getType())){
									String type = ((Token)val_peek(0).obj).getRecord().getType();
									Nodo nuevo = new Nodo ("*",((Token)val_peek(2).obj).getNodo(),((Token)val_peek(0).obj).getNodo());
									Token token = new Token(0, ((Token)val_peek(2).obj).getLexema() + "*" +((Token)val_peek(0).obj).getLexema(), ((Token)val_peek(2).obj).getNroLine(), type, null,nuevo);
									token.setRecord(((Token)val_peek(2).obj).getRecord());
		   							yyval.obj = token; 
	   							}else{
	   								/*$$.obj  = new Error("ss", 2);*/
	   								addError("Error semántico: Los tipos de datos de la operacion * no coinciden. ", ((Token)val_peek(2).obj).getNroLine());
	   							}
							}
break;
case 68:
//#line 524 "especificacion.y"
{
								if (datosCompatibles(((Token)val_peek(2).obj).getRecord().getType(),((Token)val_peek(0).obj).getRecord().getType())){
									String type = ((Token)val_peek(0).obj).getRecord().getType();
									Nodo nuevo = new Nodo ("/",((Token)val_peek(2).obj).getNodo(),((Token)val_peek(0).obj).getNodo());
									Token token = new Token(0, ((Token)val_peek(2).obj).getLexema() + "*" +((Token)val_peek(0).obj).getLexema(), ((Token)val_peek(2).obj).getNroLine(), type, null,nuevo);
									token.setRecord(((Token)val_peek(2).obj).getRecord());
		   							yyval.obj = token;
		   						}else{
	   								addError("Error semántico: Los tipo de datos de la operacion / no coinciden. ", ((Token)val_peek(2).obj).getNroLine());
	   							}
							}
break;
case 69:
//#line 535 "especificacion.y"
{
						yyval.obj = (Token)val_peek(0).obj;
					}
break;
case 70:
//#line 538 "especificacion.y"
{
							yyval.obj = (Token)val_peek(1).obj;
						}
break;
case 71:
//#line 546 "especificacion.y"
{	
					/*System.out.println(((Token)$1.obj).getRecord().getIdToken());*/
					/*	if (((Token)$1.obj).getRecord().getIdToken()==lexico.CADENA){*/
						setRegla(((Token)val_peek(3).obj).getNroLine(), "Impresion",((Token)val_peek(3).obj).getLexema()+"("+((Token)val_peek(1).obj).getLexema()+")" ) ;
						Nodo nodoCadena = new Nodo(((Token)val_peek(1).obj).getLexema());
						Nodo nuevo = new Nodo(((Token)val_peek(3).obj).getLexema(), nodoCadena, null);
						((Token)val_peek(3).obj).setNodo(nuevo);
					/*}*/
				}
break;
case 72:
//#line 555 "especificacion.y"
{	
										System.out.println("je");
										Nodo nuevo = new Nodo("error", null , null);
										((Token)val_peek(3).obj).setNodo(nuevo);
										addError("Error sintactico: el contenido de impresion debe ser una cadena. ", ((Token)val_peek(3).obj).getNroLine());
									}
break;
case 73:
//#line 563 "especificacion.y"
{	

											((Token)val_peek(2).obj).setNodo(new Nodo(null, null, ((Token)val_peek(0).obj).getNodo())); /*nodo basura*/
											((Token)val_peek(2).obj).setRecord(table.get(((Token)val_peek(2).obj).getLexema()));								
											if (isDeclarated((Token)val_peek(2).obj)){
												if (datosCompatibles( ((Token)val_peek(2).obj).getRecord().getType(),((Token)val_peek(0).obj).getType())){
													if (checkAmbito(((Token)val_peek(2).obj).getLexema())){
														setRegla(((Token)val_peek(2).obj).getNroLine(), "Asignacion", ((Token)val_peek(2).obj).getLexema()+":="+((Token)val_peek(0).obj).getLexema());
														Nodo nodoId = new Nodo(table.get(((Token)val_peek(2).obj).getLexema()));
														Nodo nuevo = new Nodo(":=", nodoId, ((Token)val_peek(0).obj).getNodo());
														nodoId.setPadre(nuevo);
														System.out.println("Datos Compatibles");
														((Token)val_peek(0).obj).getNodo().setPadre(nuevo);
														/*$$.obj = nuevo;*/
														((Token)val_peek(2).obj).setNodo(nuevo);
														registrarEscritura(((Token)val_peek(2).obj).getLexema());
													}else{
														addError("Error Semantico: identificador '"+((Token)val_peek(2).obj).getLexema()+"' no pertenece al ambito '"+ambito.get(ambito.size()-1)+"'.",((Token)val_peek(2).obj).getNroLine());
													}
												}else{
												 	
													addError("Error semántico: Los tipos de la asignacion no coinciden. ", ((Token)val_peek(1).obj).getNroLine());
												}
											}
											else{
														System.out.println(((Token)val_peek(2).obj).getLexema()+ "  no esta declarada");
														/*$$.obj = new Nodo(null, null, ((Token)$3.obj).getNodo());*/
														addError("Error Sintactico: identificador '"+((Token)val_peek(2).obj).getLexema()+"' no esta declarado.",((Token)val_peek(2).obj).getNroLine());
											}
										}
break;
case 74:
//#line 603 "especificacion.y"
{	
					TableRecord tr = table.get(((Token)val_peek(0).obj).getLexema());
					Nodo nuevo = new Nodo(table.get(((Token)val_peek(0).obj).getLexema()));
					((Token)val_peek(0).obj).setNodo(nuevo);
	   			}
break;
case 75:
//#line 608 "especificacion.y"
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
case 76:
//#line 621 "especificacion.y"
{ 
	   
	   			isDeclarated((Token)val_peek(0).obj);
	   			/*TODO ¿chequeo ambito? ¿chequeo de declaracion?*/
	   			System.out.println(((Token)val_peek(0).obj).getLexema());
	   			Nodo nuevo = new Nodo(table.get(((Token)val_peek(0).obj).getLexema()));
	   			TableRecord tr = table.get(((Token)val_peek(0).obj).getLexema());
	   			nuevo.setTableRec(tr);
	   			((Token)val_peek(0).obj).setNodo(nuevo);
	   			((Token)val_peek(0).obj).setRecord(tr);
	   			 
	   		 }
break;
//#line 1569 "Parser.java"
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
