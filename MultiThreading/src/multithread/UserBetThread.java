package multithread;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;


public class UserBetThread {
	
//	 betting against
//	    -- Explaination: user2 is betting against user1's bet. TRUE says user2 does not think user1 will reach the goal
//	
//	INSERT INTO public.user_bets(user_bet_id, user_id, bet_id, amount_bet, betting_against) VALUES (1, 2, 1, 100, TRUE);
//
//	INSERT INTO public.user_bets(user_bet_id, user_id, bet_id, amount_bet, betting_against) VALUES (2, 4, 1, 100, TRUE);
//
//	INSERT INTO public.user_bets(user_bet_id, user_id, bet_id, amount_bet, betting_against) VALUES (3, 3, 4, 200, TRUE);
//
//
//	-- betting for
//	    -- Explaination: user3 is betting for user1's bet. FALSE says user3 thinks user1 will reach the goal
//	INSERT INTO public.user_bets(user_bet_id, user_id, bet_id, amount_bet, betting_against) VALUES (4, 3, 1, 100, FALSE);
//
//	INSERT INTO public.user_bets(user_bet_id, user_id, bet_id, amount_bet, betting_against) VALUES (5, 3, 3, 100, FALSE);
//
//	INSERT INTO public.user_bets(user_bet_id, user_id, bet_id, amount_bet, betting_against) VALUES (6, 2, 9, 100, FALSE);
//
//	INSERT INTO public.user_bets(user_bet_id, user_id, bet_id, amount_bet, betting_against) VALUES (7, 2, 10, 200, FALSE);
//
//	INSERT INTO public.user_bets(user_bet_id, user_id, bet_id, amount_bet, betting_against) VALUES (8, 2, 11, 300, FALSE);
//	
	public void insert(ArrayList<UserBets> userBets) { 
		
		
		String sql = "INSERT INTO public.bets_user_bet(id, amount_bet, betting_against, bet_id_id, user_id_id)"
				+ "VALUES(?,?,?,?,?)";
		try(Connection insConn = DriverManager.getConnection(JDBCMain.url, JDBCMain.user, JDBCMain.pwd); 
				PreparedStatement prep = insConn.prepareStatement(sql);) { 
			int count  = 1;
			for (UserBets ubet : userBets) {
               prep.setInt(1, ubet.getUserBetId());
               prep.setInt(2, ubet.getUserBetId());
               prep.setInt(3,  ubet.getBetId());
               prep.setInt(4,  ubet.getAmount());
               prep.setBoolean(5,  ubet.isBetAgainst());
               
               prep.addBatch();
               count++;
               // execute every 100 rows or less
               if (count == userBets.size()) {
                   prep.executeBatch();
               }
           }
			
			
		}catch(Exception e)
		{ 
			e.printStackTrace();
		}
	}


}
