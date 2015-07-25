package com.bapcraft.bclotto.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bapcraft.bclotto.BCLotto;
import com.bapcraft.bclotto.Drawing;
import com.bapcraft.bclotto.Ticket;

public class CommandAddTicket implements CommandExecutor {

	/*
	 * `addbcticket <uuid>`
	 * Example UUID: 0e51946b-9bd3-4ded-a738-4217ec94b1d7
	 *     (^-- I generated when when checking the format.)
	 */
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player) {
			
			sender.sendMessage("This can only be run on the console!");
			return false;
			
		} else {
			
			if (BCLotto.instance.activeDrawing == null || BCLotto.instance.activeDrawing.state != Drawing.DrawingState.READY) {
				
				BCLotto.instance.history.setupNewDrawing(); // TODO Error detection.
				
			}
			
			Ticket added = null;
			
			// Let's make sure that the UUID makes sense.
			try {
				added = new Ticket(UUID.fromString(args[0]));
			} catch (IllegalArgumentException iae) {
				added = new Ticket(Bukkit.getPlayer(args[0]).getUniqueId());
			} catch (Exception e) {
				sender.sendMessage("You did something wrong here, methinks.");
			}
			
			// Now that we have the UUID in a format we can work with, we can move on...
			
			Drawing draw = BCLotto.instance.activeDrawing;
			
			try {
				draw.addTicket(added);
			} catch (IllegalStateException ise) {
				sender.sendMessage("New lottery hasn't started yet, please wait a second...");
			}
			
			if (draw.isWinnerNormallyAvailable()) {
				
				draw.finishDrawing();
				BCLotto.instance.history.setupNewDrawing(); // Make sure it won't stay at 100%.
				
			}
			
		}
		
		return true;
		
	}

}
