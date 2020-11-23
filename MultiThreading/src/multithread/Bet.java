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


	//creating a Bet thread


	public Bet( String title, String desc, int steps, String dtf, boolean active, boolean goal, int ownerId )
	{  
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
		System.out.println("Created a new bet, and I am now inserting it "+Integer.toString(steps));
		//inserting a bet into the database
		String sql = "INSERT INTO public.bets_bet(title, description, steps_wagered, date_created, active, achieved_goal, bet_owner_user_id_id)"
				+ "VALUES(?,?,?,?,?,?,?) RETURNING id";
		try(Connection insConn = DriverManager.getConnection(JDBCMain.url, JDBCMain.user, JDBCMain.pwd); 
				PreparedStatement prep = insConn.prepareStatement(sql);) { 
			prep.setString(1,  this.getTitle());
			prep.setString(2,  this.getDesc());
			prep.setInt(3,  this.getSteps());
			prep.setString(4, this.getDtf());
			prep.setBoolean(5, this.isActive());
			prep.setBoolean(6, this.isGoalReached());
			prep.setInt(7, this.getOwnerId());

			ResultSet rs = prep.executeQuery();
			rs.next(); 
			betID = rs.getInt(1);
			System.out.println("The bet id is "+Integer.toString(betID));
		}catch(SQLException e)
		{ 
			e.printStackTrace();
		}

		//sleep for a long period of time so that we give the user time to complete the bet in real time
		//we made it less for testing purposes so that we can easily show and resolve the bets
		try {
			sleep(60*1000+(long)(Math.random()*1)*60*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("We have finished the sleep portion///////////////////////////////////////////////");

		//settle the bet
		settleBets(); //the time frame for the bet is over so it is time to finish the bet
		distributePoints(); //determine if the user wins or loses and distribute the points
	}

	public void settleBets() { 
		System.out.println("Setting the bet");

		String SQL = "UPDATE public.bets_bet "
				+ "SET active = ?, achieved_goal = ?"
				+ "WHERE id = ?";
		
		try (Connection conn = DriverManager.getConnection(JDBCMain.url, JDBCMain.user, JDBCMain.pwd); 
				PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setBoolean(1, false);

			if(Math.random() > .5) {
				System.out.println("Setting it to be true");
				this.goalReached = true;
			}
			pstmt.setBoolean(2, this.isGoalReached());

			pstmt.setInt(3, this.betID);

			pstmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}
	public void distributePoints() 
	{ 
		System.out.println("We are distributing the points");

		String sql = "SELECT id, amount_bet, betting_against, user_id_id FROM public.bets_userbet WHERE bet_id_id=?";

		try (Connection conn = DriverManager.getConnection(JDBCMain.url, JDBCMain.user, JDBCMain.pwd); 
				PreparedStatement pstmt = conn.prepareStatement(sql);) {

			pstmt.setInt(1, betID);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{ 
				int current_id = rs.getInt("id");
				int amnt = rs.getInt("amount_bet");
				Boolean betAgainst = rs.getBoolean("betting_against"); 
				int usId = rs.getInt("user_id_id");
				int alreadyPoints = 0;

				//if you lose the bet, then we don't need to do anything else
				// T F --> get points
				// T T --> no points
				// F T --> get points
				// F F --> no points
				if(betAgainst == this.goalReached) {
					System.out.println("Losing userbet, continuing");
					continue;
				}
				
				String SQL =  "SELECT public.users_profile.points FROM public.users_profile WHERE user_id=?";
				System.out.println("The user_id is "+Integer.toString(usId));
				
				try(Connection conn2 = DriverManager.getConnection(JDBCMain.url, JDBCMain.user, JDBCMain.pwd); 
						PreparedStatement pstmt2 = conn2.prepareStatement(SQL); )
				{
					pstmt2.setInt(1, usId);
					ResultSet r = pstmt2.executeQuery();
					r.next(); 
					alreadyPoints = r.getInt(1);
					System.out.println("We got the points "+Integer.toString(alreadyPoints));
				}
				catch(SQLException ex)
				{ 
					ex.printStackTrace();
				}

				SQL =  "UPDATE public.users_profile SET points=? WHERE user_id=?";
				try(Connection conn2 = DriverManager.getConnection(JDBCMain.url, JDBCMain.user, JDBCMain.pwd); 
						PreparedStatement pstmt2 = conn2.prepareStatement(SQL); )
				{
					if(!betAgainst) 
					{ 
						pstmt2.setInt(1, alreadyPoints+amnt*2);
						pstmt2.setInt(2, usId);
					}

					pstmt2.executeUpdate();

				}
				catch(SQLException ex)
				{ 
					ex.printStackTrace();
				}
				
				SQL =  "UPDATE public.bets_userbet SET payout=? WHERE id=?";
				try(Connection conn2 = DriverManager.getConnection(JDBCMain.url, JDBCMain.user, JDBCMain.pwd); 
						PreparedStatement pstmt2 = conn2.prepareStatement(SQL); )
				{
					if(!betAgainst) 
					{ 
						pstmt2.setInt(1, amnt*2);
						pstmt2.setInt(2, current_id);
					}

					pstmt2.executeUpdate();

				}
				catch(SQLException ex)
				{ 
					ex.printStackTrace();
				}
				System.out.println("Amount " + amnt + " Bet against " + betAgainst + " User Id" + usId);
			}


		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}

	}

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



