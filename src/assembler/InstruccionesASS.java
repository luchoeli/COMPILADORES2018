package assembler;

public class InstruccionesASS {


	public String sumaUsinteger(String izq, String der, String aux) {
		
		String codigo=";-------------  ADD USINT ---- ("+izq+":="+der+") \n";
		codigo += "MOV ax, " + izq + "\n";
		codigo += "ADD ax, " + der + "\n";
		codigo += "MOV "+aux+", ax" + "\n";
		codigo += "JO @LABEL_OVERFLOW" + "\n";
		return codigo;
	}
	
	public String sumaDouble(String izq, String der, String aux) {
		
		String codigo=";-------------  ADD DOUBLE ---- ("+izq+":="+der+") \n";
		codigo += "MOV ax, " + izq + "\n";
		codigo += "ADD ax, " + der + "\n";
		codigo += "MOV "+aux+", ax" + "\n";
		codigo += "JO @LABEL_OVERFLOW" + "\n";
		
		return codigo;
	}
	
	/** **/
	public String asignacionUSINT(String izq, String der){
		
		String codigo=";-------------  ASIG USINT ---- ("+izq+":="+der+")\n";
		codigo += "MOV ax, " + der + "\n";
		codigo += "MOV " + izq + ", ax" + "\n";
		return codigo;
	}
	
	public String asignacionDOUB(String izq, String der){
		String codigo=";-------------  ASIG DOUBLE ---- ("+izq+":="+der+")\n";
		codigo += "FLD " + der + "\n";
		codigo += "FSTP " + izq + "\n";
		
		return codigo;
	}

	public String restaUsinteger(String left, String rigth, String varAux) {
		String codigo=";-------------  SUB USINT ---- ("+left+"-"+rigth+")\n";
		codigo += "MOV ax, " + left + "\n";
		codigo += "SUB ax, " + rigth + "\n";
		codigo += "MOV "+varAux+ ", ax" + "\n";
		codigo += "JS @LABEL_PERDIDAINFO:"+ "\n";
		return codigo;
	}

	public String restaDouble(String left, String rigth, String varAux) {
		String codigo=";-------------  SUB DOUBLE ---- ("+left+"-"+rigth+")\n";
		codigo += "FLD " + left + "\n";
		codigo += "FILD " + rigth + "\n";
		codigo += "FSUB " + "\n";
		codigo += "FSTP " + varAux + "\n";
		return codigo;
	}

	public String multiplicaUsinteger(String left, String rigth, String varAux) {
		// TODO Auto-generated method stub
		return null;
	}

	public String multiplicaDouble(String left, String rigth, String varAux) {
		String codigo=";-------------  MULT DOUBLE ---- ("+left+"*"+rigth+")\n";
		codigo += "FLD " + left + "\n";
		codigo += "FMUL " + rigth + "\n";
		codigo += "FSTP " + varAux + "\n";
		return codigo;
	}

	public String divideDouble(String left, String rigth, String varAux) {
		String codigo=";-------------  DIV DOUBLE ---- ("+left+"/"+rigth+")\n";
		codigo += "FLD " + rigth + "\n"; //Introduce una copia de mem en ST
		codigo += "FLDZ" + "\n"; //Introduce el número cero en ST
		codigo += "FCOM" + "\n"; // Compara ST y ST(1).
		codigo += "FSTSW aux_mem_2bytes" + "\n"; //Almacena la palabra de estado en la memoria.
		codigo += "MOV ax , aux_mem_2bytes" + "\n";
		codigo += "SAHF" + "\n";   // Almacena en los ocho bits menos significativos del registro de indicadores el valor del registro AH. Operación: SF:ZF:X:AF:X:PF:X:CF  AH
		codigo += "JE @LABEL_DIVIDEZERO" + "\n";
		codigo += "FLD " + left + "\n";
		codigo += "FDIV " + rigth + "\n";
		codigo += "FSTP " + varAux + "\n"; // Extrae una copia de ST en mem
		
		return codigo;
	}

	public String divideUsinteger(String left, String rigth, String varAux) {
		String codigo=";-------------  DIV USINT---- ("+left+"/"+rigth+")\n";
		codigo += "MOV ax, " + rigth  + "\n";
		codigo += "CMP " + rigth +", @0" + "\n";
		codigo += "JE @LABEL_DIVIDEZERO" + "\n";;
		codigo += "MOV var_aux_dx, dx" + "\n";
		codigo += "MOV ax, " + left  + "\n";
		codigo += "CWD" + "\n";   //Extiende el signo de AX en DX:AX. No se afectan flags.
		codigo += "MOV bx, " + rigth + "\n";
		codigo += "DIV bx" + "\n";
		codigo += "MOV dx, var_aux_dx" + "\n";
		codigo += "MOV " + varAux + ", ax" + "\n";
		return codigo;
	}

	public String usintComparador(String left, String rigth) {
		String codigo=";-------------  COMP USINT ---- ("+left+" comp "+rigth+")\n";
		codigo += "MOV ax, " + left + "\n";
		codigo += "CMP ax, " + rigth + "\n";
		return codigo;
	}

	public String doubleComparador(String left, String rigth) {
		String codigo=";-------------  COMP DOUBLE ---- ("+left+" comp "+rigth+")\n";
		codigo += "FLD " + rigth + "\n";
		codigo += "FLD " + left + "\n";
		codigo += "FCOM" + "\n";
		codigo += "FSTSW aux_mem_2bytes" + "\n";
		codigo += "MOV AX , aux_mem_2bytes" + "\n";
		codigo += "SAHF" + "\n";
		return codigo;
	}
	
	

}
