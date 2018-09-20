package semanticSource;
import lexicalSource.*;

public class CheckKeyWord extends SimpleSemanticAction {
	
	public CheckKeyWord(LexicalAnalizer analizer){
		super(analizer);
	}
	
	public boolean execute(String buffer, char c){
		if (this.analizer.isKeyWord(buffer)){
			return true;
		}else{
			this.analizer.addProblem("Error lexico en linea: "+this.analizer.getLine()+". Palabra reservada "+buffer+" incorrecta.",this.analizer.getLine());
			buffer = "";
			return false;
		}
	}

}
