package lexicalSource;

import semanticSource.*;

public class MatrixActions {

	private SemanticAction[][] matrix;	
	
	public MatrixActions(int states, int colums){
		this.matrix=new SemanticAction[states][colums];
	}
	
	public SemanticAction get(int state, int colum) {
		return matrix[state][colum];
	}
	
	public void put(int state, int colum, SemanticAction SA){
		
			matrix[state][colum]=SA;
	}

}
