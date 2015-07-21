package com.bapcraft.bclotto.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAddTicket implements CommandExecutor {

	// `addbcticket <uuid>`
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player) {
			
			sender.sendMessage("This can only be run on the console!");
			return false;
			
		} else {
			
		}
		
		return true;
		
	}

}
