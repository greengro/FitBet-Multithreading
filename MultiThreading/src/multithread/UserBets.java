package multithread;

public class UserBets {
	private int userBetId, userId, betId, amount; 
	private boolean betAgainst;
	public UserBets(int userBetId, int userId, int betId, int amount, boolean betAgainst)
	{ 
		this.userBetId = userBetId; 
		this.userId = userId; 
		this.betId = betId; 
		this.amount = amount; 
		this.betAgainst = betAgainst; 
		
	}
	public int getUserBetId() {
		return userBetId;
	}
	public void setUserBetId(int userBetId) {
		this.userBetId = userBetId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getBetId() {
		return betId;
	}
	public void setBetId(int betId) {
		this.betId = betId;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public boolean isBetAgainst() {
		return betAgainst;
	}
	public void setBetAgainst(boolean betAgainst) {
		this.betAgainst = betAgainst;
	}

}
