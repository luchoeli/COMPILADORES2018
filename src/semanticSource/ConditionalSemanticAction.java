package semanticSource;

public class ConditionalSemanticAction implements SemanticAction{
	private SemanticAction a1,a2,a3;
	
	public ConditionalSemanticAction(SemanticAction a1, SemanticAction a2, SemanticAction a3){
		this.a1 = a1;
		this.a2 = a2;
		this.a3 = a3;
	}
	
	public boolean execute(String buffer, char c){
		if (a1.execute(buffer, c))
			return a2.execute(buffer, c);
		else 
			return a3.execute(buffer, c);
	}

}
