package semanticSource;

import lexicalSource.LexicalAnalizer;

public class SetBufferEmpty extends SimpleSemanticAction {

	public SetBufferEmpty(LexicalAnalizer analizer) {
		super(analizer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(String buffer, char c) {
		buffer="";
		this.analizer.setBuffer(buffer);
		return true;
	}

}
