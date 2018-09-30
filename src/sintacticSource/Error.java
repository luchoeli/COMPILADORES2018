package sintacticSource;
public class Error implements Comparable<Error>{
	public String description;
	public int nroLine;
	public int severity;
	
	public Error(String description, int nroLine){
		this.description = description;
		this.nroLine = nroLine;
		severity = Error.error;
	}
	
	public static String keyWord(){
		return "Palabra reservada incorrecta.";
	}
	
	public static String intRange(){
		return "Constante entera fuera de rango.";
	}
	
	public static String unsignedLongRange(){
		return "Constante entera sin signo fuera de rango.";
	}
	
	public static String lengthID(){
		return "Identificador excede longitud.";
	}
	
	public static String charUndefined(){
		return "Caracter inesperado.";
	}
	
	public static String assignment(){
		return "Asignacion invalida.";
	}
	
	public static String expression(){
		return "Expresion invalida.";
	}
	
	public static final int warning = 0;
	public static final int error = 1;
	

	@Override
	public int compareTo(Error e) {
		if (nroLine < e.nroLine){
			return -1;
		}else{
			if (nroLine>e.nroLine){
				return 1;
			}
			else{
				return 0;
			}
		}
	}
	

	

}
