package semanticSource;
import lexicalSource.*;
import sintacticSource.Error;

public class PrintErrorAction extends SimpleSemanticAction{
		
	public PrintErrorAction(LexicalAnalizer analizer){
		super(analizer);
	}
		
	public boolean execute(String buffer, char c){
		int lines=this.analizer.getLine();
		this.analizer.addError("Error Lexico:"+Error.charUndefined(), lines);
		buffer= "";
		this.analizer.setBuffer(buffer);
		return true;
	}
}
