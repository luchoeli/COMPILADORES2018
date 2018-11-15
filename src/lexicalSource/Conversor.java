package lexicalSource;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.xml.internal.ws.wsdl.writer.UsingAddressing;

import semanticSource.CheckRangeActionUnsigned;
import sintacticSource.Error;

public class Conversor {
	
	public static String dameUSINTEGER(String buffer) {
		String entero=buffer.substring(0, buffer.length()-3);
		return entero;
	}

}
