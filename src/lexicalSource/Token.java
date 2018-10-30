package lexicalSource;

import sintacticSource.Nodo;

public class Token {

	private int id;
	private String lexema;
	private int nroLine;
	private String type;
	private TableRecord record;
	private Nodo nodo;
	
	public Token(int id, String lexema, int nroLine, String type,TableRecord record, Nodo nodo){
		this.id=id;
		this.lexema=lexema;
		this.nroLine=nroLine;
		this.type=type;
		this.record=record;
		this.nodo = nodo;
	}
	
	public TableRecord getRecord() {
		return record;
	}
	public void setRecord(TableRecord record) {
		this.record = record;
	}
	public String getType() {
		return type;
	}
	public int getId() {
		return id;
	}
	public String getLexema() {
		return lexema;
	}
	public int getNroLine() {
		return nroLine;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setLexema(String lexema) {
		this.lexema = lexema;
	}
	public void setNroLine(int nroLine) {
		this.nroLine = nroLine;
	}
	public void setType(String type) {
		this.type = type;
	}

	public Nodo getNodo() {
		return nodo;
	}

	public void setNodo(Nodo nodo) {
		this.nodo = nodo;
	}
	
}
