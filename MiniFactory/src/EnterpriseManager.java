import java.util.HashMap;
import java.util.Map;

public class EnterpriseManager {
	private Map<String, Enterprise> enterprises = new HashMap<String, Enterprise>();
	
	public EnterpriseManager() {
		info("Запуск.");
	}
	
	public void add(String name, Enterprise enterprise) {
		Enterprise e = enterprises.put(name, enterprise);
	}
	
	public void delete(String name) {
		
	}
	
	protected void info(String s) {
		System.out.println("EM>"+s);
	}
	
	public String getKeysAsString() {
		String s = "";
		boolean b = false;
		for (String ss : enterprises.keySet()) {
			if (b) s += ", "; 
			else b = true;
			s += ss;
		}
		return s;
	}
}
