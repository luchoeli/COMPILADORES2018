package semanticSource;
import lexicalSource.*;
import sintacticSource.Error;
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
			this.analizer.addError("Warning Lexico: "+ Error.lengthID(), this.analizer.getLine(),Error.warning);
			buffer = buffer.substring(0,lenghtMax-1);
			this.analizer.setBuffer(buffer);
			return true;
		}
		
	}
}
