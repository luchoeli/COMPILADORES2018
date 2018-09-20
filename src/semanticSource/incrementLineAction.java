package semanticSource;
import lexicalSource.*;

public class incrementLineAction extends SimpleSemanticAction {

	public incrementLineAction(LexicalAnalizer analizer) {
		super(analizer);
	}

	@Override
	public boolean execute(String buffer, char c) {
		this.analizer.incrementLine();
		return true;
	}

}
