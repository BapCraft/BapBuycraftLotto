package com.bapcraft.bclotto;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.bapcraft.bclotto.prizes.Prize;
import com.bapcraft.bclotto.prizes.PrizeNotifyAdmin;

public class Drawing { // Noun.

	protected static int NEEDED_TICKETS = 10; // Changed in config. 
	
	protected long startTime; // Soon after the previous.
	protected long drawTime;
	public HashMap<Prize, Integer> prizes;
	public ArrayList<Ticket> pot;
	
	private volatile boolean over; // True if the thing is over.  Volatile because race condition is bad.
	private UUID winner;
	
	public Drawing() {
		
		this.startTime = System.currentTimeMillis();
		
		this.prizes = new HashMap<Prize, Integer>();
		this.pot = new ArrayList<Ticket>();
		
		this.putPrize(new PrizeNotifyAdmin(), 100);
		
	}
	
	/**
	 * <summary>
	 * Adds the prize to the pot if there is not one of its exact instance,
	 * replaces it if there is.
	 * </summary>
	 * 
	 * @param p The prize handler object.
	 * @param weight Should be fairly large.  100 is an average value.
	 */
	public void putPrize(Prize p, int weight) {
		
		this.checkOverAndAbort();
		this.prizes.put(p, weight);
		
	}
	
	/**
	 * <summary>
	 * Submits a ticket for the player specified in the ticket.
	 * Having multiple tickets for a given player is possible.
	 * </summary>
	 * 
	 * @param t The ticket.
	 */
	public void addTicket(Ticket t) {
		
		this.checkOverAndAbort();
		this.pot.add(t);
		
	}
	
	/**
	 * <summary>
	 * Gets the number of tickets a player has submitted.
	 * Will return 0 if none are found.
	 * </summary>
	 * 
	 * @param uuid The player's UUID.
	 * @return The tickets they have submitted.
	 */
	public int getNumbTicketsForPlayer(UUID uuid) {
		
		int cnt = 0;
		for (Ticket t : this.pot) if (t.player.equals(uuid)) cnt++; // <-- That's so beautiful.
		return cnt;
		
	}
	
	/**
	 * <summary>
	 * Gets the winner of the drawing.
	 * Selects one if the drawing has not occurred yet, even if
	 * there isn't enough tickets purchased. 
	 * </summary>
	 * 
	 * @return The winner.
	 */
	public UUID getWinner() {
		
		if (!over) {
			
			/*
			 * Probaby not necessary to use SecureRandom, but it can give a little
			 * bit of peace-of-mind.  And it looks better.
			 */
			
			SecureRandom sr = new SecureRandom();
			this.winner = this.pot.get(sr.nextInt(this.pot.size())).player;
			
			this.announceWinner();
			
			// Now makes sure that it doesn't do multiple confliting draws.
			over = true;
			
		}
		
		return this.winner;
		
	}
	
	/**
	 * <summary>
	 * Announces to the server if somebody has won.
	 * </summary>
	 */
	public void announceWinner() {
		Bukkit.broadcastMessage(
			"Player "
			+ Bukkit.getPlayer(this.winner).getDisplayName()
			+ " has won the lottery with "
			+ this.getNumbTicketsForPlayer(this.winner)
			+ " tickets!"
		);
	}
	
	/**
	 * <summary>
	 * Gets whether a winner can be selected as expected.
	 * Returns <code>true</code> if a winner has already been selected,
	 * or if there is enough tickets to select a winner.
	 * </summary>
	 * 
	 * @return  
	 */
	public boolean isWinnerNormallyAvailable() {
		return over || (this.pot.size() >= NEEDED_TICKETS);
	}
	
	/**
	 * <summary>
	 * Internal method used to abort any calls that shouldn't be made after the winner is picked.
	 * </summary>
	 */
	private void checkOverAndAbort() {
		if (over) throw new IllegalStateException("This lottery already occured!");
	}
	
	/**
	 * <summary>
	 * Gets the number of tickets needed to do a drawing.  Specified in the config.
	 * </summary>
	 * 
	 * @return The number of tickets.
	 */
	public static final int getTicketsNeeded() {
		return NEEDED_TICKETS;
	}
	
}
