package com.bapcraft.bclotto.commands;

import java.math.BigDecimal;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bapcraft.bclotto.BCLotto;
import com.bapcraft.bclotto.Drawing;
import com.bapcraft.bclotto.Ticket;

public class CommandCheckPot implements CommandExecutor {

	// `buycraftlottocheck`
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player) {
			
			Drawing curDrawing = BCLotto.instance.activeDrawing;
			StringBuilder sb = new StringBuilder();
			
			// Get all the values needed.
			int numHas = curDrawing.pot.size();
			int numNeeded = Drawing.getTicketsNeeded();
			BigDecimal pricePerTicket = Ticket.getTicketPrice();
			float ratio = ((float) numHas) / ((float) numNeeded); // Casts to ensure FP division. 
			
			// Make the response.
			sb.append("The current Buycraft Lottery pot has ");
			sb.append(numHas);
			sb.append(" tickets of the needed ");
			sb.append(numNeeded);
			sb.append(", for a total of ");
			sb.append( pricePerTicket.multiply(new BigDecimal(numHas)) ); // Eww.
			sb.append(" USD. ( ");
			sb.append((int) ratio * 100F);
			sb.append("% )");
			
			// Send it.
			sender.sendMessage(sb.toString());
			
		} else {
			sender.sendMessage("You're not a player!");
		}
		
		return false;
		
	}
	
}
