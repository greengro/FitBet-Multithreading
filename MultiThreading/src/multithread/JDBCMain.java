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
	public static ArrayList<Bet> bets;
	public static ArrayList<User> users; 
	public static ArrayList<UserBets> userBets;
	public static int myId;
	public static void main(String[] args) {
		
		System.out.println("Hello there jared");
		// TODO Auto-generated method stub

		
		String SQL = "INSERT INTO public.auth_user (email, last_name, first_name, password, is_superuser, username, is_staff, is_active, date_joined) VALUES "
				+ "('fakeEmail','Bets','House','password', false, 'HouseBets', false, true,'2020-11-12 00:15:39.013706+00') RETURNING id";
		
		try(Connection betsConn = DriverManager.getConnection(JDBCMain.url, JDBCMain.user, JDBCMain.pwd);
				Statement prep = betsConn.createStatement();)
			{ 
				ResultSet rs = prep.executeQuery(SQL);
				myId = rs.getInt("id");
				System.out.println("ID " + myId);
				
			}
		catch(Exception e) 
		{ 
			e.printStackTrace();
		}
		getBets();
		getUserBets(); 
		UserBetThread usThread = new UserBetThread(); 
		usThread.insert(userBets);
		
		
		for(Bet b: bets) //inserts bets into database
		{ 
			b.start();
		}

		Checker check = new Checker(); 
	
		check.run(); 
	}
	public static void getBets() 
	{ 
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now(); 
		String currDate = dtf.format(now);
		Bet bet1 = new Bet( "Bet1", "1500 steps in a week", 1500, currDate, true, false, myId);
		Bet bet2 = new Bet( "Getting Active", "2500 steps in a week", 2500, currDate, true, false, myId);
		Bet bet3 = new Bet( "Pushing Myself", "50000 steps in a month", 50000, currDate, true, false, myId);
		Bet bet4 = new Bet( "Running 2 miles", "4500 steps in a day", 4500, currDate, true, false, myId);
		Bet bet5 = new Bet( "Bet2", "500 steps in a day", 500, currDate, true, false, myId);
		Bet bet6 = new Bet( "Bet3", "5000 steps in 2 days", 5000, currDate, true, false, myId);
		Bet bet7 = new Bet( "New Habits", "50000 steps in a week", 50000, currDate, true, false, myId);
		Bet bet8 = new Bet( "Running a mile", "3500 steps in a day", 3500, currDate, true, false, myId);
		Bet bet9 = new Bet( "Running a marathon", "35000 steps in a day", 3500, currDate, true, false, myId);
		Bet bet10 = new Bet("Walking in the morning", "3000 steps in a day", 3500, currDate, true, false, myId);
		Bet bet11 = new Bet( "Completing a half marathon", "17500 steps in a day", 17500, currDate, true, false, myId);
		
		bets.add(bet1);
		bets.add(bet2); 
		bets.add(bet3);
		bets.add(bet4);
		bets.add(bet5);
		bets.add(bet6); 
		bets.add(bet7);
		bets.add(bet8);
		bets.add(bet9); 
		bets.add(bet10);
		bets.add(bet11);
	}
//	public static void getUsers() 
//	{ 
//		
//		User us1 = new User(1, "User1", "GetFit", "user1@usc.edu", "fitness123", 500, 50);
//		User us2 = new User(2, "User2", "LovesFit", "user2@gmail.com", "fitness123", 5000, 500);
//		User us3 = new User(3, "User3", "Fitness", "user3@yahoo.com", "fitness123", 100, 0);
//		User us4 = new User(4, "User4", "Running", "user4@aol.com", "fitness123", 10000, 1000);
//		User us5 = new User(5, "Jared", "Stigter", "jstigter@usc.edu", "fitness123", 10000, 1000);
//		User us6 = new User(6, "Gabe", "Dalessandro", "gdalessa@usc.edu", "fitness123", 20000, 1000);
//		User us7 = new User(7, "Carson", "Greengrove", "cgreengrove@usc.edu", "fitness123", 50000, 1000);
//		User us8 = new User(8, "Elissa", "Perdue", "eperdue@usc.edu", "fitness123", 23000, 1000);
//		User us9 = new User(9, "TJ", "Ram", "tram@usc.edu", "fitness123", 32000, 1000);
//		User us10 = new User(10, "Christian", "Barrett", "cbarrett@usc.edu", "fitness123", 40000, 1000);
//		User us11 = new User(11, "Amy", "Cosgrove", "acosgrove@usc.edu", "fitness123", 40000, 1500);
//		
//		users.add(us1);
//		users.add(us2);
//		users.add(us3);
//		users.add(us4);
//		users.add(us5);
//		users.add(us6);
//		users.add(us7);
//		users.add(us8);
//		users.add(us9);
//		users.add(us10);
//		users.add(us11);
//		
//	}
	public static void getUserBets() 
	{ 
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now(); 
		String currDate = dtf.format(now);
		UserBets ub1 = new UserBets(1, 2, 1, 100, true);
		UserBets ub2 = new UserBets(2, 4, 1, 100, true);
		UserBets ub3 = new UserBets(3, 3, 4, 200, true);
		UserBets ub4 = new UserBets(4, 3, 1, 100, false);
		UserBets ub5 = new UserBets(5, 3, 3, 100, false);
		UserBets ub6 = new UserBets(6, 2, 9, 100, false);
		UserBets ub7 = new UserBets(7, 2, 10, 200, false);
		UserBets ub8 = new UserBets(8, 2, 11, 300, false);
		
		userBets.add(ub1);
		userBets.add(ub2);
		userBets.add(ub3);
		userBets.add(ub4); 
		userBets.add(ub5);
		userBets.add(ub6);
		userBets.add(ub7);
		userBets.add(ub8);
		

	}

}
