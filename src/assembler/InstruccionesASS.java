package assembler;

public class InstruccionesASS {
	
	private int auxNro = 0;

	public String sumaUsinteger(String izq, String der, String aux) {
		
		String codigo="";
		codigo += "MOV ax, " + izq + "\n";
		codigo += "ADD ax, " + der + "\n";
		codigo += "MOV "+aux+", ax" + "\n";
		codigo += "JO @LABEL_OVERFLOW" + "\n";
		return codigo;
	}
	
	public String sumaDouble(String izq, String der, String aux) {
		
		String codigo="";
		codigo += "MOV ax, " + izq + "\n";
		codigo += "ADD ax, " + der + "\n";
		codigo += "MOV "+aux+", ax" + "\n";
		codigo += "JO @LABEL_OVERFLOW" + "\n";
		
		return codigo;
	}
	
	/** **/
	public String asignacionUSINT(String izq, String der){
		
		String codigo="";
		codigo += "MOV ax, " + der + "\n";
		codigo += "MOV " + izq + ", ax" + "\n";
		return codigo;
	}
	
	public String asignacionDOUB(String izq, String der){
		String codigo="";
		codigo += "FLD " + der + "\n";
		codigo += "FSTP " + izq + "\n";
		
		return codigo;
	}

	public String restaUsinteger(String left, String rigth, String varAux) {
		String codigo="";
		codigo += "MOV ax, " + left + "\n";
		codigo += "SUB ax, " + rigth + "\n";
		codigo += "MOV "+varAux+ ", ax" + "\n";
		codigo += "JS @LABEL_PERDIDAINFO:"+ "\n";
		return codigo;
	}

	public String restaDouble(String left, String rigth, String varAux) {
		String codigo="";
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
		// TODO Auto-generated method stub
		return null;
	}

	public String divideDouble(String left, String rigth, String varAux) {
		// TODO Auto-generated method stub
		return null;
	}

	public String divideUsinteger(String left, String rigth, String varAux) {
		// TODO Auto-generated method stub
		return null;
	}

	public String igualUsintComparador(String left, String rigth) {
		String codigo="";
		codigo += "MOV ax, " + left + "\n";
		codigo += "CMP ax, " + rigth + "\n";
		return codigo;
	}

	public String igualDoubleComparador(String left, String rigth) {
		String codigo="";
		codigo += "FLD " + rigth + "\n";
		codigo += "FLD " + left + "\n";
		codigo += "FCOM" + "\n";
		codigo += "FSTSW aux_mem_2bytes" + "\n";
		codigo += "MOV AX , aux_mem_2bytes" + "\n";
		codigo += "SAHF" + "\n";
		return codigo;
	}
}
