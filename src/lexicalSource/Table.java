package lexicalSource;

import java.util.ArrayList;
import java.util.Hashtable;

public class Table {

	private Hashtable<String,TableRecord> table;
	
	public Table(){
		table = new Hashtable<String,TableRecord>();
	}
	
	public Table(Hashtable<String,TableRecord> t){
		table=t;
	}

	public void put(String key, TableRecord tr){
		if (this.table.contains(key)) {
			//System.out.println("ya esta, incremento");
			this.table.get(key).increment();
		}else{
			//System.out.println("no esta, inserto");
			table.put(key, tr);
		}

	}
	
	public TableRecord get(String key){
		return table.get(key);
	}
	
	public void remove(String key) {
		table.remove(key);		
	}
	
	public boolean contains(String key){
		return table.containsKey(key);
		
	}
	
	public boolean containsLexema(String lexema){
		ArrayList<String> keys = new ArrayList<String>(table.keySet());
		for (String k : keys){
			if (k.equals(lexema)){
				return true;
			}

		}
		return false;
	}

	public ArrayList<TableRecord> getElements(){
		ArrayList<TableRecord> e = new ArrayList<TableRecord>(table.values());
		return e;
	}


}
