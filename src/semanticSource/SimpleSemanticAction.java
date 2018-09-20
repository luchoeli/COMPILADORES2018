package semanticSource;
import lexicalSource.*;

public abstract class SimpleSemanticAction implements SemanticAction{
	LexicalAnalizer analizer;
	
	public SimpleSemanticAction(LexicalAnalizer analizer){
		this.analizer = analizer;
	}
	
	public LexicalAnalizer getAnalizer(){
		return analizer;
	}

	public abstract boolean execute(String buffer, char c);
}
