package sintacticSource;

public class ParserWrapper {
public Parser p;
	public int Parse(){
		
		return p.yyparse();
	}
}
