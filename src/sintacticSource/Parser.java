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
	import java.util.Enumeration;
	import java.util.Vector;
//#line 24 "Parser.java"




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
public final static short IF=260;
public final static short THEN=261;
public final static short ELSE=262;
public final static short END_IF=263;
public final static short BEGIN=264;
public final static short END=265;
public final static short USINTEGER=266;
public final static short DOUBLE=267;
public final static short LONG=268;
public final static short INTEGER=269;
public final static short LINTEGER=270;
public final static short USLINTEGER=271;
public final static short SINGLE=272;
public final static short WHILE=273;
public final static short CASE=274;
public final static short DO=275;
public final static short READONLY=276;
public final static short WRITE=277;
public final static short PASS=278;
public final static short RETURN=279;
public final static short PRINT=280;
public final static short MAYORIGUAL=281;
public final static short MENORIGUAL=282;
public final static short IGUAL=283;
public final static short DISTINTO=284;
public final static short EOF=285;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    1,    1,    2,    2,    4,    4,    5,
    7,    7,    6,    6,    6,    6,    6,    6,    6,    3,
    3,    3,    3,    3,   13,   13,   13,   14,   14,   15,
   15,   15,   15,   10,   16,   16,    9,    9,   18,   18,
   18,   19,   21,   21,   20,   22,   22,   22,   22,   22,
   22,    8,    8,    8,   23,   23,   23,   11,   12,   12,
   24,   24,   17,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    2,    2,    2,    2,    3,   13,
    3,    1,    1,    1,    1,    1,    1,    1,    1,    2,
    2,    2,    2,    2,    6,    6,    6,    1,    1,    1,
    1,    1,    3,    7,    5,    6,    2,    4,    6,    6,
    6,    3,    2,    1,    3,    1,    1,    1,    1,    1,
    1,    3,    3,    1,    3,    3,    1,    4,    3,    2,
    1,    1,    3,
};
final static short yydefred[] = {                         0,
    0,    0,   13,   14,   15,   19,   17,   16,   18,    0,
    0,    0,    0,    2,    3,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   60,   28,   29,    0,    0,    0,
   62,   61,    0,    0,    0,    0,   57,    0,    0,    4,
    5,    6,    7,    0,    0,   20,   21,   22,   23,   24,
    0,   37,    0,    0,    0,    0,   46,   47,   48,   49,
   50,   51,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    9,    0,    0,    0,    0,   30,    0,   32,    0,
    0,    0,    0,    0,    0,    0,   55,   56,    0,   58,
    0,   11,   44,    0,   38,    0,    0,    0,    0,    0,
    0,    0,    0,   42,   43,   27,   25,   33,   26,   40,
   39,   41,    0,    0,    0,    0,    0,   34,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   35,    0,    0,
   63,   36,    0,    0,   10,
};
final static short yydgoto[] = {                         12,
   13,   14,   15,   16,   17,   18,   45,   34,   19,   20,
   21,   22,   23,   30,   80,  114,  124,   24,   75,   35,
   94,   65,   36,   37,
};
final static short yysindex[] = {                      -198,
  -40,  -27,    0,    0,    0,    0,    0,    0,    0,   24,
   41,    0, -198,    0,    0,   57,   58, -172,   59,   60,
   61,   63,   64, -218,    0,    0,    0, -174, -162,   50,
    0,    0, -162,  -25,   69,  -13,    0, -146, -147,    0,
    0,    0,    0,   73,  -58,    0,    0,    0,    0,    0,
   -9,    0,   56,   22, -199,  -38,    0,    0,    0,    0,
    0,    0, -162, -162, -162, -145, -162, -162,   76,   77,
 -179,    0, -138, -217, -143, -199,    0,   62,    0,   81,
   -9, -137,  -13,  -13,   22,   -9,    0,    0,    2,    0,
 -134,    0,    0,  -89,    0,  -22, -152, -129, -128,   -9,
 -127, -126,   89,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   75,  -93,   15, -140,   79,    0, -198,   17,
 -133, -219, -198,   99,   17,  104, -108,    0,  101, -162,
    0,    0,   13,   21,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  147,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -39,    0,    0,    0,    0,
    0,    0,    0,  -20,  106,    0,    0,    0,    0,    0,
    0,    0,    0,  107,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -19,    0,    0,
    0,    0,  -34,  -29,  -33,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -77,   14,   12,    0,    0,   82,    0,    1,    0,    0,
    0,    0,    0,  120,   78,    0,   30,    0,   -6,  123,
    0,    0,   34,   32,
};
final static int YYTABLESIZE=259;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         28,
   73,   54,   82,   54,   54,   54,   52,   45,   52,   52,
   52,   53,   33,   53,   53,   53,  131,   63,  107,   64,
   54,   31,   54,   12,   41,   52,   40,   52,   67,   54,
   53,  118,   53,   68,   62,  104,   61,    1,   12,    1,
    2,  122,    2,   51,   52,  127,    3,    4,    5,    6,
    7,    8,    9,  134,   10,   63,   10,   64,    1,  126,
   11,    2,   11,   38,   63,   85,   64,    3,    4,    5,
    6,    7,    8,    9,   99,   10,   77,   78,   79,  101,
   39,   11,   26,   27,   44,   93,    3,    4,    5,    6,
    7,    8,    9,  111,   31,   32,   83,   84,   87,   88,
   42,   43,   46,   47,   48,  105,   49,   50,   55,   66,
   69,   70,   71,   74,   76,   86,   89,   90,   92,   95,
   97,   98,  103,  100,  102,  108,  109,  110,  112,  115,
  133,  113,  116,   41,  120,   40,  121,  119,   41,  123,
   40,  125,  128,  130,  132,  135,    1,   53,    1,    8,
   59,    2,   91,   96,  129,   56,    0,    3,    4,    5,
    6,    7,    8,    9,  117,   10,    0,    1,    0,    0,
    2,   11,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   10,    0,    0,    0,    0,    0,
   11,    0,    0,    0,    0,    0,    0,   72,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   25,   26,   27,    0,    0,
    0,   54,   81,    0,    0,    0,   52,   45,    0,   31,
   32,   53,    0,  106,    0,   12,   31,    0,    0,    0,
    0,   54,   54,   54,   54,   29,   52,   52,   52,   52,
    0,   53,   53,   53,   53,   57,   58,   59,   60,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   59,   41,   41,   43,   44,   45,   41,   41,   43,   44,
   45,   41,   40,   43,   44,   45,  125,   43,   41,   45,
   60,   41,   62,   44,   13,   60,   13,   62,   42,   29,
   60,  125,   62,   47,   60,  125,   62,  257,   59,  257,
  260,  119,  260,  262,  263,  123,  266,  267,  268,  269,
  270,  271,  272,   41,  274,   43,  274,   45,  257,  279,
  280,  260,  280,   40,   43,   65,   45,  266,  267,  268,
  269,  270,  271,  272,   81,  274,  276,  277,  278,   86,
   40,  280,  257,  258,  257,   74,  266,  267,  268,  269,
  270,  271,  272,  100,  257,  258,   63,   64,   67,   68,
   44,   44,   44,   44,   44,   94,   44,   44,   59,   41,
  257,  259,   40,  123,   59,  261,   41,   41,  257,  263,
   59,   41,  257,  261,  123,  278,  256,  256,  256,   41,
  130,  258,   58,  122,  275,  122,   58,  123,  127,  123,
  127,  275,   44,   40,   44,  125,    0,   28,  257,   44,
   44,  260,   71,   76,  125,   33,   -1,  266,  267,  268,
  269,  270,  271,  272,  258,  274,   -1,  257,   -1,   -1,
  260,  280,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  274,   -1,   -1,   -1,   -1,   -1,
  280,   -1,   -1,   -1,   -1,   -1,   -1,  256,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  256,  257,  258,   -1,   -1,
   -1,  261,  261,   -1,   -1,   -1,  261,  261,   -1,  257,
  258,  261,   -1,  256,   -1,  256,  256,   -1,   -1,   -1,
   -1,  281,  282,  283,  284,  286,  281,  282,  283,  284,
   -1,  281,  282,  283,  284,  281,  282,  283,  284,
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
null,null,null,null,null,null,null,"ID","CTE","CADENA","IF","THEN","ELSE",
"END_IF","BEGIN","END","USINTEGER","DOUBLE","LONG","INTEGER","LINTEGER",
"USLINTEGER","SINGLE","WHILE","CASE","DO","READONLY","WRITE","PASS","RETURN",
"PRINT","MAYORIGUAL","MENORIGUAL","IGUAL","DISTINTO","EOF","\":=\"",
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
"invocacion : ID nombre_parametro ';' lista_permisos ')' error",
"invocacion : ID '(' nombre_parametro ';' lista_permisos error",
"nombre_parametro : ID",
"nombre_parametro : CTE",
"lista_permisos : READONLY",
"lista_permisos : WRITE",
"lista_permisos : PASS",
"lista_permisos : WRITE ';' PASS",
"sent_control : CASE '(' ID ')' '{' linea_control '}'",
"linea_control : CTE ':' DO bloque_de_sentencias ','",
"linea_control : linea_control CTE ':' DO bloque_de_sentencias ','",
"sent_seleccion : sent_if END_IF",
"sent_seleccion : sent_if ELSE bloque_sin_declaracion END_IF",
"sent_if : IF '(' expresion_logica ')' THEN bloque_sin_declaracion",
"sent_if : IF '(' expresion_logica THEN bloque_sin_declaracion error",
"sent_if : IF expresion_logica ')' THEN bloque_sin_declaracion error",
"bloque_sin_declaracion : '{' list_sentencias_no_declarables '}'",
"list_sentencias_no_declarables : list_sentencias_no_declarables sent_ejecutable",
"list_sentencias_no_declarables : sent_ejecutable",
"expresion_logica : expresion comparador expresion",
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
"asignacion : ID \":=\" expresion",
"asignacion : ID error",
"factor : CTE",
"factor : ID",
"bloque_de_sentencias : '{' list_sentencias '}'",
};

//#line 268 "especificacion.y"
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

public void updateTable(Vector<Token> tokens, String type){
	Enumeration e = tokens.elements();
	while (e.hasMoreElements()){
		Token token = (Token)e.nextElement();
		TableRecord tr = token.getRecord();
		String lexema = tr.getLexema();
		String newKey = lexema;// + getAlcance(alcance);
		System.out.println("TIPO: "+type);
		if ((table.contains(newKey))){
			System.out.println("TIPO: "+type);
			table.get(newKey).setType(type);
		}
		
	}
}
//#line 428 "Parser.java"
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
//#line 95 "especificacion.y"
{System.out.println("TERMINO GRAMATICA");}
break;
case 8:
//#line 109 "especificacion.y"
{System.out.println("Declaracion variable");
												 setRegla(((Token)val_peek(1).obj).getNroLine(), "Declaracion de variables", ((Token)val_peek(1).obj).getLexema());
												 updateTable(((Vector<Token>)val_peek(0).obj), ((Token)val_peek(1).obj).getLexema());												 
												 }
break;
case 9:
//#line 113 "especificacion.y"
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
//#line 127 "especificacion.y"
{setRegla(((Token)val_peek(12).obj).getNroLine(), "Declaracion de funcion", ((Token)val_peek(12).obj).getLexema()+" "+((Token)val_peek(11).obj).getLexema());}
break;
case 11:
//#line 131 "especificacion.y"
{
											Vector<Token> tokens = (Vector<Token>)val_peek(2).obj;
											Token token = (Token)val_peek(0).obj;
											tokens.add(token);
											yyval.obj = tokens;
											
											}
break;
case 12:
//#line 138 "especificacion.y"
{
							Vector<Token> tokens = new Vector<Token>();
							Token token = (Token)val_peek(0).obj;
							tokens.add(token);
							yyval.obj = tokens;
							}
break;
case 15:
//#line 148 "especificacion.y"
{this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 16:
//#line 149 "especificacion.y"
{this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 17:
//#line 150 "especificacion.y"
{this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 18:
//#line 151 "especificacion.y"
{this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 19:
//#line 152 "especificacion.y"
{this.addError("Error sintactico: Tipo de dato invalido. ", ((Token)val_peek(0).obj).getNroLine());}
break;
case 23:
//#line 159 "especificacion.y"
{System.out.println("Asignacion realizada");}
break;
case 25:
//#line 163 "especificacion.y"
{
																setRegla(((Token)val_peek(5).obj).getNroLine(), "Invocacion", ((Token)val_peek(5).obj).getLexema());
															 }
break;
case 26:
//#line 166 "especificacion.y"
{
																	addError("Error sintactico: falta '(' al inicio de la invocacion ", ((Token)val_peek(5).obj).getNroLine());
															 	}
break;
case 27:
//#line 170 "especificacion.y"
{
																	addError("Error sintactico: falta ')' al final de la invocacion ", ((Token)val_peek(5).obj).getNroLine());
															 	}
break;
case 34:
//#line 186 "especificacion.y"
{System.out.println("Case do");
				  						  setRegla(((Token)val_peek(6).obj).getNroLine(), "Sentencia de control", ((Token)val_peek(6).obj).getLexema());												 
										 }
break;
case 38:
//#line 196 "especificacion.y"
{
						      								  setRegla(((Token)val_peek(2).obj).getNroLine(), "Sentencia de Control", "else");
			   			  									}
break;
case 39:
//#line 200 "especificacion.y"
{
			   				  			setRegla(((Token)val_peek(5).obj).getNroLine(), "Sentencia de Control", "if");
			   			   			}
break;
case 40:
//#line 204 "especificacion.y"
{System.out.println("TOKE "+((Token)val_peek(2).obj).getLexema());
											addError("Falta parentesis de cierre ')'",((Token)val_peek(4).obj).getNroLine());
 										 }
break;
case 41:
//#line 208 "especificacion.y"
{System.out.println("TOKE "+((Token)val_peek(2).obj).getLexema());
											addError("Falta parentesis de cierre ')'",((Token)val_peek(4).obj).getNroLine());
 										 }
break;
case 45:
//#line 222 "especificacion.y"
{ setRegla(((Token)val_peek(2).obj).getNroLine(), "expresion logica", ((Token)val_peek(1).obj).getLexema());}
break;
case 58:
//#line 249 "especificacion.y"
{setRegla(((Token)val_peek(3).obj).getNroLine(), "Impresion",((Token)val_peek(3).obj).getLexema()+"("+((Token)val_peek(1).obj).getLexema()+")" ) ;}
break;
case 59:
//#line 252 "especificacion.y"
{System.out.println("ASIGNACION");setRegla(((Token)val_peek(2).obj).getNroLine(), "Asignacion", ((Token)val_peek(2).obj).getLexema()+":="+((Token)val_peek(0).obj).getLexema());}
break;
case 60:
//#line 253 "especificacion.y"
{
							addError("Operador invalido. ", ((Token)val_peek(1).obj).getNroLine());
						 }
break;
//#line 712 "Parser.java"
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
