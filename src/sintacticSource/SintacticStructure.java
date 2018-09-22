package sintacticSource;

public class SintacticStructure {
	private int line;
	private String type;
	private String description;
	
	public SintacticStructure(int line,String type, String description) {
		this.line=line;
		this.type=type;
		this.description=description;
	}
	
	public int getLine() {
		return line;
	}
	public String getDescription() {
		return description;
	}
	public String getType() {
		return type;
	}
}
