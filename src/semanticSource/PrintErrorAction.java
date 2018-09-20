package semanticSource;
import lexicalSource.*;

public class PrintErrorAction extends SimpleSemanticAction{
		
	public PrintErrorAction(LexicalAnalizer analizer){
		super(analizer);
	}
		
	public boolean execute(String buffer, char c){
		int lines=this.analizer.getLine();
		this.analizer.addProblem("Error Lexico en linea "+lines+": Caracter invalido :" + c, lines);
		buffer= "";
		this.analizer.setBuffer(buffer);
		return true;
	}
}
