package com.bapcraft.bclotto;

import java.math.BigDecimal;
import java.util.UUID;

public class Ticket {

	public static final BigDecimal TICKET_PRICE = new BigDecimal(1); // USD.
	
	public final UUID player;
	
	public Ticket(UUID player) {
		
		this.player = player;
		
	}
	
}
