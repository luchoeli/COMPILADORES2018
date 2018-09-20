package semanticSource;

import lexicalSource.LexicalAnalizer;

public class DiscardBuffer extends SimpleSemanticAction {
	
	
	public DiscardBuffer(LexicalAnalizer analizer) {
		super(analizer);
	}

	@Override
	public boolean execute(String buffer, char c) {
		buffer = buffer.substring(0, buffer.length()-1);
		System.out.println("BUFFER: "+buffer);
		this.analizer.setBuffer(buffer);
		return true;
	}

}
