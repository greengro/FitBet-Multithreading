package multithread;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class JDBCMain {

	public static String url = "jdbc:postgresql://localhost:5555/fitbet?stringtype=unspecified";
	public static String user = "postgres";
	public static String pwd = "postgres";
	public static int myId;
	
	public static void main(String[] args) {

		System.out.println("Hello there jared");
		// TODO Auto-generated method stub


		String SQL = "INSERT INTO public.auth_user (email, last_name, first_name, password, is_superuser, username, is_staff, is_active, date_joined) VALUES "
				+ "('fakeEmail','Bets','House','password', false, 'HouseBets', false, true,'2020-11-12 00:15:39.013706+00') RETURNING id";
		boolean error = false;
		try(Connection betsConn = DriverManager.getConnection(JDBCMain.url, JDBCMain.user, JDBCMain.pwd); //insert a HouseUser for featured bets
				Statement prep = betsConn.createStatement();)
		{ 
			ResultSet rs = prep.executeQuery(SQL);
			rs.next();
			myId = rs.getInt("id");
			System.out.println("ID " + myId);

		}
		catch(Exception e) 
		{ 
			//e.printStackTrace();
			System.out.println("there was an issue with the house user");
			error = true;			
		}

		if(error) {
			System.out.println("Now looking for existing house user");
			SQL = "SELECT id FROM public.auth_user WHERE username='HouseBets'";
			try(Connection betsConn = DriverManager.getConnection(JDBCMain.url, JDBCMain.user, JDBCMain.pwd);
					Statement prep = betsConn.createStatement();)
			{ 
				ResultSet rs = prep.executeQuery(SQL);
				rs.next();
				myId = rs.getInt("id");
				System.out.println("ID " + myId);

			}
			catch(Exception e) 
			{ 
				//e.printStackTrace();
				System.out.println("there was an issue with the house user");

			}
		}
		
		//remove pre-existing house bets
		SQL = "DELETE FROM public.bets_userbet WHERE bet_id_id IN (SELECT id FROM public.bets_bet WHERE bet_owner_user_id_id="+Integer.toString(myId)+")";
		try(Connection betsConn = DriverManager.getConnection(JDBCMain.url, JDBCMain.user, JDBCMain.pwd);
				Statement prep = betsConn.createStatement();)
		{ 
			prep.executeQuery(SQL);
		}
		catch(Exception e) 
		{ 
			System.out.println("Exception removing all the pre-existing house userbets");

		}
		
		SQL = "DELETE FROM public.bets_bet WHERE bet_owner_user_id_id="+Integer.toString(myId);
		try(Connection betsConn = DriverManager.getConnection(JDBCMain.url, JDBCMain.user, JDBCMain.pwd);
				Statement prep = betsConn.createStatement();)
		{ 
			prep.executeQuery(SQL);
		}
		catch(Exception e) 
		{ 
			System.out.println("Exception removing all the pre-existing house bets");

		}

		Checker check = new Checker(); 

		check.run(); 
	}

}
