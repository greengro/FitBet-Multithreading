package multithread;

public class JDBCMain {
	
	public static String url = "jdbc:postgresql://localhost:5555/fitbet?useUnicode=true&useJDBCCompliantTimezoneShift="
			+ "true&useLegacyDatetimeCode=false&serverTimezone=America/Los_Angeles";
	public static String user = "postgres";
	public static String pwd = "postgres";
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Checker check = new Checker(); 
		CPUBets cpuBet = new CPUBets();
	
		check.start(); 
		cpuBet.start();
		
		
		
		
		
	}

}
