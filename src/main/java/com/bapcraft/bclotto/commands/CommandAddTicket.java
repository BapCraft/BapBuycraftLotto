package com.bapcraft.bclotto.commands;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.bapcraft.bclotto.BCLotto;
import com.bapcraft.bclotto.Drawing;
import com.bapcraft.bclotto.Ticket;
import com.evilmidget38.UUIDFetcher;

public class CommandAddTicket implements CommandExecutor {

	/*
	 * `addbcticket <uuid>`
	 * Example UUID: 0e51946b-9bd3-4ded-a738-4217ec94b1d7
	 *     (^-- I generated when when checking the format.)
	 */
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender.isOp() == false) {
			
			sender.sendMessage(ChatColor.RED + "You must be OP or Console to run this command!");
			return false;
			
		} else {
			
			if (BCLotto.instance.activeDrawing == null || BCLotto.instance.activeDrawing.state != Drawing.DrawingState.READY) {
				
				BCLotto.instance.history.setupNewDrawing(); // TODO Error detection.
				
			}
			
			Ticket added = null;
			
			if (args.length != 1) {
				return false;
			}
			
			// Let's make sure that the UUID makes sense.
			try {
				added = new Ticket(UUID.fromString(args[0]));
			} catch (IllegalArgumentException iae) {
				
				// It's probably a username.
				String username = args[0];
				
				UUIDFetcher fetch = new UUIDFetcher(Arrays.asList(new String[] {username}));
				UUID uuid = null;
				
				try {
					uuid = fetch.call().get(username);
				} catch (Exception e) {
					e.printStackTrace();
					sender.sendMessage("Something went wrong when looking up the username!  Try again.");
				}
				
				added = new Ticket(uuid); // Wow.
				
			} catch (Exception e) {
				sender.sendMessage("You did something wrong here, methinks.");
			}
			
			// Now that we have the UUID in a format we can work with, we can move on...
			
			Drawing draw = BCLotto.instance.activeDrawing;
			
			try {
				
				draw.addTicket(added);
				sender.sendMessage("Ticket added successfully!");
				
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
