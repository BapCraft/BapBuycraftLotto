package com.bapcraft.bclotto;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.bapcraft.bclotto.commands.CommandAddTicket;
import com.bapcraft.bclotto.commands.CommandCheckPot;

public class BCLotto extends JavaPlugin {
	
	public static final String NAME_ADD_TICKET_COMMAND = "addbcticket";
	public static final String NAME_CHECK_POT_COMMAND = "buycraftlottocheck";
	
	public static BCLotto instance = null;
	
	public Drawing activeDrawing = null;
	public ArrayList<Drawing> previousDrawings = new ArrayList<Drawing>();
	public FileConfiguration config = null;
	public HistoryManager history;
	
	@Override
	public void onEnable() {
		
		// Inits
		instance = this;
		config = this.getConfig();
		history = new HistoryManager();
		history.init();
		
		this.getCommand(NAME_ADD_TICKET_COMMAND).setExecutor(new CommandAddTicket());
		this.getCommand(NAME_CHECK_POT_COMMAND).setExecutor(new CommandCheckPot());
		
		Drawing.NEEDED_TICKETS = config.getInt("tickets_needed", Drawing.NEEDED_TICKETS);
		Ticket.TICKET_PRICE = new BigDecimal(config.getDouble("asthetic_ticket_price", Ticket.TICKET_PRICE.doubleValue())); // Yucky.
		
	}
	
	@Override
	public void onDisable() {
		
		// De inits.
		instance = null;
		config = null;
		history.deinit();
		history = null;
		
		this.getCommand(NAME_ADD_TICKET_COMMAND).setExecutor(null);
		this.getCommand(NAME_CHECK_POT_COMMAND).setExecutor(null);
		
	}
	
}
