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
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) 
		{
			try { 
				Thread.sleep(1000);
			} 
			catch(Exception e) 
			{ 
				e.printStackTrace();
			}
			String sql = "SELECT COUNT(bet.active)"
					+ "FROM public.bet"
					+ "WHERE bet.active = true";
			try(Connection betsConn = DriverManager.getConnection(JDBCMain.url, JDBCMain.user, JDBCMain.pwd);
				Statement statement = betsConn.createStatement()) 
			{ 
				ResultSet rs = statement.executeQuery(sql);
				rs.next();
			    int count = rs.getInt(1);
			    Bet CPUBet = null;
			    if(count < numBets)
			    { 
			    	int remainder = numBets - count;
			    	int betNum = 1000;
			    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		    		LocalDateTime now = LocalDateTime.now(); 
		    		String currDate = dtf.format(now);
			    	for(int i = 0; i < remainder; i++) 
			    	{ 
			    		int minSteps = 10000; 
			    		int maxSteps = 100000;
			    		int rand = (int) (Math.random() * (maxSteps - minSteps + 1) + minSteps);
			    		CPUBet = new Bet(betNum, 0, "Featured Bet",  rand + " steps total", rand, currDate, true, false);
			    		betArr.add(CPUBet);
			    		betIDs.add(betNum);
			    		betNum++;
			    		
			    	}
			    	CPUBet.start();
			    		
			    }
			    	
			
			}catch(SQLException e) 
			{ 
				e.getMessage();
			}
		

		}
	}

}

