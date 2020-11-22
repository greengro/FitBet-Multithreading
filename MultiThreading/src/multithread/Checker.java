package multithread;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;






public class Checker extends Thread{

	public static Integer numBets = 10;
	public static ArrayList<Bet> betArr;
	public static ArrayList<Integer> betIDs;
	public static Bet bet;

	//public Checker(int house_id)
	@Override
	public void run() {
		System.out.println("We are running the checker");

		String sql = "SELECT COUNT(*)"
				+ "FROM public.bets_bet"
				+ " WHERE active=true AND bet_owner_user_id_id="+Integer.toString(JDBCMain.myId);
		System.out.println("Using Query: ("+sql+")");

		while(true) 
		{
			try { 
				Thread.sleep(1000);
			} 
			catch(Exception e) 
			{ 
				e.printStackTrace();
			}
			System.out.println("We are checking rn");


			try(Connection betsConn = DriverManager.getConnection(JDBCMain.url, JDBCMain.user, JDBCMain.pwd);
					Statement statement = betsConn.createStatement()) 
			{ 
				ResultSet rs = statement.executeQuery(sql);
				rs.next();
				int count = rs.getInt(1);
				System.out.println("We saw we have "+Integer.toString(count)+" cpu bets");
				if(count < numBets)
				{ 
					int remainder = numBets - count;
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
					LocalDateTime now = LocalDateTime.now(); 
					String currDate = dtf.format(now);
					for(int i = 0; i < remainder; i++) 
					{ 
						int minSteps = 10000; 
						int maxSteps = 100000;
						int rand = (int) (Math.random() * (maxSteps - minSteps + 1) + minSteps);
						bet = new Bet( "Featured Challenge",  rand + " steps total", rand, currDate, true, false, JDBCMain.myId);
						bet.start();
					}
				}


			}catch(SQLException e) 
			{ 
				e.printStackTrace();
				//e.getMessage();
			}


		}
	}

}

