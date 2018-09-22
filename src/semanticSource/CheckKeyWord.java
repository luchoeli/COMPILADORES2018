package semanticSource;
import lexicalSource.*;
import sintacticSource.Error;

public class CheckKeyWord extends SimpleSemanticAction {
	
	public CheckKeyWord(LexicalAnalizer analizer){
		super(analizer);
	}
	
	public boolean execute(String buffer, char c){
		if (this.analizer.isKeyWord(buffer)){
			return true;
		}else{
			this.analizer.addError("Error lexico: "+Error.keyWord(), this.analizer.getLine());
			buffer = "";
			return false;
		}
	}

}
