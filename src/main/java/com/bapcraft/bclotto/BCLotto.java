package com.bapcraft.bclotto;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.bapcraft.bclotto.commands.CommandAddTicket;
import com.bapcraft.bclotto.commands.CommandCheckPot;

public class BCLotto extends JavaPlugin {
	
	public static final String NAME_ADD_TICKET_COMMAND = "addbcticket";
	public static final String NAME_CHECK_POT_COMMAND = "buycraftlottocheck";
	
	public static final String CFG_VAR_TPP = "needed_tickets"; // Tickets Per Pot
	public static final String CFG_VAR_PRICE = "aesthetic_ticket_price";
	
	public static BCLotto instance = null;
	public static Logger log = null;
	
	public Drawing activeDrawing = null;
	public ArrayList<Drawing> previousDrawings = new ArrayList<Drawing>();
	public FileConfiguration config = null;
	public HistoryManager history;
	
	@Override
	public void onEnable() {
		
		// Inits
		config = this.getConfig();
		instance = this;
		history = new HistoryManager();
		history.init();
		log = this.getLogger();
		
		this.getCommand(NAME_ADD_TICKET_COMMAND).setExecutor(new CommandAddTicket());
		this.getCommand(NAME_CHECK_POT_COMMAND).setExecutor(new CommandCheckPot());
		
		this.doConfig();
		
	}
	
	@Override
	public void onDisable() {
		
		// De-inits
		history.deinit();
		history = null;
		instance = null;
		config = null;
		log = null;
		
		this.getCommand(NAME_ADD_TICKET_COMMAND).setExecutor(null);
		this.getCommand(NAME_CHECK_POT_COMMAND).setExecutor(null);
		
	}
	
	private void doConfig() {
		
		try {
			
			File dataFolder = this.getDataFolder();
			
			if (!dataFolder.exists()) dataFolder.mkdirs();
			
			File cfg = new File(dataFolder, "plugin.yml");
			
			if (cfg.exists()) {
				
				log.info("Config found, loading...");
				
			} else {
				
				log.info("No config fonund, creating...");
				
				config.addDefault(CFG_VAR_TPP, Drawing.NEEDED_TICKETS);
				config.addDefault(CFG_VAR_PRICE, Ticket.TICKET_PRICE.doubleValue());
				
			}
			
			Drawing.NEEDED_TICKETS = config.getInt(CFG_VAR_TPP, Drawing.NEEDED_TICKETS);
			Ticket.TICKET_PRICE = new BigDecimal(config.getDouble(CFG_VAR_PRICE, Ticket.TICKET_PRICE.doubleValue())); // Yucky.
			
			config.options().copyDefaults(true);
			this.saveConfig();
			
		} catch (Exception e) {
			e.printStackTrace();
			Bukkit.shutdown();
		}
		
	}
	
	/*
	private void loadConfig() {
		
		File cfg = new File(this.getDataFolder(), "config.yml");
		
		try {
			 
			config.load(cfg);
			
		} catch (FileNotFoundException fnfe) {
			
			cfg.getParentFile().mkdirs();
			
			try {
				cfg.createNewFile();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			
			config.set("tickets_needed", Drawing.NEEDED_TICKETS);
			config.set("asthetic_ticket_price", Ticket.TICKET_PRICE.doubleValue());
			
			try {
				config.save(cfg);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (InvalidConfigurationException ice) {
			ice.printStackTrace();
		}
		
		Drawing.NEEDED_TICKETS = config.getInt("tickets_needed", Drawing.NEEDED_TICKETS);
		Ticket.TICKET_PRICE = new BigDecimal(config.getDouble("asthetic_ticket_price", Ticket.TICKET_PRICE.doubleValue())); // Yucky.
		
		try {
			config.save(cfg);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
	*/
}
