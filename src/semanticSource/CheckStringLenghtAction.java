package semanticSource;
import lexicalSource.*;

public class CheckStringLenghtAction extends SimpleSemanticAction{
	private int lenghtMax;
	
	public CheckStringLenghtAction(LexicalAnalizer analizer, int lenght){
		super(analizer);
		this.lenghtMax=lenght;
	}

	
	public boolean execute(String buffer, char c){
		if (lenghtMax >= buffer.length()){
			return true;
		}else{
			this.analizer.addProblem("Warning Lexico en linea: "+this.analizer.getLine()+". Identificador "+buffer+" excede longitud.",this.analizer.getLine());
			buffer = buffer.substring(0,lenghtMax-1);
			this.analizer.setBuffer(buffer);
			return true;
		}
		
	}
}
