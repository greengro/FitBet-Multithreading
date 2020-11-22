package multithread;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Bet extends Thread{
	private int ownerId, steps; 
	private String title, desc;
	private boolean active, goalReached;
	private String dtf;
	public static Bet bet = Checker.bet;
	private int betID;
	
	//what is date? is it an int or a string??
	

	public Bet( String title, String desc, int steps, String dtf, boolean active, boolean goal, int ownerId )
	{  
		//this.id = id;
		this.ownerId = ownerId; 
		this.title = title; 
		this.desc = desc; 
		this.steps = steps; 
		this.dtf = dtf;
		this.active = active; 
		this.goalReached = goal;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO public.bets_bet(title, description, steps_wagered, date_created, active, achieved_goal, bet_owner_user_id_id)"
				+ "VALUES(?,?,?,?,?,?,?) RETURNING id";
		try(Connection insConn = DriverManager.getConnection(JDBCMain.url, JDBCMain.user, JDBCMain.pwd); 
				PreparedStatement prep = insConn.prepareStatement(sql);) { 
			
				ResultSet rs = prep.executeQuery();
				rs.next(); 
				betID = rs.getInt(1);

                prep.setString(1,  this.getTitle());
                prep.setString(2,  this.getDesc());
                prep.setInt(3,  this.getSteps());
                prep.setString(4, this.getDtf());
                prep.setBoolean(5, this.isActive());
                prep.setBoolean(6, this.isGoalReached());
                prep.setInt(7, this.getOwnerId());
                prep.addBatch();
                prep.executeBatch();
             
			
		}catch(SQLException e)
		{ 
			e.printStackTrace();
		}
		
		//sleep for 10-15min
		try {
			sleep((long)(2+Math.random()*1)*60*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//settle the bet
		settleBets();
		
		//etc 
		distributePoints();
	}
	
	public void settleBets() { 
		String SQL = "UPDATE public.bets_bet "
                + "SET active = ? "
                + "WHERE bet_owner_user_id_id = ?";
		

		//public.bet(bet_id, bet_owner_user_id, title, description, steps_wagered, date_created, active, achieved_goal)"
		try (Connection conn = DriverManager.getConnection(JDBCMain.url, JDBCMain.user, JDBCMain.pwd); 
                PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			int count = 0;
			
			pstmt.setBoolean(1, false);
			pstmt.setInt(2, this.getOwnerId());
			pstmt.executeUpdate();
			
			

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
		
	}
	public void distributePoints() 
	{ 
		String sql = "SELECT amount_bet, betting_against, user_id_id FROM public.bets_userbet WHERE bet_id_id=?";
		
		//public.bet(bet_id, bet_owner_user_id, title, description, steps_wagered, date_created, active, achieved_goal)"
		try (Connection conn = DriverManager.getConnection(JDBCMain.url, JDBCMain.user, JDBCMain.pwd); 
                PreparedStatement pstmt = conn.prepareStatement(sql);) {
			
			pstmt.setInt(1, betID);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{ 
				int amnt = rs.getInt("amount_bet");
				Boolean betAgainst = rs.getBoolean("betting_against"); 
				int usId = rs.getInt("user_id_id");
				
				String SQL =  "UPDATE public.users_profile SET points=? WHERE user=?";
				try(Connection conn2 = DriverManager.getConnection(JDBCMain.url, JDBCMain.user, JDBCMain.pwd); 
		                PreparedStatement pstmt2 = conn2.prepareStatement(sql); )
				{
					if(!betAgainst) 
					{ 
						pstmt2.setInt(1, amnt*2);
						pstmt2.setInt(2, usId);
					}
					
					pstmt2.executeUpdate();
					
				}
				catch(SQLException ex)
				{ 
					 System.out.println(ex.getMessage());
				}
				System.out.println("Amount " + amnt + " Bet against " + betAgainst + " User Id" + usId);
			}
			

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
		
	}

//	public int getid() 
//	{ 
//		return id;
//	}
//	public void setId(int id) { 
//		this.id = id;
//	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isGoalReached() {
		return goalReached;
	}

	public void setGoalReached(boolean goalReached) {
		this.goalReached = goalReached;
	}

	public String getDtf() {
		return dtf;
	}

	public void setDtf(String dtf) {
		this.dtf = dtf;
	}
	
}



