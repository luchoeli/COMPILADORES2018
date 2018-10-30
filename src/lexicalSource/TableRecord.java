package lexicalSource;

public class TableRecord {
	private String lexema; 
	private int idToken;
	private String type; // Tipo de identificador o constante ( INT o UNSIGNED LONG)
	private int ref; // Cantidad de referencias.
	private String uso; 
	private String ambito;


	public String getUso() {
		return uso;
	}

	public void setUso(String uso) {
		this.uso = uso;
	}

	public void setRef(int ref) {
		this.ref = ref;
	}

	private String value = "0";
	
	public TableRecord(String lexema, int idToken){
		this.lexema = lexema;
		this.setIdToken(idToken);
		this.ref = 1;
	}

	public TableRecord(String lexema, int idToken, String value){
		this.lexema = lexema;
		this.setIdToken(idToken);
		this.ref = 1;
		this.value = value;
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

}
