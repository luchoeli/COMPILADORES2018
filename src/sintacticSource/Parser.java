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
//#line 21 "Parser.java"




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
    0,    1,    1,    1,    1,    2,    2,    2,    2,    4,
    5,    7,    7,    6,    6,    3,    3,    3,   10,   12,
   12,    9,    9,   14,   15,   16,   16,   16,   16,   16,
   16,    8,    8,    8,   17,   17,   17,   11,   18,   18,
   13,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    2,    2,    2,    3,    3,    2,
   13,    3,    1,    1,    1,    2,    2,    2,    7,    5,
    4,    8,    5,    3,    3,    1,    1,    1,    1,    1,
    1,    3,    3,    1,    3,    3,    1,    4,    1,    1,
    3,
};
final static short yydefred[] = {                         0,
    0,   14,   15,    0,    0,    0,    0,    0,    3,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    5,
    0,    6,    7,    0,    0,   16,   17,   18,   40,   39,
    0,    0,    0,    0,   37,    0,    0,    8,    9,    0,
    0,   26,   27,   28,   29,   30,   31,    0,    0,    0,
    0,    0,    0,    0,    0,   38,    0,   12,    0,    0,
    0,    0,    0,   24,   35,   36,    0,    0,    0,    0,
    0,    0,    0,   41,    0,    0,    0,   19,    0,   22,
    0,    0,    0,   21,    0,    0,   20,    0,    0,    0,
   11,
};
final static short yydgoto[] = {                          6,
    7,    8,    9,   10,   11,   12,   25,   31,   13,   14,
   15,   72,   63,   32,   33,   50,   34,   35,
};
final static short yysindex[] = {                      -196,
  -11,    0,    0,   -1,   19,    0, -196,   -3,    0,    4,
   11, -194,   30,   31,   32, -214, -191, -181,   -3,    0,
 -215,    0,    0,   40,   24,    0,    0,    0,    0,    0,
   22,   44,  -60,  -20,    0,   46,   47,    0,    0, -215,
 -176,    0,    0,    0,    0,    0,    0, -214, -214, -214,
  -34, -214, -214, -214,  -33,    0, -165,    0,  -20,  -20,
  -19, -196, -169,    0,    0,    0, -164,   54, -122,  -34,
   38, -118,  -26,    0, -163, -172,   41,    0, -196,    0,
  -34, -168, -213,    0,  -34,   61,    0, -214,   -9,  -21,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,  103,   17,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   21,    0,
    0,    0,    0,    1,    5,    0,    0,    0,    0,    0,
    0,    0,    0,  -37,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -32,  -27,
  -22,    0,   11,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
  -42,    3,    8,   84,   85,   67,    0,  -38,    0,    0,
    0,    0,  -39,    0,   56,   76,    9,   15,
};
final static int YYTABLESIZE=301;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         47,
   13,   46,   74,   34,   10,   34,   78,   34,   32,   19,
   32,   61,   32,   33,   20,   33,    2,   33,   25,   69,
    4,   53,   34,   48,   34,   49,   54,   32,   16,   32,
   75,   90,   33,   48,   33,   49,   83,   25,   17,   25,
   21,   84,   29,   30,   13,   87,    1,   22,   10,   89,
    2,    3,    2,    3,   23,    4,   59,   60,   18,   13,
   86,    5,   24,    1,   48,   36,   49,   65,   66,    2,
    3,   19,    4,   26,   27,   28,   20,   37,    5,   40,
   58,   47,   41,   46,   51,   19,   55,   56,   62,   67,
   20,   68,   70,   71,   73,   76,   79,   81,   82,   80,
   88,   85,    1,   91,   38,   39,   57,   64,   52,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   13,    0,    0,    0,   10,
    0,    0,    0,    0,    0,    0,    0,    1,    0,   77,
    0,    2,    0,    2,    3,    4,    4,    0,    0,    0,
    0,    0,    5,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   42,   43,   44,   45,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   34,   34,
   34,   34,    0,   32,   32,   32,   32,    0,   33,   33,
   33,   33,    0,   25,   25,   25,   25,    0,    0,    0,
   13,    0,    0,    0,   10,    0,   13,   13,    0,   13,
   10,   10,    0,   10,   13,   13,    2,    0,   10,   10,
    4,    0,    2,    2,    0,    2,    4,    4,    0,    4,
    2,    2,    0,    0,    4,    4,    0,   42,   43,   44,
   45,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         60,
    0,   62,  125,   41,    0,   43,  125,   45,   41,    7,
   43,   50,   45,   41,    7,   43,    0,   45,   41,   62,
    0,   42,   60,   43,   62,   45,   47,   60,   40,   62,
   70,   41,   60,   43,   62,   45,   79,   60,   40,   62,
   44,   81,  257,  258,   44,   85,  260,   44,   44,   88,
  266,  267,  266,  267,   44,  269,   48,   49,   40,   59,
  274,  275,  257,  260,   43,  257,   45,   53,   54,  266,
  267,   69,  269,   44,   44,   44,   69,  259,  275,   40,
  257,   60,   59,   62,   41,   83,   41,   41,  123,  123,
   83,  257,  262,  258,   41,   58,  123,  270,   58,  263,
   40,  270,    0,  125,   21,   21,   40,   52,   33,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  125,   -1,   -1,   -1,  125,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  260,   -1,  258,
   -1,  125,   -1,  266,  267,  125,  269,   -1,   -1,   -1,
   -1,   -1,  275,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  276,  277,  278,  279,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  276,  277,
  278,  279,   -1,  276,  277,  278,  279,   -1,  276,  277,
  278,  279,   -1,  276,  277,  278,  279,   -1,   -1,   -1,
  260,   -1,   -1,   -1,  260,   -1,  266,  267,   -1,  269,
  266,  267,   -1,  269,  274,  275,  260,   -1,  274,  275,
  260,   -1,  266,  267,   -1,  269,  266,  267,   -1,  269,
  274,  275,   -1,   -1,  274,  275,   -1,  276,  277,  278,
  279,
};
}
final static short YYFINAL=6;
final static short YYMAXTOKEN=280;
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
"EOF",
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
"sent_declarativa : sent_declarativa ',' declaracion_variable",
"sent_declarativa : sent_declarativa ',' declaracion_funcion",
"declaracion_variable : tipo list_variables",
"declaracion_funcion : tipo ID '(' tipo ID ')' '{' list_sentencias RETURN '(' expresion ')' '}'",
"list_variables : list_variables ';' ID",
"list_variables : ID",
"tipo : USINTEGER",
"tipo : DOUBLE",
"sent_ejecutable : sent_seleccion ','",
"sent_ejecutable : sent_control ','",
"sent_ejecutable : imprimir ','",
"sent_control : CASE '(' ID ')' '{' linea_control '}'",
"linea_control : linea_control CTE ':' DO bloque_de_sentencias",
"linea_control : CTE ':' DO bloque_de_sentencias",
"sent_seleccion : IF '(' condicion ')' bloque_de_sentencias ELSE bloque_de_sentencias END_IF",
"sent_seleccion : IF '(' condicion ')' bloque_de_sentencias",
"condicion : expresion_logica comparador expresion_logica",
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
"factor : CTE",
"factor : ID",
"bloque_de_sentencias : '{' list_sentencias '}'",
};

//#line 172 "especificacion.y"

public LexicalAnalizer lexico;
public ArrayList<String> errors = new ArrayList<String>();
public ArrayList<String> salidaSintactico = new ArrayList<String>();

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
/*
private void addError(String e) {
	this.errors.add("ERROR SINTACTICO: " + e + "LINEA: " + lexico.getLinea());
}

private void addSalida(String e) {
	this.salidaSintactico.add(e + " LINEA: " + lexico.getLinea());
}
*/
//#line 356 "Parser.java"
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
case 6:
//#line 95 "especificacion.y"
{System.out.println("AAAAAAAAAAAAA");}
break;
//#line 509 "Parser.java"
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
