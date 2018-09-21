package semanticSource;

import java.util.ArrayList;
import java.util.List;

import lexicalSource.*;

public class CompositeAction implements SemanticAction{
	private List<SemanticAction> semanticActions;
	
	public CompositeAction(){
		this.semanticActions = new ArrayList<SemanticAction>();
	}
	
	public void add(SemanticAction sa){
		semanticActions.add(sa);
	}
	
	public boolean execute(String buffer, char c){
		for (SemanticAction action : semanticActions){
			//System.out.println("ASC: "+action.toString()+"\n");
			if (!action.execute(buffer, c)){
				return false;
			}
			buffer = ((SimpleSemanticAction)action).getAnalizer().getBuffer();
		}
		return true;
	}
}
