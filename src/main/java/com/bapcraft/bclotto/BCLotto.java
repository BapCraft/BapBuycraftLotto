package com.bapcraft.bclotto;

import org.bukkit.plugin.java.JavaPlugin;

import com.bapcraft.bclotto.commands.CommandAddTicket;

public class BCLotto extends JavaPlugin {
	
	public static final String NAME_ADD_TICKET_COMMAND = "addbcticket";
	
	@Override
	public void onEnable() {
		
		this.getCommand(NAME_ADD_TICKET_COMMAND).setExecutor(new CommandAddTicket());
		
	}
	
	@Override
	public void onDisable() {
		
	}
	
}
