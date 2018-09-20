package semanticSource;
import lexicalSource.*;
import java.util.logging.Logger;
import java.util.logging.Level;
public class CheckRangeAction extends SimpleSemanticAction{
	private double max, min;
	
	public CheckRangeAction(LexicalAnalizer analizer, double min, double max){
		super(analizer);
		this.max=max;
		this.min=min;
	}

	public boolean execute(String buffer, char c){
		//TODO ver el -3
		//System.out.println("---------"+ buffer.length()-3);
		String entero=buffer.substring(0, buffer.length());
		entero = entero.replace("D","E");
		double value;
		boolean error=true;
		try{			
			value = Double.parseDouble(entero);
			System.out.println("VALOR: "+value);
			if (((value <= max) && (value >= min)) || (value==0.0)){
				//System.out.println("CUMPLE RANGO");
				error=false;}
    	} catch (Exception ex) {
    		Logger.getLogger(CheckRangeActionUnsigned.class.getName()).log(Level.SEVERE, null, ex);
    	}
		if (error){
			
			System.out.println("Error Lexico en linea: "+this.analizer.getLine()+". Constante entera "+buffer+" fuera de rango.");
			this.analizer.addProblem("Error Lexico en linea: "+this.analizer.getLine()+". Constante entera "+buffer+" fuera de rango.",this.analizer.getLine());
			return false;   
		}
		return true;
	}
}
