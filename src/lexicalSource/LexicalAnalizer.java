package lexicalSource;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import semanticSource.*;

public class LexicalAnalizer {
	// lucho branch

	private static final int FINAL_STATE = -1; //Estado final.
	private Hashtable<String,Integer> idTokens= new Hashtable<String,Integer>();
	private List<TableRecord> symbolTable=new ArrayList<TableRecord>();
	private List<String> problems = new ArrayList<String>();
	private List<Integer> linesProblems = new ArrayList<Integer>();	
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
	
	
	public LexicalAnalizer(String in){//POR PARAMETRO EL ARCHIVO.
		this.in=in;
		initilize();
		
	}
	public void initilize(){
		this.sMatrix=new MatrixState();

		//IDENTIFICADOR
		this.idTokens.put("ID",288);
		//CONSTANTES
		this.idTokens.put("CTE",289);		
		//OPERADORES
		this.idTokens.put("+", 256);
		this.idTokens.put("-", 257);
		this.idTokens.put("*", 258);
		this.idTokens.put("/", 259);
		//ASIGNACION
		this.idTokens.put(":=", 260);
		//COMPARADORES
		this.idTokens.put(">", 261);
		this.idTokens.put("<", 262);		
		this.idTokens.put(">=", 263);
		this.idTokens.put("<=", 264);
		this.idTokens.put("=", 265);
		this.idTokens.put("!=", 266);
		//SIMBOLOS		
		this.idTokens.put("(", 267);
		this.idTokens.put(")", 268);
		this.idTokens.put(",", 269);
		this.idTokens.put(";", 270);
		this.idTokens.put("{", 271);
		this.idTokens.put("}", 272);
		this.idTokens.put("'", 273);
		//PALABRAS RESERVADAS
		this.idTokens.put("if", 274);
		this.idTokens.put("then", 275);
		this.idTokens.put("else", 276);
		this.idTokens.put("end_if", 277);
		this.idTokens.put("print", 278);		
		this.idTokens.put("int", 279);
		this.idTokens.put("void", 280);
		this.idTokens.put("case", 281);
		this.idTokens.put("usinteger", 282);
		this.idTokens.put("do", 283);
		this.idTokens.put("double", 284);
//		this.idTokens.put("WHILE", 285);
//		this.idTokens.put("FUN", 286);
//		this.idTokens.put("RETURN", 287);
		//END OF FILE
		this.idTokens.put("\0",290);
		
		
		
		//MATRIZ DE ACCIONES SEMANTICAS
		this.aMatrix=new MatrixActions(17,25);		
		//int maxInt=32767,minInt=-32768; //RANGO CONSTANTE COMUN
		
		BigInteger maxUInt= new BigInteger("65535") ,minUInt= new BigInteger("0");//RANGO CONSTANTE SIN SIGNO --VEEER--
		double minDou=2.2250738585072014E-308, maxDou=1.7976931348623157E308;   /// VEEER
		
		SemanticAction SA0 = new SetBufferEmpty(this); // INICIA BUFFER VACIO
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
		
		//FIXME ver si donde puse SA10(error) va SA18(ultimoChar al inicio y error)
		//ESTADO 0
		this.aMatrix.put(0,0, SA1);this.aMatrix.put(0,1, SA1);this.aMatrix.put(0,2, SA18);this.aMatrix.put(0,3, SA1);
		this.aMatrix.put(0,4, SA1);this.aMatrix.put(0,5, SA1);this.aMatrix.put(0,6, SA11);this.aMatrix.put(0,7, SA11);
		this.aMatrix.put(0,8, SA1);this.aMatrix.put(0,9, SA1);this.aMatrix.put(0,10, SA11);this.aMatrix.put(0,11, SA11);
		this.aMatrix.put(0,12, SA7);this.aMatrix.put(0,13, SA18);this.aMatrix.put(0,14, SA0);this.aMatrix.put(0,15, SA1);
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
			if ((i==1)||(i==2)||(i==3)|(i==8)||(i==9)||(i==14)) {  // l, L, d, u, i, D
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
				this.aMatrix.put(3, i, SA10); // imprimo error
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
				this.aMatrix.put(15, i, SA10); // 
			} else
				if (i==14){ //'
					this.aMatrix.put(15, i, SA8); //
				}else
					this.aMatrix.put(15, i, SA9);
			
		}
		//ESTADO 16
		for(int i=0;i<=18;i++){ 
			if (i==12) { // \n
				this.aMatrix.put(16, i, SA19); // 
			} else
				if (i==14){ // '
					this.aMatrix.put(16, i, SA8);
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
	}
	
	public int getToken() { //in es String entrada
		int state=0;
		int newState=0;
		boolean corte=false;
		while(!corte){  
			if (isRead()){
				if (posRead<in.length()){  //leo todo el file
					this.lastChar=in.charAt(posRead);
					System.out.println("pos read: "+posRead+" lastchar:"+this.lastChar);
					posRead++;
				}
				else{
					lastChar='\0'; // termine de leer el file(String) in
					corte=true;
				}
			}
			this.setRead(true);
			int colum=getColum(lastChar);
			if (colum==12){
				System.out.println("SALTO DE LINEA. "+"ESTADO: "+state);
			}			
			newState=sMatrix.get(state,colum); 
			
			System.out.println("si ESTADO "+state+"  SIMBOLO "+colum+" ---> nuevo estado: "+newState);
			/*
			try {
				Thread.sleep (500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			*/
			SemanticAction a = aMatrix.get(state,colum);
			if (a!=null){
				System.out.println("AS: "+a.toString()+"\n");
				a.execute(buffer,lastChar);
			}
			if (newState==FINAL_STATE){
				// FIXME YYVAL ES DEL PARSER
				//A LA VARIABLE YYLVAL <- tokens.get().getRecord();	
				if (tokens.size() !=0)
					return (tokens.get(tokens.size()-1)).getId();  // devuelvo el id del ultimo token?
				else 
					return-1;
			}
			state=newState;			
		}
		return -1;
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
		
		case 'D':
			return 13; 
		case '*':
			return 11; 
		case '/':
			return 11; 
		case '-':
			return 18; 
		case '+':
			return 10; 
		case '(':
			return 7; 
		case ')':
			return 7; 
		case ',':
			return 7; 
		case ';':
			return 7; 
		case 32:  // espacio en blanco
			return 16; 
		case '\n':
			return 12;
		case '\t':
			return 16;
		case '_':
			return 0; 
		case 'i':
			return 9; 
		case 'u':
			return 8;
		case ':':
			return 4;
		case '=':
			return 6; 
		case '<':
			return 5; 
		case '>':
			return 5;
		case '!':
			return 5;
		case '.':
			return 15;
		case '{':
			return 7;
		case '}':
			return 7;  
		case 39:	// Es el '
			return 14; 
		
		case 0:		//null
			return 18;
		}
		
		
		if ((c>=65)&&(c<=90)){ // mayusculas
			return 2;		
		}
		
		if ((c>=97)&&(c<=122)){ // minusculas
			return 1;
		}
		
		return 17;  // otros ??????????
			
	}
	
	@SuppressWarnings("unused")
	private TableRecord addSymbolToTable(TableRecord a){ 
		int size=this.symbolTable.size();
		int position=symbolTable.indexOf(a);
		if (position==-1){
			this.symbolTable.add(a);
			return a;
		}
		else{
			symbolTable.get(position).increment();
			return symbolTable.get(position);			
		}
		
	}
	
	public boolean isKeyWord(String buffer){
		return idTokens.containsKey(buffer);
	}
	
	public void makeToken(String buffer){
		int id = getID(buffer);
		String type = getType(id);
		TableRecord record=null;
		if ((type == "ID") || (type == "CTE")){
			 record = this.addSymbolToTable(new TableRecord(buffer,type));
		}
		Token t=new Token(id,buffer,this.line,type,record);	
		tokens.add(t);		
	}

	private String getType(int id) {
		if ((274<=id)&&(id<=287)){
			return "PALABRA RESERVADA";
		}
		if ((267<=id)&&(id<=273)){
			return "SIMBOLO";
		}
		if ((id==260)){
			return "ASIGNACION";
		}
		if ((260<id)&&(id<=266)){
			return "COMPARADOR";
		}
		if ((256<=id)&&(id<=259)){
			return "OPERADOR";
		}
		if (id==288){
			return "ID";
		}
		if (id==289){
			return "CTE";
		}
		if (id==290){
			return "EOF";
		}
		
		return null;
	}

	private int getID(String buffer) {
		int value=-1;
		if (this.idTokens.containsKey(buffer)){
			value = this.idTokens.get(buffer);
		}else
			{if (buffer.contains("_ui")|| buffer.contains(".")){
				value = this.idTokens.get("CTE");
			}
			else{value=this.idTokens.get("ID");}			
			}
		
		return value;
	}

	public void addProblem(String e, int line) {
		this.linesProblems.add(line);
		this.problems.add(e);		
	}

	public ArrayList<Integer> getLinesProblems() {
		return (ArrayList<Integer>) this.linesProblems;
	}

	public ArrayList<String> getProblems() {
		return (ArrayList<String>) this.problems;
	}
}
