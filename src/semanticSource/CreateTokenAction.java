package semanticSource;
import lexicalSource.*;

public class CreateTokenAction extends SimpleSemanticAction{
	
	public CreateTokenAction(LexicalAnalizer analizer){
		super(analizer);
	}
	
	public boolean execute(String buffer, char c){
		analizer.makeToken(buffer);
		return true;
	}

}
