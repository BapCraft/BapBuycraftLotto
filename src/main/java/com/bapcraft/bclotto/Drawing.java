package com.bapcraft.bclotto;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.bapcraft.bclotto.prizes.Prize;
import com.bapcraft.bclotto.prizes.PrizeNotifyAdmin;

public class Drawing { // Noun.

	protected static int NEEDED_TICKETS = 10; // Changed in config. 
	
	protected long startTime = -1; // Soon after the previous.
	protected long drawTime = -1;
	public HashMap<Prize, Integer> prizes;
	public ArrayList<Ticket> pot;
	
	public volatile DrawingState state;
	protected UUID winner;
	
	public Drawing() {
		
		this.startTime = System.currentTimeMillis();
		
		this.prizes = new HashMap<Prize, Integer>();
		this.pot = new ArrayList<Ticket>();
		
		this.putPrize(new PrizeNotifyAdmin(), 100);
		
		this.state = DrawingState.READY;
		
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
		
		if (this.state == DrawingState.READY) {
			
			/*
			 * Probaby not necessary to use SecureRandom, but it can give a little
			 * bit of peace-of-mind.  And it looks better.
			 */
			
			SecureRandom sr = new SecureRandom();
			this.winner = this.pot.get(sr.nextInt(this.pot.size())).player;
			
			this.announceWinner();
			
			// Now makes sure that it doesn't do multiple confliting draws.
			this.state = DrawingState.COMPLETED;
			
		}
		
		return this.winner;
		
	}
	
	/**
	 * <summary>
	 * Finishes the drawing and does some cleanup.
	 * Does not setup the next one.
	 * </summary>
	 */
	public void finishDrawing() {
		
		Drawing draw = BCLotto.instance.activeDrawing;
		
		UUID winner = draw.getWinner();
		
		// Weighted random calculation. (From http://stackoverflow.com/questions/6737283)
		int totalWeight = 0;
		int prizeIndex = -1;
		Set<Prize> prizes = draw.prizes.keySet();
		
		for (Integer i : draw.prizes.values()) totalWeight += i;
		
		double random = Math.random() * totalWeight;
		for (int i = 0; i < prizes.size(); i++) {
		    
			random -= draw.prizes.get((Prize) prizes.toArray()[i]);
		    
			if (random <= 0.0d) {
		        prizeIndex = i;
		        break;
		    }
		    
		}
		
		((Prize) prizes.toArray()[prizeIndex]).onWin(draw, winner); // Anti-climatic.
		
	}
	
	/**
	 * <summary>
	 * Returns the UUID of the winner, if there is one.
	 * Returns <code>null</code> otherwise.
	 * </summary>
	 * 
	 * @return Possibly, the UUID of the winner.
	 */
	public UUID getWinner_PASSIVE() {
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
		return (this.state == DrawingState.COMPLETED) || (this.pot.size() >= NEEDED_TICKETS);
	}
	
	/**
	 * <summary>
	 * Internal method used to abort any calls that shouldn't be made after the winner is picked.
	 * </summary>
	 */
	private void checkOverAndAbort() {
		
		if (this.state == DrawingState.COMPLETED) throw new IllegalStateException("This lottery already occured!");
		if (this.state == DrawingState.CANCELLED) throw new IllegalStateException("This lottery has been cancelled!");
		
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
	
	public static enum DrawingState {
		
		READY("ready"),
		CANCELLED("cancelled"),
		COMPLETED("completed");
		
		public final String name;
		
		DrawingState(String name) {
			this.name = name;
		}
		
	}
	
}
