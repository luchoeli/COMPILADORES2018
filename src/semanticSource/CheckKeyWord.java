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
			System.out.println("no es pala reserv");
			this.analizer.addError("LEX- Error lexico: "+Error.keyWord(), this.analizer.getLine());
			buffer = ""; //FIXME esta mal que se sete null.?
			return false;
		}
	}

}
