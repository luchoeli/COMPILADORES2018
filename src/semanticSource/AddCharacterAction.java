package semanticSource;
import lexicalSource.*;

public class AddCharacterAction extends SimpleSemanticAction{

	public AddCharacterAction(LexicalAnalizer analizer){
		super(analizer);
	}
	
	public boolean execute(String buffer, char c){
		//System.out.println("addCharacter ASS");
		buffer += c;
		this.analizer.setBuffer(buffer);
		return true;
	}
}
