package multithread;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;


public class CPUBets extends Thread{

	public static ArrayList<Bet> doneBets;
	@Override
	public void run() {
		while(true)
		{ 
			try { 
				Thread.sleep(900000);
				
				//call updateBets
				settleBets(Checker.betArr);
				distributePoints();
				
				
			}catch(Exception e) 
			{ 
				e.printStackTrace();
			}
			
		}
		
	}
	public void settleBets(ArrayList<Bet> bets) { 
		String SQL = "UPDATE public.bet "
                + "SET active = ? "
                + "WHERE bet_owner_user_id = 0";
		//public.bet(bet_id, bet_owner_user_id, title, description, steps_wagered, date_created, active, achieved_goal)"
		try (Connection conn = DriverManager.getConnection(JDBCMain.url, JDBCMain.user, JDBCMain.pwd); 
                PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			int count = 0;
			for(Bet b: bets) 
			{ 
				 pstmt.setBoolean(1, false);
				 doneBets.add(b);
				 pstmt.addBatch(); //check if you need this for Update
				 count++;
			}

			if(count == bets.size())
			{ 
				pstmt.executeUpdate();
			}
			

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
		
	}
	public void distributePoints() 
	{ 
		String SQL = "UPDATE user "
                + "SET points = ? "
				+ "FROM public.user user, public.user_bets userbets, public.bet bet "
                + "WHERE bet.bet_id = userbets.bet_id"
				+ "AND userbets.user_id = 0";
		//public.bet(bet_id, bet_owner_user_id, title, description, steps_wagered, date_created, active, achieved_goal)"
		try (Connection conn = DriverManager.getConnection(JDBCMain.url, JDBCMain.user, JDBCMain.pwd); 
                PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			int count = 0;
			for(Bet b: doneBets) 
			{ 
				pstmt.setInt(1, b.getSteps()*2);
				pstmt.addBatch();
				count++;
				
			}
			if(count == doneBets.size()) 
			{ 
				pstmt.executeBatch();
			}

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
		
	}

}
