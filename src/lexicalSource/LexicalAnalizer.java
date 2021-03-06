package lexicalSource;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import sintacticSource.Error;
import semanticSource.*;

public class LexicalAnalizer {
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
	
	public final static int maxInt=32767;
	public final static int minInt=-32768;
	
	public final static BigInteger maxUInt= new BigInteger("65536") ,minUInt= new BigInteger("-1");//RANGO CONSTANTE SIN SIGNO --VEEER--
	//public final static Double minDou=(Double)2.2250738585072014E-308, maxDou=(Double)1.7976931348623157E308;   /// VEEER
	public final static Double minNega = -Double.MAX_VALUE,minDou=Double.MIN_NORMAL,maxDou=Double.MAX_VALUE;   /// 
	private static final int FINAL_STATE = -1; //Estado final.
	private Hashtable<String,Integer> idTokens= new Hashtable<String,Integer>();
	private List<Error> errors = new ArrayList<Error>();
	private int line=1;
	private boolean read=true;
	private char lastChar;
	private String buffer;
	private MatrixActions aMatrix;
	private MatrixState sMatrix;
	private List<Token> tokens = new ArrayList<Token>();
	//private File file; <<-- El archivo, para ir leyendo.
	private int posRead=0;
	private String in;
	private Table table;
	
	private boolean enFuncion = false;
	

	public LexicalAnalizer(String in, Table t){//POR PARAMETRO EL ARCHIVO.
		this.in=in;
		table = t;
		initilize();
	}
	public void initilize(){
		this.sMatrix=new MatrixState();
		
		//IDENTIFICADOR
		this.idTokens.put("IDENTIFICADOR",(int)ID);
		//CADENA
		this.idTokens.put("CADENA", (int)CADENA);
		//CONSTANTES
		this.idTokens.put("CTE",(int)CTE);		
		//OPERADORES
		this.idTokens.put("+", (int)'+');
		this.idTokens.put("-", (int)'-');
		this.idTokens.put("*", (int)'*');
		this.idTokens.put("/", (int)'/');
		//ASIGNACION
		this.idTokens.put(":=", (int)ASIGNACION);
		//COMPARADORES
		this.idTokens.put(">", (int)'>');
		this.idTokens.put("<", (int)'<');		
		this.idTokens.put(">=", (int)MAYORIGUAL);
		this.idTokens.put("<=", (int)MENORIGUAL);
		this.idTokens.put("=", (int)IGUAL);
		this.idTokens.put("!=", (int)DISTINTO);
		//SIMBOLOS		
		this.idTokens.put("(", (int)'(');
		this.idTokens.put(")", (int)')');
		this.idTokens.put(",", (int)',');
		this.idTokens.put(";", (int)';');
		this.idTokens.put("{", (int)'{');
		this.idTokens.put("}", (int)'}');
		this.idTokens.put("'", (int)'\'');
		this.idTokens.put(":", (int)':');
		//PALABRAS RESERVADAS
		this.idTokens.put("if", (int)IF);
		this.idTokens.put("then", (int)THEN);
		this.idTokens.put("else", (int)ELSE);
		this.idTokens.put("end_if", (int)END_IF); 
		this.idTokens.put("case", (int)CASE);  
		this.idTokens.put("do", (int)DO);
		this.idTokens.put("usinteger", (int)USINTEGER);
		this.idTokens.put("double", (int)DOUBLE);
		this.idTokens.put("long", (int)LONG);
		this.idTokens.put("uslinteger", (int)USLINTEGER);
		this.idTokens.put("integer", (int)INTEGER);
		this.idTokens.put("linteger", (int)LINTEGER);
		this.idTokens.put("single", (int)SINGLE);
		
		this.idTokens.put("while", (int)WHILE);   // no va
		this.idTokens.put("readonly",(int)READONLY);
		this.idTokens.put("pass",(int)PASS);
		this.idTokens.put("write",(int)WRITE);
		this.idTokens.put("return",(int)RETURN);
		this.idTokens.put("print",(int)PRINT);
		this.idTokens.put("long",(int)LONG);
		//END OF FILE
		this.idTokens.put("\0",290);
		
		
		
		//MATRIZ DE ACCIONES SEMANTICAS
		this.aMatrix=new MatrixActions(17,25);		
		
		SemanticAction SA1=new SetBufferAction(this);// INICIALIZA BUFFER Y AGREGA CARACTER
		SemanticAction SA2=new CharacterToInputAction(this); //VUELVE EL ULTIMO CARACTER LEIDO A ESTADO INICIAL
		SemanticAction SA3=new CheckKeyWord(this);//CONTROLA SI EL BUFFER ES UNA PALABRA RESERVADA
		SemanticAction SA4=new CheckStringLenghtAction(this, 25);//CONTROLA QUE LA LONGITUD DE UN IDENTIFICADOR SEA MENOR A 15
		SemanticAction SA5=new CheckRangeAction(this, minDou, maxDou); //RANGO PARA ENTEROS
		SemanticAction SA6=new CheckRangeActionUnsigned(this, minUInt, maxUInt);//RANGO PARA ENTEROS SIN SIGNO
		SemanticAction SA7=new incrementLineAction(this);// LINEA++
		SemanticAction SA8=new CreateTokenAction(this);//CREA EL TOKEN
		SemanticAction SA9=new AddCharacterAction(this); // AGREGA CARACTER
		SemanticAction SA10=new PrintErrorAction(this); //IMPRIME UN ERROR.
		SemanticAction SA20=new DiscardBuffer(this);

		SemanticAction SA11= new CompositeAction(); 
		((CompositeAction)SA11).add(SA1); //INICIALIZA BUFFER Y AGREGA CARACTER.
		((CompositeAction)SA11).add(SA8); //CREA TOKEN
		
		SemanticAction SA12=new CompositeAction();
		((CompositeAction)SA12).add(SA2); //ULTIMO CARACTER AL INICIO
		((CompositeAction)SA12).add(SA4); //CONTROLA LONG DE IDENTIFICADOR, SI !OK CORTA EN 25.
		((CompositeAction)SA12).add(SA8); //CREA TOKEN.
		
		SemanticAction SA13=new CompositeAction(); 	
		((CompositeAction)SA13).add(SA2);//ULTIMO CARACTER AL INICIO
		((CompositeAction)SA13).add(SA5);//CONTROLA RANGO CONSTANTE double
		((CompositeAction)SA13).add(SA8);// SI RANGO OK, CREA TOKEN.
		
		SemanticAction SA14=new CompositeAction();
		((CompositeAction)SA14).add(SA9);//AGREGA CARACTER
		((CompositeAction)SA14).add(SA6);//CONTROLA RANGO CONSTANTE ENTERA SIN SIGNO
		((CompositeAction)SA14).add(SA8);// SI RANGO OK, CREA TOKEN.	
		
		SemanticAction SA15=new CompositeAction();
		((CompositeAction)SA15).add(SA2);// ULTIMO CARACTER AL INICIO	
		((CompositeAction)SA15).add(SA8);//CREA TOKEN.	
		
		SemanticAction SA16=new CompositeAction();
		((CompositeAction)SA16).add(SA9);//AGREGA CARACTER.	
		((CompositeAction)SA16).add(SA8);//CREA TOKEN.
		
		SemanticAction SA17=new CompositeAction();
		((CompositeAction)SA17).add(SA2);//ULTIMO CARACTER AL INICIO	
		((CompositeAction)SA17).add(SA3);//CONTROLA QUE SEA PALABRA RESERVADA.
		((CompositeAction)SA17).add(SA8);//CREA TOKEN.	
		
		SemanticAction SA18=new CompositeAction();
		((CompositeAction)SA18).add(SA2);//ULTIMO CARACTER AL INICIO	
		((CompositeAction)SA18).add(SA10);//ERROR
		
		SemanticAction SA19=new CompositeAction();
		//((CompositeAction)SA19).add(SA2);//ULTIMO CARACTER AL INICIO
		((CompositeAction)SA19).add(SA20);//DESCARTA ULTIMO CARACTER DEL BUFFER
		((CompositeAction)SA19).add(SA7);//linea ++
		
		SemanticAction SA21 = new CompositeAction ();
		((CompositeAction)SA21).add(SA10); //error
		((CompositeAction)SA21).add(SA7);//linea ++
		
		
		//FIXME ver si donde puse SA10(error) va SA18(ultimoChar al inicio y error)
		//ESTADO 0
		this.aMatrix.put(0,0, SA1);this.aMatrix.put(0,1, SA1);this.aMatrix.put(0,2, SA10);this.aMatrix.put(0,3, SA1);
		this.aMatrix.put(0,4, SA1);this.aMatrix.put(0,5, SA1);this.aMatrix.put(0,6, SA11);this.aMatrix.put(0,7, SA11);
		this.aMatrix.put(0,8, SA1);this.aMatrix.put(0,9, SA1);this.aMatrix.put(0,10, SA11);this.aMatrix.put(0,11, SA11);
		this.aMatrix.put(0,12, SA7);this.aMatrix.put(0,13, SA10);this.aMatrix.put(0,14, SA1);this.aMatrix.put(0,15, SA1);
		this.aMatrix.put(0,17, SA18);this.aMatrix.put(0,18, SA11);
		
		//ESTADO 1  Los identificadores con longitud mayor deber�n ser informados como error, y el token err�neo deber� ser descartado.
		for(int i=1;i<=18;i++){ // salteo el 0 ya que no debo hacer nada con _
			if ((i==1)||(i==2)||(i==3)||(i==8)||(i==9)||(i==13)){  // l, L, d, u, i, D
				this.aMatrix.put(1,i,SA9); // agrego al buffer
			} 
			else
				this.aMatrix.put(1, i, SA10); //  imptrimo error		
		}
		
		//ESTADO 2
		for(int i=0;i<=18;i++){ 
			if ((i==1)||(i==2)||(i==3)|(i==8)||(i==9)||(i==13)) {  // l, L, d, u, i, D
				this.aMatrix.put(2, i, SA9); // agrgo al buffer
			}
			else 
				this.aMatrix.put(2, i, SA12); //  descarto el ultimo char, controlo long y agregpo
		}
		//ESTADO 3
		for(int i=0;i<=18;i++){ 
			if (i==6) {  // =
				this.aMatrix.put(3, i, SA16); // agrego y creo 
			}
			else
				this.aMatrix.put(3, i, SA15); // agrego
		}
		//ESTADO 4
		for(int i=0;i<=18;i++){ 
			if (i==6) {  // =
				this.aMatrix.put(4, i, SA16); // agrego y creo 
			}
			else
				this.aMatrix.put(4, i, SA15); // descarto y creo
		}
		
		//ESTADO 5
		for(int i=0;i<=18;i++){ 
			if ((i==0)||(i==1)||(i==8)||(i==9)) {  //_, l, u, i
				this.aMatrix.put(5, i, SA9); // agrego al buffer  
			} 
			else
				this.aMatrix.put(5, i, SA17); // 
		}
		//ESTADO 6 unsigend int
		for(int i=0;i<=18;i++){ 
			if ((i==0)||(i==15)||(i==3)) {  // ., d, 
				this.aMatrix.put(6, i, SA9); // agrego al buffer 
			}
			else if (i!=0){
				this.aMatrix.put(6, i, SA10); // error
				}
		}
		//ESTADO 7
		for(int i=0;i<=18;i++){ 
			if (i!=8) { // u
				this.aMatrix.put(7, i, SA10); //  error
			}
			else if (i==8)  {
				this.aMatrix.put(7, i, SA9);
			}
		}
		
		//ESTADO 8
		for(int i=0;i<=18;i++){ 
			
			if (i==9) { // i
				this.aMatrix.put(8, i, SA14); // 
			} else 
				this.aMatrix.put(8, i, SA10);
			
		}
	
		//ESTADO 9 //
		for(int i=0;i<=18;i++){ 
			if ((i==3)||(i==13)) { // d, D
				this.aMatrix.put(9, i, SA9); //  agrego al buff, cheequeo rango y creo
			}
			else
				if (i==15){
					this.aMatrix.put(9, i, SA10);  // . -> error
				}
				else
					this.aMatrix.put(9, i, SA13); // fin
		}
		
		//ESTADO 10	
		for(int i=0;i<=18;i++){ 
			if (i==3) { // d 
				this.aMatrix.put(10, i, SA9);
			}
			else{
				this.aMatrix.put(10, i, SA10);
			}
		}
		//ESTADO 11
		for(int i=0;i<=18;i++){
			if (i==3 ||(i==10)||(i==18)){
				this.aMatrix.put(11, i, SA9);
			}
			else{
				this.aMatrix.put(11, i, SA10);
			}
		}
		
		//ESTADO 12
		for(int i=0;i<=18;i++){
			if (i==3){
				this.aMatrix.put(12, i, SA9);
			}
			else{
				this.aMatrix.put(12, i, SA10);
			}
		}
		//ESTADO 13
		for(int i=0;i<=18;i++){
			if (i==3){
				this.aMatrix.put(13, i, SA9);
			}
			else{
				this.aMatrix.put(13, i, SA13);
			}
		}
		//ESTADO 14
		this.aMatrix.put(14, 12, SA7);
	
		//ESTADO 15	
			for(int i=0;i<=18;i++){ 
				if (i==12) { // 
					this.aMatrix.put(15, i, SA21); // 
				} else
					if (i==14){ //'
						this.aMatrix.put(15, i, SA16); //AGREGO
					}else
						this.aMatrix.put(15, i, SA9);
				
			}
		//ESTADO 16
		for(int i=0;i<=18;i++){ 
			if (i==12) { // \n
				this.aMatrix.put(16, i, SA19); // 
			} else
				if (i==14){ // '
					this.aMatrix.put(16, i, SA16); // agrego y creo,  //FIXME por que no lo agregaaaa!!!
				}else  // otro
					this.aMatrix.put(16, i, SA9);
		}
		
	}
	
	public void incrementLine(){
		line++;
	}
	public int getLine(){
		return line;
	}	
	public boolean isRead() {
		return read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}
	public char getLastChar() {
		return lastChar;
	}
	public void setLastChar(char lastChar) {
		this.lastChar = lastChar;
	}
	public String getBuffer() {
		return buffer;
	}
	public void setBuffer(String buffer) {
		this.buffer = buffer;
	}//
	
	public Token getToken() { //in es String entrada
		int state = 0;
		int newState = 0;
		int column = -1;
		boolean corte = false;
		while(!corte){
			if (isRead()){ //Si tengo que leer el �ltimo caracter.
				if (posRead<in.length()){
					this.lastChar = in.charAt(posRead);
					posRead++;
				}else{
					lastChar = '\0';
					corte = true;
				}
			}
			this.setRead(true);
			column = getColum(lastChar);	
			newState = sMatrix.get(state,column);
			SemanticAction a = aMatrix.get(state,column);
			if (a!=null){
				a.execute(buffer,lastChar);
			}
			if (newState==FINAL_STATE){				
				int pos = tokens.size()-1;
				if (pos>=0)
					return (tokens.get(pos));
				state=0;
			}
			else{
				state=newState;
			}
		}
		return null;
		
	}
	
	public ArrayList<Token> getTokens() {
		return (ArrayList<Token>)tokens;
	}
	
	private int getColum(char c){
	// dado un char devuelve la columna en la que esta
		
		if((c>=48)&&(c<=57)){ // digitos
			return 3;
		}

		switch(c){ 		
			case 'D': return 13; 
			case '*': return 11; 
			case '/': return 11; 
			case '-': return 18; 
			case '+': return 10; 
			case '(': return 7; 
			case ')': return 7; 
			case ',': return 7; 
			case ';': return 7; 
			case 32: return 16; // espacio en blanco
			case '\n': return 12;
			case '\t': return 16;
			case '_': return 0; 
			case 'i': return 9; 
			case 'u': return 8;
			case ':': return 4;
			case '=': return 6; 
			case '<': return 5; 
			case '>': return 5;
			case '!': return 5;
			case '.': return 15;
			case '{': return 7;
			case '}': return 7;  
			case 39: return 14; // ' 	
			case 0: return 18; // NULL
		}
		
		
		if ((c>='A')&&(c<='Z')){ // mayusculas
			return 2;		
		}
		
		if ((c>='a')&&(c<='z')){ // minusculas
			return 1;
		}
		
		return 17;  // otros 
			
	}
	
	
	private TableRecord addIdentificadorToTable(TableRecord a){
		String key =a.getLexema();
		//System.out.println(key);
		if (!table.containsLexema(key)){
			//System.out.println("no esta! - lo meto");
			table.put(key,a);
			//a.print();
			return a;
		}
	//	System.out.println("no isrerto, retorno");
		return a;
	}
	
	private TableRecord addConstanteToTable(TableRecord a){
		String key = a.getLexema();
		if (table.containsLexema(key)){
			//Si existe incremento el n�mero de referencias.
			TableRecord out= table.get(key);
			out.increment();
			return out;
		}
		table.put(key, a);
		return a;
	}
	
	
	public boolean isKeyWord(String buffer){
		return idTokens.containsKey(buffer);
	}
	
	public void makeToken(String buffer){
		int id = getID(buffer);
		String type= getType(id);
		TableRecord record=null;
		if (id == ID) {
			//System.out.println("//////////makeToken//////////");
			record = new TableRecord(buffer,id);
			record.setType(null);
			record = this.addIdentificadorToTable(record);
			//System.out.println("////////////////");
			
			//record = this.addIdentificadorToTable(new TableRecord(buffer, id));
		}else if (id == CTE) {
			
			if (buffer.contains("_ui")){
				String value[] = buffer.split("_");
				record = new TableRecord(buffer,id,value[0]);
				type = "usinteger";
				record.setType(type);
				record = this.addConstanteToTable(record);
				
				//record = this.addConstanteToTable(new TableRecord(buffer, id, value[0]));
			}else if  (buffer.contains(".")){
				//FIXME agregar al constreuctor del TR el value
				String dable=buffer.substring(0, buffer.length());
				dable = dable.replace("D","E");
				record = new TableRecord(buffer,id,dable);
				type="double";
				record.setType(type);
				record = this.addConstanteToTable(record);
				
				//record = this.addConstanteToTable(new TableRecord(buffer, id));
			}	
			
		}else if (id == CADENA){
			record = new TableRecord(buffer,id,null);
			record.setType("cadena");
			record = this.addConstanteToTable(record);
		}
		Token t=new Token(id,buffer,this.line,type,record,null);	
		tokens.add(t);		
	}

	private String getType(int id) {
		//Tipos de tokens
		
		switch(id){
		case IF: return "PALABRA RESERVADA";
		case THEN: return "PALABRA RESERVADA";
		case ELSE: return "PALABRA RESERVADA";
		case END_IF: return "PALABRA RESERVADA";
		case PRINT: return "PALABRA RESERVADA";
		case BEGIN: return "PALABRA RESERVADA";
		case END: return "PALABRA RESERVADA";
		case USINTEGER: return "PALABRA RESERVADA";
		case DOUBLE: return "PALABRA RESERVADA";
		case INTEGER: return "PALABRA RESERVADA";
		case LINTEGER: return "PALABRA RESERVADA";
		case USLINTEGER: return "PALABRA RESERVADA";
		case LONG: return "PALABRA RESERVADA";
		case SINGLE: return "PALABRA RESERVADA";
		case RETURN: return "PALABRA RESERVADA";
		case CASE: return "PALABRA RESERVADA";
		case DO: return "PALABRA RESERVADA";
		case READONLY: return "PALABRA RESERVADA";
		case WRITE: return "PALABRA RESERVADA";
		case PASS: return "PALABRA RESERVADA";
		
		
		case ID: return "IDENTIFICADOR";
		
		case CTE: return "CONSTANTE";
		
		case CADENA: return "CADENA";
		
		case ASIGNACION: return "ASIGNACION";
		
		case '<': return "COMPARADOR";
		case '>': return "COMPARADOR";
		case '=': return "COMPARADOR";
		case MENORIGUAL: return "COMPARADOR";
		case MAYORIGUAL: return "COMPARADOR";
		case DISTINTO: return "COMPARADOR";
		
		case '+': return "OPERADOR";
		case '-': return "OPERADOR";
		case '*': return "OPERADOR";
		case '/': return "OPERADOR";
		
		case '(': return "SIMBOLO";
		case ')': return "SIMBOLO";
		case ',': return "SIMBOLO";
		case ';': return "SIMBOLO";
		case '{': return "SIMBOLO";
		case '}': return "SIMBOLO";
		case '\'': return "SIMBOLO";
		case ':': return "SIMBOLO";
		
		default: return "EOF";
				
		}		
	}
	private int getID(String buffer) {
		int value=-1;
		if (this.idTokens.containsKey(buffer)){
			value = this.idTokens.get(buffer);
		}else
			{
				if (buffer.contains("_ui")|| buffer.contains(".")){
					value = this.idTokens.get("CTE");
				}
				else if(buffer.contains("'")){
						 value = this.idTokens.get("CADENA");
					 }
					 	else{value=this.idTokens.get("IDENTIFICADOR");}			
			}
		
		return value;
	}

	public void addError(String des, int line) {
		// Agrega un error y en que linea sucedio .
		this.errors.add(new Error(des,line));		
	}
	public void addError(String des, int line, int severity) {
		// Agrega un error y en que linea sucedio .
		Error e = new Error(des,line);
		//e.severity = severity;		
		this.errors.add(e);		
	}
	public ArrayList<Error> getErrors() {
		return (ArrayList<Error>) this.errors;
	}
	public boolean isEnFuncion() {
		return enFuncion;
	}
	public void setEnFuncion(boolean enFuncion) {
		this.enFuncion = enFuncion;
	}
	
	
	
}
