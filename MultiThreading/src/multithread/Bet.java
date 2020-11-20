package multithread;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Bet extends Thread{
	private int id, ownerId, steps; 
	private String title, desc;
	private boolean active, goalReached;
	private String dtf;
	
	//what is date? is it an int or a string??
	

	public Bet(int id, int ownerId, String title, String desc, int steps, String dtf, boolean active, boolean goal )
	{ 
		this.id = id; 
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
		String sql = "INSERT INTO public.bet(bet_id, bet_owner_user_id, title, description, steps_wagered, date_created, active, achieved_goal)"
				+ "VALUES(?,?,?,?,?,?,?,?)";
		try(Connection insConn = DriverManager.getConnection(JDBCMain.url, JDBCMain.user, JDBCMain.pwd); 
				PreparedStatement prep = insConn.prepareStatement(sql);) { 
			
			int count  = 0;
			for (Bet bet : Checker.betArr) {
                prep.setInt(1, bet.getmyId());
                prep.setInt(2, bet.getOwnerId());
                prep.setString(3,  bet.getTitle());
                prep.setString(4,  bet.getDesc());
                prep.setInt(5,  bet.getSteps());
                prep.setString(6, bet.getDtf());
                prep.setBoolean(7, bet.isActive());
                prep.setBoolean(8, bet.isGoalReached());
                prep.addBatch();
                count++;
                // execute every 100 rows or less
                if (count == Checker.betArr.size()) {
                    prep.executeBatch();
                }
            }
			
		}catch(SQLException e)
		{ 
			e.printStackTrace();
		}
		
	}

	public int getmyId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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



