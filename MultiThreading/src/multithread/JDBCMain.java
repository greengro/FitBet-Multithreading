package multithread;

public class JDBCMain {
	
	public static String url = "jdbc:postgresql://localhost:5555/fitbet?stringtype=unspecified";
	public static String user = "postgres";
	public static String pwd = "postgres";
	public static void main(String[] args) {
		System.out.println("Hello there jared");
		// TODO Auto-generated method stub

		Checker check = new Checker(); 
	
		check.run(); 
	}

}
