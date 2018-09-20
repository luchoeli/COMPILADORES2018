package semanticSource;
import lexicalSource.*;

public class CharacterToInputAction extends SimpleSemanticAction {

	public CharacterToInputAction(LexicalAnalizer analizer){
		super(analizer);
	}
	
	public boolean execute(String buffer, char c){
		analizer.setRead(false);
		return true;
	}
}
