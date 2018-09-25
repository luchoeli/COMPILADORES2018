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
public final static short WHILE=268;
public final static short CASE=269;
public final static short DO=270;
public final static short READONLY=271;
public final static short WRITE=272;
public final static short PASS=273;
public final static short RETURN=274;
public final static short PRINT=275;
public final static short MAYORIGUAL=276;
public final static short MENORIGUAL=277;
public final static short IGUAL=278;
public final static short DISTINTO=279;
public final static short EOF=280;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    1,    1,    1,    2,    2,    2,    4,
    5,    7,    7,    6,    6,    3,    3,    3,    3,    3,
   13,   14,   14,   15,   15,   15,   15,   10,   16,   16,
    9,    9,   19,   20,   20,   18,   21,   21,   21,   21,
   21,   21,    8,    8,    8,   22,   22,   22,   11,   12,
   23,   23,   17,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    2,    1,    2,    2,    2,    2,
   13,    3,    1,    1,    1,    2,    2,    2,    2,    2,
    6,    1,    1,    1,    1,    1,    3,    7,    5,    6,
    8,    5,    3,    2,    1,    3,    1,    1,    1,    1,
    1,    1,    3,    3,    1,    3,    3,    1,    4,    3,
    1,    1,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   14,   15,    0,    0,    0,    0,    2,
    3,    0,    0,    0,    0,    0,    0,    0,    0,    9,
    0,    0,    0,    0,    0,    0,    4,    5,    7,    8,
    0,    0,   16,   17,   18,   19,   20,   22,   23,    0,
   52,   51,    0,    0,   48,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   37,   38,   39,   40,
   41,   42,    0,    0,    0,   49,    0,   12,   24,    0,
   26,    0,    0,    0,   46,   47,    0,    0,    0,    0,
    0,    0,   21,   35,    0,    0,    0,    0,    0,   27,
   33,   34,    0,    0,    0,   28,    0,   31,    0,    0,
    0,    0,    0,    0,    0,    0,   29,    0,    0,   53,
   30,    0,    0,   11,
};
final static short yydgoto[] = {                          8,
    9,   10,   11,   12,   13,   14,   32,   43,   15,   16,
   17,   18,   19,   40,   72,   88,  103,   47,   79,   85,
   63,   44,   45,
};
final static short yysindex[] = {                      -202,
  -20,  -40,  -13,    0,    0,  -10,    6,    0, -197,    0,
    0,   24,   27, -181,   30,   41,   43,   46,   50,    0,
 -214, -205, -205, -162, -163,  -20,    0,    0,    0,    0,
   57,   39,    0,    0,    0,    0,    0,    0,    0,   40,
    0,    0,   -9,    3,    0,  -25,   59,   60,   61, -183,
 -154, -191, -205, -205, -205, -205,    0,    0,    0,    0,
    0,    0, -205,  -18,  -17,    0, -153,    0,    0,   48,
    0,   69,    3,    3,    0,    0,   -9, -228, -150, -145,
   74, -156,    0,    0, -117,  -18,   62, -112,   -5,    0,
    0,    0, -144, -149,   64,    0, -202,    0,    2, -147,
 -218, -202,   80,    2,   87, -122,    0,   84, -205,    0,
    0,   34,    4,    0,
};
final static short yyrindex[] = {                         0,
    1,    0,    0,    0,    0,    0,    0,    0,  130,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -19,   88,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   89,  -39,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   90,
    0,    0,  -34,  -29,    0,    0,   95,    0,   93,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -36,   10,    8,    0,    0,   91,    0,   -1,    0,    0,
    0,    0,    0,    0,    0,    0,   38,    0,   53,    0,
    0,   35,   36,
};
final static int YYTABLESIZE=276;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         21,
    6,   45,  110,   45,   45,   45,   43,   91,   43,   43,
   43,   44,   96,   44,   44,   44,   28,   53,   27,   54,
   45,   46,   45,   20,   13,   43,   23,   43,    2,   24,
   44,    3,   44,   53,   62,   54,   61,   26,    2,   13,
    6,    3,   38,   39,   55,   25,    7,    4,    5,   56,
    6,   41,   42,    1,    2,  105,    7,    3,   26,    2,
  101,   77,    3,    4,    5,  106,    6,   29,    4,    5,
   30,    6,    7,   33,  113,   31,   53,    7,   54,   69,
   70,   71,    4,    5,   34,   84,   35,   73,   74,   36,
   75,   76,   92,   37,   48,   49,   50,   51,   52,   64,
   65,   66,   68,   81,   78,   80,   82,  112,   28,   83,
   27,   86,   87,   28,   89,   27,   90,   97,   98,   94,
   99,  100,  104,  107,  102,    6,  109,  111,  114,    1,
   25,   10,   50,   26,    2,   36,   32,    3,   93,    2,
   67,  108,    3,    4,    5,   95,    6,    0,    0,    0,
    0,    6,    7,    0,    0,    0,    0,    7,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   45,   45,   45,   45,
   22,   43,   43,   43,   43,    0,   44,   44,   44,   44,
   57,   58,   59,   60,    0,    0,    6,    6,    0,    0,
    6,    0,    0,    0,    0,    0,    6,    6,    0,    6,
    0,    0,    0,    0,    6,    6,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   41,  125,   43,   44,   45,   41,  125,   43,   44,
   45,   41,  125,   43,   44,   45,    9,   43,    9,   45,
   60,   23,   62,   44,   44,   60,   40,   62,  257,   40,
   60,  260,   62,   43,   60,   45,   62,  256,  257,   59,
  269,  260,  257,  258,   42,   40,  275,  266,  267,   47,
  269,  257,  258,  256,  257,  274,  275,  260,  256,  257,
   97,   63,  260,  266,  267,  102,  269,   44,  266,  267,
   44,  269,  275,   44,   41,  257,   43,  275,   45,  271,
  272,  273,  266,  267,   44,   78,   44,   53,   54,   44,
   55,   56,   85,   44,  257,  259,   40,   59,   59,   41,
   41,   41,  257,  257,  123,  123,   59,  109,  101,   41,
  101,  262,  258,  106,   41,  106,  273,  123,  263,   58,
  270,   58,  270,   44,  123,  125,   40,   44,  125,    0,
   41,   44,   44,  256,  257,   41,   44,  260,   86,  257,
   50,  104,  260,  266,  267,  258,  269,   -1,   -1,   -1,
   -1,  269,  275,   -1,   -1,   -1,   -1,  275,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  276,  277,  278,  279,
  281,  276,  277,  278,  279,   -1,  276,  277,  278,  279,
  276,  277,  278,  279,   -1,   -1,  256,  257,   -1,   -1,
  260,   -1,   -1,   -1,   -1,   -1,  266,  267,   -1,  269,
   -1,   -1,   -1,   -1,  274,  275,
};
}
final static short YYFINAL=8;
final static short YYMAXTOKEN=281;
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
"END_IF","BEGIN","END","USINTEGER","DOUBLE","WHILE","CASE","DO","READONLY",
"WRITE","PASS","RETURN","PRINT","MAYORIGUAL","MENORIGUAL","IGUAL","DISTINTO",
"EOF","\":=\"",
};
final static String yyrule[] = {
"$accept : programa",
"programa : list_sentencias",
"list_sentencias : sent_declarativa",
"list_sentencias : sent_ejecutable",
"list_sentencias : list_sentencias sent_declarativa",
"list_sentencias : list_sentencias sent_ejecutable",
"list_sentencias : error",
"sent_declarativa : declaracion_variable ','",
"sent_declarativa : declaracion_funcion ','",
"sent_declarativa : error ','",
"declaracion_variable : tipo list_variables",
"declaracion_funcion : tipo ID '(' tipo ID ')' '{' list_sentencias RETURN '(' expresion ')' '}'",
"list_variables : list_variables ';' ID",
"list_variables : ID",
"tipo : USINTEGER",
"tipo : DOUBLE",
"sent_ejecutable : sent_seleccion ','",
"sent_ejecutable : sent_control ','",
"sent_ejecutable : imprimir ','",
"sent_ejecutable : asignacion ','",
"sent_ejecutable : invocacion ','",
"invocacion : ID '(' nombre_parametro ';' lista_permisos ')'",
"nombre_parametro : ID",
"nombre_parametro : CTE",
"lista_permisos : READONLY",
"lista_permisos : WRITE",
"lista_permisos : PASS",
"lista_permisos : WRITE ';' PASS",
"sent_control : CASE '(' ID ')' '{' linea_control '}'",
"linea_control : CTE ':' DO bloque_de_sentencias ','",
"linea_control : linea_control CTE ':' DO bloque_de_sentencias ','",
"sent_seleccion : IF '(' expresion_logica ')' bloque_sin_declaracion ELSE bloque_sin_declaracion END_IF",
"sent_seleccion : IF '(' expresion_logica ')' bloque_sin_declaracion",
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
"factor : CTE",
"factor : ID",
"bloque_de_sentencias : '{' list_sentencias '}'",
};

//#line 229 "especificacion.y"
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
	//this.errors.add(new Error(e,line));	
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
//#line 409 "Parser.java"
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
case 10:
//#line 104 "especificacion.y"
{System.out.println("Declaracion variable");
												 setRegla(((Token)val_peek(1).obj).getNroLine(), "Declaracion de variables", ((Token)val_peek(1).obj).getLexema());
												 updateTable(((Vector<Token>)val_peek(0).obj), ((Token)val_peek(1).obj).getLexema());												 
												 }
break;
case 11:
//#line 115 "especificacion.y"
{setRegla(((Token)val_peek(12).obj).getNroLine(), "Declaracion de funcion", ((Token)val_peek(12).obj).getLexema()+" "+((Token)val_peek(11).obj).getLexema());}
break;
case 12:
//#line 119 "especificacion.y"
{
											Vector<Token> tokens = (Vector<Token>)val_peek(2).obj;
											Token token = (Token)val_peek(0).obj;
											tokens.add(token);
											yyval.obj = tokens;
											
											}
break;
case 13:
//#line 126 "especificacion.y"
{
							Vector<Token> tokens = new Vector<Token>();
							Token token = (Token)val_peek(0).obj;
							tokens.add(token);
							yyval.obj = tokens;
							}
break;
case 19:
//#line 142 "especificacion.y"
{System.out.println("signacion realizada");}
break;
case 21:
//#line 147 "especificacion.y"
{setRegla(((Token)val_peek(5).obj).getNroLine(), "Invocacion", ((Token)val_peek(5).obj).getLexema());}
break;
case 28:
//#line 161 "especificacion.y"
{System.out.println("Case do");
				  						  setRegla(((Token)val_peek(6).obj).getNroLine(), "Sentencia de control", ((Token)val_peek(6).obj).getLexema());												 
										 }
break;
case 32:
//#line 173 "especificacion.y"
{setRegla(((Token)val_peek(4).obj).getNroLine(), "Sentencia de Control", ((Token)val_peek(4).obj).getLexema());}
break;
case 36:
//#line 190 "especificacion.y"
{ setRegla(((Token)val_peek(2).obj).getNroLine(), "expresion logica", ((Token)val_peek(1).obj).getLexema());}
break;
case 49:
//#line 213 "especificacion.y"
{setRegla(((Token)val_peek(3).obj).getNroLine(), "Impresion",((Token)val_peek(3).obj).getLexema()+"("+((Token)val_peek(1).obj).getLexema()+")" ) ;}
break;
case 50:
//#line 216 "especificacion.y"
{System.out.println("ASIGNACION");setRegla(((Token)val_peek(2).obj).getNroLine(), "Asignacion", ((Token)val_peek(2).obj).getLexema()+":="+((Token)val_peek(0).obj).getLexema());}
break;
//#line 618 "Parser.java"
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
