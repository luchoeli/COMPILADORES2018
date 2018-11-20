package assembler;

public class InstruccionesASS {
	String format = "%-25s%s%n";

	public String sumaUsinteger(String izq, String der, String aux) {
		
		String codigo="";
		codigo+=String.format(format,"\t MOV ax, " + izq, ";-------------  ADD USINT ---- ("+izq+"+"+der+") ");
		codigo += "\t ADD ax, " + der + "\n";
		codigo += "\t MOV "+aux+", ax" + "\n";
		codigo += "\t JO @LABEL_OVERFLOW" + "\n";
		return codigo;
	}
	
	public String sumaDouble(String izq, String der, String aux) {
		String codigo="";
		codigo += String.format(format,"\t FLD " + izq, ";-------------  ADD DOUBLE ---- ("+izq+"+"+der+") ");
		codigo += "\t FADD " + der + "\n";
		codigo += "\t FSTP " + aux + "\n";
		codigo += "\t FLD " + aux + "\n";
		codigo += "\t FXAM" + "\n";
		codigo += "\t FSTSW aux_mem_2bytes" + "\n";
		codigo += "\t MOV ax , aux_mem_2bytes" + "\n";
		codigo += "\t FWAIT" + "\n";
		codigo += "\t SAHF" + "\n";
		codigo += "\t JZ @LABEL_SIGUIENTE_INSTRUCCION" + aux+ "\n";
		codigo += "\t JPE @LABEL_C2is1" + aux + "\n";
		codigo += "\t JMP @LABEL_SIGUIENTE_INSTRUCCION" + aux+ "\n";
		codigo += "\t @LABEL_C2is1"+ aux+ ":" + "\n";
		codigo += "\t JC    @LABEL_OVERFLOW" + "\n";
		codigo += "\t @LABEL_SIGUIENTE_INSTRUCCION"+ aux +":"+ "\n";  
		return codigo;
	}
	
	/** **/
	public String asignacionUSINT(String izq, String der){
		
		String codigo="";
		codigo += String.format(format,"\t MOV ax ," + der, ";-------------  ASIG USINT ---- ("+izq+":="+der+") ");
		codigo += "\t MOV " + izq + ", ax" + "\n";
		return codigo;
	}
	
	public String asignacionDOUB(String izq, String der){
		String codigo="";
		codigo+=String.format(format,"\t FLD " + der, ";-------------  ASIG DOUBLE ---- ("+izq+"+"+der+") ");
		codigo += "\t FSTP " + izq + "\n";
		
		return codigo;
	}

	public String restaUsinteger(String left, String rigth, String varAux) {
		String codigo="";
		codigo+=String.format(format,"\t MOV ax, " + left, ";-------------  SUB USINT ---- ("+left+"-"+rigth+")");
		
		codigo += "\t SUB ax, " + rigth + "\n";
		codigo += "\t MOV "+varAux+ ", ax" + "\n";
		codigo += "\t MOV ax, " + left + "\n";
		codigo += "\t CMP ax, " + rigth + "\n";
		codigo += "\t JB @LABEL_RESULTADO"+ "\n";
		return codigo;
	}

	public String restaDouble(String left, String rigth, String varAux) {
		String codigo="";
		codigo+=String.format(format,"\t FLD " + left,";-------------  SUB DOUBLE ---- ("+left+"-"+rigth+")");
		codigo += "\t FILD " + rigth + "\n";
		codigo += "\t FSUB " + "\n";
		codigo += "\t FSTP " + varAux + "\n";
		return codigo;
	}

	public String multiplicaUsinteger(String left, String rigth, String varAux) {
		String codigo="";
		codigo+=String.format(format,"\t MOV ax, " + left,";-------------  MULT USINT---- ("+left+"*"+rigth+")");
		//codigo += "\t MOV ax, " + left + "\n";
		codigo += "\t MOV bx, " + rigth + "\n";
		codigo += "\t MUL bx" + "\n";
		codigo += "\t MOV "+varAux+ ", ax" + "\n";
		return codigo;
	}

	public String multiplicaDouble(String left, String rigth, String varAux) {
		String codigo="";
		codigo+=String.format(format,"\t FLD " + left,";-------------  MULT DOUBLE ---- ("+left+"*"+rigth+")");
		codigo += "\t FMUL " + rigth + "\n";
		codigo += "\t FSTP " + varAux + "\n";
		return codigo;
	}

	public String divideDouble(String left, String rigth, String varAux) {
		String codigo="";
		codigo+=String.format(format,"\t FLD " + rigth,";-------------  DIV DOUBLE ---- ("+left+"/"+rigth+")");
	//	codigo += "\t FLD " + rigth + "\n"; //Introduce una copia de mem en ST
		codigo += "\t FLDZ" + "\n"; //Introduce el número cero en ST
		codigo += "\t FCOM" + "\n"; // Compara ST y ST(1).
		codigo += "\t FSTSW aux_mem_2bytes" + "\n"; //Almacena la palabra de estado en la memoria.
		codigo += "\t MOV ax , aux_mem_2bytes" + "\n";
		codigo += "\t SAHF" + "\n";   // Almacena en los ocho bits menos significativos del registro de indicadores el valor del registro AH. Operación: SF:ZF:X:AF:X:PF:X:CF  AH
		codigo += "\t JE @LABEL_DIVIDEZERO" + "\n";
		codigo += "\t FLD " + left + "\n";
		codigo += "\t FDIV " + rigth + "\n";
		codigo += "\t FSTP " + varAux + "\n"; // Extrae una copia de ST en mem
		
		return codigo;
	}

	public String divideUsinteger(String left, String rigth, String varAux) {
		String codigo="";
		codigo+=String.format(format,"\t MOV ax, " + rigth ,";-------------  DIV USINT---- ("+left+"/"+rigth+")");
		//codigo += "\t MOV ax, " + rigth  + "\n";
		codigo += "\t CMP " + rigth +", 0" + "\n";
		codigo += "\t JE @LABEL_DIVIDEZERO" + "\n";;
		codigo += "\t MOV var_aux_dx, dx" + "\n";
		codigo += "\t MOV ax, " + left  + "\n";
		codigo += "\t CWD" + "\n";   //Extiende el signo de AX en DX:AX. No se afectan flags.
		codigo += "\t MOV bx, " + rigth + "\n";
		codigo += "\t DIV bx" + "\n";
		codigo += "\t MOV dx, var_aux_dx" + "\n";
		codigo += "\t MOV " + varAux + ", ax" + "\n";
		return codigo;
	}

	public String usintComparador(String left, String rigth) {
		String codigo="";
		codigo+=String.format(format,"\t MOV ax, " + left,";-------------  COMP USINT ---- ("+left+" comp "+rigth+")");
		codigo += "\t CMP ax, " + rigth + "\n";
		return codigo;
	}

	public String doubleComparador(String left, String rigth) {
		String codigo="";
		codigo+=String.format(format,"\t FLD " + rigth ,";-------------  COMP DOUBLE ---- ("+left+" comp "+rigth+")");
		//codigo += "\t FLD " + rigth + "\n";
		codigo += "\t FLD " + left + "\n";
		codigo += "\t FCOM" + "\n";
		codigo += "\t FSTSW aux_mem_2bytes" + "\n";
		codigo += "\t MOV AX , aux_mem_2bytes" + "\n";
		codigo += "\t SAHF" + "\n";
		return codigo;
	}
	
	

}
