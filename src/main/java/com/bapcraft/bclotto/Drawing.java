package com.bapcraft.bclotto;

import java.util.ArrayList;
import java.util.HashMap;

import com.bapcraft.bclotto.prizes.Prize;

public class Drawing { // Noun.

	protected static int NEEDED_TICKETS = 10; // Changed in config. 
	
	public HashMap<Prize, Integer> prizes;
	public ArrayList<Ticket> pot;
	
	public Drawing() {
		
		this.prizes = new HashMap<Prize, Integer>();
		this.pot = new ArrayList<Ticket>();
		
	}
	
	/**
	 * <summary>
	 * Adds the prize to the pot if there is not one of its exact instance,
	 * replaces it if there is.
	 * </summary>
	 * 
	 * @param p The prize itself.
	 * @param weight Should be fairly large.  100 is a common number.
	 */
	public void putPrize(Prize p, int weight) {
		prizes.put(p, weight);
	}
	
	public static final int getTicketsNeeded() {
		return NEEDED_TICKETS;
	}
	
}
