package semanticSource;
import lexicalSource.*;

public class SetBufferAction extends SimpleSemanticAction{

	public SetBufferAction(LexicalAnalizer analizer){
		super(analizer);
	}
	
	public boolean execute(String buffer, char lastChar){
		buffer=""+ lastChar;
		this.analizer.setBuffer(buffer);
		return true;
	}
}
