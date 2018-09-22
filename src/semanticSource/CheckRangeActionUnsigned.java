package semanticSource;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import sintacticSource.Error;
import lexicalSource.*;


public class CheckRangeActionUnsigned extends SimpleSemanticAction {

	private BigInteger max, min;
	
	public CheckRangeActionUnsigned(LexicalAnalizer analizer, BigInteger min, BigInteger max){
		super(analizer);
		this.max=max; 
		this.min=min;
	}

	public boolean execute(String buffer, char c) {
		String entero=buffer.substring(0, buffer.length()-3);
		BigInteger value;
		boolean error=true;
		try{
			value = BigInteger.valueOf(Long.parseLong(entero));
			if ((value.compareTo(max) == -1) && (value.compareTo(min) == 1 )){
				error=false;
			}
    	} catch (Exception ex) {
    		Logger.getLogger(CheckRangeActionUnsigned.class.getName()).log(Level.SEVERE, null, ex);
    	}
		if (error){
			this.analizer.addError("Error Lexico:"+ Error.unsignedLongRange(), this.analizer.getLine());
			return false;   
		}
		return true;
	}
	
}


