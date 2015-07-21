package com.bapcraft.bclotto;

import org.bukkit.plugin.java.JavaPlugin;

import com.bapcraft.bclotto.commands.CommandAddTicket;
import com.bapcraft.bclotto.commands.CommandCheckPot;

public class BCLotto extends JavaPlugin {
	
	public static final String NAME_ADD_TICKET_COMMAND = "addbcticket";
	public static final String NAME_CHECK_POT_COMMAND = "buycraftlottocheck";
	
	public static BCLotto instance = null;
	
	public Drawing activeDrawing = null;
	
	@Override
	public void onEnable() {
		
		instance = this;
		
		this.getCommand(NAME_ADD_TICKET_COMMAND).setExecutor(new CommandAddTicket());
		this.getCommand(NAME_CHECK_POT_COMMAND).setExecutor(new CommandCheckPot());
		
	}
	
	@Override
	public void onDisable() {
		
		instance = null;
		
	}
	
}
