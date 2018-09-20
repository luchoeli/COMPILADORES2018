package lexicalSource;

public class TableRecord {
	private String lexema;
	private String type;
	private int total;
	
	TableRecord(String lexema, String type){
		this.lexema=lexema;
		this.type=type;
		this.total=1;
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
	public int getTotal(){
		return total;
	}
	
	public void increment(){
		this.total+=1;
	}
	
	@Override
	public boolean equals(Object o){
		if (this==o)return true;
		if (!(o instanceof TableRecord))return false;
		TableRecord aComparar=(TableRecord)o;
		return (aComparar.getLexema().equals(lexema) && aComparar.getType().equals(type));		
	}

}
