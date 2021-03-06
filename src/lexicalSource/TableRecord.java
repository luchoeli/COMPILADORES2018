package lexicalSource;

import lexicalSource.LexicalAnalizer;
 
public class TableRecord {
	private String lexema; 
	private int idToken;	// id o constante
	private String type; // Tipo de identificador o constante ( INT o UNSIGNED LONG)
	private int ref; // Cantidad de referencias.
	private String uso; 
	private String ambito;
	private String value = "0";
	
	private boolean written ;
	private boolean passed ;
	
	
	public String getUso() {
		return uso;
	}

	public void setUso(String uso) {
		this.uso = uso;
	}

	public void setRef(int ref) {
		this.ref = ref;
	}


	
	public TableRecord(String lexema, int idToken){
		this.lexema = lexema;
		this.setIdToken(idToken);
		this.ref = 1;
		setWritten(false);
		setPassed(false);
		
		if (idToken == LexicalAnalizer.DOUBLE){
			
		}
		if (idToken == LexicalAnalizer.USINTEGER){
			
		}
	}

	public TableRecord(String lexema, int idToken, String value){
		this.lexema = lexema;
		this.setIdToken(idToken);
		this.ref = 1;
		this.value = value;
		setWritten(false);
		setPassed(false);
	}

	public void setValue(String value){
		this.value = value;
	}

	public String getValue(){
		return this.value;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public String getLexema() {
		return lexema;
	}
	public void setLexema(String lexema) {
		this.lexema = lexema;
	}
	public int getRef(){
		return ref;
	}
	
	public void increment(){
		this.ref+=1;
	}
	
	public void decrement(){
		this.ref-=1;
	}
	
	public int getIdToken() {
		return idToken;
	}

	public void setIdToken(int idToken) {
		this.idToken = idToken;
	}
	
	public void print(){
		
		System.out.println("---------"+lexema+"-----------"); 
		System.out.println("idTken: "+idToken);
		System.out.println("type: "+type);
		System.out.println("ref: "+ref);
		System.out.println("value: "+value);
		System.out.println("---------------------------");

	}

	public String getAmbito() {
		return ambito;
	}

	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}

	public boolean isWritten() {
		return written;
	}

	public void setWritten(boolean written) {
		this.written = written;
	}

	public boolean isPassed() {
		return passed;
	}

	public void setPassed(boolean passed) {
		this.passed = passed;
	}
	

}
