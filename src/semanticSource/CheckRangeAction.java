package semanticSource;
import lexicalSource.*;

import java.util.logging.Logger;
import java.util.logging.Level;
public class CheckRangeAction extends SimpleSemanticAction{
	private double max, min;
	
	public CheckRangeAction(LexicalAnalizer analizer, Double min, Double max){
		super(analizer);
		this.max=max;
		this.min=min;
	}

	public boolean execute(String buffer, char c){
		
		String entero=buffer.substring(0, buffer.length());
		entero = entero.replace("D","E");
		Double value;
		boolean error=true;
		try{			
			value = Double.parseDouble(entero);
			//System.out.println(min+" < "+value+" < "+max);
			if (((value < max) && (value > min)) || (value==0.0)){
				//System.out.println("CUMPLE RANGO");
				error=false;}
    	} catch (Exception ex) {
    		Logger.getLogger(CheckRangeAction.class.getName()).log(Level.SEVERE, null, ex);
    	}
		if (error){
			
			System.out.println("Error Lexico en linea: "+this.analizer.getLine()+". Constante double "+buffer+" fuera de rango.");
			this.analizer.addError("Error Lexico: Constante double fuera de rango ", this.analizer.getLine());
			return false;   
		}
		return true;
	}
}
